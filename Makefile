artifact_name       := company-accounts.web.ch.gov.uk
version             := "unversioned"

.PHONY: all
all: build

.PHONY: clean
clean:
	mvn clean
	rm -f ./$(artifact_name).jar
	rm -f ./$(artifact_name)-*.zip
	rm -rf ./build-*
	rm -f ./build.log

.PHONY: build
build:
	mvn versions:set -DnewVersion=$(version) -DgenerateBackupPoms=false
	mvn package -DskipTests=true
	cp ./target/$(artifact_name)-$(version).jar ./$(artifact_name).jar

.PHONY: test
test: test-unit

.PHONY: test-unit
test-unit: clean
	mvn test

.PHONY: package
package:
	@test -s ./$(artifact_name).jar || { echo "ERROR: Service JAR not found"; exit 1; }
	$(info Packaging version: $(version))
	mvn versions:set -DnewVersion=$(version) -DgenerateBackupPoms=false
	mvn package -DskipTests=true
	$(eval tmpdir:=$(shell mktemp -d build-XXXXXXXXXX))
	cp ./start.sh $(tmpdir)
	cp ./routes.yaml $(tmpdir)
	cp ./target/$(artifact_name)-$(version).jar $(tmpdir)/$(artifact_name).jar
	cd $(tmpdir); zip -r ../$(artifact_name)-$(version).zip *
	rm -rf $(tmpdir)

.PHONY: dist
dist: clean build package

.PHONY: sonar
sonar:
	mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.11.0.3922:sonar

.PHONY: sonar-pr-analysis
sonar-pr-analysis:
	mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.11.0.3922:sonar -P sonar-pr-analysis

.PHONY: security-check
security-check:
	mvn org.owasp:dependency-check-maven:check -DfailBuildOnCVSS=4 -DassemblyAnalyzerEnabled=false
