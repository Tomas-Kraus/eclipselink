/*
 * Copyright (c) 2022 Oracle and/or its affiliates. All rights reserved.
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
//     02/01/2022: Tomas Kraus
//       - Issue 1442: Implement New JPA API 3.1.0 Features
package org.eclipse.persistence.jpa.jpql.parser;

import org.eclipse.persistence.jpa.jpql.WordParser;

public final class LocalDateTimeFactory extends ExpressionFactory {

    /**
     * The unique identifier of this {@link DateTimeFactory}.
     */
    public static final String ID = "functions_returning_local_datetime";

    /**
     * Creates a new <code>LocalDateTimeFactory</code>.
     */
    public LocalDateTimeFactory() {
        super(ID,
                Expression.LOCAL_DATE,
//                Expression.LOCAL_DATE_ALT,
                Expression.LOCAL_TIME,
//                Expression.LOCAL_TIME_ALT,
                Expression.LOCAL_DATETIME
//                Expression.LOCAL_DATETIME_ALT
        );
    }

    @Override
    protected AbstractExpression buildExpression(AbstractExpression parent,
                                                 WordParser wordParser,
                                                 String word,
                                                 JPQLQueryBNF queryBNF,
                                                 AbstractExpression expression,
                                                 boolean tolerant) {

        expression = new LocalDateTime(parent);
        expression.parse(wordParser, tolerant);
        return expression;
    }

}
