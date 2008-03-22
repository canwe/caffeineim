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
package ru.caffeineim.protocols.icq.packet.received.ssi;

import ru.caffeineim.protocols.icq.RawData;
import ru.caffeineim.protocols.icq.core.OscarConnection;
import ru.caffeineim.protocols.icq.integration.events.SsiFutureAuthGrantEvent;
import ru.caffeineim.protocols.icq.integration.listeners.ContactListListener;
import ru.caffeineim.protocols.icq.packet.received.ReceivedPacket;

/**
 * <p>Created by 15.08.07
 *   @author Samolisov Pavel
 */
public class SsiFutureAuthGranted__19_20 extends ReceivedPacket {

	private String senderUin;
	
	private String message;

	public SsiFutureAuthGranted__19_20(byte[] array) {
		super(array, true);
		int position = 0;

		byte[] data = getSnac().getDataFieldByteArray();
		
		// unknown 8 bytes
		position += 8;

		// uin
		RawData len = new RawData(data[position++]);
		senderUin = new String(data, position, len.getValue());
		position += len.getValue();
		
		// message
		len = new RawData(data, position, RawData.WORD_LENGHT);
		position += 2;
		message = new String(data, position, len.getValue());
	}

	public void notifyEvent(OscarConnection connection) {		
		SsiFutureAuthGrantEvent e = new SsiFutureAuthGrantEvent(this);
		for (int i = 0; i < connection.getContactListListeners().size(); i++) {
			ContactListListener l = (ContactListListener) connection.getContactListListeners().get(i);					
			l.onSsiFutureAuthGrant(e);
		}
	}

	public String getSenderUin() {
		return senderUin;
	}
	
	public String getMessage() {
		return message;
	}
}