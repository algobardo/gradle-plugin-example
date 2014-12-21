package com.crashlytics.examples.gradle

import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.api.tasks.Exec

/**
 * See http://tools.android.com/tech-docs/new-build-system/user-guide.
 */

public class PreprocessorPlugin implements Plugin<Project> {
  String sootPath = "/Volumes/Android4.4.3/androidtestingproject/Instrumentation/SootAndroidInstrumentation/build/install/SootAndroidInstrumentation/bin/SootAndroidInstrumentation"
  String workDir = "/Volumes/Android4.4.3/androidtestingproject/Instrumentation/SootAndroidInstrumentation/"
  
  void apply(Project project) {
    project.configure(project) {
      if (it.hasProperty("android")) {
        logger.warn("Test variants...")
        project.android.testVariants.all { variant ->
          logger.warn("outputFile:" + variant.packageApplication.outputFile.toString())
          logger.warn("packageApplication:" + variant.packageApplication.toString())

          def mytask = project.tasks.create("runtask${variant.baseName}", Exec.class)

          mytask.configure {
            dependsOn variant.packageApplication
            doLast {
              println "Done instrumenting the tests"
            }
            workingDir "${workDir}"
            commandLine "${sootPath}"
            args = ["${variant.packageApplication.outputFile}"]
          }

          variant.packageApplication.doLast {
            println "Working on " + variant.packageApplication.outputFile
            mytask.execute()
          }
        }

        logger.warn("Application variants...");
        /*  DEACTIVATE TO DISABLE PROBLEMS 
        project.android.applicationVariants.all { variant ->
          logger.warn("outputFile:" + variant.packageApplication.outputFile.toString())
          logger.warn("packageApplication:" + variant.packageApplication.toString())
          
          if(!variant.packageApplication.toString().contains("Release")) {
            variant.packageApplication << {
              println "Working on " + variant.packageApplication.outputFile
            }

            def mytask = project.tasks.create("runtask${variant.baseName}", Exec.class)

            mytask.configure {
              dependsOn variant.packageApplication
              doLast {
                println "Done instrumenting the application"
              }
              workingDir "${workDir}"
              commandLine "${sootPath}"
              args = ["${variant.packageApplication.outputFile}"]
            }

            variant.install.dependsOn mytask // maybe zipAlign for release
          }
        }
         UP TO HERE */ 
      }
    }
  }
}
