package ua.kiev.sa;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import ua.kiev.sa.utils.CompileUtils;
import ua.kiev.sa.utils.FileUtil;
import ua.kiev.sa.utils.ReflectionUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class ClassLoaderTest {
    private String fileContent;
    private static String SOURCE_PATH = System.getProperty("user.dir") + "\\src\\";
    private static String COMPILE_PATH = System.getProperty("user.dir") + "\\target\\classes\\";
    private static String CLASS_NAME = "ua.kiev.sa.service.TextService";

    @Before
    public void before() throws IOException {
        String sourceName = (SOURCE_PATH + CLASS_NAME).replaceAll("\\.", "/") + ".java";
        File file = new File(sourceName);
        fileContent= Files.readString(file.toPath());
    }

    @Test
    public void testCustomClassLoader() throws IllegalAccessException, InstantiationException,
            InvocationTargetException, NoSuchMethodException, InterruptedException {

        MyCustomClassLoader customClassLoader = new MyCustomClassLoader(COMPILE_PATH);
        Object result = new ReflectionUtils().invokeMethodOfLoadedClassInstance(customClassLoader, CLASS_NAME, "staticText");
        System.out.println(result);

        System.out.println("\nTo verify manually the program works \n" +
                "change the string value returned by staticText() method of TextService class \n" +
                "and recompile it\n");

        while (true) {
            if (customClassLoader.isClassChanged(COMPILE_PATH, CLASS_NAME)) {
                System.out.println(CLASS_NAME + " has been changed ");
                customClassLoader = new MyCustomClassLoader(COMPILE_PATH);
                result = new ReflectionUtils().invokeMethodOfLoadedClassInstance(customClassLoader, CLASS_NAME, "staticText");
                System.out.println(result);
            }
            System.out.println(CLASS_NAME + " hasn't been changed ");
            Thread.sleep(5_000);
        }
    }


    @Test
    public void testCustomClassLoaderAfterAutomaticFileRecompiling() throws IllegalAccessException, InstantiationException,
            InvocationTargetException, NoSuchMethodException, InterruptedException {

        int i = 0;
        String sourceName = (SOURCE_PATH + CLASS_NAME).replaceAll("\\.", "/") + ".java";
        String targetDirectory = (COMPILE_PATH + CLASS_NAME.substring(0, CLASS_NAME.lastIndexOf("."))).replaceAll("\\.", "/");
        File sourceFile = new File(sourceName);
        MyCustomClassLoader customClassLoader = new MyCustomClassLoader(COMPILE_PATH);
        Object result = new ReflectionUtils().invokeMethodOfLoadedClassInstance(customClassLoader, CLASS_NAME, "staticText");
        System.out.println(result);

        FileUtil.changeFile(new File(sourceName), "Some static text", "Some static text" + i++);
        try {
            new CompileUtils().compileClassFile(new File(sourceName), targetDirectory);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long lastModified = sourceFile.lastModified();
        for (int j = 0; j < 10; j++) {
            if (lastModified < sourceFile.lastModified()) {
                lastModified=sourceFile.lastModified();
                System.out.println(CLASS_NAME + " has been changed ");
                try {
                    new CompileUtils().compileClassFile(new File(sourceName), COMPILE_PATH);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                customClassLoader = new MyCustomClassLoader(COMPILE_PATH);
                Object updatedResult = new ReflectionUtils().invokeMethodOfLoadedClassInstance(customClassLoader, CLASS_NAME, "staticText");
                System.out.println(updatedResult);
            }
            System.out.println(CLASS_NAME + " hasn't been changed ");
            Thread.sleep(1000);
            FileUtil.changeFile(new File(sourceName), "Some static text", "Some static text" + i++);

        }
        assertEquals("9876543210",
                new ReflectionUtils().invokeMethodOfLoadedClassInstance(new MyCustomClassLoader(COMPILE_PATH), CLASS_NAME, "staticText").toString().replaceAll("Some static text", "") );
    }


    @After
    public void after() throws IOException {
        String sourceName = (SOURCE_PATH + CLASS_NAME).replaceAll("\\.", "/") + ".java";
        File file = new File(sourceName);
        Files.write(file.toPath(), fileContent.getBytes(StandardCharsets.UTF_8));
    }

}