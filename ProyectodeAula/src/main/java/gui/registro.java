package gui;

import gui.login;
import config.Conexion;
import util.placeHolderJtext;
import util.Validación;
import java.awt.BorderLayout;
import java.io.File;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.Group; 
import javafx.scene.media.Media; 
import javafx.scene.media.MediaPlayer; 
import javafx.scene.media.MediaView; 
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Desktop;
import util.Validación;
import util.Validación;
import util.placeHolderJtext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
import java.net.URL;
import java.util.List;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class registro extends javax.swing.JFrame {
    private boolean rostroGuardado = false;
    private java.util.List<Mat> fotosCapturadas;
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(registro.class.getName());

    private final JFXPanel jfxpanel = new JFXPanel();
    public registro() {
        initFX(); 
        initComponents();
        
        placeHolderJtext.addPlaceholder(caja_nombre, "Nombre completo");
        placeHolderJtext.addPlaceholder(caja_correo, "Correo electrónico");
        placeHolderJtext.addPlaceholder(caja_usuario, "Usuario");
         placeHolderJtext.addPlaceholderPassword(caja_contraseña, "Contraseña");
        placeHolderJtext.addPlaceholder(caja_codigo, "Código");
        
        
        
        
       panel_formulario.setBackground(new java.awt.Color(0, 0, 0, 0));
    panel_formulario.setOpaque(false);

    jLayeredPane1.setOpaque(false);
    jLayeredPane1.setBackground(new java.awt.Color(0, 0, 0, 0));

    jLayeredPane1.add(panel_fondo, Integer.valueOf(-1));
    jLayeredPane1.add(panel_formulario, Integer.valueOf(0));

    panel_fondo.setBounds(0, 0, jLayeredPane1.getWidth(), jLayeredPane1.getHeight());
    panel_formulario.setBounds(0, 0, jLayeredPane1.getWidth(), jLayeredPane1.getHeight());

    jLayeredPane1.setLayer(panel_fondo, javax.swing.JLayeredPane.DEFAULT_LAYER);
    jLayeredPane1.setLayer(panel_formulario, javax.swing.JLayeredPane.PALETTE_LAYER);

    panel_fondo.setPreferredSize(new java.awt.Dimension(1280, 720));
    panel_fondo.setMinimumSize(new java.awt.Dimension(1280, 720));

    setResizable(false);
    setLocationRelativeTo(null);

    panel_fondo.setLayout(new BorderLayout());
    panel_fondo.add(jfxpanel, BorderLayout.CENTER);
    
    addWindowListener(new java.awt.event.WindowAdapter() {
        @Override
        public void windowOpened(java.awt.event.WindowEvent e) {
            createScene();
        }
    });


    setVisible(true);

  
    }
    
  private void createScene() {
    Platform.runLater(new Runnable() {
        @Override
        public void run() {
            try {
                
                String ruta = System.getProperty("user.dir") + "\\fondo video.mp4";
                File file = new File(ruta);

                if (!file.exists()) {
                System.out.println("No se encontró el video");
             return;
                } 
                
                Media media = new Media(file.toURI().toString());
                MediaPlayer oracleVid = new MediaPlayer(media);
                MediaView mediaView = new MediaView(oracleVid);
                
                Group root = new Group(mediaView);
                Scene scene = new Scene(root); 
                
                mediaView.setFitWidth(1280);
                mediaView.setFitHeight(720);
              

                

                jfxpanel.setScene(scene); 
                
                oracleVid.setCycleCount(MediaPlayer.INDEFINITE);
                oracleVid.setVolume(0.7);
                mediaView.setVisible(false);

                oracleVid.setOnReady(() -> {
                    mediaView.setVisible(true);
                    oracleVid.play();
                });
                
            } catch (Exception e) {
                e.printStackTrace(); 
            }
        }
    });
}
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLayeredPane1 = new javax.swing.JLayeredPane();
        label_volverLogin = new javax.swing.JLabel();
        panel_fondo = new javax.swing.JPanel();
        panel_formulario = new javax.swing.JPanel();
        formulario_p = new PanelRedondeado();
        txt_info = new javax.swing.JLabel();
        txt_info1 = new javax.swing.JLabel();
        txt_info2 = new javax.swing.JLabel();
        caja_nombre = new javax.swing.JTextField();
        txt_info3 = new javax.swing.JLabel();
        txt_info4 = new javax.swing.JLabel();
        caja_usuario = new javax.swing.JTextField();
        txt_info5 = new javax.swing.JLabel();
        caja_codigo = new javax.swing.JTextField();
        txt_info6 = new javax.swing.JLabel();
        caja_contraseña = new javax.swing.JPasswordField();
        check_terminos = new javax.swing.JCheckBox();
        label_terminos = new javax.swing.JLabel();
        bt_registroface = new javax.swing.JButton();
        bt_registro = new javax.swing.JButton();
        caja_correo = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        label_volverLogin.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        label_volverLogin.setForeground(new java.awt.Color(24, 90, 219));
        label_volverLogin.setText("   <- Volver ");
        label_volverLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                label_volverLoginMouseClicked(evt);
            }
        });

        jLayeredPane1.setLayer(label_volverLogin, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label_volverLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(1209, Short.MAX_VALUE))
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(label_volverLogin)
                .addContainerGap(667, Short.MAX_VALUE))
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
        caja_nombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                validacion(evt);
            }
        });
        formulario_p.add(caja_nombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 124, 349, 33));

        txt_info3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txt_info3.setForeground(new java.awt.Color(0, 82, 234));
        txt_info3.setText("Correo institucional");
        formulario_p.add(txt_info3, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 175, 117, -1));

        txt_info4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txt_info4.setForeground(new java.awt.Color(0, 82, 234));
        txt_info4.setText("Usuario");
        formulario_p.add(txt_info4, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 252, 72, -1));

        caja_usuario.setForeground(new java.awt.Color(120, 120, 120));
        caja_usuario.setText("Ingrese su usuario");
        caja_usuario.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 82, 234)));
        caja_usuario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                validar(evt);
            }
        });
        formulario_p.add(caja_usuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 274, 349, 33));

        txt_info5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txt_info5.setForeground(new java.awt.Color(0, 82, 234));
        txt_info5.setText("Código estudiantil");
        formulario_p.add(txt_info5, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 325, -1, -1));

        caja_codigo.setForeground(new java.awt.Color(120, 120, 120));
        caja_codigo.setText("Ingrese su codigo");
        caja_codigo.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 82, 234)));
        caja_codigo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                validarq(evt);
            }
        });
        formulario_p.add(caja_codigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 347, 349, 37));

        txt_info6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txt_info6.setForeground(new java.awt.Color(0, 82, 234));
        txt_info6.setText("Contraseña");
        formulario_p.add(txt_info6, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 402, 85, -1));

        caja_contraseña.setForeground(new java.awt.Color(120, 120, 120));
        caja_contraseña.setText("jPasswordField2");
        caja_contraseña.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 82, 234)));
        caja_contraseña.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                vali(evt);
            }
        });
        formulario_p.add(caja_contraseña, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 424, 349, 36));

        check_terminos.setForeground(new java.awt.Color(120, 120, 120));
        check_terminos.setText("Aceptar ");
        formulario_p.add(check_terminos, new org.netbeans.lib.awtextra.AbsoluteConstraints(134, 478, -1, -1));

        label_terminos.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        label_terminos.setForeground(new java.awt.Color(0, 82, 234));
        label_terminos.setText("Terminos y condiciones");
        label_terminos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                terminos(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                entrada(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                salida(evt);
            }
        });
        formulario_p.add(label_terminos, new org.netbeans.lib.awtextra.AbsoluteConstraints(208, 480, -1, -1));

        bt_registroface.setBackground(new java.awt.Color(0, 82, 234));
        bt_registroface.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        bt_registroface.setForeground(new java.awt.Color(255, 255, 255));
        bt_registroface.setText("Registrar mi cara");
        bt_registroface.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_registrofaceActionPerformed(evt);
            }
        });
        formulario_p.add(bt_registroface, new org.netbeans.lib.awtextra.AbsoluteConstraints(141, 516, 201, 40));

        bt_registro.setForeground(new java.awt.Color(0, 82, 234));
        bt_registro.setText("¡Registrarme!");
        bt_registro.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 82, 234), 2));
        bt_registro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_registroActionPerformed(evt);
            }
        });
        formulario_p.add(bt_registro, new org.netbeans.lib.awtextra.AbsoluteConstraints(141, 568, 201, 40));

        caja_correo.setForeground(new java.awt.Color(102, 102, 102));
        caja_correo.setText("usuario@unicolombo.edu.co");
        caja_correo.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 82, 234)));
        caja_correo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                validación(evt);
            }
        });
        formulario_p.add(caja_correo, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 200, 350, 40));

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
                    .addContainerGap(23, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel_fondo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(3, 3, 3)
                    .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(19, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void validación(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_validación
        // TODO add your handling code here:
        String correo = caja_correo.getText();
        boolean valido = Validación.esCorreoInstitucional(correo);

        marcarCampo(caja_correo, valido);
    }//GEN-LAST:event_validación

    private void validacion(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_validacion
        // TODO add your handling code here:
        String correo = caja_nombre.getText();
        boolean valido = Validación.esNombreValido(correo);
        marcarCampo(caja_nombre, valido);
    }//GEN-LAST:event_validacion

    private void validar(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_validar
        // TODO add your handling code here:
        String correo = caja_usuario.getText();
        boolean valido = Validación.esUsuarioValido(correo);

        marcarCampo(caja_usuario, valido);
    }//GEN-LAST:event_validar

    private void validarq(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_validarq
        // TODO add your handling code here:
        String correo = caja_codigo.getText();
        boolean valido = Validación.esCodigoValido(correo);

        marcarCampo(caja_codigo, valido);
    }//GEN-LAST:event_validarq

    private void vali(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_vali
        // TODO add your handling code here:
        String correo = caja_contraseña.getText();
        boolean valido = Validación.esPasswordValida(correo);

        marcarCampo(caja_contraseña, valido);
    }//GEN-LAST:event_vali

    private void bt_registrofaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_registrofaceActionPerformed
        String gmail = caja_correo.getText().trim().toLowerCase();

    if (gmail.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Ingresa tu correo primero");
        return;
    }

    if (rostroGuardado) {
        JOptionPane.showMessageDialog(this, "Ya has registrado tu rostro");
        return;
    }

    // abrir ventana de captura
    new VentanaCaptura(gmail, this).setVisible(true);

    }//GEN-LAST:event_bt_registrofaceActionPerformed

    private void bt_registroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_registroActionPerformed
        

   
        String nombre  = caja_nombre.getText().trim();
        String correo  = caja_correo.getText().trim().toLowerCase();
        String usuario = caja_usuario.getText().trim();
        String codigo  = caja_codigo.getText().trim();
        String pass    = new String(caja_contraseña.getPassword());
 
        if (nombre.isEmpty() || correo.isEmpty() || usuario.isEmpty()
                || codigo.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios");
            return;
        }
        if (!Validación.esNombreValido(nombre)) {
            JOptionPane.showMessageDialog(this, "Nombre inválido");
            caja_nombre.requestFocus(); return;
        }
        if (!Validación.esCorreoInstitucional(correo)) {
            JOptionPane.showMessageDialog(this, "Correo institucional inválido");
            caja_correo.requestFocus(); return;
        }
        if (!Validación.esUsuarioValido(usuario)) {
            JOptionPane.showMessageDialog(this, "Usuario inválido");
            caja_usuario.requestFocus(); return;
        }
        if (!Validación.esCodigoValido(codigo)) {
            JOptionPane.showMessageDialog(this, "Código estudiantil inválido");
            caja_codigo.requestFocus(); return;
        }
        if (!Validación.esPasswordValida(pass)) {
            JOptionPane.showMessageDialog(this, "Contraseña inválida");
            caja_contraseña.requestFocus(); return;
        }
        if (!check_terminos.isSelected()) {
            JOptionPane.showMessageDialog(this, "Debe aceptar los términos y condiciones");
            check_terminos.requestFocus(); return;
        }
 
        try {
            Connection con = Conexion.conectar();
            String sql = "INSERT INTO usuarios "
                    + "(nombre_completo, correo, usuario, codigo_estudiantil, contrasena) "
                    + "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setString(2, correo);
            ps.setString(3, usuario);
            ps.setString(4, codigo);
            ps.setString(5, pass);
            ps.executeUpdate();
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar usuario: " + e.getMessage());
            return;
        }
 
        if (fotosCapturadas != null && !fotosCapturadas.isEmpty()) {
            guardarFotosConVariantes(correo, fotosCapturadas);
            JOptionPane.showMessageDialog(this, "Usuario registrado con rostro ✅");
        } else {
            JOptionPane.showMessageDialog(this, "Registro exitoso (sin datos faciales).");
        }
 
        new login().setVisible(true);
        this.dispose();
    }
 
    // ════════════════════════════════════════════════════════════
    //  GUARDADO CON VARIANTES SINTÉTICAS — MEJORADO
    //
    //  Cambio respecto al original:
    //  - Convierte a GRIS antes de generar variantes, para que
    //    todas las imágenes vivan en el mismo espacio de color
    //    que usa ComparadorRostros.preprocesar().
    //  - Reemplaza add/subtract de Scalar BGR por gamma real
    //    (oscuro γ=2.2, claro γ=0.5) y CLAHE agresivo.
    //  - Agrega variante de ruido gaussiano.
    //  - 20 fotos × 6 variantes = 120 imágenes en gris.
    // ════════════════════════════════════════════════════════════
 
    private void guardarFotosConVariantes(String correo, List<Mat> fotos) {
 
        File carpeta = new File(
                System.getProperty("user.dir") + "/dataset/" + correo);
 
        if (!carpeta.exists()) carpeta.mkdirs();
 
        int idx = 1;
 
        for (Mat original : fotos) {
 
            // Convertir a gris UNA sola vez por foto
            Mat gris = new Mat();
            Imgproc.cvtColor(original, gris, Imgproc.COLOR_BGR2GRAY);
            Imgproc.resize(gris, gris, new Size(128, 128));
 
            // ── Variante 1: gris original ────────────────────────
            Imgcodecs.imwrite(
                    carpeta.getAbsolutePath() + "/foto_" + idx++ + "_orig.png",
                    gris);
 
            // ── Variante 2: CLAHE agresivo ───────────────────────
            // Simula ambiente muy iluminado con contraste alto
            Mat claheVar = new Mat();
            org.opencv.imgproc.CLAHE clahe =
                    Imgproc.createCLAHE(8.0, new Size(4, 4));
            clahe.apply(gris, claheVar);
            Imgcodecs.imwrite(
                    carpeta.getAbsolutePath() + "/foto_" + idx++ + "_clahe.png",
                    claheVar);
 
            // ── Variante 3: gamma oscura (γ=2.2) ─────────────────
            // Simula poca luz / habitación tenue
            Mat oscura = aplicarGamma(gris, 2.2);
            Imgcodecs.imwrite(
                    carpeta.getAbsolutePath() + "/foto_" + idx++ + "_dark.png",
                    oscura);
 
            // ── Variante 4: gamma clara (γ=0.5) ──────────────────
            // Simula sobreexposición / flash / ventana detrás
            Mat clara = aplicarGamma(gris, 0.5);
            Imgcodecs.imwrite(
                    carpeta.getAbsolutePath() + "/foto_" + idx++ + "_bright.png",
                    clara);
 
            // ── Variante 5: flip horizontal ───────────────────────
            // Simula cara ligeramente girada
            Mat volteada = new Mat();
            Core.flip(gris, volteada, 1);
            Imgcodecs.imwrite(
                    carpeta.getAbsolutePath() + "/foto_" + idx++ + "_flip.png",
                    volteada);
 
            // ── Variante 6: ruido gaussiano suave ─────────────────
            // Simula cámara de baja calidad o compresión de video
            Mat ruido = new Mat(gris.size(), gris.type());
            Core.randn(ruido, 0, 10);
            Mat ruidosa = new Mat();
            Core.add(gris, ruido, ruidosa);
            ruidosa.convertTo(ruidosa, CvType.CV_8U);
            Imgcodecs.imwrite(
                    carpeta.getAbsolutePath() + "/foto_" + idx++ + "_noise.png",
                    ruidosa);
        }
 
        System.out.println("Dataset guardado: " + (idx - 1)
                + " fotos en " + carpeta.getAbsolutePath());
        // 20 fotos × 6 variantes = 120 imágenes en gris
    }
 
    /**
     * Corrección gamma sobre imagen en gris CV_8U.
     * gamma > 1 oscurece  |  gamma < 1 aclara
     */
    private Mat aplicarGamma(Mat src, double gamma) {
        Mat float32 = new Mat();
        src.convertTo(float32, CvType.CV_32F, 1.0 / 255.0);
        Core.pow(float32, gamma, float32);
        Mat resultado = new Mat();
        float32.convertTo(resultado, CvType.CV_8U, 255.0);
        return resultado;
    
        
    }//GEN-LAST:event_bt_registroActionPerformed
    
    private void terminos(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_terminos
        // TODO add your handling code here:
        try {
            String ruta = System.getProperty("user.dir") + "\\Terminos_Condiciones_SistemaVotacion.pdf";
            File file = new File(ruta);

            if (!file.exists()) {
                JOptionPane.showMessageDialog(this, "No se encontró el PDF");
                return;
            }

            Desktop.getDesktop().open(file);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al abrir el PDF");
            e.printStackTrace();
        }
        
    }//GEN-LAST:event_terminos

    private void entrada(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_entrada
        // TODO add your handling code here:
        label_terminos.setForeground(Color.RED);
    }//GEN-LAST:event_entrada

    private void salida(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_salida
        // TODO add your handling code here:
       label_terminos.setForeground(Color.BLUE);
    }//GEN-LAST:event_salida

    private void label_volverLoginMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_label_volverLoginMouseClicked
        // Label para mandar a registro

        login l = new login();
        l.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_label_volverLoginMouseClicked

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
    private javax.swing.JButton bt_registro;
    private javax.swing.JButton bt_registroface;
    private javax.swing.JTextField caja_codigo;
    private javax.swing.JPasswordField caja_contraseña;
    private javax.swing.JTextField caja_correo;
    private javax.swing.JTextField caja_nombre;
    private javax.swing.JTextField caja_usuario;
    private javax.swing.JCheckBox check_terminos;
    private javax.swing.JPanel formulario_p;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JLabel label_terminos;
    private javax.swing.JLabel label_volverLogin;
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
        setOpaque(false); 
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

public void marcarCampo(JTextField campo, boolean valido) {
    if (valido) {
        campo.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.GREEN));
    } else {
        campo.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.RED));
    }
}
private void initFX() {
    try {
        Platform.startup(() -> {});
    } catch (IllegalStateException e) {
      
    }
}
private void bt_faceActionPerformed(java.awt.event.ActionEvent evt) {
    String nombre = caja_usuario.getText();

    if (nombre.isEmpty()) {
        javax.swing.JOptionPane.showMessageDialog(this, "Ingresa tu nombre primero");
        return;
    }

    new VentanaCaptura(nombre, this).setVisible(true);
    this.setVisible(false);
}
public void rostroCapturadoExitosamente() {
    rostroGuardado = true;
    bt_registroface.setEnabled(false);

    JOptionPane.showMessageDialog(this, "Rostro guardado correctamente");
}
public void setFotosCapturadas(List<Mat> fotos) {
    this.fotosCapturadas = fotos;
}

 
}
