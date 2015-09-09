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
            logger.quiet "Image ID   : $container.imageId"
            logger.quiet "Name       : $container.name"
            logger.quiet "Links      : $container.hostConfig.links"
            logger.quiet "-----------------------------------------------"
        }
    }
}
