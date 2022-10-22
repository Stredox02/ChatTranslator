plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "maven-publish")
    apply(plugin = "java-library")
    apply(plugin = "com.github.johnrengelman.shadow")

    group = "it.stredox02"
    version = 0.1

    repositories {
        mavenCentral()

        maven("https://repo.codemc.io/repository/nms/")

        maven("https://papermc.io/repo/repository/maven-public")
    }


    dependencies {
        compileOnly("org.projectlombok:lombok:1.18.24")
        annotationProcessor("org.projectlombok:lombok:1.18.24")

        compileOnly("org.apache.commons:commons-lang3:3.12.0")

        implementation("org.jetbrains:annotations:23.0.0")
    }

    tasks {
        javadoc {
            options.encoding = "UTF-8"
        }
        compileJava {
            options.encoding = "UTF-8"
        }
        compileTestJava {
            options.encoding = "UTF-8"
        }
    }

}
