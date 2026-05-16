package util;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

/**
 * Comparador de rostros por SUBREGIONES INDEPENDIENTES.
 *
 * Divide la cara en 5 zonas y cada una vota por separado:
 *   1. Frente + cejas  (filas  0–35)
 *   2. Ojos            (filas 28–58)  ← zona más discriminativa
 *   3. Nariz           (filas 52–82)
 *   4. Boca            (filas 76–106)
 *   5. Cara global     (imagen completa, peso reducido)
 *
 * Acceso concedido si ≥ 4 de 5 regiones pasan su umbral individual.
 * Score: 0.0 = idéntico, 1.0 = completamente distinto.
 */
public class ComparadorRostros {

    // ── Umbral por región ────────────────────────────────────────
    private static final double UMBRAL_FRENTE = 0.32;
    private static final double UMBRAL_OJOS   = 0.28;
    private static final double UMBRAL_NARIZ  = 0.28;
    private static final double UMBRAL_BOCA   = 0.55;
    private static final double UMBRAL_GLOBAL = 0.35;

    // ── Regiones mínimas para acceso ────────────────────────────
    private static final int REGIONES_MINIMAS = 3;   // de 5

    private static final double PORCENTAJE_VOTOS = 0.55;

    private static final int TAM = 128;

    // ── Coordenadas Y de cada subregión (imagen 128×128) ─────────
    private static final int FRENTE_Y1 =  0;
    private static final int FRENTE_Y2 = 35;
    private static final int OJOS_Y1   = 28;
    private static final int OJOS_Y2   = 58;
    private static final int NARIZ_Y1  = 52;
    private static final int NARIZ_Y2  = 82;
    private static final int BOCA_Y1   = 76;
    private static final int BOCA_Y2   = 106;

    // Votos de región del último comparar()
    private static int ultimosVotosRegion = 0;

    // ════════════════════════════════════════════════════════════
    //  API PRINCIPAL
    // ════════════════════════════════════════════════════════════

    public static double comparar(Mat img1, Mat img2) {
        if (img1 == null || img2 == null || img1.empty() || img2.empty())
            return Double.MAX_VALUE;

        Mat p1 = preprocesar(img1);
        Mat p2 = preprocesar(img2);

        double sFrente = scoreRegion(p1, p2, FRENTE_Y1, FRENTE_Y2);
        double sOjos   = scoreRegion(p1, p2, OJOS_Y1,   OJOS_Y2);
        double sNariz  = scoreRegion(p1, p2, NARIZ_Y1,  NARIZ_Y2);
        double sBoca   = scoreRegion(p1, p2, BOCA_Y1,   BOCA_Y2);
        double sGlobal = scoreGlobal(p1, p2);

        int votos = 0;
        if (sFrente < UMBRAL_FRENTE) votos++;
        if (sOjos   < UMBRAL_OJOS)   votos++;
        if (sNariz  < UMBRAL_NARIZ)  votos++;
        if (sBoca   < UMBRAL_BOCA)   votos++;
        if (sGlobal < UMBRAL_GLOBAL) votos++;

        ultimosVotosRegion = votos;

        double score = sFrente * 0.15
                     + sOjos   * 0.30
                     + sNariz  * 0.25
                     + sBoca   * 0.05
                     + sGlobal * 0.15;

        System.out.printf(
            "    Frente=%.4f(%s) Ojos=%.4f(%s) Nariz=%.4f(%s)" +
            " Boca=%.4f(%s) Global=%.4f(%s) → SCORE=%.4f Regiones=%d/5%n",
            sFrente, sFrente < UMBRAL_FRENTE ? "✓" : "✗",
            sOjos,   sOjos   < UMBRAL_OJOS   ? "✓" : "✗",
            sNariz,  sNariz  < UMBRAL_NARIZ  ? "✓" : "✗",
            sBoca,   sBoca   < UMBRAL_BOCA   ? "✓" : "✗",
            sGlobal, sGlobal < UMBRAL_GLOBAL ? "✓" : "✗",
            score, votos);

        return score;
    }

    /** Votos de región de la última llamada a comparar(). */
    public static int getUltimosVotosRegion() {
        return ultimosVotosRegion;
    }

    /**
     * true si la última comparación superó el mínimo de regiones.
     * Usado internamente — en VentanaLoginFace se usa getUltimosVotosRegion()
     * directamente para mayor control.
     */
    public static boolean esMismaPersona(double score) {
        return ultimosVotosRegion >= REGIONES_MINIMAS;
    }

    public static int getRegionesMinimasRequeridas() {
        return REGIONES_MINIMAS;
    }

    public static double getPorcentajeVotos() {
        return PORCENTAJE_VOTOS;
    }

    // ════════════════════════════════════════════════════════════
    //  SCORE POR SUBREGIÓN
    // ════════════════════════════════════════════════════════════

    private static double scoreRegion(Mat p1, Mat p2, int y1, int y2) {
        Rect rect = new Rect(0, y1, TAM, y2 - y1);
        return lbpRegion(new Mat(p1, rect), new Mat(p2, rect));
    }

    private static double scoreGlobal(Mat p1, Mat p2) {
        double lbp  = lbpMultiRegion(p1, p2);
        double ssim = 1.0 - ssim(p1, p2);
        double hog  = hogDistancia(p1, p2);
        return lbp * 0.50 + ssim * 0.30 + hog * 0.20;
    }

    // ════════════════════════════════════════════════════════════
    //  PREPROCESAMIENTO
    // ════════════════════════════════════════════════════════════

    private static Mat preprocesar(Mat src) {
    Mat gris = new Mat();
    if (src.channels() >= 3)
        Imgproc.cvtColor(src, gris,
                src.channels() == 4 ? Imgproc.COLOR_BGRA2GRAY
                                    : Imgproc.COLOR_BGR2GRAY);
    else
        gris = src.clone();

    Imgproc.resize(gris, gris, new Size(TAM, TAM));

    // Máscara elíptica — elimina bordes con posible fondo
    Mat mascara = Mat.zeros(TAM, TAM, CvType.CV_8UC1);
    Imgproc.ellipse(mascara,
            new Point(TAM / 2, TAM / 2),
            new Size(52, 62),   // cubre cara pero recorta bordes
            0, 0, 360,
            new Scalar(255), -1);
    gris.copyTo(gris, mascara);

    gris = retinexDoG(gris);
    gris = corregirGamma(gris, 1.5);

    org.opencv.imgproc.CLAHE clahe = Imgproc.createCLAHE(4.0, new Size(4, 4));
    clahe.apply(gris, gris);

    Mat bilateral = new Mat();
    Imgproc.bilateralFilter(gris, bilateral, 9, 75, 75);
    return bilateral;
}

    private static Mat retinexDoG(Mat gris) {
        Mat float32 = new Mat();
        gris.convertTo(float32, CvType.CV_32F);
        Mat blur1 = new Mat(), blur2 = new Mat();
        Imgproc.GaussianBlur(float32, blur1, new Size(0, 0), 1.0);
        Imgproc.GaussianBlur(float32, blur2, new Size(0, 0), 2.0);
        Mat dog = new Mat();
        Core.subtract(blur1, blur2, dog);
        Core.normalize(dog, dog, 0, 255, Core.NORM_MINMAX);
        Mat resultado = new Mat();
        dog.convertTo(resultado, CvType.CV_8U);
        return resultado;
    }

    private static Mat corregirGamma(Mat src, double gamma) {
        Mat float32 = new Mat();
        src.convertTo(float32, CvType.CV_32F, 1.0 / 255.0);
        Core.pow(float32, 1.0 / gamma, float32);
        Mat resultado = new Mat();
        float32.convertTo(resultado, CvType.CV_8U, 255.0);
        return resultado;
    }

    // ════════════════════════════════════════════════════════════
    //  LBP SOBRE SUBREGIÓN (grilla 2×4)
    // ════════════════════════════════════════════════════════════

    private static double lbpRegion(Mat img1, Mat img2) {
        int gridRows = 2, gridCols = 4;
        int altZona   = img1.rows() / gridRows;
        int anchoZona = img1.cols() / gridCols;

        double[][] pesos = {
            {0.5, 1.5, 1.5, 0.5},
            {0.5, 1.5, 1.5, 0.5}
        };

        Mat lbp1 = calcularLBP(img1);
        Mat lbp2 = calcularLBP(img2);

        double distTotal = 0, pesoTotal = 0;

        for (int row = 0; row < gridRows; row++) {
            for (int col = 0; col < gridCols; col++) {
                int x  = col * anchoZona;
                int y  = row * altZona;
                int x2 = Math.min(x + anchoZona, lbp1.cols());
                int y2 = Math.min(y + altZona,   lbp1.rows());
                if (x2 <= x || y2 <= y) continue;
                Rect zona = new Rect(x, y, x2 - x, y2 - y);

                Mat h1 = histLBP(new Mat(lbp1, zona));
                Mat h2 = histLBP(new Mat(lbp2, zona));

                double chi      = Imgproc.compareHist(h1, h2, Imgproc.HISTCMP_CHISQR);
                double distNorm = 1.0 - Math.exp(-chi / 50.0);

                double peso = pesos[row][col];
                distTotal  += distNorm * peso;
                pesoTotal  += peso;
            }
        }

        return pesoTotal > 0 ? distTotal / pesoTotal : 1.0;
    }

    // ════════════════════════════════════════════════════════════
    //  LBP MULTIRREGIÓN 4×4 (para score global)
    // ════════════════════════════════════════════════════════════

    private static double lbpMultiRegion(Mat img1, Mat img2) {
        int grid    = 4;
        int tamZona = TAM / grid;

        double[][] pesosGrid = {
            {0.5, 1.0, 1.0, 0.5},
            {1.0, 2.0, 2.0, 1.0},
            {1.0, 2.0, 2.0, 1.0},
            {0.5, 1.0, 1.0, 0.5}
        };

        Mat lbp1 = calcularLBP(img1);
        Mat lbp2 = calcularLBP(img2);

        double distTotal = 0, pesoTotal = 0;

        for (int row = 0; row < grid; row++) {
            for (int col = 0; col < grid; col++) {
                int x  = col * tamZona;
                int y  = row * tamZona;
                int x2 = Math.min(x + tamZona, lbp1.cols());
                int y2 = Math.min(y + tamZona, lbp1.rows());
                if (x2 <= x || y2 <= y) continue;
                Rect zona = new Rect(x, y, x2 - x, y2 - y);

                Mat h1 = histLBP(new Mat(lbp1, zona));
                Mat h2 = histLBP(new Mat(lbp2, zona));

                double chi      = Imgproc.compareHist(h1, h2, Imgproc.HISTCMP_CHISQR);
                double distNorm = 1.0 - Math.exp(-chi / 50.0);

                double peso = pesosGrid[row][col];
                distTotal  += distNorm * peso;
                pesoTotal  += peso;
            }
        }

        return pesoTotal > 0 ? distTotal / pesoTotal : 1.0;
    }

    private static Mat calcularLBP(Mat gris) {
        int rows = gris.rows(), cols = gris.cols();
        byte[] src = new byte[(int) gris.total()];
        gris.get(0, 0, src);
        byte[] dst = new byte[(rows - 2) * (cols - 2)];
        int[] off  = { -cols-1, -cols, -cols+1, 1, cols+1, cols, cols-1, -1 };

        for (int r = 1; r < rows - 1; r++) {
            for (int c = 1; c < cols - 1; c++) {
                int idx    = r * cols + c;
                int centro = src[idx] & 0xFF;
                int codigo = 0;
                for (int k = 0; k < 8; k++)
                    if ((src[idx + off[k]] & 0xFF) >= centro) codigo |= (1 << k);
                dst[(r-1)*(cols-2)+(c-1)] = (byte) codigo;
            }
        }

        Mat result = Mat.zeros(rows - 2, cols - 2, CvType.CV_8UC1);
        result.put(0, 0, dst);
        return result;
    }

    private static Mat histLBP(Mat lbp) {
        Mat hist = new Mat();
        Imgproc.calcHist(java.util.Arrays.asList(lbp),
                new MatOfInt(0), new Mat(), hist,
                new MatOfInt(256), new MatOfFloat(0, 256));
        Core.normalize(hist, hist, 0, 1, Core.NORM_MINMAX);
        return hist;
    }

    // ════════════════════════════════════════════════════════════
    //  SSIM
    // ════════════════════════════════════════════════════════════

    private static double ssim(Mat img1, Mat img2) {
        final double C1 = 6.5025, C2 = 58.5225;
        Mat i1 = new Mat(), i2 = new Mat();
        img1.convertTo(i1, CvType.CV_32F);
        img2.convertTo(i2, CvType.CV_32F);

        Mat i1_2 = new Mat(); Core.multiply(i1, i1, i1_2);
        Mat i2_2 = new Mat(); Core.multiply(i2, i2, i2_2);
        Mat i1i2 = new Mat(); Core.multiply(i1, i2, i1i2);

        Size k = new Size(11, 11);
        Mat mu1 = new Mat(); Imgproc.GaussianBlur(i1,   mu1, k, 1.5);
        Mat mu2 = new Mat(); Imgproc.GaussianBlur(i2,   mu2, k, 1.5);
        Mat mu1_2 = new Mat(); Core.multiply(mu1, mu1, mu1_2);
        Mat mu2_2 = new Mat(); Core.multiply(mu2, mu2, mu2_2);
        Mat mu1mu2= new Mat(); Core.multiply(mu1, mu2, mu1mu2);

        Mat s1  = new Mat(); Imgproc.GaussianBlur(i1_2, s1,  k, 1.5);
        Core.subtract(s1, mu1_2, s1);
        Mat s2  = new Mat(); Imgproc.GaussianBlur(i2_2, s2,  k, 1.5);
        Core.subtract(s2, mu2_2, s2);
        Mat s12 = new Mat(); Imgproc.GaussianBlur(i1i2, s12, k, 1.5);
        Core.subtract(s12, mu1mu2, s12);

        Mat t1 = new Mat(); Core.multiply(mu1mu2, new Scalar(2), t1);
        Core.add(t1, new Scalar(C1), t1);
        Mat t2 = new Mat(); Core.multiply(s12, new Scalar(2), t2);
        Core.add(t2, new Scalar(C2), t2);
        Mat t3 = new Mat(); Core.multiply(t1, t2, t3);

        Core.add(mu1_2, mu2_2, t1); Core.add(t1, new Scalar(C1), t1);
        Core.add(s1,    s2,    t2); Core.add(t2, new Scalar(C2), t2);
        Core.multiply(t1, t2, t1);

        Mat ssimMap = new Mat(); Core.divide(t3, t1, ssimMap);
        return Core.mean(ssimMap).val[0];
    }

    // ════════════════════════════════════════════════════════════
    //  HOG
    // ════════════════════════════════════════════════════════════

    private static double hogDistancia(Mat img1, Mat img2) {
        float[] d1 = descriptorHOG(img1);
        float[] d2 = descriptorHOG(img2);
        double dot = 0, n1 = 0, n2 = 0;
        for (int i = 0; i < d1.length; i++) {
            dot += d1[i] * d2[i];
            n1  += d1[i] * d1[i];
            n2  += d2[i] * d2[i];
        }
        if (n1 == 0 || n2 == 0) return 1.0;
        return 1.0 - Math.max(0.0, dot / (Math.sqrt(n1) * Math.sqrt(n2)));
    }

    private static float[] descriptorHOG(Mat gris) {
        int tamCelda = 16, numBins = 9;
        int celdasX  = TAM / tamCelda, celdasY = TAM / tamCelda;

        Mat gris32 = new Mat(); gris.convertTo(gris32, CvType.CV_32F);
        Mat gradX  = new Mat(); Imgproc.Sobel(gris32, gradX, CvType.CV_32F, 1, 0, 3);
        Mat gradY  = new Mat(); Imgproc.Sobel(gris32, gradY, CvType.CV_32F, 0, 1, 3);

        float[] dx = new float[(int) gradX.total()];
        float[] dy = new float[(int) gradY.total()];
        gradX.get(0, 0, dx);
        gradY.get(0, 0, dy);

        float[] desc = new float[celdasY * celdasX * numBins];
        int idx = 0;

        for (int cy = 0; cy < celdasY; cy++) {
            for (int cx = 0; cx < celdasX; cx++) {
                float[] bins = new float[numBins];
                for (int r = cy*tamCelda; r < (cy+1)*tamCelda; r++) {
                    for (int c = cx*tamCelda; c < (cx+1)*tamCelda; c++) {
                        float vx  = dx[r*TAM+c], vy = dy[r*TAM+c];
                        float mag = (float) Math.sqrt(vx*vx + vy*vy);
                        float ang = (float) Math.toDegrees(
                                Math.atan2(Math.abs(vy), Math.abs(vx)));
                        bins[Math.min(numBins-1, (int)(ang/20f))] += mag;
                    }
                }
                float sum = 0;
                for (float b : bins) sum += b;
                if (sum > 0) for (int k = 0; k < numBins; k++) bins[k] /= sum;
                for (float b : bins) desc[idx++] = b;
            }
        }
        return desc;
    }
}