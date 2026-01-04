pipeline {
    agent {
        label 'master-slave'
    }

    tools {
        maven 'Maven-3.8.9'
        jdk 'JDK-17'
    }

    environment {
        APPLICATION_NAME = 'eureka'
        SONAR_URL = 'http://136.116.21.201:9000'
        SONAR_TOKEN = credentials('sonar_creds')
        POM_VERSION = readMavenPom().getVersion()
        POM_PACKAGING = readMavenPom().getPackaging()
    }

    stages {

        stage('Build') {
            steps {
                echo "Building ${APPLICATION_NAME} Application"
                sh 'mvn clean package -DskipTests=true'
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo 'Starting SonarQube Scan'
                withSonarQubeEnv('SonarQube') {
                    sh """
                        mvn sonar:sonar \
                        -Dsonar.projectKey=i27-eureka \
                        -Dsonar.projectName=i27-eureka \
                        -Dsonar.host.url=${SONAR_URL} \
                        -Dsonar.login=${SONAR_TOKEN}
                    """
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 2, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('DockerBuild') {
            steps {
                ///i27-eureka-0.0.1-SNAPSHOT.jar
                echo "Existing Jar Format: i27-${env.APPLICATION_NAME}-${env.POM_VERSION}.${env.POM_PACKAGING}"
                echo "Target Jar Format: i27-${env.APPLICATION_NAME}-${BUILD_NUMBER}-${BRANCH_NAME}.${env.POM_PACKAGING}"
                // New format
                // i27-eureka-22-master.jar
                echo '**** Building Docker Image ****'
                // sh 'docker build -t eureka:latest .'
            }
        }
    }
}



