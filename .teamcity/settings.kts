import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.Swabra
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.swabra
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs

version = "2021.1"

project {
    params {
        param("teamcity.ui.settings.readOnly", "true")
    }
//    buildType(Build)
//    buildType(cleanFiles(agentRequirements(Build)))
    buildType(Maven("Build","clean compile","-Dmaven.test.failure.ignore=true"))
}

class Maven (Name:String,Goals:String,RunnerArgs:String): BuildType({
    id(name)
    this.name=Name
    vcs {
        root(DslContext.settingsRoot)
    }
    steps {
        maven {
            this.goals = Goals
            this.runnerArgs = RunnerArgs
            mavenVersion = bundled_3_6()
        }
    }
})
