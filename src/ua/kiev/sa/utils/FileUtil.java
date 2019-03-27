package ua.kiev.sa.utils;

import java.io.File;
import java.nio.file.Files;

public class FileUtils {
    public void changeFile(String path, String name, String oldString, String newString){
        name = (path + name).replaceAll("\\.", "/");
        String filePath = name + ".java;
        .UTF_8;

        String content = new String(Files.readAllBytes(path), charset);
        content = content.replaceAll(oldString, newString);
        Files.write(path, content.getBytes(charset));

    }
}
