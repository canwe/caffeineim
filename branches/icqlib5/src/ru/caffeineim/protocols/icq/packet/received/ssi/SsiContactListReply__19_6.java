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

import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

import ru.caffeineim.protocols.icq.Flap;
import ru.caffeineim.protocols.icq.Item;
import ru.caffeineim.protocols.icq.RawData;
import ru.caffeineim.protocols.icq.Snac;
import ru.caffeineim.protocols.icq.core.OscarConnection;
import ru.caffeineim.protocols.icq.exceptions.ConvertStringException;
import ru.caffeineim.protocols.icq.integration.events.ContactListEvent;
import ru.caffeineim.protocols.icq.integration.listeners.ContactListListener;
import ru.caffeineim.protocols.icq.packet.received.ReceivedPacket;

/**
 * <p>Created by
 *   @author Lo�c Broquet 
 */
public final class SsiContactListReply__19_6 extends ReceivedPacket {
	private int count;
	
	private int timeStamp;
	
	private SortedMap<Integer, Item> items = new TreeMap<Integer, Item>();
	
	public SsiContactListReply__19_6(byte array[]) throws ConvertStringException {
		super(array, true);
		int position = 8;
		byte data[] = getSnac().getDataFieldByteArray();
		//int ver = (new RawData(data, position, RawData.BYTE_LENGHT)).getValue();
		position++;
		count = (new RawData(data, position, RawData.WORD_LENGHT)).getValue();
		position += RawData.WORD_LENGHT;
		
		for(int i = 0; i < count; i++) {
			Item item = new Item(data, position);
			int id = (item.getGroup() << 16) + item.getId();
			items.put(id, item);
			position += item.getLength();
		}
		
		timeStamp = (new RawData(data, position, RawData.DWORD_LENGHT)).getValue();
		position += RawData.DWORD_LENGHT;
	}
	
	public void execute(OscarConnection connection) {
		connection.sendFlap(new Flap(2, new Snac(19, 7, 0, 0, 0)));
	}
	
	public void notifyEvent(OscarConnection connection) {
		ContactListEvent e = new ContactListEvent(this);
		for (int i = 0; i < connection.getContactListListeners().size(); i++) {
			ContactListListener l = (ContactListListener) connection.getContactListListeners().get(i);
			l.updateContactList(e);
		}
	}
	
	public int getCount() {
		return count;
	}
	
	public int getTimeStamp() {
		return timeStamp;
	}
	
	public Iterator<Item> getItemsIterator() {
		return items.values().iterator();
	}

	public Item get(int id) {
		return items.get(id);
	}
}