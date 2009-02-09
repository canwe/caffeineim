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

import ru.caffeineim.protocols.icq.packet.sent.authorization.Ping;

/**
 * @author Samolisov Pavel
 * @since  22.06.2008
 */
public class OscarPingHandler implements Runnable {

	public static final String THREAD_NAME = "OscarPingHandlerThread";	
	
	private Thread thread;
	private int interval;
	private OscarConnection connection;
		
	public OscarPingHandler(OscarConnection connection, int interval) {
		this.connection = connection;
		this.interval = interval;
		
		thread = new Thread(this);
		thread.start();
	}
		
	public void run() {
		try {
			// TODO подумать как сдесь сделать перелогин при получении соединения
            while(true) {
            	if (connection.isLogged()) {
            		
            		if (connection.getClient().getAnalyser().isDebugging())
            			System.out.println("ping...");
            		
            		connection.sendFlap(new Ping());
            	}
            	
            	Thread.sleep(interval);
            }
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}