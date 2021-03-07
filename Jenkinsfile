pipeline {
    agent any
    options {
        timestamps()
        skipDefaultCheckout true
        buildDiscarder(logRotator(numToKeepStr: '5'))
        timeout (time: 5, unit: 'MINUTES')
    }
    environment {
        GIT_COMMIT = ""
        GIT_COMMIT_AUTHOR = ""
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

        stage("Checkout") {
            steps {
                checkout([$class                           : 'GitSCM',
                          branches                         : [[name: 'refs/heads/${BRANCH_NAME}']],
                          doGenerateSubmoduleConfigurations: false,
                          extensions                       : [[$class: 'LocalBranch', localBranch: '${BRANCH_NAME}'],
                                                              [$class: 'CheckoutOption', timeout: 5],
                                                              [$class: 'CloneOption', depth: 0, noTags: true, reference: 'workspace/qaguru4_pipeline', shallow: true, timeout: 5]],
                          submoduleCfg                     : [],
                          userRemoteConfigs                : [[url: 'https://github.com/aikfiend/qaguru4.git']]
                ])
                script {
                    GIT_COMMIT = sh(returnStdout: true, script: "git log -n 1 --pretty=format:'%H'").trim()
                    GIT_COMMIT_AUTHOR = sh(returnStdout: true, script: "git log -n 1 --pretty=format:'%ae' | grep -o '.*@' | sed 's/@//'").trim()
                }
            }
        }

        stage("Simple Tests") {
            steps {
                script {
                    SIMPLE_TESTS_RESULT = 'FAILED'
                }
                timeout(time: 2, unit: 'MINUTES') {
                    sh './gradlew --console=plain --info -Pverbose.tests clean simpleTests'
                    script {
                        SIMPLE_TESTS_RESULT = 'SUCCESS'
                    }
                }
            }
        }
    }
    post {
        always {
            junit 'build/test-results/**/*.xml'
            allure([includeProperties: false,
                    jdk: '',
                    properties: [],
                    reportBuildPolicy: 'ALWAYS',
                    results: [[path: 'build/allure-results']]
            ])
        }
    }
}