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

import ru.caffeineim.protocols.icq.Flap;
import ru.caffeineim.protocols.icq.RawData;
import ru.caffeineim.protocols.icq.Snac;
import ru.caffeineim.protocols.icq.Tlv;
import ru.caffeineim.protocols.icq.setting.enumerations.MetaTypeEnum;

/**
 * <p>Created by 25.03.2008
 *   @author Samolisov Pavel
 */
public abstract class RequestUserInfo extends Flap {
	
	protected static final int REQUEST_LENGHT = 0x0E00;
	
	/** 
	 * Creates a new instance of RequestShortUserInfo 
	 * 
	 * @param uinSearch ICQ UIN to search
	 * @param uinForRequest for request ICQ UIN
	 * @param subType subtype of meta request
	 */
	public RequestUserInfo(String uinSearch, String uinForRequest, int subType) {
		super(2);
		Snac snac = new Snac(0x15, 0x02, 0x00, 0x00, 0x00);	
				
		// Creating TLV 
		Tlv tlv = new Tlv(0x01);
		
		// Chunk size
		tlv.appendRawDataToTlv(new RawData(REQUEST_LENGHT, RawData.WORD_LENGHT));
		
		// Converting UIN to little endian 
		RawData rUin = new RawData(Integer.parseInt(uinForRequest), RawData.DWORD_LENGHT);
		rUin.invertIndianness();
		
		// request owner UIN
		tlv.appendRawDataToTlv(rUin);
		
		// Request Type
		tlv.appendRawDataToTlv(new RawData(MetaTypeEnum.CLIENT_ADVANCED_META, RawData.WORD_LENGHT));
		
		// Adding sequence-id 
		tlv.appendRawDataToTlv(new RawData(0x0200, RawData.WORD_LENGHT));
		
		// Request SubType
		tlv.appendRawDataToTlv(new RawData(subType, RawData.WORD_LENGHT));
		
		// UIN to search
		rUin = new RawData(Integer.parseInt(uinSearch), RawData.DWORD_LENGHT);
		rUin.invertIndianness();
		tlv.appendRawDataToTlv(rUin);
		
		snac.addTlvToSnac(tlv);
		
		addSnac(snac);		
	}
}