package Todo_Project.db;

import java.sql.*;

public class db {

    Connection conn = null;

    public Connection initialize() {
        String url = "jdbc:mysql://localhost:3306/todo_list";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, "todo_admin", "admin");

            String str = "create table if not exists todo " + 
                            "(id INTEGER AUTO_INCREMENT not NULL PRIMARY KEY, "+ 
                            "task VARCHAR(200), "+
                            "status boolean DEFAULT false)";
            Statement stmt = conn.createStatement();
            stmt.execute(str);

            return conn;
        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }

    }

    public Connection getConnection(){
        return conn;
    }
}
