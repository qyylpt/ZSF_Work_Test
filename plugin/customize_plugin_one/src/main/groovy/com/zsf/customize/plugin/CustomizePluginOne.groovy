package com.zsf.customize.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

public class CustomizePluginOne implements Plugin<Project> {

    def methodOneFromCustomizePluginOne(String info){
        return "自定义插件CustomizePluginOne 方法 one 返回信息 : " + info
    }

    @Override
    void apply(Project project) {
        println("\n \n ======= 自定义插件CustomizePluginOne apply 开始 ======== \n \n")
        println(methodOneFromCustomizePluginOne("方法一 来自自定义插件一"))
        println("\n \n ======= 自定义插件CustomizePluginOne apply 结束 ======== \n \n")

        project.task('CustomPluginTaskOne') {
            doLast {
                println "=============================自定义插件Task : CustomPluginTaskOne"
            }
        }
    }
}