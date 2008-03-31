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
package ru.caffeineim.protocols.icq.packet.received.generic;

import ru.caffeineim.protocols.icq.core.OscarConnection;
import ru.caffeineim.protocols.icq.integration.events.UINRegistrationSuccessEvent;
import ru.caffeineim.protocols.icq.integration.listeners.MetaInfoListener;
import ru.caffeineim.protocols.icq.packet.received.ReceivedPacket;

/**
 * <p>Created by
 *   @author Egor Baranov 
 */
public class UINRegistrationSuccess__17_5 extends ReceivedPacket {
    protected String uin;

    public UINRegistrationSuccess__17_5(byte array[]) {
        super(array, true);
        int num = this.getSnac().getByteArray()[11] << 8 + this.getSnac()
                .getByteArray()[12];
        uin = String.valueOf(num);
    }

    public String getNewUIN() {
        return uin;
    }

    public void notifyEvent(OscarConnection connection) {
        UINRegistrationSuccessEvent e = new UINRegistrationSuccessEvent(this);
        for (int i = 0; i < connection.getMetaInfoListeners().size(); i++) {
            MetaInfoListener l = (MetaInfoListener) connection
                    .getMessagingListeners().get(i);
            l.registerNewUINSuccess(e);
        }
    }

}
