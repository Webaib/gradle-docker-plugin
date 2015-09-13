package com.bmuschko.gradle.docker.response.container

import com.bmuschko.gradle.docker.response.ResponseHandler
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging

class ListContainersResponseHandler implements ResponseHandler<String, List> {
    private final Logger logger

    ListContainersResponseHandler() {
        this(Logging.getLogger(ListContainersResponseHandler))
    }

    private ListContainersResponseHandler(Logger logger) {
        this.logger = logger
    }

    @Override
    String handle(List containers) {
        for(container in containers) {
            logger.quiet "Created : $container.created"
            logger.quiet "Status  : $container.status"
            logger.quiet "Labels  : ${container.labels?.collect {it}?.join(', ')}"
            logger.quiet "Id      : $container.id"
            logger.quiet "Command : $container.command"
            logger.quiet "Ports   : ${container.ports?.join(', ')}"
            logger.quiet "Names   : ${container.names?.join(', ')}"
            logger.quiet "Image   : $container.image"
            logger.quiet "-----------------------------------------------"
        }
    }
}
