package util;


public class Validación {
    public static boolean esNombreValido(String nombre) {
        return nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]{4,}");
    }

    public static boolean esCorreoInstitucional(String correo) {
        return correo.matches("^[a-zA-Z0-9._%+-]+@unicolombo\\.edu\\.co$");
    }

    public static boolean esUsuarioValido(String usuario) {
        return usuario.matches("[a-zA-Z0-9]{4,}");
    }

    public static boolean esCodigoValido(String codigo) {
        return codigo.matches("\\d{6,10}");
    }

    public static boolean esPasswordValida(String pass) {
        return pass.length() >= 4;
    }
}
