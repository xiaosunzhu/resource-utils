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
package net.sunyijun.resource.config;


import net.sunyijun.resource.UnicodeInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;


/**
 * Properties file read/write util. Compatible with
 * unicode that include BOM.
 *
 * @author yijun.sun
 * @see UnicodeInputStream
 * @since 0.0.1
 */
class PropertiesIO {

    /**
     * Read properties file.
     *
     * @param absolutePath absolute path in file system.
     */
    public static Properties load(String absolutePath) {
        Properties configs = new Properties();
        if (absolutePath == null) {
            return configs;
        }
        File configFile = new File(absolutePath);
        FileInputStream inStream = null;
        try {
            inStream = new FileInputStream(configFile);
            configs.load(new UnicodeInputStream(inStream).skipBOM());
        } catch (IOException e) {
            LOGGER.warn("Load " + absolutePath + " error!", e);
        } finally {
            try {
                if (inStream != null) {
                    inStream.close();
                }
            } catch (IOException ignore) {
                // do nothing.
            }
        }
        return configs;
    }

    /**
     * Write into properties file.
     *
     * @param absolutePath absolute path in file system.
     * @param configs      all configs to write.
     * @throws java.io.IOException
     */
    public static void store(String absolutePath, Properties configs) throws IOException {
        OutputStream outStream = null;
        try {
            outStream = new FileOutputStream(new File(absolutePath));
            configs.store(outStream, null);
        } finally {
            try {
                if (outStream != null) {
                    outStream.close();
                }
            } catch (IOException ignore) {
                // do nothing.
            }
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesIO.class);

}
