package com.crashlytics.examples.gradle

import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.api.tasks.Exec

/**
 * See http://tools.android.com/tech-docs/new-build-system/user-guide.
 */

public class PreprocessorPlugin implements Plugin<Project> {
  String sootPath = "/Volumes/Android4.4.3/androidtestingproject/Instrumentation/SootAndroidInstrumentation"

  void apply(Project project) {
    project.configure(project) {
      if (it.hasProperty("android")) {
        project.android.testVariants.all { variant ->
          logger.warn(variant.packageApplication.outputFile.toString())
          logger.warn(variant.packageApplication.toString())
          
          variant.packageApplication << {
            println "Working on " + variant.packageApplication.outputFile
          }

          def mytask = project.tasks.create("runtask${variant.baseName}", Exec.class)

          mytask.configure {
            dependsOn variant.packageApplication
            doLast {
              println "Done instrumenting the tests"
            }
            commandLine "${sootPath}"
            args = ["${variant.packageApplication.outputFile}"]
          }

          variant.dex.dependsOn mytask
        }
      } 
    }
  }
}
