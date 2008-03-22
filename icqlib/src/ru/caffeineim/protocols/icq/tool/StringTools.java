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
package ru.caffeineim.protocols.icq.tool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * <p>Created by 15.08.07
 *   @author Samolisov Pavel
 */
public class StringTools {	
	/**
	 * Convert String to Byte Array in Windows-1251 encoding
	 * 
	 * @param s source string  
	 * @return byte array for sended
	 */
	public static byte[] stringToByteArray1251(String s) {
		byte abyte0[] = s.getBytes();
		for(int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			switch (c) {
				case 1025:
					abyte0[i] = -88;
					break;
				case 1105:
					abyte0[i] = -72;
					break;

				/* Ukrainian CP1251 chars section */
				case 1168:
					abyte0[i] = -91;
					break;
				case 1028:
					abyte0[i] = -86;
					break;
				case 1031:
					abyte0[i] = -81;
					break;
				case 1030:
					abyte0[i] = -78;
					break;
				case 1110:
					abyte0[i] = -77;
					break;
				case 1169:
					abyte0[i] = -76;
					break;
				case 1108:
					abyte0[i] = -70;
					break;
				case 1111:
					abyte0[i] = -65;
					break;
				/* end of section */

				default:
					char c1 = c;
					if (c1 >= '\u0410' && c1 <= '\u044F') {
						abyte0[i] = (byte) ((c1 - 1040) + 192);
					}
					break;
			}
		}
		
		return abyte0;
	}	
	
	/**
	 * Convert String from Windows-1251 encoding to UTF8
	 * 
	 * @param s source string
	 * @return string in UTF8 Encoding
	 */
    public static String stringCP1251ToUTF8(String s) {        
        String res = "";                
        
    	try {
    		ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeUTF(s);
            byte[] raw = baos.toByteArray(); 
            byte[] result = new byte[raw.length-2];            
            System.arraycopy(raw, 2, result, 0, raw.length-2);
                
            String ent = new String(result);    		
            for (int i = 0; i < ent.length(); i++) {
            	if (Character.getType(ent.charAt(i)) == Character.CONTROL)
            		res += ".";            		
        		else
        			res += ent.charAt(i);            	
        	}                       
    	}
        catch (Exception e) {
            e.printStackTrace();
        }		
        
        return res;
    }
    
    /**
	 * Convert String from UTF8 encoding to Windows-1251
	 * 
	 * @param s source string
	 * @return string in Windows-1251 encoding
	 */
    public static String UTF8ToStringCP1251(String s) {
    	byte[] buf = s.getBytes();
    	String res = "";
        try {
            byte[] buf2 = new byte[buf.length + 2];
            // add length in start string
            buf2[0] = (byte) ((buf.length >> 8) & 0x000000FF);
            buf2[1] = (byte) ((buf.length) & 0x000000FF);            
            System.arraycopy(buf, 0, buf2, 2, buf.length);
            
            // convert string
            ByteArrayInputStream bais = new ByteArrayInputStream(buf2);
            DataInputStream dis = new DataInputStream(bais);
            
            res =  dis.readUTF();         
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
    	return res;
    }    
}
