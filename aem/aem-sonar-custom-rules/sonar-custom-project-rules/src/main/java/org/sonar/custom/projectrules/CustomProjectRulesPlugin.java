package org.sonar.custom.projectrules;

import org.sonar.api.Plugin;

public class CustomProjectRulesPlugin implements Plugin {

  @Override
  public void define(Context context) {

    context.addExtension(CustomProjectRulesDefinition.class);

    context.addExtension(CustomProjectRulesCheckRegistrar.class);

  }

}
