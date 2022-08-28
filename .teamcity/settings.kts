
project {
    params {
        param("teamcity.ui.settings.readOnly", "true")
    }
//    buildType(Build)
    buildType(cleanFiles(agentRequirements(Build)))
//    buildType(cleanFiles(agentRequirements( Maven("Build","clean compile","-Dmaven.test.failure.ignore=true"))))
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
