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
    }
}