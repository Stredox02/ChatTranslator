plugins {
    `java-library`
    id("io.papermc.paperweight.userdev") version "1.3.7"
}

repositories {
    mavenCentral()

    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    compileOnly(project(":common"))
    compileOnly(project(":nms:interfaces"))

    //Spigot
    paperDevBundle("1.19.2-R0.1-SNAPSHOT")

    compileOnly("io.netty:netty-all:4.1.84.Final")
    compileOnly("com.mojang:brigadier:1.0.18")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks {
    jar {
        dependsOn(reobfJar)
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name()

        options.release.set(17)
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name()
    }

    reobfJar {
      outputJar.set(layout.buildDirectory.file("libs/1.19.2.jar"))
    }

}
