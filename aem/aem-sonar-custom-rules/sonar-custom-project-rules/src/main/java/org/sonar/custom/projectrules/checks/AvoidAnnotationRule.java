package org.sonar.custom.projectrules.checks;

import java.util.List;

import org.sonar.check.Rule;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.tree.AnnotationTree;
import org.sonar.plugins.java.api.tree.BaseTreeVisitor;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.custom.projectrules.utils.PrinterVisitor;

@Rule(key = AvoidAnnotationRule.RULE_KEY)
public class AvoidAnnotationRule extends BaseTreeVisitor implements JavaFileScanner {

  
	public static final String RULE_KEY = "AvoidAnnotation";

	private JavaFileScannerContext context;

   
  @Override
  public void scanFile(JavaFileScannerContext context) {
    this.context = context;

    scan(context.getTree());

    System.out.println(PrinterVisitor.print(context.getTree()));
  }

  @Override
  public void visitMethod(MethodTree tree) {
    List<AnnotationTree> annotations = tree.modifiers().annotations();
    for (AnnotationTree annotationTree : annotations) {
      if (annotationTree.annotationType().is(Tree.Kind.IDENTIFIER)) {
        IdentifierTree idf = (IdentifierTree) annotationTree.annotationType();
        
        if (idf.name().equals("Test")) {
        	System.out.println(String.format("Avoid using annotation @%s", "Test"));
            context.reportIssue(this, idf, String.format("Avoid using annotation @%s", "Test"));
        }
      }
    }

    super.visitMethod(tree);
  }
}
