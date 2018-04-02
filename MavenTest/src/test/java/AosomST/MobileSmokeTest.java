package AosomST;

import static org.junit.Assert.*;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.remote.DesiredCapabilities;
import io.appium.java_client.AppiumDriver;

public class MobileSmokeTest {
	AppiumDriver<WebElement> driver;

	String log4jConfPath = "E:\\workspace\\MavenTest\\src\\src\\log4j.properties";
	
	Logger log = Logger.getLogger("MobileSmokeTestclass");
	
	@Before
	public void setUp() throws Exception {
		
		DesiredCapabilities capabilities = new DesiredCapabilities();
		
		capabilities.setCapability("browserName", "Chrome");
		capabilities.setCapability("platformName", "Android");	
		capabilities.setCapability("deviceName", "Android");
		capabilities.setCapability("platformVersion", "4.4.4");
		capabilities.setCapability("udid","235f94b4");

		log.debug("setup appium driver capability");
		
	    try{
	    	driver = new AppiumDriver<WebElement>(new URL("http://127.0.0.1:4723/wd/hub"),capabilities);
	    }
	    catch(Exception e){
    		e.getStackTrace();
    		System.out.println(e.toString());
    		
    	}
	    driver.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
		System.out.println("Initializing finished");
		log.debug("Initializing finished.");

	}
	
	

	@After
	public void tearDown() throws Exception {
		
		driver.quit();
		log.debug("Test finished.");
	}

	@Test
	public void test() {
		
System.out.println("Starting JUNIT test case");
		
		String productName ="sport";
		String url ="https://www.aosom.co.uk";
		
		String BillingFirstName ="Laura";
		String BillingLastName ="Cen";
		String BillingCompany ="ABB De.Gmbh";
		String BillingEmailAddr = "shenzhe1642_cn@163.com";
		String BillingAddr ="testForLaura";
		String BillingCity ="London";
		String BillingState ="Great London";
		String BillingZip ="UB6 7RH";
		String BillingTelephone = "08002404004";
		String BillingFax = "08002404404";
		
		log.debug("Opening UK site");
		//this.driver.get(url);
		this.driver.navigate().to(url);
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		log.debug("Open UK site "+this.driver.getCurrentUrl() + " succssfully");
		System.out.println("Opened UK site "+this.driver.getCurrentUrl() + " succssfully");
				
		WebElement searchEle= this.driver.findElement(By.name("q"));
		log.debug("clicking on Search button");
		searchEle.sendKeys(productName);
		searchEle.submit();
		
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		takeScreenShot.takeScreenShot(driver);
		
		//add to cart
		this.driver.findElement(By.xpath("//*[@id='divforcomparing0']/div[2]/div[3]//*[@title='Add to Cart']")).click();
		log.debug("clicking first product from searching result on adding to cart");
				
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		takeScreenShot.takeScreenShot(driver);
		
		//proceed to checkout
			this.driver.findElement(By.xpath("//*[@title='Proceed to Checkout']")).click();
		log.debug("clicking on proceed to checkout on shopping cart page");
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.driver.findElement(By.xpath("//input[@id='billing:firstname']")).sendKeys(BillingFirstName);
		this.driver.findElement(By.xpath("//input[@id='billing:lastname']")).sendKeys(BillingLastName);
		this.driver.findElement(By.xpath("//input[@id='billing:company']")).sendKeys(BillingCompany);
		this.driver.findElement(By.xpath("//input[@id='billing:email']")).sendKeys(BillingEmailAddr);
		this.driver.findElement(By.xpath("//input[@id='billing:street1']")).sendKeys(BillingAddr);
		this.driver.findElement(By.xpath("//input[@id='billing:city']")).sendKeys(BillingCity);
		this.driver.findElement(By.xpath("//input[@id='billing:region']")).sendKeys(BillingState);
		this.driver.findElement(By.xpath("//input[@id='billing:postcode']")).sendKeys(BillingZip);
		this.driver.findElement(By.xpath("//input[@id='billing:telephone']")).sendKeys(BillingTelephone);
		this.driver.findElement(By.xpath("//input[@id='billing:fax']")).sendKeys(BillingFax);
		
		 log.debug("Filling information for billing");
		
		 if(this.driver.findElement(By.xpath("//input[@id='billing:use_for_shipping_yes']")).isSelected() == false){
			
			this.driver.findElement(By.xpath("//input[@id='billing:use_for_shipping_yes']")).click();
		 }
		
		 try {
			Thread.sleep(8000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
		 
	    this.driver.hideKeyboard();
	   
		
	    
	    WebElement payMethodEle = this.driver.findElement(By.id("p_method_paypal_express"));
	    
	    System.out.println("swiping to element: " + payMethodEle.isSelected());
		if(payMethodEle.isDisplayed()==true){
			payMethodEle.click();
		}
	    
	    try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	 
	       
	    //submit checkout review
	    WebElement plcOrdBtn= this.driver.findElement(By.xpath("//*[@id='checkout-review-submit']//*[@type='button']"));
	    plcOrdBtn.sendKeys(Keys.ENTER);
	    log.debug("clicking on Place Order button");

	    
	    try {
			Thread.sleep(18000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    log.debug("Switching to paypal webpage");
	    
	    if(this.driver.findElement(By.xpath("//*[@id='paypalLogo']")).isDisplayed()){
	    	 log.debug("Switch to PayPal webpage successfully");
	    	
	    }
	    System.out.println("taking screenshot");
	    takeScreenShot.takeScreenShot(driver);
	
	}

}

