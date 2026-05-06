package dao;
import config.Conexion;
import java.sql.*;

public class VotoBD {

    public boolean registrarVoto(int idUsuario, int idCandidato, int idEleccion) {

        try {
            Connection con = Conexion.conectar();

            // Se verifica en la base de datos que el usuario ya votó
            String verificar = "SELECT * FROM votos WHERE id_usuario = ? AND id_eleccion = ?";
            PreparedStatement ps1 = con.prepareStatement(verificar);
            ps1.setInt(1, idUsuario);
            ps1.setInt(2, idEleccion);

            ResultSet rs = ps1.executeQuery();

            if (rs.next()) {
                return false; //Quiere decir que el usuario ya votó
            }

            String insertar = "INSERT INTO votos (id_usuario, id_candidato, id_eleccion) VALUES (?, ?, ?)";
            PreparedStatement ps2 = con.prepareStatement(insertar);

            ps2.setInt(1, idUsuario);
            ps2.setInt(2, idCandidato);
            ps2.setInt(3, idEleccion);

            ps2.executeUpdate();

            return true;

        } catch (Exception e) {
            System.out.println("Error al votar: " + e);
            return false;
        }
    }
}