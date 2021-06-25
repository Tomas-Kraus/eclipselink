#
# Copyright (c) 2021 Oracle and/or its affiliates. All rights reserved.
#
# This program and the accompanying materials are made available under the
# terms of the Eclipse Distribution License v. 1.0, which is available at
# http://www.eclipse.org/org/documents/edl-v10.php.
#
# SPDX-License-Identifier: BSD-3-Clause

# PostgreSQL database setup (shell script)

echo 'Using PostgreSQL database'

readonly DB_DRIVER='org.postgresql.Driver'
readonly DB_XA_DRIVER='org.postgresql.Driver'
readonly DB_HOST='127.0.0.1'
readonly DB_PORT='5432'
readonly DB_NAME='ecltests'
readonly DB_USER='user'
readonly DB_PASSWORD='Us3r_P44sw0rd'
readonly DB_URL="jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}"
readonly DB_PLATFORM='org.eclipse.persistence.platform.database.PostgreSQLPlatform'
readonly DB_PROPERTIES="url=${DB_URL}"
readonly DATASOURCE_TYPE='java.sql.Driver'
readonly DATASOURCE_TX_SUPPORT='LOCAL_TRANSACTION'
readonly LOGGING_LEVEL='info'

readonly DOCKER_ENV="-e POSTGRES_USER=${DB_USER} -e POSTGRES_DB=${DB_NAME} -e POSTGRES_PASSWORD=${DB_PASSWORD}"
readonly DOCKER_IMG='postgres'

echo " - DB_DRIVER:             ${DB_DRIVER}"
echo " - DB_XA_DRIVER:          ${DB_XA_DRIVER}"
echo " - DB_NAME:               ${DB_NAME}"
echo " - DB_USER:               ${DB_USER}"
echo " - DB_URL:                ${DB_URL}"
echo " - DB_PLATFORM:           ${DB_PLATFORM}"
echo " - DB_PROPERTIES:         ${DB_PROPERTIES}"
echo " - DATASOURCE_TYPE:       ${DATASOURCE_TYPE}"
echo " - DATASOURCE_TX_SUPPORT: ${DATASOURCE_TX_SUPPORT}"
echo " - LOGGING_LEVEL:         ${LOGGING_LEVEL}"
echo " - DOCKER_IMG:            ${DOCKER_IMG}"

# Wait for PostgreSQL Docker container to come up
# Parameters: $1 - Docker container name
docker_wait() {
    COUNT='0'
    echo 'Waiting for database container:'
    while ! nc -z ${DB_HOST} ${DB_PORT} </dev/null; do
        echo -n '.'
        COUNT=$(( COUNT + 1 ))
        if [ "${COUNT}" -gt "79" ]; then
            echo ''
            COUNT='0'
        fi
        sleep 1;
    done
    if [ "${COUNT}" -gt '0' ]; then
        echo ''
    fi
    echo 'Waiting for database:'
    COUNT='0'
    while ! docker exec ${1} bash -c "echo ${DB_PASSWORD} | psql --username=${DB_USER} --password ecltests -c 'SELECT 0;'" >/dev/null 2>&1 ; do
        echo -n '.'
        COUNT=$(( COUNT + 1 ))
        if [ "${COUNT}" -gt '79' ]; then
            echo ''
            COUNT='0'
        fi
        sleep 1;
    done
    if [ "${COUNT}" -gt '0' ]; then
        echo ''
    fi
}
