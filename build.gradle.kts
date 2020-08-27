group = "io.terraformkt"
version = "0.1.0"

plugins {
    id("tanvd.kosogor") version "1.0.9" apply true
    kotlin("jvm") version "1.3.72" apply true
}
repositories {
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    implementation("com.squareup", "kotlinpoet", "1.6.0")
    implementation("com.squareup.moshi", "moshi", "1.8.0")
    implementation("com.squareup.moshi", "moshi-kotlin", "1.8.0")
}

sourceSets.main {
    java.srcDirs("generated")
}
