package gui.components;

import java.awt.*;
import javax.swing.*;

public class RoundedPanel extends JPanel {

    private int cornerRadius = 25;

    public RoundedPanel() {
        setOpaque(false);
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        // SOMBRA
        g2.setColor(new Color(0, 0, 0, 25));
        g2.fillRoundRect(
                4,
                4,
                getWidth() - 8,
                getHeight() - 8,
                cornerRadius,
                cornerRadius
        );

        // FONDO
        g2.setColor(getBackground());
        g2.fillRoundRect(
                0,
                0,
                getWidth() - 8,
                getHeight() - 8,
                cornerRadius,
                cornerRadius
        );

        // BORDE SUAVE
        g2.setColor(new Color(230, 230, 230));
        g2.drawRoundRect(
                0,
                0,
                getWidth() - 9,
                getHeight() - 9,
                cornerRadius,
                cornerRadius
        );

        g2.dispose();

        super.paintComponent(g);
    }
}