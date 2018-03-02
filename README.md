# GMaven
a gradle plugin to  publish your code to github  as  maven repo

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


...



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
