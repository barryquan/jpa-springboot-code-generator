# akali-code-generator  
项目代号：akali  
项目代号来源：LOL的阿卡丽的神秘商店，提供一些神秘的工具用于降低无用的代码搬运量，不成为一个代码的搬运工，更专注于业务功能的实现，不再为搭建项目的基本分层而烦恼   
基于jpa的java实体进行web层、service层、reposotory层的增删改查基本功能代码生成，让人更加专注于业务功能。  
同时提供实体对应的Dto和搜索参数类的自动生成功能  
还提供了从数据库生成基本结构的功能，数据库的功能还有待完善。  
克隆[jpa-codegen](https://github.com/gcdd1993/jpa-codegen)，进行修改成适合自己项目的代码生成器  
代码生成器采用的是spring boot 2.2版本，可能跟之前的版本依赖有所不同，引用请记得修改对应的版本，已适合自己项目版本为主  
运行方式：  
根据自己的项目功能结构，修改application.yml文件对应的配置  
进入CodeGenerator.class的main方法，根据需要修改读取的配置文件和使用的方式  
如果基于已有的实体进行代码生成，请调用packInclude方法，并传入实体所在的包名  
如果是基于数据库表进行代码生成的，请配置相关的数据库信息，配置项为akali.db为前缀的。详细配置请参考：GeneratorConstants.DBConstants  
配置文件支持yml文件和properties文件。程序会根据文件后缀进行判断，无需开发者手动进行判断  