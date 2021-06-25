#
# Copyright (c) 2021 Oracle and/or its affiliates. All rights reserved.
#
# This program and the accompanying materials are made available under the
# terms of the Eclipse Distribution License v. 1.0, which is available at
# http://www.eclipse.org/org/documents/edl-v10.php.
#
# SPDX-License-Identifier: BSD-3-Clause

###############################################################################
# Local environment setup                                                     #
###############################################################################
# Shell variables: WS_DIR
# Arguments: $1 - Script path
#            $2 - cd to Helidon root directory from script path
#
# Atleast WS_DIR or both arguments must be passed.

# WS_DIR variable verification.
if [ -z "${WS_DIR}" ]; then

    if [ -z "${1}" ]; then
        echo "ERROR: Missing required script path, exitting"
        exit 1
    fi

    if [ -z "${2}" ]; then
        echo "ERROR: Missing required cd to Helidon root directory from script path, exitting"
        exit 1
    fi

    readonly WS_DIR=$(cd $(dirname -- "${1}") ; cd ${2} ; pwd -P)

fi

# Multiple definition protection.
if [ -z "${__LOCAL_ENV_INCLUDED__}" ]; then
    readonly __LOCAL_ENV_INCLUDED__='true'

    . ${WS_DIR}/etc/test/error_handlers.sh

    # Docker helper functions

    # Docker container start.
    # Database containers require listening TCP port to be mapped to specific host:port.
    # Arguments: $1 - name:version of the image
    #            $2 - name of the container (--name ${2})
    #            $3 - container port publishing (--publish ${3})
    #            $4 - additional docker run command arguments
    #            $5 - name of variable with container running status
    #            $6 - container start trigger variable name (optional, default true)
    docker_start() {
        if [ -z "${6}" ] || [ -n "${6}" -a -n "${!6}" ]; then
            echo -n 'Starting Docker container: '
            docker run -d \
               --name "${2}" \
               --publish "${3}" \
               ${4} \
               --rm ${1} && \
            [ -n "${5}" ] && eval "${5}='1'"
        fi
    }

    # Docker container stop.
    # Arguments: $1 - name of the container
    #            $2 - docker trigger variable name (optional, default true)
    docker_stop() {
        if [ -z "${2}" ] || [ -n "${2}" -a -n "${!2}" ]; then
            echo -n 'Stopping Docker container: '
            docker stop "${1}"
        fi
    }

else
    echo "WARNING: ${WS_DIR}/etc/scripts/includes/docker-env.sh included multiple times."
fi
