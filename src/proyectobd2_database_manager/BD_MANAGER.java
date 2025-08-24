/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectobd2_database_manager;
import java.awt.List;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListModel;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author micha
 */
public class BD_MANAGER {
    private String HOST = "localhost";
    private String PORT = "3306";
    private String DATABASE = "sismosdb";
    private String USER = "michael";         
    private String PASSWORD = "1111";
    private String URL= "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE;


    public BD_MANAGER(String host, String port, String Db, String user, String pass){
        this.DATABASE=Db;
        this.HOST=host;
        this.PORT=port;
        this.USER=user;
        this.PASSWORD=pass;
    }
    public Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexion exitosa a la base de datos: " + DATABASE);
        } catch (SQLException e) {
            System.out.println("Error al conectar: " + e.getMessage());
        }
        return connection;
    }
    
    public void mostrarTablas(Connection con, JList lista) {
        String sql = "SHOW TABLES";
        DefaultListModel<String> modelo = new DefaultListModel();
        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("=== TABLAS EN LA BASE DE DATOS ===");
            while (rs.next()) {
                System.out.println(rs.getString(1));
                modelo.addElement(rs.getString(1));
            }
            lista.setModel(modelo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void mostrarVistas(Connection con, JList<String> lista) {
        DefaultListModel<String> modelo = new DefaultListModel<>();
        String sql = "SHOW FULL TABLES WHERE Table_type = 'VIEW'";
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                // Columna 1 = nombre de la vista, col 2 = tipo
                System.out.println("");
                modelo.addElement(rs.getString(1));
            }
            lista.setModel(modelo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void mostrarProcedimientos(Connection con, JList<String> lista) {
        DefaultListModel<String> modelo = new DefaultListModel<>();
        String db = this.DATABASE;
        String sql = "SHOW PROCEDURE STATUS WHERE Db = '" + db + "'";
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                // Columna "Name" contiene el nombre del procedimiento
                modelo.addElement(rs.getString("Name"));
            }
            lista.setModel(modelo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void mostrarFunciones(Connection con, JList<String> lista) {
        DefaultListModel<String> modelo = new DefaultListModel<>();
        String db = this.DATABASE;
        String sql = "SHOW FUNCTION STATUS WHERE Db = '" + db + "'";
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                modelo.addElement(rs.getString("Name"));
            }
            lista.setModel(modelo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void mostrarTriggers(Connection con, JList<String> lista) {
        DefaultListModel<String> modelo = new DefaultListModel<>();
        String db = this.DATABASE;
        String sql = "SHOW TRIGGERS FROM `" + db + "`";
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                // Columna "Trigger" trae el nombre
                modelo.addElement(rs.getString("Trigger"));
            }
            lista.setModel(modelo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    public void mostrarIndices(Connection con, JList<String> lista) {
    DefaultListModel<String> modelo = new DefaultListModel<>();
    // 1) Obtener tablas
    try (Statement st = con.createStatement();
         ResultSet rsTablas = st.executeQuery("SHOW TABLES")) {
        while (rsTablas.next()) {
            String tabla = rsTablas.getString(1);
            // 2) Por cada tabla, listar índices
            try (Statement stIdx = con.createStatement();
                 ResultSet rsIdx = stIdx.executeQuery("SHOW INDEX FROM `" + tabla + "`")) {
                while (rsIdx.next()) {
                    String idx = rsIdx.getString("Key_name");
                    boolean notUnique = rsIdx.getInt("Non_unique") == 1;
                    modelo.addElement(tabla + " -> " + idx + (notUnique ? " (NO ÚNICO)" : " (ÚNICO)"));
                }
            }
        }lista.setModel(modelo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void mostrarUsuarios(Connection con, JList<String> lista) {
        DefaultListModel<String> modelo = new DefaultListModel<>();
        String sql = "SELECT CONCAT(User, '@', Host) AS usr FROM mysql.user ORDER BY User, Host";
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                modelo.addElement(rs.getString("usr"));
            }
            lista.setModel(modelo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public String generarSQLTabla(String nombreTabla, JTable tabla) {
        StringBuilder sql = new StringBuilder("CREATE TABLE " + nombreTabla + " ("+"\n");
        ArrayList<String> pks = new ArrayList<>();

        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            String nombreCol = (String) model.getValueAt(i, 0);
            String tipo = (String) model.getValueAt(i, 1);
            String tam = (String) model.getValueAt(i, 2);
            Boolean pk = (Boolean) model.getValueAt(i, 3);

            if (nombreCol == null || nombreCol.isEmpty()) {
                continue;
            }

            sql.append("    "+nombreCol).append(" ").append(tipo);
            if (tam != null && !tam.isEmpty()) {
                sql.append("(").append(tam).append(")");
            }
            sql.append(", ").append("\n");

            if (pk != null && pk) {
                pks.add(nombreCol);
            }
        }

        if (!pks.isEmpty()) {
            sql.append("    PRIMARY KEY(")
                    .append(String.join(",", pks))
                    .append("), ").append("\n");
        }
        // eliminar última coma
        if (sql.toString().endsWith(", ")) {
            sql.setLength(sql.length() - 2);
        }

        sql.append(");");
        return sql.toString();
    }

    public void crearTabla(Connection con, String sql) {
        try (Statement st = con.createStatement()) {
            st.executeUpdate(sql);
            JOptionPane.showMessageDialog(null, "Tabla creada con éxito.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al crear tabla: " + e.getMessage());
        }
    }

}
