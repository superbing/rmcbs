package com.bfd;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * RmcsWebApplicationTest
 * 
 * @author 姓名 工号
 * @version [版本号, 2018年7月31日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class RmcsWebApplicationTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public RmcsWebApplicationTest(String testName) {
        super(testName);
    }
    
    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(RmcsWebApplicationTest.class);
    }
    
    /**
     * Rigourous Test :-)
     */
    public void testApp() {
        assertTrue(true);
    }
}
