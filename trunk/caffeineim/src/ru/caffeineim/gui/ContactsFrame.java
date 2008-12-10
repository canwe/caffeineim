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
 * Описание: Класс представляет собой контейнер с контактами
 * @version 0.0.1  10.12.2008
 * @author Renat Nasyrov
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import ru.caffeineim.protocols.icq.contacts.*;


public class ContactsFrame extends JFrame implements ActionListener {
    
    private static final String CMD_HISTORY = "history";
    private static final String titleStr = "Список собеседников";
    private static final String onlineStr = "Показать/скрыть оффлайн";
    private static final String groupStr = "Отображать/скрыть группы";
    private static final String soundStr = "Включить/отключить звуки";
    private static final String privateStr = "Вывести приватные списки";
    private static final String historyStr = "История сообщений";
    private static final String toolsStr = "Настройки";
    private static final String infoStr = "Информация о Контакте";

    public ContactsFrame() {

        setTitle(titleStr);
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

        btn = createToolBarButton("on-line.png", "on-line", onlineStr);
        tb.add(btn);
        
        btn = createToolBarButton("group.png", "group", groupStr);
        tb.add(btn);
        
        btn = createToolBarButton("sound.png", "sound", soundStr);
        tb.add(btn);

        btn = createToolBarButton("noprivate.png", "private", privateStr);
        tb.add(btn);

        btn = createToolBarButton("history.png", "history", historyStr);
        tb.add(btn);

        btn = createToolBarButton("tools.png", "tools", toolsStr);
        tb.add(btn);
        
        btn = createToolBarButton("info.png", "info", infoStr);
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
        
        if (CMD_HISTORY.equals(cmd)) {
            HistoryFrame f = new HistoryFrame();
            f.setLocation(500, 300);
            f.setVisible(true);
        }
    }
     
}