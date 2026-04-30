package util;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

public class ComparadorRostros {

    public static double comparar(Mat img1, Mat img2) {

        Mat gris1 = new Mat();
        Mat gris2 = new Mat();

        Imgproc.cvtColor(img1, gris1, Imgproc.COLOR_BGR2GRAY);
        Imgproc.cvtColor(img2, gris2, Imgproc.COLOR_BGR2GRAY);

        Imgproc.resize(gris1, gris1, new Size(200, 200));
        Imgproc.resize(gris2, gris2, new Size(200, 200));

        Mat diff = new Mat();
        Core.absdiff(gris1, gris2, diff);

        Scalar suma = Core.sumElems(diff);

        return suma.val[0];
    }
}