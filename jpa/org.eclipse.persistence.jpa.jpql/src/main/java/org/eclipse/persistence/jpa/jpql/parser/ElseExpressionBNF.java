/*
 * Copyright (c) 2006, 2021 Oracle and/or its affiliates. All rights reserved.
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
//     Oracle - initial API and implementation
//
package org.eclipse.persistence.jpa.jpql.parser;

/**
 * The query BNF for a else expression specified in the case expression.
 *
 * @version 2.4
 * @since 2.3
 * @author Pascal Filion
 */
@SuppressWarnings("nls")
public final class ElseExpressionBNF extends JPQLQueryBNF {

    /**
     * The unique identifier of this BNF rule.
     */
    public static final String ID = "else_expression";

    /**
     * Creates a new <code>ElseExpressionBNF</code>.
     */
    public ElseExpressionBNF() {
        super(ID);
    }

    @Override
    protected void initialize() {
        super.initialize();
        setHandleAggregate(true);
        setFallbackBNFId(ScalarExpressionBNF.ID);
        registerChild(ScalarExpressionBNF.ID);
    }
}
