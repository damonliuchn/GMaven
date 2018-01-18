package com.masonliu.gmaven.model

import org.gradle.api.Project

class GitRepoExtension {
    static final String EXTENSION_NAME = 'gmavenGitRepo'
    String organization
    String repository
    String branch
    String type

    String path
    Project project

    public GitRepoExtension(Project project) {
        this.project = project
        this.branch = 'maven'
        this.type = 'releases'
        this.path = "${System.env.HOME}/.gradle/caches"
    }

    public File repositoryDir() {
        return project.file("$path/$organization/$repository")
    }

    public String gitRemoteUrl() {
        return "git@github.com:$organization/${repository}.git"
    }
}
