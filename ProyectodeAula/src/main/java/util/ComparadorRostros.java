package util;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

public class ComparadorRostros {

    public static double comparar(Mat img1, Mat img2) {

        if (img1.empty() || img2.empty()) {
            return Double.MAX_VALUE;
        }

        Mat gris1 = new Mat();
        Mat gris2 = new Mat();

        // Convertir a gris si es necesario
        if (img1.channels() == 3) {
            Imgproc.cvtColor(img1, gris1, Imgproc.COLOR_BGR2GRAY);
        } else {
            gris1 = img1.clone();
        }

        if (img2.channels() == 3) {
            Imgproc.cvtColor(img2, gris2, Imgproc.COLOR_BGR2GRAY);
        } else {
            gris2 = img2.clone();
        }

        // Redimensionar
        Imgproc.resize(gris1, gris1, new Size(200, 200));
        Imgproc.resize(gris2, gris2, new Size(200, 200));

        // Mejorar iluminación
        Imgproc.equalizeHist(gris1, gris1);
        Imgproc.equalizeHist(gris2, gris2);

        // Reducir ruido
        Imgproc.GaussianBlur(
                gris1,
                gris1,
                new Size(3, 3),
                0
        );

        Imgproc.GaussianBlur(
                gris2,
                gris2,
                new Size(3, 3),
                0
        );

        // Diferencia absoluta
        Mat diff = new Mat();

        Core.absdiff(gris1, gris2, diff);

        Scalar suma = Core.sumElems(diff);

        // Distancia promedio
        return suma.val[0] / (200.0 * 200.0);
    }

    // Método opcional para validar coincidencia
    public static boolean esMismaPersona(double distancia) {

        double UMBRAL = 35;

        return distancia < UMBRAL;
    }
}