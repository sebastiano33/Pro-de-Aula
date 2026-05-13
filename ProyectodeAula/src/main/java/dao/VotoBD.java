package dao;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Maneja el registro y consulta de votos usando archivos TXT.
 * Formato del archivo votos.txt:
 *   id_usuario|id_candidato|id_eleccion|fecha_hora
 */
public class VotoBD {

    // Ruta del archivo donde se guardan los votos
    private static final String ARCHIVO_VOTOS = "votos.txt";

    // ── Registrar un voto ────────────────────────────────────────
    public boolean registrarVoto(int idUsuario, int idCandidato, int idEleccion) {
        // 1. Verificar si el usuario ya votó en esa elección
        if (yaVoto(idUsuario, idEleccion)) {
            return false; // Ya votó
        }

        // 2. Escribir el voto en el archivo TXT
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO_VOTOS, true))) {
            String fecha = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            // Formato: id_usuario|id_candidato|id_eleccion|fecha
            bw.write(idUsuario + "|" + idCandidato + "|" + idEleccion + "|" + fecha);
            bw.newLine();
            return true;
        } catch (IOException e) {
            System.out.println("Error al guardar voto: " + e.getMessage());
            return false;
        }
    }

    // ── Verificar si un usuario ya votó en una elección ─────────
    public boolean yaVoto(int idUsuario, int idEleccion) {
        File archivo = new File(ARCHIVO_VOTOS);
        if (!archivo.exists()) return false;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;
                String[] partes = linea.split("\\|");
                if (partes.length >= 3) {
                    int uid = Integer.parseInt(partes[0].trim());
                    int eid = Integer.parseInt(partes[2].trim());
                    if (uid == idUsuario && eid == idEleccion) {
                        return true;
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error al leer votos: " + e.getMessage());
        }
        return false;
    }

    // ── Contar votos de un candidato en una elección ─────────────
    public int contarVotos(int idCandidato, int idEleccion) {
        File archivo = new File(ARCHIVO_VOTOS);
        if (!archivo.exists()) return 0;

        int total = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;
                String[] partes = linea.split("\\|");
                if (partes.length >= 3) {
                    int cid = Integer.parseInt(partes[1].trim());
                    int eid = Integer.parseInt(partes[2].trim());
                    if (cid == idCandidato && eid == idEleccion) {
                        total++;
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error al contar votos: " + e.getMessage());
        }
        return total;
    }

    // ── Obtener mapa candidato → votos para una elección ─────────
    // Útil para PanelResultados sin necesitar BD
    public Map<Integer, Integer> obtenerResultados(int idEleccion) {
        Map<Integer, Integer> resultados = new HashMap<>();
        File archivo = new File(ARCHIVO_VOTOS);
        if (!archivo.exists()) return resultados;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;
                String[] partes = linea.split("\\|");
                if (partes.length >= 3) {
                    int cid = Integer.parseInt(partes[1].trim());
                    int eid = Integer.parseInt(partes[2].trim());
                    if (eid == idEleccion) {
                        resultados.merge(cid, 1, Integer::sum);
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error al obtener resultados: " + e.getMessage());
        }
        return resultados;
    }
}