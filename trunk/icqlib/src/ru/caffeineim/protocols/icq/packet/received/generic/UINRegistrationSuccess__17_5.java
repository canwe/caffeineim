package ru.caffeineim.protocols.icq.packet.received.generic;

import ru.caffeineim.protocols.icq.core.OscarConnection;
import ru.caffeineim.protocols.icq.integration.events.UINRegistrationSuccessEvent;
import ru.caffeineim.protocols.icq.integration.listeners.MetaInfoListener;
import ru.caffeineim.protocols.icq.packet.received.ReceivedPacket;

public class UINRegistrationSuccess__17_5 extends ReceivedPacket
{
    protected String uin;
    
    public UINRegistrationSuccess__17_5(byte array[])
    {
        super(array, true);
    }
    
    public String getNewUIN()
    {
        if (uin == null)
        {
            int num = this.getSnac().getByteArray()[11] << 8 + this.getSnac().getByteArray()[12];
            uin = String.valueOf(num);
        }
        
        return uin;
    }
    
    public void notifyEvent(OscarConnection connection) 
    {
        UINRegistrationSuccessEvent e = new UINRegistrationSuccessEvent(this);
        for (int i = 0; i < connection.getMetaInfoListeners().size(); i++)
        {
            MetaInfoListener l = (MetaInfoListener)connection.getMessagingListeners().get(i);
            l.registerNewUINSuccess(e);
        }
    }

}
