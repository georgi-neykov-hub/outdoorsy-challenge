plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    api(projects.rentals.data)
    api(libs.paging.common)

    testImplementation(libs.junit)
}