Java获取资源（getResource的方式）的搜寻顺序是：首先是Bootstrap加载的jar包，然后是扩展加载的jar包，最后是我们自己配置的classpath。具体参考[查找Classes的官方文档](http://docs.oracle.com/javase/1.5.0/docs/tooldocs/findingclasses.html "How Classes are Found")。  
前两个步骤一般我们利用不了。如果了解关于classpath的一些知识，就能更准确的配置资源文件。

首先先总结一些基本的信息：

##### 1. 获取classpath
一般来说，可以通过System.getProperty("java.class.path")来获取classpath。  
但是有一种情况并不适用，如果程序以jar包方式运行，这样获取的classpath就只有一个，就是运行的jar包文件。但其实classpath并不只有这一个jar包，这只是一个入口jar，真正的classpath还包括该jar包的META-INF/MANIFEST.MF文件中的“Class-Path: ”项。因此该方法并不完全适用。

##### 2. classpath的父路径
获取到的classpath并不能确定是绝对路径还是相对路径，取决于配置写的是绝对还是相对路径，获取的时候并不会自动转换为绝对路径。因此需要确定如何得到父路径。

- 以非jar包运行，此时classpath的父路径是工作目录，取自System.getProperty("user.dir")。  
- 以jar包运行，该jar包配置的“Class-Path: ”的父路径是该jar包所在目录。

##### 3. 配置classpath
如同上面所说，是否是以jar包运行，classpath的情况的不同的，配置方式也有所差异。配置classpath是为了能够找到class文件（包括jar包中）及资源文件，如果配置目录并不会查找目录下的所有jar包，因此凡是在jar包中的资源需要配置jar包。 

- 以非jar包运行，那么就可以使用java的 -cp 或 -classpath 进行配置，后面跟随的是目录或jar文件，并且支持*通配符匹配多个jar文件，多个classpath以系统路径分隔符进行分隔（通过System.getProperty("path.separator")获得，windows是";",linux是":"）。  
- 以jar包运行，使用-cp/-classpath是无效的，必须在META-INF/MANIFEST.MF文件中配置“Class-Path: ”项，包括目录或jar文件，这里并不支持*通配符匹配多个jar文件，需要一一配置，多个classpath以空格分隔。

##### 4. classpath查找顺序
遍历classpath的顺序与配置的classpath的顺序完全一致。如果是以jar包运行，因为入口是jar包，在System中获取的classpath也是该jar包，因此会先在该jar包中查找，然后再按顺序遍历查找MANIFEST.MF文件中“Class-Path: ”的配置。

由以上特点总结，在配置、或查找资源时，需要注意运行方式。如果以jar包方式运行，而需要读取jar包外的资源，就需要把资源所在的路径（相对路径是相对于jar包所在目录）配置到MANIFEST.MF文件中。