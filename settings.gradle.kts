pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://papermc.io/repo/repository/maven-public/")
    }
}


rootProject.name = "ChatTranslator"
include("bukkit")
include("nms")
include("nms:interfaces")
include("nms:v1_8_r3")
include("common")
include("nms:v1_19_r1")
