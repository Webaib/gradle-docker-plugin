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
package com.bmuschko.gradle.docker.tasks.image

import com.bmuschko.gradle.docker.response.image.ListImagesResponseHandler
import com.bmuschko.gradle.docker.response.ResponseHandler
import com.bmuschko.gradle.docker.tasks.AbstractDockerRemoteApiTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional

class DockerListImages extends AbstractDockerRemoteApiTask {
	private ResponseHandler<Void, List<Object>> responseHandler = new ListImagesResponseHandler()

	@Input
	@Optional
	Boolean showAll

	@Input
	@Optional
	String filters

	List images
	
	String firstId

	@Input
	@Optional
	greps

	DockerListImages() {
		ext.getFirstId = {
			firstId
		}
	}

	@Override
	void runRemoteCommand(dockerClient) {
		def listImagesCmd = dockerClient.listImagesCmd()

		if(getShowAll()) {
			listImagesCmd.withShowAll(getShowAll())
		}

		if(getFilters()) {
			listImagesCmd.withFilters(getFilters())
		}

		List allImages = listImagesCmd.exec()

		images = greps && !showAll ? allImages.findAll { image ->
			greps.any { grep ->
				grep.collect { k, v ->
					image.hasProperty(k) &&
						(image.("$k").getClass().isArray() ?
							image.("$k").any {it ==~ v} :
							image.("$k")  ==~ v)
				}.every { it == true }
			}
		} : allImages
	
		firstId = images.size() > 0 ? images.first().id : "---" 
	
		responseHandler.handle(images)
	}

	void setResponseHandler(ResponseHandler<Void, List<Object>> responseHandler) {
		this.responseHandler = responseHandler
	}
}
