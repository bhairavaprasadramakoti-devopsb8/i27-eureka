pipeline {
    agent {
        label 'master-slave'
    }
    tools {
        maven 'Maven-3.8.9'
        jdk 'JDK-17'
    }
    environment {
        APPLICATION_NAME = "eureka"
        SONAR_URL = "http://136.116.21.201:9000"
        SONAR_TOKEN = credentials('sonar_creds')
    }
    stages {
        stage ('build'){
            steps {
                echo "Building ${APPLICATION_NAME} Application"
                sh "mvn package -DskipTests=true"
                archiveArtifacts artifacts: 'target/*jar'
            }
        }
        stage ('sonarqube') {
            steps {
                echo "Starting Sonar Scans"
                sh """
                mvn clean verify sonar:sonar \
                    -Dsonar.projectKey=i27-eureka \
                    -Dsonar.host.url=${env.SONAR_URL} \
                    -Dsonar.login=${env.SONAR_TOKEN}
                """
            }
        }
    }
}


