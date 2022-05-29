import java.io.File
import java.util.*

class MavenConfig(
  val name: String,
  val url: String,
  val urlSnapshot: String? = null,
  val userName: String? = null,
  val pwd: String? = null,
) {
  companion object {
    private val repositories = mutableListOf<MavenConfig>()

    fun getRepositories() = repositories.toList()

    private fun addRepository(name: String, propertiesFilePath: String) {
      val properties = Properties()
        .apply {
          try {
            File(propertiesFilePath).inputStream().use {
              load(it)
            }
          } catch (e: Exception) {
            println("$name::$propertiesFilePath 文件不存在 ,跳过配置")
            return
          }
        }
      repositories.add(
        MavenConfig(
          name,
          properties["MAVEN_PATH"].toString(),
          properties["MAVEN_PATH_SNAPSHOT"]?.toString(),
          properties["MAVEN_USER"]?.toString(),
          properties["MAVEN_PWD"]?.toString(),
        )
      )
    }

    init {
      addRepository("AliDev", System.getProperty("user.home") + "/.m2/alidev.properties")
    }
  }
}