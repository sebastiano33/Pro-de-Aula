package gui.admin;

import config.Conexion;
import dao.VotoBD;
import gui.menuPrincipal;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;

public class PanelEstadoVotacion extends JPanel {

    private menuPrincipal menu;
    private int idUsuario;
    private VotoBD votoBD = new VotoBD();

    public PanelEstadoVotacion(menuPrincipal menu, int idUsuario) {
        this.menu = menu;
        this.idUsuario = idUsuario;
        initComponentes();
        cargarElecciones();
    }

    private void initComponentes() {
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(1190, 570));

        // ── Título ──────────────────────────────────────────
        JLabel lbl_titulo = new JLabel("Estado de votación");
        lbl_titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lbl_titulo.setForeground(new Color(0, 74, 173));
        add(lbl_titulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 400, 30));

        JLabel lbl_sub = new JLabel("Consulta el estado de tu participación en cada elección.");
        lbl_sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl_sub.setForeground(new Color(100, 100, 100));
        add(lbl_sub, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, 500, 20));

        JPanel deco = new JPanel();
        deco.setBackground(new Color(0, 74, 173));
        add(deco, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 74, 120, 4));

        // ── Botón volver ────────────────────────────────────
        JButton btn_volver = new JButton("←");
        btn_volver.setFont(new Font("Segoe UI", Font.BOLD, 22));
        btn_volver.setForeground(Color.WHITE);
        btn_volver.setBackground(new Color(0, 74, 173));
        btn_volver.setFocusPainted(false);
        btn_volver.setBorderPainted(false);
        btn_volver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn_volver.addActionListener(e -> {
            Window ventana = SwingUtilities.getWindowAncestor(this);
            if (ventana instanceof menuPrincipal m) {
                m.restaurarMenuPrincipal();
            }
        });
        add(btn_volver, new org.netbeans.lib.awtextra.AbsoluteConstraints(1110, 20, 50, 40));
    }

    private void cargarElecciones() {
        // Panel contenedor con scroll
        JPanel contenedor = new JPanel();
        contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.Y_AXIS));
        contenedor.setBackground(Color.WHITE);
        contenedor.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        try {
            Connection con = Conexion.conectar();
            String sql = "SELECT id_eleccion, nombre, fecha_inicio, fecha_fin, estado_activa, descripcion FROM elecciones";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            boolean hayElecciones = false;

            while (rs.next()) {
                hayElecciones = true;
                int idEleccion   = rs.getInt("id_eleccion");
                String nombre    = rs.getString("nombre");
                String fechaIni  = rs.getString("fecha_inicio").substring(0, 10);
                String fechaFin  = rs.getString("fecha_fin").substring(0, 10);
                int activa       = rs.getInt("estado_activa");
                String desc      = rs.getString("descripcion");

                boolean yaVoto   = votoBD.yaVoto(idUsuario, idEleccion);

                JPanel tarjeta   = crearTarjeta(nombre, fechaIni, fechaFin, activa, desc, yaVoto);
                contenedor.add(tarjeta);
                contenedor.add(Box.createVerticalStrut(15));
            }

            con.close();

            if (!hayElecciones) {
                JLabel lbl = new JLabel("No hay elecciones registradas.");
                lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                lbl.setForeground(new Color(120, 120, 120));
                lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
                contenedor.add(Box.createVerticalStrut(80));
                contenedor.add(lbl);
            }

        } catch (Exception e) {
            JLabel lbl = new JLabel("Error al cargar elecciones: " + e.getMessage());
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            lbl.setForeground(new Color(180, 50, 50));
            contenedor.add(lbl);
        }

        JScrollPane scroll = new JScrollPane(contenedor);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBackground(Color.WHITE);

        add(scroll, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 95, 1130, 450));
    }

    private JPanel crearTarjeta(String nombre, String fechaIni, String fechaFin,
                                 int activa, String desc, boolean yaVoto) {

        JPanel tarjeta = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(245, 248, 255));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(new Color(210, 225, 245));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
            }
        };
        tarjeta.setOpaque(false);
        tarjeta.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        tarjeta.setPreferredSize(new Dimension(1100, 130));
        tarjeta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

        // Círculo de estado votación (✅ o ❌)
        JPanel circulo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color color = yaVoto ? new Color(220, 255, 220) : new Color(255, 220, 220);
                g2.setColor(color);
                g2.fillOval(0, 0, getWidth(), getHeight());
            }
        };
        circulo.setOpaque(false);
        circulo.setPreferredSize(new Dimension(60, 60));
        tarjeta.add(circulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 35, 60, 60));

        JLabel lbl_voto_icono = new JLabel(yaVoto ? "✅" : "❌", SwingConstants.CENTER);
        lbl_voto_icono.setFont(new Font("Segoe UI", Font.PLAIN, 26));
        tarjeta.add(lbl_voto_icono, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 35, 60, 60));

        // Nombre elección
        JLabel lbl_nombre = new JLabel(nombre);
        lbl_nombre.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lbl_nombre.setForeground(new Color(0, 74, 173));
        tarjeta.add(lbl_nombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 18, 500, 25));

        // Descripción
        JLabel lbl_desc = new JLabel(desc != null ? desc : "Sin descripción");
        lbl_desc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl_desc.setForeground(new Color(100, 100, 100));
        tarjeta.add(lbl_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 45, 550, 20));

        // Fechas
        JLabel lbl_fechas = new JLabel("📅  " + fechaIni + "  →  " + fechaFin);
        lbl_fechas.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl_fechas.setForeground(new Color(120, 120, 120));
        tarjeta.add(lbl_fechas, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 70, 400, 20));

        // Separador vertical
        JSeparator sep = new JSeparator(SwingConstants.VERTICAL);
        sep.setForeground(new Color(210, 225, 245));
        tarjeta.add(sep, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 15, 2, 100));

        // Badge estado elección (ACTIVA / CERRADA)
        String estadoTxt = activa == 1 ? "ACTIVA" : "CERRADA";
        Color estadoColor = activa == 1 ? new Color(34, 139, 34) : new Color(180, 50, 50);
        Color estadoBg = activa == 1 ? new Color(220, 255, 220) : new Color(255, 220, 220);

        JLabel lbl_estado = new JLabel(estadoTxt, SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(estadoBg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                super.paintComponent(g);
            }
        };
        lbl_estado.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl_estado.setForeground(estadoColor);
        lbl_estado.setOpaque(false);
        tarjeta.add(lbl_estado, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 20, 90, 28));

        // Estado de participación del usuario
        String participTxt = yaVoto ? "Ya votaste" : "No has votado";
        Color participColor = yaVoto ? new Color(34, 139, 34) : new Color(180, 50, 50);

        JLabel lbl_particip = new JLabel(participTxt, SwingConstants.CENTER);
        lbl_particip.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl_particip.setForeground(participColor);
        tarjeta.add(lbl_particip, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 65, 160, 25));

        return tarjeta;
    }
}