/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.google.code.caffeineim.gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 *
 * @author renat
 */
public class LoginFrame extends JFrame {
    
    public LoginFrame() {

        setTitle("Caffeine");
        setSize(400, 300);
        
        GridBagPanel c = new GridBagPanel();
        setContentPane(c);

        TitledBorder titleBorder= new TitledBorder("Введите учетные данные");
        c.setBorder(titleBorder);
        
        c.setAnchor(GridBagConstraints.WEST);
        c.setInsets(8, 4, 8, 4);               
                                                
        JComboBox comboNumber= new JComboBox();
        comboNumber.setEditable(true);
        comboNumber.setModel(new DefaultComboBoxModel(new String[] { "395245849", "330893303", "1445935","222523534" }));
        comboNumber.setPreferredSize(new java.awt.Dimension(55, 21));

        Box hbox = Box.createHorizontalBox();
        hbox.add(new JButton("Вход"));
        hbox.add(Box.createHorizontalStrut(4));
        hbox.add(new JButton("Сервер/Прокси"));
        
        c.place(new JLabel("ICQ#/Email:"),          0, 0, 1, 1);
        c.place(comboNumber,                        0, 1, 1, 1);
        c.place(new JLabel("Password:"),            0, 2, 1, 1);
        c.place(new JTextField(20),                 0, 3, 1, 1);
        c.place(new JLabel("Забыли пароль?"),       0, 4, 1, 1);
        c.place(new JCheckBox("Запомнить пароль"),  0, 5, 1, 1);
        c.place(new JCheckBox("Автовход"),          0, 6, 1, 1);
        c.place(new JCheckBox("Чужой компьютер"),   0, 7, 1, 1);       
        c.place(hbox,                               0, 8, 1, 1);
               
    }

}
