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

import com.bmuschko.gradle.docker.response.ResponseHandler
import com.bmuschko.gradle.docker.response.image.ListImagesResponseHandler
import com.bmuschko.gradle.docker.tasks.AbstractDockerRemoteApiTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional

class DockerListContainers extends AbstractDockerRemoteApiTask {
	private ResponseHandler<Void, List<Object>> responseHandler = new ListImagesResponseHandler()

	@Input
	@Optional
	Boolean showAll
	
	@Input
	@Optional
	Integer limit
	
	@Input
	@Optional
	String since
	
	@Input
	@Optional
	String before
	

	@Input
	@Optional
	String filters

	List containers

	@Optional
	greps

	@Override
	void runRemoteCommand(dockerClient) {
		def listContainersCmd = dockerClient.listContainersCmd()

		if(getShowAll()) {
			listContainersCmd.withShowAll(getShowAll())
		}
		
		if(getLimit()) {
			listContainersCmd.withLimit(getLimit())
		}
		
		if(getSince()) {
			listContainersCmd.withSince(getSince())
		}
		
		if(getBefore()) {
			listContainersCmd.withBefore(getBefore())
		}

		if(getFilters()) {
			listContainersCmd.withFilters(getFilters())
		}
		
		List allContainers = listContainersCmd.exec()
		
		images = greps ? allImages.findAll {
			image ->
			greps.any {
				grep ->
				grep.collect {
					k, v ->
					image.hasProperty(k) &&
						(image.("$k").getClass().isArray() ? image.("$k").any {it ==~ v} : image.("$k")  ==~ v)
				}.every {
					it == true
				}
			}
		} : allImages

		responseHandler.handle(images)
	}

	void setResponseHandler(ResponseHandler<Void, List<Object>> responseHandler) {
		this.responseHandler = responseHandler
	}
}