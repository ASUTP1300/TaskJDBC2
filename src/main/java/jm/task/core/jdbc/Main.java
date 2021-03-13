package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDaoHibernateImpl;
import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;


import java.security.Provider;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
       // реализуйте алгоритм здесь
       User user_1 = new User("Ruslan","Manasipov", (byte) 29);
       User user_2 = new User("Nikola","Chuchva", (byte) 35);
       User user_3 = new User("Avik","Manucharyan", (byte) 28);
       User user_4 = new User("Elnur","Mamedov", (byte) 30);

       ArrayList<User> userList = new ArrayList<>(Arrays.asList( user_1, user_2, user_3, user_4));
       UserServiceImpl userService = new UserServiceImpl();
       userService.createUsersTable();

      for ( User us : userList){
          userService.saveUser(us.getName(), us.getLastName(), us.getAge());
          System.out.println("User с именем  - " + us.getName() + " добавлен в базу");
      }
      System.out.println(userService.getAllUsers());
      userService.cleanUsersTable();
      userService.dropUsersTable();
    }
}
