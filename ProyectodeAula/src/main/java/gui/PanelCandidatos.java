/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui;
 
import config.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
 
public class PanelCandidatos extends javax.swing.JPanel {
 
    public PanelCandidatos() {
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
            this.setLayout(new java.awt.GridLayout(0, 2));
 
            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String carrera = rs.getString("carrera");
                String foto = rs.getString("foto");
                int idCandidato = rs.getInt("id_candidato");
 
                // ← Ahora se pasa idEleccion para que el voto se guarde correctamente
                CardCandidato card = new CardCandidato(nombre, carrera, foto, rs.getInt("id_candidato"), idEleccion);
                this.add(card);
            }
 
            this.revalidate();
            this.repaint();
 
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}