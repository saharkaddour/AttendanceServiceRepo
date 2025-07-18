pipeline {
    agent any

    tools {
        maven "M3"
        git 'DefaultGit'
        jdk "JDK17"
    }

    environment {
        SONARQUBE_ENV = 'SonarQube'
    }

    stages {
        stage('Checkout') {
            steps {
                git credentialsId: 'github-token', url: 'https://github.com/saharkaddour/AttendanceServiceRepo.git', branch: 'saharkaddour-V-1'
            }
        }

        stage('Clean') {
            steps {
                bat 'mvnw.cmd clean'
            }
        }

        stage('Compile') {
            steps {
                bat 'mvnw.cmd compile'
            }
        }

        stage('Code Quality (SonarQube)') {
            steps {
                withSonarQubeEnv("${SONARQUBE_ENV}") {
                    bat 'mvnw.cmd sonar:sonar'
                }
            }
        }

        stage('Test') {
            steps {
                bat 'mvnw.cmd test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/TEST-*.xml'
                    jacoco exclusionPattern: '**/target/jacoco.exec', minimumBranchCoverage: '0', minimumClassCoverage: '0', minimumInstructionCoverage: '0', minimumLineCoverage: '0', minimumMethodCoverage: '0'
                }
            }
        }

        stage('Package') {
            steps {
                bat 'mvnw.cmd package'
            }
        }

        stage('Upload to Artifactory') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'artifactory-token', usernameVariable: 'ART_USER', passwordVariable: 'ART_PASS')]) {
                    bat '''
                        jfrog rt config --url=http://localhost:8082/artifactory --user=%ART_USER% --password=%ART_PASS% --interactive=false
                        jfrog rt upload "target\\*.jar" "libs-release-local/attendance-service/"
                    '''
                }
            }
        }
    }

    post {
        success {
            echo '✅ Pipeline completed successfully!'
        }
        failure {
            echo '❌ Pipeline failed.'
        }
    }
}
