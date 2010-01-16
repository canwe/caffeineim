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

import ru.caffeineim.protocols.icq.core.exceptions.LoginException;
import ru.caffeineim.protocols.icq.integration.events.LoginErrorEvent;
import ru.caffeineim.protocols.icq.integration.listeners.OurStatusListener;

/**
 * <p>Created by 13.06.2008
 *   @author Samolisov Pavel
 *   @author Prolubnikov Dmitry
 */
public class OscarPacketHandler implements Runnable {

    public static final String THREAD_NAME = "OscarPacketHandlerThread";

    private Thread thread;
    private OscarClient client;

    private boolean runnable;

    public OscarPacketHandler(OscarClient client) {
        this.client = client;
        runnable = true;
        thread = new Thread(this);
        thread.start();
    }

    public void run() {
        try {
            while(runnable) {
                if (!client.getMessageQueue().isEmpty()) {
                   client.getAnalyser().handlePacket((byte[]) client.getMessageQueue().poll());
                } else {
                	try {
                		Thread.sleep(100);
                	}
                	catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
        catch (LoginException ex) {
            // create event and notifycation
            LoginErrorEvent e = new LoginErrorEvent(ex.getErrorType());
            for (int i = 0; i < client.getAnalyser().getConnection().getOurStatusListeners().size(); i++) {
                OurStatusListener l = (OurStatusListener) client.getAnalyser().getConnection()
                		.getOurStatusListeners().get(i);
                l.onAuthorizationFailed(e);
            }
            runnable = false;
        }
    }

    public synchronized void stop() {
    	runnable = false;
    }
}
