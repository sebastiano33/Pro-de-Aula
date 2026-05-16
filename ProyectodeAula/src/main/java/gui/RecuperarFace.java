package gui;

import util.ComparadorRostros;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Login facial con doble verificación:
 *
 *  1. VERIFICACIÓN PROPIA  — la cara debe parecerse suficientemente
 *     al dataset del correo ingresado (votos por regiones ≥ mínimo).
 *
 *  2. VERIFICACIÓN CRUZADA — la cara NO debe parecerse más a otro
 *     usuario registrado que al dueño del correo. Si algún usuario
 *     ajeno obtiene mejor score que el titular, se bloquea el acceso.
 *
 * Esto evita que alguien ingrese el correo de otro y cuele su cara.
 */
public class RecuperarFace extends JFrame {

    private String nombreParaMenu;
    private int    idUsuario;
    private boolean procesando   = false;
    private int intentosFallidos = 0;

    // ── Parámetros de seguridad ──────────────────────────────────
    // Margen: el score del titular debe ser al menos MARGEN_SEGURIDAD
    // menor que el mejor score de cualquier usuario ajeno.
    // Ejemplo: si titular=0.18 y ajeno=0.21 → diferencia=0.03 < 0.06 → BLOQUEADO
    private static final double MARGEN_SEGURIDAD = 0.02;

    // Mínimo de votos por región que debe obtener el titular
    private static final int VOTOS_REGION_MINIMOS = 4; // de 5

    // Porcentaje mínimo de fotos del dataset propias que deben votar a favor
    private static final double PORCENTAJE_VOTOS_PROPIOS = 0.45;

    public RecuperarFace(int idUsuario, String correo, String nombre) {

        this.idUsuario      = idUsuario;
        this.nombreParaMenu = nombre;

        util.OpenCVLoader.loadLibrary();

        setTitle("Face ID — " + nombre);
        setSize(420, 340);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        PanelReconocimiento panel = new PanelReconocimiento();
        add(panel);
        setVisible(true);

        final Timer[] timer = new Timer[1];

        timer[0] = new Timer(2500, e -> {

            if (procesando) return;
            procesando = true;

            String correoLimpio = correo.trim().toLowerCase();

            // ── 1. Verificar que el dataset del titular existe ───
            File carpetaTitular = new File(
                    System.getProperty("user.dir") + "/dataset/" + correoLimpio);

            System.out.println("\n══ Intento " + (intentosFallidos + 1) + " ══");
            System.out.println("Titular: " + correoLimpio);

            if (!carpetaTitular.exists() || !carpetaTitular.isDirectory()) {
                timer[0].stop();
                JOptionPane.showMessageDialog(this,
                        "No hay datos faciales registrados para este usuario.");
                panel.cerrarCamara();
                dispose();
                new login().setVisible(true);
                return;
            }

            File[] archivosTitular = imagenesDe(carpetaTitular);
            if (archivosTitular == null || archivosTitular.length == 0) {
                procesando = false;
                return;
            }

            // ── 2. Obtener rostro de la cámara ───────────────────
            Mat rostroActual = panel.obtenerRostro();
            if (rostroActual == null || rostroActual.empty()) {
                System.out.println("Sin rostro detectado, esperando...");
                procesando = false;
                return;
            }

            // ── 3. Comparar contra dataset del TITULAR ───────────
            System.out.println("\n── Comparando contra titular ──");
            double mejorScoreTitular = Double.MAX_VALUE;
            int    votosRegionTitular = 0;
            int    votosFotosTitular  = 0;

            for (File f : archivosTitular) {
                Mat img = Imgcodecs.imread(f.getAbsolutePath(), Imgcodecs.IMREAD_COLOR);
                if (img.empty()) continue;

                double score = ComparadorRostros.comparar(rostroActual, img);
                int    votosRegion = ComparadorRostros.getUltimosVotosRegion();

                System.out.printf("  %s → %.4f (regiones=%d/5)%n",
                        f.getName(), score, votosRegion);

                if (score < mejorScoreTitular) {
                    mejorScoreTitular  = score;
                    votosRegionTitular = votosRegion;
                }
                if (votosRegion >= VOTOS_REGION_MINIMOS) votosFotosTitular++;
            }

            int minFotosTitular = (int) Math.ceil(
                    archivosTitular.length * PORCENTAJE_VOTOS_PROPIOS);

            System.out.printf("%nTITULAR → mejorScore=%.4f | fotosFavor=%d/%d (mín %d)" +
                              " | mejorRegiones=%d/5%n",
                    mejorScoreTitular, votosFotosTitular,
                    archivosTitular.length, minFotosTitular, votosRegionTitular);

            // Criterio propio: debe pasar regiones Y votos de fotos
            boolean pasaPropio = (votosRegionTitular >= VOTOS_REGION_MINIMOS)
                              && (votosFotosTitular  >= minFotosTitular);

            if (!pasaPropio) {
                System.out.println("✗ BLOQUEADO — no supera verificación propia");
                registrarFallo(timer[0], panel, mejorScoreTitular);
                procesando = false;
                return;
            }

            // ── 4. Verificación CRUZADA contra otros usuarios ────
            System.out.println("\n── Verificación cruzada ──");
            File dirDataset = new File(System.getProperty("user.dir") + "/dataset");
            File[] carpetasAjenas = dirDataset.listFiles(f ->
                    f.isDirectory() && !f.getName().equalsIgnoreCase(correoLimpio));

            double mejorScoreAjeno  = Double.MAX_VALUE;
            String usuarioMasCercano = "ninguno";

            if (carpetasAjenas != null) {
                for (File carpetaAjena : carpetasAjenas) {
                    File[] archivosAjenos = imagenesDe(carpetaAjena);
                    if (archivosAjenos == null || archivosAjenos.length == 0) continue;

                    // Tomar muestra representativa: máximo 15 fotos por usuario ajeno
                    // para no hacer el login demasiado lento con 10 usuarios
                    File[] muestra = muestraAleatoria(archivosAjenos, 15);

                    double mejorScoreEsteAjeno = Double.MAX_VALUE;
                    int    votosEsteAjeno = 0;

                    for (File f : muestra) {
                        Mat img = Imgcodecs.imread(
                                f.getAbsolutePath(), Imgcodecs.IMREAD_COLOR);
                        if (img.empty()) continue;

                        double score = ComparadorRostros.comparar(rostroActual, img);
                        int    votos = ComparadorRostros.getUltimosVotosRegion();

                        if (score < mejorScoreEsteAjeno) {
                            mejorScoreEsteAjeno = score;
                        }
                        if (votos >= VOTOS_REGION_MINIMOS) votosEsteAjeno++;
                    }

                    System.out.printf("  Usuario ajeno [%s] → mejorScore=%.4f" +
                                      " fotosFavor=%d/%d%n",
                            carpetaAjena.getName(), mejorScoreEsteAjeno,
                            votosEsteAjeno, muestra.length);

                    if (mejorScoreEsteAjeno < mejorScoreAjeno) {
                        mejorScoreAjeno    = mejorScoreEsteAjeno;
                        usuarioMasCercano  = carpetaAjena.getName();
                    }
                }
            }

            // ── 5. Decisión final ────────────────────────────────
            // El titular debe tener un score MARGEN_SEGURIDAD más bajo
            // que cualquier usuario ajeno. Si no, alguien más se le parece
            // demasiado y bloqueamos para evitar suplantación.
            boolean accesoFinal;

            if (mejorScoreAjeno == Double.MAX_VALUE) {
                // No hay otros usuarios registrados — solo verificación propia
                accesoFinal = pasaPropio;
            } else {
                double diferencia = mejorScoreAjeno - mejorScoreTitular;
                accesoFinal = pasaPropio && (diferencia >= MARGEN_SEGURIDAD);

                System.out.printf("%nCruce más cercano: [%s] score=%.4f%n",
                        usuarioMasCercano, mejorScoreAjeno);
                System.out.printf("Diferencia titular vs ajeno: %.4f (mín %.2f)%n",
                        diferencia, MARGEN_SEGURIDAD);

                if (!accesoFinal && pasaPropio) {
                    System.out.println("✗ BLOQUEADO — diferencia insuficiente" +
                                       " con usuario ajeno");
                }
            }

            System.out.printf("%nRESULTADO FINAL → ACCESO=%s%n",
                    accesoFinal ? "✓ SÍ" : "✗ NO");

            if (accesoFinal) {

            timer[0].stop();

            panel.cerrarCamara();

            dispose();

            JOptionPane.showMessageDialog(
                this,
                "Rostro verificado correctamente"
        );

            CambiarContraseña ventana =
                    new CambiarContraseña(idUsuario);

            ventana.setVisible(true);

        } else {

            registrarFallo(
                timer[0],
                panel,
                mejorScoreTitular
        );
}

            procesando = false;
        });

        timer[0].start();
    }

    // ════════════════════════════════════════════════════════════
    //  UTILIDADES PRIVADAS
    // ════════════════════════════════════════════════════════════

    /** Registra un intento fallido y cierra tras 5. */
    private void registrarFallo(Timer timer, PanelReconocimiento panel,
                                double mejorScore) {
        intentosFallidos++;
        System.out.println("Intento fallido " + intentosFallidos + "/5");

        if (intentosFallidos >= 5) {
            timer.stop();
            JOptionPane.showMessageDialog(this,
                    "Rostro no reconocido tras 5 intentos.\n" +
                    "Mejor score obtenido: " +
                    String.format("%.4f", mejorScore));
            panel.cerrarCamara();
            dispose();
            new login().setVisible(true);
        }
    }

    /** Lista solo imágenes válidas de una carpeta. */
    private File[] imagenesDe(File carpeta) {
        return carpeta.listFiles(f -> {
            String n = f.getName().toLowerCase();
            return n.endsWith(".jpg") || n.endsWith(".png") || n.endsWith(".jpeg");
        });
    }

    /**
     * Devuelve hasta 'max' archivos distribuidos uniformemente.
     * Evita leer las 120 fotos de cada usuario ajeno en cada intento.
     */
    private File[] muestraAleatoria(File[] archivos, int max) {
        if (archivos.length <= max) return archivos;

        List<File> muestra = new ArrayList<>();
        double paso = (double) archivos.length / max;
        for (int i = 0; i < max; i++) {
            muestra.add(archivos[(int)(i * paso)]);
        }
        return muestra.toArray(new File[0]);
    }
}