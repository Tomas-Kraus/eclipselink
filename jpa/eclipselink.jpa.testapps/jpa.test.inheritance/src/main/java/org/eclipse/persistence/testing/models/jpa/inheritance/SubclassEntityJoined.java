/*
 * Copyright (c) 1998, 2022 Oracle and/or its affiliates. All rights reserved.
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
//     dminsky - initial implementation
package org.eclipse.persistence.testing.models.jpa.inheritance;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="JPA_JOINED_SUBCLASS")
@DiscriminatorValue(value="SUB")
public class SubclassEntityJoined extends MappedSuperclassJoined {

    @Column(name="SUBCLASS_ATTRIBUTE")
    protected String subclassAttribute;

    public SubclassEntityJoined() {
        super();
    }

    public String getSubclassAttribute() {
        return subclassAttribute;
    }

    public void setSubclassAttribute(String subclassAttribute) {
        this.subclassAttribute = subclassAttribute;
    }

}
