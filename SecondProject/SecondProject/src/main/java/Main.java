import TestUtils.*;

public class Main {

    public static void main(String[] args)

    {
        XMLsource source = new XMLsource();
        String websiteToTest = source.getTestURL();
        System.out.println("DBG MSG: websiteToTest = "+websiteToTest);
        TestConductor newTest = new TestConductor(websiteToTest);
        System.out.println("DBG MSG: Finished Setting up Property :: Default is Chrome driver");
        //--------------------------
          newTest.performTests(source.getDriverType());
        //--------------------------
        System.out.println("DBG MSG: Finished Testing "+websiteToTest);
    }
}



