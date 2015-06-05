/*
 * Copyright 2015 yijun.sun
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.sunyijun.resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Class path utils.
 *
 * @author yijun.sun
 * @since 0.0.5
 */
public class ClassPathUtil {

    private static volatile Boolean runInJar;
    private static volatile String[] classPathsInSystemProperty;
    private static volatile String[] classPathsIncludeManifest;

    /**
     * Test if current run as a jar(java -jar ....jar).
     *
     * @return if use java -jar ....jar to run, return true, else return false.
     */
    public static boolean testRunMainInJar() {
        if (runInJar == null) {
            synchronized (ClassPathUtil.class) {
                if (runInJar == null) {
                    runInJar = isRunInJar();
                }
            }
        }
        return runInJar;
    }

    private static boolean isRunInJar() {
        String[] classPaths = getClassPathsInSystemProperty();
        if (classPaths.length != 1) {
            return false;
        }
        String classPath = classPaths[0];
        String runCommand = System.getProperty("sun.java.command");
        return !(runCommand == null || runCommand.trim().isEmpty()) && runCommand.contains(classPath);
    }

    /**
     * <p>
     * Get all class path strings.
     * </p>
     * Uncertain is relative or absolute.<br>
     *
     * @return all class path string array.
     */
    public static String[] getAllClassPaths() {
        if (testRunMainInJar()) {
            return getClassPathsIncludeManifest();
        } else {
            return getClassPathsInSystemProperty();
        }
    }

    /**
     * <p>
     * Get all class path strings that is file system path, not jar file.
     * </p>
     * Uncertain is relative or absolute.<br>
     *
     * @return class paths that is file system path.
     */
    public static String[] getAllClassPathNotInJar() {
        String[] allClassPath = getAllClassPaths();
        List<String> allClassPathNotInJar = new ArrayList<String>();
        for (String classPath : allClassPath) {
            if (classPath.endsWith("jar")) {
                continue;
            }
            allClassPathNotInJar.add(classPath);
        }
        return allClassPathNotInJar.toArray(new String[allClassPathNotInJar.size()]);
    }

    /**
     * <p>
     * Get class path from System.getProperty("java.class.path").
     * </p>
     * If run in jar(java -jar ....jar), this class path is just running jar file,
     * more class path can't get from system properties, must get from "Class-Path" in manifest file.
     *
     * @return all class path string array.
     */
    public static String[] getClassPathsInSystemProperty() {
        if (classPathsInSystemProperty == null) {
            synchronized (ClassPathUtil.class) {
                if (classPathsInSystemProperty == null) {
                    String classPathAll = System.getProperty("java.class.path");
                    classPathsInSystemProperty = classPathAll.split(System.getProperty("path.separator"));
                }
            }
        }
        return classPathsInSystemProperty;
    }

    private static String[] getClassPathsIncludeManifest() {
        if (classPathsIncludeManifest == null) {
            synchronized (ClassPathUtil.class) {
                if (classPathsIncludeManifest == null) {
                    classPathsIncludeManifest = new String[0];
                    InputStream is = ClassPathUtil.class.getResourceAsStream("/META-INF/MANIFEST.MF");
                    if (is == null) {
                        return classPathsIncludeManifest;
                    }
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    String line;
                    String classPathString = "";
                    try {
                        boolean startClassPathLine = false;
                        while ((line = reader.readLine()) != null) {
                            if ((!startClassPathLine) && line.trim().startsWith("Class-Path:")) {
                                startClassPathLine = true;
                                classPathString = line;
                            } else if (startClassPathLine) {
                                if (!line.startsWith(" ")) {
                                    break;
                                }
                                classPathString += line.trim();
                            }
                        }
                    } catch (IOException e) {
                        return classPathsIncludeManifest;
                    } finally {
                        try {
                            reader.close();
                            is.close();
                        } catch (IOException ignored) {
                        }
                    }
                    String[] classPathInManifestTemp = classPathString.split(" ");
                    classPathsIncludeManifest =
                            new String[classPathInManifestTemp.length - 1 + getClassPathsInSystemProperty().length];
                    System.arraycopy(getClassPathsInSystemProperty(), 0, classPathsIncludeManifest, 0,
                            getClassPathsInSystemProperty().length);
                    System.arraycopy(classPathInManifestTemp, 1, classPathsIncludeManifest,
                            getClassPathsInSystemProperty().length,
                            classPathInManifestTemp.length - 1);//0 is "Class-Path:", remove
                }
            }
        }
        return classPathsIncludeManifest;
    }

}
