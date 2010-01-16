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
package ru.caffeineim.test.protocols.icq;

import ru.caffeineim.protocols.icq.core.OscarConnection;
import ru.caffeineim.protocols.icq.integration.OscarInterface;
import ru.caffeineim.protocols.icq.integration.events.IncomingMessageEvent;
import ru.caffeineim.protocols.icq.integration.events.IncomingUrlEvent;
import ru.caffeineim.protocols.icq.integration.events.IncomingUserEvent;
import ru.caffeineim.protocols.icq.integration.events.LoginErrorEvent;
import ru.caffeineim.protocols.icq.integration.events.MessageAckEvent;
import ru.caffeineim.protocols.icq.integration.events.MessageErrorEvent;
import ru.caffeineim.protocols.icq.integration.events.MessageMissedEvent;
import ru.caffeineim.protocols.icq.integration.events.OffgoingUserEvent;
import ru.caffeineim.protocols.icq.integration.events.OfflineMessageEvent;
import ru.caffeineim.protocols.icq.integration.events.StatusEvent;
import ru.caffeineim.protocols.icq.integration.listeners.MessagingListener;
import ru.caffeineim.protocols.icq.integration.listeners.OurStatusListener;
import ru.caffeineim.protocols.icq.integration.listeners.UserStatusListener;
import ru.caffeineim.protocols.icq.setting.enumerations.StatusModeEnum;

/**
 * <p>Created by 22.03.2008
 *   @author Samolisov Pavel
 */
public class SampleEventsTest implements MessagingListener, UserStatusListener, OurStatusListener {

	private static final String SERVER = "login.icq.com";
    private static final int PORT = 5190;

    private OscarConnection connection;

    public SampleEventsTest(String uin, String password) {
        System.out.println("Login to ICQ server");
    	connection = new OscarConnection(SERVER, PORT, uin, password);
        connection.getPacketAnalyser().setDebug(true);
        //connection.getPacketAnalyser().setDump(true);

        // Зарегестрируем класс как слушатель
        connection.addMessagingListener(this);
        connection.addUserStatusListener(this);
        connection.addOurStatusListener(this);

        connection.connect();
    }

    public void onIncomingMessage(IncomingMessageEvent e) {
        System.out.println(e.getSenderID() + " sent : " + e.getMessage());
    }

    public void onIncomingUrl(IncomingUrlEvent e) {
        System.out.println(e.getSenderID() + " sent : " + e.getUrl());
    }

    public void onOfflineMessage(OfflineMessageEvent e) {
        System.out.println(e.getSenderUin() + " sent new offline message");
        System.out.println(" text: " + e.getMessage());
        System.out.println(" date: " + e.getSendDate());
        System.out.println(" type: " + e.getMessageType());
        System.out.println(" flag: " + e.getMessageFlag());
    }

    public void onOffgoingUser(OffgoingUserEvent e) {
        System.out.println(e.getOffgoingUserId() + " went offline.");
    }

    public void onIncomingUser(IncomingUserEvent e) {
        System.out.println(e.getIncomingUserId() + " has just signed on.");
    }

    public void onMessageMissed(MessageMissedEvent e) {
		System.out.println("Message from " + e.getUin() + " can't be recieved because " + e.getReason());
	}

    public void onMessageError(MessageErrorEvent e) {
        System.out.println("Message error code " + e.getError().getCode() + " occurred");
    }

    public void onMessageAck(MessageAckEvent e) {
    	System.out.println("MessageAck " + e.getRcptUin());
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Use : SampleEventsTest MY_UIN MY_PASSWORD");
        } else {
            new SampleEventsTest(args[0], args[1]);
        }
    }

	public void onLogin() {
		OscarInterface.changeStatus(connection, new StatusModeEnum(StatusModeEnum.ONLINE));

    	// Запросим сообщения, присланные нам в оффлайн
        OscarInterface.requestOfflineMessages(connection);
	}

    public void onLogout(Exception e) {
        System.out.println("Logged out (possibly due to error)");
        e.printStackTrace();
        connection.close();
        System.exit(1);
    }

	public void onAuthorizationFailed(LoginErrorEvent e) {
		System.out.println("Authorization Failed! You UIN or Password is not valid");
		connection.close();
		System.exit(1);
	}

	public void onStatusResponse(StatusEvent e) {
		// XXX
	}
}
