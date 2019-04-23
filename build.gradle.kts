import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


//1.
plugins {
    kotlin("jvm") version "1.2.71"
    //3.
    application
}
//4.
application{
    mainClassName="com.atoken.game.AppKt"
}

dependencies {
    compile(kotlin("stdlib-jdk8"))
    compile ("com.github.shaunxiao:kotlinGameEngine:v0.0.4")
}
repositories {
    mavenCentral()
    //2.
    maven { setUrl("https://jitpack.io") }
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}