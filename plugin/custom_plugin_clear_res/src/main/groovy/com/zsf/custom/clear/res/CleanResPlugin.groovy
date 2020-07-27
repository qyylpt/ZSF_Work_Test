package com.zsf.custom.clear.res

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

public class CleanResPlugin implements Plugin<Project> {

    static final String GROUP = 'LintCleaner'
    static final String EXTENSION_NAME = 'lintCleaner'

    @Override
    void apply(Project project) {

        // 获取外部参数
        project.extensions.create(EXTENSION_NAME, PluginExtension, project)

        // 创建清理任务
        Task cleanTask = project.tasks.create(CleanTask.NAME, CleanTask)

        // 执行完lint后,执行 CleanTask 任务
        cleanTask.dependsOn project.tasks.getByName('lint')
    }
}