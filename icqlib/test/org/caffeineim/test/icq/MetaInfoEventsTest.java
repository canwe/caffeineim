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
import ru.caffeineim.protocols.icq.integration.events.MetaShortUserInfoEvent;
import ru.caffeineim.protocols.icq.integration.events.UINRegistrationFailedEvent;
import ru.caffeineim.protocols.icq.integration.events.UINRegistrationSuccessEvent;
import ru.caffeineim.protocols.icq.integration.listeners.MetaInfoListener;
import ru.caffeineim.protocols.icq.packet.sent.meta.RequestShortUserInfo;

/**
 * <p>Created by 22.03.2008
 *   @author Samolisov Pavel
 */
public class MetaInfoEventsTest implements MetaInfoListener, Observer {
	
	private static final String SERVER = "login.icq.com";
    private static final int PORT = 5190;
    
    private static final String UIN1 = "213536590";
    private static final String UIN2 = "205717272";
    
    private OscarConnection connection;

    public MetaInfoEventsTest(String uin, String password) {       
    	connection = new OscarConnection(SERVER, PORT, uin, password);
        connection.getPacketAnalyser().setDebug(true);        
                        
        connection.addMetaInfoListener(this);
                
        connection.addObserver(this);
    }
    

    public void update(Observable obs, Object obj) {
    	System.out.println("Send meta request");
    	connection.sendFlap(new RequestShortUserInfo(UIN1, connection.getUserId()));
    	connection.sendFlap(new RequestShortUserInfo(UIN2, connection.getUserId()));
    }
    
	public void onShortUserInfo(MetaShortUserInfoEvent e) {
		System.out.println("Short User Info: ");
    	System.out.println("  Nick Name = "  + e.getNickName());
    	System.out.println("  First Name = " + e.getFirstName());
    	System.out.println("  Last Name = "  + e.getLastName());
    	System.out.println("  Email = "      + e.getEmail());
    	System.out.println("  Auth = "       + e.getAuthFlag());		
	}    
    
    public void registerNewUINSuccess(UINRegistrationSuccessEvent event)
    {
        
    }
    
    public void registerNewUINFailed(UINRegistrationFailedEvent event)
    {
        
    }
        
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Use : MetaInfoEventsTest MY_UIN MY_PASSWORD");
        } else {
            new MetaInfoEventsTest(args[0], args[1]);
        }
    }
}