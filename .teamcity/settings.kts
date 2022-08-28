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

    // process each object created from the maven class in sequence/parallel
    sequential {
        buildType(cleanFiles(agentRequirements(Maven("Build","clean compile","-Dmaven.test.failure.ignore=true"))))
        parallel{
          buildType(cleanFiles(agentRequirements(Maven("Unit","clean test","-Dmaven.test.failure.ignore=true -Dtest=*.unit.*Test"))))
          buildType(cleanFiles(agentRequirements(Maven("Integration","clean test","-Dmaven.test.failure.ignore=true -Dtest=*.integration.*Test"))))
        }
        buildType(cleanFiles(agentRequirements(vcsTrigger(Maven("Package","clean package","-Dmaven.test.failure.ignore=true -DskipTests")))))
    }
}

class Maven (Name:String,Goals:String,RunnerArgs:String): BuildType({
    id(Name)
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
    if (buildType.triggers.items.find { it.type == "vcs" } ==null ) {
        buildType.triggers {
            vcs {
                branchFilter = ""
                enableQueueOptimization = false
            }
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
