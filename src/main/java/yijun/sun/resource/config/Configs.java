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


import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Global configs. All methods are static.<p/>
 * Default support 2 config file.<br/>
 * One is for product function config, called system config,
 * default class path is {@value #DEFAULT_SYSTEM_CONFIG_ABSOLUTE_CLASS_PATH}.<br/>
 * Another is for debug function config, called debug config,
 * default class path is {@value #DEFAULT_DEBUG_CONFIG_ABSOLUTE_CLASS_PATH}.<br/>
 * If want to use other path, before get config,
 * call {@link #setSystemConfigs(String, OneProperties)} or {@link #setDebugConfigs(OneProperties, String)}.<p/>
 * <p/>
 * If want to use some other config files, can {@link #addSelfConfigs(String, OneProperties)},
 * first param "configAbsoluteClassPath" is class path and also is an identity for config file.
 * When get self config, must provide configAbsoluteClassPath.
 *
 * @author yijun.sun
 * @since 0.0.1
 */
public class Configs {

    /**
     * Default system config file absolute class path: {@value}
     */
    public static final String DEFAULT_SYSTEM_CONFIG_ABSOLUTE_CLASS_PATH = "/config/config.properties";
    /**
     * Default debug config file absolute class path: {@value}
     */
    public static final String DEFAULT_DEBUG_CONFIG_ABSOLUTE_CLASS_PATH = "/config/debug.properties";

    private static String systemConfigAbsoluteClassPath = DEFAULT_SYSTEM_CONFIG_ABSOLUTE_CLASS_PATH;
    private static String debugConfigAbsoluteClassPath = DEFAULT_DEBUG_CONFIG_ABSOLUTE_CLASS_PATH;

    private static OneProperties systemConfigs = new OneProperties();
    private static OneProperties debugConfigs = new OneProperties();
    private static Map<String, OneProperties> otherConfigs = new ConcurrentHashMap<String, OneProperties>();

    static {
        systemConfigs.initConfigs(systemConfigAbsoluteClassPath);
        debugConfigs.initConfigs(debugConfigAbsoluteClassPath);
    }

    /**
     * Get system config string.
     *
     * @param key config key
     * @return config value string. Return null if not config in
     * system config file "{@value #systemConfigAbsoluteClassPath}" or self define system config path.
     * @see #setSystemConfigs(String, OneProperties)
     */
    public static String getSystemConfig(IConfigKey key) {
        return systemConfigs.getConfig(key);
    }

    /**
     * Get debug config boolean value
     *
     * @param key config key
     * @return true/false. If not config in system config file
     * "{@value #systemConfigAbsoluteClassPath}" or self define system config path,
     * return false.
     * @see #setSystemConfigs(String, OneProperties)
     */
    public static boolean isSystemConfig(IConfigKey key) {
        return systemConfigs.isConfigTrue(key);
    }

    /**
     * Get system config decimal.
     *
     * @param key config key
     * @return config BigDecimal value. Return null if not config in
     * system config file "{@value #systemConfigAbsoluteClassPath}" or self define system config path.
     * @see #setSystemConfigs(String, OneProperties)
     */
    public static BigDecimal getSystemConfigDecimal(IConfigKey key) {
        return systemConfigs.getDecimalConfig(key);
    }

    /**
     * Get debug config string.
     *
     * @param key config key
     * @return config value string. Return null if not config in
     * debug config file "{@value #debugConfigAbsoluteClassPath}" or self define debug config path.
     * @see #setDebugConfigs(OneProperties, String)
     */
    public static String getDebugConfig(IConfigKey key) {
        return debugConfigs.getConfig(key);
    }

    /**
     * Get debug config boolean value
     *
     * @param key config key
     * @return true/false. If not config in debug config file
     * "{@value #debugConfigAbsoluteClassPath}" or self define debug config path,
     * return false.
     * @see #setDebugConfigs(OneProperties, String)
     */
    public static boolean isDebugConfig(IConfigKey key) {
        return debugConfigs.isConfigTrue(key);
    }

    /**
     * Get debug config decimal.
     *
     * @param key config key
     * @return config BigDecimal value. Return null if not config in
     * debug config file "{@value #debugConfigAbsoluteClassPath}" or self debug system config path.
     * @see #setDebugConfigs(OneProperties, String)
     */
    public static BigDecimal getDebugConfigDecimal(IConfigKey key) {
        return debugConfigs.getDecimalConfig(key);
    }

    /**
     * Get self config string.
     *
     * @param configAbsoluteClassPath config path. {@link #addSelfConfigs(String, OneProperties)}
     * @param key                     config key in configAbsoluteClassPath config file
     * @return config value string. Return null if not add config file or not config in config file.
     * @see #addSelfConfigs(String, OneProperties)
     */
    public static String getSelfConfig(String configAbsoluteClassPath, IConfigKey key) {
        OneProperties configs = otherConfigs.get(configAbsoluteClassPath);
        if (configs == null) {
            return null;
        }
        return configs.getConfig(key);
    }

    /**
     * Get self config boolean value
     *
     * @param configAbsoluteClassPath config path. {@link #addSelfConfigs(String, OneProperties)}
     * @param key                     config key in configAbsoluteClassPath config file
     * @return true/false. If not add config file or not config in config file, return false.
     * @see #addSelfConfigs(String, OneProperties)
     */
    public static boolean isSelfConfig(String configAbsoluteClassPath, IConfigKey key) {
        OneProperties configs = otherConfigs.get(configAbsoluteClassPath);
        return configs != null && configs.isConfigTrue(key);
    }

    /**
     * Get self config decimal.
     *
     * @param configAbsoluteClassPath config path. {@link #addSelfConfigs(String, OneProperties)}
     * @param key                     config key in configAbsoluteClassPath config file
     * @return config BigDecimal value. Return null if not add config file or not config in config file.
     * @see #addSelfConfigs(String, OneProperties)
     */
    public static BigDecimal getSelfConfigDecimal(String configAbsoluteClassPath, IConfigKey key) {
        OneProperties configs = otherConfigs.get(configAbsoluteClassPath);
        if (configs == null) {
            return null;
        }
        return configs.getDecimalConfig(key);
    }

    /**
     * Modify system configs.
     *
     * @param modifyConfig need update configs. If one value is null, will not update that one.
     */
    public static void modifySystemConfig(Map<IConfigKey, String> modifyConfig) throws IOException {
        systemConfigs.modifyConfig(modifyConfig);
    }

    /**
     * Modify one system config.
     *
     * @param key   need update config's key
     * @param value new config value
     */
    public static void modifySystemConfig(IConfigKey key, String value) throws IOException {
        systemConfigs.modifyConfig(key, value);
    }

    /**
     * Modify debug configs.
     *
     * @param modifyConfig need update configs. If one value is null, will not update that one.
     */
    public static void modifyDebugConfig(Map<IConfigKey, String> modifyConfig) throws IOException {
        debugConfigs.modifyConfig(modifyConfig);
    }

    /**
     * Modify self configs.
     *
     * @param configAbsoluteClassPath config path. {@link #addSelfConfigs(String, OneProperties)}
     * @param modifyConfig            need update configs. If one value is null, will not update that one.
     */
    public static void modifySelfConfig(String configAbsoluteClassPath, Map<IConfigKey, String> modifyConfig)
            throws IOException {
        OneProperties configs = otherConfigs.get(configAbsoluteClassPath);
        if (configs == null) {
            return;
        }
        configs.modifyConfig(modifyConfig);
    }

    /**
     * Modify one self config.
     *
     * @param configAbsoluteClassPath config path. {@link #addSelfConfigs(String, OneProperties)}
     * @param key                     need update config's key
     * @param value                   new config value
     */
    public static void modifySelfConfig(String configAbsoluteClassPath, IConfigKey key, String value)
            throws IOException {
        systemConfigs.modifyConfig(key, value);
    }

    /**
     * Add self define configs file.<p/>
     * Can use self configs path or self class extends {@link OneProperties}.
     *
     * @param configAbsoluteClassPath self configs absolute class path.
     *                                This path also is a config file key string.
     *                                Can't be null, if null add nothing.
     * @param configsObj              self class extends {@link OneProperties}.
     *                                Can be null, if null means not use self class.
     * @see OneProperties
     */
    public static void addSelfConfigs(String configAbsoluteClassPath, OneProperties configsObj) {
        if (configAbsoluteClassPath == null) {
            return;
        }
        if (configsObj == null) {
            OneProperties configs = otherConfigs.get(configAbsoluteClassPath);
            if (configs == null) {
                configsObj = new OneProperties();
            } else {
                configsObj = configs;
            }
        }
        configsObj.initConfigs(configAbsoluteClassPath);
        otherConfigs.put(configAbsoluteClassPath, configsObj);
    }

    /**
     * Set self define system configs.<p/>
     * Can use self system configs path or self class extends {@link OneProperties}.
     *
     * @param systemConfigAbsoluteClassPath self system configs absolute class path.
     *                                      Can be null, if null means use
     *                                      default path "{@value #systemConfigAbsoluteClassPath}"
     * @param systemConfigsObj              self class extends {@link OneProperties}.
     *                                      Can be null, if null means not use self class.
     * @see OneProperties
     */
    public static void setSystemConfigs(String systemConfigAbsoluteClassPath, OneProperties systemConfigsObj) {
        if (systemConfigsObj != null) {
            Configs.systemConfigs = systemConfigsObj;
        }
        if (systemConfigAbsoluteClassPath != null) {
            Configs.systemConfigAbsoluteClassPath = systemConfigAbsoluteClassPath;
            Configs.systemConfigs.initConfigs(Configs.systemConfigAbsoluteClassPath);
        } else if (systemConfigsObj != null) { // use new systemConfigs, need initConfigs.
            Configs.systemConfigs.initConfigs(Configs.systemConfigAbsoluteClassPath);
        }
    }

    /**
     * Set self define debug configs.<p/>
     * Can use self debug configs path or self class extends {@link OneProperties}.
     *
     * @param debugConfigsObj              self class extends {@link OneProperties}.
     *                                     If null means not use self class.
     * @param debugConfigAbsoluteClassPath self system configs path.
     *                                     If null means use default path "{@value #debugConfigAbsoluteClassPath}"
     * @see OneProperties
     */
    public static void setDebugConfigs(OneProperties debugConfigsObj, String debugConfigAbsoluteClassPath) {
        if (debugConfigsObj != null) {
            Configs.debugConfigs = debugConfigsObj;
        }
        if (debugConfigAbsoluteClassPath != null) {
            Configs.debugConfigAbsoluteClassPath = debugConfigAbsoluteClassPath;
            Configs.debugConfigs.initConfigs(Configs.debugConfigAbsoluteClassPath);
        } else if (debugConfigs != null) { // use new systemConfigs, need initConfigs.
            Configs.debugConfigs.initConfigs(Configs.debugConfigAbsoluteClassPath);
        }
    }

}
