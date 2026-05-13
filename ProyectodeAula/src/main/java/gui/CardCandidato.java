/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui;
 
import dao.VotoBD;
import java.awt.Image;
import java.io.IOException;
import javax.swing.ImageIcon;
 
public class CardCandidato extends javax.swing.JPanel {
 
    private int idCandidato;
    private int idEleccion; // ← necesario para guardar el voto correctamente
 
    public CardCandidato(String nombre, String carrera, String foto, int idCandidato, int idEleccion) {
        initComponents();
        this.idCandidato = idCandidato;
        this.idEleccion = idEleccion;
 
        lbl_nombre.setText(nombre);
        lbl_carrera.setText(carrera);
 
        ImageIcon icon = new ImageIcon(foto);
        Image img = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        lbl_foto.setIcon(new ImageIcon(img));
 
        btn_votar.addActionListener(e -> votar());
    }
 
    private void votar() {
        btn_votar.setEnabled(false);
        btn_votar.setText("Verificando...");
 
        util.OpenCVLoader.loadLibrary();
 
        new Thread(() -> {
            try {
                org.opencv.videoio.VideoCapture camera = new org.opencv.videoio.VideoCapture(0);
 
                if (!camera.isOpened()) {
                    javax.swing.SwingUtilities.invokeLater(() -> {
                        javax.swing.JOptionPane.showMessageDialog(null, "No se pudo abrir la cámara");
                        btn_votar.setEnabled(true);
                        btn_votar.setText("Votar");
                    });
                    return;
                }
 
                java.io.InputStream xmlStream = getClass().getResourceAsStream("/haarcascade_frontalface_default.xml");
                java.nio.file.Path xmlTemp = java.nio.file.Files.createTempFile("haarcascade_", ".xml");
                java.nio.file.Files.copy(xmlStream, xmlTemp, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                xmlStream.close();
 
                org.opencv.objdetect.CascadeClassifier detector =
                        new org.opencv.objdetect.CascadeClassifier(xmlTemp.toAbsolutePath().toString());
 
                org.opencv.core.Mat frame = new org.opencv.core.Mat();
                org.opencv.core.Mat gray = new org.opencv.core.Mat();
 
                long inicio = System.currentTimeMillis();
                boolean permitido = false;
                String mensajeError = "";
 
                while (System.currentTimeMillis() - inicio < 3000) {
                    camera.read(frame);
                    if (frame.empty()) continue;
 
                    org.opencv.imgproc.Imgproc.cvtColor(frame, gray, org.opencv.imgproc.Imgproc.COLOR_BGR2GRAY);
                    org.opencv.core.MatOfRect rostros = new org.opencv.core.MatOfRect();
                    detector.detectMultiScale(gray, rostros);
 
                    int cantidad = rostros.toArray().length;
                    if (cantidad == 1) {
                        permitido = true;
                        break;
                    } else if (cantidad == 0) {
                        mensajeError = "No se detectó ninguna persona frente a la cámara.";
                    } else {
                        mensajeError = "Solo puede haber una persona frente a la cámara.";
                        permitido = false;
                        break;
                    }
                }
 
                camera.release();
 
                if (!permitido && mensajeError.isEmpty()) {
                    mensajeError = "No se detectó ninguna persona. Intente de nuevo.";
                }
 
                final boolean votoPermitido = permitido;
                final String errorFinal = mensajeError;
 
                javax.swing.SwingUtilities.invokeLater(() -> {
                    if (votoPermitido) {
    int idUsuario = util.SesionUsuario.getIdUsuario();
    dao.VotoBD votoBD = new dao.VotoBD();

    boolean registrado = votoBD.registrarVoto(idUsuario, idCandidato, idEleccion);

    if (registrado) {
        PanelGestionVotaciones.usuariosQueVotaron.add(idUsuario);
        javax.swing.JOptionPane.showMessageDialog(null,
            "✅ Voto registrado correctamente para el candidato #" + idCandidato);
        btn_votar.setEnabled(false);
        btn_votar.setText("✓ Votado");
    } else {
        javax.swing.JOptionPane.showMessageDialog(null,
            "❌ Ya votaste en esta elección. No puedes votar dos veces.");
        btn_votar.setEnabled(false);
        btn_votar.setText("Ya votaste");
    }
}
                });
 
            } catch (IOException ex) {
                System.getLogger(CardCandidato.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }).start();
    }
 
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    



    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbl_foto = new javax.swing.JLabel();
        lbl_nombre = new javax.swing.JLabel();
        lbl_carrera = new javax.swing.JLabel();
        lbl_numero = new javax.swing.JLabel();
        btn_votar = new javax.swing.JButton();

        setMaximumSize(new java.awt.Dimension(220, 280));
        setMinimumSize(new java.awt.Dimension(220, 280));
        setPreferredSize(new java.awt.Dimension(220, 280));

        lbl_nombre.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbl_nombre.setForeground(new java.awt.Color(0, 0, 255));

        lbl_carrera.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbl_carrera.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbl_numero.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        btn_votar.setBackground(new java.awt.Color(8, 51, 162));
        btn_votar.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        btn_votar.setForeground(new java.awt.Color(255, 255, 255));
        btn_votar.setText("Votar");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lbl_nombre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(43, 43, 43)
                                .addComponent(lbl_foto, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(lbl_numero, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addGap(44, 44, 44)
                                    .addComponent(lbl_carrera, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addComponent(btn_votar, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(55, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_foto, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbl_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbl_carrera, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_numero, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_votar, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_votar;
    private javax.swing.JLabel lbl_carrera;
    private javax.swing.JLabel lbl_foto;
    private javax.swing.JLabel lbl_nombre;
    private javax.swing.JLabel lbl_numero;
    // End of variables declaration//GEN-END:variables
}

