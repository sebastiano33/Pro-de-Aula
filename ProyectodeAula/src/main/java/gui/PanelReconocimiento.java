package gui;

import org.opencv.core.*;
import org.opencv.videoio.VideoCapture;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

public class PanelReconocimiento extends JPanel {

    private VideoCapture camera;
    private CascadeClassifier detector;

    public PanelReconocimiento() {

        // ================= CARGAR CASCADE =================
        try {
            URL resource = getClass().getResource("/haarcascade_frontalface_default.xml");

            if (resource == null) {
                System.out.println("❌ No se encontró el XML en resources");
                return;
            }

            String ruta = resource.toURI().getPath();
            detector = new CascadeClassifier("C:/opencv2/haarcascade_frontalface_default.xml");

            if (detector.empty()) {
                System.out.println("❌ ERROR: Cascade no cargado");
            } else {
                System.out.println("✅ Cascade cargado correctamente");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // ================= ABRIR CÁMARA =================
        camera = new VideoCapture(0);

        if (!camera.isOpened()) {
            System.out.println("❌ ERROR: No se pudo abrir la cámara");
        } else {
            System.out.println("✅ Cámara iniciada");
        }

        // ================= TIMER =================
        Timer timer = new Timer(30, e -> repaint());
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (camera == null || detector == null) return;

        Mat frame = new Mat();

        // Leer frame
        if (!camera.read(frame) || frame.empty()) {
            return;
        }

        // Detectar rostros
        MatOfRect rostros = new MatOfRect();
        detector.detectMultiScale(frame, rostros);

        // Dibujar rectángulos
        for (Rect rect : rostros.toArray()) {
            Imgproc.rectangle(frame, rect, new Scalar(0, 255, 0), 2);
        }

        // Convertir a imagen
        Image img = org.opencv.highgui.HighGui.toBufferedImage(frame);

        // Dibujar en pantalla
        g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
    }
}