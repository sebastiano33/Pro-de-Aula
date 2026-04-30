package gui;

import org.opencv.core.*;
import org.opencv.videoio.VideoCapture;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class PanelReconocimiento extends JPanel {

    private VideoCapture camera;
    private CascadeClassifier detector;
    private Mat frame;

    public PanelReconocimiento() {

        util.OpenCVLoader.loadLibrary();

        camera = new VideoCapture(0);
        frame = new Mat();

        
        String ruta = System.getProperty("user.dir") + "/haarcascade_frontalface_default.xml";
        System.out.println("Ruta XML: " + ruta);

        detector = new CascadeClassifier(ruta);

        if (detector.empty()) {
            System.out.println("No se cargó el clasificador");
            JOptionPane.showMessageDialog(this, "Error cargando el XML de reconocimiento facial");
            return;
        } else {
            System.out.println("Clasificador cargado correctamente");
        }

        
        Timer timer = new Timer(30, e -> repaint());
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (camera == null || !camera.isOpened()) return;
        if (detector.empty()) return;

        camera.read(frame);

        if (!frame.empty()) {

            Mat gris = new Mat();
            Imgproc.cvtColor(frame, gris, Imgproc.COLOR_BGR2GRAY);

            MatOfRect rostros = new MatOfRect();
            detector.detectMultiScale(gris, rostros);

            for (Rect rect : rostros.toArray()) {
                Imgproc.rectangle(frame, rect, new Scalar(0, 255, 0), 2);
            }

            BufferedImage img = matToBufferedImage(frame);
            g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
        }
    }

    
    public Mat obtenerRostro() {

        if (frame.empty() || detector.empty()) return null;

        Mat gris = new Mat();
        Imgproc.cvtColor(frame, gris, Imgproc.COLOR_BGR2GRAY);

        MatOfRect rostros = new MatOfRect();
        detector.detectMultiScale(gris, rostros);

        Rect[] faces = rostros.toArray();

        if (faces.length > 0) {
            Rect rect = faces[0];

            Mat rostro = new Mat(gris, rect);
            Imgproc.resize(rostro, rostro, new Size(200, 200));

            return rostro;
        }

        return null;
    }

    
    private BufferedImage matToBufferedImage(Mat mat) {
        int type = BufferedImage.TYPE_BYTE_GRAY;

        if (mat.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }

        byte[] b = new byte[(int) (mat.total() * mat.channels())];
        mat.get(0, 0, b);

        BufferedImage img = new BufferedImage(mat.cols(), mat.rows(), type);
        img.getRaster().setDataElements(0, 0, mat.cols(), mat.rows(), b);

        return img;
    }

    
    public void cerrarCamara() {
        if (camera != null && camera.isOpened()) {
            camera.release();
            System.out.println("Cámara liberada");
        }
    }
}