/*
 * Copyright (c) 2011, 2022 Oracle and/or its affiliates. All rights reserved.
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
//     tware - testing for query downcasting
package org.eclipse.persistence.testing.models.jpa.inheritance;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="CMP3_JALOPY")
@DiscriminatorValue("J")
public class Jalopy extends Car {

    protected int percentRust = 0;

    public int getPercentRust() {
        return percentRust;
    }

    public void setPercentRust(int percentRust) {
        this.percentRust = percentRust;
    }


}
