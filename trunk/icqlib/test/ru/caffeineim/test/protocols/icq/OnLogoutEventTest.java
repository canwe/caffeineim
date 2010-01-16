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
import ru.caffeineim.protocols.icq.integration.events.IncomingUserEvent;
import ru.caffeineim.protocols.icq.integration.events.LoginErrorEvent;
import ru.caffeineim.protocols.icq.integration.events.OffgoingUserEvent;
import ru.caffeineim.protocols.icq.integration.events.StatusEvent;
import ru.caffeineim.protocols.icq.integration.listeners.OurStatusListener;
import ru.caffeineim.protocols.icq.integration.listeners.UserStatusListener;
import ru.caffeineim.protocols.icq.setting.enumerations.StatusModeEnum;

/**
 * <p>Created by 22.06.2008
 *   @author Samolisov Pavel
 */
public class OnLogoutEventTest implements OurStatusListener, UserStatusListener {

	private static final String SERVER = "login.icq.com";
    private static final int PORT = 5190;

    private OscarConnection connection;

    public OnLogoutEventTest(String uin, String password) {
    	connection = new OscarConnection(SERVER, PORT, uin, password);
        connection.getPacketAnalyser().setDebug(true);

        connection.addUserStatusListener(this);
        connection.addOurStatusListener(this);

        connection.connect();
    }

	public void onIncomingUser(IncomingUserEvent e) {
		// TODO Auto-generated method stub
	}

	public void onOffgoingUser(OffgoingUserEvent e) {
		// TODO Auto-generated method stub
	}

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Use : OnLogoutEventsTest MY_UIN MY_PASSWORD");
        } else {
            new OnLogoutEventTest(args[0], args[1]);
        }
    }

	public void onLogin() {
		OscarInterface.changeStatus(connection, new StatusModeEnum(StatusModeEnum.ONLINE));
	}

	public void onLogout(Exception e) {
		e.printStackTrace();
		connection.close();
		System.exit(1);
	}

	public void onAuthorizationFailed(LoginErrorEvent e) {
		connection.close();
		System.exit(1);
	}

	public void onStatusResponse(StatusEvent e) {
		System.out.println("--> onStatusResponse, status = " + e.getStatusMode());
	}
}
