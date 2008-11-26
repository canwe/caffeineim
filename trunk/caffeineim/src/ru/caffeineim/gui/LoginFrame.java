package ru.caffeineim.gui;

import java.awt.*;
import java.awt.event.*;
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
        comboNumber.setModel(new DefaultComboBoxModel(
                new String[] { "395245849", "330893303", "1445935","222523534" }));
        comboNumber.setPreferredSize(new java.awt.Dimension(55, 21));

        JButton btnLogin = new JButton("Вход");
        btnLogin.setMnemonic(KeyEvent.VK_C);
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                LoginFrame.this.dispose();

                JFrame contactList = new ContactList();
                contactList.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                contactList.setLocation(800, 300);
                //contactList  frame_main.pack();
                contactList.setVisible(true);
                                    
            }
        }); 
        
        Box hbox = Box.createHorizontalBox();
        hbox.add(btnLogin);
        hbox.add(Box.createHorizontalStrut(4));
        hbox.add(new JButton("Сервер/Прокси"));
        
        c.place(new JLabel("ICQ#/Email:"),          0, 0, 1, 1);
        c.place(new JLabel("Password:"),            0, 2, 1, 1);
        c.place(new JLabel("Забыли пароль?"),       0, 4, 1, 1);
        c.place(new JCheckBox("Запомнить пароль"),  0, 5, 1, 1);
        c.place(new JCheckBox("Автовход"),          0, 6, 1, 1);
        c.place(new JCheckBox("Чужой компьютер"),   0, 7, 1, 1);       
        c.place(hbox,                               0, 8, 1, 1);

        c.setFill(GridBagConstraints.HORIZONTAL);
        c.place(comboNumber,                        0, 1, 1, 1);
        c.place(new JTextField(20),                 0, 3, 1, 1);
        
    }

}
