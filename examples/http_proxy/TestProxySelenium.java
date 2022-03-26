import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


public class TestProxySelenium {
    public static void main(String[] args) throws InterruptedException {
        // 目标网站
        String targetUrl = "https://dev.kdlapi.com/testproxy";

        // 代理ip: port
        String proxyServer = "59.38.241.25:23916";

        // 创建webdriver驱动，设置代理
        System.setProperty("webdriver.chrome.driver", "${chromedriver_path}"); // webdriver驱动路径
        Proxy proxy = new Proxy().setHttpProxy(proxyServer).setSslProxy(proxyServer);
        ChromeOptions options = new ChromeOptions();
        options.setProxy(proxy);
        WebDriver driver = new ChromeDriver(options);

        // 发起请求
        driver.get(targetUrl);
        WebElement element = driver.findElement(By.xpath("/html"));
        String resText = element.getText().toString();
        System.out.println(resText);
        Thread.sleep(3000);

        // 关闭webdriver
        driver.quit();
    }
}