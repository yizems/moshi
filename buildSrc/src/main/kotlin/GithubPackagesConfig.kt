
object GithubPackagesConfig {
    val Url: String by lazy {
        System.getProperty("GITHUB_PACKAGES_URL")
    }
    val UserName: String by lazy {
        System.getProperty("GITHUB_PACKAGES_USER_NAME")
    }
    val PWD by lazy {
        System.getProperty("GITHUB_PACKAGES_TOKEN")
    }
}