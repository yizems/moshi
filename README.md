## 修改

吐槽: gradle kts 真的好复杂, 感觉还没有 groovy 好写, 提示倒是挺好

版本号配置: 根目录 `gradle.properties` 中的 `VERSION_NAME`

最新版本: `1.12.0.40`

## 为何会有该库

主要是旧项目迁移, 为了少改代码, 增加一些兼容性

## 已完成

[修改后特性快速入门](./快速入门.md)

- 增加专用注解`JsonIgnore`,控制是否支持序列化和反序列化
- 添加对 `ArrayList,MutableList,HashSet,LinkedHashSet,MutableSet,,HashMap,LinkedHashMap,MutableMap` 的支持
  - 调整map 反序列化 构造对象: LinkedHashTreeMap->LinkedHashMap
- 反序列化大小写不敏感
- 增加对Android JsonObject,JsonArray 的支持
- 增加`moshiInstances` 默认实现, 添加部分便捷方法, 看 `MoshiDefault.kt`
- 增加`MJsonObject` 和 `MJsonArray` : 参考`fastjson`
- 增加对 非构造函数中的`val`变量序列化支持,注意:`val`不支持反序列化
- 数据类型 不一致 兼容, 例如 `bool` 支持读取 `int`(==1) `string`(=="1" or == "true") 数据
- moshi 如果没有指定数据类型, 会将 int 读为 double, 现在默认为 long, 除非有小数位
- moshi 序列化时, double/float 如果没有小数位,则按照long的形式进行序列化

## 其他

- kotlin:test 被玩坏了,我也不知道为啥坏了=.= 但是项目功能正常
- adapter 模块无修改,请使用官方的
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


### 2 使用

```gradle

// 使用插件
apply plugin: 'kotlin-kapt'

//依赖
def moshi_version = "1.12.0.23"

implementation "com.squareup.moshi:moshi:${moshi_version}"
implementation "com.squareup.moshi:moshi-android-ext:${moshi_version}"
implementation "com.squareup.moshi:moshi-kotlin:${moshi_version}"
implementation "com.squareup.moshi:jobj:${moshi_version}"
kapt "com.squareup.moshi:moshi-kotlin-codegen:${moshi_version}"

// 还需要使用 kotlin 的反射库
implementation "com.jetbrains.kotlin:kotlin-reflect:${kotlin_version}"

```
