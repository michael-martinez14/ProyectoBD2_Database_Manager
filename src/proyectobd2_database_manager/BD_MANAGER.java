/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectobd2_database_manager;
import com.mysql.cj.jdbc.result.ResultSetMetaData;
import java.awt.List;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
    private String HOST = "";
    private String PORT = "";
    private String DATABASE = "";
    private String USER = "";         
    private String PASSWORD = "";
    private String URL= "";

    public String getURL() {
        return URL;
    }

    public void setURL() {
        this.URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE;
    }
    

    public String getHOST() {
        return HOST;
    }

    public void setHOST(String HOST) {
        this.HOST = HOST;
    }

    public String getPORT() {
        return PORT;
    }

    public void setPORT(String PORT) {
        this.PORT = PORT;
    }

    public String getDATABASE() {
        return DATABASE;
    }

    public void setDATABASE(String DATABASE) {
        this.DATABASE = DATABASE;
    }

    public String getUSER() {
        return USER;
    }

    public void setUSER(String USER) {
        this.USER = USER;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }
    


    public BD_MANAGER(String host, String port, String Db, String user, String pass){
        this.DATABASE=Db;
        this.HOST=host;
        this.PORT=port;
        this.USER=user;
        this.PASSWORD=pass;
        setURL();
    }
    public Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.out.println("Error al conectar: " + e.getMessage());
        }
        return connection;
    }
    
    public void mostrarTablas(Connection con, JList lista) {
        String sql = "SHOW TABLES";
        DefaultListModel<String> modelo = new DefaultListModel();
        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

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
                modelo.addElement(rs.getString("Trigger"));
            }
            lista.setModel(modelo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    public void mostrarIndices(Connection con, JList<String> lista) {
    DefaultListModel<String> modelo = new DefaultListModel<>();
    try (Statement st = con.createStatement();
         ResultSet rsTablas = st.executeQuery("SHOW TABLES")) {

        while (rsTablas.next()) {
            String tabla = rsTablas.getString(1);
            try (Statement stIndex = con.createStatement();
                 ResultSet rsIndex = stIndex.executeQuery("SHOW INDEX FROM `" + tabla + "`")) {

                while (rsIndex.next()) {
                    String index = rsIndex.getString("Key_name");
                    String salida = tabla + " -> " + index;
                    modelo.addElement(salida);
                }
            }
        }

        lista.setModel(modelo);

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

            sql.append("   "+nombreCol).append(" ").append(tipo);
            if (tam != null && !tam.isEmpty()) {
                sql.append("(").append(tam).append(")");
            }
            sql.append(", ").append("\n");

            if (pk != null && pk) {
                pks.add(nombreCol);
            }
        }

        if (!pks.isEmpty()) {
            sql.append("   PRIMARY KEY(").append(String.join(",", pks)).append(")").append("\n");
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
    
    public String generarSQLVista(String nombreVista, String consulta) {
        return "CREATE VIEW " + nombreVista + " AS"+"\n" + consulta + ";";
    }

    public void crearVista(Connection con, String sql) {
        try (Statement st = con.createStatement()) {
            st.executeUpdate(sql);
            JOptionPane.showMessageDialog(null, "Vista creada con éxito.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al crear vista: " + e.getMessage());
        }
    }
    
    
    //obtener de los ddl
    public String getDDLTabla(Connection conexion, String nombreTabla) {
    String ddl = "";
    String sql = "SHOW CREATE TABLE " + nombreTabla;
    try (Statement st = conexion.createStatement();
         ResultSet rs = st.executeQuery(sql)) {
        if (rs.next()) {
            ddl = rs.getString("Create Table");
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
    return ddl;
}

public String getDDLView(Connection conexion, String nombreVista) {
    String ddl = "";
    String sql = "SHOW CREATE VIEW " + nombreVista;
    try (Statement st = conexion.createStatement();
         ResultSet rs = st.executeQuery(sql)) {
        if (rs.next()) {
            ddl = rs.getString("Create View");
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
    return ddl;
}

public String getDDLProcedimiento(Connection conexion, String nombreProc) {
    String ddl = "";
    String sql = "SHOW CREATE PROCEDURE " + nombreProc;
    try (Statement st = conexion.createStatement();
         ResultSet rs = st.executeQuery(sql)) {
        if (rs.next()) {
            ddl = rs.getString("Create Procedure");
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
    return ddl;
}

public String getDDLFuncion(Connection conexion, String nombreFuncion) {
    String ddl = "";
    String sql = "SHOW CREATE FUNCTION " + nombreFuncion;
    try (Statement st = conexion.createStatement();
         ResultSet rs = st.executeQuery(sql)) {
        if (rs.next()) {
            ddl = rs.getString("Create Function");
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
    return ddl;
}

public String getDDLTrigger(Connection conexion, String nombreTrigger) {
    String ddl = "";
    String sql = "SHOW CREATE TRIGGER " + nombreTrigger;
    try (Statement st = conexion.createStatement();
         ResultSet rs = st.executeQuery(sql)) {
        if (rs.next()) {
            try {
                ddl = rs.getString("SQL Original Statement");
            } catch (SQLException ex) {
                ddl = rs.getString("Create Trigger");
            }
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
    return ddl;
}

public String getDDLUsuario(Connection conexion, String nombreUsuario) {
    String ddl = "";
    String sql = "SHOW CREATE USER '" + nombreUsuario + "'@'localhost'";
    try (Statement st = conexion.createStatement();
         ResultSet rs = st.executeQuery(sql)) {
        if (rs.next()) {
            ddl = rs.getString(1);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
    return ddl;
}

    public void ejecutarSQL_DDLmodified(Connection conexion, String sql) {
        try (Statement st = conexion.createStatement()) {
            for (String sentencia : sql.split(";")) {
                if (!sentencia.trim().isEmpty()) {
                    st.executeUpdate(sentencia.trim());
                }
            }
            JOptionPane.showMessageDialog(null, "Cambios a DDL aplicados con éxito.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    @Override
    public String toString() {
        return USER+" -->"+ DATABASE;
    }
    
    public String toString2() {
        
        return HOST + ";" + PORT + ";" + DATABASE + ";" + USER + ";" + PASSWORD;
    }
    
    
    public JTable ejecutarSQL_SELECT(Connection con, String sql, JTable tabla) {
    try (Statement stmt = con.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
        ResultSetMetaData metaData = (ResultSetMetaData) rs.getMetaData();
        int columnas = metaData.getColumnCount();
        DefaultTableModel modelo = new DefaultTableModel();
        for (int i = 1; i <= columnas; i++) {
            modelo.addColumn(metaData.getColumnName(i));
        }
        while (rs.next()) {
            Object[] fila = new Object[columnas];
            for (int i = 1; i <= columnas; i++) {
                fila[i - 1] = rs.getObject(i);
            }
            modelo.addRow(fila);
        }
        tabla.setModel(modelo);
        
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null,
                "Error al ejecutar consulta: " + e.getMessage());
    }

    return tabla;
}
    
    public JTable ejecutarSQL_SELECT_Tabla(Connection con, String sql, JTable tabla, String nombreTabla) {
    try (Statement stmt = con.createStatement()) {

        String sqlColumnas = "SHOW COLUMNS FROM " + nombreTabla;
        ResultSet rsColumnas = stmt.executeQuery(sqlColumnas);

        DefaultTableModel modelo = new DefaultTableModel();
        while (rsColumnas.next()) {
            modelo.addColumn(rsColumnas.getString("Field")); 
        }
        rsColumnas.close();

        ResultSet rs = stmt.executeQuery(sql);
        int columnas = modelo.getColumnCount();

        while (rs.next()) {
            Object[] fila = new Object[columnas];
            for (int i = 1; i <= columnas; i++) {
                fila[i - 1] = rs.getObject(i);
            }
            modelo.addRow(fila);
        }
        rs.close();
        tabla.setModel(modelo);

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null,
                "Error al ejecutar consulta: " + e.getMessage());
    }
    return tabla;
}

    
}
