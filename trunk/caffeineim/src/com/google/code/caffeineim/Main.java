package com.google.code.caffeineim;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.JFrame;
import javax.swing.UnsupportedLookAndFeelException;

import com.google.code.caffeineim.gui.LoginFrame;
import com.google.code.caffeineim.gui.MessageWindow;

/**
 *
 * @author renat
 */
public class Main {

    public static void main(String args[]) {
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {                
                try {

                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

                    LoginFrame frame = new LoginFrame();
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setLocationRelativeTo(null);
                    frame.pack();
                    frame.setVisible(true);
                    
                    MessageWindow mw_frame = new MessageWindow();
//                    mw_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//                    mw_frame.setLocationRelativeTo(null);
                    mw_frame.pack();
                    mw_frame.setVisible(true);

                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InstantiationException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnsupportedLookAndFeelException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }                    
            }
        });
    }

}
