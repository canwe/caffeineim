/** * Copyright 2008 Caffeine-Soft Group * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at *  * http://www.apache.org/licenses/LICENSE-2.0
 *  * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. */
package ru.caffeineim.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 * Описание: Класс предназначен для ввода регистрационных данных
 * @version 0.0.1  10.12.2008
 * @author Renat Nasyrov
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

                JFrame contactList = new ContactsFrame();
                contactList.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                contactList.setLocation(800, 300);
                contactList.setVisible(true);
                                    
            }
//        private void delButActionPerformed(java.awt.event.ActionEvent evt) {                                       
//        cl.removeContact(cl.getSelectedContact());
//        }
        }); 
        
        Box hbox = Box.createHorizontalBox();
        hbox.add(btnLogin);
        hbox.add(Box.createHorizontalStrut(4));
        
        JButton btnProxy = new JButton("Сервер/Прокси");
        btnProxy.setMnemonic(KeyEvent.VK_C);
        btnProxy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame ps = new ProxySet();
                ps.pack();
                ps.setVisible(true);
                ps.setLocation(500, 300);
                ps.setVisible(true);
            }
        });
        hbox.add(btnProxy);
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
