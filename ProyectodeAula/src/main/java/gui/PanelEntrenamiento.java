package gui;

import javax.swing.*;

public class PanelEntrenamiento extends JPanel {

    public PanelEntrenamiento() {
        JButton btnEntrenar = new JButton("Entrenar modelo");
        add(btnEntrenar);

        btnEntrenar.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Entrenamiento (aún por implementar completo)");
        });
    }
}
