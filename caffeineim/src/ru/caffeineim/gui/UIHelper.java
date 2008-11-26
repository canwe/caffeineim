/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.caffeineim.gui;

import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.ImageIcon;

import ru.caffeineim.Main;

/**
 *
 * @author renat
 */
public class UIHelper {
    
    public static ImageIcon getIcon(String filename) {
        Image img = Toolkit.getDefaultToolkit().getImage(
                Main.class.getResource("images/" + filename));
        return new ImageIcon(img);
    }    

}
