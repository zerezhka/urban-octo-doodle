# GitHub Explorer

Android app for searching GitHub users, browsing their repositories, and downloading repo archives — built with Jetpack Compose + modern Android architecture.

## Features

- Search GitHub users with live results
- Browse user repositories with description, language, and star count
- Download repo zipballs via Ketch with app-wide progress overlay (up to 3 stacked)
- Indeterminate progress for downloads with unknown Content-Length
- Open downloaded files via system file picker
- Snackbar notifications on download completion
- Offline caching via Room (users + repos)
- Compose animations: staggered list entrance, animated icon transitions, slide navigation

## Architecture

```
search/ui/         SearchScreen, SearchViewModel
githubrepos/ui/    GithubReposScreen, GithubReposViewModel
downloads/ui/      DownloadsScreen, DownloadProgressOverlay, ViewModels
main/
  ui/              MainActivity, AppNavGraph, ErrorMapping, compose/Icons
  data/            Domain models (GithubUser, GithubRepository)
  usecase/         SearchUseCase, ReposUseCase
  repository/      SearchRepository, LocalDataSource, RemoteDataSource
  db/              Room database, DAOs, DB models
  theme/           Material3 theme
network/           Retrofit API service, network models
di/                Hilt DI module
```

**Layers:** UI -> ViewModel -> UseCase -> Repository -> DataSource (Local/Remote)

**Stack:** Kotlin, Jetpack Compose, Hilt, Room, Retrofit + OkHttp, Coil 3, Ketch, Navigation Compose (type-safe), Material3, Timber

## Setup

1. Get a GitHub personal access token at https://github.com/settings/tokens

2. Create `local.properties` in the project root:
```properties
sdk.dir=/home/user/Android/Sdk
github_pat=github_pat_YOUR_TOKEN_HERE
```

Or set `GITHUB_TOKEN` as an environment variable.

See `app/build.gradle.kts` for token configuration details.

## Build

```bash
./gradlew :app:assembleDebug
```

## TODO

- [ ] Abstract Ketch behind a `DownloadRepository` interface (dependency inversion — ViewModels currently depend on concrete library)
- [ ] Extract `GithubUser` network deserialization from domain model (currently `@Serializable` domain model is used directly in Retrofit response)
- [ ] Add unit tests for ViewModels with fake repositories
- [ ] Implement pagination for search results (GitHub API supports it)
- [ ] Split `main/` catch-all package into proper `core/` modules (data, db, repository, usecase)
- [ ] Add auth abstraction — inject token provider instead of hardcoding `BuildConfig.GITHUB_TOKEN` in headers
- [ ] Add retry button on error states
- [ ] Migrate `main/ui/compose/Icons` to a shared `core/ui` module
