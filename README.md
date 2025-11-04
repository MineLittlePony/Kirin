# Kirin UI

 [![Current Version](https://img.shields.io/github/v/release/MineLittlePony/Kirin)](https://github.com/MineLittlePony/Kirin/releases/latest)
[![Build Status](https://github.com/MineLittlePony/Kirin/actions/workflows/gradle-build.yml/badge.svg)](https://github.com/MineLittlePony/Kirin/actions/workflows/gradle-build.yml)
![License](https://img.shields.io/github/license/MineLittlePony/Kirin)
![](https://img.shields.io/badge/api-fabric-orange.svg)


Embedded common code used across several Mine Little Pony projects to create their GUIs.

Embedded common code used across several Mine Little Pony projects to create their GUIs.

## If you're a player

This mod is a _library_ and is usually embedded or installed as a dependency for other mods, so installing it on its own doesn't do anything.

It will occasionally get updates independent of other projects that depend on it, so whilst installing it separately is not recommended it is permitted if you're having issues that updating solves but the downstream project hasn't (or can't) be updated for whatever reason.

## If you're a mod developer

Kirin provides support for creating GUIs, and hooking into existing GUIs to add a variety of different controls: Buttons, Toggles, Cyclers, and scrolling containers that makes them both simpler and easier to configure, with more customisation options than the regular game's classes. It also provides an advanced configuration system for loading a config file in json format with support for live updates whilst the game is running.

For more on how to use it, I recommend checking out the projects source on [github](https://github.com/MineLittlePony/Kirin).

https://minelittlepony-mod.com

## Building

1. JDK 8 is required. Install it using https://adoptopenjdk.net/

2. Open a terminal window in the same directory as the sources (git clone or extracted from zip). Run the following command (windows).

```
gradlew build
```

3. After some time, the built mod will be in `/build/libs`.

## Installation (Modders):

Maven: `https://repo.minelittlepony-mod.com/maven/snapshot`

Dependency: `com.minelittlepony:Kirin:1.6.4-1.16-rc1`
