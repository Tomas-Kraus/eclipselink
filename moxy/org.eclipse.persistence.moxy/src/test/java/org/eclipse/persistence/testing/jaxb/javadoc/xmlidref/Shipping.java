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
//     Praba Vijayaratnam - 2.3 - initial implementation
package org.eclipse.persistence.testing.jaxb.javadoc.xmlidref;

import jakarta.xml.bind.annotation.XmlIDREF;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="shipping")
public class Shipping {

    @XmlIDREF
    public Customer customer;

    public void setCustomer(Customer customer){
        this.customer = customer;
    }


    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Shipping s)) {
            return false;
        }
        if (this.customer == null) {
            return s.customer == null;
        }
        if (s.customer == null) {
            return false;
        }
        return true;
    }
}
