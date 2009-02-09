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
package ru.caffeineim.protocols.icq.packet.received.byddylist;

import ru.caffeineim.protocols.icq.RawData;
import ru.caffeineim.protocols.icq.core.OscarConnection;
import ru.caffeineim.protocols.icq.integration.events.OffgoingUserEvent;
import ru.caffeineim.protocols.icq.integration.listeners.StatusListener;
import ru.caffeineim.protocols.icq.packet.received.ReceivedPacket;

/**
 * @author Fabrice Michellonet
 */
public class OffgoingUser__3_12 extends ReceivedPacket {

	private RawData userId;

	public OffgoingUser__3_12(byte[] array) {
		super(array, true);
		int position = 0;

		byte[] data = getSnac().getDataFieldByteArray();

		/* Retreiving user id */
		RawData idLen = new RawData(data, position, 1);
		position++;
		userId = new RawData(data, position, idLen.getValue());
	}

	public void notifyEvent(OscarConnection connection) {
		OffgoingUserEvent e = new OffgoingUserEvent(this);
		for (int i = 0; i < connection.getStatusListeners().size(); i++) {
			StatusListener l = (StatusListener) connection.getStatusListeners().get(i);
			l.onOffgoingUser(e);
		}
	}

	public String getUserId() {
		return userId.getStringValue();
	}
}