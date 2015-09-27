/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bmuschko.gradle.docker.tasks.container

import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional

class DockerRemoveContainer extends DockerExistingContainer {

	@Input
	@Optional
	Boolean force

	@Input
	@Optional
	Boolean removeVolumes
	
	@Input
	@Optional
	Boolean ignoreException

	@Override
	void runRemoteCommand(dockerClient) {
		logger.quiet "Removing container with ID '${getContainerId()}'."
		def removeContainerCmd = dockerClient.removeContainerCmd(getContainerId())

		if(getForce()) {
			removeContainerCmd.withForce(getForce())
		}

		if(getRemoveVolumes()) {
			removeContainerCmd.withRemoveVolumes(getRemoveVolumes())
		}

		if (getIgnoreException()) {
			try {
				removeContainerCmd.exec()
			} catch (Exception e) {
				logger.quiet "Can't remove container with ID '${getContainerId()}'."
			}
		} else {
			removeContainerCmd.exec()
		}
	}
}
