package org.polarsys.capella.docgen.test.ju.reporter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.polarsys.capella.docgen.test.ju.cases.AbstractCapellaDocGenTest;

/**
 * This reporter is used to save expected test results. Not intended to be used
 * for test execution.
 * 
 * @author Arnaud Dieumegard
 *
 */
public class CapellaDocGenHtmlDomainElementResultsReporter extends AbstractCapellaDocGenHtmlReporter {
	
	public CapellaDocGenHtmlDomainElementResultsReporter(AbstractCapellaDocGenTest launchedTest) {
		super(launchedTest);
	}

	@Override
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
