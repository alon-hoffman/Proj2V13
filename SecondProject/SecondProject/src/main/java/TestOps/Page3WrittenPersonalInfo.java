package TestOps;

import java.util.Random;
import TestUtils.WebDriverInstance;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class Page3WrittenPersonalInfo {

    public void newUserLogin () {
    //do {
        FindElements FE = new FindElements();
        FE.waitSomeTime(1000,"In Page3");

        FindAndClick clickAndWrite = new FindAndClick();

        By ByParLink = new By.ByPartialLinkText("תמצאו");

        String userName = "קובי";
        clickAndWrite.writeInElement(By.id("ember1235"), userName);

        if (assertField(userName,WebDriverInstance.getInstance().findElement(By.id("ember1235")))) {

            String TestEmail = "test" + this.generateRandom() + "." + this.generateRandom() + "@gmail.com";
            clickAndWrite.writeInElement(By.id("ember1237"), TestEmail);

            if (assertField(TestEmail,WebDriverInstance.getInstance().findElement(By.id("ember1237")))) {

                String UserPassword = "AutoProj"+generateRandom();
                clickAndWrite.writeInElement(By.id("valPass"), UserPassword);
                clickAndWrite.writeInElement(By.id("ember1241"), UserPassword);
                System.out.println("UserPassword :: "+UserPassword);
                // Password is protected - returned value does not match

                            try {
                                clickTheBigButton("button");
                            } catch (Exception e) {
                                System.out.println("Exception in Button Click"+e.getMessage());
                            }
            }// endif

        } else {
            System.out.println("Failed Assert Test.");
        }

        FE.waitSomeTime(2000,"Page3WrittenPersonalInfo.thisIsMe() :: After Loop :: Check URL");
        //} while(WebDriverInstance.getInstance().getCurrentUrl().equals("https://buyme.co.il/?modal=login"));

    }

    private boolean assertField(String expected, WebElement el) {

        FindElements fe = new FindElements();
        fe.waitSomeTime(1000, "Wait for Field to update");
        String actual = el.getAttribute("value");

        //actual = actual.replaceAll("\\s+","");
        System.out.println("DBG: Expected="+expected+" :: actual="+actual);


        //String actualToPerson = GetTextFromInputElBelow(InputFieldToPerson);

        if (fe.assertText(expected,actual)) {
//------------------------------------------------------------------------------------
            System.out.println("-->Passed Assert Test :: actual="+actual);
        }
        else
        {
            System.out.println("Expected="+expected+" :: actual="+actual);
            return false;
        }

        return true;
    }


    private void clickTheBigButton(String LocalTagName)  {

        FindAndClick findAndClick= new FindAndClick();
        By ByLookup = By.tagName(LocalTagName);
        findAndClick.clickElement(ByLookup);

    }

    private int generateRandom () {
        Random rnd = new Random();
        return rnd.nextInt(10000);
    }
}

