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

import java.util.EventListener;
import java.util.EventObject;

import ru.caffeineim.protocols.icq.RawData;
import ru.caffeineim.protocols.icq.exceptions.ConvertStringException;
import ru.caffeineim.protocols.icq.integration.events.MetaNoteUserInfoEvent;
import ru.caffeineim.protocols.icq.integration.listeners.MetaInfoListener;
import ru.caffeineim.protocols.icq.tool.StringTools;

/**
 * @author Samolisov Pavel
 * @since  30.03.2008
 */
public class NotesUserInfoParser extends BaseMetaInfoParser {

	private String note;
	
	@Override
	protected EventObject getNewEvent() {
		return new MetaNoteUserInfoEvent(this);
	}

	@Override
	protected void sendMessage(EventListener listener, EventObject e) {
		((MetaInfoListener) listener).onNotesUserInfo((MetaNoteUserInfoEvent) e);
	}

	public void parse(byte[] data, int position) throws ConvertStringException {
		position += 3; // skip subtype and success byte (always 0x0A) and data size.
		
		// Note string lenght
		RawData rStrLen = new RawData(data, position, RawData.WORD_LENGHT);
		rStrLen.invertIndianness();
		position += RawData.WORD_LENGHT;
		
		// Note String
		note = StringTools.byteArrayToString(data, position, rStrLen.getValue() - 1);		
	}

	public String getNote() {
		return note;
	}
}