plugins {
    id("java")
    id("application")
}

group = "com.om1cael.ticnet"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.apache.logging.log4j:log4j-bom:2.24.3"))
    implementation("org.apache.logging.log4j:log4j-core")
    implementation("com.google.code.gson:gson:2.11.0")
}

application {
    mainClass.set("com.om1cael.ticnet.Main")
}