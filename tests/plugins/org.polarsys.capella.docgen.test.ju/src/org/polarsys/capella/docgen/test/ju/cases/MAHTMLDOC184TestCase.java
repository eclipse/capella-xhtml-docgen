package org.polarsys.capella.docgen.test.ju.cases;

import java.util.Collection;

import org.eclipse.core.runtime.Path;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.polarsys.capella.docgen.test.ju.reporter.AbstractCapellaDocGenHtmlReporter;

@RunWith(Parameterized.class)
public class MAHTMLDOC184TestCase extends AbstractCapellaDocGenTest {

	private static String NAME = "MAHTMLDOC-184";

	@Override
	public String getProjectName() {
		return NAME;
	}

	@Override
	protected String getModelName() {
		return NAME;
	}

	@Parameters
	public static Collection<Object[]> data() {
		Path path = new Path(
				"/model/" + NAME + "/" + NAME + "." + AbstractCapellaDocGenHtmlReporter.TEST_RESULTS_FILE_EXTENSION);
		return getTestParameters(path);
	}

	@Parameter
	public static String elementID;

	@Parameter(1)
	public static String expectedContent;

	@Override
	@Test
	public void test() throws Exception {
		String generatedContent = getReporter().getTestResults().get(elementID);
		assertNotNull("Expected object " + elementID + " cannot be found", generatedContent);
		assertEquals("Object " + elementID + " does not match", expectedContent, generatedContent);
	}

	@Override
	@After
	public void tearDown() throws Exception {
		getReporter().afterTestExecution();
//		super.tearDown();
	}
}
