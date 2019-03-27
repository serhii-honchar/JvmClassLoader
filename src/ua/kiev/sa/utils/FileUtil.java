package ua.kiev.sa.utils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class FileUtil {

    public static void changeFile(File file, String oldString, String newString) {
        try {
            String content = Files.readString(file.toPath());
            content = content.replaceAll(oldString, newString);
            Files.write(file.toPath(), content.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
