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
package org.eclipse.persistence.testing.tests.xdb;

import org.eclipse.persistence.expressions.*;
import org.eclipse.persistence.platform.database.oracle.Oracle9Platform;
import org.eclipse.persistence.testing.framework.*;

public class ExistsNodeWithJoinTest extends TestCase {
    Employee_XML employee = null;

    public ExistsNodeWithJoinTest() {
        setDescription("Tests the use of the existsNode() function as well as testing use of XMLType in a join");
    }

    @Override
    public void setup() {
        if (!(getSession().getPlatform() instanceof Oracle9Platform)) {
            throw new TestWarningException("This test is intended for the Oracle9Platform");
        }
    }

    @Override
    public void reset() {
    }

    @Override
    public void test() {
        employee = (Employee_XML)getSession().readObject(Employee_XML.class, new ExpressionBuilder().get("manager").get("resume").existsNode("//education/degree").greaterThan(0));
    }

    @Override
    public void verify() {
        if ((employee == null) || !employee.firstName.equals("Frank")) {
            throw new TestErrorException("Wrong Employee returned:" + employee);
        }
    }
}
