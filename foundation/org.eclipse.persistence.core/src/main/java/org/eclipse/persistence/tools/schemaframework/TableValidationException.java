/*
 * Copyright (c) 1998, 2023 Oracle and/or its affiliates. All rights reserved.
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
//     11/29/2023: Tomas Kraus
//       - New Jakarta Persistence 3.2 Features
package org.eclipse.persistence.tools.schemaframework;

import java.util.List;

import org.eclipse.persistence.internal.localization.ExceptionLocalization;

public abstract class TableValidationException extends Exception {
    private final String table;
    private final Type type;

    private TableValidationException(String message, String table, Type type) {
        super(message);
        this.table = table;
        this.type = type;
    }

    public String getTable() {
        return table;
    }

    public Type getType() {
        return type;
    }

    public abstract <T extends TableValidationException> T as(Class<T> type);

    public static final class MissingTable extends TableValidationException {

        MissingTable(String table) {
            super(ExceptionLocalization.buildMessage(
                    "schema_validation_missing_table", new String[] {table}),
                  table, Type.MISSING_TABLE);
        }

        public <T extends TableValidationException> T as(Class<T> cls) {
            if (cls == MissingTable.class) {
                return cls.cast(this);
            }
            throw new IllegalArgumentException(
                    String.format("Cannot cast this TableValidationException implementation as %s", cls.getName()));
        }

    }

    public static final class MissingColumns extends TableValidationException {

        private final List<String> columns;

        MissingColumns(String table, List<String> columns) {
            super(ExceptionLocalization.buildMessage(
                          "schema_validation_table_missing_columns", new String[] {table}),
                  table, Type.MISSING_COLUMNS);
            this.columns = columns;
        }

        public List<String> getColumns() {
            return columns;
        }

        public <T extends TableValidationException> T as(Class<T> cls) {
            if (cls == MissingColumns.class) {
                return cls.cast(this);
            }
            throw new IllegalArgumentException(
                    String.format("Cannot cast this TableValidationException implementation as %s", cls.getName()));
        }

    }

    public static final class SurplusColumns extends TableValidationException {

        private final List<String> columns;

        SurplusColumns(String table, List<String> columns) {
            super(ExceptionLocalization.buildMessage(
                          "schema_validation_table_surplus_columns", new String[] {table}),
                  table, Type.SURPLUS_COLUMNS);
            this.columns = columns;
        }

        public List<String> getColumns() {
            return columns;
        }

        public <T extends TableValidationException> T as(Class<T> cls) {
            if (cls == SurplusColumns.class) {
                return cls.cast(this);
            }
            throw new IllegalArgumentException(
                    String.format("Cannot cast this TableValidationException implementation as %s", cls.getName()));
        }

    }

    public static final class DifferentColumns extends TableValidationException {

        private final List<Difference> differences;

        DifferentColumns(String table, List<Difference> columns) {
            super(ExceptionLocalization.buildMessage(
                          "schema_validation_table_surplus_columns", new String[] {table}),
                  table, TableValidationException.Type.SURPLUS_COLUMNS);
            this.differences = columns;
        }

        public List<Difference> getDifferences() {
            return differences;
        }

        public <T extends TableValidationException> T as(Class<T> cls) {
            if (cls == SurplusColumns.class) {
                return cls.cast(this);
            }
            throw new IllegalArgumentException(
                    String.format("Cannot cast this TableValidationException implementation as %s", cls.getName()));
        }

        public static abstract class Difference {

            private final String columnName;
            private final Type type;

            private Difference(String columnName, Type type) {
                this.columnName = columnName;
                this.type = type;
            }

            public abstract <T extends Difference> T as(Class<T> type);

            public String getColumnName() {
                return columnName;
            }

            public Type getType() {
                return type;
            }

        }

        public static class TypeDifference extends Difference {

            private final String modelValue;
            private final String dbValue;

            public TypeDifference(String columnName, String modelValue, String dbValue) {
                super(columnName, Type.TYPE_DIFFERENCE);
                this.dbValue = dbValue;
                this.modelValue = modelValue;
            }

            public <T extends Difference> T as(Class<T> cls) {
                if (cls == TypeDifference.class) {
                    return cls.cast(this);
                }
                throw new IllegalArgumentException(
                        String.format("Cannot cast this Difference implementation as %s", cls.getName()));
            }

            public String getDbValue() {
                return dbValue;
            }

            public String getModelValue() {
                return modelValue;
            }

        }

        public enum Type {
            /** Type difference. */
            TYPE_DIFFERENCE;
        }

    }

    public enum Type {
        /** Missing table in the schema. */
        MISSING_TABLE,
        /** Table with missing columns in the schema. */
        MISSING_COLUMNS,
        /** Table with surplus columns in the schema. */
        SURPLUS_COLUMNS;
    }


}
