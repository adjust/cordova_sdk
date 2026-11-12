// swift-tools-version:5.9

import PackageDescription

let package = Package(
    name: "com.adjust.sdk.odm",
    platforms: [
        .iOS(.v13),
    ],
    products: [
        .library(
            name: "com.adjust.sdk.odm",
            targets: ["com.adjust.sdk.odm"]
        ),
    ],
    dependencies: [
        .package(url: "https://github.com/adjust/ios_sdk.git", exact: "5.8.1"),
    ],
    targets: [
        .target(
            name: "com.adjust.sdk.odm",
            dependencies: [
                .product(name: "AdjustGoogleOdm", package: "ios_sdk"),
            ],
            path: "src/ios",
            publicHeadersPath: "."
        ),
    ]
)
