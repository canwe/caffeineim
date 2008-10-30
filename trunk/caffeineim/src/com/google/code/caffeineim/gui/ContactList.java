package com.google.code.caffeineim.gui;

/**
 *
 * @author renat
 */

import com.google.code.caffeineim.Main;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class ContactList extends JFrame implements ActionListener {

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
        ImageIcon img = getImageIcon(icon);
        JButton btn = new JButton(img);        
        btn.setActionCommand(cmd);
        btn.setToolTipText(tip);
        btn.addActionListener(this);        
        return btn;
    }
    
    private static ImageIcon getImageIcon(String filename) {
        Image img = Toolkit.getDefaultToolkit().getImage(
                Main.class.getResource("images/" + filename));
        return new ImageIcon(img);
    }

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
    }
    
    
}
