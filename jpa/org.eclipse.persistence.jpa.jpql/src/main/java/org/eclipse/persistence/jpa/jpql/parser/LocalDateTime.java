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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.function.Function;
import java.util.function.Supplier;

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
 */
public final class LocalDateTime extends DateTime {

    /**
     * Type of local date/time expression.
     */
    private enum Type {
        /** LOCAL DATE */
        DATE,
        /** LOCAL TIME */
        TIME,
        /** LOCAL DATETIME */
        DATETIME;

        /**
         * Convert local date/time text identifier to {@link LocalDateTime.Type}.
         *
         * @param name local date/time text identifier
         * @return {@link LocalDateTime.Type} matching local date/time text identifier
         *         or {@code null} when identifier is unknown.
         */
        private static final Type getType(final String name) {
            switch(name) {
                case LOCAL_DATE:
                case LOCAL_DATE_ALT:
                    return DATE;
                case LOCAL_TIME:
                case LOCAL_TIME_ALT:
                    return TIME;
                case LOCAL_DATETIME:
                case LOCAL_DATETIME_ALT:
                    return DATETIME;
                default:
                    return null;
            }
        }
    }

    /**
     * Creates a new <code>DateTime</code>.
     *
     * @param parent The parent of this expression
     */
    public LocalDateTime(AbstractExpression parent) {
        super(parent);
    }

    /**
     * Execute supplier depending on local date/time text identifier in {@link LocalDateTime} expression.
     *
     * @param dateAction function executed for {@code LOCAL DATE}
     * @param timeAction function executed for {@code LOCAL TIME}
     * @param dateTimeAction function executed for {@code LOCAL DATETIME}
     */
    public <R> R getValueByType(Supplier<R> dateAction, Supplier<R> timeAction, Supplier<R> dateTimeAction) {
        switch(Type.getType(getText())) {
            case DATE:
                return dateAction.get();
            case TIME:
                return timeAction.get();
            case DATETIME:
                return dateTimeAction.get();
            default:
                throw new IllegalStateException("Unknown value of " + getText() + " LocalDateTime expression");
        }
    }

    /**
     * Execute action depending on local date/time text identifier in {@link LocalDateTime} expression.
     *
     * @param dateAction function executed for {@code LOCAL DATE}
     * @param timeAction function executed for {@code LOCAL TIME}
     * @param dateTimeAction function executed for {@code LOCAL DATETIME}
     */
    public void runByType(Runnable dateAction, Runnable timeAction, Runnable dateTimeAction) {
        switch(Type.getType(getText())) {
            case DATE:
                dateAction.run();
                return;
            case TIME:
                timeAction.run();
                return;
            case DATETIME:
                dateTimeAction.run();
                return;
            default:
                throw new IllegalStateException("Unknown value of " + getText() + " LocalDateTime expression");
        }
    }

    @Override
    public void accept(ExpressionVisitor visitor) {
        visitor.visit(this);
    }

}
