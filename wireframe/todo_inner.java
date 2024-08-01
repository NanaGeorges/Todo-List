package Todo_Project.wireframe;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.*;

import javax.swing.*;

import java.awt.*;
import javax.swing.border.LineBorder;
import javax.swing.text.html.HTML;

import javafx.event.ActionEvent;

public class todo_inner {
    JPanel innerPanel;
    JPanel panel;
    Connection conn = null;

    public void setInner(int id, String todo, int status) throws SQLException {
        innerPanel = new JPanel();
        innerPanel.setName(Integer.toString(id));
        
        innerPanel.setBorder(new LineBorder(Color.LIGHT_GRAY));

        GridLayout layout = new GridLayout();
        layout.setColumns(3);
        layout.setRows(1);
        
        innerPanel.setLayout(layout);
        innerPanel.setPreferredSize(new Dimension(320, 30));

        JLabel todo_label = new JLabel("<HTML>"+todo+"</HTML>", JLabel.CENTER);
        todo_label.setForeground(status == 1 ? Color.GREEN : Color.BLACK);
        

        if (status == 0) {
            JCheckBox todo_status = new JCheckBox();
            todo_status.setSelected(status == 1 ? true : false);
            

            todo_status.addItemListener(new ItemListener() {

                @Override
                public void itemStateChanged(ItemEvent e) {
                    // TODO Auto-generated method stub

                    try {
                        PreparedStatement ps = conn.prepareStatement("update todo set status = ? where id = ? ");
                        ps.setBoolean(1, e.getStateChange() == 1 ? true : false);
                        ps.setInt(2, Integer.parseInt(todo_status.getParent().getName()));

                        int rs = ps.executeUpdate();
                        if (rs == 1) {

                            todo_label.setForeground(e.getStateChange() == 1 ? Color.GREEN : Color.BLACK);
                            if (e.getStateChange() == 1) {
                                todo_status.getParent().remove(todo_status);
                                innerPanel.repaint();
                                innerPanel.revalidate();
                            }
                        }
                    } catch (SQLException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }

                }

            });
            innerPanel.add(todo_status);
        }
        JButton delete = new JButton("Delete");
        delete.setName(Integer.toString(id));
        delete.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {

                try {
                    PreparedStatement ps = conn.prepareStatement("delete from todo where ID = ?");
                    ps.setInt(1, Integer.parseInt(delete.getName()));

                    int b = ps.executeUpdate();
                    if (b == 1) {

                        panel.remove(delete.getParent());
                       
                        panel.repaint();
                        panel.revalidate();
                    }

                } catch (Exception f) {
                    f.printStackTrace();
                }
            }

        });

        innerPanel.add(todo_label);
        innerPanel.add(delete);

        
        panel.revalidate();

    }

    public todo_inner(Connection conn, JPanel panel) {
        this.conn = conn;
        this.panel = panel;
    }

    public JPanel getInnerPanel() {
        return innerPanel;
    }
}
