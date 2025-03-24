# lorcana-tools

![CI](https://github.com/great-illuminary/lorcana-tools/actions/workflows/build.yml/badge.svg)
![License](https://img.shields.io/github/license/great-illuminary/lorcana-tools)
![Last Release](https://img.shields.io/github/v/release/great-illuminary/lorcana-tools)

[
![Discord](https://img.shields.io/badge/Discord-Lorcana_Manager-blue)
](https://discord.gg/cd4hRF2PXm)

![badge](https://img.shields.io/badge/json-kotlin-green)
![badge](https://img.shields.io/badge/android-blue)
![badge](https://img.shields.io/badge/ios-white)
![badge](https://img.shields.io/badge/js-yellow)
![badge](https://img.shields.io/badge/jvm-red)
![badge](https://img.shields.io/badge/linux-blue)
![badge](https://img.shields.io/badge/windows-blueviolet)
![badge](https://img.shields.io/badge/mac-orange)

Set of Lorcana Tools

- deck probability checker

## Roadmap

Adding scenarios for the decks retrieved via dreamborn to help trigger more visibilities

## iOS Fastlane

### Installation

With the regular machine :
```
bundle exec fastlane match appstore
bundle exec fastlane deliver download_screenshots
bundle exec fastlane deliver download_metadata
```

With the ci environment :
```
bundle exec fastlane match appstore --readonly
```

### Compilation

```
bundle exec fastlane release
```