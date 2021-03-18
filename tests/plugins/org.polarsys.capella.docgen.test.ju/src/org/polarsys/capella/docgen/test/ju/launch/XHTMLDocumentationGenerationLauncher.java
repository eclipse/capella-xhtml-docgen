package org.polarsys.capella.docgen.test.ju.launch;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.egf.model.domain.Domain;
import org.eclipse.egf.model.domain.DomainViewpoint;
import org.eclipse.egf.model.domain.EMFDomain;
import org.eclipse.egf.model.fcore.Contract;
import org.eclipse.egf.model.fcore.FactoryComponent;
import org.eclipse.egf.model.fcore.Viewpoint;
import org.eclipse.egf.model.pattern.PatternExecutionReporter;
import org.eclipse.egf.model.pattern.TypePatternExecutionReporter;
import org.eclipse.egf.model.types.Type;
import org.eclipse.egf.model.types.TypeString;
import org.eclipse.egf.pattern.EGFPatternPlugin;
import org.eclipse.egf.producer.EGFProducerPlugin;
import org.eclipse.egf.producer.manager.ActivityManagerProducer;
import org.eclipse.egf.producer.manager.IActivityManager;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;
import org.polarsys.kitalpha.doc.gen.business.core.util.MonitorServices;

public class XHTMLDocumentationGenerationLauncher {

	private XHTMLDocumentationGenerationLauncher() {
	}

	public static void gen(String projectName, String outputFolder, FactoryComponent melodyLauncher, URI uri,
			PatternExecutionReporter reporter) {
		IPath path = new Path(projectName + "/" + outputFolder);
		path = path.append("output");
		IFolder folder = ResourcesPlugin.getWorkspace().getRoot().getFolder(path);
		if (!folder.exists()) {
			try {
				folder.create(true, true, null);
			} catch (CoreException e) {
				EGFPatternPlugin.getDefault().logError(e);
			}
		}

		if (melodyLauncher instanceof FactoryComponent) {
			final FactoryComponent factoryComponent = (FactoryComponent) melodyLauncher;

			/** Setting the values of the mandatory contracts **/
			setContract(factoryComponent, "projectName", projectName, true);
			setContract(factoryComponent, "outputFolder", outputFolder + "/output", true);
			setDomain(factoryComponent, uri);
			setContract(factoryComponent, "pattern.execution.reporter", reporter, false);

			/** Setting the values of the optional contracts **/
			setContract(factoryComponent, "copyright", "", false);
			setContract(factoryComponent, "logo.alt", "", false);
			setContract(factoryComponent, "logo.path", "", false);

			try {
				@SuppressWarnings("rawtypes")
				ActivityManagerProducer producer = EGFProducerPlugin.getActivityManagerProducer(factoryComponent);
				@SuppressWarnings({ "unchecked", "rawtypes" })
				final IActivityManager activityManager = producer.createActivityManager(factoryComponent);
				activityManager.initializeContext();
				IWorkbench wb = PlatformUI.getWorkbench();
				final IProgressService ps = wb.getProgressService();

				Display.getDefault().syncExec(new Runnable() {
					@Override
					public void run() {
						try {
							ps.busyCursorWhile(new IRunnableWithProgress() {
								@Override
								public void run(IProgressMonitor monitor) {
									try {
										MonitorServices.initMonitor(monitor);
										MonitorServices.init(0);
										activityManager.invoke(monitor);
										activityManager.dispose();
										MonitorServices.dispose();
									} catch (Exception e) {
										EGFPatternPlugin.getDefault().logError(e);
									}
								}
							});
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				});

			} catch (Exception e) {
				EGFPatternPlugin.getDefault().logError(e);
			}
		}
	}

	private static void setDomain(FactoryComponent factoryComponent, URI uri) {
		Viewpoint viewpoint = factoryComponent.getViewpointContainer().getViewpoint(DomainViewpoint.class);
		if (viewpoint instanceof DomainViewpoint) {
			DomainViewpoint domainViewpoint = (DomainViewpoint) viewpoint;
			Domain domain = domainViewpoint.getDomains().get(0);
			if (domain instanceof EMFDomain) {
				EMFDomain domainURI = (EMFDomain) domain;
				domainURI.setUri(uri);
			}
		}
	}

	private static void setContract(FactoryComponent factoryComponent, String contractName,
			PatternExecutionReporter reporter, boolean mandatory) {
		Contract invokedContract = factoryComponent.getContract(contractName);
		if (invokedContract != null) {
			Type type = invokedContract.getType();
			if (type instanceof TypePatternExecutionReporter) {

				TypePatternExecutionReporter typeReporter = (TypePatternExecutionReporter) type;
				typeReporter.setInstance(reporter);
			}
		} else {
			if (mandatory) {
				throw new IllegalArgumentException("Can't find the mandatory contract " + contractName
						+ " in the factory component " + factoryComponent.getName());
			}
		}
	}

	private static void setContract(FactoryComponent factoryComponent, String contractName, String value,
			boolean mandatory) {
		Contract invokedContract = factoryComponent.getContract(contractName);
		if (invokedContract != null) {
			Type type = invokedContract.getType();
			if (type instanceof TypeString) {
				TypeString typeString = (TypeString) type;
				typeString.setValue(value);
			}
		} else {
			if (mandatory) {
				throw new IllegalArgumentException("Can't find the mandatory contract " + contractName
						+ " in the factory component " + factoryComponent.getName());
			}
		}
	}
}
