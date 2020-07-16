package com.zsf.custom.clear.res

import org.gradle.api.Project


/**
 * 插件默认xml和排除日志输出路径
 */
public class PluginExtension {

    String lintXmlPath
    String outputPath

    public PluginExtension(Project project){

        // 默认路径 注意这里的路径对应 Android studio module中 lint Task
        lintXmlPath = "$project.buildDir/reports/lint-results.xml"
        outputPath = "$project.buildDir/reports/lintCleanerLog.txt"
    }

    @Override
    String toString() {
        return "配置项:\n\tlintXmlPath:" + lintXmlPath + "\n" +
                "outputPath:" + outputPath + "\n"
    }

}