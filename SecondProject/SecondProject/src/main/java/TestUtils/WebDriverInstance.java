package TestUtils;

import org.openqa.selenium.chrome.ChromeDriver;

public class WebDriverInstance {

private static ChromeDriver instance = null;

private WebDriverInstance() { }

    public static ChromeDriver getInstance() {
        if (instance == null) {
            instance = new ChromeDriver();
        }
        return instance;
    }
}

