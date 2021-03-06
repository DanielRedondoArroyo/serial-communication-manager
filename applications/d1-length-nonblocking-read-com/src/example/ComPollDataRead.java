/*
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

package example;

import com.embeddedunveiled.serial.SerialComManager;
import com.embeddedunveiled.serial.SerialComManager.BAUDRATE;
import com.embeddedunveiled.serial.SerialComManager.DATABITS;
import com.embeddedunveiled.serial.SerialComManager.FLOWCONTROL;
import com.embeddedunveiled.serial.SerialComManager.PARITY;
import com.embeddedunveiled.serial.SerialComManager.STOPBITS;

/* 
 * This example demonstrates how to read data from serial port and buffer it locally until a 
 * particular number of data bytes has been received from serial port.
 * 
 * There are many different versions of read methods provided by this library and developer 
 * can use the method that is best fit for application requirement. Other variant of read are :
 * 
 * readBytes(long handle)
 * readBytes(long handle, byte[] buffer, int offset, int length, long context)
 * readBytes(long handle, int byteCount)
 * readBytesBlocking(long handle, int byteCount, long context)
 * readBytesDirect(long handle, java.nio.ByteBuffer buffer, int offset, int length)
 * readSingleByte(long handle)
 * readString(long handle)
 * readString(long handle, int byteCount)
 * 
 * This design may be used for "send command and read response" type applications.
 */
public final class ComPollDataRead {

	public static void main(String[] args) {
		try {
			// get serial communication manager instance
			SerialComManager scm = new SerialComManager();

			String PORT = null;
			int osType = scm.getOSType();
			if(osType == SerialComManager.OS_LINUX) {
				PORT = "/dev/ttyUSB0";
			}else if(osType == SerialComManager.OS_WINDOWS) {
				PORT = "COM51";
			}else if(osType == SerialComManager.OS_MAC_OS_X) {
				PORT = "/dev/cu.usbserial-A70362A3";
			}else if(osType == SerialComManager.OS_SOLARIS) {
				PORT = null;
			}else{
			}

			long handle = scm.openComPort(PORT, true, true, true);
			scm.configureComPortData(handle, DATABITS.DB_8, STOPBITS.SB_1, PARITY.P_NONE, BAUDRATE.B9600, 0);
			scm.configureComPortControl(handle, FLOWCONTROL.NONE, 'x', 'x', false, false);
			
			scm.writeString(handle, "test", 0);

			// This is the final buffer in which all data read will be placed
			byte[] dataBuffer = new byte[100];
			byte[] data = null;
			int x = 0;
			int index = 0;
			int totalNumberOfBytesReadTillNow = 0;
			// Keep buffering data until 10 or more than 10 bytes are received.
			while (totalNumberOfBytesReadTillNow <= 10) {
				data = scm.readBytes(handle);
				if(data != null) {
					for(x=0; x<data.length; x++) {
						dataBuffer[index] = data[x];
						index++;
					}
					totalNumberOfBytesReadTillNow = totalNumberOfBytesReadTillNow + data.length;
				}
				Thread.sleep(10);
			}

			String readData = new String(dataBuffer);
			System.out.println("Data received is : " + readData);

			scm.closeComPort(handle);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

