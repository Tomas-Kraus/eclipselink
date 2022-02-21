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
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Map;

import jakarta.json.JsonValue;
import jakarta.persistence.PersistenceException;

import org.eclipse.persistence.internal.databaseaccess.FieldTypeDefinition;
import org.eclipse.persistence.internal.localization.ExceptionLocalization;
import org.eclipse.persistence.internal.sessions.AbstractSession;
import org.eclipse.persistence.platform.database.PostgreSQLPlatform;
import org.postgresql.util.PGobject;

/**
 * Postgres 10 database platform extension.
 * <p>
 * <b>Purpose</b>: Provides Postgres 10 specific behavior.
 * <p>
 * <b>Responsibilities</b>:
 * <ul>
 * <li>Native JSON support added in version 10.
 * </ul>
 * This class requires Postgres JDBC driver on the classpath.
 */
public class PostgreSQL10Platform extends PostgreSQLPlatform {

    /**
     * Creates an instance of Postgres 10 platform.
     */
    public PostgreSQL10Platform() {
        super();
    }

    /**
     * Build the mapping of database types to class types for the schema framework.
     *
     * @return database types to class types {@code Map} for the schema framework
     */
    @Override
    protected Map<String, Class<?>> buildClassTypes() {
        final Map<String, Class<?>> classTypeMapping = super.buildClassTypes();
        classTypeMapping.put("JSONB", jakarta.json.JsonValue.class);
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
        fieldTypeMapping.put(jakarta.json.JsonObject.class, new FieldTypeDefinition("JSONB"));
        fieldTypeMapping.put(jakarta.json.JsonArray.class, new FieldTypeDefinition("JSONB"));
        fieldTypeMapping.put(jakarta.json.JsonValue.class, new FieldTypeDefinition("JSONB"));
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
        if (parameter instanceof PGobject) {
            statement.setObject(index, parameter);
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
        if (parameter instanceof PGobject) {
            statement.setObject(name, parameter);
        } else {
            super.setParameterValueInDatabaseCall(parameter, statement, name, session);
        }
    }

}
