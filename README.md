## 修改

吐槽: gradle kts 真的好复杂, 感觉还没有 groovy 好写, 提示倒是挺好

版本号配置: 根目录 `gradle.properties` 中的 `VERSION_NAME`

最新版本: `1.13.0.2`

注意:
1.12.0.x 的 JsonIgnore 现已调整为 Json()

## 为何会有该库

主要是旧项目迁移, 为了少改代码, 增加一些兼容性

## 已完成

[修改后特性快速入门](./快速入门.md)

- 注解`Json` 增加 单独控制序列化和反序列化的字段
- 添加对 `ArrayList,MutableList,HashSet,LinkedHashSet,MutableSet,HashMap,MutableMap` 的支持
  - 调整map 反序列化 构造对象: LinkedHashTreeMap->HashMap
- 反序列化大小写不敏感
- 增加对Android JsonObject,JsonArray 的支持
- 增加`moshiInstances` 默认实现, 添加部分便捷方法, 看 `MoshiDefault.kt`
- 增加`MJsonObject` 和 `MJsonArray` : 参考`fastjson`
- 增加对 非构造函数中的`val`变量序列化支持,注意:`val`不支持反序列化
- 数据类型 不一致 兼容, 例如 `bool` 支持读取 `int`(==1) `string`(=="1" or == "true") 数据


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
