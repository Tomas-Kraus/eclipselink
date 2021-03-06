/*
 * Copyright (c) 1998, 2020 Oracle and/or its affiliates. All rights reserved.
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
//    Denise Smith - June 2012
package org.eclipse.persistence.testing.jaxb.inheritance.simple;

import jakarta.xml.bind.annotation.XmlValue;

public class Simple {
    @XmlValue
    String foo;

    public boolean equals(Object compareObject){
        if(compareObject instanceof Simple){
            if(foo == null){
                return ((Simple)compareObject).foo == null;
            }
            return foo.equals(((Simple)compareObject).foo);
        }
        return false;
    }
}
