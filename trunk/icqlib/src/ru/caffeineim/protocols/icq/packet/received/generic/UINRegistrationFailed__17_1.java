package ru.caffeineim.protocols.icq.packet.received.generic;

import ru.caffeineim.protocols.icq.core.OscarConnection;
import ru.caffeineim.protocols.icq.integration.events.UINRegistrationFailedEvent;
import ru.caffeineim.protocols.icq.integration.listeners.MetaInfoListener;
import ru.caffeineim.protocols.icq.packet.received.ReceivedPacket;

public class UINRegistrationFailed__17_1 extends ReceivedPacket
{
    public UINRegistrationFailed__17_1(byte array[])
    {
        super(array, true);
    }
    
    public void notifyEvent(OscarConnection connection) 
    {
        UINRegistrationFailedEvent e = new UINRegistrationFailedEvent(this);
        for (int i = 0; i < connection.getMetaInfoListeners().size(); i++)
        {
            MetaInfoListener l = (MetaInfoListener)connection.getMessagingListeners().get(i);
            l.registerNewUINFailed(e);
        }
    }
}
