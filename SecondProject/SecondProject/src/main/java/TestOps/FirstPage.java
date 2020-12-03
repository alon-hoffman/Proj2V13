package TestOps;

import org.openqa.selenium.By;

public class FirstPage {

    public void clickToRegister() {
        clickToRegister("seperator-link");
    }
    public void clickToRegister(String clsName) {
        FindAndClick findAndClick = new FindAndClick();
        findAndClick.clickElement(By.className(clsName));
    }
}
