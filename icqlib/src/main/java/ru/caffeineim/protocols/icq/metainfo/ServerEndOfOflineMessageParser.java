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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.caffeineim.protocols.icq.core.OscarConnection;
import ru.caffeineim.protocols.icq.exceptions.ConvertStringException;
import ru.caffeineim.protocols.icq.packet.sent.meta.AckOfflineMessages;

/**
 * <p>Created by 24.03.2008
 *   @author Samolisov Pavel
 */
public class ServerEndOfOflineMessageParser implements IMetaInfoParser {

	private static Log log = LogFactory.getLog(ServerEndOfOflineMessageParser.class);

	public void execute(OscarConnection connection) {
		connection.sendFlap(new AckOfflineMessages(connection.getUserId()));
	}


	public void notifyEvent(OscarConnection connection) {
		log.debug("EOF Ofline message has been received");
	}


	public void parse(byte[] data, int position) throws ConvertStringException {
	}
}
