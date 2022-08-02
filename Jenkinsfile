
pipeline{
    agent any
    environment {
        CREDENTIAL = credentials('kubernetes')
    }
    tools {
        maven 'Maven'
    }
    stages{
       stage('GetCode'){
            steps{
                checkout scm
            }
       }        
       stage('Build'){
            steps{
                sh 'mvn clean'
                
            }
       }
       stage('SonarQube analysis') {
        	steps{
        		withSonarQubeEnv('sonarqube-6.5') { 
              			//sh "sudo rm ~/.m2/repository/org/owasp/dependency-check-data/7.0/jsrepository.json"
        			sh "mvn test -Dtest=TestControllerTests  -DfailIfNoTests=false"
        			sh "mvn clean install sonar:sonar -Dsonar.login=admin -Dsonar.password=admin"
    			}
        	}
        }
        stage('Quality'){
            steps{
                script{
                    sleep(10)
                    //qualitygate = waitForQualityGate()
                    //if (qualitygate.status != "OK") {
                      //  currentBuild.result = "FAILURE"
                        //slackSend (channel: '****', color: '#F01717', message: "*$JOB_NAME*, <$BUILD_URL|Build #$BUILD_NUMBER>: Code coverage threshold was not met! <http://****.com:9000/sonarqube/projects|Review in SonarQube>.")
                    //}
                
                    waitForQualityGate abortPipeline: true                    
                }
            }
       }
      stage('Zip the Report Folder'){
            steps{
              script{
		      
                 sh "sudo apt install zip unzip"
                //sh "cd /var/lib/jenkins/workspace/sonar-email/target/site/"
                sh "sudo zip -r jacoco.zip /var/lib/jenkins/workspace/sonar/target/site/jacoco"
		sh "sudo mv /var/lib/jenkins/workspace/sonar/jacoco.zip /home/mohammad_fazil/jacoco.zip"
                sh "pwd"
                sh "ls -a"
              }
            }
       }
       stage('SonarQube report to GCS Bucket'){
            steps{
		    sh """
                	gcloud version

                	gcloud auth activate-service-account --key-file="$CREDENTIAL"
			gsutil cp -r /home/mohammad_fazil/jacoco.zip gs://sonarreport/codecoverage/
		    """
                
            }
       }
      stage('Dependency Check') {
        	steps{
        		withSonarQubeEnv('sonarqube-6.5') { 
              
        			sh "mvn dependency-check:aggregate"
				
				sh """
					gcloud version

                			gcloud auth activate-service-account --key-file="$CREDENTIAL"
					gsutil cp -r ${env.WORKSPACE}/target/dependency-check-report.html gs://sonarreport/dependency/
				
				"""
    			}
        	}
      }
      stage('Build Docker Image') {
		    steps {
			    sh 'whoami'
			    script {
				    myimage = docker.build("fazilniveus/devops:${env.BUILD_ID}")
			    }
		    }
	    }
	    
	    stage("Push Docker Image") {
		    steps {
			    script {
				    echo "Push Docker Image"
				    withCredentials([string(credentialsId: 'dockerhub', variable: 'dockerhub')]) {
            				sh "docker login -u fazilniveus -p ${dockerhub}"
				    }
				        myimage.push("${env.BUILD_ID}")
				    
			    }
		    }
	    }
	    
	    
	    stage("Vulnerability Scanning") {
		    steps {
			    script {
				    
            			sh "wget -qO - https://raw.githubusercontent.com/anchore/grype/main/install.sh | sudo bash -s -- -b /usr/local/bin"
				sh "grype fazilniveus/devops:${env.BUILD_ID} >> Vulnerable.txt"
				    
				sh """
					gcloud version

                			gcloud auth activate-service-account --key-file="$CREDENTIAL"
					gsutil cp -r ${env.WORKSPACE}/Vulnerable.txt gs://sonarreport/vulnerable/
					
					echo "Done"
				
				"""
				
				    
			    }
		    }
	    }
      
        
    }
}
