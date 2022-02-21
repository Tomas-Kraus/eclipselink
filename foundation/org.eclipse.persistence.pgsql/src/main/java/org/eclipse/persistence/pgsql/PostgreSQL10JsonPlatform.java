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
//     13/01/2022-4.0.0 Tomas Kraus - 1391: JSON support in JPA
package org.eclipse.persistence.pgsql;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.json.JsonValue;
import jakarta.persistence.PersistenceException;

import org.eclipse.persistence.internal.localization.ExceptionLocalization;
import org.eclipse.persistence.json.JsonPlatform;
import org.postgresql.util.PGobject;

public class PostgreSQL10JsonPlatform extends JsonPlatform {

    // Default Postgres 10 type for JSON data.
    private static final String JSON_DEFAULT_TYPE = "jsonb";

    /**
     * INTERNAL:
     * Convert JSON value field to JDBC statement type.
     * Postgres JSON storage type is {@code JSONB} and target Java type is {@code PGobject}.
     *
     * @param <T> classification type
     * @param jsonValue source JSON value field
     * @return converted JDBC statement type
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T convertJsonValueToDataValue(final JsonValue jsonValue) throws PersistenceException {
        if (jsonValue == null) {
            return null;
        }
        try {
            final PGobject pgObject = new PGobject();
            pgObject.setType(JSON_DEFAULT_TYPE);
            pgObject.setValue(super.convertJsonValueToDataValue(jsonValue));
            return (T) pgObject;
        } catch (SQLException e) {
            throw new PersistenceException(ExceptionLocalization.buildMessage("json_pgsql_jsonvalue_to_database_type"), e);
        }
    }

    // Default resultSet.getString(columnNumber); call works too.
    /**
     * Retrieve JSON data from JDBC {@code ResultSet}.
     * JSON data retrieved from Postgres JDBC {@code ResultSet} are returned as {@code PGobject} instance.
     * It must be converted to {@code String} first to be accepted by common {@code JsonTypeConverter}.
     *
     * @param resultSet source JDBC {@code ResultSet}
     * @param columnNumber index of column in JDBC {@code ResultSet}
     * @return JSON data from JDBC {@code ResultSet} as {@code String} to be parsed by common {@code JsonTypeConverter}
     * @throws SQLException if data could not be retrieved
     */
    @Override
    public Object getJsonDataFromResultSet(final ResultSet resultSet, final int columnNumber) throws SQLException {
        // ResultSet returns an instance of PGobject.
        final Object rawData = resultSet.getObject(columnNumber);
        // Following code is called through reflection to avoid PGobject dependency.
        if (rawData instanceof PGobject) {
            return ((PGobject) rawData).getValue();
        } else if (rawData instanceof String) {
            return rawData;
        }
        throw new PersistenceException(ExceptionLocalization.buildMessage("json_pgsql_unknown_type"));
    }

}
