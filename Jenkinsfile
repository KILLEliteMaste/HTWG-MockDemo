pipeline {
    agent any
    tools {
        maven 'Maven'
        jdk 'JDK 11'
    }
    stages {
        // Run unit test in all cases
        stage('Unit Test') {
            steps {
                    echo BRANCH_NAME
                    sh 'mvn -Dmaven.test.failure.ignore=true clean test site'
                    jacoco()
                    recordIssues(tools: [checkStyle(), findBugs(useRankAsPriority: true), pmdParser()])
            }
            post {
                always {
                    junit 'target/surefire-reports/**/*.xml'
                }
            }
        }

        // Build involves integration tests and upload to artifactory
        stage('Build') {
            when {
                anyOf {
                    branch 'master'
                    branch 'development'
                }
            }
            steps {
                    echo BRANCH_NAME
                    sh 'mvn -Dbuild.number=$BUILD_NUMBER clean install site' 
            }
            post {
                always {
                    junit 'target/failsafe-reports/**/*.xml'
                }
            }
        }

    }
}
