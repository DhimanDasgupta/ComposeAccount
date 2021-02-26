package com.buildsrc

object App {
    const val applicationId = "com.dhimandasgupta.composeaccount"
    const val versionCode = 1
    const val versionName = "1.0"
}

object Versions {
    const val minSdk = 21
    const val targetSdk = 30
    const val compileSdk = 30
    const val buildTool = "30.0.3"
    const val kotlin = Deps.Kotlin.version
}

object Deps {
    const val androidGradlePlugin = "com.android.tools.build:gradle:7.0.0-alpha08"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Kotlin.version}"
    const val hiltGradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:${Hilt.version}"

    object Kotlin {
        const val version = "1.4.30"
        const val stdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
        const val extensions = "org.jetbrains.kotlin:kotlin-android-extensions:$version"
        const val common = "org.jetbrains.kotlin:kotlin-stdlib-common:$version"

        // const val compilerVersion = "1.4.30"

        object Coroutine {
            private const val version = "1.4.2"

            const val coroutineCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
            const val coroutineAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
        }

        object Ktlint {
            private const val version = "0.40.0"

            const val ktlint = "com.pinterest:ktlint:$version"
        }
    }

    object Retrofit {
        private const val version = "2.9.0"

        const val retrofit = "com.squareup.retrofit2:retrofit:$version"
        const val converter = "com.squareup.retrofit2:converter-gson:$version"
    }

    object Hilt {
        const val version = "2.32-alpha"

        const val kapt = "com.google.dagger:hilt-compiler:$version"
        const val hiltAndroid = "com.google.dagger:hilt-android:$version"
    }

    object HiltAndroid {
        private const val version = "1.0.0-alpha02"

        const val viewModel = "androidx.hilt:hilt-lifecycle-viewmodel:$version"
    }

    object AndroidX {
        private const val androidXCoreVersion = "1.3.2"
        private const val materialVersion = "1.3.0"
        private const val androidAppCompatVersion = "1.2.0"
        private const val activityComposeVersion = "1.3.0-alpha02"

        const val coreKtx = "androidx.core:core-ktx:$androidXCoreVersion"
        const val appcompat = "androidx.appcompat:appcompat:$androidAppCompatVersion"
        const val material = "com.google.android.material:material:$materialVersion"
        const val activityCompose = "androidx.activity:activity-compose:$activityComposeVersion"

        object Compose {
            private const val version = "1.0.0-beta01"

            const val ui = "androidx.compose.ui:ui:$version"
            const val runtime = "androidx.compose.runtime:runtime:$version"
            const val foundation = "androidx.compose.foundation:foundation-layout:$version"
            const val material = "androidx.compose.material:material:$version"
            const val tooling = "androidx.compose.ui:ui-tooling:$version"
            const val animationCore = "androidx.compose.animation:animation-core:$version"
            const val animation = "androidx.compose.animation:animation:$version"
            const val liveDataRuntime = "androidx.compose.runtime:runtime-livedata:$version"
            const val composeViewModel = "androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0-alpha02"
        }

        object Accompanist {
            private const val version = "0.6.0"

            const val accompanistCoil = "dev.chrisbanes.accompanist:accompanist-coil:$version"
            const val accompanistMdc = "dev.chrisbanes.accompanist:accompanist-mdc-theme:$version"
        }

        object Lifecycle {
            private const val version = "2.3.0"

            const val liveDataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:$version"
            const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
        }

        object DataStore {
            private const val version = "1.0.0-alpha02"

            const val protoDataStore = "androidx.datastore:datastore-core:$version"
            const val preferenceDataStore = "androidx.datastore:datastore-preferences:$version"
        }
    }

    object ProtocolBuffer {
        private const val pluginVersion = "0.8.13"
        private const val version = "4.0.0-rc-2"

        const val protocolBufferGradlePlugin = "com.google.protobuf:protobuf-gradle-plugin:$pluginVersion"
        const val protocolBufferLite = "com.google.protobuf:protobuf-javalite:$version"
        const val protocolBuffer = "com.google.protobuf:protoc:$version"
    }

    object UCrop {
        private const val version = "2.2.6"

        const val ucrop = "com.github.yalantis:ucrop:$version"
    }
}