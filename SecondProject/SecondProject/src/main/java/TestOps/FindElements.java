package TestOps;

import TestUtils.WebDriverInstance;
import org.openqa.selenium.*;
import org.testng.Assert;
import org.openqa.selenium.support.locators.RelativeLocator;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FindElements {
//------------------------------------------------------------------------------------------------------------------------
//  Global Vars
//------------------------------------------------------------------------------------------------------------------------
    private WebDriver driver;
    private String CurrURL;
//------------------------------------------------------------------------------------------------------------------------
//  Main Procedure block :: Called from Main Conductor
//------------------------------------------------------------------------------------------------------------------------

    public void page4FindBy (String byTextClassName)  {

        final String startingURL = WebDriverInstance.getInstance().getCurrentUrl();
        final int NumOfTests = 10;
    //---------------------------------------------
    // Main Test Repeat loop
    //---------------------------------------------
        int repeatTest = 0;
        while (repeatTest < NumOfTests) {
            repeatTest++;

            //------------Category Selection-------------------------------------------------------
            if (selectFromCategories(startingURL,byTextClassName,repeatTest,NumOfTests)) {

                //----------------------------------------------
                // select one random choice from found list
                //----------------------------------------------

                waitSomeTime(3000, "moved to new page :");
                CurrURL = driver.getCurrentUrl(); // driver navigation is 100%, assert not required
                System.out.println("moved to new page :: " + CurrURL);

                //--------------------------------------------------------------------------------------------------
                // Start BUNDLE TEST HERE :: Check next Page type :: bundle ,package ,supplier or cancel next
                //--------------------------------------------------------------------------------------------------
                boolean continueRepeatLoop = true;
                boolean continueCardSelection = false;

                // DBG: Report the next type of page
                reportMajorActivity("Testing the next page type.");
                reportBundleTest(true);

                // nestedIF to determine the type of page in the next step.
                if ((CurrURL.contains("bundle")) || CurrURL.contains("package")) {
                    reportMajorActivity("-->Expected Next page is : Bundle or package :: Skipping Card Selection.");
                } else {
                    if (CurrURL.contains("supplier")) {
                        reportMajorActivity("-->Selecting A Card from Cards list :: expected next page is : supplier.");
                        continueCardSelection = true;
                    } else {
                        reportMajorActivity("-->Cannot locate next step Items :: Must Goto end of RepeatLoop.");
                        continueRepeatLoop = false;
                    }
                }// end if-else bundle

                //--------------------------------------------------------------------------------------------------
                // conditional continue - depends on the page type && if list was found
                //--------------------------------------------------------------------------------------------------
                if (continueRepeatLoop) {
                    if (continueCardSelection) {
                        // ----------------------------------
                        selectACardUntilFound();
                        // ----------------------------------
                    }// end if cardSelection
                        //-----------------------------------
                        doFinalPageSelections();
                        //-----------------------------------
                }// end if ContinueRepeatLoop
                //----------------------------------------------------------------------------
                if (repeatTest < NumOfTests) {
                    waitSomeTime(4000, "Test " + repeatTest + " Complete! Wait before next Test, out of " + NumOfTests);
                } // end if
                //----------------------------------------------------------------------------

            } else {
                reportMajorActivity("Some Error happened during Selection from Categories.");
            } // end if category selection complete

            //----------------------------------------------------------------------------
            //                reportMajorActivity("DBG:: -->Press Any Key to Continue....");
            //                int inputData = System.in.read();
            //----------------------------------------------------------------------------

        }// end repeat tests - first while loop

        //----------------------------------------------------------------------------
        // Finally DO:
        reportMajorActivity("      --- End Test Sequence ---");
        waitSomeTime(5000, "Test "+repeatTest+" Complete! Finished "+repeatTest+"Tests, out of "+NumOfTests);
        driver.close();
        reportMajorActivity("Driver Closed successfully");
        //----------------------------------------------------------------------------
    }// end Page4 find by

    public boolean assertText (String Expected, String Actual) {

        try {
            Assert.assertEquals(Expected, Actual);
            reportMajorActivity("Assert.equals "+Expected+"=="+Actual+" :: true");
            return true;

        } catch (Exception e) {
            reportMajorActivity("Error in Assert Text :: "+e.getMessage());
            return false;
        }
    }

    public void waitSomeTime(int milsec, String reason) {
        //Actions action = new Actions(driver);

        try {
            Thread.sleep(milsec);
            System.out.println("Waiting for " + milsec + "ml wait time :: " + reason);
            //return true;
        } catch (InterruptedException e) {
            System.out.println("Error waiting");
            e.printStackTrace();
            //return false;
        }
    }
//------------------------------------------------------------------------------------------------------------------------
//  Helper Methods
//------------------------------------------------------------------------------------------------------------------------

    private boolean selectFromCategories (String StartingURL, String ByTextClassName, int repeatTest, int NumOfTests) {
        int foundListSize;
        List<WebElement> insideDivElements;

        do {
            navigateDriverTo(StartingURL);
            reportMajorActivity("Starting Test "+repeatTest+" out of "+NumOfTests);

            // CategorySelection
            int budget = selectAndclickMenuItem("סכום", ByTextClassName);
            int region = selectAndclickMenuItem("אזור", ByTextClassName);
            int category = selectAndclickMenuItem("קטגוריה", ByTextClassName);

            StringBuilder newURL = new StringBuilder(); // build the new URL for Navigation after the search
            newURL.append("https://buyme.co.il/search?budget=").append(budget)
                    .append("&category=").append(category)
                    .append("&region=").append(region);
            System.out.println("Default URL to navigate to :" + newURL.toString());

            // Click The Button
            By ByParLink = new By.ByPartialLinkText("תמצאו");

            if(waitAndClickElement(WebDriverInstance.getInstance().findElement(ByParLink))) {
                System.out.println("waitAndClickElement ::"+ByParLink.toString()+" Clicked the Category Button");
            }
            else {
                System.out.println("Error Clicking Category Button  ---> Cannot Continue Tests. Reset required.");
                return false; // end loop here
            }// If Button Clicked  - continue

            System.out.println("Driver Navigation Complete :: Current URL :: " + reportDriverNavigation());
            reportMajorActivity("Page 4 Complete -> reading results list before next step");

            // ---------------------------------------------------------------------------------------
            // Page 5 - Get Cards list = may be empty -> repeat random selection until list has values
            // ---------------------------------------------------------------------------------------

            try {
                driver = TestUtils.WebDriverInstance.getInstance();
                CurrURL = driver.getCurrentUrl();

            } catch (Exception e) {
                System.out.println("Reactivating the driver with the selected choices.");
                navigateDriverTo(newURL.toString());

                System.out.println("driver.getCurrentUrl() " + driver.getCurrentUrl());

                clickMenuItem("סכום", ByTextClassName, budget);
                clickMenuItem("אזור", ByTextClassName, region);
                clickMenuItem("קטגוריה", ByTextClassName, category);
                WebDriverInstance.getInstance().findElement(ByParLink).click(); // Click The Button again!!!!!
                // wait until all elements show on screen
            } // driver have been reactivated with the new values, navigation continues

            waitSomeTime(3000, "Waiting for results to show on screen");
            reportMajorActivity("Clicked the button to get the search results on screen :: " + ByParLink.toString());

            //------------------------------------------------------------------------------------------
            // continue test to select one option from found list if current page is not a bundle
            //------------------------------------------------------------------------------------------

            By BySubMethod = By.className("card-items");
            By Tag_a = new By.ByTagName("a");
            try {
                WebElement DivCardItems = driver.findElement(BySubMethod);
                reportMajorActivity("Checking Found Div Element for Links :: " + DivCardItems.getTagName());
                System.out.println("" + DivCardItems.getText());
                reportMajorActivity("Getting Links List -> reading results after URL update");

                //page-title
                waitSomeTime(1000,"Before Scroll Down");
                WebElement PageTitle = driver.findElement(By.className("page-title"));

                reportMajorActivity("Scrolling down Page "+PageTitle.getText());
                scrollDownToView(DivCardItems);
                scrollDownToView(DivCardItems);
                scrollDownToView();

                waitSomeTime(2000,"After Scroll Down");

                List<WebElement> testList = DivCardItems.findElements(By.className("thumbnail"));
                reportMajorActivity("Test List Size is "+testList.size());
                System.out.println("--> PageTitle :: "+ PageTitle.getText());
                insideDivElements = findElementsIn(DivCardItems, Tag_a);
                foundListSize = insideDivElements.size();

                reportMajorActivity("Size :: List of found links ::" + insideDivElements.size());
                if (foundListSize>0)
                {
                    int randSelection = generateRandom(0, insideDivElements.size());
                    System.out.println("Selected Item #" + randSelection + " text::" + insideDivElements.get(randSelection).getText() + " -->TagName::" + insideDivElements.get(randSelection).getTagName());
                    waitAndClickElement(insideDivElements.get(randSelection));//.click();
                    reportMajorActivity("Clicked Selected Item #" + randSelection + " " + insideDivElements.get(randSelection).getText());
                }

            } catch (Exception e) {
                System.out.println("Objects were not found :: "+e.getMessage());
                foundListSize = 0;
                waitSomeTime(1000,"Wait to restart the loop :: REASON: list is empty");
            }
        } while (foundListSize == 0);  // repeat action until found list has elements

        return true;
    } // end selectFromCategories

    private void navigateDriverTo(String url) {
        driver= WebDriverInstance.getInstance();
        String BeforeURL = driver.getCurrentUrl();
        driver.get(url);
        driver.navigate();
        //System.out.println("Driver Navigated to :: "+reportDriverNavigation());
        //System.out.println("Driver Navigation Complete :: Current URL :: " + reportDriverNavigation(BeforeURL));
        waitSomeTime(4000, "Active URL :: "+url);
    }

    private String reportDriverNavigation(String beforeURL){
        String afterURL = beforeURL;
        System.out.println("--> TestUtils.WebDriverInstance.getInstance() BEFORE :: " + beforeURL);
        do {
            waitSomeTime(100, "Waiting for URL update...");
            afterURL = WebDriverInstance.getInstance().getCurrentUrl();
        } while (beforeURL.equals(afterURL));

        return afterURL;
    }
    private String reportDriverNavigation(){
        return reportDriverNavigation(WebDriverInstance.getInstance().getCurrentUrl());
    }

    private void selectACardUntilFound () {
        // ----------------------------------
        // select a card loop until found
        // ----------------------------------
        boolean RepeatSelectACard;
        do {
            driver = TestUtils.WebDriverInstance.getInstance();
            CurrURL = driver.getCurrentUrl();
            WebElement CardsEl = driver.findElement(new By.ByClassName("card"));
            scrollDownToView(CardsEl);
            scrollDownToView(CardsEl);
            scrollDownToView();
            //-------------------------------------------------------------------------------------------
            reportMajorActivity("select a Card, Click Selected");
            waitSomeTime(2000, "Wait for scroll down");
            //-------------------------------------------------------------------------------------------
            List<WebElement> cards = driver.findElements(new By.ByClassName("card"));
            System.out.println("Cards List Size :: " + cards.size());
            //-------------------------------------------------------------------------------------------
            // Find the manual gift card
            //-------------------------------------------------------------------------------------------
            WebElement relevantBtn = null;

            int CardsIndex = 0;
            int foundCard = -1;

            // checks first 6 links
            for (WebElement card : cards) {

                WebElement Btn = card.findElement(new By.ByClassName("btn")); //(new By.ByPartialLinkText("לבחירה"));
                System.out.println("Button Text : " + Btn.getText() + " :: TagName : " + Btn.getTagName());

                if (Btn.getTagName().startsWith("button")) {

                    foundCard = CardsIndex;
                    relevantBtn = Btn;
                }
                CardsIndex++;
            }// end foreach loop
            //-----------------------------------------------------------------------------------------------

            reportMajorActivity("Found " + CardsIndex + " Items.");
            RepeatSelectACard = false;

            if (foundCard < 0) {
                reportMajorActivity("Manual Card was not found. Selecting a random card instead.");
                // choose random card and press select
                relevantBtn = cards.get(generateRandom(0, cards.size())).findElement(new By.ByTagName("a"));

            } else {

                reportMajorActivity("Manual Gift Card is item #" + foundCard);

                WebElement inputField = cards.get(foundCard).findElement(new By.ByTagName("input"));
                int sum = generateRandom(1, 5) * 100;
                inputField.sendKeys(String.valueOf(sum));
                System.out.println("Sent Keys to inputField in Cards sum=" + sum);
            }

            if (!waitAndClickElement(relevantBtn)) {
                reportMajorActivity("Error in Cards.card.relevantButton " + relevantBtn.getTagName());
                System.out.println("Try A Different Card - RepeatSelectACard = true;");
                RepeatSelectACard = true;
            }
        } while (RepeatSelectACard);
        reportMajorActivity("Finished Card Selection - starting Final Page Selections.");
    }// end card selection

    private void doFinalPageSelections () {
        //----------------------------------------------------------------------------
        waitSomeTime(3000, "moved to new page - accept User information");
        //----------------------------------------------------------------------------
        driver = TestUtils.WebDriverInstance.getInstance();
        CurrURL = driver.getCurrentUrl(); // driver navigation is 100%, assert not required
        reportMajorActivity("driver navigation  :: "+CurrURL);
        WebElement title = driver.findElement(new By.ByClassName("page-title"));
        System.out.println("Current Page Title is :: "+title.getText());

        //----------------------------------------------------------------------------------------------------
        // make final page selections
        //----------------------------------------------------------------------------------------------------
        // radio Button - someone else    .ui-radio
        WebElement RadioBtn1 = driver.findElement(new By.ByClassName("selected"));
        System.out.println("Radio Button Selection is :" + RadioBtn1.getText());
        RadioBtn1.click();

        // enter receiver name
        String to = "Mr To Guy";

        WebElement InputFieldToPerson = driver.findElement(RelativeLocator.withTagName("input").below(RadioBtn1));
        InputFieldToPerson.sendKeys(to);

        waitSomeTime(2000,"Wait for Name Field Update");
        String actualToPerson = InputFieldToPerson.getAttribute("value");
        //String actualToPerson = GetTextFromInputElBelow(InputFieldToPerson);

        if (assertText(to,actualToPerson)) {
            System.out.println("-->Passed Assert Receiver Name Test");
        }
        else
        {
            reportMajorActivity("Expected="+to+"  :: actual="+actualToPerson);
        }

        WebElement inputFrom = driver.findElement(RelativeLocator.withTagName("input").below(InputFieldToPerson));
        //String fromName = GetTextFromInputElBelow(inputFrom);
        String fromName = inputFrom.getAttribute("value");
        //reportMajorActivity("Found Text in input field From :: "+fromName);
        fromName = fromName.replaceAll("\\s+","");
        String ExpectedName = "קובי";

        if (assertText(ExpectedName,fromName)) {
            //------------------------------------------------------------------------------------
            System.out.println("-->Passed Assert Sender Name Test");
        }
        else
        {
            reportMajorActivity("Expected=קובי  but actual="+fromName);
        }
        // pick event from drop down.
        WebElement EventMenu = WebDriverInstance.getInstance().findElement(new By.ByPartialLinkText("לאיזה אירוע?"));
        EventMenu.click();
        System.out.println(EventMenu.getText());
        System.out.println("Clicked on Drop Down Event");
        List<WebElement> EventMenuItems = driver.findElements(By.className("active-result"));
        int menuItemIndex = 0;
        List<Integer> MenuOptions = new ArrayList<>();

        for (WebElement menuItem : EventMenuItems) {
            if (menuItem.getText().startsWith("יום הולדת")) {
                MenuOptions.add(menuItemIndex);
                System.out.println("birthday item is #" + menuItemIndex);

            } else {
                if (menuItem.getText().startsWith("חתונה")) {
                    System.out.println("Wedding Item is #" + menuItemIndex);
                    MenuOptions.add(menuItemIndex);
                } // end inner if
            } // end else
            menuItemIndex++;
        }

        waitSomeTime(500, "wait BEFORE click for menu items to update ::");
        EventMenuItems.get(MenuOptions.get(generateRandom(0, MenuOptions.size()))).click();
        waitSomeTime(500, "wait After click for menu items to update ::");

        WebElement upload = driver.findElement(By.name("fileUpload"));
        String imgPathLocal = "C:\\Users\\MD\\Pictures\\Saved Pictures\\pretty sun – חיפוש Google_files\\29cb9550cf608f571fffc5487292931b.jpg";

        waitSomeTime(1000, "waiting for image Element");
        upload.sendKeys(imgPathLocal); // UPDATE VAR TO YOUR LOCAL IMAGE PATH

        WebElement dib = WebDriverInstance.getInstance().findElement(new By.ByClassName("sending-methods"));
        System.out.println(dib.getTagName()+ " ::  Dib element.getText() :: "+dib.getText());
        List<WebElement> dibButtons = dib.findElements(new By.ByTagName("button"));
        System.out.println("dibButtons List Size is :: "+dibButtons.size());

        int email = 0;
        int sms = 0;
        int SelectionIndex = 0;
        for (WebElement btn : dibButtons) {

            if (btn.getText().startsWith("במייל"))
            {
                email=SelectionIndex;

            } else if (btn.getText().startsWith("ב-SMS"))
            {
                sms=SelectionIndex;
            }
            // end if

            SelectionIndex++;
        } // end for each loop - Select random

        int randSelection = generateRandom(0,2);

        if (randSelection == 0) //email:: randSelection == 0
        {
            reportMajorActivity("do email :: Button Text is ::"+dibButtons.get(email).getText());
            dibButtons.get(email).click();
            waitSomeTime(2500, "Wait for other fields to show");

            WebElement inputEmail = driver.findElement(By.cssSelector("input[type=email]")); // By.ById("ember2042"));
            inputEmail.sendKeys("email@email.com");
            findClickSaveButtonBellow(inputEmail);

        } else {//sms :: randSelection == 1
            reportMajorActivity("do sms :: Button Text is ::"+dibButtons.get(sms).getText());
            dibButtons.get(sms).click();
            waitSomeTime(2500, "Wait for Tel Input fields to show");
            List<WebElement> Tel = WebDriverInstance.getInstance().findElements(By.cssSelector("input[type=tel]"));
            System.out.println("Tel Fields List Size "+Tel.size());
            waitSomeTime(1300,"Wait until Tels Field(0) to update");
            Tel.get(0).sendKeys("0541234567");
            waitSomeTime(1300,"Wait until Tels Field(1) to update");
            Tel.get(1).sendKeys("0547654321");
            findClickSaveButtonBellow(Tel.get(0));
        } // end else do sms
        reportMajorActivity("Final Selections Completed.");
    }// end finalPageSelections

    private void reportBundleTest (boolean show) {
        if (show) {
            System.out.println("Search index of bundle   : " + CurrURL.indexOf("bundle"));
            System.out.println("Search index of supplier : " + CurrURL.indexOf("supplier"));
            System.out.println("Search index of package  : " + CurrURL.indexOf("package"));
        }
    }

    private int generateRandom (int min, int max) {
        Random rnd = new Random();
        return (rnd.nextInt(max-min)+min);
    }

    private void reportMajorActivity (String text) {
        System.out.println("_______________________________________________________");
        System.out.println(text);
        System.out.println("_______________________________________________________");
    }

    private void scrollDownToView() {
        driver = TestUtils.WebDriverInstance.getInstance();
        CurrURL = driver.getCurrentUrl();
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        String scrollToBottom = "window.scrollBy(0,document.body.scrollHeight)";
        jse.executeScript(scrollToBottom);
    }

    private void scrollDownToView(WebElement element) {
        driver = TestUtils.WebDriverInstance.getInstance();
        CurrURL = driver.getCurrentUrl();
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        String scrollViewElements = "arguments[0].scrollIntoView();";
        jse.executeScript(scrollViewElements,element);
    }

    private int selectAndclickMenuItem(String partialLink, String ClassNameToFind) {
        FindAndClick fc = new FindAndClick();
        fc.clickByPartialLink(partialLink);
        // "active-result"
        int SizeOfSum = WebDriverInstance.getInstance().findElements(By.className(ClassNameToFind)).size();

        System.out.println("Sizeof element list is "+SizeOfSum);
        int ItemSelection = this.generateRandom(1,SizeOfSum);
        WebElement SelectedEl= WebDriverInstance.getInstance().findElements(By.className(ClassNameToFind)).get(ItemSelection);

        System.out.println("SELECTED ITEM => "+ItemSelection+" "+SelectedEl.getText());
        waitAndClickElement(SelectedEl);
        return ItemSelection;
    }

    private boolean waitAndClickElement (WebElement el) {
        boolean WaitForButton;
        int tries = 5;
        int ActualAttempts = 0;
        int addedTime = 1000;
        do {
            // if error repeats - exit process and go to start
            try {
                String msg = "Trying to Click :: "+el.getTagName()+" :: Attempt "+ActualAttempts+" out of "+tries;
                waitSomeTime(addedTime, msg);
                el.click();
                WaitForButton=false;
            }catch (Exception e) {
                reportMajorActivity("Error in Cards Selected Button :: "+e.getMessage());
                WaitForButton=true;
                addedTime+=1000;
                ActualAttempts++;
            }

        } while (WaitForButton && ActualAttempts<tries);

        if (WaitForButton) { reportMajorActivity("Error in Cards.card.relevantButton "+el.getTagName());
            return false;
        }
        return true;
    }

    private void clickMenuItem(String partialLink, String ClassNameToFind, int index) {
        FindAndClick fc = new FindAndClick();
        fc.clickByPartialLink(partialLink);
        int SizeOfSum = WebDriverInstance.getInstance().findElements(By.className(ClassNameToFind)).size();
        System.out.println("List Sizeof element "+partialLink+" is "+SizeOfSum);
        WebElement SelectedEl= WebDriverInstance.getInstance().findElements(By.className(ClassNameToFind)).get(index);
        reportMajorActivity("SELECTED ITEM => "+index+" "+SelectedEl.getText()); //" Element Text is "+we.getText()
        SelectedEl.click();
    }

    private List<WebElement> findElementsIn(WebElement TempDriver,By byMethod) {
        return TempDriver.findElements(byMethod);
    }

    private void findClickSaveButtonBellow (WebElement el){

        List<WebElement> btnsBelow = WebDriverInstance.getInstance().findElements(RelativeLocator.withTagName("button").below(el));

        waitSomeTime(1000,"Avoiding e=stale element reference:: Below WE="+el.getTagName());
        for(int i=0;i<btnsBelow.size();i++){ // find the save button
            System.out.println(btnsBelow.get(i).getText());
            if (btnsBelow.get(i).getText().contains("שמירה")) {
                btnsBelow.get(i).click();
                reportMajorActivity("-->Clicked Save Button (index "+i+") :: ");
                break;
            } // end if
        }//
    }// end find
} // end class FindElements
