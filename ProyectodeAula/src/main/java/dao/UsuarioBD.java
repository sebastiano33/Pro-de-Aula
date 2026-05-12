package dao;

import config.Conexion;
import java.sql.*;

public class UsuarioBD {

    public int loginUsuario(String usuario, String contrasena) {

        try {
            Connection con = Conexion.conectar();

            String sql = "SELECT id FROM usuarios WHERE usuario = ? AND contrasena = ? AND estado = 'ACTIVO'";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, usuario);
            ps.setString(2, contrasena);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("id"); // Aqui obtengo el id
            }

        } catch (Exception e) {
            System.out.println("Error en login: " + e);
        }

        return -1; // Valida que esté en la posición correcta
    }

    public String obtenerRol(String user, String pass) {
    String rol = null;
    String sql = "SELECT rol FROM usuarios WHERE usuario = ? AND contrasena = ?";
    
    try (Connection con = Conexion.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {
        
        ps.setString(1, user);
        ps.setString(2, pass);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            rol = rs.getString("rol"); // Retorna 'admin' o 'votante'
        }
    } catch (SQLException e) {
        System.err.println("Error en login: " + e.getMessage());
    }
    return rol;
    }
    
    public int obtenerIdPorCorreo(String correo) {
    int id = -1;

    try {
        
        Connection con = Conexion.conectar();
        String sql = "SELECT id FROM usuarios WHERE correo = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, correo);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            id = rs.getInt("id");
        }

        con.close();

    } catch (Exception e) {
        e.printStackTrace();
    }

    return id;

}
}