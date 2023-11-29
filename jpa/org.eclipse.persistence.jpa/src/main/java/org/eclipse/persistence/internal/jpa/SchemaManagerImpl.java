/*
 * Copyright (c) 1998, 2023 Oracle and/or its affiliates. All rights reserved.
 * Copyright (c) 1998, 2023 IBM Corporation. All rights reserved.
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
//     11/28/2023: Tomas Kraus
//       - New Jakarta Persistence 3.2 Features
package org.eclipse.persistence.internal.jpa;

import java.util.List;
import java.util.function.Consumer;

import jakarta.persistence.SchemaManager;
import jakarta.persistence.SchemaValidationException;
import org.eclipse.persistence.internal.localization.ExceptionLocalization;
import org.eclipse.persistence.internal.sessions.DatabaseSessionImpl;
import org.eclipse.persistence.tools.schemaframework.TableValidationException;

public class SchemaManagerImpl implements SchemaManager {

    private final org.eclipse.persistence.tools.schemaframework.SchemaManager schemaManager;

    SchemaManagerImpl(DatabaseSessionImpl session) {
        schemaManager = new org.eclipse.persistence.tools.schemaframework.SchemaManager(session);
    }

    @Override
    public void create(boolean createSchemas) {
        if (createSchemas) {
            schemaManager.createDefaultTables(true);
        }
    }

    @Override
    public void drop(boolean dropSchemas) {
        if (dropSchemas) {
            schemaManager.dropDefaultTables();
        }
    }

    // TODO-API-3.2
    @Override
    public void validate() throws SchemaValidationException {
        ValidationFailure failures = new ValidationFailure();
        if (!schemaManager.validateDefaultTables(failures, true)) {
            throw new SchemaValidationException(
                    ExceptionLocalization.buildMessage("schema_validation_failed"),
                    failures.result().toArray(new TableValidationException[failures.result().size()]));
        }
    }

    @Override
    public void truncate() {
        schemaManager.truncateDefaultTables(false);
    }

    private static final class ValidationFailure implements Consumer<List<TableValidationException>> {

        private static List<TableValidationException> validationResult;

        private ValidationFailure() {
            validationResult = null;
        }

        @Override
        public void accept(List<TableValidationException> failures) {
            validationResult = failures;
        }

        private List<TableValidationException> result() {
            return validationResult;
        }

    }

}
