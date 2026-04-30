package util;

import java.io.*;
import java.nio.file.*;

public class OpenCVLoader {

    public static void loadLibrary() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            String resourcePath;
            String fileName;

            if (os.contains("win")) {
                fileName = "opencv_java4110.dll";
                resourcePath = "/libs/opencv/windows/x64/" + fileName;
            } else if (os.contains("linux")) {
                fileName = "libopencv_java4110.so";
                resourcePath = "/libs/opencv/linux/x64/" + fileName;
            } else if (os.contains("mac")) {
                fileName = "libopencv_java4110.dylib";
                resourcePath = "/libs/opencv/mac/x64/" + fileName;
            } else {
                throw new UnsupportedOperationException("OS no soportado: " + os);
            }

            InputStream in = OpenCVLoader.class.getResourceAsStream(resourcePath);

            if (in == null) {
                throw new FileNotFoundException("No se encontró el archivo: " + resourcePath);
            }

            String ext = fileName.substring(fileName.lastIndexOf('.'));
            Path tempFile = Files.createTempFile("opencv_", ext);
            tempFile.toFile().deleteOnExit();

            Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
            in.close();

            System.load(tempFile.toAbsolutePath().toString());
            System.out.println("✓ OpenCV cargado correctamente.");

        } catch (Exception e) {
            throw new RuntimeException("Error al cargar OpenCV: " + e.getMessage(), e);
        }
    }
}
