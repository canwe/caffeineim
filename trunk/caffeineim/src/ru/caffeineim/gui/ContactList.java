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

/**
 *
 * @author renat
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class ContactList extends JFrame implements ActionListener {
    
    private static final String CMD_HISTORY = "history";

    public ContactList() {

        setTitle("Список собеседников");
        setSize(300, 500);

        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        
        JToolBar toolBar = createToolBar();
        c.add(toolBar, BorderLayout.NORTH);
        
    }

    private JToolBar createToolBar() {
        
        JButton btn;
        JToolBar tb = new JToolBar();
        tb.setFloatable(false);

        btn = createToolBarButton("on-line.png", "on-line", "Показать/скрыть оффлайн");
        tb.add(btn);
        
        btn = createToolBarButton("group.png", "group", "Отображать/скрыть группы");
        tb.add(btn);
        
        btn = createToolBarButton("sound.png", "sound", "Включить/отключить звуки");
        tb.add(btn);

        btn = createToolBarButton("noprivate.png", "private", "Вывести приватные списки");
        tb.add(btn);

        btn = createToolBarButton("history.png", "history", "История сообщений");
        tb.add(btn);

        btn = createToolBarButton("tools.png", "tools", "Настройки");
        tb.add(btn);
        
        btn = createToolBarButton("info.png", "info", "Информация о Контакте");
        tb.add(btn);

        return tb;

    }
    
    private JButton createToolBarButton(String icon, String cmd, String tip) {
        ImageIcon img = UIHelper.getIcon(icon);
        JButton btn = new JButton(img);        
        btn.setActionCommand(cmd);
        btn.setToolTipText(tip);
        btn.addActionListener(this);        
        return btn;
    }
    
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals(CMD_HISTORY)) {
            HistoryFrame f = new HistoryFrame();
            f.setLocation(500, 300);
            f.setVisible(true);
        }
    }
    
    
}
