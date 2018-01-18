package com.masonliu.gmaven.task

import com.masonliu.gmaven.model.GitRepoExtension
import com.masonliu.gmaven.model.LibraryExtension
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Upload

class UploadToGitTask extends DefaultTask {

    static final String NAME = "uploadToGit"
    GitRepoExtension mGitRepoExtension
    LibraryExtension mLibraryExtension

    static void create(Project project) {
        project.extensions.create(LibraryExtension.EXTENSION_NAME, LibraryExtension)
        project.extensions.create(GitRepoExtension.EXTENSION_NAME, GitRepoExtension, project);
        def gitRepoExtension = project.extensions.findByName(GitRepoExtension.EXTENSION_NAME) as GitRepoExtension
        def libraryExtension = project.extensions.findByName(LibraryExtension.EXTENSION_NAME) as LibraryExtension
        //创建task
        project.task(NAME, type: UploadToGitTask, dependsOn: 'build') {
            conventionMapping.mGitRepoExtension = { gitRepoExtension }
            conventionMapping.mLibraryExtension = { libraryExtension }
        }
    }

    @TaskAction
    void upload() {
        mGitRepoExtension = getmGitRepoExtension()
        mLibraryExtension = getmLibraryExtension()

        //更新本地仓库
        updateLocalRepo(project, mGitRepoExtension)
        //发布到本地仓库
        uploadLocal()
        //发布到远程仓库
        uploadGit()
    }

    private void updateLocalRepo(project, gitRepoExtension) {
        def repoDir = gitRepoExtension.repositoryDir()
        repoDir.mkdirs()
        try {
            project.exec {
                workingDir repoDir
                executable "git"
                args "init"
            }
            project.exec {
                workingDir repoDir
                executable "git"
                args "remote", "add", "origin", gitRepoExtension.gitRemoteUrl()
            }
        } catch (Exception e) {
        }
        try {
            project.exec {
                workingDir repoDir
                executable "git"
                args "pull", "--rebase", "origin", gitRepoExtension.branch
            }
        } catch (Exception e) {
        }
    }

    private void uploadLocal() {
        def uploadLocalTask = project.task('uploadLocal', type: Upload) {
            configuration = project.configurations['archives']
            //配置pom
            repositories.mavenDeployer {
                repository(url: "file:${mGitRepoExtension.repositoryDir()}/releases") {
                    pom.groupId = mLibraryExtension.group
                    pom.artifactId = mLibraryExtension.artifactId
                    pom.version = mLibraryExtension.version
                    pom.packaging = mLibraryExtension.packaging
                }
            }
        }
        uploadLocalTask.execute()
    }

    private void uploadGit() {
        def repoDir = mGitRepoExtension.repositoryDir()
        try {
            project.exec {
                workingDir repoDir
                executable "git"
                args "checkout", mGitRepoExtension.branch
            }
            project.exec {
                workingDir repoDir
                executable "git"
                args "add", "-A"
            }
            project.exec {
                workingDir repoDir
                executable "git"
                args "commit", "-m", "$mLibraryExtension.artifactId ${mLibraryExtension.version}\n$mLibraryExtension.description"
            }
            project.exec {
                workingDir repoDir
                executable "git"
                args "push", "-f", "origin", mGitRepoExtension.branch
            }
        } catch (Exception e) {
            e.printStackTrace()
        }
    }
}
