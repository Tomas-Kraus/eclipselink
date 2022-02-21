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
package org.eclipse.persistence.json;

import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import jakarta.json.Json;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import jakarta.json.JsonWriter;

import org.eclipse.persistence.internal.databaseaccess.DatabaseJsonPlatform;
import org.eclipse.persistence.internal.databaseaccess.DatabasePlatform;
import org.eclipse.persistence.platform.database.MySQLPlatform;

/** JSON database platform. */
public class JsonPlatform implements DatabaseJsonPlatform {

    private static final class JsonPlatformFactory implements DatabaseJsonPlatform.Factory {

        // Factory singleton instance.
        private static final JsonPlatformFactory INSTANCE = new JsonPlatformFactory();

        private JsonPlatformFactory() {
            this.mapping = initMapping();
        }

        // JSON database platform factory mapping.
        private final Map<Class<? extends DatabasePlatform>, Supplier<DatabaseJsonPlatform>> mapping;

        // Iniotialize JSON database platform factory mapping.
        private Map<Class<? extends DatabasePlatform>, Supplier<DatabaseJsonPlatform>> initMapping() {
            final Map<Class<? extends DatabasePlatform>, Supplier<DatabaseJsonPlatform>> mapping = new HashMap<>();
            mapping.put(MySQLPlatform.class, MySQLJsonPlatform::new);
            return Collections.unmodifiableMap(mapping);
        }

        @Override
        public DatabaseJsonPlatform create(final Class<? extends DatabasePlatform> platformClass) {
            final Supplier<DatabaseJsonPlatform> supplier = mapping.get(platformClass);
            return supplier != null ? supplier.get() : new JsonPlatform();
        }
    }

    /**
     * Returns JSON database platorm factory.
     *
     * @return singleton factory instance.
     */
    public static JsonPlatformFactory getFactory() {
        return JsonPlatformFactory.INSTANCE;
    }

    public boolean isJsonType(final Type type) {
        switch (type.getTypeName()) {
            case "jakarta.json.JsonValue":
            case "jakarta.json.JsonArray":
            case "jakarta.json.JsonObject":
                return true;
            default:
                return false;
        }
    }

    // Common JSON types support:
    // Stores JsonValue instances as VARCHAR.
    /**
     * INTERNAL:
     * Convert JSON value field to JDBC statement type.
     * Common JSON storage type is {@code VARCHAR} so target Java type is {@code String}.
     *
     * @param <T> classification type
     * @param jsonValue source JSON value field
     * @return converted JDBC statement type
     */
    @SuppressWarnings("unchecked")
    public <T> T convertJsonValueToDataValue(final JsonValue jsonValue) {
        if (jsonValue == null) {
            return null;
        }
        final StringWriter sw = new StringWriter(128);
        try (final JsonWriter jw = Json.createWriter(sw)) {
            jw.write(jsonValue);
        }
        return (T) sw.toString();
    }

    /**
     * Convert JDBC {@code ResultSet} type to JSON value field.
     * This method consumes value returned by {@link Object getJsonDataFromResultSet(ResultSet, int)}.
     * Both methods must be overwritten by platform specific code when jdbcValue is not String.
     *
     * @param jdbcValue source classification type value from JDBC
     * @return converted JSON field value
     */
    public JsonValue convertDataValueToJsonValue(Object jdbcValue) {
        if (jdbcValue == null) {
            return null;
        }
        try (final JsonReader jr = Json.createReader(new StringReader((String)jdbcValue))) {
            return jr.readValue();
        }
    }

}
