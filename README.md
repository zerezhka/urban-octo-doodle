Get a GitHub personal access token `https://github.com/settings/tokens` and store it in a `local.properties` in the same directory as the script.

Or just provide $GITHUB_TOKEN as an environment variable.

```properties
sdk.dir=/home/user/Android/sdk
github.properties.token=github_pat_12ACOW....
```


see `app/build.gradle.kts` for more details, if you want to change `properties.file` to another, etc