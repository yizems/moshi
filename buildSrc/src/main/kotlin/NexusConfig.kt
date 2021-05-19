import java.io.File
import java.util.*

object NexusConfig {

    private const val PROJECT_ABS_PATH = "E:/Project2/moshi"

    private val config by lazy {
        Properties()
            .apply {
                try {
                    load(File("$PROJECT_ABS_PATH/nexus.properties").inputStream())
                } catch (e: Exception) {
                    println("$PROJECT_ABS_PATH 目录下未找到 nexus.properties ,无法上传 nexus packages")
                }
            }
    }

    val url by lazy {
        config["URL"]?.toString()
    }
    val userName by lazy {
        config["USER_NAME"]?.toString()
    }
    val pwd by lazy {
        config["PWD"]?.toString()
    }
}