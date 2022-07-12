## 修改

**重要说明-本分支**
由于原库需要兼容高版本的JDK, 导致安卓上使用异常, 本库只支持 JDK 8+, 移除了jdk 16,17,18 相关支持的代码,并简化了编译脚本, 如果是后台需要用,


版本号配置: 根目录 `gradle.properties` 中的 `VERSION_NAME`

最新版本: `1.13.0.14`

注意:
1.12.0.x 的 JsonIgnore 现已调整为 Json()

## 为何会有该库

主要是旧项目迁移, 为了少改代码, 增加一些兼容性

## 特性

### 1 `JsonIgnore`

```kotlin
data class Demo(
  @Json(serialize = true,deserialize = true)
  var name:String
)
```


### 2 val 成员变量 默认支持序列化(不支持反序列化)

如果我们的代码这样写:
```kotlin
data class Demo(
  var name:String
){
  val age:Int
  val age2:Int
      get() {
        return 10
      }

}
```

`age`和`age2` 字段是不支持序列化到json字符串的; 现已支持,无需配置;

如果不需要序列化,加上 `Json(ignore=true)` 注解即可;




### 3 关于遇到获取不到adapter 报错的说明

原库的设计理念是 标准DTO和常用数据格式, 但是我们总是会遇到各种使用到的, 不常见的数据类型;

比如 `DataLocal`, 如果一旦遇到,我们需要使用 原库 提供的 自定义adapter的方式 来完成 序列化和反序列化;

所以原库的扩展性特别强, 也要提醒大家, 不是所有对象都支持;

目前支持的数据类型有:

- 基本数据类型和String
- 自定义的实体类
- 集合: Map,List, 本库添加的几个集合支持
- 本库: 安卓自带的JsonObject 和 JsonArray
- 本库: MJsonObject MJsonArray


更多特性,请看[MoshiEx](https://github.com/yizems/MoshiEx)

## 其他

- retrofit 使用时注意要在依赖中排除掉 官方的moshi依赖

## 使用方式

### 1 发布到nexus

根目录创建`nexus.properties` 文件

```properties
URL=
USER_NAME=
PWD=
```

然后修改`NexusConfig.PROJECT_ABS_PATH` 为你本地的项目绝对路径,ex:`E:/Project2/moshi`

然后依次执行:
- `moshi:publishMavenPublicationToNexusRepository`
- `kotlin:reflect:publishMavenPublicationToNexusRepository`
- `kotlin:codegen:publishMavenPublicationToNexusRepository`
- `android:publishMavenPublicationToNexusRepository`
- `jsonobj:publishMavenPublicationToNexusRepository`

一键执行命令:

`gradlew moshi:publishMavenPublicationToNexusRepository kotlin:reflect:publishMavenPublicationToNexusRepository kotlin:codegen:publishMavenPublicationToNexusRepository android:publishMavenPublicationToNexusRepository jsonobj:publishMavenPublicationToNexusRepository`


### 2 发布到GithubPackages

配置类似,看 `GithubPackagesConfig`

`github.properties`

```properties
URL=
USER_NAME=
PWD=
```

**github packages 上传地址为:  https://maven.pkg.github.com/用户名/仓库名**

一键执行命令:

`gradlew moshi:publishMavenPublicationToGithubPackagesRepository kotlin:reflect:publishMavenPublicationToGithubPackagesRepository kotlin:codegen:publishMavenPublicationToGithubPackagesRepository android:publishMavenPublicationToGithubPackagesRepository jsonobj:publishMavenPublicationToGithubPackagesRepository`

### 3 本仓库 packages 使用方式

```
//添加仓库
repositories {
   maven {
        url 'https://maven.pkg.github.com/yizems/moshi'
        credentials {
            username 你的github用户名
            password 你的github access token (https://github.com/settings/tokens)
        }
    }
}
```

添加依赖:

https://github.com/yizems?tab=packages&repo_name=moshi
