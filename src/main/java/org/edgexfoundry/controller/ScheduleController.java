/*******************************************************************************
 * Copyright 2016-2017 Dell Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @microservice:  device-modbus
 * @author: Anantha Boyapalle, Dell
 * @version: 1.0.0
 *******************************************************************************/
package org.edgexfoundry.controller;

import org.edgexfoundry.domain.meta.CallbackAlert;
import org.edgexfoundry.exception.controller.ServiceException;
import org.edgexfoundry.handler.SchedulerCallbackHandler;
import org.edgexfoundry.support.logging.client.EdgeXLogger;
import org.edgexfoundry.support.logging.client.EdgeXLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ScheduleController {

	@Autowired
	private SchedulerCallbackHandler callbackHandler;

	private static final EdgeXLogger logger = EdgeXLoggerFactory.getEdgeXLogger(ScheduleController.class);

	public String handlePUT(@RequestBody CallbackAlert data) {
		try {
			logger.debug("put callback : '" + data + "'");
			return (callbackHandler.handlePUT(data) == true) ? "true" : "false";
		} catch (Exception e) {
			logger.error("put error : " + e.getMessage());
			throw new ServiceException(e);
		}
	}

	public String handlePOST(@RequestBody CallbackAlert data) {
		try {
			logger.debug("post callback : '" + data + "'");
			return (callbackHandler.handlePOST(data) == true) ? "true" : "false";
		} catch (Exception e) {
			logger.error("post error : " + e.getMessage());
			throw new ServiceException(e);
		}
	}

	public String handleDELETE(@RequestBody CallbackAlert data) {
		try {
			logger.debug("delete callback : '" + data + "'" );
			return (callbackHandler.handleDELETE(data) == true) ? "true" : "false";
		} catch (Exception e) {
			logger.error("delete error : " + e.getMessage());
			throw new ServiceException(e);
		}
	}

}
// SDK Scheduler Block -->
