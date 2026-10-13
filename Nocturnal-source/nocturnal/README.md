# Nocturnal

A Fabric client-side GUI starter for Minecraft 1.21.11 and Java 21.

## Controls
- **Right Shift** opens/closes the Nocturnal ClickGUI.
- **Escape** closes the GUI.
- Select categories in the sidebar; click a module row to toggle its UI state; scroll to browse longer lists.

## Build
Install JDK 21 and use a Gradle installation compatible with the Fabric Loom version in `gradle.properties`:

```sh
gradle build
```

The built mod JAR is written to `build/libs/`.

## Important scope note
The GUI, tab navigation, module rows, toggles, scrolling, and accent switch are implemented. The listed modules are demonstration entries: most do not yet alter gameplay. Verify the exact Minecraft/Fabric/Loom dependency versions and compile against the target 1.21.11 mappings before installing. This source package is not itself a compiled release JAR.
