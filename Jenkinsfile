pipeline {
    agent {
        label 'master-slave'
    }

    tools {
        maven 'Maven-3.8.9'
        jdk 'JDK-17'
    }

    parameters {
        choice(name: 'buildOnly',
            choices: 'no\nyes',   // ['false', 'true'],
            description: 'Set to true to only build the application without deploying'   
        )
        choice(name: 'dockerPush',
            choices: 'no\nyes', // ['false', 'true'],
            description: 'Set to true to only push the docker image to registry'
        )
        choice(name: 'deployToDev',
            choices: 'no\nyes', // ['false', 'true'],
            description: 'Set to true to deploy to Dev environment'
        )
        choice(name: 'deployToTest',
            choices: 'no\nyes', // ['false', 'true'],
            description: 'Set to true to deploy to Test environment'
        )
        choice(name: 'deployToStage',
            choices: 'no\nyes', // ['false', 'true'],
            description: 'Set to true to deploy to Stage environment'
        )
        choice(name: 'deployToProd',
            choices: 'no\nyes', // ['false', 'true'],
            description: 'Set to true to deploy to Prod environment'
        )
        
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
            when {
                anyOf {
                    // expression { params.buildOnly == 'no' }
                    // expression { params.dockerPush == 'yes' }
                    // expression { params.deployToDev == 'yes' }
                    // expression { params.deployToTest == 'yes' }
                    // expression { params.deployToStage == 'yes' }
                    // expression { params.deployToProd == 'yes' }
                    // expression { params.buildOnly == 'no' || params.dockerPush == 'yes' || params.deployToDev == 'yes' || params.deployToTest == 'yes' || params.deployToStage == 'yes' || params.deployToProd == 'yes'
                    expression {
                        params.buildOnly == 'yes'
                        params.dockerPush == 'yes'
                    }
                }
            }
            steps {
                script {
                    // Calling the buildApp method
                    buildApp().call()
                }
                // echo "Building ${APPLICATION_NAME} Application"
                // sh 'mvn clean package -DskipTests=true'
                // archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
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

        stage('DockerBuildAndPush') {
            when {
                anyOf {
                    expression {
                        params.dockerPush == 'yes'
                    }
                }
            }
            steps {
                dockerBuildAndPush().call()
                ///i27-eureka-0.0.1-SNAPSHOT.jar
                // sh """
                // ls -la
                // cp ${WORKSPACE}/target/i27-${env.APPLICATION_NAME}-${env.POM_VERSION}.${env.POM_PACKAGING} ./.cicd
                // echo "Existing Jar Format: i27-${env.APPLICATION_NAME}-${env.POM_VERSION}.${env.POM_PACKAGING}"
                // echo "Target Jar Format: i27-${env.APPLICATION_NAME}-${BUILD_NUMBER}-${BRANCH_NAME}.${env.POM_PACKAGING}"
                // echo '**** New format ****'
                // echo '**** i27-eureka-22-master.jar ****'
                // echo '**** Building Docker Image ****'
                // // sh 'docker build -t eureka:latest .'
                // docker build --no-cache --build-arg JAR_SOURCE=i27-${env.APPLICATION_NAME}-${env.POM_VERSION}.${env.POM_PACKAGING} -t ${env.DOCKER_HUB}/${env.APPLICATION_NAME}:$GIT_COMMIT ./.cicd
                // echo "***************** Docker Login *****************"
                // docker login -u ${DOCKER_CREDS_USR} -p ${DOCKER_CREDS_PSW}
                // echo "****************** Pushing Image to Docker Hub *****************"
                // docker push ${env.DOCKER_HUB}/${env.APPLICATION_NAME}:$GIT_COMMIT
                // """
            }
        }
        stage ('DeployToDev') {
            when {
                anyOf {
                    expression {
                        params.deployToDev == 'yes'
                    } 
                }
            }
            steps {
                script {
                    // Calling the method and passing the arguments
                    dockerDeploy('dev', '5761').call()
                }

                // echo "Deploying to Dev environment"
                // script {
                //     try {
                //     // Stop the Container
                //     sh "docker stop ${env.APPLICATION_NAME}-dev"
                //     // Remove the Container
                //     sh "docker rm ${env.APPLICATION_NAME}-dev"
                //     // Creating a Container
                //     }
                //     catch(err) {
                //         echo "Error Caught: $err"                                    
                //     }
                //     sh "docker run --name ${env.APPLICATION_NAME}-dev -d -p 5761:8761 -t ${env.DOCKER_HUB}/${env.APPLICATION_NAME}:$GIT_COMMIT"
                // }
            }

        }
        stage ('DeployToTest') {
            when {
                anyOf {
                    expression {
                        params.deployToTest == 'yes'
                    } 
                }
            }
            steps {
                script {
                    // Calling the method and passing the arguments
                    dockerDeploy('test', '6761').call()
                }
                // echo "Deploying to Test environment"
                // script {
                //     try {
                //     // Stop the Container
                //     sh "docker stop ${env.APPLICATION_NAME}-test"
                //     // Remove the Container
                //     sh "docker rm ${env.APPLICATION_NAME}-test"
                //     // Creating a Container
                //     }
                //     catch(err) {
                //         echo "Error Caught: $err"                                    
                //     }
                //     sh "docker run --name ${env.APPLICATION_NAME}-test -d -p 5761:8761 -t ${env.DOCKER_HUB}/${env.APPLICATION_NAME}:$GIT_COMMIT"
                // }
            }

        }
        stage ('DeployToStage') {
            when {
                anyOf {
                    expression {
                        params.deployToStage == 'yes'
                    } 
                }
            }
            steps {
                script {
                    // Calling the method and passing the arguments
                    dockerDeploy('stage', '7761').call()
                }
                // echo "Deploying to Stage environment"
                // script {
                //     try {
                //     // Stop the Container
                //     sh "docker stop ${env.APPLICATION_NAME}-stage"
                //     // Remove the Container
                //     sh "docker rm ${env.APPLICATION_NAME}-stage"
                //     // Creating a Container
                //     }
                //     catch(err) {
                //         echo "Error Caught: $err"                                    
                //     }
                //     sh "docker run --name ${env.APPLICATION_NAME}-stage -d -p 5761:8761 -t ${env.DOCKER_HUB}/${env.APPLICATION_NAME}:$GIT_COMMIT"
                // }
            }

        }
        stage ('DeployToProd') {
            when {
                anyOf {
                    expression {
                        params.deployToProd == 'yes'
                    } 
                }
            }
            steps {
                script {
                    // Calling the method and passing the arguments
                    dockerDeploy('prod', '8761').call()
                }
                // echo "Deploying to Prod environment"
                // script {
                //     try {
                //     // Stop the Container
                //     sh "docker stop ${env.APPLICATION_NAME}-prod"
                //     // Remove the Container
                //     sh "docker rm ${env.APPLICATION_NAME}-prod"
                //     // Creating a Container
                //     }
                //     catch(err) {
                //         echo "Error Caught: $err"                                    
                //     }
                //     sh "docker run --name ${env.APPLICATION_NAME}-prod -d -p 5761:8761 -t ${env.DOCKER_HUB}/${env.APPLICATION_NAME}:$GIT_COMMIT"
                // }
            }

        }
    }
}

// Build the application
def buildApp() {
    return {
        echo "Building ${APPLICATION_NAME} Application"
        sh 'mvn clean package -DskipTests=true'
        archiveArtifacts artifacts: 'target/*.jar' //, fingerprint: true
    }
}

// Docker Build and Push Method
def dockerBuildAndPush() {
    return {
        echo "**** Building Docker Images ****"
        sh "docker build --no-cache --build-arg JAR_SOURCE=i27-${env.APPLICATION_NAME}-${env.POM_VERSION}.${env.POM_PACKAGING} -t ${env.DOCKER_HUB}/${env.APPLICATION_NAME}:$GIT_COMMIT ./.cicd"
        echo "***************** Docker Login *****************"
        sh "docker login -u ${DOCKER_CREDS_USR} -p ${DOCKER_CREDS_PSW}"
        echo "****************** Docker Push *****************"
        sh "docker push ${env.DOCKER_HUB}/${env.APPLICATION_NAME}:$GIT_COMMIT"
    }
}

// Deploy to Container Method
def dockerDeploy(envDeploy, port) {
    return {
        echo "Deploying to $envDeploy environment"
        script {
            try {
                // Stop the Container
                sh "docker stop ${env.APPLICATION_NAME}-$envDeploy"
                // Remove the Container
                sh "docker rm ${env.APPLICATION_NAME}-$envDeploy"
                // Creating a Container
            }
            catch(err) {
                echo "Error Caught: $err"                                    
            }
            sh "docker run --name ${env.APPLICATION_NAME}-$envDeploy -d -p $port:8761 -t ${env.DOCKER_HUB}/${env.APPLICATION_NAME}:$GIT_COMMIT"
        }
    }
}

// New format
// i27-eureka-22-master.jar
// bhairavaprasadramakoti
