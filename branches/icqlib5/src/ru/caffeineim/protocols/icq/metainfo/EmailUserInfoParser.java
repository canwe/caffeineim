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
package ru.caffeineim.protocols.icq.metainfo;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.List;

import ru.caffeineim.protocols.icq.RawData;
import ru.caffeineim.protocols.icq.exceptions.ConvertStringException;
import ru.caffeineim.protocols.icq.integration.events.MetaEmailUserInfoEvent;
import ru.caffeineim.protocols.icq.integration.listeners.MetaInfoListener;

/**
 * @author Samolisov Pavel
 * @since  26.03.2008
 */
public class EmailUserInfoParser extends BaseMetaInfoParser {
	private List<String> emails = new ArrayList<String>();

	
	protected EventObject getNewEvent() {
		return new MetaEmailUserInfoEvent(this);
	}

	
	protected void sendMessage(EventListener listener, EventObject e) {
		((MetaInfoListener) listener).onEmailUserInfo((MetaEmailUserInfoEvent) e);	
	}

	
	public void parse(byte[] data, int position) throws ConvertStringException {
		position += 3; // skip subtype and success byte (always 0x0A) and data size.
		
		int len = (new RawData(data, position, RawData.BYTE_LENGHT)).getValue();
		position += RawData.BYTE_LENGHT;
		
		for (int i = 0; i < len; i++) {			
			// Skiping publish byte
			position += RawData.BYTE_LENGHT;
			
			// Email lenght
			RawData rStrLen = new RawData(data, position, RawData.WORD_LENGHT);
			rStrLen.invertIndianness();		
			position += RawData.WORD_LENGHT;;
		
			// Email
			String email = (new RawData(data, position, rStrLen.getValue() - 1)).getStringValue();
			position += rStrLen.getValue();
			
			emails.add(email);
		}
	}

	public List<String> getEmails() {
		return emails;
	}	
}