object RepositoryGenerator extends App {
  slick.codegen.SourceCodeGenerator.run(
    profile = "slick.jdbc.MySQLProfile",
    jdbcDriver = "com.mysql.jdbc.Driver",
    url = "jdbc:mysql://172.30.1.23:3306/coupang",
    outputDir = "/Users/JohnJongyoonKim/Documents/projects/cpTracker/app",
    pkg = "repositories",
    user = Some("john"),
    password = Some("H3141592"),
    ignoreInvalidDefaults = true,
    outputToMultipleFiles = false
  )
}
