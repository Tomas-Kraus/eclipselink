/*
 * Copyright (c) 1998, 2021 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0,
 * or the Eclipse Distribution License v. 1.0 which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: EPL-2.0 OR BSD-3-Clause
 */

// Contributors:
//     Oracle - initial API and implementation from Oracle TopLink
package org.eclipse.persistence.testing.sdo.model.changesummary;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ChangeSummaryXSDWithCSOnChildTestSuite {
    public ChangeSummaryXSDWithCSOnChildTestSuite() {
    }

    /**
     *
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());

    }

    /**
    *  Inherited suite mthod for generating all test cases.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite("ChangeSummary XSD Child Test Cases");
        suite.addTest(new TestSuite(ChangeSummaryXSDWithCSonChildDeleteComplexSingleBelowRootTest.class));
        suite.addTest(new TestSuite(ChangeSummaryXSDWithCSonChildDetachComplexSingleBelowRootTest.class));
        suite.addTest(new TestSuite(ChangeSummaryXSDWithCSonChildUnsetComplexSingleBelowRootTest.class));
        suite.addTest(new TestSuite(ChangeSummaryXSDWithCSonChildDeleteChainToComplexSingleBelowRootTest.class));
        suite.addTest(new TestSuite(ChangeSummaryXSDWithCSonChildDeleteChainToComplexSingleAtRootTest.class));
        suite.addTestSuite(ChangeSummaryOnChildGetRootObjectTestCases.class);

        return suite;
    }
}
