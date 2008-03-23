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
package ru.caffeineim.protocols.icq.packet.received.icbm;

import ru.caffeineim.protocols.icq.RawData;
import ru.caffeineim.protocols.icq.Tlv;
import ru.caffeineim.protocols.icq.core.OscarConnection;
import ru.caffeineim.protocols.icq.packet.received.ReceivedPacket;

/**
 * <p>Created by
 *   @author Lo�c Broquet 
 */
public class MissedMessage__4_10 extends ReceivedPacket {
	
	private Tlv usrClass;
	private Tlv usrStatus;
	private Tlv clientOnlineTime;
	private Tlv offlineTime;
	
	private RawData uin;
	private RawData reason;
	
	public static final short MESSAGE_INVALID       = 0;
	public static final short MESSAGE_TOO_LARGE     = 1;
	public static final short MESSAGE_RATE_EXCEEDED = 2;
	public static final short SENDER_TOO_EVIL       = 3;
	public static final short USER_TOO_EVIL         = 4;

	/** 
	 * Creates a new instance of MissedMessage__4_10 
	 */
	public MissedMessage__4_10(byte[] array) {
		super(array, true);
		
		byte data[] = getSnac().getDataFieldByteArray();
		int index = 0;
		
		do {
			// TODO разобраться и починить
/*			RawData type = new RawData(data, index, RawData.WORD_LENGHT);
			index += RawData.WORD_LENGHT;
			RawData uinLg = new RawData(data, index, RawData.BYTE_LENGHT);
			index += RawData.BYTE_LENGHT;
			uin = new RawData(data, index, uinLg.getValue());
			index += uinLg.getValue();
			RawData warningLvl = new RawData(data, index, RawData.WORD_LENGHT);
			index += RawData.WORD_LENGHT;
			RawData nbOfTlv = new RawData(data, index, RawData.WORD_LENGHT); // Should de 4
			index += RawData.WORD_LENGHT;

			usrClass = new Tlv(array, index);		
			index += usrClass.getLength();
			usrStatus = new Tlv(array, index);
			index += usrStatus.getLength();
			clientOnlineTime = new Tlv(array, index);
			index += clientOnlineTime.getLength();
			offlineTime = new Tlv(array, index);
			index += offlineTime.getLength();

			RawData numberOfMisedMsg = new RawData(data, index, RawData.WORD_LENGHT);
			index += RawData.WORD_LENGHT;
			reason = new RawData(data, index, RawData.WORD_LENGHT);
			index += RawData.WORD_LENGHT;*/
		}
		while(index < array.length);
	}
	
	public void execute(OscarConnection connection) throws Exception {		
	}
	
	public void notifyEvent(OscarConnection connection) {
		System.out.print("Message from "+uin.getStringValue()+" can't be recieved because ");
		switch(reason.getValue()) {
			case MESSAGE_INVALID:
				System.out.println("message invalid");
				break;
			case MESSAGE_TOO_LARGE:
				System.out.println("message too large");
				break;
			case MESSAGE_RATE_EXCEEDED:
				System.out.println("message rate exceeded");
				break;
			case SENDER_TOO_EVIL:
				System.out.println("sender too evil");
				break;
			case USER_TOO_EVIL:
				System.out.println("you are too evil");
				break;
			default:
				System.out.println("of unknown reason "+reason.getValue());
		}
	}
}
