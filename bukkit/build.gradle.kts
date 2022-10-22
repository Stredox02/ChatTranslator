repositories {
    maven("https://oss.sonatype.org/content/repositories/snapshots/")

    maven("https://jitpack.io")
}

dependencies {
    implementation(project(":common"))
    implementation(project(":nms:interfaces"))
    implementation(project(":nms:v1_8_r3"))
    implementation(project(":nms:v1_19_r1"))

    val v1192 = File(rootDir.absolutePath, "nms//v1_19_r1//build//libs//1.19.2.jar")
    implementation(files(v1192))

    //Spigot
    compileOnly("org.spigotmc:spigot:1.8.8-R0.1-SNAPSHOT")

    implementation("me.carleslc.Simple-YAML:Simple-Yaml:1.8")

    //Inventories
    implementation("fr.minuskube.inv:smart-invs:1.2.7")

    //GeoIP
    implementation("com.maxmind.geoip2:geoip2:3.0.1")
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    archiveFileName.set("ChatTranslator.jar")
}
