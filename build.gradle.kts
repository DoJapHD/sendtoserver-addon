plugins {
    id("net.labymod.labygradle")
    id("net.labymod.labygradle.addon")
}

val versions = providers.gradleProperty("net.labymod.minecraft-versions").get().split(";")

group = "de.dojaphd"
version = providers.environmentVariable("VERSION").getOrElse("1.0.0")

labyMod {
    defaultPackageName = "de.dojaphd.sendserver.core"

    addonInfo {
        namespace = "sendserveraddon"
        displayName = "Send To Server"
        author = "DoJapHD"
        description = "Easy to use, command based way, to switch between server. '/ssahelp' for more information."
        minecraftVersion = "*"
        version = rootProject.version.toString()
    }

    minecraft {
        registerVersion(versions.toTypedArray()) {
            runs {
                getByName("client") {
                    // When the property is set to true, you can log in with a Minecraft account
                    devLogin = true
                }
            }
        }
    }
}

subprojects {
    plugins.apply("net.labymod.labygradle")
    plugins.apply("net.labymod.labygradle.addon")

    group = rootProject.group
    version = rootProject.version
}

