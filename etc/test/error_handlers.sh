#
# Copyright (c) 2021 Oracle and/or its affiliates. All rights reserved.
#
# This program and the accompanying materials are made available under the
# terms of the Eclipse Distribution License v. 1.0, which is available at
# http://www.eclipse.org/org/documents/edl-v10.php.
#
# SPDX-License-Identifier: BSD-3-Clause

###############################################################################
# Error handling functions                                                    #
###############################################################################

# Multiple definition protection.
# The same code is included in both local and pipeline environment setup.
if [ -z "${__ERROR_HANDLER_INCLUDED__}" ]; then
    readonly __ERROR_HANDLER_INCLUDED__='true'

    # Default error handler.
    # Shell variables: CODE
    #                  BASH_SOURCE
    #                  LINENO
    #                  BASH_COMMAND
    on_error() {
        CODE="${?}" && \
        set +x && \
        printf "[ERROR] Error(code=%s) occurred at %s:%s command: %s\n" \
            "${CODE}" "${BASH_SOURCE}" "${LINENO}" "${BASH_COMMAND}"
    }

    # Error handling setup
    # Arguments: $1 - error handler name (optional, default name is 'on_error')
    error_trap_setup() {
        # trace ERR through pipes
        set -o pipefail || true
        # trace ERR through commands and functions
        set -o errtrace || true
        # exit the script if any statement returns a non-true return value
        set -o errexit || true
        # Set error handler
        trap "${1:-on_error}" ERR
    }

else
    echo "WARNING: ${WS_DIR}/etc/scripts/includes/error_handlers.sh included multiple times."
    echo "WARNING: Make sure that only one from local and pipeline environment setups is loaded."
fi
