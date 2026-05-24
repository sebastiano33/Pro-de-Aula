/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui.panels;
 
import config.Conexion;
import gui.components.CardCandidato;
import gui.menuPrincipal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class PanelCandidatos extends javax.swing.JPanel {
     private menuPrincipal menu;
 
    public PanelCandidatos(menuPrincipal menu1) {
        this.menu = menu1;
        initComponents();
        
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
      public void cargarCandidatos(int idEleccion) {
        try {
            Connection con = Conexion.conectar();
            String sql = "SELECT * FROM candidatos WHERE id_eleccion = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idEleccion);
            ResultSet rs = ps.executeQuery();
 
            this.removeAll();
            this.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER,25,25));
            this.setBackground(new java.awt.Color(245,245,245));
 
            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String carrera = rs.getString("carrera");
                String foto = rs.getString("foto");
                int idCandidato = rs.getInt("id_candidato");
 
                // ← Ahora se pasa idEleccion para que el voto se guarde correctamente
                CardCandidato card = new CardCandidato(nombre, carrera, foto, rs.getInt("id_candidato"), idEleccion);
                this.add(card);
            }
            // Botón volver
javax.swing.JButton btn_volver = new javax.swing.JButton("←");
btn_volver.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 22));
btn_volver.setForeground(java.awt.Color.WHITE);
btn_volver.setBackground(new java.awt.Color(0, 74, 173));
btn_volver.setFocusPainted(false);
btn_volver.setBorderPainted(false);
btn_volver.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
btn_volver.addActionListener(e -> {
    if (menu != null) menu.restaurarMenuPrincipal();
});
this.add(btn_volver);

this.revalidate();
            this.revalidate();
            
 
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}