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
package ru.caffeineim.protocols.icq.core;

import java.io.IOException;
import java.util.EventListener;
import java.util.List;
import java.util.Observable;
import java.util.Vector;

import ru.caffeineim.protocols.icq.Flap;
import ru.caffeineim.protocols.icq.Tlv;
import ru.caffeineim.protocols.icq.integration.listeners.ContactListListener;
import ru.caffeineim.protocols.icq.integration.listeners.MessagingListener;
import ru.caffeineim.protocols.icq.integration.listeners.MetaAckListener;
import ru.caffeineim.protocols.icq.integration.listeners.MetaInfoListener;
import ru.caffeineim.protocols.icq.integration.listeners.StatusListener;
import ru.caffeineim.protocols.icq.integration.listeners.XStatusListener;
import ru.caffeineim.protocols.icq.request.Request;
import ru.caffeineim.protocols.icq.request.RequestKeeper;
import ru.caffeineim.protocols.icq.request.event.RequestListener;
import ru.caffeineim.protocols.icq.setting.Tweaker;

/**
 * <p>Created by
 *   @author Fabrice Michellonet 
 */
public class OscarConnection extends Observable {

	private Tlv cookie;
	private String userId;
	private String password;
	private boolean logged = false;

	private Tweaker tweaker;
	private OscarClient client;
	private OscarPacketAnalyser analyser;
	private RequestKeeper requestKeeper;
	private List<EventListener> messagingListeners;
	private List<EventListener> statusListeners;
	private List<EventListener> xStatusListeners;
	private List<EventListener> contactListListeners;
	private List<EventListener> metaInfoListeners;
	private List<EventListener> metaAckListeners;
  
	private int flapSeqNrs;

	public OscarConnection(String host, int port, String userId, String password) {
		this(host, port, userId, password, new Tweaker());
	}

	public OscarConnection(String host, int port, String userId, String password, Tweaker tweaker) {
		this.userId = userId;
		this.password = password;
		this.tweaker = tweaker;
		this.flapSeqNrs = 0;
		analyser = new OscarPacketAnalyser(this);
		client = new OscarClient(host, port, analyser);
		requestKeeper = new RequestKeeper();
		messagingListeners = new Vector<EventListener>();
		statusListeners  = new Vector<EventListener>();
		xStatusListeners  = new Vector<EventListener>();
		contactListListeners   = new Vector<EventListener>();
		metaInfoListeners  = new Vector<EventListener>();
		metaAckListeners = new Vector<EventListener>();
    	
		client.connectToServer();	
	}

	public void addMetaAckListener(MetaAckListener listener) {
		metaAckListeners.add(listener);
	}
  
	public boolean removeMetaAckListener(MetaAckListener listener) {
		return metaAckListeners.remove(listener);
	}
	
	public void addMetaInfoListener(MetaInfoListener listener) {
		metaInfoListeners.add(listener);
	}
  
	public boolean removeMetaInfoListener(MetaInfoListener listener) {
		return metaInfoListeners.remove(listener);
	}
  
	public void addContactListListener(ContactListListener listener) {
		contactListListeners.add(listener);
	}
  
	public boolean removeContactListListener(ContactListListener listener) {
		return contactListListeners.remove(listener);
	}
  
	public void addMessagingListener(MessagingListener listener) {
		messagingListeners.add(listener);
	}

	public boolean removeMessagingListener(MessagingListener listener) {
		return messagingListeners.remove(listener);
	}

	public void addStatusListener(StatusListener listener) {
		statusListeners.add(listener);
	}

	public boolean removeStatusListener(StatusListener listener) {
		return statusListeners.remove(listener);
	}
	
	public void addXStatusListener(XStatusListener listener) {
		xStatusListeners.add(listener);
	}

	public boolean removeXStatusListener(XStatusListener listener) {
		return xStatusListeners.remove(listener);
	}

	/**
	 * This method send a packet to the server.
	 *
	 * @param flapPacket The paquet to be sent.
	 */
	public synchronized void sendFlap(Flap flapPacket) {
		if (flapPacket.getSequenceNumber() == Integer.MAX_VALUE) { // not assigned yet..
			flapSeqNrs++;

			if (flapSeqNrs > 0xffff)
				flapSeqNrs = 0;
			
			flapPacket.setSequenceNumber(flapSeqNrs);
		}

		try {			
			client.sendPacket(flapPacket.getByteArray());
		} 
		catch (IOException e) {
			if (logged) {
				// signal logout..
				try {
					client.disconnect();
				} catch (IOException e1) { }
              
				for (int i = 0; i < getStatusListeners().size(); i++) {
					StatusListener l = (StatusListener) getStatusListeners().get(i);
					l.onLogout();
				}
			}
		} catch (NullPointerException e) {
			System.err.println("OOPS! NullPointerException");
			e.printStackTrace();
		}
	}


	/**
	 * This method send a packet to the server and start the monitoring system.<br/>
	 * The <b>listener</b> will be warned of the server reply by a <b>RequestAnswerEvent</b> event.
	 *
	 * @param flapPacket The paquet to be sent.
	 * @param listener The class that monitor the packet.
	 *
	 * @return The request object that has been created, null if the flap do not contains
	 * a Snac section.
	 */
	public Request sendMonitoredFlap(Flap flapPacket, RequestListener listener) {
		int requestId;
		Request request = null;

		if (flapPacket.hasSnac()) {
			requestId = requestKeeper.nextAvailableRequestId();
			flapPacket.getSnac().setRequestId(requestId);

			request = new Request(flapPacket, listener);
			requestKeeper.addRequest(request);
		}
		sendFlap(flapPacket);

		return request;
	}

	/**
	 * This will cause the connection to be closed.
	 *
	 * @throws IOException
	 */
	public void close() throws IOException {
		client.disconnect();
		setLogged(false);
	}

	protected void setLogged(boolean status) {
		logged = status;
		setChanged();
		notifyObservers();
	}

	public boolean isLogged() {
		return logged;
	}

	public String getUserId() {
		return userId;
	}

	public String getPassword() {
		return password;
	}

	public Tlv getCookie() {
		return this.cookie;
	}

	public void setCookie(Tlv cookie) {
		this.cookie = cookie;
	}

	public OscarClient getClient() {
		return this.client;
	}

	public void setClient(OscarClient client) {
		this.client = client;
	}

	public Tweaker getTweaker() {
		return this.tweaker;
	}

	public OscarPacketAnalyser getPacketAnalyser() {
		return this.analyser;
	}

	public List<EventListener> getMessagingListeners() {
		return messagingListeners;
	}

	public List<EventListener> getStatusListeners() {
		return statusListeners;
	}
	
	public List<EventListener> getXStatusListeners() {
		return xStatusListeners;
	}

	public List<EventListener> getContactListListeners() {
		return contactListListeners;
	}

	public List<EventListener> getMetaInfoListeners() {
		return metaInfoListeners;
	}
	
	public List<EventListener> getMetaAckListeners() {
		return metaAckListeners;
	}
	
	public RequestKeeper getRequestKeeper() {
		return requestKeeper;
	}
}