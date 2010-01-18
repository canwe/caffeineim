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
package ru.caffeineim.protocols.icq.packet.sent.meta;

import ru.caffeineim.protocols.icq.RawData;
import ru.caffeineim.protocols.icq.setting.enumerations.MetaTypeEnum;

/**
 * @author Samolisov Pavel
 * @since  25.03.2008
 */
public abstract class RequestUserInfo extends BaseClientMeta {
	
	protected static final int REQUEST_LENGHT = 0x0E00;
	
	/** 
	 * Creates a new instance of RequestShortUserInfo 
	 * 
	 * @param uinSearch ICQ UIN to search
	 * @param uinForRequest for request ICQ UIN
	 * @param subType subtype of meta request
	 */
	public RequestUserInfo(String uinSearch, String uinForRequest, int subType) {
		super(REQUEST_LENGHT, uinForRequest, MetaTypeEnum.CLIENT_ADVANCED_META, subType);
		
		// UIN to search
		RawData rUin = new RawData(Integer.parseInt(uinSearch), RawData.DWORD_LENGHT);
		rUin.invertIndianness();
		tlv.appendRawDataToTlv(rUin);
		
		finalizePacket();
	}
}