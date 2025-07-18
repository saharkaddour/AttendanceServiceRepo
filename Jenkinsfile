// This pipeline automates the build, test, and code quality analysis using Maven and SonarQube,
// and integrates JMeter for performance testing.

pipeline {
    agent any // Exécute le pipeline sur n'importe quel agent disponible (ici, votre machine Windows)

    tools {
        maven "M3" // Assurez-vous que "M3" est configuré dans Jenkins > Gérer Jenkins > Outils globaux
        git 'DefaultGit' // Assurez-vous que 'DefaultGit' est configuré
        jdk "JDK17" // Assurez-vous que "JDK17" est configuré
    }

    environment {
        // Variables d'environnement pour SonarQube
        SONARQUBE_ENV = 'SonarQube' // Nom de votre configuration SonarQube dans Gérer Jenkins > Système

        // Variables d'environnement pour les services Docker et JMeter
        SPRING_BOOT_APP_NAME = 'attendance-service-backend' // Nom du conteneur pour votre backend Spring Boot
        JMeter_CONTAINER_NAME = 'jmeter-test-runner' // Nom du conteneur pour l'exécution de JMeter
        DOCKER_NETWORK_NAME = 'jmeter-test-network' // Nom du réseau Docker pour la communication entre les conteneurs

        // Chemin où le workspace Jenkins (sur Windows) sera monté dans le conteneur Docker JMeter
        // C'est un chemin Linux, car c'est le chemin INTERNE au conteneur Docker
        JMeter_WORKSPACE_CONTAINER = '/jenkins_workspace'

        // Chemin du fichier JMeter .jmx RELATIF à la racine de votre dépôt Git
        // IMPORTANT : Ajustez ce chemin si votre .jmx n'est pas directement à la racine du dépôt
        JMeter_TEST_PLAN_RELATIVE_PATH = 'attendance_test_plan.jmx' // Ex: 'jmeter-tests/attendance_test_plan.jmx'

        JMeter_RESULTS_JTL = 'results.jtl' // Nom du fichier de résultats JMeter
        JMeter_REPORT_HTML_DIR = 'report_html' // Nom du dossier pour le rapport HTML JMeter
    }

    stages {
        stage('Checkout') {
            steps {
                // Clone le dépôt Git. Tous les fichiers (code, Jenkinsfile, .jmx) seront dans ${WORKSPACE}
                git credentialsId: 'github-token', url: 'https://github.com/saharkaddour/AttendanceServiceRepo.git', branch: 'saharkaddour-V-1'
            }
        }

        stage('Clean') {
            steps {
                // Nettoie le projet Maven
                bat 'mvnw.cmd clean'
            }
        }

        stage('Compile') {
            steps {
                // Compile le code source du projet
                bat 'mvnw.cmd compile'
            }
        }

        stage('Code Quality (SonarQube)') {
            steps {
                // Exécute l'analyse de qualité de code avec SonarQube
                withSonarQubeEnv("${SONARQUBE_ENV}") {
                    bat 'mvnw.cmd sonar:sonar'
                }
            }
        }

        stage('Tests') {
            steps {
                // Exécute les tests unitaires
                bat 'mvnw.cmd test'
            }
            post {
                always {
                    // Publie les rapports de tests JUnit
                    junit '**/target/surefire-reports/TEST-*.xml'
                    // Publie les rapports de couverture de code JaCoCo
                    jacoco exclusionPattern: '**/target/jacoco.exec', minimumBranchCoverage: '0', minimumClassCoverage: '0', minimumInstructionCoverage: '0', minimumLineCoverage: '0', minimumMethodCoverage: '0'
                }
            }
        }

        stage('Package') {
            steps {
                // Crée le fichier JAR exécutable
                bat 'mvnw.cmd package'
            }
        }

        // --- DÉBUT DES STAGES DE TESTS DE PERFORMANCE JMETER ---

        stage('Setup Docker Network') {
            steps {
                wsl docker network create your-network-name-here         
            }

        stage('Start Spring Boot Backend in Docker') {
            steps {
                script {
                    // Chemin complet du JAR compilé dans l'espace de travail Jenkins sur Windows
                    // VÉRIFIEZ LE NOM EXACT DE VOTRE FICHIER JAR !
                    def jarPath = "${WORKSPACE}\\target\\attendance-service-0.0.1-SNAPSHOT.jar"

                    bat "wsl docker run -d --rm --name \"${SPRING_BOOT_APP_NAME}\" --network \"${DOCKER_NETWORK_NAME}\" -p 8088:8088 -v \"${jarPath}:/app.jar\" openjdk:17-jdk-slim java -jar /app.jar"

                    echo "Waiting for Spring Boot backend to start..."
                    sleep time: 30, unit: 'SECONDS'
                    echo "Spring Boot backend should be up."
                }
            }
        }

        stage('Run JMeter Performance Tests') {
            steps {
                script {
                   bat "wsl docker run --rm --name \"${JMeter_CONTAINER_NAME}\" --network \"${DOCKER_NETWORK_NAME}\" -v \"${WORKSPACE}:${JMeter_WORKSPACE_CONTAINER}\" justb4/jmeter:latest -n -t \"${JMeter_WORKSPACE_CONTAINER}/${JMeter_TEST_PLAN_RELATIVE_PATH}\" -l \"${JMeter_WORKSPACE_CONTAINER}/${JMeter_RESULTS_JTL}\" -e -o \"${JMeter_WORKSPACE_CONTAINER}/${JMeter_REPORT_HTML_DIR}\" -Jusers=10 -Jramp_time=5 -Jduration=60 -Jhost=\"${SPRING_BOOT_APP_NAME}\" -Jport=8088 -Jpath=/api/attendance"                
            }
        }

        // --- FIN DES STAGES DE TESTS DE PERFORMANCE JMETER ---

        stage('Publish JMeter Reports') {
            steps {
                script {
                    // Vérifie que le dossier du rapport HTML existe avant de tenter de le publier
                    bat "IF NOT EXIST \"${WORKSPACE}\\${JMeter_REPORT_HTML_DIR}\" exit 1"
                }
 
                publishHTML(target: [
                    allowMissing: false,
                    alwaysLinkToLastBuild: false,
                    keepAll: true,
                    reportDir: "${WORKSPACE}\\${JMeter_REPORT_HTML_DIR}", // Chemin direct dans le workspace Jenkins
                    reportFiles: 'index.html',
                    reportName: 'JMeter Performance Report',
                    reportTitles: 'JMeter Report'
                ])
                archiveArtifacts artifacts: "${WORKSPACE}\\${JMeter_RESULTS_JTL}", fingerprint: true
            }
        }
    }

    // Actions post-build (s'exécutent après tous les stages)
   post {
    // S'assure que le backend Docker et le réseau sont nettoyés, même si le pipeline échoue
    always {
        echo 'Cleaning up Docker resources...'
        // Arrête et supprime le conteneur du backend Spring Boot
        // '|| exit 0' permet à la commande de ne pas faire échouer l'étape Jenkins si le conteneur n'existe pas ou est déjà arrêté/supprimé
        bat "wsl docker stop ${SPRING_BOOT_APP_NAME} || exit 0"
        bat "wsl docker rm ${SPRING_BOOT_APP_NAME} || exit 0"
        // Supprime le réseau Docker
        bat "wsl docker network rm ${DOCKER_NETWORK_NAME} || exit 0"
        echo 'Docker cleanup complete.'
    }
}
        success {
            echo '✅ Pipeline completed successfully!'
        }
        failure {
            echo '❌ Pipeline failed.'
        }
    }
}
