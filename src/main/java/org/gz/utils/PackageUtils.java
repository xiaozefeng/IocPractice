package org.gz.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;

/**
 * @author xiaozefeng
 */
public class PackageUtils {
    private static String root = PackageUtils.class.getClassLoader().getResource("").getPath();
    private static String separator = File.separator;
    private static List<Class> classes;

    private static final String classSuffix = ".class";

    private static final String point = ".";

    public static List<Class> getAllClass() {
        root = new File(root).getPath();
        classes = new ArrayList<>();
        listFilesForFolder(new File(root));
        return classes;
    }

    private static void listFilesForFolder(File folder) {
        Arrays.stream(Objects.requireNonNull(folder.listFiles()))
                .forEach(f -> {
                    if (f.isDirectory()) {
                        listFilesForFolder(f);
                    } else {
                        String path = f.getPath();
                        if (path.endsWith(classSuffix)) {
                            // 去掉绝对路径
                            String classPath = path.replaceAll(Matcher.quoteReplacement(root), "")
                                    // 更换间隔符
                                    .replaceAll(Matcher.quoteReplacement(separator), point)
                                    // 去掉后缀
                                    .replaceAll(classSuffix, "");

                            // 去掉开头的 .
                            if (classPath.startsWith(Matcher.quoteReplacement(point))) {
                                classPath = classPath.replaceFirst(Matcher.quoteReplacement(point), "");
                            }
                            try {
                                classes.add(Class.forName(classPath));
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

    }
}
