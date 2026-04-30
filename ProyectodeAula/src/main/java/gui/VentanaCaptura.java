package gui;

import gui.registro;
import util.OpenCVLoader;
import org.opencv.core.*;
import org.opencv.videoio.VideoCapture;
import org.opencv.imgcodecs.Imgcodecs;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VentanaCaptura extends JFrame {
    private JFrame ventanaAnterior;
    private String nombre;
    private VideoCapture camera;
    private JLabel labelCamara;
    private Mat frame;
    private String nombrePersona;
    private registro registroPadre;

    public VentanaCaptura(String nombrePersona, registro aThis) {
        this.nombrePersona = nombrePersona;
         this.nombre = nombre;
        this.ventanaAnterior = ventanaAnterior;
        this.registroPadre = aThis;
            
         OpenCVLoader.loadLibrary();    

        setTitle("Capturar foto - " + nombrePersona);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        labelCamara = new JLabel();
        add(labelCamara, BorderLayout.CENTER);

        JButton btnCapturar = new JButton("Capturar foto");
        btnCapturar.setBackground(new Color(25,118,210));
        btnCapturar.setForeground(Color.WHITE);
        add(btnCapturar, BorderLayout.SOUTH);

        camera = new VideoCapture(0);
        frame = new Mat();

        Timer timer = new Timer(30, e -> {
            if (camera.isOpened()) {
                camera.read(frame);
                if (!frame.empty()) {
                    labelCamara.setIcon(new ImageIcon(matToBufferedImage(frame)));
                }
            }
        });
        timer.start();

        btnCapturar.addActionListener(e -> {
            guardarFoto();
            camera.release();
            dispose();
        });
    }

    // 💾 GUARDAR POR PERSONA
    private void guardarFoto() {
        try {
            // Carpeta por persona
            File carpeta = new File("dataset/" + nombrePersona);

            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }

            // Contador automático
            int numero = carpeta.list().length + 1;

            String ruta = carpeta.getAbsolutePath() + "/foto_" + numero + ".png";

            Imgcodecs.imwrite(ruta, frame);
            Imgcodecs.imwrite(ruta, frame);

            registroPadre.rostroCapturadoExitosamente();

            JOptionPane.showMessageDialog(this, "Foto guardada:\n" + ruta);
            javax.swing.JOptionPane.showMessageDialog(this, "Foto guardada correctamente");

// cerrar cámara
camera.release();

// cerrar solo la ventana de cámara
this.dispose();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private BufferedImage matToBufferedImage(Mat mat) {
        int type = BufferedImage.TYPE_BYTE_GRAY;

        if (mat.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }

        int bufferSize = mat.channels() * mat.cols() * mat.rows();
        byte[] b = new byte[bufferSize];
        mat.get(0, 0, b);

        BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), type);
        final byte[] targetPixels = ((java.awt.image.DataBufferByte)
                image.getRaster().getDataBuffer()).getData();

        System.arraycopy(b, 0, targetPixels, 0, b.length);

        return image;
    }
}

