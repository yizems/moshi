object NexusConfig {
    val nexusUrl by lazy {
        System.getProperty("NEXUS_URL")
    }
    val nexusUserName by lazy {
        System.getProperty("NEXUS_USER_NAME")
    }
    val nexusPWD by lazy {
        System.getProperty("NEXUS_PWD")
    }
}