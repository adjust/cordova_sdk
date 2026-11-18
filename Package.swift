// swift-tools-version:5.9

import PackageDescription

let package = Package(
    name: "com.adjust.sdk",
    platforms: [
        .iOS(.v13),
    ],
    products: [
        .library(
            name: "com.adjust.sdk",
            targets: ["com.adjust.sdk"]
        ),
    ],
    dependencies: [
        .package(url: "https://github.com/apache/cordova-ios.git", from: "8.0.0"),
        .package(url: "https://github.com/adjust/ios_sdk.git", exact: "5.8.2"),
    ],
    targets: [
        .target(
            name: "com.adjust.sdk",
            dependencies: [
                .product(name: "Cordova", package: "cordova-ios"),
                .product(name: "AdjustSdk", package: "ios_sdk"),
            ],
            path: "src/ios",
            publicHeadersPath: "."
        ),
    ]
)
