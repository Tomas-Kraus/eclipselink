/*
 * Copyright (c) 2011, 2024 Oracle and/or its affiliates. All rights reserved.
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
// mmacivor - April 20/2010 - 2.1 - Initial implementation
package org.eclipse.persistence.testing.jaxb.externalizedmetadata.mappings.xmlinversereference;

import jakarta.xml.bind.annotation.XmlID;

public class PhoneNumber {

    @XmlID
    public String id;

    public String number;

    public Employee emp;

    public boolean equals(Object obj) {
        PhoneNumber phone = null;
        try {
            phone = (PhoneNumber)obj;
        } catch(ClassCastException ex) {
            return false;
        }
        return id.equals(phone.id) && number.equals(phone.number) && (emp == phone.emp || (emp != null && phone.emp != null));
    }
}
