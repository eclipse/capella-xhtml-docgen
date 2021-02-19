package org.polarsys.capella.docgen.test.ju.reporter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.egf.model.pattern.PatternContext;
import org.eclipse.egf.pattern.execution.InternalPatternContext;
import org.polarsys.capella.core.data.capellacore.CapellaElement;
import org.polarsys.capella.docgen.test.ju.cases.AbstractCapellaDocGenTest;
import org.polarsys.kitalpha.doc.gen.business.core.reporter.DocGenHtmlReporter;
import org.polarsys.kitalpha.doc.gen.business.core.util.DocGenHtmlConstants;

public class CapellaDocGenHtmlDomainElementReporter extends DocGenHtmlReporter {
	
	public static final String TEST_RESULTS_FILE_EXTENSION = "test_results.ser";

	protected AbstractCapellaDocGenTest launchedTest;
	public static Map<String, String> testResults = new HashMap<String, String>();

	public CapellaDocGenHtmlDomainElementReporter(AbstractCapellaDocGenTest test) {
		this.launchedTest = test;
		CapellaDocGenHtmlDomainElementReporter.testResults.clear();
	}
	
	public Map<String, String> getTestResults() {
		return testResults;
	}

	public CapellaDocGenTestResult getTestData(String output, PatternContext context,
			Map<String, Object> parameterValues, String fileName) {
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

				testResult = new CapellaDocGenTestResult(id, type, patternClass, fileName, htmlContent);
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

	@Override
	public void loopFinished(String output, String outputWithCallBack, PatternContext context,
			Map<String, Object> parameterValues) {
		String fileName = (String) context.getValue(DocGenHtmlConstants.FILE_NAME);
		CapellaDocGenTestResult testData = getTestData(output, context, parameterValues, fileName);

		if (testData != null) {
			getTestResults().put(testData.getClassifierId(), testData.getGeneratedHTMLContent());
		}
	}

	protected File getSerializedTestResultsFile(boolean newFile) throws IOException {
		IFile outCapellaModel = ResourcesPlugin.getWorkspace().getRoot()
				.getFile(new Path(launchedTest.getSemanticModelURI().toPlatformString(true)));
		File testResultsFile = null;
		if (outCapellaModel.exists()) {
			IPath outFilePath = outCapellaModel.getRawLocation().removeFileExtension()
					.addFileExtension(TEST_RESULTS_FILE_EXTENSION + (newFile ? ".new" : ""));
			testResultsFile = outFilePath.toFile();
			if (!testResultsFile.exists()) {
				testResultsFile.createNewFile();
			} else if (newFile) {
				testResultsFile.delete();
				testResultsFile.createNewFile();
			}
		}
		return testResultsFile;
	}
	
	public void afterTestExecution() {
		FileOutputStream fileOut;
		try {
			File outFile = getSerializedTestResultsFile(true);
			fileOut = new FileOutputStream(outFile);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(getTestResults());
			out.close();
			fileOut.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
