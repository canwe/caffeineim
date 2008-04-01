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
package ru.caffeineim.protocols.icq.tool;

import ru.caffeineim.protocols.icq.core.OscarConnection;
import ru.caffeineim.protocols.icq.exceptions.ConvertStringException;
import ru.caffeineim.protocols.icq.packet.sent.generic.SetICQStatus;
import ru.caffeineim.protocols.icq.packet.sent.generic.SetIdleTime;
import ru.caffeineim.protocols.icq.packet.sent.icbm.SendType1Message;
import ru.caffeineim.protocols.icq.packet.sent.icbm.SendType2Message;
import ru.caffeineim.protocols.icq.packet.sent.icbm.SendXStatus;
import ru.caffeineim.protocols.icq.packet.sent.icbm.XStatusRequest;
import ru.caffeineim.protocols.icq.packet.sent.location.SetLocationInformation;
import ru.caffeineim.protocols.icq.packet.sent.meta.ChangePassword;
import ru.caffeineim.protocols.icq.packet.sent.meta.RequestFullUserInfo;
import ru.caffeineim.protocols.icq.packet.sent.meta.RequestOfflineMessages;
import ru.caffeineim.protocols.icq.packet.sent.meta.RequestShortUserInfo;
import ru.caffeineim.protocols.icq.setting.enumerations.IdleTimeEnum;
import ru.caffeineim.protocols.icq.setting.enumerations.StatusModeEnum;
import ru.caffeineim.protocols.icq.setting.enumerations.XStatusModeEnum;

/**
 * <p>Created by
 *   @author Fabrice Michellonet
 */
public class OscarInterface {

	/**
	 * Отправляем сообщение по 1-му каналу
	 * 
	 * @param connection соединение
	 * @param userId UIN получателя
	 * @param message сообщение
	 * @throws ConvertStringException 
	 */
	public static void sendBasicMessage(OscarConnection connection, String userId, String message) 
			throws ConvertStringException {
		connection.sendFlap(new SendType1Message(userId, message));
	}

	/**
	 * Отправляем сообщение по 2-му каналу
	 * 
	 * @param connection соединение
	 * @param userId UIN получателя
	 * @param message сообщение
	 * @throws ConvertStringException 
	 */
	public static void sendExtendedMessage(OscarConnection connection, String userId, String message) 
			throws ConvertStringException {
		// WARNING: Extended Messages will NOT be delivered to offline contacts
		// and on AIM, only Basic Messages
		connection.sendFlap(new SendType2Message(userId, message));
	}

	/**
	 * Смена статуса
	 * 
	 * @param connection соединение
	 * @param newStatus новый статус
	 */
	public static void changeStatus(OscarConnection connection, StatusModeEnum newStatus) {
		connection.sendFlap(new SetICQStatus(newStatus, connection.getTweaker()
				.getInitialStatusFlags(), connection.getTweaker()
				.getTcpConnectionFlag(), connection.getClient()
				.getInetaddress(), connection.getTweaker()
				.getP2PPortListening()));
	}

	/**
	 * Смена X-статуса
	 * 
	 * @param connection соедиенение
	 * @param newStatus новый статус
	 */
	public static void changeXStatus(OscarConnection connection, XStatusModeEnum newStatus) {
		connection.sendFlap(new SetLocationInformation(newStatus));
	}

	/**
	 * Посылаем XStatuc в ответ на запрос
	 * 
	 * @param connection соединение
	 * @param xstatus наш XStatus
	 * @param title титл статуса
	 * @param description подробное описание статуса
	 * @param time время запроса
	 * @param msgId Id сообщение с запросом
	 * @param userId Id пользователя запросившего статус
	 * @param tcpVersion версия TCP протокола
	 * @throws ConvertStringException 
	 */
	public static void sendXStatus(OscarConnection connection, XStatusModeEnum xstatus, String title, 
			String description, int time, int msgId, String userId, int tcpVersion) throws ConvertStringException {
		connection.sendFlap(new SendXStatus(time, msgId, userId, connection.getUserId(), tcpVersion, xstatus, title, description));
	}
	
	/**
	 * Посылаем запрос на XStatus
	 * 
	 * @param connection соединение
	 * @param userId UIN пользователя
	 */
	public static void sendXStatusRequest(OscarConnection connection, String userId) {
		connection.sendFlap(new XStatusRequest(userId, connection.getUserId()));
	}

	/**
	 * Смена времени бездействия
	 * 
	 * @param connection соединение
	 * @param idleTimeMode режим времени
	 */
	public static void setIdleTime(OscarConnection connection, IdleTimeEnum idleTimeMode) {
		connection.sendFlap(new SetIdleTime(idleTimeMode));
	}

	/**
	 * Запрос offline-сообщений
	 * 
	 * @param connection соединение
	 */
	public static void requestOfflineMessages(OscarConnection connection) {				
		connection.sendFlap(new RequestOfflineMessages(connection.getUserId()));		
	}
	
	/**
	 * Запрос короткой информации о пользователе
	 * 
	 * @param connection соединение
	 * @param uin UIN пользователя, о котором запрашиваем информацию
	 */
	public static void requestShortUserInfo(OscarConnection connection, String uin) {
		connection.sendFlap(new RequestShortUserInfo(uin, connection.getUserId()));
	}
	
	/**
	 * Запрос полной информации о пользователе
	 * 
	 * @param connection соединение
	 * @param uin UIN пользователя о котором запрашиваем информацию
	 */
	public static void requestFullUserInfo(OscarConnection connection, String uin) {
		connection.sendFlap(new RequestFullUserInfo(uin, connection.getUserId()));
	}
		
	/**
	 * Смена пароля пользователем
	 * 
	 * @param connection соединение
	 * @param newPassword новый пароль пользователя
	 * @throws ConvertStringException
	 */
	public static void changePassword(OscarConnection connection, String newPassword) throws ConvertStringException {
		connection.sendFlap(new ChangePassword(connection.getUserId(), newPassword));
	}
}