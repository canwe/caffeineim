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

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import ru.caffeineim.protocols.icq.core.OscarConnection;
import ru.caffeineim.protocols.icq.integration.events.IncomingUserEvent;
import ru.caffeineim.protocols.icq.integration.events.LoginErrorEvent;
import ru.caffeineim.protocols.icq.integration.events.OffgoingUserEvent;
import ru.caffeineim.protocols.icq.integration.events.StatusEvent;
import ru.caffeineim.protocols.icq.integration.listeners.StatusListener;
import ru.caffeineim.protocols.icq.setting.enumerations.StatusModeEnum;
import ru.caffeineim.protocols.icq.tool.OscarInterface;

/**
 * @author Samolisov Pavel
 * @since  22.06.2008
 */
public class OnLogoutEventTest implements Observer, StatusListener {

	private static final String SERVER = "login.icq.com";
    private static final int PORT = 5190;

    private OscarConnection connection;

    public OnLogoutEventTest(String uin, String password) {
    	connection = new OscarConnection(SERVER, PORT, uin, password);
        connection.getPacketAnalyser().setDebug(true);

        connection.addStatusListener(this);

        // Уведомим приложение об установленом соединении
        connection.addObserver(this);
    }

	public void update(Observable o, Object arg) {
		OscarInterface.changeStatus(connection, new StatusModeEnum(StatusModeEnum.ONLINE));
	}

	public void onAuthorizationFailed(LoginErrorEvent e) {
		// TODO Auto-generated method stub
	}

	public void onIncomingUser(IncomingUserEvent e) {
		// TODO Auto-generated method stub
	}

	public void onLogout() {
		System.out.println("--> On Logout");
		try {
			connection.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void onOffgoingUser(OffgoingUserEvent e) {
		// TODO Auto-generated method stub
	}

	public void onStatusChange(StatusEvent e) {
		System.out.println("--> onStatusChange, status = " + e.getStatusMode());
	}

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Use : OnLogoutEventsTest MY_UIN MY_PASSWORD");
        } else {
            new OnLogoutEventTest(args[0], args[1]);
        }
    }
}
