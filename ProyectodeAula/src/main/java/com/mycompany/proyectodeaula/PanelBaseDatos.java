package com.mycompany.proyectodeaula;

import org.opencv.core.*;
import org.opencv.videoio.VideoCapture;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.imgcodecs.Imgcodecs;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class PanelBaseDatos extends JPanel {

    private VideoCapture camera;
    private CascadeClassifier detector;
    private int contador = 0;

    public PanelBaseDatos() {
        JButton btnCapturar = new JButton("Capturar rostro");
        add(btnCapturar);

        try {
    InputStream is = getClass().getResourceAsStream("/haarcascade_frontalface_default.xml");

    if (is == null) {
        System.out.println("❌ No se encontró el XML");
        return;
    }

    Path temp = Files.createTempFile("cascade-", ".xml");
    Files.copy(is, temp, StandardCopyOption.REPLACE_EXISTING);

    detector = new CascadeClassifier(temp.toString());

    if (detector.empty()) {
        System.out.println("❌ Cascade no cargado");
    } else {
        System.out.println("✅ Cascade cargado en PanelBaseDatos");
    }

} catch (Exception e) {
    e.printStackTrace();
}
        camera = new VideoCapture(0);

        btnCapturar.addActionListener((ActionEvent e) -> capturar());
    }

    private void capturar() {
        Mat frame = new Mat();
        camera.read(frame);

        MatOfRect rostros = new MatOfRect();
        detector.detectMultiScale(frame, rostros);

        for (Rect rect : rostros.toArray()) {
            Mat rostro = new Mat(frame, rect);

            new File("dataset/persona1").mkdirs();
            String ruta = "dataset/persona1/rostro_" + contador++ + ".jpg";

            Imgcodecs.imwrite(ruta, rostro);
            System.out.println("Guardado: " + ruta);
        }
    }
}