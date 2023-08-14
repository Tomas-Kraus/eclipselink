/*
 * Copyright (c) 1998, 2023 Oracle and/or its affiliates. All rights reserved.
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
package org.eclipse.persistence.testing.oxm.mappings.directcollection.identifiedbyname.withoutgroupingelement;

import org.eclipse.persistence.oxm.mappings.XMLCompositeDirectCollectionMapping;
import org.eclipse.persistence.sessions.Project;
import org.eclipse.persistence.testing.oxm.mappings.XMLMappingTestCases;
import org.eclipse.persistence.testing.oxm.mappings.directcollection.Employee;

import java.util.Vector;

public class DirectCollectionWithoutGroupingElementSingleNodeTestCases extends XMLMappingTestCases {
    private final static String XML_RESOURCE = "org/eclipse/persistence/testing/oxm/mappings/directcollection/identifiedbyname/withoutgroupingelement/DirectCollectionWithoutGroupingElementSingleNode.xml";
    private final static int CONTROL_ID = 123;
    private final static String CONTROL_RESPONSIBILITY1 = "type";
    private final static String CONTROL_RESPONSIBILITY2 = "write";
    private final static String CONTROL_RESPONSIBILITY3 = "read";

    public DirectCollectionWithoutGroupingElementSingleNodeTestCases(String name) throws Exception {
        super(name);
        setControlDocument(XML_RESOURCE);
        Project p = new DirectCollectionWithoutGroupingElementIdentifiedByNameProject();
        XMLCompositeDirectCollectionMapping mapping = (XMLCompositeDirectCollectionMapping)p.getDescriptor(Employee.class).getMappingForAttributeName("responsibilities");
        mapping.setUsesSingleNode(true);
        setProject(p);
    }

    @Override
    protected Object getControlObject() {
        Vector responsibilities = new Vector();
        responsibilities.addElement(CONTROL_RESPONSIBILITY1);
        responsibilities.addElement(CONTROL_RESPONSIBILITY2);
        responsibilities.addElement(CONTROL_RESPONSIBILITY3);

        Employee employee = new Employee();
        employee.setID(CONTROL_ID);
        employee.setResponsibilities(responsibilities);
        return employee;
    }
}
