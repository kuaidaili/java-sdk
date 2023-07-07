package org.example;
import com.microsoft.playwright.*;

public class App {
    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch();
            BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                                     .setProxy("http://ip:port"));
            Page page = context.newPage();
            Response response = page.navigate("https://dev.kdlapi.com/testproxy");
            System.out.println("响应为：" + response.text());
        }
    }
}
