import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

dependencies {
    // Annotations
    compileOnly("org.jetbrains:annotations:23.0.0")
    // BukkitAPI
    compileOnly("org.spigotmc:spigot-api:1.13.2-R0.1-SNAPSHOT")
    compileOnly("com.destroystokyo.paper:paper-api:1.13.2-R0.1-SNAPSHOT")
    // YAML Library
    implementation("dev.dejvokep:boosted-yaml:1.3")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<ShadowJar> {
    archiveFileName.set("OpenWallet-API-${project.version}.jar")
    exclude(
            "org/intellij/lang/annotations/**",
            "org/jetbrains/annotations/**",
            "org/checkerframework/**",
            "META-INF/**",
            "javax/**"
    )
    mergeServiceFiles()
}
