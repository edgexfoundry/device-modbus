# v0.2 (10/20/2017)
# Release Notes

## Notable Changes
The Barcelona Release (v 0.2) of the Modbus micro service includes the following:
* POM changes for appropriate repository information for distribution/repos management, checkstyle plugins, etc.
* Removed all references to unfinished DeviceManager work as part of Dell Fuse
* Added Dockerfile for creation of micro service targeted for ARM64
* Consolidated Docker properties files to common directory

## Bug Fixes
* Fixed Consul configuration properties
* Fixed Device equality logic
* Added check for service existence after initialization to Base Service

 - [#14](https://github.com/edgexfoundry/device-modbus/pull/14) - Remove staging plugin contributed by Jeremy Phelps ([JPWKU](https://github.com/JPWKU))
 - [#13](https://github.com/edgexfoundry/device-modbus/pull/13) - Adds null check in BaseService contributed by Tyler Cox ([trcox](https://github.com/trcox))
 - [#12](https://github.com/edgexfoundry/device-modbus/pull/12) - Fixes Maven artifact dependency path contributed by Tyler Cox ([trcox](https://github.com/trcox))
 - [#11](https://github.com/edgexfoundry/device-modbus/pull/11) - added staging and snapshots repos to pom along with nexus staging mav… contributed by Jim White ([jpwhitemn](https://github.com/jpwhitemn))
 - [#10](https://github.com/edgexfoundry/device-modbus/pull/10) - Removed device manager url refs in properties files contributed by Jim White ([jpwhitemn](https://github.com/jpwhitemn))
 - [#9](https://github.com/edgexfoundry/device-modbus/pull/9) - Added support for aarch64 arch contributed by ([feclare](https://github.com/feclare))
 - [#8](https://github.com/edgexfoundry/device-modbus/pull/8) - Fixes device comparison logic contributed by Tyler Cox ([trcox](https://github.com/trcox))
 - [#7](https://github.com/edgexfoundry/device-modbus/pull/7) - Consolidates Docker properties files contributed by Tyler Cox ([trcox](https://github.com/trcox))
 - [#6](https://github.com/edgexfoundry/device-modbus/pull/6) - Fixes Consul Properties contributed by Tyler Cox ([trcox](https://github.com/trcox))
 - [#5](https://github.com/edgexfoundry/device-modbus/pull/5) - Adds Docker build capability contributed by Tyler Cox ([trcox](https://github.com/trcox))
 - [#4](https://github.com/edgexfoundry/device-modbus/pull/4) - Add distributionManagement for artifact storage contributed by Tyler Cox ([trcox](https://github.com/trcox))
 - [#3](https://github.com/edgexfoundry/device-modbus/pull/3) - fix change of packaging for schedule clients (sorry Tyler - change will impact all DS) contributed by Jim White ([jpwhitemn](https://github.com/jpwhitemn))
 - [#1](https://github.com/edgexfoundry/device-modbus/pull/1) - Contributed Project Fuse source code contributed by Tyler Cox ([trcox](https://github.com/trcox))
