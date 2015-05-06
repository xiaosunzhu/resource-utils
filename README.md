# resource-utils
Utils for java project to get resource/lib/dll/.. files, and adapt normal classes,runnable jar,web container and so on.


## Base Utils

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