package org.sonar.custom.projectrules;

import java.util.List;
import org.sonar.plugins.java.api.CheckRegistrar;
import org.sonar.plugins.java.api.JavaCheck;
import org.sonarsource.api.sonarlint.SonarLintSide;

@SonarLintSide
public class CustomProjectRulesCheckRegistrar implements CheckRegistrar {

  @Override
  public void register(RegistrarContext registrarContext) {

    registrarContext.registerClassesForRepository(CustomProjectRulesDefinition.REPOSITORY_KEY, checkClasses(), testCheckClasses());
  }

  public static List<Class<? extends JavaCheck>> checkClasses() {
    return CustomProjectRulesList.getJavaChecks();
  }

  public static List<Class<? extends JavaCheck>> testCheckClasses() {
    return CustomProjectRulesList.getJavaTestChecks();
  }
}
