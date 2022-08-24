import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.Swabra

version = "2021.1"

project {
//    params {
//        param("teamcity.ui.settings.readOnly", "true")
//    }
    buildType(Build)
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
    features {
	Swabra {                                  // Cleans up files left by the previous build
        }  
    }
    requirements {
        contains("teamcity.agent.name", "linux")  // only use agents with linux in theyre name 
	equals("aws.region", "ap-southeast-2")    // only use agents in ap-southeast-2
    }
})

