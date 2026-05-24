package gui.panels;

import org.opencv.core.*;
import org.opencv.videoio.VideoCapture;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Panel de cámara con dos modos de obtención de rostro:
 *  - obtenerRostro()      → gris preprocesado (para login)
 *  - obtenerRostroColor() → BGR crudo recortado (para registro)
 */
public class PanelReconocimiento extends JPanel {

    private VideoCapture camera;
    private CascadeClassifier detector;
    private Mat frame = new Mat();

    // Último rostro válido (gris, para login)
    private Mat ultimoRostroGris  = null;
    // Último rostro válido (color, para registro)
    private Mat ultimoRostroColor = null;

    public PanelReconocimiento() {

        util.OpenCVLoader.loadLibrary();

        camera = new VideoCapture(0);

        String ruta = System.getProperty("user.dir")
                + "/haarcascade_frontalface_default.xml";

        detector = new CascadeClassifier(ruta);

        if (detector.empty()) {
            JOptionPane.showMessageDialog(this,
                    "Error: no se pudo cargar el clasificador Haar.\n"
                    + "Ruta: " + ruta);
        }

        // Repintar a ~30 fps
        new Timer(33, e -> repaint()).start();
    }

    // ════════════════════════════════════════════════════════════
    //  PINTADO CON DETECCIÓN
    // ════════════════════════════════════════════════════════════

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        if (!camera.isOpened() || detector.empty()) return;

        camera.read(frame);
        if (frame.empty()) return;

        // Detectar en escala de grises
        Mat gris = new Mat();
        Imgproc.cvtColor(frame, gris, Imgproc.COLOR_BGR2GRAY);

        MatOfRect rostros = new MatOfRect();
        detector.detectMultiScale(gris, rostros, 1.1, 5,
                0, new Size(80, 80), new Size());

        Rect[] faces = rostros.toArray();

        for (Rect r : faces) {
            Imgproc.rectangle(frame, r, new Scalar(0, 200, 0), 2);
        }

        // Guardar mejor rostro detectado en este frame
        if (faces.length > 0) {
            Rect mejor = rostroMasCentrado(faces, frame.cols(), frame.rows());

            // ── Versión GRIS (para login) ────────────────────────
            Mat subGris = new Mat(gris, mejor);
            Mat rGris   = new Mat();
            Imgproc.resize(subGris, rGris, new Size(128, 128));
            ultimoRostroGris = rGris.clone();

            // ── Versión COLOR (para registro) ────────────────────
            Mat subColor = new Mat(frame, mejor);
            Mat rColor   = new Mat();
            Imgproc.resize(subColor, rColor, new Size(128, 128));
            ultimoRostroColor = rColor.clone();
        }

        // Dibujar en pantalla
        BufferedImage img = matToBufferedImage(frame);
        g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
    }

    // ════════════════════════════════════════════════════════════
    //  API PÚBLICA
    // ════════════════════════════════════════════════════════════

    /**
     * Para LOGIN: devuelve rostro en gris normalizado.
     * El ComparadorRostros hace su propio preprocesamiento.
     */
    public Mat obtenerRostro() {
        return ultimoRostroGris;
    }

    /**
     * Para REGISTRO: devuelve rostro en BGR crudo.
     * No aplica equalizeHist ni blur — se preserva toda la info.
     */
    public Mat obtenerRostroColor() {
        return ultimoRostroColor;
    }

    public void cerrarCamara() {
        if (camera != null && camera.isOpened()) {
            camera.release();
        }
    }

    // ════════════════════════════════════════════════════════════
    //  UTILIDADES PRIVADAS
    // ════════════════════════════════════════════════════════════

    /**
     * Elige el rostro más cercano al centro del frame
     * (descarta detecciones falsas en los bordes).
     */
    private Rect rostroMasCentrado(Rect[] faces, int w, int h) {
        int cx = w / 2, cy = h / 2;
        Rect mejor = faces[0];
        double mejorDist = Double.MAX_VALUE;
        for (Rect r : faces) {
            int rx = r.x + r.width  / 2;
            int ry = r.y + r.height / 2;
            double d = Math.hypot(rx - cx, ry - cy);
            if (d < mejorDist) { mejorDist = d; mejor = r; }
        }
        return mejor;
    }

    private BufferedImage matToBufferedImage(Mat mat) {
        int type = mat.channels() > 1
                ? BufferedImage.TYPE_3BYTE_BGR
                : BufferedImage.TYPE_BYTE_GRAY;
        byte[] b = new byte[(int)(mat.total() * mat.channels())];
        mat.get(0, 0, b);
        BufferedImage img = new BufferedImage(mat.cols(), mat.rows(), type);
        img.getRaster().setDataElements(0, 0, mat.cols(), mat.rows(), b);
        return img;
    }
}