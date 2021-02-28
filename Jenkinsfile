pipeline {
    agent any
    options {
        timestamps()
        skipDefaultCheckout true
        buildDiscarder(logRotator(numToKeepStr: '5'))
        timeout (time: 5, unit: 'MINUTES')
    }
    triggers {
        pollSCM('@hourly')
//        The same, but cron-style:
//        pollSCM('H * * * *')
    }
    stages {
        stage("Working with reference repo") {
            parallel {
                stage("Create reference repo") {
                    when {
                        not {
                            expression {
                                fileExists("../qaguru4_pipeline")
                            }
                        }
                    }
                    steps {
                        ws('workspace/qaguru4_pipeline') {
                            sh 'git clone --mirror https://github.com/aikfiend/qaguru4.git .'
                        }
                    }
                }
                stage("Update reference repo") {
                    when {
                        expression {
                            fileExists("../qaguru4_pipeline")
                        }
                    }
                    steps {
                        ws('workspace/qaguru4_pipeline') {
                            sh 'git fetch --all --prune'
                        }
                    }
                }
            }
        }
        stage("SimpleTests") {
            steps {
                script {
                    SIMPLE_TESTS_RESULT = 'FAILED'
                }
                timeout(time: 2, unit: 'MINUTES') {
                    sh './gradlew --build-cache --console=plain simpleTests'
                    script {
                        SIMPLE_TESTS_RESULT = 'SUCCESS'
                    }
                }
            }
            post {
                always {
                    junit 'build/test-results/**/*.xml'
                }
            }
        }
    }
}