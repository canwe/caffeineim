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

import java.util.Iterator;
import java.util.Map.Entry;

import ru.caffeineim.protocols.icq.core.OscarConnection;
import ru.caffeineim.protocols.icq.integration.OscarInterface;
import ru.caffeineim.protocols.icq.integration.events.LoginErrorEvent;
import ru.caffeineim.protocols.icq.integration.events.MetaAffilationsUserInfoEvent;
import ru.caffeineim.protocols.icq.integration.events.MetaBasicUserInfoEvent;
import ru.caffeineim.protocols.icq.integration.events.MetaEmailUserInfoEvent;
import ru.caffeineim.protocols.icq.integration.events.MetaInterestsUserInfoEvent;
import ru.caffeineim.protocols.icq.integration.events.MetaMoreUserInfoEvent;
import ru.caffeineim.protocols.icq.integration.events.MetaNoteUserInfoEvent;
import ru.caffeineim.protocols.icq.integration.events.MetaShortUserInfoEvent;
import ru.caffeineim.protocols.icq.integration.events.MetaWorkUserInfoEvent;
import ru.caffeineim.protocols.icq.integration.events.StatusEvent;
import ru.caffeineim.protocols.icq.integration.events.UINRegistrationFailedEvent;
import ru.caffeineim.protocols.icq.integration.events.UINRegistrationSuccessEvent;
import ru.caffeineim.protocols.icq.integration.listeners.MetaInfoListener;
import ru.caffeineim.protocols.icq.integration.listeners.OurStatusListener;

/**
 * <p>Created by 22.03.2008
 *   @author Samolisov Pavel
 */
public class MetaInfoEventsTest implements MetaInfoListener, OurStatusListener {

    private static final String SERVER = "login.icq.com";
    private static final int PORT = 5190;

    private static final String UIN1 = "217709";

    private OscarConnection connection;

    public MetaInfoEventsTest(String uin, String password) {
        connection = new OscarConnection(SERVER, PORT, uin, password);
        connection.getPacketAnalyser().setDebug(true);
        connection.getPacketAnalyser().setDump(true);

        connection.addMetaInfoListener(this);
        connection.addOurStatusListener(this);

        connection.connect();
    }

    public void onShortUserInfo(MetaShortUserInfoEvent e) {
        System.out.println("Short User Info: ");
        System.out.println("  Nick Name = "  + e.getNickName());
        System.out.println("  First Name = " + e.getFirstName());
        System.out.println("  Last Name = "  + e.getLastName());
        System.out.println("  Email = "      + e.getEmail());
        System.out.println("  Auth = "       + e.isAuth());
    }

    public void onBasicUserInfo(MetaBasicUserInfoEvent e) {
        System.out.println("Basic User Info: ");
        System.out.println("  Nick Name = "  + e.getNickName());
        System.out.println("  First Name = " + e.getFirstName());
        System.out.println("  Last Name = "  + e.getLastName());
        System.out.println("  Email = "      + e.getEmail());
        System.out.println("  Home City = "  + e.getHomeCity());
        System.out.println("  Home State = " + e.getHomeState());
        System.out.println("  Home Phone = " + e.getHomePhone());
        System.out.println("  Home Fax = "   + e.getHomeFax());
        System.out.println("  Home Address = "  + e.getHomeAddress());
        System.out.println("  Cell Phone = "  + e.getCellPhone());
        System.out.println("  Zip = "         + e.getZipCode());
        System.out.println("  Home Country = "  + e.getHomeCountry());
        System.out.println("  GMT offset = "  + e.getTimeZone());

        System.out.println("  Auth = "       + e.isAuth());
        System.out.println("  WebAware = "       + e.isWebaware());
        System.out.println("  DirectConnection = " + e.isDirectConnection());
        System.out.println("  PublishPrimaryEmail = " + e.isPublishPrimaryEmail());
    }

    public void onEmailUserInfo(MetaEmailUserInfoEvent e) {
        System.out.println("Email User Info: ");
        for (int i = 0; i < e.getEmails().size(); i++)
        {
            System.out.println("Email: " + e.getEmails().get(i));
        }
    }

    public void onWorkUserInfo(MetaWorkUserInfoEvent e) {
        System.out.println("Work User Info: ");
        System.out.println("  Work City = "  + e.getWorkCity());
        System.out.println("  Work State = "  + e.getWorkState());
        System.out.println("  Work Phone = "  + e.getWorkPhone());
        System.out.println("  Work Fax = "  + e.getWorkFax());
        System.out.println("  Work Address = "  + e.getWorkAddress());
        System.out.println("  Work Zip = "  + e.getWorkZip());
        System.out.println("  Work Country = "  + e.getWorkCountry());
        System.out.println("  Work Company = "  + e.getWorkCompany());
        System.out.println("  Work Department = "  + e.getWorkDepartment());
        System.out.println("  Work Position = "  + e.getWorkPosition());
        System.out.println("  Work WebPage = "  + e.getWorkWebPage());
        System.out.println("  Work Occupation = "  + e.getWorkOccupation());
    }

    public void onMoreUserInfo(MetaMoreUserInfoEvent e) {
        System.out.println("More User Info");
        System.out.println(" age = " + e.getAge());
        System.out.println(" gender = " + e.getGender());
        System.out.println(" homePage = " + e.getHomePage());
        System.out.println(" birth = " + e.getBirth());
        System.out.println(" languages:");
        for (int i = 0; i < e.getLanguages().size(); i++) {
            System.out.println(e.getLanguages().get(i));
        }
        System.out.println(" original City = " + e.getOriginalCity());
        System.out.println(" original State = " + e.getOriginalState());
        System.out.println(" original Country = " + e.getOriginalCountry());
        System.out.println(" marital Status = " + e.getMaritalStatus());
    }

    public void onNotesUserInfo(MetaNoteUserInfoEvent e) {
        System.out.println(" About info = " + e.getNote());
    }

    public void onInterestsUserInfo(MetaInterestsUserInfoEvent e) {
        System.out.println("Interests User Info: ");
        for (Iterator iter = e.getInterests().entrySet().iterator(); iter.hasNext();) {
            Entry entry = (Entry) iter.next();
            System.out.println("Category: " + entry.getKey() + " interest: " + entry.getValue());
        }
    }

    public void onAffilationsUserInfo(MetaAffilationsUserInfoEvent e) {
        System.out.println("PostBackgrounds User Info: ");
        for (Iterator iter = e.getPostBackgrounds().entrySet().iterator(); iter.hasNext();) {
            Entry entry = (Entry) iter.next();
            System.out.println("Category: " + entry.getKey() + " postbackground: " + entry.getValue());
        }

        System.out.println("Affilations User Info: ");
        for (Iterator iter = e.getAffilations().entrySet().iterator(); iter.hasNext();) {
            Entry entry = (Entry) iter.next();
            System.out.println("Category: " + entry.getKey() + " affilations: " + entry.getValue());
        }
    }

    public void onRegisterNewUINSuccess(UINRegistrationSuccessEvent e) {
        System.out.println("Registration of new number complete");
        System.out.println("New UIN: " + e.getNewUIN());
    }

    public void onRegisterNewUINFailed(UINRegistrationFailedEvent e) {
        System.out.println("UIN Registration filed!");
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Use : MetaInfoEventsTest MY_UIN MY_PASSWORD");
        } else {
            new MetaInfoEventsTest(args[0], args[1]);
        }
    }

	public void onAuthorizationFailed(LoginErrorEvent e) {
		connection.close();
		System.exit(1);
	}

	public void onLogin() {
        // Запрашиваем мета-информацию
        OscarInterface.requestFullUserInfo(connection, UIN1);
	}

	public void onLogout(Exception e) {
		e.printStackTrace();
		connection.close();
		System.exit(1);
	}

	public void onStatusResponse(StatusEvent e) {
		// XXX
	}
}
