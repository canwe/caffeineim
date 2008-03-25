package ru.caffeineim.protocols.icq.integration.events;

import java.util.EventObject;

import ru.caffeineim.protocols.icq.packet.received.generic.UINRegistrationSuccess__17_5;

public class UINRegistrationSuccessEvent extends EventObject
{
    private static final long serialVersionUID = 1;
    
    public UINRegistrationSuccessEvent(UINRegistrationSuccess__17_5 source)
    {
        super(source);
    }
    
    String getNewUIN()
    {
        return ((UINRegistrationSuccess__17_5)this.getSource()).getNewUIN();
    }
}
