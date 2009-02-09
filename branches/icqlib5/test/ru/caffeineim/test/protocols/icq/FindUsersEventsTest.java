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
import ru.caffeineim.protocols.icq.integration.events.MetaAffilationsUserInfoEvent;
import ru.caffeineim.protocols.icq.integration.events.MetaBasicUserInfoEvent;
import ru.caffeineim.protocols.icq.integration.events.MetaEmailUserInfoEvent;
import ru.caffeineim.protocols.icq.integration.events.MetaInterestsUserInfoEvent;
import ru.caffeineim.protocols.icq.integration.events.MetaMoreUserInfoEvent;
import ru.caffeineim.protocols.icq.integration.events.MetaNoteUserInfoEvent;
import ru.caffeineim.protocols.icq.integration.events.MetaShortUserInfoEvent;
import ru.caffeineim.protocols.icq.integration.events.MetaWorkUserInfoEvent;
import ru.caffeineim.protocols.icq.integration.events.UINRegistrationFailedEvent;
import ru.caffeineim.protocols.icq.integration.events.UINRegistrationSuccessEvent;
import ru.caffeineim.protocols.icq.integration.listeners.MetaInfoListener;
import ru.caffeineim.protocols.icq.packet.sent.meta.FindUsersByUIN;

/**
 * @author Samolisov Pavel
 * @since  30.03.2008
 */
public class FindUsersEventsTest implements Observer, MetaInfoListener {

	private static final String SERVER = "login.icq.com";
    private static final int PORT = 5190;
    private static final String UIN = "217709";

    private OscarConnection connection;

    public FindUsersEventsTest(String uin, String password) {
    	connection = new OscarConnection(SERVER, PORT, uin, password);
        connection.getPacketAnalyser().setDebug(true);
        connection.getPacketAnalyser().setDump(true);

        connection.addMetaInfoListener(this);

        connection.addObserver(this);
    }

	public void update(Observable o, Object arg) {
    	connection.sendFlap(new FindUsersByUIN(UIN, connection.getUserId()));
	}

	public void onAffilationsUserInfo(MetaAffilationsUserInfoEvent e) {
	}

	public void onBasicUserInfo(MetaBasicUserInfoEvent e) {
	}

	public void onEmailUserInfo(MetaEmailUserInfoEvent e) {
	}

	public void onInterestsUserInfo(MetaInterestsUserInfoEvent e) {
	}

	public void onMoreUserInfo(MetaMoreUserInfoEvent e) {
	}

	public void onNotesUserInfo(MetaNoteUserInfoEvent e) {
	}

	public void onShortUserInfo(MetaShortUserInfoEvent e) {
	}

	public void onWorkUserInfo(MetaWorkUserInfoEvent e) {
	}

	public void onRegisterNewUINFailed(UINRegistrationFailedEvent e) {
	}

	public void onRegisterNewUINSuccess(UINRegistrationSuccessEvent e) {
	}

	public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Use : FindUsersEventsTest MY_UIN MY_PASSWORD");
        } else {
            new FindUsersEventsTest(args[0], args[1]);
        }
    }

}
