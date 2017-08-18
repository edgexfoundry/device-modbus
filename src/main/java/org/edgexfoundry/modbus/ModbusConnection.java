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
package org.edgexfoundry.modbus;

import java.net.InetAddress;
import java.util.HashMap;

import javax.xml.bind.DatatypeConverter;

import org.edgexfoundry.domain.ModbusObject;
import org.edgexfoundry.domain.meta.Addressable;
import org.edgexfoundry.domain.meta.Protocol;
import org.edgexfoundry.exception.BadCommandRequestException;
import org.edgexfoundry.support.logging.client.EdgeXLogger;
import org.edgexfoundry.support.logging.client.EdgeXLoggerFactory;

import com.ghgande.j2mod.modbus.ModbusIOException;
import com.ghgande.j2mod.modbus.io.ModbusSerialTransaction;
import com.ghgande.j2mod.modbus.io.ModbusTCPTransaction;
import com.ghgande.j2mod.modbus.msg.ModbusRequest;
import com.ghgande.j2mod.modbus.msg.ReadMultipleRegistersRequest;
import com.ghgande.j2mod.modbus.msg.ReadMultipleRegistersResponse;
import com.ghgande.j2mod.modbus.msg.WriteSingleRegisterRequest;
import com.ghgande.j2mod.modbus.net.SerialConnection;
import com.ghgande.j2mod.modbus.net.TCPMasterConnection;
import com.ghgande.j2mod.modbus.procimg.SimpleRegister;
import com.ghgande.j2mod.modbus.util.SerialParameters;

public class ModbusConnection {

	private static HashMap<String, Object> connections;
	private final static EdgeXLogger logger = EdgeXLoggerFactory.getEdgeXLogger(ModbusConnection.class);


	public ModbusConnection(){
		connections = new HashMap<String, Object>();
	}

	public Object getModbusConnection(Addressable addressable){
		Object connection = null;
		synchronized(connections) {
			if(connections.containsKey(addressable.getAddress())){
				connection = connections.get(addressable.getAddress());
			}
			else{ 
				if(addressable.getProtocol() == Protocol.HTTP){
					logger.info("creating TCP connection");
					connection = createTCPConnection(addressable);
				}
				else /*if(addressable.getProtocol() == Protocol.OTHER)*/{
					connection = createSerialConnection(addressable);
				}
				connections.put(addressable.getAddress(), connection);
			}
		}
		return connection;
	}

	private Object createSerialConnection(Addressable addressable) {
		SerialConnection con = null;
		try
		{
			SerialParameters params = new SerialParameters();
			String address = addressable.getAddress();
			String[] serialParams = address.split(",");
			if(serialParams.length > 0)
			{
				if(serialParams[0] != null){
					params.setPortName(serialParams[0].trim());
					logger.info("Port:" + serialParams[0].trim());
				}
				if(serialParams[1] != null){
					params.setBaudRate(Integer.parseInt(serialParams[1].trim()));
					logger.info("BaudRate:" + serialParams[1].trim());
				}
				if(serialParams[2] != null){

					params.setDatabits(Integer.parseInt(serialParams[2].trim()));
					logger.info("Data Bits:" + serialParams[2].trim());

				}
				if(serialParams[3] != null){
					params.setStopbits(Integer.parseInt(serialParams[3].trim()));
					logger.info("Stop Bitse:" + serialParams[3].trim());

					
				}
				if(serialParams[4] != null){
					params.setParity(Integer.parseInt(serialParams[4].trim()));
					logger.info("Parity:" + serialParams[4].trim());

				}

				
				params.setEncoding("rtu");
				params.setEcho(false);
			}			
			con = new SerialConnection(params);
			con.setTimeout(100);		
			con.open();

		}catch(Exception e){
			logger.error("Exception in creating Serial connection:" + e);
		}
		return con;
	}

	private Object createTCPConnection(Addressable addressable) {

		TCPMasterConnection con = null;
		try{
			InetAddress addr = 	InetAddress.getByName(addressable.getAddress());
			con = new TCPMasterConnection(addr);
			con.setPort(addressable.getPort());
			//con.connect();
			logger.info("Created TCP Connection");
		}catch(Exception e){
			logger.error("Exception in creating TCP connection:" + e);
		}
		return con;
	}

	public String getValue(Object connection, Addressable addressable, ModbusObject object, int retryCount) {
		String result = "";
		ReadMultipleRegistersResponse res = null; //the response
		ReadMultipleRegistersRequest req = new ReadMultipleRegistersRequest(Integer.parseInt(object.getAttributes().getHoldingRegister()), 1);
		logger.info("Holding Register:" + object.getAttributes().getHoldingRegister());
		if(connection instanceof TCPMasterConnection){
			TCPMasterConnection con = (TCPMasterConnection)connection;

			req.setUnitID(0);

			try
			{
				if(!con.isConnected())
					con.connect();
				ModbusTCPTransaction transaction = new ModbusTCPTransaction(con);
				transaction.setRequest(req);
				transaction.execute();
				res = (ReadMultipleRegistersResponse) transaction.getResponse();
				result = "" + res.getRegisterValue(0);
			}catch(ModbusIOException ioe){
				retryCount ++;
				if(retryCount < 3){
					logger.warn("Cannot get the value:" + ioe.getMessage() + ",count:" + retryCount);
					getValue(connection, addressable, object, retryCount);
				}
				else{
					throw new BadCommandRequestException(ioe.getMessage());
				}

			}
			catch(Exception e){
				logger.error("General Exception e:" + e.getMessage());

				throw new BadCommandRequestException(e.getMessage());
			}
			finally{
				con.close();
			}
		}
		else if(connection instanceof SerialConnection){
			req.setUnitID(1);
			req.setHeadless();
			SerialConnection con = (SerialConnection)connection;
			try
			{
				if(!con.isOpen())
				{
					con.open();
				}
				ModbusSerialTransaction transaction = new ModbusSerialTransaction(con);
				transaction.setRequest(req);
				transaction.execute();
				Thread.sleep(1000);
				res = (ReadMultipleRegistersResponse) transaction.getResponse();
				result = "" + res.getRegisterValue(0);
				logger.info("Result:" + result);

			}catch(ModbusIOException ioe){
				retryCount ++;
				if(retryCount < 3){
					logger.warn("Cannot get the value:" + ioe.getMessage() + ",count:" + retryCount);
					getValue(connection, addressable, object, retryCount);
				}
				else{
					throw new BadCommandRequestException(ioe.getMessage());
				}

			}
			catch(Exception e){
				logger.error("General Exception e:" + e.getMessage());

				throw new BadCommandRequestException(e.getMessage());
			}
			finally{
				//con.close();
			}
		}

		return result;
	}

	public String setValue(Object connection, Addressable addressable, ModbusObject object, String value, int retryCount) {
		String result = "";
		String scaledValue = "";
		if(connection instanceof TCPMasterConnection){
			TCPMasterConnection con = (TCPMasterConnection)connection;
			logger.info("Setting value here scale:" + object.getProperties().getValue().getScale() + ", property:" + object.getName() + "Value:" + value);
			try{
				if(value != null){
					float scale = Float.parseFloat(object.getProperties().getValue().getScale());
					Float newValue = (Integer.parseInt(value)/scale);

					scaledValue = newValue.intValue() + "" ;
				}
				ModbusRequest req = new WriteSingleRegisterRequest(Integer.parseInt(object.getAttributes().getHoldingRegister()), new SimpleRegister(Integer.parseInt(scaledValue)));
				req.setUnitID(0);
				con.connect();
				ModbusTCPTransaction transaction = new ModbusTCPTransaction(con);
				transaction.setRequest(req);
				transaction.execute();
				result = transaction.getResponse().getHexMessage();
				DatatypeConverter.parseHexBinary(result.replaceAll(" ", ""));
				logger.info("After setting value:" + result);
				result = scaledValue;
			}catch(ModbusIOException ioe){

				retryCount ++;
				if(retryCount < 3){
					logger.error("Cannot set the value:" + ioe.getMessage() + ",count:" + retryCount);
					setValue(connection, addressable, object, value, retryCount);
				}
				else{

					throw new BadCommandRequestException(ioe.getMessage());
				}

			}
			catch(Exception e){
				logger.error("Cannot set the value general Exception:" + e.getMessage());
				throw new BadCommandRequestException(e.getMessage());
			}

			finally{
				//transaction = null;
				con.close();
			}
		}else if(connection instanceof SerialConnection){
			SerialConnection con = (SerialConnection)connection;
			logger.info("Setting value here scale:" + object.getProperties().getValue().getScale() + ", property:" + object.getName() + "Value:" + value);
			try{
				if(value != null){
					float scale = Float.parseFloat(object.getProperties().getValue().getScale());
					Float newValue = (Integer.parseInt(value)/scale);

					scaledValue = newValue.intValue() + "" ;
				}
				ModbusRequest req = new WriteSingleRegisterRequest(Integer.parseInt(object.getAttributes().getHoldingRegister()), new SimpleRegister(Integer.parseInt(scaledValue)));
				req.setUnitID(0);
				con.open();
				ModbusSerialTransaction transaction = new ModbusSerialTransaction(con);
				transaction.setRequest(req);
				transaction.execute();
				result = transaction.getResponse().getHexMessage();
				DatatypeConverter.parseHexBinary(result.replaceAll(" ", ""));
				logger.info("After setting value:" + result);
				result = scaledValue;
			}catch(ModbusIOException ioe){

				retryCount ++;
				if(retryCount < 3){
					logger.error("Cannot set the value:" + ioe.getMessage() + ",count:" + retryCount);
					setValue(connection, addressable, object, value, retryCount);
				}
				else{

					throw new BadCommandRequestException(ioe.getMessage());
				}

			}
			catch(Exception e){
				logger.error("Cannot set the value general Exception:" + e.getMessage());
				throw new BadCommandRequestException(e.getMessage());
			}

			finally{
				//transaction = null;
				con.close();
			}
		}

		return result;
	}
}
