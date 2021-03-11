package jm.task.core.jdbc.util;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import static java.lang.Class.*;

public class Util {

    private static  Util instance;
    // реализуйте настройку соеденения с БД
    private final static String URL = "jdbc:mysql://localhost:3306/mydbtest";
    private final static String URLFIXED =
            "jdbc:mysql://localhost:3306/mydbtest?useUnicode=true&useSSL=true&useJDBCCompliantTimezoneShift=true" +
                    "&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private final static String USERNAME = "root";
    private final static String PASSWORD = "root";
    private Connection connection;



     private Util() {
        try {
            connection = DriverManager.getConnection(URLFIXED, USERNAME, PASSWORD);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public static Util getInstance() throws SQLException {
         if (instance == null){
             instance = new Util();
         } else if (instance.getConnection().isClosed()){
             instance = new Util();
         }
         return instance;
    }

}





