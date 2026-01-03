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
                    -Dsonar.host.url=http://136.119.175.25:9000 \
                    -Dsonar.login=sqa_ef3c202cdd0ccf67383baeaca3d409839d845734
                """
            }
        }
    }
}


