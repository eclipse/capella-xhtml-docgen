package org.polarsys.capella.docgen.test.ju.cases;

import java.util.Collection;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.Path;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.polarsys.capella.docgen.test.ju.reporter.CapellaDocGenHtmlDomainElementReporter;
import org.polarsys.capella.docgen.test.ju.util.CapellaDocGenTestUtil;

@RunWith(Parameterized.class)
public class IFETestCase extends AbstractCapellaDocGenTest {

	private static String NAME = "IFE";

	@Override
	public String getProjectName() {
		return NAME;
	}

	@Override
	protected String getModelName() {
		return NAME;
	}

	@Parameters(name = "IFE - {0}")
	public static Collection<Object[]> data() {
		Path path = new Path(
				"/model/" + NAME + "/" + NAME + "." + CapellaDocGenHtmlDomainElementReporter.TEST_RESULTS_FILE_EXTENSION);
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
		
		// Generated content must not be null
		assertNotNull("Expected object " + elementID + " cannot be found", generatedContent);
		
		// Format generated content
		generatedContent = CapellaDocGenTestUtil.formatDocGenOutput(generatedContent);
		
		// Generated content must match
		assertEquals("Object " + elementID + " does not match", expectedContent, generatedContent);
	}

	@Override
	protected void tearDown() throws Exception {
//		super.tearDown();
	}
}
