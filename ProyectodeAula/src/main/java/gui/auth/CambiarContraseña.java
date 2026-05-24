package gui.auth;

import dao.UsuarioBD;

import javax.swing.*;

public class CambiarContraseña extends JFrame {

    private int idUsuario;

    public CambiarContraseña(int idUsuario) {

        this.idUsuario = idUsuario;

        setTitle("Cambiar contraseña");

        setSize(400, 250);

        setLocationRelativeTo(null);

        setLayout(null);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel txt1 = new JLabel("Nueva contraseña:");
        txt1.setBounds(40, 40, 150, 30);
        add(txt1);

        JPasswordField nueva = new JPasswordField();
        nueva.setBounds(40, 70, 300, 30);
        add(nueva);

        JLabel txt2 = new JLabel("Confirmar contraseña:");
        txt2.setBounds(40, 110, 170, 30);
        add(txt2);

        JPasswordField confirmar = new JPasswordField();
        confirmar.setBounds(40, 140, 300, 30);
        add(confirmar);

        JButton cambiar = new JButton("Cambiar");
        cambiar.setBounds(120, 180, 140, 30);
        add(cambiar);

        cambiar.addActionListener(e -> {

            String pass1 = new String(nueva.getPassword());

            String pass2 = new String(confirmar.getPassword());

            if (pass1.isEmpty() || pass2.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Completa todos los campos"
                );

                return;
            }

            if (!pass1.equals(pass2)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Las contraseñas no coinciden"
                );

                return;
            }

            UsuarioBD dao = new UsuarioBD();

            boolean actualizado =
                    dao.actualizarContraseña(idUsuario, pass1);

            if (actualizado) {

                JOptionPane.showMessageDialog(
                        this,
                        "Contraseña actualizada correctamente"
                );

                dispose();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Error al actualizar contraseña"
                );
            }
        });
    }
}