# SonarQube Custom Project Rules

Project with Custom SonarQube Rules(only a sample rule has been enabled now)

To build Plugin Jar:

Clone the project git clone https://github.com/techforum-repo/projects.git, cd to sonar-custom-project-rules

```sh
mvn clean install

```

Copy target/sonar-custom-project-rules-1.0.0.jar to SonarQube extensions folder.

Download the pre-built jar file from - https://github.com/techforum-repo/projects/releases/download/latest/sonar-custom-project-rules-1.0.0.jar

The rules are registered with the repository name custom-project-rules

### To configure new rules

* Create new rule class under src\main\java\org\sonar\custom\projectrules\checks (Refer AvoidAnnotationRule.java)

* Add new Java rule file entry to src\main\java\org\sonar\custom\projectrules\CustomProjectRulesList.java 

```
	 public static List<Class<? extends JavaCheck>> getJavaChecks() {
		return Collections.unmodifiableList(Arrays.asList(
		   AvoidAnnotationRule.class, NewRule.class));
	  }
 ```

 * Create the meta data for the new rules under src\main\resources\org\sonar\l10n\custom\projectrules\squid (refer AvoidAnnotation_java.json)
 
 * Create the Rule definition and the details under src\main\resources\org\sonar\l10n\custom\projectrules\squid (refer AvoidAnnotation_java.html)
 
 * Create the Junit test class for the new rules under src\test\java\org\sonar\custom\projectrules\checks(refer AvoidAnnotationCheckTest.java) - the JUNIT test cases can be executed to verify the rule exception is thrown for the test classes
 
 * Create the test classes under src\test\files (refer AvoidAnnotationCheck.java) 
