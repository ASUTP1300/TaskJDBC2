package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    public void createUsersTable()  {
        Util connect = new Util();
        String sqlCommand = "CREATE TABLE IF NOT EXISTS Users (id INT PRIMARY KEY AUTO_INCREMENT," +
                " name VARCHAR(32),  lastName VARCHAR(32), age smallint)";
        Statement statement = null;
        try {
            statement = connect.getConnection().createStatement();
            statement.executeUpdate(sqlCommand);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (connect.getConnection() != null) {
                    connect.getConnection().close();
                }
                if (statement != null){
                    statement.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
    public void dropUsersTable()  {
        Util connect = new Util();
        String sqlCommand = "DROP TABLE IF EXISTS Users";
        Statement statement = null;
        try {
           statement = connect.getConnection().createStatement();
           statement.executeUpdate(sqlCommand);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (connect.getConnection() != null) {
                    connect.getConnection().close();
                }
                if (statement != null){
                    statement.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

    }
    @Override
    public void saveUser(String name, String lastName, byte age) {
        Util connect = new Util();
        String sqlCommand = "INSERT INTO Users (name, lastName, age) VALUES ( ?, ?, ?)";
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connect.getConnection().prepareStatement(sqlCommand);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (connect.getConnection() != null) {
                    connect.getConnection().close();
                }
                if (preparedStatement != null){
                    preparedStatement.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
    @Override
    public void removeUserById(long id) {
        Util connect = new Util();
        String sqlCommand = "DROP FROM Users WHERE id = ?";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connect.getConnection().prepareStatement(sqlCommand);
            preparedStatement.setLong(1, id);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (connect.getConnection() != null) {
                    connect.getConnection().close();
                }
                if (preparedStatement != null){
                    preparedStatement.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
    @Override
    public List<User> getAllUsers() {
        Util connect = new Util();
        String sqlCommand = "SELECT id, name, lastName, age FROM Users";
        Statement statement = null;
        List<User> users = new ArrayList<>();
        try {
            statement = connect.getConnection().createStatement();
            ResultSet result = statement.executeQuery(sqlCommand);

            while (result.next()){
                User user = new User();
                user.setId(result.getLong("id"));
                user.setName(result.getString("name"));
                user.setLastName(result.getString("lastName"));
                user.setAge(result.getByte("age"));
                users.add(user);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }  finally {
            try {
                if (connect.getConnection() != null) {
                    connect.getConnection().close();
                }
                if (statement != null){
                    statement.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return users;
    }
    @Override
    public void cleanUsersTable() {
        Util connect = new Util();
        String sqlCommand = "TRUNCATE TABLE Users";
        Statement statement = null;
        try {
            statement = connect.getConnection().createStatement();
            statement.executeUpdate(sqlCommand);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (connect.getConnection() != null) {
                    connect.getConnection().close();
                }
                if (statement != null){
                    statement.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

    }
}
