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
        stage('SonarQube analysis') {
            def scannerHome = tool 'SonarQube Scanner';
            withSonarQubeEnv('My SonarQube Server') { // If you have configured more than one global server connection, you can specify its name
                sh "${scannerHome}/bin/sonar-scanner"
            }
        }
    }
}
