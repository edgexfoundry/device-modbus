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
package org.edgexfoundry.domain;

import java.util.ArrayList;
import java.util.List;

import org.edgexfoundry.domain.meta.DeviceProfile;

@SuppressWarnings("serial")
public class ModbusDeviceProfile extends DeviceProfile {
	
	private List<ModbusObject> ModbusResources = new ArrayList<ModbusObject>();

	public ModbusDeviceProfile(DeviceProfile profile) {
		this.setCommands(profile.getCommands());
		this.setCreated(profile.getCreated());
		this.setDescription(profile.getDescription());
		this.setId(profile.getId());
		this.setLabels(profile.getLabels());
		this.setManufacturer(profile.getManufacturer());
		this.setModel(profile.getModel());
		this.setModified(profile.getModified());
		this.setName(profile.getName());
		this.setResources(profile.getResources());
		this.setObjects(profile.getObjects());
		this.setDeviceResources(profile.getDeviceResources());
	}

	public List<ModbusObject> getModbusResources() {
		return ModbusResources;
	}

	public void setModbusResources(List<ModbusObject> objects) {
		this.ModbusResources = objects;
	}

}
