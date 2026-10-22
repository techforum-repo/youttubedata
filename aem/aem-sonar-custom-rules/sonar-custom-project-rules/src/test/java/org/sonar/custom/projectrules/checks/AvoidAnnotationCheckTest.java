
package org.sonar.custom.projectrules.checks;

import org.junit.Test;
import org.sonar.custom.projectrules.checks.AvoidAnnotationRule;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class AvoidAnnotationCheckTest {

  @Test
  public void detected() {

    AvoidAnnotationRule check = new AvoidAnnotationRule();
  
     JavaCheckVerifier.newVerifier()
      .onFile("src/test/files/AvoidAnnotationCheck.java")
      .withCheck(check)
      .verifyIssues();
     
  }
}
