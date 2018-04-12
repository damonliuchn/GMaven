# GMaven
a gradle plugin to  publish your code to github  as  maven repo

将你的Android Library Module或者Java Library Module 打包发布到github上，作为Maven仓库可以使用maven、gradle引用。
# Usage
### 1、congif in your build.gradle file
```java
buildscript {
    repositories {
        ...
        maven { url "https://github.com/MasonLiuChn/MasonMavenRepository/raw/maven/releases" }
    }
    dependencies {
        ...
        classpath 'com.masonliu:gmaven:1.0.8'
    }
}
```


```java
apply plugin: 'maven'
apply plugin: 'gmaven'
gmavenGitRepo {
    organization = 'your organization name like MasonLiuChn'
    repository = 'your repository name like MasonMavenRepository'
    branch = 'master'
    type = 'releases'
}

gmavenLibrary {
    group = 'com.masonliu'
    artifactId = 'test_plugin'
    version = '1.4.5'
    packaging = 'aar'
    description = "description"
}
```
### 2、run uploadToGit gradle task like "gradle uploadToGit"

### 3、use libray like this：
```java
repositories {
    maven { url "https://github.com/MasonLiuChn/MasonMavenRepository/raw/maven/releases" }
}

dependencies {
    compile 'net.masonliu:dagger2plus:1.0.0'
    apt 'net.masonliu:dagger2plus-compiler:1.0.0'
}
```
