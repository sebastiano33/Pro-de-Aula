import java.awt.BorderLayout;
import java.io.File;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.Group; // CORREGIDO
import javafx.scene.media.Media; // CORREGIDO
import javafx.scene.media.MediaPlayer; // FALTA ESTA
import javafx.scene.media.MediaView; // FALTA ESTA
import javax.swing.JOptionPane;

public class registro extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(registro.class.getName());

    private final JFXPanel jfxpanel = new JFXPanel();
    public registro() {
        initComponents();
        
        panel_formulario.setBackground(new java.awt.Color(0, 0, 0, 0));
        panel_formulario.setOpaque(false);


        jLayeredPane1.setOpaque(false);
        jLayeredPane1.setBackground(new java.awt.Color(0, 0, 0, 0));
        panel_formulario.setOpaque(false);
       
        jLayeredPane1.add(panel_fondo, Integer.valueOf(-1));
        jLayeredPane1.add(panel_formulario, Integer.valueOf(0));

        panel_fondo.setBounds(0, 0, jLayeredPane1.getWidth(), jLayeredPane1.getHeight());
        panel_formulario.setBounds(0, 0, jLayeredPane1.getWidth(), jLayeredPane1.getHeight());
       
        jLayeredPane1.setLayer(panel_fondo, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(panel_formulario, javax.swing.JLayeredPane.PALETTE_LAYER);
        panel_fondo.setPreferredSize(new java.awt.Dimension(1280, 720));
        panel_fondo.setMinimumSize(new java.awt.Dimension(1280, 720));
        
        createScene();
        setTitle("Video Fondo");
        
        setResizable(false);
        setLocationRelativeTo(null);
        
        panel_fondo.setLayout(new BorderLayout());
        panel_fondo.add(jfxpanel, BorderLayout.CENTER);
        this.setLocationRelativeTo(null);
        
    }
    
  private void createScene() {
    Platform.runLater(new Runnable() {
        @Override
        public void run() {
            try {
                // He cambiado las barras a / para evitar errores de escape
                File file = new File("C:/Users/DISTRIEMPAQUES/Downloads/fondo video.mp4");
                
                if (!file.exists()) {
                    System.out.println("OJO: El archivo no existe en esa ruta.");
                }

                Media media = new Media(file.toURI().toString());
                MediaPlayer oracleVid = new MediaPlayer(media);
                MediaView mediaView = new MediaView(oracleVid);

                // IMPORTANTE: Ajusta el video al tamaño del panel
                mediaView.setFitWidth(panel_fondo.getWidth());
                mediaView.setFitHeight(panel_fondo.getHeight());
                mediaView.setPreserveRatio(false); // Para que llene todo el fondo

                Group root = new Group(mediaView);
                Scene scene = new Scene(root);

                jfxpanel.setScene(scene); // Asegúrate que se llame jfxpanel (minúscula)
                
                oracleVid.setCycleCount(MediaPlayer.INDEFINITE);
                oracleVid.setVolume(0.7);
                oracleVid.play();
                
            } catch (Exception e) {
                e.printStackTrace(); // Esto te dirá en la consola exactamente qué falló
            }
        }
    });
}
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLayeredPane1 = new javax.swing.JLayeredPane();
        panel_fondo = new javax.swing.JPanel();
        panel_formulario = new javax.swing.JPanel();
        formulario_p = new PanelRedondeado();
        txt_info = new javax.swing.JLabel();
        txt_info1 = new javax.swing.JLabel();
        txt_info2 = new javax.swing.JLabel();
        caja_nombre = new javax.swing.JTextField();
        txt_info3 = new javax.swing.JLabel();
        caja_correo = new javax.swing.JPasswordField();
        txt_info4 = new javax.swing.JLabel();
        caja_usuario = new javax.swing.JTextField();
        txt_info5 = new javax.swing.JLabel();
        caja_codigo = new javax.swing.JTextField();
        txt_info6 = new javax.swing.JLabel();
        caja_contraseña = new javax.swing.JPasswordField();
        check_terminos = new javax.swing.JCheckBox();
        label_terminos = new javax.swing.JLabel();
        bt_registro = new javax.swing.JButton();
        bt_faceId = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1280, Short.MAX_VALUE)
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 721, Short.MAX_VALUE)
        );

        panel_formulario.setOpaque(false);
        panel_formulario.setLayout(new java.awt.GridBagLayout());

        formulario_p.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_info.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        txt_info.setForeground(new java.awt.Color(13, 71, 161));
        txt_info.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txt_info.setText("CREAR CUENTA");
        formulario_p.add(txt_info, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 30, 234, 28));

        txt_info1.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        txt_info1.setForeground(new java.awt.Color(102, 102, 102));
        txt_info1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txt_info1.setText("Regístrate para acceder al sistema de votaciones.");
        formulario_p.add(txt_info1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 60, -1, -1));

        txt_info2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txt_info2.setForeground(new java.awt.Color(0, 82, 234));
        txt_info2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txt_info2.setText("Nombre completo");
        formulario_p.add(txt_info2, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 94, 113, 24));

        caja_nombre.setForeground(new java.awt.Color(102, 102, 102));
        caja_nombre.setText("Escriba aquí su nombre.");
        caja_nombre.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 82, 234)));
        formulario_p.add(caja_nombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 124, 349, 33));

        txt_info3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txt_info3.setForeground(new java.awt.Color(0, 82, 234));
        txt_info3.setText("Correo institucional");
        formulario_p.add(txt_info3, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 175, 117, -1));

        caja_correo.setForeground(new java.awt.Color(120, 120, 120));
        caja_correo.setText("jPasswordField1");
        caja_correo.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 82, 234)));
        formulario_p.add(caja_correo, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 197, 349, 37));

        txt_info4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txt_info4.setForeground(new java.awt.Color(0, 82, 234));
        txt_info4.setText("Usuario");
        formulario_p.add(txt_info4, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 252, 72, -1));

        caja_usuario.setForeground(new java.awt.Color(120, 120, 120));
        caja_usuario.setText("Ingrese su usuario");
        caja_usuario.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 82, 234)));
        formulario_p.add(caja_usuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 274, 349, 33));

        txt_info5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txt_info5.setForeground(new java.awt.Color(0, 82, 234));
        txt_info5.setText("Código estudiantil");
        formulario_p.add(txt_info5, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 325, -1, -1));

        caja_codigo.setForeground(new java.awt.Color(120, 120, 120));
        caja_codigo.setText("2025######");
        caja_codigo.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 82, 234)));
        formulario_p.add(caja_codigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 347, 349, 37));

        txt_info6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txt_info6.setForeground(new java.awt.Color(0, 82, 234));
        txt_info6.setText("Contraseña");
        formulario_p.add(txt_info6, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 402, 85, -1));

        caja_contraseña.setForeground(new java.awt.Color(120, 120, 120));
        caja_contraseña.setText("jPasswordField2");
        caja_contraseña.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 82, 234)));
        formulario_p.add(caja_contraseña, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 424, 349, 36));

        check_terminos.setForeground(new java.awt.Color(120, 120, 120));
        check_terminos.setText("Aceptar ");
        formulario_p.add(check_terminos, new org.netbeans.lib.awtextra.AbsoluteConstraints(134, 478, -1, -1));

        label_terminos.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        label_terminos.setForeground(new java.awt.Color(0, 82, 234));
        label_terminos.setText("Terminos y condiciones");
        formulario_p.add(label_terminos, new org.netbeans.lib.awtextra.AbsoluteConstraints(208, 480, -1, -1));

        bt_registro.setBackground(new java.awt.Color(0, 82, 234));
        bt_registro.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        bt_registro.setForeground(new java.awt.Color(255, 255, 255));
        bt_registro.setText("Registarse");
        formulario_p.add(bt_registro, new org.netbeans.lib.awtextra.AbsoluteConstraints(141, 516, 201, 40));

        bt_faceId.setForeground(new java.awt.Color(0, 82, 234));
        bt_faceId.setText("Registrar mi cara");
        bt_faceId.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 82, 234), 2));
        formulario_p.add(bt_faceId, new org.netbeans.lib.awtextra.AbsoluteConstraints(141, 568, 201, 40));

        javax.swing.GroupLayout panel_fondoLayout = new javax.swing.GroupLayout(panel_fondo);
        panel_fondo.setLayout(panel_fondoLayout);
        panel_fondoLayout.setHorizontalGroup(
            panel_fondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_fondoLayout.createSequentialGroup()
                .addGap(313, 313, 313)
                .addComponent(panel_formulario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_fondoLayout.createSequentialGroup()
                .addContainerGap(412, Short.MAX_VALUE)
                .addComponent(formulario_p, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(289, 289, 289))
        );
        panel_fondoLayout.setVerticalGroup(
            panel_fondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_fondoLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(panel_formulario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(formulario_p, javax.swing.GroupLayout.PREFERRED_SIZE, 650, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(35, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel_fondo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(168, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(47, 47, 47)
                    .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(48, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel_fondo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(3, 3, 3)
                    .addComponent(jLayeredPane1)
                    .addGap(3, 3, 3)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new registro().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bt_faceId;
    private javax.swing.JButton bt_registro;
    private javax.swing.JTextField caja_codigo;
    private javax.swing.JPasswordField caja_contraseña;
    private javax.swing.JPasswordField caja_correo;
    private javax.swing.JTextField caja_nombre;
    private javax.swing.JTextField caja_usuario;
    private javax.swing.JCheckBox check_terminos;
    private javax.swing.JPanel formulario_p;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JLabel label_terminos;
    private javax.swing.JPanel panel_fondo;
    private javax.swing.JPanel panel_formulario;
    private javax.swing.JLabel txt_info;
    private javax.swing.JLabel txt_info1;
    private javax.swing.JLabel txt_info2;
    private javax.swing.JLabel txt_info3;
    private javax.swing.JLabel txt_info4;
    private javax.swing.JLabel txt_info5;
    private javax.swing.JLabel txt_info6;
    // End of variables declaration//GEN-END:variables
class PanelRedondeado extends javax.swing.JPanel {
    public PanelRedondeado() {
        setOpaque(false); // Importante para que se vea el redondeo
    }
    @Override
    protected void paintComponent(java.awt.Graphics g) {
        java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
        g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new java.awt.Color(255, 255, 255, 80));
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
        super.paintComponent(g);
    }
}
}
