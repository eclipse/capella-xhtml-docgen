package org.polarsys.capella.docgen.test.ju.suites;

import java.util.ArrayList;
import java.util.List;

import org.polarsys.capella.docgen.test.ju.cases.MAHTMLDOC184TestCase;
import org.polarsys.capella.test.framework.api.BasicTestArtefact;
import org.polarsys.capella.test.framework.api.BasicTestSuite;

import junit.framework.Test;

public class CapellaDocgenTestSuite extends BasicTestSuite {
	
	public static Test suite() {
		return new CapellaDocgenTestSuite();
	}
	
	@Override
	protected List<BasicTestArtefact> getTests() {
		List<BasicTestArtefact> tests = new ArrayList<BasicTestArtefact>();
		tests.add(new MAHTMLDOC184TestCase());
		return tests;
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
}
