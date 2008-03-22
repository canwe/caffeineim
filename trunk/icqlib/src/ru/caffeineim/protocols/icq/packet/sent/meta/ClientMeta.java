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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import ru.caffeineim.protocols.icq.Flap;
import ru.caffeineim.protocols.icq.RawData;
import ru.caffeineim.protocols.icq.Snac;
import ru.caffeineim.protocols.icq.Tlv;
import ru.caffeineim.protocols.icq.core.OscarConnection;
import ru.caffeineim.protocols.icq.setting.enumerations.MetaTypeEnum;
import ru.caffeineim.protocols.icq.setting.enumerations.MetaSubTypeEnum;

/**
 * <p>Created by
 *   @author Fabrice Michellonet
 *   @author Samolisov Pavel
 */
public class ClientMeta extends Flap {
	// TODO жесткий рефакторинг работы с метаинфой
	private static final byte[] unknownSMSField = new byte[] {
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
	};
	
	private static final int REQUEST_ACK_LENGHT = 0x0800;
	
	private static final int SMS_LENGHT = 0x0129;

	private Tlv tlv1;

	private ClientMeta(String userId, int lenght, MetaTypeEnum subCommand) {
		super(2);
		/* Creating TLV and Adding length */
		tlv1 = new Tlv(new RawData(lenght, RawData.WORD_LENGHT), 0x01);
		/* Converting UIN to little endian */
		RawData rUin = new RawData(Integer.parseInt(userId), RawData.DWORD_LENGHT);
		rUin.invertIndianness();
		/* Adding uin */
		tlv1.appendRawDataToTlv(rUin);
		/* Adding sub-Command */
		tlv1.appendRawDataToTlv(new RawData(subCommand.getType(), RawData.WORD_LENGHT));
		/* Adding sequence-id */
		tlv1.appendRawDataToTlv(new RawData(0x00, RawData.WORD_LENGHT));
	}

	private ClientMeta(String userId, int lenght, MetaTypeEnum subCommand, MetaSubTypeEnum subType) {
		this(userId, lenght, subCommand);
		/* Adding sub-type */
		RawData rdSubType = new RawData(subType.getSubType());
		rdSubType.invertIndianness();
		tlv1.appendRawDataToTlv(rdSubType);
	}

	private static ClientMeta finalizePacket(ClientMeta meta, Tlv tlv1) {
		Snac snac = new Snac(0x15, 0x02, 0x0, 0x0, 0x00);
		snac.addTlvToSnac(tlv1);
		meta.addSnac(snac);
		
		return meta;
	}

	public static ClientMeta requestOfflineMessages(OscarConnection connection) {		
		ClientMeta meta = new ClientMeta(connection.getUserId(), 
					REQUEST_ACK_LENGHT,
					new MetaTypeEnum(MetaTypeEnum.CLIENT_REQUEST_OFFLINE_MESSAGES));
    
		finalizePacket(meta, meta.tlv1);
		
		return meta;		
	}
	
	public static ClientMeta requestClientMeta(OscarConnection connection) {		
		ClientMeta meta = new ClientMeta(connection.getUserId(), 
					REQUEST_ACK_LENGHT,
					new MetaTypeEnum(MetaTypeEnum.CLIENT_ADVANCED_META));
    
		finalizePacket(meta, meta.tlv1);
		
		return meta;		
	}

	public static ClientMeta ackOfflineMessages(OscarConnection connection) {
		ClientMeta meta = new ClientMeta(connection.getUserId(),
					REQUEST_ACK_LENGHT,
                    new MetaTypeEnum(MetaTypeEnum.CLIENT_ACK_OFFLINE_MESSAGES));

		finalizePacket(meta, meta.tlv1);
		
		return meta;
	}

	public static ClientMeta sendSMS(OscarConnection connection, String cellNumber, String message) {
		int totalLenght = SMS_LENGHT + cellNumber.length() + message.length() +
        			connection.getUserId().length() * 2;

		RawData rawLenght = new RawData(totalLenght, RawData.WORD_LENGHT);
		rawLenght.invertIndianness();
		totalLenght = rawLenght.getValue();

		ClientMeta meta = new ClientMeta(connection.getUserId(),
                      totalLenght,
                      new MetaTypeEnum(MetaTypeEnum.CLIENT_ADVANCED_META),
                      new MetaSubTypeEnum(MetaSubTypeEnum.CLIENT_SEND_SMS));

		/* Unknown */
		meta.tlv1.appendRawDataToTlv(new RawData(0x01, RawData.WORD_LENGHT));
		/* Unknown */
		meta.tlv1.appendRawDataToTlv(new RawData(0x16, RawData.WORD_LENGHT));
		/* Unknown */
		meta.tlv1.appendRawDataToTlv(new RawData(unknownSMSField));

		/* creating Current time */
		// JD 1.3 Compatibility thanks to Leidson Campos.
		SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z",
					new Locale("EN", Locale.getDefault().getCountry()));
		dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
		String time = dateFormatter.format(new Date());

		/* Creating XML content */
		String smsMessage = new String();
		smsMessage += "<icq_sms_message>";
		smsMessage += "<destination>" + cellNumber + "</destination>";
		smsMessage += "<text>" + message + "</text>";
		smsMessage += "<codepage>1252</codepage>";
		smsMessage += "<encoding>utf8</encoding>";
		smsMessage += "<senders_UIN>" + connection.getUserId() + "</senders_UIN>";
		smsMessage += "<senders_name>" + connection.getUserId() + "</senders_name>";
		smsMessage += "<delivery_receipt>Yes</delivery_receipt>";
		smsMessage += "<time>" + time + "</time>";
		smsMessage += "</icq_sms_message>";

		Tlv tlvMessage = new Tlv(smsMessage, 0x00);
		/* Adding message */
		meta.tlv1.appendTlvToTlv(tlvMessage);

		finalizePacket(meta, meta.tlv1);
		
		return meta;
	}
  
	public static ClientMeta searchByUinTlv(OscarConnection connection, String uin) {
		ClientMeta meta = new ClientMeta(connection.getUserId(),
				0x1200,
				new MetaTypeEnum(MetaTypeEnum.CLIENT_ADVANCED_META),
				new MetaSubTypeEnum(MetaSubTypeEnum.SEARCH_BY_UIN_TLV));
	  
		meta.tlv1.appendRawDataToTlv(new RawData(0x3601, RawData.WORD_LENGHT)); // TLV 0x0136
		meta.tlv1.appendRawDataToTlv(new RawData(0x0400, RawData.WORD_LENGHT)); // Length 4
		RawData rUin = new RawData(Integer.parseInt(uin), RawData.DWORD_LENGHT);
		rUin.invertIndianness();
		meta.tlv1.appendRawDataToTlv(rUin);
		
		finalizePacket(meta, meta.tlv1);
		
		return meta;
	}
}