Gradle multi-project configuration template
===========================================

Gradle build template with two Spring Boot projects (project1 and project2) that uses shared set of modules (framework). In the framework there are three modules defined. Core module with Spring Boot dependency and two modules (module1 and module2) both dependent on core module.

Project1 uses both module1 and module2 so it's dependency tree look like this:
```
:project1
+--- :module1
|    \--- :core-module
\--- :module2
     \--- :core-module
```
Project2 uses only module1 so it's dependency tree is a bit simpler:
```
:project2
+--- :module1
     \--- :core-module
```
Build scripts are flooded with comments explaining why important directives are placed there. There are also links to the Gradle's documentation or other sources of information.

Key ideas behind this project are explained in the article [Gradle multi-project using cross dependent modules](http://vkuzel.blogspot.cz/2016/01/gradle-multi-project-using-cross.html).