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
package ru.caffeineim.protocols.icq.packet.received.meta;

import ru.caffeineim.protocols.icq.RawData;
import ru.caffeineim.protocols.icq.core.OscarConnection;
import ru.caffeineim.protocols.icq.integration.events.MetaInfoEvent;
import ru.caffeineim.protocols.icq.integration.events.OfflineMessageEvent;
import ru.caffeineim.protocols.icq.integration.listeners.MessagingListener;
import ru.caffeineim.protocols.icq.integration.listeners.MetaInfoListener;
import ru.caffeineim.protocols.icq.packet.received.ReceivedPacket;
import ru.caffeineim.protocols.icq.packet.sent.meta.ClientMeta;
import ru.caffeineim.protocols.icq.setting.enumerations.GenderEnum;
import ru.caffeineim.protocols.icq.setting.enumerations.MessageTypeEnum;
import ru.caffeineim.protocols.icq.setting.enumerations.MetaTypeEnum;
import ru.caffeineim.protocols.icq.setting.enumerations.MetaSubTypeEnum;

/**
 * <p>Created by
 *   @author Fabrice Michellonet
 *   @author Samolisov Pavel 
 */
public class ServerMetaReply__21_3 extends ReceivedPacket {

	private RawData metaType;
	private RawData metaSubType;
	private RawData senderUin;
	private RawData year;
	private RawData month;
	private RawData day;
	private RawData hour;
	private RawData minute;
	private RawData messageType;
	private RawData messageFlag;
	private RawData sequence;
	private String message;
	private boolean isOfflineMessage = false;
	private String nick;
	private String firstName;
	private String lastName;
	private String email;
	private boolean auth;
	private GenderEnum gender;

	public ServerMetaReply__21_3(byte[] array) {
		super(array, true);

		int position = 0;
		byte[] data = getSnac().getDataFieldByteArray();

		/* Skipping 4 byte of TLV(1) + length field + my uin */
		position += 10;

		/* Retreiving sub-command */
		metaType = new RawData(data, position, 2);
		position += 2;

		/* Retreiving the sequence */
		sequence = new RawData(data, position, 2);
		position += 2;
		
		switch(metaType.getValue()) {
			case MetaTypeEnum.SERVER_OFFLINE_MESSAGE:
				parseOfflineMessage(data, position);
				break;							
			case MetaTypeEnum.SERVER_ADVANCED_META:				
				metaSubType = new RawData(data, position, 2);
				metaSubType.invertIndianness();
				position += 2;
				switch(metaSubType.getValue()) {
					case MetaSubTypeEnum.SERVER_USER_FOUND:
					case MetaSubTypeEnum.SERVER_LAST_USER_FOUND:
						parseUserFound(data, position);
						break;
					case MetaSubTypeEnum.SERVER_SHORT_USER_INFO_REPLY:
						parseShortUserInfo(data, position);
				}
				break;
		}
	}

	public void execute(OscarConnection connection) {
		switch(metaType.getValue()) {
	  		case MetaTypeEnum.SERVER_END_OF_OFFLINE_MESSAGES:
	  			connection.sendFlap(ClientMeta.ackOfflineMessages(connection));
	  			break;
		}
	}

	public void notifyEvent(OscarConnection connection) {
		switch(getMetaType().getType()) {
			case MetaTypeEnum.SERVER_OFFLINE_MESSAGE:
				notifyOfflineMessage(connection);
				break;
			case MetaTypeEnum.SERVER_ADVANCED_META:
				notifyMetaInfo(connection);
				break;
		}
	}

	private void notifyOfflineMessage(OscarConnection connection) {
		OfflineMessageEvent e = new OfflineMessageEvent(this);
		for (int i = 0; i < connection.getMessagingListeners().size(); i++) {
			MessagingListener l = (MessagingListener) connection.getMessagingListeners().get(i);
			l.onOfflineMessage(e);
		}
	}
  
	private void notifyMetaInfo(OscarConnection connection) {
		MetaInfoEvent e = new MetaInfoEvent(this);
		for (int i = 0; i < connection.getMetaInfoListeners().size(); i++) {
			MetaInfoListener l = (MetaInfoListener) connection.getMetaInfoListeners().get(i);            		
			l.updateContactMetaInfo(e);
		}		
	}

	private void parseOfflineMessage(byte[] data, int position) {
		/* Retreiving sender uin */
		senderUin = new RawData(data, position, 4);
		senderUin.invertIndianness();
		position += 4;

		/* Retreiving year */
		year = new RawData(data, position, 2);
		year.invertIndianness();
		position += 2;

		/* Retreiving month */
		month = new RawData(data, position, 1);
		position++;

		/* Retreiving day */
		day = new RawData(data, position, 1);
		position++;

		/* Retreiving hour */
		hour = new RawData(data, position, 1);
		position++;

		/* Retreiving minute */
		minute = new RawData(data, position, 1);
		position++;

		/* Retreiving message type */
		messageType = new RawData(data, position, 1);
		position++;

		/* Retreiving message flag */
		messageFlag = new RawData(data, position, 1);
		position++;

		/* Retreiving message length */
		RawData msgLen = new RawData(data, position, 2);
		msgLen.invertIndianness();
		position += 2;

		/* Retreiving message */
		message = new String(data, position, msgLen.getValue() - 1);
		isOfflineMessage = true;
	}

  	private void parseUserFound(byte[] data, int position) {
		position += 3; // skip success byte (always 0x0A) and data size.
		
		/* Found UIN */
		senderUin = new RawData(data, position, RawData.DWORD_LENGHT);
		senderUin.invertIndianness();
		position += 4;
		
		/* Nickname */
		RawData rStrLen = new RawData(data, position, RawData.WORD_LENGHT);
		rStrLen.invertIndianness();
		position += 2;
		
		nick = (new RawData(data, position, rStrLen.getValue() - 1)).getStringValue();
		position += rStrLen.getValue();
		
		/* First Name */
		rStrLen = new RawData(data, position, RawData.WORD_LENGHT);
		rStrLen.invertIndianness();
		position += 2;
		
		firstName = (new RawData(data, position, rStrLen.getValue() - 1)).getStringValue();
		position += rStrLen.getValue();
		
		/* Last Name */
		rStrLen = new RawData(data, position, RawData.WORD_LENGHT);
		rStrLen.invertIndianness();
		position += 2;
		
		lastName = (new RawData(data, position, rStrLen.getValue() - 1)).getStringValue();
		position += rStrLen.getValue();
		
		/* email */
		rStrLen = new RawData(data, position, RawData.WORD_LENGHT);
		rStrLen.invertIndianness();
		position += 2;
		
		email = (new RawData(data, position, rStrLen.getValue() - 1)).getStringValue();
		position += rStrLen.getValue();
	}
  	
  	private void parseShortUserInfo(byte[] data, int position) {
		position += 1; // skip success byte (always 0x0A) and data size.
				
		// Nickname lenght
		RawData rStrLen = new RawData(data, position, RawData.WORD_LENGHT);
		rStrLen.invertIndianness();
		position += 2;
		
		// Nickname
		nick = (new RawData(data, position, rStrLen.getValue() - 1)).getStringValue();
		position += rStrLen.getValue();
		
		// First Name lenght
		rStrLen = new RawData(data, position, RawData.WORD_LENGHT);
		rStrLen.invertIndianness();
		position += 2;
		
		// First Name
		firstName = (new RawData(data, position, rStrLen.getValue() - 1)).getStringValue();
		position += rStrLen.getValue();
		
		// Last Name lenght
		rStrLen = new RawData(data, position, RawData.WORD_LENGHT);
		rStrLen.invertIndianness();
		position += 2;
		
		// Last Name
		lastName = (new RawData(data, position, rStrLen.getValue() - 1)).getStringValue();
		position += rStrLen.getValue();
		
		// Email lenght 
		rStrLen = new RawData(data, position, RawData.WORD_LENGHT);
		rStrLen.invertIndianness();
		position += 2;
		
		// Email
		email = (new RawData(data, position, rStrLen.getValue() - 1)).getStringValue();
		position += rStrLen.getValue();		
		
		// Auth Flag
		RawData authdata = new RawData(data, position, RawData.BYTE_LENGHT);
		auth = authdata.getValue() == 0;
		position += 1;
		
		// unknown
		position += 1;
		
		// Gender
		RawData genderdata = new RawData(data, position, RawData.BYTE_LENGHT);
		System.out.println("Gender = " + genderdata.getValue());
		
		// TODO Дописать реализацию разбора Gender
	}
	
  	public MetaTypeEnum getMetaType() {
  		return new MetaTypeEnum(metaType.getValue());
  	}
  	
  	public MetaSubTypeEnum getMetaSubType() {
  		return new MetaSubTypeEnum(metaSubType.getValue());
  	}

  	public String getSenderUin() {
  		return senderUin.getStringValue();
  	}

  	public int getYear() {
  		return year.getValue();
  	}

  	public int getMonth() {
  		return month.getValue();
  	}

  	public int getDay() {
  		return day.getValue();
  	}

  	public int getHour() {
  		return hour.getValue();
  	}

  	public int getMinute() {
  		return minute.getValue();
  	}

  	public MessageTypeEnum getMessageType() {
  		return new MessageTypeEnum(messageType.getValue());
  	}

  	/**
  	 * This returns the message that has been sent offline.
  	 * <p>
  	 * <i> The message should be a null terminated string, but Gaim's implementation of offline messages is buggy;<br/>
  	 * It do not send the null terminator.<br/>
  	 * Therefor, we must not rely on this indicator.
  	 * </i>
  	 * </p
  	 *
  	 * @return The offline message.
  	 */
  	public String getMessage() {
  		int nullPos = message.indexOf(0x00);
  		/* Gaim bug */
  		if (nullPos != -1)
  			return message.substring(0, nullPos);
  		else
  			return message;
  	}

  	public boolean getIsOfflineMessage() {
  		return isOfflineMessage;
  	}
  
  	public String getNickName() {
  		return nick;
  	}

  	public String getFirstName() {
  		return firstName;
  	}

  	public String getLastName() {
  		return lastName;
  	}
  	
  	public String getEmail() {
  		return email;
  	}
  	
  	public boolean getAuthFlag() {
  		return auth;
  	}
  	
  	public GenderEnum getGender() {
  		return gender; //
  	}
}