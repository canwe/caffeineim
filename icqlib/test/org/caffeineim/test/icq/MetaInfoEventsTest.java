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
import ru.caffeineim.protocols.icq.setting.enumerations.AffilationEnum;
import ru.caffeineim.protocols.icq.setting.enumerations.InterestsEnum;
import ru.caffeineim.protocols.icq.setting.enumerations.LanguagesEnum;
import ru.caffeineim.protocols.icq.setting.enumerations.PostBackgroundEnum;
import ru.caffeineim.protocols.icq.tool.OscarInterface;

/**
 * <p>Created by 22.03.2008
 *   @author Samolisov Pavel
 */
public class MetaInfoEventsTest implements MetaInfoListener, Observer {
	
	private static final String SERVER = "login.icq.com";
    private static final int PORT = 5190;
    
    private static final String UIN1 = "217709";    
    
    private OscarConnection connection;

    public MetaInfoEventsTest(String uin, String password) {       
    	connection = new OscarConnection(SERVER, PORT, uin, password);
        connection.getPacketAnalyser().setDebug(true);
        //connection.getPacketAnalyser().setDump(true);
                        
        connection.addMetaInfoListener(this);
                
        connection.addObserver(this);
    }
        
    public void update(Observable obs, Object obj) {
    	System.out.println("Send meta request");
    	
    	// Запрашиваем инфу
    	OscarInterface.requestFullUserInfo(connection, UIN1);
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
		for (String email : e.getEmails())
		{
			System.out.println("Email: " + email);
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
		for (LanguagesEnum lang : e.getLanguages()) {
			System.out.println(lang);
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
		for (InterestsEnum code : e.getInterests().keySet()) {
			System.out.println("Category: " + code + " interest: " + e.getInterests().get(code));
		}
	}
	
	public void onAffilationsUserInfo(MetaAffilationsUserInfoEvent e) {
		System.out.println("PostBackgrounds User Info: ");
		for (PostBackgroundEnum code : e.getPostBackgrounds().keySet()) {
			System.out.println("Category: " + code + " postbackground: " + e.getPostBackgrounds().get(code));
		}
		
		System.out.println("Affilations User Info: ");
		for (AffilationEnum code : e.getAffilations().keySet()) {
			System.out.println("Category: " + code + " affilations: " + e.getAffilations().get(code));
		}
	}	
	
	public void registerNewUINSuccess(UINRegistrationSuccessEvent e) {
        
    }
    	
    public void registerNewUINFailed(UINRegistrationFailedEvent e) {
        
    }
	
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Use : MetaInfoEventsTest MY_UIN MY_PASSWORD");
        } else {
            new MetaInfoEventsTest(args[0], args[1]);
        }
    }	
}