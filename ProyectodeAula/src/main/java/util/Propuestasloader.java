/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author DISTRIEMPAQUES
 */
public class Propuestasloader {
     public static String obtenerPropuestas(String nombreCandidato) {

        StringBuilder propuestas = new StringBuilder();

        boolean encontrado = false;

        try {

            InputStream is =
                Propuestasloader.class.getResourceAsStream(
                    "/propuestas/propuestas.txt"
                );

            BufferedReader br = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8)
            );

            String linea;

            while ((linea = br.readLine()) != null) {

                linea = linea.trim();

                // Detecta secciones
                if (linea.startsWith("[") && linea.endsWith("]")) {

                    String nombre =
                        linea.substring(1, linea.length() - 1);

                    // Si ya encontró otro candidato → detener
                    if (encontrado) {
                        break;
                    }

                    // Buscar candidato actual
                    if (nombre.equalsIgnoreCase(nombreCandidato)) {
                        encontrado = true;
                    }

                    continue;
                }

                // Guardar propuestas
                if (encontrado) {

                    propuestas.append(linea)
                               .append("\n");
                }
            }

            br.close();

        } catch (Exception e) {

            e.printStackTrace();

            return "Error al cargar propuestas.";
        }

        if (propuestas.isEmpty()) {

            return "No hay propuestas registradas.";
        }

        return propuestas.toString();
    }
}
