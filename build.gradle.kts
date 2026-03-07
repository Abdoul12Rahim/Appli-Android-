plugins {
    id("com.android.application") version "8.9.1" apply false // Mets la version de ton AGP ici (souvent 8.2.0 ou 8.3.0)
    id("org.jetbrains.kotlin.android") version "2.0.21" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21" apply false
    id("com.google.devtools.ksp") version "2.0.21-1.0.28" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.21" apply false
}