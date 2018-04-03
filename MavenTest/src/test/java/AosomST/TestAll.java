package AosomST;

import junit.framework.Test;
import junit.framework.TestSuite;

public class TestAll {
	public static Test suite(){
		
		TestSuite suite = new TestSuite("All tests");
		
		//suite.addTestSuite(SmokeTest.class);
		
		suite.addTest(new SmokeTest("testCALoginPayPal"));
		
		return suite;
	}

}
