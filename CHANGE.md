### 0.0.6 ###
Publish: 2015-06-30

* Fixed
    1. If not Configs.addSelfConfigs, Configs.getSelfConfig(configAbsoluteClassPath, key) can't get value.


### 0.0.5 ###
Publish: 2015-06-05

* Functions
    1. Add utils to test if current project start main in a jar, use java -jar ...
    2. Add utils to get class paths, may include class path in manifest file, or may get class path exclude jar ...
    3. Support to find config in classpath which is file system path first, if running as a jar(java -jar).


### 0.0.4 ###
Publish: 2015-05-26

* Functions
    1. Modify Configs usage, support auto add self config when get self config.
    2. If not found configure file, try get as resource.


### 0.0.3 ###
Publish: 2015-05-12

* Fixed
    1. ResourceUtil.getAbsolutePath() can't find resource, return null.
    2. Get self config with prefix not add prefix.


### 0.0.2 ###
Publish: 2015-05-12

* Functions
    1. Add config key prefix support.

* Fixed
    1. Want to modify self configs but modified system configs.