/** * Copyright 2008 Caffeine-Soft Group * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at *  * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */

package com.google.code.caffeineim.gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author Urfin, Renat
 */
public class MessageWindow extends JFrame implements ActionListener{
    
    private static String uin="345678"; //[] array of contacts
    private static int a;               //[] quantity of contacts
    private static int v;               //int v - version of type dialog 1=Minimum 2=Standart 3=Advanced


    public MessageWindow() {
        
        JPanel contentPane = new JPanel(new GridBagLayout());
        JPanel contentPaneUp = new JPanel(new GridBagLayout());
        JTextArea areaText = new JTextArea();

        JScrollPane answerScrollPane = new JScrollPane(areaText); 
        answerScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        answerScrollPane.setPreferredSize(new Dimension(350, 150));
        answerScrollPane.setMinimumSize(new Dimension(100, 20));

        JTabbedPane tabUINs  = new JTabbedPane();
        tabUINs.addTab(uin, answerScrollPane);

        contentPaneUp.add(tabUINs, new GridBagConstraints(GridBagConstraints.RELATIVE,0,4,1,1.1,1,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0,0,0,0),0,0));

        JPanel contentPaneGND = new JPanel(new GridBagLayout());

        JTextPane paneTextGND = new JTextPane();
        JScrollPane answerScrollPaneGND = new JScrollPane(paneTextGND); 
        answerScrollPaneGND.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        answerScrollPaneGND.setPreferredSize(new Dimension(350, 100));
        answerScrollPaneGND.setMinimumSize(new Dimension(100, 20));

        if (v>1) {
            //paneService for additional buttons
            JPanel paneService = new JPanel();
            //**********************************
            contentPaneGND.add(paneService, new GridBagConstraints(0, 0, 4, 1, 1.1, 1,
                    GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        }
       contentPaneGND.add(answerScrollPaneGND, new GridBagConstraints(GridBagConstraints.RELATIVE,1,4,1,1.1,1,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0,0,0,0),0,0));
       JButton buttonSend = new JButton("Отправить");
       contentPaneGND.add(buttonSend, new GridBagConstraints(0,GridBagConstraints.RELATIVE,2,0,1,1.1,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0),0,0));
       
       JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,contentPaneUp,contentPaneGND);
        splitPane.setOneTouchExpandable(true);
        splitPane.setResizeWeight(0.5);
        contentPane.add(splitPane, new GridBagConstraints(GridBagConstraints.RELATIVE,GridBagConstraints.RELATIVE,1,1,1.1,1,
                GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH, new Insets(0,0,0,0),0,0));
       
        setContentPane(contentPane);
        setDefaultLookAndFeelDecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(400, 300);
        pack();
    }
    
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
