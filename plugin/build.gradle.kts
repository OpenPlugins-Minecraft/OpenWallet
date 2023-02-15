import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("net.minecrell.plugin-yml.bukkit") version "0.5.2"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

dependencies {
    // Project
    implementation(project(":api"))
    // Annotations
    compileOnly("org.jetbrains:annotations:23.0.0")
    // BukkitAPI
    compileOnly("org.spigotmc:spigot-api:1.13.2-R0.1-SNAPSHOT")
    compileOnly("com.destroystokyo.paper:paper-api:1.13.2-R0.1-SNAPSHOT")
    // YAML Library
    implementation("dev.dejvokep:boosted-yaml:1.3")
    // Lombok
    compileOnly("org.projectlombok:lombok:1.18.26")
    annotationProcessor("org.projectlombok:lombok:1.18.26")
    testCompileOnly("org.projectlombok:lombok:1.18.26")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.26")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<ShadowJar> {
    archiveFileName.set("OpenWallet-${project.version}.jar")
    exclude(
            "org/intellij/lang/annotations/**",
            "org/jetbrains/annotations/**",
            "org/checkerframework/**",
            "META-INF/**",
            "javax/**"
    )
    mergeServiceFiles()
}

bukkit {
    name = "OpenWallet"
    main = "xyz.neziw.wallet.WalletPlugin"
    version = "${project.version}"
    apiVersion = "1.13"
    authors = listOf("IndianPL", "Techno Neziw")
}
