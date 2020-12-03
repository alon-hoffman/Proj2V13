package TestOps;

import org.openqa.selenium.By;

public class Page2LoginOrRegister {

    public void clickedRegister() {
        clickButtonBYClassName("text-btn");
    }
    public void clickButtonBYClassName(String ButtonClassName) {

        FindAndClick findAndClick= new FindAndClick();
        findAndClick.clickElement(By.className(ButtonClassName));

    }
}
