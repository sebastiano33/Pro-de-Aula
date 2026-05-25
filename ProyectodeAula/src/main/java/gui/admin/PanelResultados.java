package gui.admin;

import config.Conexion;
import dao.VotoBD;
import java.awt.*;
import java.sql.*;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.*;

/**
 * Panel que muestra los resultados de votos por candidato.
 * Lee los votos desde votos.txt mediante VotoBD.
 * Muestra barras de porcentaje visuales en la tabla.
 */
public class PanelResultados extends javax.swing.JPanel {

    private JTable tabla;
    private DefaultTableModel modelo;
    private JComboBox<String> combo_elecciones;
    private JButton btn_actualizar;
    private JLabel lbl_total;

    // Guardamos el total de votos para usarlo en el renderer
    private int totalVotosActual = 0;

    public PanelResultados() {
         initComponentes();
    cargarElecciones();

    JButton btn_volver = new JButton("←");

    btn_volver.setFont(new Font("Segoe UI", Font.BOLD, 22));
    btn_volver.setForeground(Color.WHITE);
    btn_volver.setBackground(new Color(0, 74, 173));

    btn_volver.setFocusPainted(false);
    btn_volver.setBorderPainted(false);
    btn_volver.setCursor(new Cursor(Cursor.HAND_CURSOR));

    btn_volver.addActionListener(e -> {

        this.setVisible(false);

        Window ventana = SwingUtilities.getWindowAncestor(this);

        if (ventana instanceof menuAdmin m) {
            m.restaurarMenuAdm();
        }
    });

    add(btn_volver,
        new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 15, 50, 40));

    revalidate();
    repaint();
    }

    private void initComponentes() {
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(900, 580));
        setMinimumSize(new Dimension(900, 580));
        setMaximumSize(new Dimension(900, 580));
        setBorder(BorderFactory.createLineBorder(new Color(153, 153, 153)));

        // ── Título ───────────────────────────────────────────────
        JLabel lbl_titulo = new JLabel("Resultados de votación.");
        lbl_titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lbl_titulo.setForeground(new Color(0, 74, 173));
        add(lbl_titulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 14, 400, 30));

        JLabel lbl_sub = new JLabel("Consulta los votos registrados por candidato en cada elección.");
        lbl_sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl_sub.setForeground(new Color(100, 100, 100));
        add(lbl_sub, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 44, 600, 20));

        // ── Separador decorativo ─────────────────────────────────
        JPanel deco1 = new JPanel();
        deco1.setBackground(new Color(0, 74, 173));
        add(deco1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 68, 120, 4));

        JPanel deco2 = new JPanel();
        deco2.setBackground(new Color(0, 74, 173));
        deco2.setOpaque(false);
        add(deco2, new org.netbeans.lib.awtextra.AbsoluteConstraints(144, 72, 80, 4));

        // ── Selector de elección ─────────────────────────────────
        JLabel lbl_elegir = new JLabel("Seleccionar elección:");
        lbl_elegir.setFont(new Font("Segoe UI", Font.BOLD, 13));
        add(lbl_elegir, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 160, 25));

        combo_elecciones = new JComboBox<>();
        combo_elecciones.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combo_elecciones.setBackground(Color.WHITE);
        add(combo_elecciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(185, 90, 480, 28));

        btn_actualizar = new JButton("Consultar");
        btn_actualizar.setBackground(new Color(0, 74, 173));
        btn_actualizar.setForeground(Color.WHITE);
        btn_actualizar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn_actualizar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn_actualizar.setFocusPainted(false);
        btn_actualizar.setBorderPainted(false);
        btn_actualizar.addActionListener(e -> cargarResultados());
        add(btn_actualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 88, 120, 30));

        // ── Tabla ────────────────────────────────────────────────
        // Columnas: #, ID, Nombre, Carrera, Votos, % del total (barra visual)
        String[] columnas = {"#", "ID", "Nombre candidato", "Carrera", "Votos", "% del total"};
        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
            @Override
            public Class<?> getColumnClass(int col) {
                // La columna de votos es Integer para que el renderer la reciba bien
                if (col == 4) return Integer.class;
                if (col == 5) return Double.class;
                return String.class;
            }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(38);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.setGridColor(new Color(230, 230, 230));
        tabla.setShowVerticalLines(false);
        tabla.setSelectionBackground(new Color(232, 240, 254));
        tabla.setSelectionForeground(Color.BLACK);
        tabla.setIntercellSpacing(new Dimension(0, 1));

        // Encabezado
        JTableHeader header = tabla.getTableHeader();
        header.setBackground(new Color(0, 74, 173));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);
        header.setPreferredSize(new Dimension(0, 36));

        // Anchos de columnas
        tabla.getColumnModel().getColumn(0).setPreferredWidth(30);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(50);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(200);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(160);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(55);
        tabla.getColumnModel().getColumn(5).setPreferredWidth(200); // Barra ancha

        // ── Renderer de filas alternas (base) ────────────────────
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, col);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 248, 255));
                }
                ((JLabel) c).setHorizontalAlignment(
                        (col == 0 || col == 1 || col == 4) ? JLabel.CENTER : JLabel.LEFT);
                return c;
            }
        });

        // ── Renderer especial: columna 5 = barra de porcentaje ───
        tabla.getColumnModel().getColumn(5).setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {

                double pct = (value instanceof Double) ? (Double) value : 0.0;

                // Panel que dibuja la barra + texto
                JPanel panel = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);

                        int w = getWidth();
                        int h = getHeight();
                        int margen = 6;
                        int barH = 14;
                        int barY = (h - barH) / 2;
                        int barW = w - margen * 2 - 50; // 50px reservado para el texto

                        // Fondo de la barra (gris claro)
                        g2.setColor(new Color(220, 228, 240));
                        g2.fillRoundRect(margen, barY, barW, barH, 8, 8);

                        // Relleno proporcional al porcentaje
                        int fill = (int) (barW * pct / 100.0);
                        if (fill > 0) {
                            // Gradiente azul
                            GradientPaint gp = new GradientPaint(
                                    margen, barY, new Color(0, 74, 173),
                                    margen + fill, barY, new Color(30, 120, 220));
                            g2.setPaint(gp);
                            g2.fillRoundRect(margen, barY, fill, barH, 8, 8);
                        }

                        // Texto del porcentaje a la derecha de la barra
                        g2.setColor(new Color(40, 40, 40));
                        g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                        String texto = String.format("%.1f%%", pct);
                        FontMetrics fm = g2.getFontMetrics();
                        int tx = margen + barW + 6;
                        int ty = (h + fm.getAscent() - fm.getDescent()) / 2;
                        g2.drawString(texto, tx, ty);

                        g2.dispose();
                    }
                };

                panel.setBackground(isSelected
                        ? new Color(232, 240, 254)
                        : (row % 2 == 0 ? Color.WHITE : new Color(245, 248, 255)));
                panel.setOpaque(true);
                return panel;
            }
        });

        // ── Renderer columna Votos (negrita azul) ─────────────────
        tabla.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                        t, value, isSelected, hasFocus, row, col);
                lbl.setHorizontalAlignment(JLabel.CENTER);
                int v = (value instanceof Integer) ? (Integer) value : 0;
                lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
                lbl.setForeground(v > 0 ? new Color(0, 74, 173) : Color.GRAY);
                if (!isSelected) {
                    lbl.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 248, 255));
                }
                return lbl;
            }
        });

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(210, 220, 235), 1));
        scroll.getViewport().setBackground(Color.WHITE);
        add(scroll, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 135, 860, 360));

        // ── Panel footer con totales ──────────────────────────────
        JPanel panel_resumen = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(240, 247, 255));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(new Color(220, 230, 240));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
            }
        };
        panel_resumen.setOpaque(false);
        panel_resumen.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));

        lbl_total = new JLabel("Total de votos registrados: —");
        lbl_total.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl_total.setForeground(new Color(0, 74, 173));
        panel_resumen.add(lbl_total);

        add(panel_resumen, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 510, 860, 55));
    }

    // ── Carga los nombres de elecciones desde la BD ──────────────
    private void cargarElecciones() {
        try {
            Connection con = Conexion.conectar();
            String sql = "SELECT id_eleccion, nombre FROM elecciones ORDER BY id_eleccion";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            combo_elecciones.addItem("-- Seleccione una elección --");
            while (rs.next()) {
                int id = rs.getInt("id_eleccion");
                String nombre = rs.getString("nombre");
                combo_elecciones.addItem(id + " | " + nombre);
            }
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar elecciones: " + e.getMessage());
        }
    }

    // ── Consulta candidatos (BD) y votos (TXT) ───────────────────
    private void cargarResultados() {
        int seleccionado = combo_elecciones.getSelectedIndex();
        if (seleccionado == 0) {
            JOptionPane.showMessageDialog(this, "Por favor seleccione una elección.");
            return;
        }

        String item = (String) combo_elecciones.getSelectedItem();
        int idEleccion;
        try {
            idEleccion = Integer.parseInt(item.split("\\|")[0].trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error al leer la elección seleccionada.");
            return;
        }

        modelo.setRowCount(0);

        try {
            // 1. Obtener candidatos desde la BD
            Connection con = Conexion.conectar();
            String sql = "SELECT id_candidato, nombre, carrera FROM candidatos " +
                         "WHERE id_eleccion = ? ORDER BY id_candidato";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idEleccion);
            ResultSet rs = ps.executeQuery();

            java.util.List<Object[]> candidatos = new java.util.ArrayList<>();
            while (rs.next()) {
                candidatos.add(new Object[]{
                    rs.getInt("id_candidato"),
                    rs.getString("nombre"),
                    rs.getString("carrera")
                });
            }
            con.close();

            // 2. Obtener votos desde el archivo TXT
            VotoBD votoBD = new VotoBD();
            Map<Integer, Integer> votosMap = votoBD.obtenerResultados(idEleccion);

            // 3. Combinar: agregar votos a cada candidato
            java.util.List<Object[]> filas = new java.util.ArrayList<>();
            int totalVotos = 0;
            for (Object[] c : candidatos) {
                int idCandidato = (int) c[0];
                int votos = votosMap.getOrDefault(idCandidato, 0);
                totalVotos += votos;
                filas.add(new Object[]{idCandidato, c[1], c[2], votos});
            }

            // 4. Ordenar por votos descendente
            filas.sort((a, b) -> Integer.compare((int) b[3], (int) a[3]));

            totalVotosActual = totalVotos;

            // 5. Llenar la tabla con porcentaje como Double (para el renderer)
            int fila = 1;
            for (Object[] f : filas) {
                int votos = (int) f[3];
                double pct = totalVotos > 0 ? (votos * 100.0 / totalVotos) : 0.0;
                modelo.addRow(new Object[]{
                    fila++,
                    f[0],          // id_candidato
                    f[1],          // nombre
                    f[2],          // carrera
                    votos,         // Integer → renderer negrita azul
                    pct            // Double  → renderer barra de progreso
                });
            }

            lbl_total.setText("Total de votos: " + totalVotos
                    + "   |   Candidatos: " + filas.size()
                    + "   |   Fuente: votos.txt");

            if (filas.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No hay candidatos registrados para esta elección.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al consultar resultados: " + e.getMessage());
        }
    }
}