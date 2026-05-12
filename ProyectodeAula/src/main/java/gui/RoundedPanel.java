/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

import java.awt.*;
import javax.swing.*;

/**
 *
 * @author DISTRIEMPAQUES
 */
public class RoundedPanel extends JPanel {
      private int cornerRadius = 25;

    public RoundedPanel() {
        setOpaque(false);
    }

    
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );

        g2.setColor(getBackground());

        g2.fillRoundRect(
            0,
            0,
            getWidth(),
            getHeight(),
            cornerRadius,
            cornerRadius
        );

        g2.dispose();

        super.paintComponent(g);
    }
    
}
