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

        
        Timer timer = new Timer(2000, e -> {

            String base = System.getProperty("user.dir");
            String correoLimpio = correo.trim().toLowerCase();

            File carpeta = new File(base + "/dataset/" + correoLimpio);

            System.out.println("Buscando en: " + carpeta.getAbsolutePath());

            File[] archivos = carpeta.listFiles();

            
            if (!carpeta.exists() || archivos == null || archivos.length == 0) {

                JOptionPane.showMessageDialog(
                    this,
                    "No hay datos faciales para este usuario"
                );

                return;
            }

            
            Mat rostroActual = panel.obtenerRostro();

            if (rostroActual == null) {

                System.out.println("No se detectó rostro");
                return;
            }

            
            boolean accesoPermitido = false;

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
                    "Resultado REAL: " + resultado
                );

                
                if (resultado < 35) {

                    accesoPermitido = true;
                    break;
                }
            }

            
            if (accesoPermitido) {

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

                System.out.println("Rostro no reconocido");
            }

        });

        timer.start();
    }
}