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
package ru.caffeineim.protocols.icq.metainfo;

import ru.caffeineim.protocols.icq.setting.enumerations.MetaSubTypeEnum;
import ru.caffeineim.protocols.icq.setting.enumerations.MetaTypeEnum;

/**
 * <p>Created by 24.03.2008
 *   @author Samolisov Pavel
 */
public class MetaInfoParserFactory {
	
	/**
	 * Строим конкретную реализацию интерфейса IMetaInfoParser для обработки 
	 * того или иного типа метаинформации
	 * 
	 * @param metaType
	 * @param metaSubType
	 * @return
	 */
	public static IMetaInfoParser buildMetaInfoParser(int metaType, int metaSubType) {
		IMetaInfoParser parser = null;
		
		switch (metaType) {
			case MetaTypeEnum.SERVER_OFFLINE_MESSAGE:
				parser = new OfflineMessageParser();				
				break;
				
			case MetaTypeEnum.SERVER_END_OF_OFFLINE_MESSAGES:
				parser = new ServerEndOfOflineMessageParser();
				break;
				
			case MetaTypeEnum.SERVER_ADVANCED_META:								
				switch (metaSubType) {
					case MetaSubTypeEnum.SERVER_USER_FOUND:
					case MetaSubTypeEnum.SERVER_LAST_USER_FOUND:
						// TODO сюда засунем поиск
						break;
					case MetaSubTypeEnum.SERVER_SHORT_USER_INFO_REPLY:
						parser = new ShortUserInfoParser();
						break;
				}
				break;
		}
			
		return parser;
	}
}