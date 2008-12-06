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

import java.util.Observable;
import java.util.Observer;

import ru.caffeineim.protocols.icq.core.OscarConnection;
import ru.caffeineim.protocols.icq.exceptions.ConvertStringException;
import ru.caffeineim.protocols.icq.integration.events.IncomingMessageEvent;
import ru.caffeineim.protocols.icq.integration.events.IncomingUrlEvent;
import ru.caffeineim.protocols.icq.integration.events.LoginErrorEvent;
import ru.caffeineim.protocols.icq.integration.events.MessageAckEvent;
import ru.caffeineim.protocols.icq.integration.events.MessageErrorEvent;
import ru.caffeineim.protocols.icq.integration.events.MessageMissedEvent;
import ru.caffeineim.protocols.icq.integration.events.OfflineMessageEvent;
import ru.caffeineim.protocols.icq.integration.events.XStatusRequestEvent;
import ru.caffeineim.protocols.icq.integration.events.XStatusResponseEvent;
import ru.caffeineim.protocols.icq.integration.listeners.MessagingListener;
import ru.caffeineim.protocols.icq.integration.listeners.XStatusListener;
import ru.caffeineim.protocols.icq.setting.enumerations.StatusModeEnum;
import ru.caffeineim.protocols.icq.setting.enumerations.XStatusModeEnum;
import ru.caffeineim.protocols.icq.tool.OscarInterface;

/**
 * <p>Created by 22.03.2008
 *   @author Samolisov Pavel
 */
public class XStatusEventsTest  implements MessagingListener, XStatusListener, Observer {
	
	private static final String SERVER = "login.icq.com";
    private static final int PORT = 5190;
    
    private static final String XSTATUS_BASE_STRING = "Я люблю";
    private static final String XSTATUS_EXT_STRING = "Веру";
    
    private OscarConnection connection;

    public XStatusEventsTest(String uin, String password) {       
    	connection = new OscarConnection(SERVER, PORT, uin, password);
		connection.getPacketAnalyser().setDebug(true);		
               
        connection.addMessagingListener(this);
        connection.addXStatusListener(this);
        
        connection.addObserver(this);            
    }
        
    public void update(Observable obs, Object obj) {
    	// Устанавливаем себе расширенный статус    	
    	OscarInterface.changeStatus(connection, new StatusModeEnum(StatusModeEnum.ONLINE));
    	OscarInterface.changeXStatus(connection, new XStatusModeEnum(XStatusModeEnum.LOVE));
    }
    
    public void onAuthorizationFailed(LoginErrorEvent e) {
    	System.out.println("Authorization Failed! You UIN or Password is not valid");    
    }   
    
    public void onIncomingMessage(IncomingMessageEvent e) {
        // В ответ на входящее сообщение запросим расширенный статус пославшего
    	System.out.println(e.getSenderID() + " sent : " + e.getMessage());
        OscarInterface.sendXStatusRequest(connection, e.getSenderID());    	
    }
    
    public void onIncomingUrl(IncomingUrlEvent e) {
        System.out.println(e.getSenderID() + " sent : " + e.getUrl());        
    }

    public void onOfflineMessage(OfflineMessageEvent e) {
        System.out.println(e.getSenderUin() + " sent : " + e.getMessage() + " while i was offline");
    }
    
    public void onMessageMissed(MessageMissedEvent e) {
		System.out.println("Message from " + e.getUin() + " can't be recieved because " + e.getReason());
	}
    
    public void onMessageError(MessageErrorEvent e) {
        System.out.println("Message error code " + e.getErrorCode() + " occurred");        
    }
    
    public void onMessageAck(MessageAckEvent e) {
    	System.out.println("Message Ack " + e.getMessageType() + " " + e.getRcptUin());
    }
    
    public void onXStatusRequest(XStatusRequestEvent e) {
    	// Посылаем свой статус, если просят
    	try {
    		OscarInterface.sendXStatus(connection, new XStatusModeEnum(XStatusModeEnum.LOVE), 
    				XSTATUS_BASE_STRING, XSTATUS_EXT_STRING, e.getTime(), e.getMsgID(), e.getSenderID(), e.getSenderTcpVersion());
    	}
    	catch(ConvertStringException ex) {
    		System.out.println(ex.getMessage());
    	}
    }
    
    public void onXStatusChange(XStatusResponseEvent e) {
    	// Если человек присылает статус - пошлем ему его собственный статус как сообщение
    	// Да, да, вот такие мы коварные
    	try {    		
    		OscarInterface.sendExtendedMessage(connection, e.getSenderID(), "Title = " + e.getTitle() + " Description = " + e.getDescription() + " XStatus = " + e.getXStatus().toString());    		
    	}
    	catch (ConvertStringException ex) {
    		System.out.println(ex.getMessage());
		}
    }
       
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Use : XStatusEventsTest MY_UIN MY_PASSWORD");
        } else {
            new XStatusEventsTest(args[0], args[1]);
        }
    }
}