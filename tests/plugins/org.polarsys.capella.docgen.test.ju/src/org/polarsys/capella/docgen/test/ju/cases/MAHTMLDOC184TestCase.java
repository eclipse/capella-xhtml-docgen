package org.polarsys.capella.docgen.test.ju.cases;

import java.util.Map;

public class MAHTMLDOC184TestCase extends AbstractCapellaDocGenTest {

	private static String NAME = "MAHTMLDOC-184";
	
	@Override
	protected boolean regenerateTestResults() {
		return false;
	}
	
	@Override
	public String getProjectName() {
		return NAME;
	}
	
	@Override
	protected String getModelName() {
		return NAME;
	}
	
	@Override
	public void test() throws Exception {
		if (!regenerateTestResults()) {
			Map<String, String> expectedTestResults = getReporter().getExpectedTestResults();
			StringBuilder sb = new StringBuilder();
			getReporter().getTestResults().entrySet().stream().forEach(entry -> {
				String expectedResult = expectedTestResults.get(entry.getKey());
				assertNotNull("Object " + entry.getKey() + " does not exist", expectedResult);
				assertEquals("Object " + entry.getKey() + " does not match", expectedResult, entry.getValue());
				sb.append(entry.getKey());
				sb.append(System.lineSeparator());
			});
			System.out.println(sb.toString());
		}
	}
	
	@Override
	protected void tearDown() throws Exception {
		if (regenerateTestResults()) {
			getReporter().afterTestExecution();
		}
		super.tearDown();
	}
}
