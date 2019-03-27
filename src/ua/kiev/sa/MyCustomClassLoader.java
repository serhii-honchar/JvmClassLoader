import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MyCustomClassLoader extends ClassLoader {
    private String path;

    private Map<String, Integer> classHashMap = new HashMap<>();
//    create an infinite loop that prints TextService#staticText() method result.
//    If TextService#staticText() text updated and class TextService was recompiled
//    an updated text version should start printing without restart the app


    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return super.loadClass(name);
    }

    public MyCustomClassLoader(String path) {
        this.path = path;
    }

    @Override
    public Class findClass(String className) {
        byte[] b = loadClassFromFile(path, className);
        classHashMap.put(className, Arrays.hashCode(b));
        Class<?> aClass = defineClass(className, b, 0, b.length);
        return aClass;
    }


    private byte[] loadClassFromFile(String path, String name) {
        InputStream is = null;
        byte[] returnData = null;

        name = (path + name).replaceAll("\\.", "/");

        String filePath = name + ".class";

        File file = new File(filePath);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            is = new FileInputStream(file);
            int tmp = 0;
            while ((tmp = is.read()) != -1) {
                os.write(tmp);
            }
            returnData = os.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                os.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }

        }
        return returnData;

    }

//    private byte[] loadClassFromFile(String fileName) {
//        String moduleName = getClass().getModule().getName();
//        InputStream inputStream  = null;
//        byte[] buffer;
//        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
//        int nextValue = 0;
//        try {
//            String filename1=fileName.replace('.', File.separatorChar) + ".class";
////            System.out.println(filename1);
////            String moduleAndFilename = moduleName+"/"+filename1;
////            System.out.println(moduleAndFilename);
////            inputStream  = ModuleLayer.boot().findModule(moduleName).get().getResourceAsStream(fileName);
//            inputStream = getSystemResourceAsStream(filename1);
//
//            while ((nextValue = inputStream.read()) != -1) {
//                byteStream.write(nextValue);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        buffer = byteStream.toByteArray();
//        return buffer;
//    }

    public boolean isClassChanged(String path, String filename) {
        return classHashMap.get(filename) != Arrays.hashCode(loadClassFromFile(path, filename));
    }

    private int getClassHash(String path, String filename) {
        if (classHashMap.get(filename) == null) {
            int hashCode = Arrays.hashCode(loadClassFromFile(path, filename));
            classHashMap.put(filename, hashCode);
            return hashCode;
        }
        return classHashMap.get(filename);
    }

//    private void compileClassFile(File file) {
//        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
//        StandardJavaFileManager manager = compiler.getStandardFileManager(null, null, null);
//        Iterable<? extends JavaFileObject> sources =
//                manager.getJavaFileObjectsFromFiles(Arrays.asList(file));
//        JavaCompiler.CompilationTask task = compiler.getTask(null, null, null, null, null, sources);
//        task.call();
//    }
}


