/**
 * Author : Rishi Gupta
 * 
 * This file is part of 'serial communication manager' library.
 * Copyright (C) <2014-2016>  <Rishi Gupta>
 *
 * This 'serial communication manager' is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by the Free Software 
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * The 'serial communication manager' is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR 
 * A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with 'serial communication manager'.  If not, see <http://www.gnu.org/licenses/>.
 */

package test2;

import java.util.Arrays;

import com.embeddedunveiled.serial.SerialComManager;
import com.embeddedunveiled.serial.SerialComManager.BAUDRATE;
import com.embeddedunveiled.serial.SerialComManager.DATABITS;
import com.embeddedunveiled.serial.SerialComManager.FLOWCONTROL;
import com.embeddedunveiled.serial.SerialComManager.PARITY;
import com.embeddedunveiled.serial.SerialComManager.STOPBITS;
import com.embeddedunveiled.serial.SerialComUtil;

public final class Test2 {
	public static void main(String[] args) {
		try {
			// get serial communication manager instance
			SerialComManager scm = new SerialComManager();

			String PORT = null;
			String PORT1 = null;
			int osType = scm.getOSType();
			if(osType == SerialComManager.OS_LINUX) {
				PORT = "/dev/ttyUSB0";
				PORT1 = "/dev/ttyUSB1";
			}else if(osType == SerialComManager.OS_WINDOWS) {
				PORT = "COM51";
				PORT1 = "COM52";
			}else if(osType == SerialComManager.OS_MAC_OS_X) {
				PORT = "/dev/cu.usbserial-A70362A3";
				PORT1 = "/dev/cu.usbserial-A602RDCH";
			}else if(osType == SerialComManager.OS_SOLARIS) {
				PORT = null;
				PORT1 = null;
			}else{
			}

			// try opening serial port for read and write without exclusive ownership
			long handle = scm.openComPort(PORT, true, true, false);
			// configure data communication related parameters
			scm.configureComPortData(handle, DATABITS.DB_8, STOPBITS.SB_1, PARITY.P_NONE, BAUDRATE.B115200, 0);
			// configure line control related parameters
			scm.configureComPortControl(handle, FLOWCONTROL.NONE, 'x', 'x', false, false);
			String[] config = scm.getCurrentConfiguration(handle);
			System.out.println(Arrays.toString(config));

			long handle1 = scm.openComPort(PORT1, true, true, false);
			scm.configureComPortData(handle1, DATABITS.DB_8, STOPBITS.SB_1, PARITY.P_NONE, BAUDRATE.B115200, 0);
			scm.configureComPortControl(handle1, FLOWCONTROL.NONE, 'x', 'x', false, false);
			String[] config1 = scm.getCurrentConfiguration(handle1);
			System.out.println(Arrays.toString(config1));

			scm.writeSingleByte(handle, (byte) 'A');
			Thread.sleep(200);
			byte[] datarcv = scm.readSingleByte(handle1);
			if(datarcv != null) {
				System.out.println("readSingleByte is : " + datarcv[0]);
			}


			String data111 = scm.readString(handle1);
			System.out.println("data read for 1 byte is : " + data111);

			// test single byte
			if(scm.writeString(handle, "1", 0) == true) {
				System.out.println("write success 1 byte");
			}
			Thread.sleep(200);
			String data = scm.readString(handle1);
			System.out.println("data read for 1 byte is : " + data);

			// test 2 byte
			if(scm.writeString(handle, "22", 0) == true) {
				System.out.println("write success 2 byte");
			}
			Thread.sleep(200);
			data = scm.readString(handle1);
			System.out.println("data read for 2 byte is : " + data);

			// test 3 byte
			if(scm.writeString(handle, "333", 0) == true) {
				System.out.println("write success 3 byte");
			}
			Thread.sleep(200);
			data = scm.readString(handle1);
			System.out.println("data read for 3 byte is : " + data);

			// test 4 byte
			if(scm.writeString(handle, "4444", 0) == true) {
				System.out.println("write success 4 byte");
			}
			Thread.sleep(200);
			data = scm.readString(handle1);
			System.out.println("data read for 4 byte is : " + data);

			// test 5 byte
			if(scm.writeString(handle, "55555", 0) == true) {
				System.out.println("write success 5 byte");
			}
			Thread.sleep(200);
			data = scm.readString(handle1);
			System.out.println("data read for 5 byte is : " + data);

			// test 10 byte
			if(scm.writeString(handle, "1000000000", 0) == true) {
				System.out.println("write success 10 byte");
			}
			Thread.sleep(200);
			data = scm.readString(handle1);
			System.out.println("data read for 10 byte is : " + data);

			// Test send command and read expected response
			byte[] COMMAND = new byte[]  { (byte) 0xaa,0x00,0x09,0x0a,0x00,0x0a,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x09 };
			byte[] RESPONSE = new byte[] { (byte) 0xaa,0x08,0x08,0x00,0x05,0x08,0x08,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x05 };

			scm.writeBytes(handle, COMMAND);
			Thread.sleep(200);
			byte[] datares = scm.readBytes(handle1);
			System.out.println("Command read  : " + SerialComUtil.byteArrayToHexString(datares, ","));

			scm.writeBytes(handle1, RESPONSE);
			Thread.sleep(200);
			datares = scm.readBytes(handle);
			System.out.println("Response read : " + SerialComUtil.byteArrayToHexString(datares, ","));

			scm.closeComPort(handle);
			scm.closeComPort(handle1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
