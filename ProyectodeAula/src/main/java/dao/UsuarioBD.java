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
}