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
//     13/01/2022-4.0.0 Tomas Kraus
//       - 1391: JSON support in JPA
package org.eclipse.persistence.internal.databaseaccess;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

import org.eclipse.persistence.internal.expressions.ParameterExpression;
import org.eclipse.persistence.internal.sessions.AbstractRecord;

/**
 * Database platform JSON extension.
 * Provides an interface to implement JSON specific features.
 */
public interface DatabaseJsonPlatform {
    /**
     * Database platform JSON extension factory.
     * Provides an interface to implement JSON specific features.
     */
    interface Factory {
        /**
         * Creates an instance of Database platform JSON extension.
         *
         * @param platformClass source database platform class
         * @return Database platform JSON extension matching provided platform
         */
        DatabaseJsonPlatform create(final Class<? extends DatabasePlatform> platformClass);
    }

    /**
     * Retrieve JSON data from JDBC {@code ResultSet}.
     *
     * @param resultSet source JDBC {@code ResultSet}
     * @param columnNumber index of column in JDBC {@code ResultSet}
     * @return JSON data from JDBC {@code ResultSet} as {@code String} to be parsed by {@code JsonTypeConverter}
     * @throws SQLException if data could not be retrieved
     */
    default Object getJsonDataFromResultSet(ResultSet resultSet, int columnNumber) throws SQLException {
        return resultSet.getString(columnNumber);
    }

    default  void updateFieldTypes(Hashtable<Class<?>, FieldTypeDefinition> fieldTypeMapping) {}

    default boolean isJsonType(final Type type) {
        return false;
    }

    default String customParameterMarker() {
        return "?";
    }

}
