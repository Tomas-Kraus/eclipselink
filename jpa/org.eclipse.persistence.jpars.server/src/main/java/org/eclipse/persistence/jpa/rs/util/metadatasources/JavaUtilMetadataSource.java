/*
 * Copyright (c) 2011, 2021 Oracle and/or its affiliates. All rights reserved.
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
//      gonural - initial
package org.eclipse.persistence.jpa.rs.util.metadatasources;

import java.util.Map;

import org.eclipse.persistence.jaxb.metadata.MetadataSource;
import org.eclipse.persistence.jaxb.xmlmodel.XmlBindings;

/**
 * Makes java.util package classes available to JPA-RS JAXB context.
 *
 * @author gonural
 *
 */
public class JavaUtilMetadataSource  implements MetadataSource {
    private XmlBindings xmlBindings;

    public JavaUtilMetadataSource() {
        xmlBindings = new XmlBindings();
        xmlBindings.setPackageName("java.util");
    }

    @Override
    public XmlBindings getXmlBindings(Map<String, ?> properties, ClassLoader classLoader) {
        return this.xmlBindings;
    }
}
