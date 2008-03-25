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
import ru.caffeineim.protocols.icq.Tlv;
import ru.caffeineim.protocols.icq.core.OscarConnection;
import ru.caffeineim.protocols.icq.integration.events.IncomingUserEvent;
import ru.caffeineim.protocols.icq.integration.listeners.StatusListener;
import ru.caffeineim.protocols.icq.packet.received.ReceivedPacket;
import ru.caffeineim.protocols.icq.setting.enumerations.StatusFlagEnum;
import ru.caffeineim.protocols.icq.setting.enumerations.StatusModeEnum;
import ru.caffeineim.protocols.icq.setting.enumerations.TcpConnectionFlagEnum;

/**
 * <p>Created by
 *   @author Fabrice Michellonet 
 */
public class IncomingUser__3_11 extends ReceivedPacket {

	private RawData userId;
	private RawData internalIp;
	private RawData port;
	private RawData tcpFlag;
	private RawData tcpVersion;
	private RawData cookie;
	private RawData versioning1;
	private RawData versioning2;
	private Tlv externalIp;
	private Tlv userStatus;
	private Tlv capabilities;
	private Tlv onlineSince;
	private Tlv idleTime;
	private Tlv memberSince;
	private Tlv timeUpdate;

	private boolean isContainingCapabilities = false;
	private boolean isConstainingDirectConnectionInformation = false;

	public IncomingUser__3_11(byte[] array) {
		super(array, true);
		int position = 0;
		byte[] data = getSnac().getDataFieldByteArray();

		/* Retreiving user id */
		RawData idLen = new RawData(data, position, 1);
		position++;
		userId = new RawData(data, position, idLen.getValue());
		position += userId.getByteArray().length;

		/* Skipping "Warning Level" */
		position += 2;

		/* Retreiving Number of TLV */
		RawData nbTLV = new RawData(data, position, 2);
		position += 2;

		for (int i = 0; i < nbTLV.getValue(); i++) {
			Tlv tmpTlv = new Tlv(data, position);
			switch (tmpTlv.getType()) {
				case 0x0C:
					/* Retreiving Tlv(0x0C) */
					parseCli2Cli(tmpTlv.getByteArray());
					isConstainingDirectConnectionInformation = true;
					break;

				case 0x0A:
					/* Retreiving external Ip */
					externalIp = tmpTlv;
					break;
        
				case 0x06:
					/* Retreiving user Status */
					userStatus = tmpTlv;
					break;
        
				case 0x0D:
					/* retreiving capabilities' Tlv */
					capabilities = tmpTlv;
					isContainingCapabilities = true;
					break;

				case 0x0F:
					/* retreive client idle time */
					idleTime = tmpTlv;
					break;
				
				case 0x03:
					/* retreive client idle time */
					onlineSince = tmpTlv;
					break;
				
				case 0x05:
					/* retreive client member since time */
					memberSince = tmpTlv;
					break;
        
				case 0x011:
					/* retreive time update */
					timeUpdate = tmpTlv;
					break;
			}
			position += tmpTlv.getByteArray().length;
		}
	}

	public void notifyEvent(OscarConnection connection) {    
		IncomingUserEvent e = new IncomingUserEvent(this);
		for (int i = 0; i < connection.getStatusListeners().size(); i++) {
			StatusListener l = (StatusListener) connection.getStatusListeners().get(i);
			l.onIncomingUser(e);
		}
	}

	private void parseCli2Cli(byte[] data) {
		/* Skipping Tlv type & legth fields */
		int position = 4;

		/* Retreiving internal ip */
		internalIp = new RawData(data, position, 4);
		position += 4;

		/* Retreiving port */
		port = new RawData(data, position, 4);
		position += 4;

		/* retreiving tcp flag */
		tcpFlag = new RawData(data, position, 1);
		position++;

		/* reteiving tcp version */
		tcpVersion = new RawData(data, position, 2);
		position += 2;

		/* Retreiving cookie */
		cookie = new RawData(data, position, 4);
		position += 4;

		/* skipping next 8 byte */
		position += 8;

		/* Retreiving abused time stamp [versioning1] */
		versioning1 = new RawData(data, position, 4);
		position += 4;

		/* Retreiving 2nd abused time stamp [versioning2] */
		versioning2 = new RawData(data, position, 4);
	}

	public boolean getIsContainingCapabilities() {
		return isContainingCapabilities;
	}

	public boolean isConstainingDirectConnectionInformation() {
		return isConstainingDirectConnectionInformation;
	}

	public String getUserId() {
		return userId.getStringValue();
	}

	public String getInterlnalIp() {
		String add = "";
		add += ( (internalIp.getValue() >> 24) & 0xFF) + ".";
		add += ( (internalIp.getValue() >> 16) & 0xFF) + ".";
		add += ( (internalIp.getValue() >> 8) & 0xFF) + ".";
		add += (internalIp.getValue() & 0xFF);
	
		return add;
	}

	public int getPort() {
		return port.getValue();
	}

	public TcpConnectionFlagEnum getTcpFlag() {
		return new TcpConnectionFlagEnum(tcpFlag.getValue());
	}

	public int getTcpVersion() {
		return tcpVersion.getValue();
	}

	public int getCookie() {
		return cookie.getValue();
	}

	public int getVersionning1() {
		return versioning1.getValue();
	}

	public int getVersionning2() {
		return versioning2.getValue();
	}

	public String getExternalIp() {
		String add = "";
		add += ( (externalIp.getValue() >> 24) & 0xFF) + ".";
		add += ( (externalIp.getValue() >> 16) & 0xFF) + ".";
		add += ( (externalIp.getValue() >> 8) & 0xFF) + ".";
		add += (externalIp.getValue() & 0xFF);
		
		return add;
	}

	public StatusModeEnum getStatusMode() {
		/* AIM do not send statuses */
		if (userStatus == null)
			return new StatusModeEnum(StatusModeEnum.ONLINE);
		return new StatusModeEnum(userStatus.getValue() & 0xFFFF);
	}

	public StatusFlagEnum getStatusFlag() {
		/* AIM do not send status */
		if (userStatus == null)
			return new StatusFlagEnum(StatusFlagEnum.DIRECT_CONNECTION_ALLOWED);
		return new StatusFlagEnum(userStatus.getValue());
	}

	public Tlv getCapabilities() {
		return capabilities;
	}

	public int getOnlineSince() {
		return onlineSince.getValue();
	}
}