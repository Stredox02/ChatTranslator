dependencies {
    compileOnly(project(":nms:interfaces"))

    //Spigot
    compileOnly("org.spigotmc:spigot:1.8.8-R0.1-SNAPSHOT")

    //Databases
    implementation("com.zaxxer:HikariCP:4.0.3")

    //Compression
    implementation("org.apache.commons:commons-compress:1.21")
}
