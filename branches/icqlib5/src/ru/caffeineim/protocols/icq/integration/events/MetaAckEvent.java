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

import ru.caffeineim.protocols.icq.metainfo.MetaAckParser;

/**
 * @author Samolisov Pavel
 * @since  30.03.2008
 */
public class MetaAckEvent extends EventObject {
	
	private static final long serialVersionUID = 7502170145119936435L;

	public MetaAckEvent(MetaAckParser source) {
		super(source);
	}
	
	public boolean isOk() {
		return ((MetaAckParser) getSource()).isOk();
	}
}
