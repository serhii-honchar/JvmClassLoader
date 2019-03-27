package ua.kiev.sa.utils;

import javax.tools.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CompileUtils {

    public void compileClassFile(File sourceFile, String compiledFilePath) throws IOException {

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager manager = compiler.getStandardFileManager(null, null, null);
        Files.createDirectories(Paths.get(compiledFilePath));
        manager.setLocationFromPaths(StandardLocation.CLASS_OUTPUT, List.of(Path.of(compiledFilePath)));
        Iterable<? extends JavaFileObject> sources = manager.getJavaFileObjectsFromFiles(Collections.singletonList(sourceFile));
        JavaCompiler.CompilationTask task = compiler.getTask(null, manager, null, null, null, sources);
        task.call();

    }


}
