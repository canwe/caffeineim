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
package org.caffeineim.test.icq;

import java.util.Observable;
import java.util.Observer;

import ru.caffeineim.protocols.icq.core.OscarConnection;
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
import ru.caffeineim.protocols.icq.integration.listeners.StatusListener;
import ru.caffeineim.protocols.icq.setting.enumerations.StatusModeEnum;
import ru.caffeineim.protocols.icq.tool.OscarInterface;

/**
 * <p>Created by 22.03.2008
 *   @author Samolisov Pavel
 */
public class SampleEventsTest implements MessagingListener, StatusListener, Observer {

	private static final String SERVER = "login.icq.com";
    private static final int PORT = 5190;
    
    private OscarConnection connection;

    public SampleEventsTest(String uin, String password) {
        System.out.println("Login to ICQ server");
    	connection = new OscarConnection(SERVER, PORT, uin, password);
        connection.getPacketAnalyser().setDebug(true);        
               
        // Зарегестрируем класс как слушатель
        connection.addMessagingListener(this);
        connection.addStatusListener(this);
        
        // Уведомим приложение об установленом соединении
        connection.addObserver(this);
    }

    // Метод будет вызван при установке соединения
    public void update(Observable obs, Object obj) {    	
    	OscarInterface.changeStatus(connection, new StatusModeEnum(StatusModeEnum.ONLINE));    	
    	
    	// Запросим сообщения, присланные нам в оффлайн
        OscarInterface.requestOfflineMessages(connection);                
    }
    
    public void onIncomingMessage(IncomingMessageEvent e) {
        System.out.println(e.getSenderID() + " sent : " + e.getMessage());
    }
    
    public void onIncomingUrl(IncomingUrlEvent e) {
        System.out.println(e.getSenderID() + " sent : " + e.getUrl());
    }

    public void onOfflineMessage(OfflineMessageEvent e) {
        System.out.println(e.getSenderUin() + " sent : " + e.getMessage() + " while i was offline");
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
        System.out.println("Message error code " + e.getErrorCode() + " occurred");
    }

    public void onLogout() {
        System.out.println("Logged out (possibly due to error)");
    }
    
    public void onMessageAck(MessageAckEvent e) {
    	System.out.println("MessageAck " + e.getRcptUin());
    }

    public void onStatusChange(StatusEvent e) {
    	System.out.println("StatusEvent: " + e.getStatusMode());
    }
    
	public void onAuthorizationFailed(LoginErrorEvent e) {
		System.out.println("Authorization Failed! You UIN or Password is not valid");
	}
    
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Use : SampleEventsTest MY_UIN MY_PASSWORD");
        } else {
            new SampleEventsTest(args[0], args[1]);
        }
    }
}