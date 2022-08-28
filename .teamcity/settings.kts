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
    buildType(vcsTrigger(cleanFiles(agentRequirements( Maven("Build","clean compile","-Dmaven.test.failure.ignore=true")))))
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

fun vcsTrigger(buildType: BuildType): BuildType{
    buildType.triggers {
        vcs {
            branchFilter = ""
            enableQueueOptimization = false
        }
    }
    return buildType
}

fun agentRequirements(buildType: BuildType): BuildType{
    buildType.requirements {
        contains("teamcity.agent.name", "linux")
        equals("aws.region", "ap-southeast-2")
    }
    return buildType
}

fun cleanFiles(buildType: BuildType): BuildType {
    buildType.features {
        swabra {
            lockingProcesses = Swabra.LockingProcessPolicy.REPORT
            verbose = true
        }
    }
    return buildType
}

fun vcsTrigger(buildType: BuildType): BuildType{
    if (buildType.triggers.items.find { it.type == "vcs" } == null ) {
        buildType.triggers {
            vcs {
                branchFilter = ""
                enableQueueOptimization = false
            }
        }
    }
    return buildType
}
