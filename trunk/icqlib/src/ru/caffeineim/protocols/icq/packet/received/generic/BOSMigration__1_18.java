/**
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package ru.caffeineim.protocols.icq.packet.received.generic;

import java.io.IOException;

import ru.caffeineim.protocols.icq.RawData;
import ru.caffeineim.protocols.icq.Tlv;
import ru.caffeineim.protocols.icq.core.OscarConnection;
import ru.caffeineim.protocols.icq.packet.received.ReceivedPacket;

/**
 * <p>Created by
 *   @author Lo�c Broquet 
 */
public class BOSMigration__1_18 extends ReceivedPacket {
	private Tlv server;
	private Tlv cookie;

	public BOSMigration__1_18(byte array[]) {
		super(array, true);
		byte data[] = getSnac().getDataFieldByteArray();
		int nbFamilies = (new RawData(data, 0, 2)).getValue();
		server = new Tlv(data, 2 + 2 * nbFamilies);
		cookie = new Tlv(data, 2 + 2 * nbFamilies + server.getLength());
	}

	public void execute(OscarConnection connection) {
		try {
			connection.close();
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public void notifyEvent(OscarConnection oscarconnection) {
	}

	public String getBOSAddress() {
		return server.getStringValue();
	}

	public String getCookie() {
		return cookie.getStringValue();
	}
}