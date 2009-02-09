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
package ru.caffeineim.protocols.icq.integration.listeners;

import java.util.EventListener;

import ru.caffeineim.protocols.icq.integration.events.IncomingUserEvent;
import ru.caffeineim.protocols.icq.integration.events.LoginErrorEvent;
import ru.caffeineim.protocols.icq.integration.events.OffgoingUserEvent;
import ru.caffeineim.protocols.icq.integration.events.StatusEvent;

/**
 * <p>Created by
 *   @author Fabrice Michellonet 
 */
public interface StatusListener extends EventListener {

	public abstract void onIncomingUser(IncomingUserEvent e);

	public abstract void onOffgoingUser(OffgoingUserEvent e);

	public abstract void onStatusChange(StatusEvent e);

	public abstract void onLogout();
	
	public abstract void onAuthorizationFailed(LoginErrorEvent e);
}