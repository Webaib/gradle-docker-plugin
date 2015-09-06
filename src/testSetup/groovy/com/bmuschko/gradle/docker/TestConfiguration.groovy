package com.bmuschko.gradle.docker

final class TestConfiguration {
    private static final String DOCKER_SERVER_URL_SYS_PROP = 'dockerServerUrl'
    private static final String DOCKER_CERT_PATH_SYS_PROP = 'dockerCertPath'
    private static final String DOCKER_PRIVATE_REGISTRY_URL_SYS_PROP = 'dockerPrivateRegistryUrl'

    private TestConfiguration() {}

    static String getDockerServerUrl() {
        System.properties[DOCKER_SERVER_URL_SYS_PROP] ?: 'https://localhost:2376'
    }

    static File getDockerCertPath() {
        System.properties[DOCKER_CERT_PATH_SYS_PROP] ? new File(System.properties[DOCKER_CERT_PATH_SYS_PROP]) : null
    }

    static String getDockerPrivateRegistryUrl() {
        System.properties[DOCKER_PRIVATE_REGISTRY_URL_SYS_PROP] ?: 'http://localhost:5000'
    }

    static String getDockerPrivateRegistryDomain() {
        getDockerPrivateRegistryUrl() - 'http://' - 'https://'
    }
}
