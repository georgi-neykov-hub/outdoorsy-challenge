Outdoorsy Challenge
===================

## Summary

### Architecture

* The project is based on Gradle 8.2, dependencies are handled via TOML-based version catalogs.
* The project uses a layered architecture, with separate Gradle modules per-feature, each split into
  submodules for model, data and domain layers.
* Dependency resolution is done via Koin, graph integrity is done via a
* The UI is based on AndroidX Compose 1.5.x with AndroidX-based view models as a middle-man.
* The networking layer is implemented via OkHttp, Retrofit and Kotlinx Serialization.

## Running

* To checkout the project (git is required to be installed):

```
git checkout https://github.com/georgi-neykov-hub/outdoorsy-challenge.git
```

* To build an APK with the application, execute from the directory of the project:

```
./gradlew assemble
```

* If the build is successful, APK files will be in the `./app/build/outputs/apk` subfolder of the
  project.



