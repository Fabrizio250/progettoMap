/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hasbullateam.escape_room.database;

/**
 *
 * @author gioel
 */

import hasbullateam.escape_room.TabbedMenu;
import hasbullateam.escape_room.type.GameMode;
import hasbullateam.escape_room.type.NameDb;
import hasbullateam.escape_room.type.Result;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

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
                //creaimo le tabelle delle statistiche associate al nuovo utente
                createRecordStats(username);
                return true;

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**OK**/
    private static void createRecordStats(String username){
        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbusername, dbpassword)) {
            int id = DbEscapeRoom.idByUsername(username);
            PreparedStatement preparedStatement;
            String query;

            query = "INSERT INTO statsTris(id_user) VALUES (?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            preparedStatement.executeUpdate();

            query = "INSERT INTO statsPingPong(id_user) VALUES (?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            preparedStatement.executeUpdate();

            query = "INSERT INTO statsMorraCinese(id_user) VALUES (?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

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
                //System.out.println(userFound + " " + passFound);
                //System.out.println(BCrypt.hashpw(password, BCrypt.gensalt()));
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

    /**FUNCTION TO INCREMENT STATS**/
    public static void incrementStats(NameDb db, Result result, GameMode mode){
        String query ="";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbusername, dbpassword)) {
            int id = idByUsername(TabbedMenu.loggedUser);
            if (mode==GameMode.MODE_1v1){
                if (result== Result.WIN_PLAYER1){
                    query = "UPDATE "+ db.getValue() +" SET winPlayer1=winPlayer1+1, losePlayer2=losePlayer2+1  where id_user = "+id;
                }else {
                    if (result==Result.WIN_PLAYER2){
                        query = "UPDATE "+ db.getValue() +" SET winPlayer2=winPlayer2+1, losePlayer1=losePlayer1+1  where id_user = "+id;
                    }
                }
            }else{
                if (mode==GameMode.MODE_1vCPU){
                    if (result==Result.WIN_PLAYER1){
                        query = "UPDATE "+ db.getValue() +" SET winPlayer1=winPlayer1+1, losePC=losePC+1  where id_user = "+id;
                    }else {
                        if (result==Result.WIN_PLAYER2){
                            query = "UPDATE "+ db.getValue() +" SET winPC=winPC+1, losePlayer1=losePlayer1+1  where id_user = "+id;
                        }
                    }
                }
            }
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //restituise tutti i valori delle statistiche all' interno di una matrice

    public static int[] statsMiniGiochi(NameDb nameDb){
        int[] stats = new int[6];

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbusername, dbpassword)) {
            int id = idByUsername(TabbedMenu.loggedUser);

            /**Query per estrarre stats Tris e conservarle nell array**/
            String query = "SELECT winPlayer1,winPlayer2,winPc,losePlayer1,losePlayer2,losePC from "+nameDb.getValue()+ " where id_user = "+id;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                int i = 0;
                while (i<=5){
                    stats[i] = resultSet.getInt(i+1);
                    //System.out.println(stats[i]);
                    i++;
                }
            }


        }catch (SQLException e) {
            e.printStackTrace();
        }

        return stats;
    }

    public static void printStatsDb(NameDb nameDb){
        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbusername, dbpassword)) {
            String query = "SELECT * from "+nameDb.getValue()+";";
            //System.out.println(query);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
               // System.out.println("Id: "+resultSet.getInt(1));
                //System.out.println("Id_user: "+resultSet.getInt(2));
                //System.out.println("WinPlayer1: "+resultSet.getInt(3));
                //System.out.println("WinPlayer2: "+resultSet.getInt(4));
                //System.out.println("WinPC: "+resultSet.getInt(5));
                //System.out.println("losePlayer1: "+resultSet.getInt(6));
               // System.out.println("losePlayer2: "+resultSet.getInt(7));
                //System.out.println("losePC: "+resultSet.getInt(8));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void printAllStatsDb(){
        System.out.println("StatsTris:");
        printStatsDb(NameDb.TRIS);
        System.out.println("StatsPingPong:");
        printStatsDb(NameDb.PINGPONG);
        System.out.println("StatsMorraCinese:");
        printStatsDb(NameDb.MORRACINESE);
    }


}
