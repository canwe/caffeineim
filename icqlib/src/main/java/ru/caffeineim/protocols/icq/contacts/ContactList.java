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
package ru.caffeineim.protocols.icq.contacts;

import java.util.Iterator;

import ru.caffeineim.protocols.icq.core.OscarConnection;
import ru.caffeineim.protocols.icq.exceptions.ConvertStringException;
import ru.caffeineim.protocols.icq.packet.sent.buddylist.AddToContactList;
import ru.caffeineim.protocols.icq.packet.sent.buddylist.RemoveFromContactList;
import ru.caffeineim.protocols.icq.packet.sent.meta.RequestShortUserInfo;
import ru.caffeineim.protocols.icq.packet.sent.ssi.SsiAddItem;
import ru.caffeineim.protocols.icq.packet.sent.ssi.SsiBeginEdit;
import ru.caffeineim.protocols.icq.packet.sent.ssi.SsiContactListRequest;
import ru.caffeineim.protocols.icq.packet.sent.ssi.SsiEndEdit;
import ru.caffeineim.protocols.icq.packet.sent.ssi.SsiRemoveItem;
import ru.caffeineim.protocols.icq.packet.sent.ssi.SsiRemoveYourself;
import ru.caffeineim.protocols.icq.packet.sent.ssi.SsiSendAuthReplyMessage;
import ru.caffeineim.protocols.icq.packet.sent.ssi.SsiSendAuthRequestMessage;
import ru.caffeineim.protocols.icq.packet.sent.ssi.SsiSendYouWereAdded;
import ru.caffeineim.protocols.icq.packet.sent.ssi.SsiUpdateGroupHeader;

/**
 * <p>Created by 15.08.2007
 *   @author Samolisov Pavel
 */
public class ContactList {
    // TODO needs complete refactoring!
    private short maxGroupId = 0;
    private short maxContactId = 0;
    private Group rootGroup;
    private static ContactList impl = null;

    /**
     * private constructor - implements pattern singleton
     *
     * @param rootGroup
     */
    private ContactList(Group rootGroup) {
        this.rootGroup = rootGroup;
        // load maxGroupId and maxContactId

        for (Iterator iter = rootGroup.getContainedItems().iterator(); iter.hasNext();) {
            ContactListItem item = (ContactListItem) iter.next();
            if (item instanceof Group) {
                Group grp = (Group) item;

                if (grp.getGroupId() > maxGroupId)
                    maxGroupId = grp.getGroupId();

                for (Iterator grpiter = rootGroup.getContainedItems().iterator(); grpiter.hasNext();) {
                    ContactListItem cntct = (ContactListItem) grpiter.next();
                    if (cntct instanceof Contact) {
                        Contact cnt = (Contact) cntct;
                        if (cnt.getItemId() > maxContactId)
                            maxContactId = cnt.getItemId();
                    }
                }
            }
        }
    }

    private ContactList() {}

    /**
     * Add new contact to contact's list
     *
     * @param connection
     * @param uin
     * @param grp
     * @throws ConvertStringException
     */
    public void addContact(OscarConnection connection, String uin, Group grp)
            throws ConvertStringException {
        Contact cnt = new Contact(++maxContactId, grp.getGroupId(), uin);
        addContact(connection, cnt, grp);
    }

    /**
     * Add new contact to contact's list
     *
     * @param connection
     * @param contact
     * @param grp
     * @throws ConvertStringException
     */
    public void addContact(OscarConnection connection, Contact contact, Group grp)
            throws ConvertStringException {
        grp.addItem(contact);

        connection.sendFlap(new SsiBeginEdit());
        connection.sendFlap(new SsiAddItem(contact));
        connection.sendFlap(new SsiUpdateGroupHeader(grp));
        connection.sendFlap(new SsiEndEdit());

        connection.sendFlap(new AddToContactList(contact.getId()));

        if (contact.getNickName() != null && contact.getNickName() != "") {
            connection.sendFlap(new RequestShortUserInfo(contact.getId(), connection.getUserId()));
        }
    }

    /**
     * Remove contact from contact's list
     *
     * @param connection
     * @param uin contact uin
     * @throws ConvertStringException
     */
    public void removeContact(OscarConnection connection, String uin)
            throws ConvertStringException {
        Contact cnt = getContactByUIN(uin);
        if (cnt != null) {
            removeContact(connection, cnt);
        }
    }

    /**
     * Remove contact from contact's list
     *
     * @param connection
     * @param contact
     * @throws ConvertStringException
     */
    public void removeContact(OscarConnection connection, Contact contact)
            throws ConvertStringException {
        Group grp = getGroupById(contact.getGroupId());
        if (grp != null)
            grp.removeItem(contact);

        connection.sendFlap(new SsiBeginEdit());
        connection.sendFlap(new SsiRemoveItem(contact));
        connection.sendFlap(new SsiUpdateGroupHeader(grp));
        connection.sendFlap(new SsiEndEdit());

        connection.sendFlap(new RemoveFromContactList(contact.getId()));
    }

    /**
     * Add group to contact's list
     *
     * @param connection
     * @param grpName name of group
     * @throws ConvertStringException
     */
    public void addGroup(OscarConnection connection, String grpName) throws ConvertStringException {
        addGroup(connection, new Group(++maxGroupId, grpName));
    }

    /**
     * Add group to contact's list
     *
     * @param connection
     * @param grp
     * @throws ConvertStringException
     */
    public void addGroup(OscarConnection connection, Group grp)
            throws ConvertStringException {
        rootGroup.addItem(grp);

        connection.sendFlap(new SsiBeginEdit());
        connection.sendFlap(new SsiAddItem(grp));
        connection.sendFlap(new SsiEndEdit());
    }

    /**
     * Remove group from contact's list
     *
     * @param connection
     * @param grp
     * @throws ConvertStringException
     */
    public void removeGroup(OscarConnection connection, Group grp)
            throws ConvertStringException {
        rootGroup.removeItem(grp);

        connection.sendFlap(new SsiBeginEdit());
        connection.sendFlap(new SsiRemoveItem(grp));
        connection.sendFlap(new SsiEndEdit());
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (Iterator iter = rootGroup.getContainedItems().iterator(); iter.hasNext();) {
            ContactListItem item = (ContactListItem) iter.next();
            sb.append(item.getId() + ":\n");
            if (item instanceof Group) {
                Group grp = (Group) item;
                for (Iterator grpiter = grp.getContainedItems().iterator(); grpiter.hasNext();) {
                    ContactListItem grpitem = (ContactListItem) grpiter.next();
                    if (grpitem instanceof Contact) {
                        Contact cnt = (Contact) grpitem;
                        sb.append("   " + cnt.getNickName() + " (" + cnt.getId() + ")\n");
                    }
                }
            }
        }

        return sb.toString();
    }

    /**
     * Find group by group id
     *
     * @param id
     * @return
     */
    private Group getGroupById(short id) {
        for (Iterator iter = rootGroup.getContainedItems().iterator(); iter.hasNext();) {
            ContactListItem item = (ContactListItem) iter.next();
            if (item.getGroupId() == id) {
                return (Group) item;
            }
        }

        return null;
    }

    /**
     * Find contact by uin
     *
     * @param uin
     * @return
     */
    private Contact getContactByUIN(String uin) {
        for (Iterator iter = rootGroup.getContainedItems().iterator(); iter.hasNext();) {
            ContactListItem item = (ContactListItem) iter.next();
            if (item instanceof Group) {
                Group grp = (Group) item;
                for (Iterator grpiter = grp.getContainedItems().iterator(); grpiter.hasNext();) {
                    ContactListItem cntct = (ContactListItem) grpiter.next();
                    if (cntct.getId().equals(uin))
                        return (Contact) cntct;
                }
            }
        }

        return null;
    }


    /**
     * return instance of ContactList
     * if instance not exists - create new
     *
     * @param rootGroup
     * @return
     */
    public static ContactList getInstance(Group rootGroup) {
        if (impl == null) {
            impl = new ContactList(rootGroup);
        }
        return impl;
    }

    /**
     * return created instance of ContactList
     * if instance not exists - return null!
     *
     * @return
     */
    public static ContactList getInstance() {
        return impl;
    }

    /**
     * Remove me from uin's contact's list
     *
     * @param connection
     * @param uin
     */
    public static void removeYourself(OscarConnection connection, String uin) {
        connection.sendFlap(new SsiRemoveYourself(uin));
    }

    /**
     * Send Authorization Request message
     *
     * @param connection
     * @param uin
     * @param message
     * @throws ConvertStringException
     */
    public static void sendAuthRequestMessage(OscarConnection connection, String uin, String message)
            throws ConvertStringException {
        connection.sendFlap(new SsiSendAuthRequestMessage(uin, message));
    }

    /**
     * Send Authorization Reply message
     *
     * @param connection
     * @param uin
     * @param message
     * @param auth - auth flag
     * @throws ConvertStringException
     */
    public static void sendAuthReplyMessage(OscarConnection connection, String uin, String message,
            boolean auth) throws ConvertStringException {
        connection.sendFlap(new SsiSendAuthReplyMessage(uin, message, auth));
    }

    /**
     * Send "You were added" message
     *
     * @param connection
     * @param uin
     */
    public static void sendYouWereAdded(OscarConnection connection, String uin) {
        connection.sendFlap(new SsiSendYouWereAdded(uin));
    }

    /**
     * Send contact's list request
     *
     * @param connection
     */
    public static void sendContatListRequest(OscarConnection connection) {
        connection.sendFlap(new SsiContactListRequest());
    }

    public static Group getRootGroup() {
        if (impl != null)
            return impl.rootGroup;

        return null;
    }
}
