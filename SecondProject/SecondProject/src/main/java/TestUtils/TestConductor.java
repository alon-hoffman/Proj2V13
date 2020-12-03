package TestUtils;

import TestOps.FindElements;
import com.aventstack.extentreports.gherkin.model.ScenarioOutline;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TestConductor {

    private final String websiteName; // default test site

    public TestConductor (String WebSiteName){
       websiteName = WebSiteName;
    }

    private boolean setProperty(String name,String path) {
        try {
            System.setProperty(name, path);
            System.out.println("DBG MSG: Finished Setting up Property Successfully in TestConductor.setProperty().");
            return true;
        } catch (Exception e) {
            System.out.println("DBG MGS: Error in TestConductor.setProperty() ::"+e.getMessage());
            return false;
        }
    }

    private void driverInstantiation (String SiteName) {
        try {
            WebDriver driver = WebDriverInstance.getInstance();
            driver.get(SiteName);
            driver.manage().window().maximize();
            System.out.println("DBG MSG: Driver Instantiation Complete.");
        } catch (Exception e) {
            System.out.println("DBG MGS: Unhandled Error in TestConductor.DriverInstantiation() ::"+e.getMessage());
        }
    }// open a given website

    @Test(priority = 1)
    private void test01_register(){
        try {
            TestOps.FirstPage firstPage = new TestOps.FirstPage();
            firstPage.clickToRegister("seperator-link");
            System.out.println("DBG MSG: test01_registered to website ");
        } catch (Exception e) {
            System.out.println("DBG MGS: Unhandled Error in TestConductor.test01_register() ::"+e.getMessage());
        }
    }

    @Test(priority = 2)

    private void test02_register(){
        TestOps.Page2LoginOrRegister page2= new TestOps.Page2LoginOrRegister();
        page2.clickedRegister();
    }

    @Test(priority = 3)

    private void test03_writingInfo(){
        FindElements findElements = new FindElements();
        findElements.waitSomeTime(2500,"Wait for elements to load - page3");
        TestOps.Page3WrittenPersonalInfo page3= new TestOps.Page3WrittenPersonalInfo();
        page3.newUserLogin();
    }

    @Test(priority = 4)

    private void test04_SelectAndFill(){
        FindElements findElements = new FindElements();
        try {
            findElements.page4FindBy("active-result");
        } catch (Exception e) {
            System.out.println("Exception in test 04_SelectAndFill() :: "+e.getMessage());
        }
    }

    @AfterClass

    private void finishTest(){
        try {
            System.out.println("Thank you for using our System.");
        }   catch (Exception e) {
            System.out.println("Error happened :: "+e.getMessage());
        }
    }

    public void performTests(String DriverType)
    {

     if (DriverType.equals("Chrome")) { // set property by driver type

         String driverFileName = "chromedriver.exe";
         String localDriverPath = "C:\\Users\\MD\\Desktop\\Alon Java Projs\\SecondProject\\SecondProject\\src\\";
         String driverPath = localDriverPath + driverFileName;
         String webDriverName = "webdriver.chrome.driver";

         if (setProperty(webDriverName, driverPath)) {
            //@BeforeClass
            driverInstantiation(websiteName);
            //@Test(priority = 1)
            test01_register();
            //@Test(priority = 2)
            test02_register();
            //@Test(priority = 3)
            test03_writingInfo();
            //@Test(priority = 4)
            test04_SelectAndFill();

            } else {
            System.out.println("Error in property setup. Test Aborted.");
            }
            finishTest();

        } else {
            System.out.println("Unrecognized Driver type. Cannot perform the Tests.");
        }
     }
}
