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
package yijun.sun.resource.config;


import yijun.sun.resource.ResourceUtil;

import java.io.IOException;
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
        this.propertiesFilePath = ResourceUtil.getAbsolutePath(propertiesAbsoluteClassPath);
        loadConfigs();
    }

    /**
     * Load properties. Will refresh configs every time.
     */
    protected void loadConfigs() {
        configs = PropertiesIO.load(propertiesFilePath);
    }

    /**
     * Get config string.<p/>
     * If not config will return null.
     *
     * @param key config key
     * @return string value
     */
    protected String getConfig(IConfigKey key) {
        if (configs == null) {
            loadConfigs();
        }
        return configs.getProperty(key.getKeyString());
    }

    /**
     * Get config bool value. For true/false config.<p/>
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
     * Get config decimal value. For all number config.<p/>
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
     * Modify one config and write new config into properties file.
     *
     * @param key   need update config key
     * @param value new value
     */
    protected void modifyConfig(IConfigKey key, String value) throws IOException {
        if (configs == null) {
            loadConfigs();
        }
        configs.setProperty(key.getKeyString(), value);
        PropertiesIO.store(propertiesFilePath, configs);
    }

    /**
     * Modify configs and write new configs into properties file.<p/>
     * If new config value is null, will not update old value.
     *
     * @param modifyConfig need update config map.
     */
    protected void modifyConfig(Map<IConfigKey, String> modifyConfig) throws IOException {
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

}
