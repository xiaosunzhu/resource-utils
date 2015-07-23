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


import net.sunyijun.resource.ClassPathUtil;
import net.sunyijun.resource.ResourceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Properties;


/**
 * Base config utils for on properties file.
 *
 * @author yijun.sun
 * @since 0.0.1
 */
public class OneProperties {

    private static final String TRUE = "true";

    private String propertiesAbsoluteClassPath;
    private String propertiesFilePath;

    /**
     * Properties object loaded from properties file.
     */
    private Properties configs;

    protected OneProperties() {
    }

    /**
     * Init properties file path. Will reload configs every time.
     */
    void initConfigs(String propertiesAbsoluteClassPath) {
        this.propertiesAbsoluteClassPath = propertiesAbsoluteClassPath;
        this.propertiesFilePath = ResourceUtil.getAbsolutePath(propertiesAbsoluteClassPath);
        loadConfigs();
    }

    /**
     * Load properties. Will refresh configs every time.
     */
    protected void loadConfigs() {
        configs = new Properties();
        // If run as a jar, find in file system classpath first, if not found, then get resource in jar.
        if (ClassPathUtil.testRunMainInJar()) {
            String[] classPathsInFileSystem = ClassPathUtil.getAllClassPathNotInJar();
            String workDir = System.getProperty("user.dir");
            String mainJarRelativePath = ClassPathUtil.getClassPathsInSystemProperty()[0];
            File mainJar = new File(workDir, mainJarRelativePath);
            File mainJarDir = mainJar.getParentFile();
            for (String classPath : classPathsInFileSystem) {
                File classPathFile = new File(classPath);
                File configFile;
                if (classPathFile.isAbsolute()) {
                    configFile = new File(classPathFile, propertiesAbsoluteClassPath);
                } else {
                    configFile = new File(new File(mainJarDir, classPath), propertiesAbsoluteClassPath);
                }
                if (configFile.exists() && configFile.isFile()) {
                    InputStream is = null;
                    try {
                        is = new FileInputStream(configFile);
                        loadConfigsFromStream(is);
                    } catch (FileNotFoundException e) {
                        LOGGER.warn("Load config file " + configFile.getPath() + " error!", e);
                    }
                    return;
                }
            }
        }
        if (propertiesFilePath == null) {
            if (propertiesAbsoluteClassPath == null) {
                return;
            }
            InputStream is = OneProperties.class.getResourceAsStream(propertiesAbsoluteClassPath);
            loadConfigsFromStream(is);
        } else {
            configs = PropertiesIO.load(propertiesFilePath);
        }
    }

    private void loadConfigsFromStream(InputStream is) {
        if (is == null) {
            configs = new Properties();
            return;
        }
        try {
            configs = PropertiesIO.load(is);
        } finally {
            try {
                is.close();
            } catch (IOException ignored) {
            }
        }
    }

    /**
     * <p>Get config string.</p>
     * If not config will return null.
     *
     * @param key config key
     * @return string value
     */
    protected String getConfig(IConfigKey key) {
        if (configs == null) {
            loadConfigs();
        }
        String value = configs.getProperty(key.getKeyString());
        if (value == null && key instanceof IConfigKeyHaveDefault) {
            return ((IConfigKeyHaveDefault) key).getDefaultValueStr();
        }
        return value;
    }

    /**
     * <p>Get config string. Config key with prefix.</p>
     * If not config will return null.
     *
     * @param keyPrefix config key prefix
     * @param key       config key
     * @return string value
     */
    protected String getConfig(String keyPrefix, IConfigKey key) {
        if (configs == null) {
            loadConfigs();
        }
        String value = configs.getProperty(keyPrefix + key.getKeyString());
        if (value == null && key instanceof IConfigKeyHaveDefault) {
            return ((IConfigKeyHaveDefault) key).getDefaultValueStr();
        }
        return value;
    }

    /**
     * <p>Get config bool value. For true/false config.</p>
     * If not config will return false.
     *
     * @param key config key
     * @return true/false
     */
    protected boolean isConfigTrue(IConfigKey key) {
        String value = getConfig(key);
        if (value == null) {
            return false;
        }
        value = value.toLowerCase();
        return TRUE.equals(value);
    }

    /**
     * <p>Get config bool value. For true/false config. Config key with prefix.</p>
     * If not config will return false.
     *
     * @param keyPrefix config key prefix
     * @param key       config key
     * @return true/false
     */
    protected boolean isConfigTrue(String keyPrefix, IConfigKey key) {
        String value = getConfig(keyPrefix, key);
        if (value == null) {
            return false;
        }
        value = value.toLowerCase();
        return TRUE.equals(value);
    }

    /**
     * <p>Get config decimal value. For all number config.</p>
     * If not config will return null.
     *
     * @param key config key
     * @return BigDecimal object.
     * @see BigDecimal
     */
    protected BigDecimal getDecimalConfig(IConfigKey key) {
        String value = getConfig(key);
        if (value == null) {
            return null;
        }
        return new BigDecimal(value);
    }

    /**
     * <p>Get config decimal value. For all number config. Config key with prefix.</p>
     * If not config will return null.
     *
     * @param keyPrefix config key prefix
     * @param key       config key
     * @return BigDecimal object.
     * @see BigDecimal
     */
    protected BigDecimal getDecimalConfig(String keyPrefix, IConfigKey key) {
        String value = getConfig(keyPrefix, key);
        if (value == null) {
            return null;
        }
        return new BigDecimal(value);
    }

    /**
     * Modify one config and write new config into properties file.
     *
     * @param key   need update config key
     * @param value new value
     */
    protected void modifyConfig(IConfigKey key, String value) throws IOException {
        if (propertiesFilePath == null) {
            LOGGER.warn("Config " + propertiesAbsoluteClassPath + " is not a file, maybe just a resource in library.");
        }
        if (configs == null) {
            loadConfigs();
        }
        configs.setProperty(key.getKeyString(), value);
        PropertiesIO.store(propertiesFilePath, configs);
    }

    /**
     * Modify one config and write new config into properties file. Config key with prefix.
     *
     * @param keyPrefix config key prefix
     * @param key       need update config key
     * @param value     new value
     */
    protected void modifyConfig(String keyPrefix, IConfigKey key, String value) throws IOException {
        if (propertiesFilePath == null) {
            LOGGER.warn("Config " + propertiesAbsoluteClassPath + " is not a file, maybe just a resource in library.");
        }
        if (configs == null) {
            loadConfigs();
        }
        configs.setProperty(keyPrefix + key.getKeyString(), value);
        PropertiesIO.store(propertiesFilePath, configs);
    }

    /**
     * <p>Modify configs and write new configs into properties file.</p>
     * If new config value is null, will not update old value.
     *
     * @param modifyConfig need update config map.
     */
    protected void modifyConfig(Map<? extends IConfigKey, String> modifyConfig) throws IOException {
        if (propertiesFilePath == null) {
            LOGGER.warn("Config " + propertiesAbsoluteClassPath + " is not a file, maybe just a resource in library.");
        }
        if (configs == null) {
            loadConfigs();
        }
        for (IConfigKey key : modifyConfig.keySet()) {
            if (modifyConfig.get(key) != null) {
                configs.setProperty(key.getKeyString(), modifyConfig.get(key));
            }
        }
        PropertiesIO.store(propertiesFilePath, configs);
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(OneProperties.class);

}
