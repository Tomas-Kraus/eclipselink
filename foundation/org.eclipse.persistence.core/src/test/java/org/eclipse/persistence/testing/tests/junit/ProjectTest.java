/*
 * Copyright (c) 2024 Oracle and/or its affiliates. All rights reserved.
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
//      07/17/2024-5.0 Tomas Kraus
package org.eclipse.persistence.testing.tests.junit;

import org.eclipse.persistence.expressions.Expression;
import org.eclipse.persistence.internal.jpa.jpql.HermesParser;
import org.eclipse.persistence.internal.sessions.AbstractSession;
import org.eclipse.persistence.queries.DatabaseQuery;
import org.eclipse.persistence.queries.JPAQueryBuilder;
import org.eclipse.persistence.sessions.Project;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ProjectTest {

    // Verify that default JPAQueryBuilder returned by Project instance is HermesParser
    @Test
    public void testDefaultQueryBuilder() {
        Project project = new Project();
        JPAQueryBuilder builder = project.getQueryBuilder();
        assertTrue("builder is not an instance of HermesParser class",
                   HermesParser.class.isAssignableFrom(builder.getClass()));
    }

    // Verify that JPAQueryBuilder returned by Project instance matches CustomQueryBuilder
    // after CustomQueryBuilder::new is set as JPAQueryBuilder instance factory
    @Test
    public void testCustomQueryBuilder() {

        class CustomQueryBuilder implements JPAQueryBuilder {
            @Override
            public void setValidationLevel(String level) {
                throw new UnsupportedOperationException();
            }
            @Override
            public DatabaseQuery buildQuery(CharSequence jpqlQuery, AbstractSession session) {
                throw new UnsupportedOperationException();
            }
            @Override
            public Expression buildSelectionCriteria(String entityName, String selectionCriteria, AbstractSession session) {
                throw new UnsupportedOperationException();
            }
            @Override
            public void populateQuery(CharSequence jpqlQuery, DatabaseQuery query, AbstractSession session) {
                throw new UnsupportedOperationException();
            }
        }

        Project project = new Project();
        project.setQueryBuilderSupplier(CustomQueryBuilder::new);
        JPAQueryBuilder builder = project.getQueryBuilder();
        assertTrue("builder is not an instance of CustomQueryBuilder class",
                   CustomQueryBuilder.class.isAssignableFrom(builder.getClass()));
    }

}
