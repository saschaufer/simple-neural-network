pipeline {

    agent any

    tools {
        maven 'maven'
        jdk 'temurin-21-jdk-amd64'
    }

    options {
        skipDefaultCheckout(true)
        timeout(time: 30, unit: 'MINUTES')
    }

    stages {

        stage('Checkout SCM') {

            steps {
                checkout scm
                script {
                    env.GIT_BRANCH = sh (
                      script: "git branch --show-current",
                      returnStdout: true
                    ).trim()
                    env.GIT_COMMIT_MESSAGE = sh (
                      script: "git show -s --format=%s",
                      returnStdout: true
                    ).trim()
                }
                echo "Checked out branch \"${env.GIT_BRANCH}\" on commit message \"${env.GIT_COMMIT_MESSAGE}\""
            }
        }

        stage('Test') {

            steps {
                catchError(buildResult: 'FAILURE', stageResult: 'FAILURE') {
                    sh '''
                       mvn -B -U clean verify
                    '''
                }
            }
        }

        stage('Build & Deploy Snapshot (optional)') {

            when {
                allOf {
                    expression { currentBuild.currentResult == 'SUCCESS' }
                    expression { env.GIT_BRANCH ==~ '^develop(-\\d+\\.\\d+\\.\\d+)?$' }
                }
            }

            steps {
                catchError(buildResult: 'FAILURE', stageResult: 'FAILURE') {
                    sh '''
                       mvn -B clean deploy \
                       -DskipTests \
                       -Dmaven.install.skip=true
                    '''
                }
            }
        }

        stage('Build & Deploy Release (optional)') {

            when {
                allOf {
                    expression { currentBuild.currentResult == 'SUCCESS' }
                    expression { env.GIT_BRANCH ==~ '^main(-\\d+\\.\\d+\\.\\d+)?$' }
                    expression { env.GIT_COMMIT_MESSAGE ==~ '^Merge pull request(.*)' }
                }
            }

            steps {
                catchError(buildResult: 'FAILURE', stageResult: 'FAILURE') {
                    sh '''
                       mvn -B clean \
                       release:prepare release:perform \
                       -DdryRun=true \
                       -Darguments="-DskipTests"
                    '''
                    sh '''
                       mvn -B clean \
                       release:clean release:prepare \
                       -DpreparationGoals="clean verify -DskipTests"
                    '''
                    sh '''
                       mvn -B release:perform \
                       -Dgoals="clean deploy -DskipTests -Dmaven.install.skip=true"
                    '''
                }
            }
        }
    }
}
