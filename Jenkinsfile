Jenkins file:

// This pipeline automates the build, test, and code quality analysis using Maven and SonarQube.

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


        // Stage 3: Compile - Compiles the project source code.
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

        // Stage 6: Package - Creates the executable JAR/WAR file.
        stage('Package') {
            steps {
                bat 'mvnw.cmd package'
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
