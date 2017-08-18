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

import org.edgexfoundry.domain.ModbusDeviceProfile;
import org.edgexfoundry.domain.meta.Device;

@SuppressWarnings("serial")
public class ModbusDevice extends Device {
	private Boolean state = false;
	
	private ModbusDeviceProfile profile;
	private Integer failures = 0;
	
	public ModbusDevice(Device device) {
		this.setAdminState(device.getAdminState());
		this.setAddressable(device.getAddressable());
		this.setProfile(device.getProfile());
		this.setModbusProfile(new ModbusDeviceProfile(device.getProfile()));
		this.setCreated(device.getCreated());
		this.setDescription(device.getDescription());
		this.setId(device.getId());
		this.setLabels(device.getLabels());
		this.setLastConnected(device.getLastConnected());
		this.setModified(device.getModified());
		this.setName(device.getName());
		this.setOperatingState(device.getOperatingState());
		this.setService(device.getService());
		this.setLocation(device.getLocation());
	}

	public ModbusDeviceProfile getModbusProfile() {
		if (profile == null) {
			this.profile = new ModbusDeviceProfile(getProfile());
		}
		return profile;
	}

	public void setModbusProfile(ModbusDeviceProfile profile) {
		this.profile = profile;
	}

	public Boolean getState() {
		return state;
	}

	public void setState(Boolean state) {
		this.state = state;
	}

	public Integer getFailures() {
		return failures;
	}

	public void setFailures(Integer failures) {
		this.failures = failures;
	}
	
	public Device getDevice() {
		return (Device) this;
	}
	
}
