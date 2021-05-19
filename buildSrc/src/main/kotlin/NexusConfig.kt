import java.io.File
import java.lang.IllegalArgumentException
import java.util.*

object NexusConfig {

    private const val PROJECT_ABS_PATH = "E:/Project2/moshi"

    private val config by lazy {
        Properties()
            .apply {
                try {
                    load(File("$PROJECT_ABS_PATH/nexus.properties").inputStream())
                } catch (e: Exception) {
                    IllegalArgumentException("$PROJECT_ABS_PATH 目录下未找到 nexus.properties")
                        .printStackTrace()
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