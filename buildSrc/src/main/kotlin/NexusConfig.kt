import java.io.File
import java.util.*

object NexusConfig {

    private const val PROJECT_ABS_PATH = "E:/Project2/moshi"

    private val config by lazy {
        Properties()
            .apply {
                load(File("$PROJECT_ABS_PATH/nexus.properties").inputStream())
            }
    }

    val nexusUrl by lazy {
        config["NEXUS_URL"].toString()
    }
    val nexusUserName by lazy {
        config["NEXUS_USER_NAME"].toString()
    }
    val nexusPWD by lazy {
        config["NEXUS_PWD"].toString()
    }
}