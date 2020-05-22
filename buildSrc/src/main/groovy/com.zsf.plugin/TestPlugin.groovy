package com.zsf.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class TestPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.logger.debug "================自定义插件成功！==========================自定义插件成功！==========================自定义插件成功！==========================自定义插件成功！==========================自定义插件成功！=========="
        project.task('testTaskOne') {
            doLast{
                println "Hello gradle plugin================自定义插件成功！==========================自定义插件成功！==========================自定义插件成功！==========================自定义插件成功！=========="
            }
        }
    }
}
