# resource-utils
Utils for java project to get resource/lib/dll/.. files, and adapt normal classes,runnable jar,web container and so on. Or easy to get/set configs.

Maven dependency:

    <groupId>net.sunyijun</groupId>
    <artifactId>resource-utils</artifactId>

## Basic Utils

### ResourceUtil
Functions:

- Get absolute path in file system from a classPath
- Copy resource from class path to file system

### UnicodeInputStream
An InputStream wrapper campatible for unicode file. Provide this tool because some unicode file have BOM header, but java base io read BOM as content, this tool can skip BOM.

Simple usage:

    FileInputStream fis = new FileInputStream(file);
    UnicodeInputStream unicodeInputStream = new UnicodeInputStream(fis, enc).skipBOM();
	

### UnicodeReader
A Reader wrapper campatible for unicode file. Like UnicodeInputStream.


## Library Utils

### BaseUtil
Functions:

- Get java library paths or one path. Where jvm can find libraris.

### PrepareLibs
All static methods. prepare../initial..() methods can copy libraries in class path to library path. Or load..() methods can load libraries.  
Provide these functions because sometimes library depends another library, jvm always find the depended library in library paths. So copy libraries to library path is always useful.

Simple usage to load library:

    loadDllFiles(libClassPath); // load dll library in class path
    loadLibFiles(".dll", libClassPath); // same as loadDllFiles(libClassPath)

## Config Utils
Tools for easy add configs. The simplest way to use configs:

- First, create a properties file named "config.properties" in class path "/config".<br/>
- Then define an enum implements IConfigKey, this enum contains config keys in properties file.<br/>
- After all, you can use static methods in Configs, to get or modify configs in properties file.

### IConfigKey / IConfigKeyHaveDefault
Config key interface. Normally, use IConfigKey, if you want to provide defualt value when not config, can use IConfigKeyHaveDefault.
Use enum to provide config keys like this:

    public enum SelfConfig implements IConfigKey {
        CONFIG1("str"), 
        CONFIG2("bool"),
        CONFIG3("num");
        // "str","bool","num" are keys in properties file. 
  
        private final String key;
  
        SelfConfig(String key) {
            this.key = key;
        }
  
        public String getKeyString() {
            return key;
        }
    }

### Configs
Global configs. All methods are static. Can easy to get config values in config files. Support add prefix for key string, this can simulate split configs with sections.
Default support 2 config file. One is for product function config, called system config, default class path is "/config/config.properties". Another is for debug function config, called debug config, default class path is "/config/debug.properties".

If run as a jar(java -jar ....jar), will first to find config in classpath which not a jar file. If not found in file system, then to find in jar.

Get or modify default product config:

    String configStr = Configs.getSystemConfig(SelfConfig.CONFIG1); // get str=... in /config/config.properties
    String configStr = Configs.getSystemConfig("1.", SelfConfig.CONFIG1); // get 1.str=... in /config/config.properties
    boolean configBool = Configs.isSystemConfig(SelfConfig.CONFIG2); // get bool=... in /config/config.properties
    BigDecimal configNum = Configs.getSystemConfigDecimal(SelfConfig.CONFIG3); // get num=... in /config/config.properties
    
    Configs.modifySystemConfig(SelfConfig.CONFIG1, "newValue"); // set str=newValue in /config/config.properties
    Configs.modifySystemConfig("1.", SelfConfig.CONFIG1, "newValue"); // set 1.str=newValue in /config/config.properties