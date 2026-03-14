# ComposeBook

A Gradle plugin that generates a live component catalog for Jetpack Compose apps — like Storybook, but on your device.

Annotate composables with `@Page`, connect a device or emulator, and run one Gradle task to browse your entire component library live and interactive.

---

## Requirements

- Android project with Jetpack Compose enabled
- `minSdk` 24 or higher
- KSP (Kotlin Symbol Processing) — added automatically by the plugin

---

## Setup

Apply the plugin in your app module's `build.gradle.kts`:

```kotlin
plugins {
    id("io.github.cerrativan.composebook") version "0.1.0-SNAPSHOT"
}
```

> **Note:** ComposeBook is not yet published to Maven Central. Publication is coming soon. In the meantime, refer to the repository for local setup instructions.

That is all. The plugin automatically configures KSP, adds the annotation processor, and includes the UI library. No additional dependency declarations or manifest changes are required.

---

## Usage

### Annotating composables

Add `@Page` to any no-parameter `@Composable` function:

```kotlin
import io.github.cerrativan.composebook.annotations.Page

@Page(name = "Primary Button", group = "Buttons")
@Composable
fun PrimaryButtonPreview() {
    PrimaryButton(text = "Submit", onClick = {})
}
```

- `name` — the display name shown in the catalog
- `group` — the category used for filtering and grouping

### Composables with parameters

`@Page` must be applied to a no-parameter composable. If the component you want to catalog requires parameters, create a dedicated preview wrapper:

```kotlin
// Your actual component
@Composable
fun UserCard(name: String, avatarUrl: String, onClick: () -> Unit) { ... }

// Preview wrapper — this is what gets cataloged
@Page(name = "User Card", group = "Cards")
@Composable
fun UserCardPreview() {
    UserCard(
        name = "Jane Doe",
        avatarUrl = "https://example.com/avatar.png",
        onClick = {}
    )
}
```

---

## Running the catalog

With a device or emulator connected, run:

```
./gradlew :app:composebookRun
```

This will:
1. Build and install the debug APK
2. Launch the ComposeBook catalog on the connected device

The catalog displays a searchable, filterable list of all `@Page`-annotated composables. Tapping a component opens it full screen, live and interactive.

---

## How it works

1. The Gradle plugin applies KSP and wires in the annotation processor and UI library for debug builds only.
2. At build time, KSP scans the project for `@Page` annotations and generates a `PagesRegistry` containing references to all annotated composables.
3. A `ComposebookActivity` is generated and injected into the debug APK automatically — no manifest changes needed.
4. The `composebookRun` task installs the APK and launches the catalog activity via ADB.
5. ComposeBook has zero footprint in release builds.

---

## Module structure

| Module | Description |
|---|---|
| `composebook-annotations` | `@Page` annotation definition |
| `composebook-processor` | KSP processor; generates `PagesRegistry` and `ComposebookActivity` |
| `composebook-ui` | Android library containing the catalog screens |
| `composebook-plugin` | Gradle plugin that wires all modules into the consumer project |
| `sampleapp` | Demo application showing ComposeBook in use |

---

## Roadmap

**v0.1 (current)**
- `@Page` annotation
- Searchable and filterable catalog UI
- Zero release footprint
- Single Gradle task to build and launch

**v2 (planned)**
- `@PageParameter` annotation for live parameter controls — tweak component state directly from the catalog UI without modifying code

---

## License

This project is licensed under the [Apache License 2.0](LICENSE).
