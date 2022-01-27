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
package org.eclipse.persistence.platform.database.oracle;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Map;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import jakarta.json.JsonWriter;
import jakarta.persistence.PersistenceException;

import oracle.jdbc.OracleType;
import oracle.sql.json.OracleJsonFactory;
import oracle.sql.json.OracleJsonValue;
import org.eclipse.persistence.internal.databaseaccess.FieldTypeDefinition;
import org.eclipse.persistence.internal.sessions.AbstractSession;

/**
 * <p><b>Purpose:</b>
 * Supports certain new Oracle 21c data types, and usage of certain Oracle JDBC specific APIs.
 * <p> Supports Oracle JSON data type.
 * <p> Supports Oracle OracleJsonValue derived Java types.
 */
public class Oracle21Platform extends Oracle19Platform {

    // Oracle JSON factory.
    private final OracleJsonFactory factory;

    /**
     * Creates an instance of Oracle 21c database platform.
     */
    public Oracle21Platform() {
        super();
        this.factory = new OracleJsonFactory();
    }

    /**
     * Build the mapping of database types to class types for the schema framework.
     *
     * @return database types to class types {@code Map} for the schema framework
     */
    @Override
    protected Map<String, Class<?>> buildClassTypes() {
        final Map<String, Class<?>> classTypeMapping = super.buildClassTypes();
        classTypeMapping.put("JSON", JsonValue.class);
        return classTypeMapping;
    }

    /**
     * Build the mapping of class types to database types for the schema framework.
     *
     * @return {@code Hashtable} mapping class types to database types for the schema framework
     */
    @Override
    protected Hashtable<Class<?>, FieldTypeDefinition> buildFieldTypes() {
        final Hashtable<Class<?>, FieldTypeDefinition>fieldTypeMapping = super.buildFieldTypes();
        // Mapping for JSON type is set in JsonTypeConverter#initialize.
        fieldTypeMapping.put(JsonObject.class, new FieldTypeDefinition("JSON"));
        fieldTypeMapping.put(JsonArray.class, new FieldTypeDefinition("JSON"));
        fieldTypeMapping.put(JsonValue.class, new FieldTypeDefinition("JSON"));
        return fieldTypeMapping;
    }

    /**
     * INTERNAL
     * Set the parameter in the JDBC statement at the given index.
     * This support a wide range of different parameter types, and is heavily optimized for common types.
     * Handles Postgres specific PGobject instances.
     *
     * @param parameter the parameter to set
     * @param statement target {@code PreparedStatement} instance
     * @param index index of the parameter in the statement
     * @param session current database session
     */
    @Override
    public void setParameterValueInDatabaseCall(
            final Object parameter, final PreparedStatement statement,
            final int index, final AbstractSession session
    ) throws SQLException {
        // Instance check is called through reflection to avoid PGobject dependency
        if (parameter instanceof OracleJsonValue) {
            statement.setObject(index, parameter, OracleType.JSON);
        } else {
            super.setParameterValueInDatabaseCall(parameter, statement, index, session);
        }
    }

    /**
     * INTERNAL
     * Set the parameter in the JDBC statement at the given index.
     * This support a wide range of different parameter types, and is heavily optimized for common types.
     * Handles Postgres specific PGobject instances.
     *
     * @param parameter the parameter to set
     * @param statement target {@code CallableStatement} instance
     * @param name name of the parameter in the statement
     * @param session current database session
     */
    @Override
    public void setParameterValueInDatabaseCall(
            final Object parameter, final CallableStatement statement,
            final String name, final AbstractSession session
    ) throws SQLException {
        // Instance check is called through reflection to avoid PGobject dependency
        if (parameter instanceof OracleJsonValue) {
            statement.setObject(name, parameter, OracleType.JSON);
        } else {
            super.setParameterValueInDatabaseCall(parameter, statement, name, session);
        }
    }

    // Postgres specific JSON types support:
    // Stores JsonValue instances as JSONB.
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
        final StringWriter sw = new StringWriter(128);
        try (final JsonWriter jw = Json.createWriter(sw)) {
            jw.write(jsonValue);
        }
        try (final Reader jr = new StringReader(sw.toString())) {
            return (T) factory.createJsonTextValue(jr);
        } catch (IOException e) {
            throw new PersistenceException("Could not convert JsonValue to OracleJsonValue", e);
        }
    }

    /**
     * Convert JDBC {@code ResultSet} type to JSON value field.
     *
     * @param jdbcValue source classification type value from JDBC
     * @return converted JSON field value
     */
    public JsonValue convertDataValueToJsonValue(Object jdbcValue) {
        if (jdbcValue == null) {
            return null;
        }
        if (jdbcValue instanceof OracleJsonValue) {
        // Depends on javax.json, not jakarta.json!
        //    return ((OracleJsonValue) jdbcValue).wrap(JsonValue.class);
            try (final JsonReader jr = Json.createReader(new StringReader(((OracleJsonValue) jdbcValue).toString()))) {
                return jr.readValue();
            }
        }
        throw new PersistenceException("Could not convert JDBC ResultSet type to JsonValue");
    }

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
        return resultSet.getObject(columnNumber, OracleJsonValue.class);
    }

}
