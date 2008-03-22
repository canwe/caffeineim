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
package ru.caffeineim.protocols.icq.integration.events;

import java.util.EventObject;

import ru.caffeineim.protocols.icq.packet.received.meta.ServerMetaReply__21_3;
import ru.caffeineim.protocols.icq.setting.enumerations.GenderEnum;
import ru.caffeineim.protocols.icq.setting.enumerations.MetaSubTypeEnum;
import ru.caffeineim.protocols.icq.setting.enumerations.MetaTypeEnum;

/**
 * <p>Created by
 *   @author Lo�c Broquet
 *   @author Samolisov Pavel 
 */
public class MetaInfoEvent extends EventObject {
		
    private static final long serialVersionUID = -397005868703632983L;

    /** 
	 * Creates a new instance of MetaInfoEvent 
	 */
	public MetaInfoEvent(ServerMetaReply__21_3 source) {
		super(source);
	}
	
	public String getUin() {
		return ((ServerMetaReply__21_3)getSource()).getSenderUin();
	}
	
	public String getNickName() {
		return ((ServerMetaReply__21_3)getSource()).getNickName();
	}
	
	public String getFirstName() {
		return ((ServerMetaReply__21_3)getSource()).getFirstName();
	}
	
	public String getLastName() {
		return ((ServerMetaReply__21_3)getSource()).getLastName();
	}
	
	public String getEmail() {
		return ((ServerMetaReply__21_3)getSource()).getEmail();
	}
	
	public boolean getAuthFlag() {
		return ((ServerMetaReply__21_3)getSource()).getAuthFlag();
	}
	
	public GenderEnum getGender() {
		return ((ServerMetaReply__21_3)getSource()).getGender();
	}
	
	public MetaTypeEnum getMetaType() {
		return ((ServerMetaReply__21_3)getSource()).getMetaType();
	}
	
	public MetaSubTypeEnum getMetaSubType() {
		return ((ServerMetaReply__21_3)getSource()).getMetaSubType();
	}
}
