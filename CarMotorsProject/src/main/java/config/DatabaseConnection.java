/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author ADMiN
 */
public class DatabaseConnection {
     private static final String host = "jdbc:mysql://localhost:3306/carmotors";
    private static final String user = "root";
    private static final String password = "ivan1703";

    private static Connection con;

    DatabaseConnection() {
        this.con = null;
    }

    public static Connection getConnection() {
        if (con == null) {
            try {
                con = DriverManager.getConnection(host, user, password);
                System.out.println("Conexion exitosa");
            } catch (SQLException e) {
                System.err.println("Error al conectar con la base de datos: " + e.getMessage());
            }
        }

        return con;
    }

    public static void closeConnection() {
        if (con != null) {
            try {
                con.close();
                con = null;
            } catch (SQLException e) {
                System.err.println("Error al desconectar de la base de datos: " + e.getMessage());
            }
        }
    }

    
    
}
