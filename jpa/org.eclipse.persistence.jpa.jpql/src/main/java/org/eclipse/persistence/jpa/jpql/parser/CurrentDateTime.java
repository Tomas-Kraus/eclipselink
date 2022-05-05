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

import java.util.List;
import org.eclipse.persistence.jpa.jpql.WordParser;

/**
 * This {@link Expression} represents a date or time. It supports the following identifiers:
 * <p>
 * <b>CURRENT_DATE</b>: This function returns the value of current date on the database server.
 * <p>
 * <b>CURRENT_TIME</b>: This function returns the value of current time on the database server.
 * <p>
 * <b>CURRENT_TIMESTAMP</b>: This function returns the value of current timestamp on the database
 * server.
 *
 * <div><b>BNF:</b> <code>functions_returning_datetime ::= CURRENT_DATE | CURRENT_TIME | CURRENT_TIMESTAMP</code></div>
 * <p>
 * The JDBC escape syntax may be used for the specification of date, time, and timestamp literals.
 *
 * <div><b>BNF:</b> <code>expression ::= {d 'yyyy-mm-dd'} | {t 'hh:mm:ss'} | {ts 'yyyy-mm-dd hh:mm:ss.f...'}</code></div>
 *
 * @version 2.4
 * @since 2.3
 * @author Pascal Filion
 */
public final class CurrentDateTime extends DateTime {

    /**
     * Creates a new <code>DateTime</code>.
     *
     * @param parent The parent of this expression
     */
    public CurrentDateTime(AbstractExpression parent) {
        super(parent);
    }

    /**
     * Determines whether this {@link CurrentDateTime} represents the JPQL identifier
     * {@link Expression#CURRENT_DATE CURRENT_DATE}.
     *
     * @return <code>true</code> if this {@link Expression} represents
     * {@link Expression#CURRENT_DATE CURRENT_DATE}; <code>false</code> otherwise
     */
    public boolean isCurrentDate() {
        return getText() == CURRENT_DATE;
    }

    /**
     * Determines whether this {@link CurrentDateTime} represents the JPQL identifier
     * {@link Expression#CURRENT_TIME CURRENT_TIME}.
     *
     * @return <code>true</code> if this {@link Expression} represents
     * {@link Expression#CURRENT_TIME CURRENT_TIME}; <code>false</code> otherwise
     */
    public boolean isCurrentTime() {
        return getText() == CURRENT_TIME;
    }

    /**
     * Determines whether this {@link CurrentDateTime} represents the JPQL identifier
     * {@link Expression#CURRENT_TIMESTAMP CURRENT_TIMESTAMP}.
     *
     * @return <code>true</code> if this {@link Expression} represents
     * {@link Expression#CURRENT_TIMESTAMP CURRENT_TIMESTAMP}; <code>false</code>
     * otherwise
     */
    public boolean isCurrentTimestamp() {
        return getText() == CURRENT_TIMESTAMP;
    }

    @Override
    public void accept(ExpressionVisitor visitor) {
        visitor.visit(this);
    }

}
