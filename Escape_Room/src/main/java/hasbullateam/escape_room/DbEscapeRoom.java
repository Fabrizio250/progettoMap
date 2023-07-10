/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hasbullateam.escape_room;

/**
 *
 * @author gioel
 */

import org.mindrot.jbcrypt.BCrypt;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbEscapeRoom {
    private static final String jdbcUrl = "jdbc:h2:./data/dbEscapeRoom"; // Percorso e nome del database
    private static final String dbusername = "hasbullaTeam";
    private static final String dbpassword = "pippoKill";
    private static String seed = "$2a$10$KhSFa9Vxyhz7x0mXflQWAu";
    
    public DbEscapeRoom(){};

    /**OK**/
    public static void createTable(){
        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbusername, dbpassword);
             Statement statement = connection.createStatement()) {

            // Creazione della tabella user
            String createTableSql = "CREATE TABLE IF NOT EXISTS user (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "username VARCHAR(255) UNIQUE NOT NULL," +
                    "password VARCHAR(255) NOT NULL)";
            statement.executeUpdate(createTableSql);

            // Creazione della tabella statsTris
            createTableSql = "CREATE TABLE IF NOT EXISTS statsTris (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "id_user INT NOT NULL DEFAULT 0," +
                    "winPlayer1 INT DEFAULT 0," +
                    "winPlayer2 INT DEFAULT 0,"+
                    "winPC INT DEFAULT 0," +
                    "losePlayer1 INT DEFAULT 0," +
                    "losePlayer2 INT DEFAULT 0,"+
                    "losePC INT DEFAULT 0," +
                    "drawPlayer1 INT DEFAULT 0," +
                    "drawPlayer2 INT DEFAULT 0,"+
                    "drawPC INT DEFAULT 0," +
                    "FOREIGN KEY (id_user) REFERENCES user(id))";
            statement.executeUpdate(createTableSql);

            //creazione della tabella statsPingPong
            createTableSql = "CREATE TABLE IF NOT EXISTS statsPingPong (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "id_user INT NOT NULL," +
                    "winPlayer1 INT DEFAULT 0," +
                    "winPlayer2 INT DEFAULT 0,"+
                    "winPC INT DEFAULT 0," +
                    "losePlayer1 INT DEFAULT 0," +
                    "losePlayer2 INT DEFAULT 0,"+
                    "losePC INT DEFAULT 0," +
                    "drawPlayer1 INT DEFAULT 0," +
                    "drawPlayer2 INT DEFAULT 0,"+
                    "drawPC INT DEFAULT 0," +
                    "FOREIGN KEY (id_user) REFERENCES user(id))";
            statement.executeUpdate(createTableSql);

            //creazione della tabella statsMorraCinese
            createTableSql = "CREATE TABLE IF NOT EXISTS statsMorraCinese (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "id_user INT NOT NULL DEFAULT 0," +
                    "winPlayer1 INT DEFAULT 0," +
                    "winPlayer2 INT DEFAULT 0,"+
                    "winPC INT DEFAULT 0," +
                    "losePlayer1 INT DEFAULT 0," +
                    "losePlayer2 INT DEFAULT 0,"+
                    "losePC INT DEFAULT 0," +
                    "drawPlayer1 INT DEFAULT 0," +
                    "drawPlayer2 INT DEFAULT 0,"+
                    "drawPC INT DEFAULT 0," +
                    "FOREIGN KEY (id_user) REFERENCES user(id))";
            statement.executeUpdate(createTableSql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**OK**/
    //1 = user inserito correttamnete, 0 = user non inserito o user già esistente con quell' username
    public static boolean insertNewUser(String username, String password){
        if (!userExists(username)){
            try (Connection connection = DriverManager.getConnection(jdbcUrl, dbusername, dbpassword)) {

                String query = "INSERT INTO user(username,password) VALUES (?,?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1,username);
                preparedStatement.setString(2, BCrypt.hashpw(password, seed));  //criptimao la password
                preparedStatement.executeUpdate();
                return true;

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    /**OK**/
    //1 = user eliminato, 0 = user non eliminato
    public static boolean deleteUser(String username){
        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbusername, dbpassword)) {

            String query = "DELETE FROM user WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,username);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return !userExists(username);
    }


    /**OK**/
    // 1 = modificato correttamente, 0 = non modificato
    public static boolean modifyUserPassword(String username, String password){
        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbusername, dbpassword)) {
            int id = idByUsername(username);

            String query = "UPDATE user SET password = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,BCrypt.hashpw(password, seed));
            preparedStatement.setInt(2,id);
            preparedStatement.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean userLogin(String username, String password){
        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbusername, dbpassword)) {
            String query = "SELECT * from user where username = ? ";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if( resultSet.next()){
                String userFound = resultSet.getString(2);
                String passFound = resultSet.getString(3);
                System.out.println(userFound + " " + passFound);
                System.out.println(BCrypt.hashpw(password, BCrypt.gensalt()));
                if (userFound.equals(username) && passFound.equals(BCrypt.hashpw(password, seed))){
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;  
    }
    
    //0 = no user , >=1 = idUser
    public static int idByUsername(String username){
        Integer userId = 0;
        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbusername, dbpassword)) {
            String query = "SELECT* from user where username = ? ";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if( resultSet.next()){
                userId = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userId;
    }

    public static boolean userExists(String username){
        boolean userExists = false;
        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbusername, dbpassword)) {

            String query = "SELECT* from user where username = ? ";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if( resultSet.next()){
                /*
                String userFound = resultSet.getString(2);
                String passFoun = resultSet.getString(3);
                System.out.println(userFound + passFoun);
                */
                
                userExists = true;
            }else {
                //NO user found
                userExists = false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userExists;
    }

}
