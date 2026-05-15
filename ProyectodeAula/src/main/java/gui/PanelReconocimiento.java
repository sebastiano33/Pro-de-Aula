package gui;

import org.opencv.core.*;
import org.opencv.videoio.VideoCapture;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class PanelReconocimiento extends JPanel {

    private VideoCapture camera;
    private CascadeClassifier detector;
    private Mat frame;

    // Último rostro válido
    private Mat ultimoRostro = null;

    public PanelReconocimiento() {

        util.OpenCVLoader.loadLibrary();

        camera = new VideoCapture(0);

        frame = new Mat();

        String ruta =
                System.getProperty("user.dir")
                + "/haarcascade_frontalface_default.xml";

        System.out.println("Ruta XML: " + ruta);

        detector = new CascadeClassifier(ruta);

        if (detector.empty()) {

            System.out.println("No se cargó el clasificador");

            JOptionPane.showMessageDialog(
                    this,
                    "Error cargando el XML de reconocimiento facial"
            );

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

        if (camera == null || !camera.isOpened()) {
            return;
        }

        if (detector.empty()) {
            return;
        }

        camera.read(frame);

        if (!frame.empty()) {

            Mat gris = new Mat();

            Imgproc.cvtColor(
                    frame,
                    gris,
                    Imgproc.COLOR_BGR2GRAY
            );

            MatOfRect rostros = new MatOfRect();

            detector.detectMultiScale(
                    gris,
                    rostros,
                    1.1,
                    3,
                    0,
                    new Size(30, 30),
                    new Size()
            );

            // Dibujar rectángulo
            for (Rect rect : rostros.toArray()) {

                Imgproc.rectangle(
                        frame,
                        rect,
                        new Scalar(0, 255, 0),
                        2
                );
            }

            BufferedImage img = matToBufferedImage(frame);

            g.drawImage(
                    img,
                    0,
                    0,
                    getWidth(),
                    getHeight(),
                    null
            );
        }
    }

    public Mat obtenerRostro() {

        if (frame.empty() || detector.empty()) {
            return ultimoRostro;
        }

        Mat gris = new Mat();

        Imgproc.cvtColor(
                frame,
                gris,
                Imgproc.COLOR_BGR2GRAY
        );

        MatOfRect rostros = new MatOfRect();

        detector.detectMultiScale(
                gris,
                rostros,
                1.1,
                3,
                0,
                new Size(30, 30),
                new Size()
        );

        Rect[] faces = rostros.toArray();

        if (faces.length > 0) {

            Rect rect = faces[0];

            // ===== RECORTE CENTRAL MEJORADO =====

            int x = rect.x + rect.width / 8;
            int y = rect.y + rect.height / 8;

            int w = rect.width * 3 / 4;
            int h = rect.height * 3 / 4;

            // Evitar salir de límites
            if (x + w > gris.cols()) {
                w = gris.cols() - x;
            }

            if (y + h > gris.rows()) {
                h = gris.rows() - y;
            }

            Rect mejorRect = new Rect(x, y, w, h);

            // Recortar mejor rostro
            Mat rostro = new Mat(gris, mejorRect);

            // Resize uniforme
            Imgproc.resize(
                    rostro,
                    rostro,
                    new Size(200, 200)
            );

            // Mejorar contraste
            Imgproc.equalizeHist(
                    rostro,
                    rostro
            );

            // Reducir ruido
            Imgproc.GaussianBlur(
                    rostro,
                    rostro,
                    new Size(3, 3),
                    0
            );

            // Guardar último rostro válido
            ultimoRostro = rostro.clone();

            return ultimoRostro;
        }

        return ultimoRostro;
    }

    private BufferedImage matToBufferedImage(Mat mat) {

        int type = BufferedImage.TYPE_BYTE_GRAY;

        if (mat.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }

        byte[] b = new byte[(int) (mat.total() * mat.channels())];

        mat.get(0, 0, b);

        BufferedImage img =
                new BufferedImage(
                        mat.cols(),
                        mat.rows(),
                        type
                );

        img.getRaster().setDataElements(
                0,
                0,
                mat.cols(),
                mat.rows(),
                b
        );

        return img;
    }

    public void cerrarCamara() {

        if (camera != null && camera.isOpened()) {

            camera.release();

            System.out.println("Cámara liberada");
        }
    }
}