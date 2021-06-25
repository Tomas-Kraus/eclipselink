#!/bin/bash -e
#
# Copyright (c) 2021 Oracle and/or its affiliates. All rights reserved.
#
# This program and the accompanying materials are made available under the
# terms of the Eclipse Distribution License v. 1.0, which is available at
# http://www.eclipse.org/org/documents/edl-v10.php.
#
# SPDX-License-Identifier: BSD-3-Clause

# Path to this script
[ -h "${0}" ] && readonly SCRIPT_PATH="$(readlink "${0}")" || readonly SCRIPT_PATH="${0}"

# Load pipeline environment setup and define WS_DIR
. $(dirname -- "${SCRIPT_PATH}")/etc/test/docker-env.sh "${SCRIPT_PATH}" '.'


# Local error handler
test_on_error() {
    CODE="${?}" && \
    set +x && \
    printf "[ERROR] Error(code=%s) occurred at %s:%s command: %s\n" \
        "${CODE}" "${BASH_SOURCE}" "${LINENO}" "${BASH_COMMAND}"
    docker_stop "${DOCKER_CONT_NAME}" 'FLAG_C_RUN'
}

# Setup error handling using default settings (defined in includes/error_handlers.sh)
error_trap_setup 'test_on_error'

print_help() {
    echo 'Usage: test.sh [-hc] -d <database> -t <tests>'
    echo ''
    echo '  -h print this help and exit'
    echo '  -c start and stop Docker containers'
    echo '  -d <database> select database'
    echo '     <database> :: mysql | pgsql'
    echo '  -t <tests> select tests to run'
    echo '     test-srg | test-lrg | test-core-srg | test-core-lrg | ...'
}

# Evaluate command line arguments
if [ "$#" -gt '0' ]; then
    while getopts 'hcd:t:' flag 2> /dev/null; do
        case "${flag}" in
            h) print_help && exit;;
            c) readonly FLAG_C='1';;
            d) readonly FLAG_D=${OPTARG};;
            t) readonly FLAG_T=${OPTARG};;
        esac
    done
fi

# Load database setup
if [ -n "${FLAG_D}" ]; then
    case "${FLAG_D}" in
        mysql) . ${WS_DIR}/etc/test/mysql.sh;;
        pgsql) . ${WS_DIR}/etc/test/pgsql.sh;;
        *)     echo 'ERROR: Unknown database name, exitting.' && exit 1;;
    esac
else
    echo 'ERROR: No database was selected, exitting.'
    exit 1
fi

if [ -n "${FLAG_T}" ]; then
    case "${FLAG_T}" in
        srg) readonly TEST_PROFILE='test-srg';;
        lrg) readonly TEST_PROFILE='test-lrg';;
        core-srg) readonly TEST_PROFILE='test-core-srg'
                  readonly TEST_MODULE='org.eclipse.persistence.core.test';;
        core-lrg) readonly TEST_PROFILE='test-core-lrg'
                  readonly TEST_MODULE='org.eclipse.persistence.core.test';;
        *) echo 'ERROR: Unknown tests name, exitting.' && exit 1;;
    esac
    readonly MVN_TEST="-P${TEST_PROFILE}"
    if [ -n ${TEST_MODULE} ]; then
        readonly MVN_MODULE="-pl :${TEST_MODULE}"
    fi
else
    echo 'ERROR: No tests were selected, exitting.'
    exit 1
fi

# Start docker Container
if [ -n "${FLAG_C}" ]; then
    readonly DOCKER_CONT_NAME="eclipselink-tests-${FLAG_D}"
    docker_start "${DOCKER_IMG}" \
                 "${DOCKER_CONT_NAME}" \
                 "${DB_HOST}:${DB_PORT}:${DB_PORT}" \
                 "${DOCKER_ENV}" \
                 'FLAG_C_RUN' \
                 'FLAG_C'
    docker_wait "${DOCKER_CONT_NAME}"
    #docker logs -f ${DOCKER_CONT_NAME}
fi

set -x && \
mvn ${MVN_TEST} -P${FLAG_D} ${MVN_MODULE} \
    -Ddb.driver="${DB_DRIVER}" \
    -Ddb.xa.driver="${DB_XA_DRIVER}" \
    -Ddb.user="${DB_USER}" \
    -Ddb.pwd="${DB_PASSWORD}" \
    -Ddb.url="${DB_URL}" \
    -Ddb.platform="${DB_PLATFORM}" \
    -Ddb.properties="${DB_PROPERTIES}" \
    -Ddatasource.type="${DATASOURCE_TYPE}" \
    -Ddatasource.transactionsupport="${DATASOURCE_TX_SUPPORT}" \
    -Dlogging.level="${LOGGING_LEVEL}" \
    verify && \
set +x

# Stop docker Container
docker_stop "${DOCKER_CONT_NAME}" 'FLAG_C_RUN'
