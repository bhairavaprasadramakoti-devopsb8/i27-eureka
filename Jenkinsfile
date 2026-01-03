pipeline {
    agent {
        label 'master-slave'
    }
    tools {
        maven 'Maven-3.8.9'
        jdk 'JDK-17'
    }
    stages {
        stage ('build'){
            steps {
                echo 'Building Eureka Application'
                sh "mvn --version"
            }
        }
    }
}