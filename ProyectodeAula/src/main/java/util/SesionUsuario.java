package util;

import gui.admin.PanelGestionVotaciones;

public class SesionUsuario {
    private static int idUsuario = -1;

    public static void setIdUsuario(int id) {
        idUsuario = id;
    }

    public static int getIdUsuario() {
        return idUsuario;
    }

    public static boolean haVotado(int idCandidato) {
        return PanelGestionVotaciones.usuariosQueVotaron.contains(idUsuario);
    }
}
