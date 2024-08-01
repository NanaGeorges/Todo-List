package Todo_Project;

import javax.sound.midi.SysexMessage;
import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.event.*;
import java.awt.*;

import java.sql.*;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import Todo_Project.db.db;
import Todo_Project.wireframe.todo_inner;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.WindowEvent;

public class layout extends JFrame implements ActionListener {

    JFrame f;
    JTextField tf;
    JButton add;
    static JPanel panel = null, innerPanel;

    static Connection conn;

    static todo_inner todoInner;

    public ArrayList<JPanel> o_panels = new ArrayList<JPanel>(); // Your List

    int ctr = 0;

    layout() {

        f = new JFrame("Todo List");

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
        Date d = new Date();  
        JLabel date = new JLabel("Date - "+formatter.format(d));
        date.setBounds(20, 20, 150, 30);
        
        Format dayFormat = new SimpleDateFormat("EEEE");
        JLabel day = new JLabel(dayFormat.format(d));
        day.setBounds(300, 20, 50, 30);
           
        f.add(date);
        f.add(day);

        tf = new JTextField();
        tf.setBounds(20, 60, 250, 30);

        
        f.add(tf);

        add = new JButton("Add");
        add.setBounds(280, 60, 80, 30);

        f.add(add);

        add.addActionListener(this);

        tf.addKeyListener(new KeyListener(){

            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if(e.getKeyCode() == 10){
                    add.doClick();
                }
                
            }

            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                // TODO Auto-generated method stub
                
            }
            
        });


        f.setSize(400, 450);
        f.setLayout(null);
        f.setVisible(true);

        panel = new JPanel();

        panel.setBackground(Color.LIGHT_GRAY);
        panel.setVisible(true);
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        JScrollPane scrollPane = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(20, 120, 340, 260);

        f.add(scrollPane);
    }

    public void setTodo() {
        try {

            PreparedStatement ps = conn.prepareStatement("select * from todo");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                todoInner.setInner(rs.getInt(1), rs.getString(2), rs.getInt(3));

                o_panels.add(todoInner.getInnerPanel());
                panel.add(o_panels.get(ctr));
                ctr++;

            }

        } catch (Exception excep) {
            excep.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if (e.getSource() == add) {
            String getTodo = tf.getText();

            if (getTodo.length() > 0) {
                try {
                    PreparedStatement ps = conn.prepareStatement("insert into todo (task, status) values (?, 0)");
                    ps.setString(1, getTodo);

                    int ok = ps.executeUpdate();
                    if (ok == 1) {
                        ResultSet rs = ps.getGeneratedKeys();
                        while (rs.next()) {
                            todoInner.setInner(rs.getInt(1), getTodo, 0);
                        }

                        o_panels.add(todoInner.getInnerPanel());
                        panel.add(o_panels.get(ctr));

                        panel.revalidate();
                        ctr++;

                        tf.setText("");
                    }
                } catch (Exception excep) {
                    excep.printStackTrace();
                }
            } else {
                JDialog d = new JDialog(f, "Todo List", true);
                d.setLayout(new FlowLayout());

                JLabel l = new JLabel("Please enter a todo task");
                JButton b = new JButton("OK");

                

                b.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        d.setVisible(false);
                    }
                });

                d.addKeyListener(new KeyListener(){

                    @Override
                    public void keyTyped(java.awt.event.KeyEvent e) {
                        // TODO Auto-generated method stub
                        
                    }

                    @Override
                    public void keyPressed(java.awt.event.KeyEvent e) {
                        if(e.getKeyCode() == 10){
                            b.doClick();
                        }
                        
                    }

                    @Override
                    public void keyReleased(java.awt.event.KeyEvent e) {
                        // TODO Auto-generated method stub
                        
                    }
                    
                });
                d.add(l);
                d.add(b);
                d.setSize(200, 110);
                d.setLocationRelativeTo(f);
                
                d.setVisible(true);

            }

        }

    }

    public static void main(String[] args) {

        db d = new db();
        conn = d.initialize();

        layout l = new layout();
        if (conn != null && panel != null) {

            todoInner = new todo_inner(conn, panel);
            l.setTodo();

        } else {
            JLabel error = new JLabel("Error in Database");
            panel.add(error);
        }

    }
}
