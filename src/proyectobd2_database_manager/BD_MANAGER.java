/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectobd2_database_manager;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author micha
 */
public class BD_MANAGER {
    private String HOST = "localhost";
    private String PORT = "3306";
    private String DATABASE = "maquillaje";
    private String USER = "michael";         
    private String PASSWORD = "1111";
    private String URL= "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE
            + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    
    public Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println(" Conexión exitosa a la base de datos: " + DATABASE);
        } catch (SQLException e) {
            System.out.println("Error al conectar: " + e.getMessage());
        }
        return connection;
    }
    
    
}
