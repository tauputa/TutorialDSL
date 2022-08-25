import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs

version = "2021.1"

project {
    params {
        param("teamcity.ui.settings.readOnly", "true")
    }
//    buildType(Build)
    buildType(agentRequirements(Build))
}

object Build : BuildType({
    id("Build")
    name = "CleanTest"
    vcs {
        root(DslContext.settingsRoot)
    }
    steps {
        maven {
            goals = "clean test"
            runnerArgs = "-Dmaven.test.failure.ignore=true"
            mavenVersion = bundled_3_6()
        }
    }
    triggers {
        vcs {
        }
    }
})

fun agentRequirements(buildType: BuildType): BuildType{
    buildType.requirements {
        contains("teamcity.agent.name", "linux")
        equals("aws.region", "ap-southeast-2")
    }
    return buildType
}
