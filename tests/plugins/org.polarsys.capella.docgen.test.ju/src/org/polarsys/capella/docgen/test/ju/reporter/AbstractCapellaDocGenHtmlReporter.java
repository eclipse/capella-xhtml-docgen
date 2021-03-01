package org.polarsys.capella.docgen.test.ju.reporter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.NotImplementedException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.egf.model.pattern.PatternContext;
import org.eclipse.egf.pattern.execution.InternalPatternContext;
import org.polarsys.capella.core.data.capellacore.CapellaElement;
import org.polarsys.capella.docgen.test.ju.cases.AbstractCapellaDocGenTest;
import org.polarsys.kitalpha.doc.gen.business.core.reporter.DocGenHtmlReporter;

public abstract class AbstractCapellaDocGenHtmlReporter extends DocGenHtmlReporter {

	protected final String TEST_RESULTS_FILE_EXTENSION = "test_results.ser";

	protected AbstractCapellaDocGenTest launchedTest;
	Map<String, String> testResults = new HashMap<String, String>();

	public Map<String, String> getTestResults() {
		return testResults;
	}

	public CapellaDocGenTestResult getTestData(String output, PatternContext context,
			Map<String, Object> parameterValues) {
		Object element = null;
		CapellaDocGenTestResult testResult = null;
		if (parameterValues.containsKey("element")) {
			element = parameterValues.get("element");
		} else if (parameterValues.containsKey("parameter")) {
			element = parameterValues.get("parameter");
		}
		if (element instanceof CapellaElement) {
			CapellaElement cElement = (CapellaElement) element;

			String id = cElement.getId();
			String type = cElement.getClass().getName();
			String htmlContent = output;

			if (context instanceof InternalPatternContext) {
				InternalPatternContext ipContext = (InternalPatternContext) context;
				String patternClass = ipContext.getNode().getPatternClass();

				testResult = new CapellaDocGenTestResult(id, type, patternClass, htmlContent);
			}
		}
		return testResult;
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> getExpectedTestResults() throws IOException, ClassNotFoundException {
		File serializedTestResultsFile = getSerializedTestResultsFile(false);
		Map<String, String> result = null;
		if (serializedTestResultsFile != null) {
			FileInputStream fileIn = new FileInputStream(serializedTestResultsFile);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			Object res = in.readObject();
			if (res instanceof Map<?, ?>) {
				Map<?, ?> mapRes = (Map<?, ?>) res;
				boolean typeMatch = mapRes.values().stream().anyMatch(elem -> elem instanceof String);
				if (typeMatch) {
					result = (Map<String, String>) res;
				}
			}
			in.close();
			fileIn.close();
		}
		return result;
	}

	public AbstractCapellaDocGenHtmlReporter(AbstractCapellaDocGenTest test) {
		this.launchedTest = test;
	}

	@Override
	public void loopFinished(String output, String outputWithCallBack, PatternContext context,
			Map<String, Object> parameterValues) {
		CapellaDocGenTestResult testData = getTestData(output, context, parameterValues);

		if (testData != null) {
			getTestResults().put(testData.getClassifierId(), testData.getGeneratedHTMLContent());
		}
	}

	public void afterTestExecution() {
		throw new NotImplementedException("This method call should be surcharged");
	}

	protected File getSerializedTestResultsFile(boolean overwrite) throws IOException {
		IFile outCapellaModel = ResourcesPlugin.getWorkspace().getRoot()
				.getFile(new Path(launchedTest.getSemanticModelURI().toPlatformString(true)));
		File testResultsFile = null;
		if (outCapellaModel.exists()) {
			IPath outFilePath = outCapellaModel.getRawLocation().removeFileExtension()
					.addFileExtension(TEST_RESULTS_FILE_EXTENSION);
			testResultsFile = outFilePath.toFile();
			if (!testResultsFile.exists()) {
				testResultsFile.createNewFile();
			} else if (overwrite) {
				testResultsFile.delete();
				testResultsFile.createNewFile();
			}
		}
		return testResultsFile;
	}
}
