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
//     06/14/2010-2.2 Karen Moore
//       - 264417:  Table generation is incorrect for JoinTables in AssociationOverrides
package org.eclipse.persistence.testing.models.jpa.ddlgeneration;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

import java.util.ArrayList;
import java.util.Collection;

import static jakarta.persistence.FetchType.EAGER;

@Entity(name="DDL_PHONE")
public class PhoneNumber {
    @Id
    @Column(name="NUMB")
    @GeneratedValue
    public int number;

    @ManyToMany(mappedBy="contactInfo.phoneNumbers", fetch=EAGER)
    public Collection<Employee> employees;

    public PhoneNumber() {
        employees = new ArrayList<>();
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
    }

    public Collection<Employee> getEmployees() {
        return employees;
    }

    public int getNumber() {
        return number;
    }

    public void setEmployees(Collection<Employee> employees) {
        this.employees = employees;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
