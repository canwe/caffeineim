package ru.caffeineim.protocols.icq.integration.events;

import java.util.EventObject;

import ru.caffeineim.protocols.icq.packet.received.generic.UINRegistrationFailed__17_1;

public class UINRegistrationFailedEvent  extends EventObject
{
    private static final long serialVersionUID = 1;
    
    public UINRegistrationFailedEvent(UINRegistrationFailed__17_1 source)
    {
        super(source);
    }
}
