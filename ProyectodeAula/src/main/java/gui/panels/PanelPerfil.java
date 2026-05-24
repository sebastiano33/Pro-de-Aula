package gui.panels;

import config.Conexion;
import gui.menuPrincipal;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;

public class PanelPerfil extends javax.swing.JPanel {

    // ── Campos ─────────────────────────────────────────────
    private JLabel val_correo;

    private JTextField txt_nombre;
    private JTextField txt_usuario;
    private JTextField txt_codigo;

    private JPasswordField txt_pass;
    private JPasswordField txt_pass2;

    // ── Botones ────────────────────────────────────────────
    private JButton btn_editar;
    private JButton btn_guardar;
    private JButton btn_cancelar;
    private JButton btn_volver;

    // ── Valores originales ─────────────────────────────────
    private String nombreOriginal;
    private String usuarioOriginal;
    private String codigoOriginal;

    public PanelPerfil() {

        initComponentes();
        cargarDatos();
    }

    private menuPrincipal menu;

    public PanelPerfil(menuPrincipal menu) {
    this.menu = menu;
    initComponentes();
    cargarDatos();
}

    private void initComponentes() {

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        setBackground(Color.WHITE);

        setPreferredSize(new Dimension(900, 580));

        // ─────────────────────────────────────────────────────
        // BOTÓN VOLVER
        // ─────────────────────────────────────────────────────

        btn_volver = new JButton("←");

        btn_volver.setFont(new Font("Segoe UI", Font.BOLD, 22));

        btn_volver.setForeground(Color.WHITE);

        btn_volver.setBackground(new Color(0, 74, 173));

        btn_volver.setFocusPainted(false);

        btn_volver.setBorderPainted(false);

        btn_volver.setCursor(new Cursor(Cursor.HAND_CURSOR));

        add(btn_volver,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(
                        820,
                        20,
                        50,
                        40
                ));

        btn_volver.addActionListener(e -> {

            java.awt.Window ventana =
                    SwingUtilities.getWindowAncestor(this);

            if (ventana instanceof menuPrincipal menu) {

                menu.restaurarMenuPrincipal();
            }
        });

        // ─────────────────────────────────────────────────────
        // TITULOS
        // ─────────────────────────────────────────────────────

        JLabel lbl_titulo = new JLabel("Mi perfil");

        lbl_titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));

        lbl_titulo.setForeground(new Color(0, 74, 173));

        add(lbl_titulo,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(
                        30,
                        20,
                        300,
                        30
                ));

        JLabel lbl_sub = new JLabel(
                "Consulta y actualiza tu información personal."
        );

        lbl_sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        lbl_sub.setForeground(new Color(100, 100, 100));

        add(lbl_sub,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(
                        30,
                        50,
                        500,
                        20
                ));

        JPanel deco1 = new JPanel();

        deco1.setBackground(new Color(0, 74, 173));

        add(deco1,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(
                        30,
                        74,
                        120,
                        4
                ));

        // ─────────────────────────────────────────────────────
        // TARJETA
        // ─────────────────────────────────────────────────────

        JPanel tarjeta = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {

                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g;

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                g2.setColor(new Color(245, 248, 255));

                g2.fillRoundRect(
                        0,
                        0,
                        getWidth(),
                        getHeight(),
                        20,
                        20
                );

                g2.setColor(new Color(210, 225, 245));

                g2.drawRoundRect(
                        0,
                        0,
                        getWidth() - 1,
                        getHeight() - 1,
                        20,
                        20
                );
            }
        };

        tarjeta.setOpaque(false);

        tarjeta.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        add(tarjeta,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(
                        30,
                        95,
                        840,
                        440
                ));

        // ─────────────────────────────────────────────────────
        // AVATAR
        // ─────────────────────────────────────────────────────

        JPanel avatar = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {

                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g;

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                g2.setColor(new Color(0, 74, 173));

                g2.fillOval(
                        0,
                        0,
                        getWidth(),
                        getHeight()
                );

                g2.setColor(Color.WHITE);

                g2.setFont(
                        new Font("Segoe UI", Font.BOLD, 36)
                );

                FontMetrics fm = g2.getFontMetrics();

                String letra = "U";

                g2.drawString(
                        letra,
                        (getWidth() - fm.stringWidth(letra)) / 2,
                        (getHeight()
                                + fm.getAscent()
                                - fm.getDescent()) / 2
                );
            }
        };

        avatar.setOpaque(false);

        tarjeta.add(
                avatar,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(
                        360,
                        20,
                        80,
                        80
                )
        );

        JLabel lbl_perfil =
                new JLabel("Información de cuenta");

        lbl_perfil.setFont(
                new Font("Segoe UI", Font.BOLD, 15)
        );

        lbl_perfil.setForeground(
                new Color(0, 74, 173)
        );

        lbl_perfil.setHorizontalAlignment(
                JLabel.CENTER
        );

        tarjeta.add(
                lbl_perfil,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(
                        280,
                        108,
                        240,
                        20
                )
        );

        JSeparator sep = new JSeparator();

        sep.setForeground(new Color(210, 225, 245));

        tarjeta.add(
                sep,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(
                        20,
                        135,
                        800,
                        2
                )
        );

        // ─────────────────────────────────────────────────────
        // FORMULARIO
        // ─────────────────────────────────────────────────────

        int x1 = 40;
        int x2 = 450;
        int y = 155;

        // Nombre
        tarjeta.add(
                crearLabel("Nombre completo"),
                new org.netbeans.lib.awtextra.AbsoluteConstraints(
                        x1,
                        y,
                        180,
                        20
                )
        );

        txt_nombre = crearCampo();

        tarjeta.add(
                txt_nombre,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(
                        x1,
                        y + 24,
                        350,
                        35
                )
        );

        // Correo
        tarjeta.add(
                crearLabel("Correo institucional"),
                new org.netbeans.lib.awtextra.AbsoluteConstraints(
                        x2,
                        y,
                        300,
                        20
                )
        );

        val_correo = new JLabel("—");

        val_correo.setFont(
                new Font("Segoe UI", Font.PLAIN, 13)
        );

        val_correo.setForeground(
                new Color(120, 120, 120)
        );

        val_correo.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                0,
                                0,
                                1,
                                0,
                                new Color(200, 200, 200)
                        ),
                        BorderFactory.createEmptyBorder(
                                4,
                                6,
                                4,
                                6
                        )
                )
        );

        tarjeta.add(
                val_correo,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(
                        x2,
                        y + 24,
                        350,
                        35
                )
        );

        y += 75;

        // Usuario
        tarjeta.add(
                crearLabel("Usuario"),
                new org.netbeans.lib.awtextra.AbsoluteConstraints(
                        x1,
                        y,
                        180,
                        20
                )
        );

        txt_usuario = crearCampo();

        tarjeta.add(
                txt_usuario,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(
                        x1,
                        y + 24,
                        350,
                        35
                )
        );

        // Código
        tarjeta.add(
                crearLabel("Código estudiantil"),
                new org.netbeans.lib.awtextra.AbsoluteConstraints(
                        x2,
                        y,
                        200,
                        20
                )
        );

        txt_codigo = crearCampo();

        tarjeta.add(
                txt_codigo,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(
                        x2,
                        y + 24,
                        350,
                        35
                )
        );

        y += 75;

        // Password
        tarjeta.add(
        crearLabel("Nueva contraseña"),
        new org.netbeans.lib.awtextra.AbsoluteConstraints(
                x1,
                y,
                250,
                20
        )
);

txt_pass = new JPasswordField();

txt_pass.setFont(new Font("Segoe UI", Font.PLAIN, 13));
txt_pass.setEnabled(false);

txt_pass.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createMatteBorder(
                0,
                0,
                2,
                0,
                new Color(0, 74, 173)
        ),
        BorderFactory.createEmptyBorder(4, 6, 4, 6)
));

tarjeta.add(
        txt_pass,
        new org.netbeans.lib.awtextra.AbsoluteConstraints(
                x1,
                y + 24,
                350,
                35
        )
);

tarjeta.add(
        crearLabel("Confirmar nueva contraseña"),
        new org.netbeans.lib.awtextra.AbsoluteConstraints(
                x2,
                y,
                250,
                20
        )
);

txt_pass2 = new JPasswordField();

txt_pass2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
txt_pass2.setEnabled(false);

txt_pass2.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createMatteBorder(
                0,
                0,
                2,
                0,
                new Color(0, 74, 173)
        ),
        BorderFactory.createEmptyBorder(4, 6, 4, 6)
));

tarjeta.add(
        txt_pass2,
        new org.netbeans.lib.awtextra.AbsoluteConstraints(
                x2,
                y + 24,
                350,
                35
        )
);

JLabel lbl_importante = new JLabel(
        "IMPORTANTE: deja la contraseña vacía si no deseas cambiarla."
);

lbl_importante.setFont(new Font("Segoe UI", Font.BOLD, 12));
lbl_importante.setForeground(new Color(180, 50, 50));

tarjeta.add(
        lbl_importante,
        new org.netbeans.lib.awtextra.AbsoluteConstraints(
                40,
                y + 70,
                500,
                20
        )
);

        // ─────────────────────────────────────────────────────
        // BOTONES
        // ─────────────────────────────────────────────────────

        y += 90;

        btn_editar = new JButton("Editar datos");

        btn_editar.setBackground(new Color(0, 74, 173));

        btn_editar.setForeground(Color.WHITE);

        btn_editar.setFocusPainted(false);

        btn_editar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        tarjeta.add(
                btn_editar,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(
                        250,
                        y,
                        160,
                        38
                )
        );

        btn_editar.addActionListener(
                e -> activarEdicion()
        );

        btn_guardar = new JButton("Guardar cambios");

        btn_guardar.setBackground(new Color(34, 139, 34));

        btn_guardar.setForeground(Color.WHITE);

        btn_guardar.setVisible(false);

        btn_guardar.setFocusPainted(false);

        tarjeta.add(
                btn_guardar,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(
                        180,
                        y,
                        200,
                        38
                )
        );

        btn_guardar.addActionListener(
                e -> guardarCambios()
        );

        btn_cancelar = new JButton("Cancelar");

        btn_cancelar.setBackground(new Color(180, 50, 50));

        btn_cancelar.setForeground(Color.WHITE);

        btn_cancelar.setVisible(false);

        btn_cancelar.setFocusPainted(false);

        tarjeta.add(
                btn_cancelar,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(
                        400,
                        y,
                        160,
                        38
                )
        );

        btn_cancelar.addActionListener(
                e -> cancelarEdicion()
        );
    }

    // ─────────────────────────────────────────────────────
    // HELPERS
    // ─────────────────────────────────────────────────────

    private JLabel crearLabel(String texto) {

        JLabel lbl = new JLabel(texto);

        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));

        lbl.setForeground(new Color(0, 74, 173));

        return lbl;
    }

    private JTextField crearCampo() {

        JTextField tf = new JTextField();

        tf.setEnabled(false);

        tf.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                0,
                                0,
                                2,
                                0,
                                new Color(0, 74, 173)
                        ),
                        BorderFactory.createEmptyBorder(
                                4,
                                6,
                                4,
                                6
                        )
                )
        );

        return tf;
    }

    // ─────────────────────────────────────────────────────
    // CARGAR DATOS
    // ─────────────────────────────────────────────────────

    private void cargarDatos() {

        int idUsuario =
                util.SesionUsuario.getIdUsuario();

        try {

            Connection con =
                    Conexion.conectar();

            String sql =
                    "SELECT nombre_completo, correo, usuario, codigo_estudiantil FROM usuarios WHERE id=?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, idUsuario);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                nombreOriginal =
                        rs.getString("nombre_completo");

                usuarioOriginal =
                        rs.getString("usuario");

                codigoOriginal =
                        rs.getString("codigo_estudiantil");

                txt_nombre.setText(nombreOriginal);

                val_correo.setText(
                        rs.getString("correo")
                );

                txt_usuario.setText(usuarioOriginal);

                txt_codigo.setText(codigoOriginal);
            }

            con.close();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar datos"
            );
        }
    }

    // ─────────────────────────────────────────────────────
    // EDICIÓN
    // ─────────────────────────────────────────────────────

    private void activarEdicion() {

        txt_nombre.setEnabled(true);

        txt_usuario.setEnabled(true);

        txt_codigo.setEnabled(true);

        txt_pass.setEnabled(true);

        txt_pass2.setEnabled(true);

        btn_editar.setVisible(false);

        btn_guardar.setVisible(true);

        btn_cancelar.setVisible(true);
    }

    private void cancelarEdicion() {

        txt_nombre.setText(nombreOriginal);

        txt_usuario.setText(usuarioOriginal);

        txt_codigo.setText(codigoOriginal);

        txt_pass.setText("");

        txt_pass2.setText("");

        desactivarEdicion();
    }

    private void desactivarEdicion() {

        txt_nombre.setEnabled(false);

        txt_usuario.setEnabled(false);

        txt_codigo.setEnabled(false);

        txt_pass.setEnabled(false);

        txt_pass2.setEnabled(false);

        btn_editar.setVisible(true);

        btn_guardar.setVisible(false);

        btn_cancelar.setVisible(false);
    }

    // ─────────────────────────────────────────────────────
    // GUARDAR
    // ─────────────────────────────────────────────────────

    private void guardarCambios() {

        String nombre =
                txt_nombre.getText().trim();

        String usuario =
                txt_usuario.getText().trim();

        String codigo =
                txt_codigo.getText().trim();

        String pass = new String(txt_pass.getPassword()).trim();

        if (pass.equals("Dejar en blanco para mantener igual")) {
        pass = "";
        }

        String pass2 =
                new String(txt_pass2.getPassword()).trim();

        if (nombre.isEmpty()
                || usuario.isEmpty()
                || codigo.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Completa todos los campos"
            );

            return;
        }

        if (!pass.isEmpty()) {

            if (!pass.equals(pass2)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Las contraseñas no coinciden"
                );

                return;
            }

            if (pass.length() < 6) {

                JOptionPane.showMessageDialog(
                        this,
                        "La contraseña debe tener mínimo 6 caracteres"
                );

                return;
            }
        }

        int idUsuario =
                util.SesionUsuario.getIdUsuario();

        try {

            Connection con =
                    Conexion.conectar();

            PreparedStatement ps;

            if (!pass.isEmpty()) {

                String sql =
                        "UPDATE usuarios SET nombre_completo=?, usuario=?, codigo_estudiantil=?, contraseña=? WHERE id=?";

                ps = con.prepareStatement(sql);

                ps.setString(1, nombre);

                ps.setString(2, usuario);

                ps.setString(3, codigo);

                ps.setString(4, pass);

                ps.setInt(5, idUsuario);

            } else {

                String sql =
                        "UPDATE usuarios SET nombre_completo=?, usuario=?, codigo_estudiantil=? WHERE id=?";

                ps = con.prepareStatement(sql);

                ps.setString(1, nombre);

                ps.setString(2, usuario);

                ps.setString(3, codigo);

                ps.setInt(4, idUsuario);
            }

            int filas = ps.executeUpdate();

            con.close();

            if (filas > 0) {

                nombreOriginal = nombre;

                usuarioOriginal = usuario;

                codigoOriginal = codigo;

                txt_pass.setText("");

                txt_pass2.setText("");

                JOptionPane.showMessageDialog(
                        this,
                        "Datos actualizados correctamente"
                );

                desactivarEdicion();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo actualizar"
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al guardar: " + e.getMessage()
            );
        }
    }
}