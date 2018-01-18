package com.masonliu.gmaven

import com.masonliu.gmaven.task.UploadToGitTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class GMavenPlugin implements Plugin<Project> {

    void apply(Project project) {
        UploadToGitTask.create(project)
    }

}
