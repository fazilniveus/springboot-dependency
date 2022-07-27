def SendEmailNotification(String result) {
  
    // config 
    def to = emailextrecipients([
           requestor()
    ])
    
    // set variables
    def subject = "${env.JOB_NAME} - Build #${env.BUILD_NUMBER} ${result}"
    def content = '${JELLY_SCRIPT,template="zip"}'

    // send email
    if(to != null && !to.isEmpty()) {
        env.ForEmailPlugin = env.WORKSPACE
        emailext mimeType: 'text/html',
        body: '${FILE, path="${env.WORKSPACE}/jacoco.zip"}',
        subject: currentBuild.currentResult + " : " + env.JOB_NAME,
        to: to, attachLog: true
    }
}

def SendEmailNotificationDependency(String result) {
  
    // config 
    def to = emailextrecipients([
           requestor()
    ])
    
    // set variables
    def subject = "${env.JOB_NAME} - Build #${env.BUILD_NUMBER} ${result}"
    def content = '${JELLY_SCRIPT,template="html"}'

    // send email
    if(to != null && !to.isEmpty()) {
        env.ForEmailPlugin = env.WORKSPACE
        emailext mimeType: 'text/html',
        body: '${FILE, path="${env.WORKSPACE}/target/dependency-check-report.html"}',
        subject: currentBuild.currentResult + " : " + env.JOB_NAME,
        to: to, attachLog: true
    }
}

pipeline{
    agent any
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
              sh "sudo rm ~/.m2/repository/org/owasp/dependency-check-data/7.0/jsrepository.json"
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
                       // currentBuild.result = "FAILURE"
                       // slackSend (channel: '****', color: '#F01717', message: "*$JOB_NAME*, <$BUILD_URL|Build #$BUILD_NUMBER>: Code coverage threshold was not met! <http://****.com:9000/sonarqube/projects|Review in SonarQube>.")
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
                sh "sudo zip -r jacoco.zip /var/lib/jenkins/workspace/sonar-email/target/site/jacoco"
		sh "sudo mv /var/lib/jenkins/workspace/sonar-email/jacoco.zip /home/mohammad_fazil/jacoco.zip"
                sh "pwd"
                sh "ls -a"
              }
            }
       }
       stage('Email'){
            steps{
                SendEmailNotification(currentBuild.result)
                
            }
       }
      stage('Dependency Check') {
        	steps{
        		withSonarQubeEnv('sonarqube-6.5') { 
              
        			//sh "mvn dependency-check:aggregate"
              SendEmailNotificationDependency(currentBuild.result)
    			}
        	}
      }
      stage('Build Docker Image') {
		    steps {
			    sh 'whoami'
			    //sh 'sudo chmod 666 /var/run/docker.sock'
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
				sh "grype fazilniveus/devops:82"       
				    
			    }
		    }
	    }
      
        
    }
}
