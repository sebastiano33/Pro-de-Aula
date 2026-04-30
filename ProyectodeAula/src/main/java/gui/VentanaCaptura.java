package gui;

import org.opencv.core.Mat;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VentanaCaptura extends JFrame {

    private List<Mat> fotosCapturadas = new ArrayList<>();
    private int contador = 0;

    private PanelReconocimiento panelCamara;
    private String gmail;
    private registro registroPadre;

    public VentanaCaptura(String gmail, registro registroPadre) {

        util.OpenCVLoader.loadLibrary();

        this.gmail = gmail;
        this.registroPadre = registroPadre;

        setTitle("Captura facial - " + gmail);
        setSize(900, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        panelCamara = new PanelReconocimiento();
        add(panelCamara, BorderLayout.CENTER);

        JPanel panelDerecha = new JPanel();
        panelDerecha.setPreferredSize(new Dimension(250, 0));
        panelDerecha.setLayout(new GridLayout(7, 1));

        panelDerecha.add(new JLabel("INSTRUCCIONES", SwingConstants.CENTER));
        panelDerecha.add(new JLabel("• Mira al frente"));
        panelDerecha.add(new JLabel("• Buena iluminación"));
        panelDerecha.add(new JLabel("• Sin gorra/lentes"));
        panelDerecha.add(new JLabel("• Solo una persona"));
        panelDerecha.add(new JLabel("• Cara centrada"));
        panelDerecha.add(new JLabel("• No moverse"));

        add(panelDerecha, BorderLayout.EAST);

        JPanel panelBotones = new JPanel();

        JButton btnCapturar = new JButton("Capturar (12)");
        JButton btnFinalizar = new JButton("Finalizar");

        panelBotones.add(btnCapturar);
        panelBotones.add(btnFinalizar);

        add(panelBotones, BorderLayout.SOUTH);

        btnCapturar.addActionListener(e -> capturarAutomatico());

        btnFinalizar.addActionListener(e -> {
            if (fotosCapturadas.size() < 12) {
                JOptionPane.showMessageDialog(this, "Debes capturar las 12 fotos");
                return;
            }

            registroPadre.setFotosCapturadas(fotosCapturadas);

            JOptionPane.showMessageDialog(this, "Fotos listas ✅");
            dispose();
        });
    }

    private void capturarAutomatico() {

        new Thread(() -> {

            while (contador < 12) {

                Mat rostro = panelCamara.obtenerRostro();

                if (rostro != null) {
                    fotosCapturadas.add(rostro.clone());
                    contador++;
                    System.out.println("Foto " + contador);
                }

                try {
                    Thread.sleep(500);
                } catch (Exception e) {}
            }

            SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(this, "12 fotos capturadas ✅")
            );

        }).start();
    }
}