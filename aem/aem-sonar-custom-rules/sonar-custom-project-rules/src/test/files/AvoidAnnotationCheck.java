class AvoidAnnotationCheck {

  int aField;

  @MyAnnotation
  public void aMethod() {

  }

  @Test // Noncompliant {{Avoid using annotation @Test}}
  public void aMethod() {

  }

}
