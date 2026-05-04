package gui;

import util.ComparadorRostros;
import org.opencv.core.*;
import org.opencv.videoio.VideoCapture;
import org.opencv.imgcodecs.Imgcodecs;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class VentanaLoginFace extends JFrame {
    private String nombreParaMenu;
    private VideoCapture camera;
    private Mat frame;

    public VentanaLoginFace(String correo, String nombre) {
        this.nombreParaMenu = nombre;

        util.OpenCVLoader.loadLibrary();

        camera = new VideoCapture(0);
        frame = new Mat();

        setTitle("Face ID");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

      
        PanelReconocimiento panel = new PanelReconocimiento();
        add(panel);

        setVisible(true);

        Timer timer = new Timer(2000, e -> {

            camera.read(frame);

            if (!frame.empty()) {

                File carpeta = new File("dataset/" + correo);

                if (!carpeta.exists() || carpeta.listFiles() == null) {
                    JOptionPane.showMessageDialog(this, "No hay datos faciales para este usuario");
                    return;
                }

                for (File imgFile : carpeta.listFiles()) {

                    Mat imgGuardada = Imgcodecs.imread(imgFile.getAbsolutePath());

                    double resultado = ComparadorRostros.comparar(frame, imgGuardada);

                    System.out.println("Comparación: " + resultado);

                    if (resultado < 5000000) {

                        camera.release();
                        dispose();

                        JOptionPane.showMessageDialog(this, "Acceso permitido");

                        new menuPrincipal(nombreParaMenu).setVisible(true);
                        return;
                    }
                }
            }
        });

        timer.start();
    }
}