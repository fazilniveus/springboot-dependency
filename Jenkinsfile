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
        		withSonarQubeEnv('sonarqube-6.7.1') { 
        			sh "mvn test -Dtest=TestControllerTests  -DfailIfNoTests=false"
        			sh "mvn clean install sonar:sonar -DfailIfNoTests=false"
    			}
        	}
        }
    }
}