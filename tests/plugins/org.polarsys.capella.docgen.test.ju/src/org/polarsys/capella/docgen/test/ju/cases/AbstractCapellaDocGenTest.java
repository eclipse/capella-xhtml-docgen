package org.polarsys.capella.docgen.test.ju.cases;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.egf.model.fcore.Activity;
import org.eclipse.egf.model.fcore.FactoryComponent;
import org.eclipse.emf.common.util.URI;
import org.eclipse.sirius.business.api.session.Session;
import org.polarsys.capella.docgen.test.ju.launch.XHTMLDocumentationGenerationLauncher;
import org.polarsys.capella.docgen.test.ju.reporter.AbstractCapellaDocGenHtmlReporter;
import org.polarsys.capella.docgen.test.ju.reporter.CapellaDocGenHtmlDomainElementReporter;
import org.polarsys.capella.docgen.test.ju.reporter.CapellaDocGenHtmlDomainElementResultsReporter;
import org.polarsys.capella.test.framework.api.BasicTestCase;
import org.polarsys.kitalpha.doc.gen.business.core.sirius.util.session.DiagramSessionHelper;
import org.polarsys.kitalpha.doc.gen.business.core.ui.helper.InvokeActivityHelper;

public abstract class AbstractCapellaDocGenTest extends BasicTestCase {

	protected static String OUTPUT_FOLDER_PATH = "doc/";

	private static final URI CAPELLA_TEST_LAUNCHER_URI = URI.createURI(
			"platform:/plugin/org.polarsys.capella.docgen.test.ju/egf/capellatestlauncher.fcore#_zup7kAkdEeCBJtEcjZDVOA",
			true);

	private static AbstractCapellaDocGenHtmlReporter reporter;
	private static Session sessionForTestModel;

	protected String getProjectName() {
		throw new NotImplementedException("Method \"getProjectName\" should be implemented for this test case.");
	}
	
	protected String getModelName() {
		throw new NotImplementedException("Method \"getModelName\" should be implemented for this test case.");
	}

	public String getPathPrefix() {
		return "/" + getProjectName() + "/" + getModelName() + ".";
	}
	
	public URI getSemanticModelURI() {
		return URI.createPlatformResourceURI(getPathPrefix() + "capella", true);
	}
	
	private URI getDesignModelURI() {
		return URI.createPlatformResourceURI(getPathPrefix() + "aird", true);
	}
	
	@Override
	public List<String> getRequiredTestModels() {
		return Arrays.asList(getModelName());
	}
	
	protected boolean regenerateTestResults() {
		return false;
	}
	
	private AbstractCapellaDocGenHtmlReporter setReporter() {
		if (regenerateTestResults()) {
			reporter = new CapellaDocGenHtmlDomainElementResultsReporter(this);
		} else {
			reporter = new CapellaDocGenHtmlDomainElementReporter(this);
		}
		return reporter;
	}
	
	protected static AbstractCapellaDocGenHtmlReporter getReporter() {
		return reporter;
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		getAirdFileForLoadedModel(getModelName());
		getCapellaFileForLoadedModel(getModelName());

		DiagramSessionHelper.setAirdUri(getDesignModelURI());
		DiagramSessionHelper.initSession();

		sessionForTestModel = getSessionForTestModel(getModelName());
		sessionForTestModel.open(new NullProgressMonitor());

		launchDocumentationGeneration();
	}

	private void launchDocumentationGeneration() {
		setReporter();
		// Launch docgen
		Activity melodyLauncher = InvokeActivityHelper.getActivity(CAPELLA_TEST_LAUNCHER_URI);
		if (melodyLauncher instanceof FactoryComponent) {
			FactoryComponent factoryComponent = (FactoryComponent) melodyLauncher;
			XHTMLDocumentationGenerationLauncher.gen(getProjectName(), OUTPUT_FOLDER_PATH, factoryComponent,
					getSemanticModelURI(), reporter);
		}
	}
}
