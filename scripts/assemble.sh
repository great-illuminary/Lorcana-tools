#!/bin/bash

bash ./check.sh

./gradlew kotlinUpgradeYarnLock || exit 1
./gradlew :androidApp:bundleRelease :androidApp:assembleRelease || exit 1
# disable for now due to kotlin being really slow on ci for this one
# ./gradlew :jsApp:jsBrowserDistribution || exit 1

mkdir -p bundles/android
cp -r androidApp/build/outputs/bundle/release/*.aab bundles/android/
cp -r androidApp/build/outputs/apk/release/*.apk bundles/android/

# disable for now due to kotlin being really slow on ci for this one
# mkdir -p bundles/web
# cp -r jsApp/build/dist/js/productionExecutable/* bundles/web/
