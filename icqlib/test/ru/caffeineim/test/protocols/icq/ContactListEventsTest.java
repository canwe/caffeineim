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

import ru.caffeineim.protocols.icq.contacts.ContactList;
import ru.caffeineim.protocols.icq.contacts.ContactListItem;
import ru.caffeineim.protocols.icq.contacts.Group;
import ru.caffeineim.protocols.icq.core.OscarConnection;
import ru.caffeineim.protocols.icq.exceptions.ConvertStringException;
import ru.caffeineim.protocols.icq.integration.OscarInterface;
import ru.caffeineim.protocols.icq.integration.events.ContactListEvent;
import ru.caffeineim.protocols.icq.integration.events.IncomingMessageEvent;
import ru.caffeineim.protocols.icq.integration.events.IncomingUrlEvent;
import ru.caffeineim.protocols.icq.integration.events.IncomingUserEvent;
import ru.caffeineim.protocols.icq.integration.events.LoginErrorEvent;
import ru.caffeineim.protocols.icq.integration.events.MessageAckEvent;
import ru.caffeineim.protocols.icq.integration.events.MessageErrorEvent;
import ru.caffeineim.protocols.icq.integration.events.MessageMissedEvent;
import ru.caffeineim.protocols.icq.integration.events.OffgoingUserEvent;
import ru.caffeineim.protocols.icq.integration.events.OfflineMessageEvent;
import ru.caffeineim.protocols.icq.integration.events.SsiAuthReplyEvent;
import ru.caffeineim.protocols.icq.integration.events.SsiAuthRequestEvent;
import ru.caffeineim.protocols.icq.integration.events.SsiFutureAuthGrantEvent;
import ru.caffeineim.protocols.icq.integration.events.SsiModifyingAckEvent;
import ru.caffeineim.protocols.icq.integration.events.StatusEvent;
import ru.caffeineim.protocols.icq.integration.listeners.ContactListListener;
import ru.caffeineim.protocols.icq.integration.listeners.MessagingListener;
import ru.caffeineim.protocols.icq.integration.listeners.OurStatusListener;
import ru.caffeineim.protocols.icq.integration.listeners.UserStatusListener;
import ru.caffeineim.protocols.icq.setting.enumerations.SsiResultModeEnum;
import ru.caffeineim.protocols.icq.setting.enumerations.StatusModeEnum;

/**
 * <p> Created by 22.03.2008
 *   @author Samolisov Pavel
 *   @author Egor Baranov
 */
public class ContactListEventsTest implements MessagingListener, UserStatusListener,
		OurStatusListener, ContactListListener {

    private static final String SERVER = "login.icq.com";
    private static final int PORT = 5190;

    private OscarConnection connection;

    public ContactListEventsTest(String uin, String password) {
        connection = new OscarConnection(SERVER, PORT, uin, password);
        connection.getPacketAnalyser().setDebug(true);

        connection.addMessagingListener(this);
        connection.addUserStatusListener(this);
        connection.addContactListListener(this);
        connection.addOurStatusListener(this);

        connection.connect();
    }

    public void onOffgoingUser(OffgoingUserEvent e) {
        System.out.println(e.getOffgoingUserId() + " went offline.");
    }

    public void onIncomingUser(IncomingUserEvent e) {
        System.out.println(e.getIncomingUserId() + " has just signed on (" + e.getClientData() + ").");
        //System.out.printf("Capabilites: 0x%X", new Object[]{new Integer(e.getClientData().getCaps())});
    }

    public void updateContactList(ContactListEvent e) {
        System.out.println("\nMy Contact List");
        System.out.println(ContactList.getInstance(e.getRoot()));
    }

    public void onSsiModifyingAck(SsiModifyingAckEvent e) {
        for (int i = 0; i < e.getResults().length; i++) {
            System.out.println("Result = " + e.getResults()[i] + " code = " + e.getResults()[i].getResult());
        }

        if (e.getResults()[e.getResults().length - 1].getResult() == SsiResultModeEnum.NO_ERRORS) {
            System.out.println("\nMy Contact List");
            System.out.println(ContactList.getInstance().toString());
        }
    }

    public void onIncomingMessage(IncomingMessageEvent e) {
        System.out.println(e.getSenderID() + " sent : " + e.getMessage());

        try {
            OscarInterface.sendBasicMessage(connection, e.getSenderID(), e.getMessage());

            String[] params = e.getMessage().split(" ");
            if (params[0].equals("add") && params.length > 1) {
                System.out.println("Add new user!");
                ContactList.getInstance().addContact(connection, params[1],
                        (Group) ContactList.getRootGroup().getContainedItems().get(0));
                ContactList.sendYouWereAdded(connection, params[1]);
            } else if (params[0].equals("addgrp") && params.length > 1) {
                System.out.println("Add new group!");
                ContactList.getInstance().addGroup(connection, params[1]);
            } else if (params[0].equals("remove") && params.length > 1) {
                System.out.println("Remove contact!");
                ContactList.getInstance().removeContact(connection, params[1]);
            } else if (params[0].equals("remgrp")) {
                System.out.println("Remove group "
                        + ((ContactListItem) ContactList.getRootGroup().getContainedItems().get(0)).getId() + "!");
                ContactList.getInstance().removeGroup(connection,
                        (Group) ContactList.getRootGroup().getContainedItems().get(0));
            } else if (params[0].equals("remme") && params.length > 1) {
                ContactList.removeYourself(connection, params[1]);
            } else if (params[0].equals("auth") && params.length > 1) {
                ContactList.sendAuthRequestMessage(connection, params[1], "Авторизуй меня!");
            } else if (params[0].equals("msge") && params.length > 2) {
                OscarInterface.sendExtendedMessage(connection, params[2], params[1]);
            } else if (params[0].equals("msgb")) {
                OscarInterface.sendBasicMessage(connection, params[2], params[1]);
            }
        } catch (ConvertStringException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void onIncomingUrl(IncomingUrlEvent e) {
        System.out.println(e.getSenderID() + " sent : " + e.getUrl());
    }

    public void onOfflineMessage(OfflineMessageEvent e) {
        System.out.println(e.getSenderUin() + " sent : " + e.getMessage() + " while i was offline");
    }

    public void onMessageError(MessageErrorEvent e) {
        System.out.println("Message error code " + e.getError().getCode() + " occurred");
    }

    public void onMessageMissed(MessageMissedEvent e) {
        System.out.println("Message from " + e.getUin() + " can't be recieved because " + e.getReason());
    }

    public void onLogout(Exception e) {
        System.out.println("Logged out (possibly due to error)");
        e.printStackTrace();
        connection.close();
        System.exit(1);
    }

    public void onLogin() {
        OscarInterface.changeStatus(connection, new StatusModeEnum(StatusModeEnum.ONLINE));
        ContactList.sendContatListRequest(connection);
    }

    public void onMessageAck(MessageAckEvent e) {

    }

    public void onAuthorizationFailed(LoginErrorEvent e) {
        System.out.println("Login Error " + e.getErrorMessage());
        connection.close();
        System.exit(1);
    }

    public void onSsiFutureAuthGrant(SsiFutureAuthGrantEvent e) {
        System.out.println("FutureAuthGrant UIN: " + e.getSenderUin() + " Mesage: " + e.getMessage());
    }

    public void onSsiAuthRequest(SsiAuthRequestEvent e) {
        try {
            System.out.println("AuthRequest UIN: " + e.getSenderUin() + " Mesage: " + e.getMessage());
            ContactList.sendAuthReplyMessage(connection, e.getSenderUin(), "Welcome!", true);
        } catch (ConvertStringException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void onSsiAuthReply(SsiAuthReplyEvent e) {
        System.out.println("AuthReply UIN: " + e.getSenderUin() + " Mesage: "
                + e.getMessage() + " Flag: " + e.getAuthFlag());
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Use : ContactListEventsTest MY_UIN MY_PASSWORD");
        } else {
            new ContactListEventsTest(args[0], args[1]);
        }
    }

	public void onStatusResponse(StatusEvent e) {
        System.out.println("StatusEvent: " + e.getStatusMode());
	}
}
