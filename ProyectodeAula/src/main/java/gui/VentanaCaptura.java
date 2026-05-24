package gui;

import gui.panels.PanelReconocimiento;
import gui.auth.registro;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgcodecs.Imgcodecs;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Captura 20 fotos con variación forzada de pose/iluminación.
 * Guarda las imágenes CRUDAS en color (sin preprocesar) para
 * que el comparador tenga máxima información disponible.
 */
public class VentanaCaptura extends JFrame {

    // ── Config ───────────────────────────────────────────────────
    private static final int TOTAL_FOTOS    = 20;   // más muestras = mejor discriminación
    private static final int MS_ENTRE_FOTOS = 800;  // forzar variación natural

    // ── Estado ───────────────────────────────────────────────────
    private List<Mat> fotosCapturadas = new ArrayList<>();
    private int contador = 0;
    private boolean capturando = false;

    // ── UI ───────────────────────────────────────────────────────
    private PanelReconocimiento panelCamara;
    private JLabel lbl_instruccion;
    private JLabel lbl_contador;
    private JProgressBar progressBar;
    private JButton btnCapturar;
    private JButton btnFinalizar;

    // ── Referencia al padre ──────────────────────────────────────
    private String gmail;
    private registro registroPadre;

    // ── Instrucciones dinámicas por fase ─────────────────────────
    private static final String[] INSTRUCCIONES = {
        "📷 Mira directo a la cámara  (1–5)",
        "↙ Gira levemente a la IZQUIERDA  (6–8)",
        "↗ Gira levemente a la DERECHA  (9–11)",
        "⬆ Levanta un poco el mentón  (12–14)",
        "⬇ Baja un poco el mentón  (15–17)",
        "💡 Vuelve al frente — posición normal  (18–20)"
    };

    public VentanaCaptura(String gmail, registro registroPadre) {

        util.OpenCVLoader.loadLibrary();

        this.gmail          = gmail;
        this.registroPadre  = registroPadre;

        setTitle("Registro facial — " + gmail);
        setSize(960, 560);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // ── Panel cámara ────────────────────────────────────────
        panelCamara = new PanelReconocimiento();
        add(panelCamara, BorderLayout.CENTER);

        // ── Panel derecho ────────────────────────────────────────
        JPanel panelDerecha = new JPanel();
        panelDerecha.setPreferredSize(new Dimension(260, 0));
        panelDerecha.setLayout(new GridLayout(10, 1, 4, 4));
        panelDerecha.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        panelDerecha.add(bold("INSTRUCCIONES GENERALES"));
        panelDerecha.add(new JLabel("• Buena iluminación frontal"));
        panelDerecha.add(new JLabel("• Sin gorra ni lentes"));
        panelDerecha.add(new JLabel("• Solo tú en cámara"));
        panelDerecha.add(new JLabel("• Cara visible y despejada"));
        panelDerecha.add(new JSeparator());

        lbl_instruccion = new JLabel(INSTRUCCIONES[0]);
        lbl_instruccion.setForeground(new Color(0, 74, 173));
        lbl_instruccion.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panelDerecha.add(lbl_instruccion);

        lbl_contador = new JLabel("Fotos: 0 / " + TOTAL_FOTOS);
        lbl_contador.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panelDerecha.add(lbl_contador);

        progressBar = new JProgressBar(0, TOTAL_FOTOS);
        progressBar.setStringPainted(true);
        panelDerecha.add(progressBar);

        add(panelDerecha, BorderLayout.EAST);

        // ── Panel botones ────────────────────────────────────────
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 8));

        btnCapturar = new JButton("▶ Iniciar captura (" + TOTAL_FOTOS + " fotos)");
        btnCapturar.setBackground(new Color(0, 74, 173));
        btnCapturar.setForeground(Color.WHITE);
        btnCapturar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCapturar.setFocusPainted(false);

        btnFinalizar = new JButton("✔ Confirmar y cerrar");
        btnFinalizar.setEnabled(false);
        btnFinalizar.setFont(new Font("Segoe UI", Font.BOLD, 13));

        panelBotones.add(btnCapturar);
        panelBotones.add(btnFinalizar);
        add(panelBotones, BorderLayout.SOUTH);

        // ── Listeners ────────────────────────────────────────────
        btnCapturar.addActionListener(e -> iniciarCaptura());

        btnFinalizar.addActionListener(e -> {
            if (fotosCapturadas.size() < TOTAL_FOTOS) {
                JOptionPane.showMessageDialog(this,
                        "Debes completar las " + TOTAL_FOTOS + " fotos.");
                return;
            }
            registroPadre.setFotosCapturadas(fotosCapturadas);
            JOptionPane.showMessageDialog(this,
                    "✅ " + fotosCapturadas.size() + " fotos listas para guardar.");
            panelCamara.cerrarCamara();
            dispose();
        });

        setVisible(true);
    }

    // ════════════════════════════════════════════════════════════
    //  CAPTURA CON VARIACIÓN FORZADA
    // ════════════════════════════════════════════════════════════

    private void iniciarCaptura() {

        if (capturando) return;
        capturando   = true;
        contador     = 0;
        fotosCapturadas.clear();

        btnCapturar.setEnabled(false);

        new Thread(() -> {

            while (contador < TOTAL_FOTOS) {

                // Instrucción por fase
                int fase = contador < 5  ? 0 :
                           contador < 8  ? 1 :
                           contador < 11 ? 2 :
                           contador < 14 ? 3 :
                           contador < 17 ? 4 : 5;

                final int f = fase;
                SwingUtilities.invokeLater(() ->
                        lbl_instruccion.setText(INSTRUCCIONES[f]));

                // Obtener frame crudo (COLOR, sin preprocesar)
                Mat rostroColor = panelCamara.obtenerRostroColor();

                if (rostroColor != null && !rostroColor.empty()) {

                    fotosCapturadas.add(rostroColor.clone());
                    contador++;

                    final int c = contador;
                    SwingUtilities.invokeLater(() -> {
                        lbl_contador.setText("Fotos: " + c + " / " + TOTAL_FOTOS);
                        progressBar.setValue(c);
                    });

                    System.out.println("Foto " + contador + " capturada ✓");
                } else {
                    System.out.println("Sin rostro detectado, reintentando...");
                }

                try { Thread.sleep(MS_ENTRE_FOTOS); }
                catch (InterruptedException ignored) {}
            }

            SwingUtilities.invokeLater(() -> {
                btnFinalizar.setEnabled(true);
                lbl_instruccion.setText("✅ Captura completa. Pulsa Confirmar.");
                JOptionPane.showMessageDialog(this,
                        "✅ " + TOTAL_FOTOS + " fotos capturadas correctamente.");
            });

            capturando = false;

        }).start();
    }

    // ── Helper ───────────────────────────────────────────────────
    private JLabel bold(String text) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return l;
    }
}