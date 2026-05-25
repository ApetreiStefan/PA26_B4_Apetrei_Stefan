public class TestClass {

    @MyTestAnnotation
    public void myAnnotatedMethod() {
        System.out.println("Executing myAnnotatedMethod (0 args)");
    }

    @MyTestAnnotation
    public void myAnnotatedMethodWithInt(int value) {
        System.out.println("Executing myAnnotatedMethodWithInt (1 int arg). Value: " + value);
    }

    @MyTestAnnotation
    public void myAnnotatedMethodWithInteger(Integer value) {
        System.out.println("Executing myAnnotatedMethodWithInteger (1 Integer arg). Value: " + value);
    }

    public void myNormalMethod() {
        System.out.println("Executing myNormalMethod (0 args)");
    }
}
