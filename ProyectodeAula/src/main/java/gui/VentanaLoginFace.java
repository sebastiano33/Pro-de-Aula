package gui;

import util.ComparadorRostros;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.io.File;

public class VentanaLoginFace extends JFrame {

    private String nombreParaMenu;
    private int idUsuario;

    private boolean procesando = false;

    // Contador de intentos fallidos
    private int intentosFallidos = 0;

    public VentanaLoginFace(int idUsuario, String correo, String nombre) {

        this.idUsuario = idUsuario;
        this.nombreParaMenu = nombre;

        util.OpenCVLoader.loadLibrary();

        setTitle("Face ID");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        PanelReconocimiento panel = new PanelReconocimiento();

        add(panel);

        setVisible(true);

        // TIMER CORREGIDO
        final Timer[] timer = new Timer[1];

        timer[0] = new Timer(2000, e -> {

            if (procesando) {
                return;
            }

            procesando = true;

            String base = System.getProperty("user.dir");

            String correoLimpio =
                    correo.trim().toLowerCase();

            File carpeta = new File(
                    base + "/dataset/" + correoLimpio
            );

            System.out.println(
                    "Buscando en: "
                    + carpeta.getAbsolutePath()
            );

            // Verificar carpeta
            if (!carpeta.exists()) {

                timer[0].stop();

                JOptionPane.showMessageDialog(
                        this,
                        "No existen datos faciales"
                );

                panel.cerrarCamara();

                dispose();

                new login().setVisible(true);

                return;
            }

            File[] archivos = carpeta.listFiles();

            if (archivos == null || archivos.length == 0) {

                procesando = false;

                return;
            }

            // Obtener rostro actual
            Mat rostroActual = panel.obtenerRostro();

            if (rostroActual == null) {

                System.out.println(
                        "Esperando rostro..."
                );

                procesando = false;

                return;
            }

            // =====================================
            // BUSCAR MEJOR COINCIDENCIA
            // =====================================

            double mejorResultado = Double.MAX_VALUE;

            for (File imgFile : archivos) {

                Mat imgGuardada = Imgcodecs.imread(
                        imgFile.getAbsolutePath(),
                        Imgcodecs.IMREAD_GRAYSCALE
                );

                if (imgGuardada.empty()) {
                    continue;
                }

                Imgproc.resize(
                        imgGuardada,
                        imgGuardada,
                        new Size(200, 200)
                );

                double resultado =
                        ComparadorRostros.comparar(
                                rostroActual,
                                imgGuardada
                        );

                System.out.println(
                        imgFile.getName()
                                + " -> "
                                + resultado
                );

                // Guardar mejor coincidencia
                if (resultado < mejorResultado) {
                    mejorResultado = resultado;
                }
            }

            System.out.println(
                    "MEJOR RESULTADO: "
                            + mejorResultado
            );

            // =====================================
            // LOGIN EXITOSO
            // =====================================

            if (mejorResultado < 45) {

                timer[0].stop();

                panel.cerrarCamara();

                dispose();

                JOptionPane.showMessageDialog(
                        this,
                        "Acceso permitido"
                );

                new menuPrincipal(
                        idUsuario,
                        nombreParaMenu
                ).setVisible(true);

            } else {

                intentosFallidos++;

                System.out.println(
                        "Intento fallido: "
                                + intentosFallidos
                );

                // =====================================
                // DEMASIADOS INTENTOS
                // =====================================

                if (intentosFallidos >= 5) {

                    timer[0].stop();

                    JOptionPane.showMessageDialog(
                            this,
                            "Rostro no reconocido"
                    );

                    panel.cerrarCamara();

                    dispose();

                    new login().setVisible(true);
                }
            }

            procesando = false;

        });

        timer[0].start();
    }
}