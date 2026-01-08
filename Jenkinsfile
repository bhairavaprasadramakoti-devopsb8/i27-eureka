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
        SONAR_URL = 'http://34.57.107.12:9000'
        SONAR_TOKEN = credentials('sonar_creds')
        POM_VERSION = readMavenPom().getVersion()
        POM_PACKAGING = readMavenPom().getPackaging()

        // Docker hub details
        DOCKER_HUB = "docker.io/bhairavaprasadramakoti"
        DOCKER_CREDS = credentials("dockerhub_creds")
        //JFROG_DOCKER_REPO = "i27.jfrog.io"
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
                sh """
                cp ${WORKSPACE}/target/i27-${env.APPLICATION_NAME}-${env.POM_VERSION}.${env.POM_PACKAGING} ./.cicd
                echo "Existing Jar Format: i27-${env.APPLICATION_NAME}-${env.POM_VERSION}.${env.POM_PACKAGING}"
                echo "Target Jar Format: i27-${env.APPLICATION_NAME}-${BUILD_NUMBER}-${BRANCH_NAME}.${env.POM_PACKAGING}"
                echo '**** New format ****'
                echo '**** i27-eureka-22-master.jar ****'
                echo '**** Building Docker Image ****'
                // sh 'docker build -t eureka:latest .'
                docker build --no-cache --build-arg JAR_SOURCE=i27-${env.APPLICATION_NAME}-${env.POM_VERSION}.${env.POM_PACKAGING} -t ${env.DOCKER_HUB}/${env.APPLICATION_NAME}:$GIT_COMMIT ./.cicd
                """
            }
        }
    }
}

// New format
// i27-eureka-22-master.jar
// bhairavaprasadramakoti
