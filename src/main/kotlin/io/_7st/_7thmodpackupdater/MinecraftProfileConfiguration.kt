package io._7st._7thmodpackupdater

class MinecraftProfileConfiguration {
    class Profiles {
        class Resolution {
            var height: Int? = null
            var width: Int? = null
        }

        var name: String? = null
        var type: String? = null
        var created: String? = null
        var lastUsed: String? = null
        var icon: String? = null
        var lastVersionId: String? = null
        var gameDir: String? = null
        var javaDir: String? = null
        var javaArgs: String? = null
        var logConfig: String? = null
        var logConfigIsXML: Boolean? = null
        var resolution: Resolution? = null
    }

    class AuthDatabase {
        var accessToken: String? = null
        var username: String? = null
        var profiles: HashMap<String, String>? = null
    }

    class LauncherVersion {
        var name: String? = null
        var format: Int? = null
        var profilesFormat: Int? = null
    }

    class Settings {
       var crashAssistance = true
       var enableAdvanced = false
       var enableAnalytic = true
       var enableHistorical = false
       var enableReleases = true
       var keepLauncherOpen = false
       var profileSorting = "ByLastPlayed"
       var showGameLog = false
       var showMenu = false
       var soundOn = false
   }

    class SelectedUser {
        var account: String? = null
        var profile: String? = null
    }

    lateinit var profiles: HashMap<String, Profiles>
    var clientToken: String? = null
    var authenticationDatabase: HashMap<String, AuthDatabase>? = null
    var launcherVersion: LauncherVersion? = null
    lateinit var settings: Settings
    var analyticsToken: String? = null
    var analyticsFailcount: String? = null
    var selectedUser: SelectedUser? = null
    var version: Int? = null
}