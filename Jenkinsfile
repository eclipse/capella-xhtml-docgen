pipeline {
  agent { label 'migration' }
  
  tools {
        maven 'apache-maven-latest'
        jdk 'openjdk-jdk14-latest'
  }
  stages {
    stage('Generate TP') {
      steps {
        sh 'mvn verify -e -f releng/org.polarsys.capella.docgen.target/pom.xml'
      }
    }
    stage('Package docgen addon') {
      steps {
        sh 'mvn clean install -P full -P sign -e -f pom.xml'
      }
    }
    stage('Run tests') {
      steps {
      	wrap([$class: 'Xvnc', takeScreenshot: false, useXauthority: true]) {
        	sh 'mvn -Dmaven.test.failure.ignore=true -Dtycho.localArtifacts=ignore integration-test -P tests -X -e -f pom.xml'
        }
      }
    }
    stage('Archive artifacts') {
      steps {
        archiveArtifacts artifacts: 'releng/org.polarsys.capella.docgen.site/target/CapellaXHTMLDocGen-*.zip, releng/org.polarsys.capella.docgen.site/target/repository/**'
      }
    }
    stage('Deploy') {
	  when {
		  not { changeRequest() }
	  }
      steps {
          sshagent ( ['projects-storage.eclipse.org-bot-ssh']) {
            sh '''
				PROMOTED_SRC="releng/org.polarsys.capella.docgen.site/target/repository/*"
				PROMOTED_DST="/home/data/httpd/download.eclipse.org/capella/addons/xhtmldocgen/updates/nightly/master/"
				ssh genie.capella@projects-storage.eclipse.org rm -fr $PROMOTED_DST
				ssh genie.capella@projects-storage.eclipse.org mkdir -p $PROMOTED_DST
				scp -r $PROMOTED_SRC genie.capella@projects-storage.eclipse.org:$PROMOTED_DST
				
				PROMOTED_SRC="releng/org.polarsys.capella.docgen.site/target/*-dropins.zip"
				PROMOTED_DST="/home/data/httpd/download.eclipse.org/capella/addons/xhtmldocgen/dropins/nightly/master/"
				ssh genie.capella@projects-storage.eclipse.org rm -fr $PROMOTED_DST
				ssh genie.capella@projects-storage.eclipse.org mkdir -p $PROMOTED_DST
				scp -r $PROMOTED_SRC genie.capella@projects-storage.eclipse.org:$PROMOTED_DST
			  
            '''
        }
      }
    }
  }
}