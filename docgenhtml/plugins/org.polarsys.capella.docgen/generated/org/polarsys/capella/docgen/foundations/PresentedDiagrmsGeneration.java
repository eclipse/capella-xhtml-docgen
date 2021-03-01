//Generated with EGF 1.6.2.202001031546
package org.polarsys.capella.docgen.foundations;

import org.eclipse.egf.common.helper.*;
import java.util.*;
import org.eclipse.emf.ecore.*;
import org.eclipse.egf.model.pattern.*;
import org.eclipse.egf.pattern.execution.*;
import org.eclipse.egf.pattern.query.*;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.polarsys.capella.core.data.capellacore.CapellaElement;
import org.polarsys.capella.core.data.capellacore.NamedElement;
import org.polarsys.capella.docgen.diagram.CapellaHelper;
import org.polarsys.capella.docgen.util.DocGenHtmlCapellaControl;
import org.polarsys.capella.docgen.util.DocGenHtmlCapellaUtil;
import org.polarsys.capella.docgen.util.CapellaDiagramHelper;
import org.polarsys.capella.docgen.util.CapellaServices;
import org.polarsys.capella.docgen.util.CapellaLabelProviderHelper;
import org.polarsys.kitalpha.doc.gen.business.core.preference.helper.DocgenDiagramPreferencesHelper;

public class PresentedDiagrmsGeneration {
	protected static String nl;

	public static synchronized PresentedDiagrmsGeneration create(String lineSeparator) {
		nl = lineSeparator;
		PresentedDiagrmsGeneration result = new PresentedDiagrmsGeneration();
		nl = null;
		return result;
	}

	public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
	protected final String TEXT_1 = "<h2>Diagrams displaying \"";
	protected final String TEXT_2 = "\"</h2>" + NL + "<ul>";
	protected final String TEXT_3 = NL + "<li>";
	protected final String TEXT_4 = NL;
	protected final String TEXT_5 = " ";
	protected final String TEXT_6 = NL + "</li>";
	protected final String TEXT_7 = NL + "</ul>";

	public PresentedDiagrmsGeneration() {
		//Here is the constructor
		StringBuffer stringBuffer = new StringBuffer();

		// add initialisation of the pattern variables (declaration has been already done).

	}

	public String generate(Object argument) throws Exception {
		final StringBuffer stringBuffer = new StringBuffer();

		InternalPatternContext ctx = (InternalPatternContext) argument;
		Map<String, String> queryCtx = null;
		IQuery.ParameterDescription paramDesc = null;
		Node.Container currentNode = ctx.getNode();

		List<Object> elementList = null;
		//this pattern can only be called by another (i.e. it's not an entry point in execution)

		for (Object elementParameter : elementList) {

			this.element = (org.polarsys.capella.core.data.capellacore.CapellaElement) elementParameter;

			if (preCondition(ctx)) {
				ctx.setNode(new Node.Container(currentNode, getClass()));
				orchestration(ctx);
			}

		}
		ctx.setNode(currentNode);
		if (ctx.useReporter()) {
			ctx.getReporter().executionFinished(OutputManager.computeExecutionOutput(ctx), ctx);
		}

		stringBuffer.append(TEXT_4);
		stringBuffer.append(TEXT_4);
		return stringBuffer.toString();
	}

	public String orchestration(PatternContext ctx) throws Exception {
		InternalPatternContext ictx = (InternalPatternContext) ctx;

		method_setContext(new StringBuffer(), ictx);

		method_body(new StringBuffer(), ictx);

		if (ictx.useReporter()) {
			Map<String, Object> parameterValues = new HashMap<String, Object>();
			parameterValues.put("element", this.element);
			String outputWithCallBack = OutputManager.computeLoopOutput(ictx);
			String loop = OutputManager.computeLoopOutputWithoutCallback(ictx);
			ictx.getReporter().loopFinished(loop, outputWithCallBack, ictx, parameterValues);
		}
		return null;
	}

	protected org.polarsys.kitalpha.doc.gen.business.core.util.IFileNameService fileNameService = null;

	public void set_fileNameService(org.polarsys.kitalpha.doc.gen.business.core.util.IFileNameService object) {
		this.fileNameService = object;
	}

	protected org.polarsys.kitalpha.doc.gen.business.core.util.IDiagramHelper helper = null;

	public void set_helper(org.polarsys.kitalpha.doc.gen.business.core.util.IDiagramHelper object) {
		this.helper = object;
	}

	protected org.polarsys.capella.core.data.capellacore.CapellaElement element = null;

	public void set_element(org.polarsys.capella.core.data.capellacore.CapellaElement object) {
		this.element = object;
	}

	public Map<String, Object> getParameters() {
		final Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("element", this.element);
		return parameters;
	}

	protected void method_setContext(final StringBuffer stringBuffer, final PatternContext ctx) throws Exception {

		fileNameService = DocGenHtmlCapellaUtil.SERVICE;
		helper = new CapellaDiagramHelper();

		InternalPatternContext ictx = (InternalPatternContext) ctx;
		new Node.DataLeaf(ictx.getNode(), getClass(), "setContext", stringBuffer.toString());
	}

	protected void method_body(final StringBuffer stringBuffer, final PatternContext ctx) throws Exception {

		List<DSemanticDiagram> diagramSet = CapellaHelper.getDiagramContainingObject(element);
		if (diagramSet.size() >= 1) {
			stringBuffer.append(TEXT_1);
			stringBuffer.append(CapellaLabelProviderHelper.getText(element));
			stringBuffer.append(TEXT_2);
			for (DSemanticDiagram diagram : diagramSet) {
				EObject eObject = diagram.getTarget();
				if (eObject == null) {
					//The diagram could not be exported
					continue;
				}
				String generatedFolder = fileNameService.getFileName(eObject);
				stringBuffer.append(TEXT_3);
				if (eObject instanceof NamedElement
						&& DocGenHtmlCapellaControl.isPageCandidate((CapellaElement) eObject)) {
					stringBuffer.append(TEXT_4);
					stringBuffer.append(CapellaServices.getHyperlinkFromElement(diagram));
					stringBuffer.append(TEXT_5);
				} else {
					stringBuffer.append(TEXT_4);
					stringBuffer.append(CapellaLabelProviderHelper.getText(eObject));
				}
				stringBuffer.append(TEXT_6);

			}
			stringBuffer.append(TEXT_7);
		}
		InternalPatternContext ictx = (InternalPatternContext) ctx;
		new Node.DataLeaf(ictx.getNode(), getClass(), "body", stringBuffer.toString());
	}

	public boolean preCondition(PatternContext ctx) throws Exception {
		return DocgenDiagramPreferencesHelper.getExportDiagram();
	}
}