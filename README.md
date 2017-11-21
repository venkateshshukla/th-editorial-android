## The Hindu Editorials (Android Client)

An android application that gathers the editorials of The Hindu (leading Indian
english daily) and serves to the user.

#### Steps

1. Install Android Studio
2. Import project.
3. Go to https://console.firebase.google.com and download `google-services.json` and keep it in `app/` folder.
4. Create a `keystore.properties` file in the project root folder. The required keys with dummy values are mentioned below. While signing, update these values.

#### keystore.properties

    keyAlias=alias_name
    keyPassword=password
    storeFile=path/to/keystore/file
    storePassword=password
