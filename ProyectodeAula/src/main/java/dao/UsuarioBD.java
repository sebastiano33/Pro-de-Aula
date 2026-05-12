package dao;

import config.Conexion;
import java.sql.*;

public class UsuarioBD {

    public boolean loginUsuario(String usuario, String contraseña) {

        try {
            Connection con = Conexion.conectar();

            String sql = "SELECT * FROM usuarios WHERE usuario = ? AND contrasena = ?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, usuario);
            ps.setString(2, contraseña);

            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (Exception e) {
            System.out.println("Error en login: " + e);
            return false;
        }
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
}