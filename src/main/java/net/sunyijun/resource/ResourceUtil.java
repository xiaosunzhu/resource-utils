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


import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;


/**
 * Copy resources to file system.<br/>
 *
 * @author yijun.sun
 * @since 0.0.1
 */
public class ResourceUtil {

    /**
     * Copy resources to file system.
     *
     * @param resourceAbsoluteClassPath resource's absolute class path, start with "/"
     * @param targetFile                target file
     * @throws java.io.IOException
     */
    public static void copyResourceToFile(String resourceAbsoluteClassPath, File targetFile) throws IOException {
        InputStream is = ResourceUtil.class.getResourceAsStream(resourceAbsoluteClassPath);
        if (is == null) {
            throw new IOException("Resource not found! " + resourceAbsoluteClassPath);
        }
        OutputStream os = null;
        try {
            os = new FileOutputStream(targetFile);
            byte[] buffer = new byte[2048];
            int length;
            while ((length = is.read(buffer)) != -1) {
                os.write(buffer, 0, length);
            }
            os.flush();
        } finally {
            try {
                is.close();
                if (os != null) {
                    os.close();
                }
            } catch (Exception ignore) {
                // ignore
            }
        }
    }

    /**
     * Get absolute path in file system from a classPath. If this resource not exists, return null.
     */
    public static String getAbsolutePath(String classPath) {
        URL configUrl = Thread.currentThread().getContextClassLoader().getResource(classPath);
        if (configUrl == null) {
            return null;
        }
        try {
            return configUrl.toURI().getPath();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
