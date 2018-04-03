package AosomST;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.junit.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.*;



public class SmokeTest extends TestCase {
	WebDriver wb;
	
	String log4jConfPath = "E:\\workspace\\AosomChrome\\src\\log4j.properties";
	
	Logger log = Logger.getLogger("SmokeTestclass");
	
	private static String watchedLog ="";
	
	public SmokeTest(String name){
		
		super(name);
	}
	
	
	@Rule
	public TestWatcher watchman = new TestWatcher(){
		
	  @Override
      public Statement apply(Statement base, Description description) {
		  return super.apply(base, description);
		  }
		
		
	   @Override
	   protected void failed(Throwable e,Description description){
		   watchedLog += description.getDisplayName()+""+e.getClass().getSimpleName()+e.getMessage() +"\r\n";
		   System.out.println(watchedLog);
		   log.debug(watchedLog);

	   }
		
	   
	   @Override
	   protected void succeeded(Description description){
		   watchedLog += description + ""+ "success! \n";
		   System.out.println(watchedLog);
		   log.debug(watchedLog);
	   }
	   
	   
	   @Override
	   protected void starting(Description description){
		   super.starting(description);
		   System.out.println("starting");
		   log.debug("Starting");
	   }
	   
	   @Override
	   protected void finished(Description description){
		   super.finished(description);
		   System.out.println("finished");
		   log.debug("Finished");
	   }
		
	};

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		
		System.setProperty("webdriver.chrome.driver", "E:\\chromedriver_win32\\chromedriver.exe");
		PropertyConfigurator.configure(log4jConfPath);
		this.wb = new ChromeDriver();
	}

	@After
	public void tearDown() throws Exception {
		this.wb.quit();
	}
    

	
	@Test
	public void testCALoginPayPal (){
		
		//PropertyConfigurator.configure(log4jConfPath);
		long pageLoadTimeout = 9000 ;
		int j =1;
		String url ="https://www.aosom.ca";
		String LoginEmail= "shenzhe1642_cn@163.com";
		String LoginPwd = "111111";
		CharSequence countrycs ="Canada";
		
		String prdName ="sport";
		/*String prdtTitle;
		String prdPrice;
		String prdShippingFee;
		String prdPriceNum ="";
		double prdShipFeeValue = 0.00;
		double prdPriceValue;	*/
		String cartTotalPrice;
		String TaxPrice;
		String cartGrandTotal;
		double cartPrdPriceValue,cartTotalPriceValue;
		double taxRate = 0.05;
		double taxPriceValue;
		double GrandTotalValue;
	
				
		Map<String, String> prdsAttr = new HashMap<String, String>();
				
		this.wb.manage().timeouts().implicitlyWait(pageLoadTimeout,TimeUnit.MILLISECONDS);
		
		this.wb.get(url);
		log.debug("Start browser to website: " +url);
		
		//new WebDriverWait(this.wb,pageLoadTimeout).until(webDriver->((JavascriptExecutor)this.wb).executeScript("return document.readyState").equals("complete"));
				
		this.wb.manage().window().maximize();
		log.debug("Maximize browser window");
		
		//user login
		boolean loginResult = GeneralOper.userLoginFromHeader(LoginEmail, LoginPwd,this.wb);
		//Assert.assertTrue("----User "+LoginEmail+" login failed!",loginResult);
		
		//empthCart
		boolean emptyCartResult = GeneralOper.emptyCart(this.wb);
		Assert.assertTrue("----Empty Shopping cart failed !",emptyCartResult);
		
		//user logout
		GeneralOper.userLogoutFromHeader(this.wb);
		
		//add product by searching product Name
		prdsAttr=GeneralOper.addProductBySearch(prdName, prdsAttr, this.wb);
		
		//add product by product detail page
		prdsAttr=GeneralOper.addTwoProductsByProductPage(prdName, prdsAttr, this.wb);
			
				
		//switch to shopping cart
		this.wb.findElement(By.xpath("//div[@class='alertcontent']//a[contains(@class,'cart')]")).click();
		log.debug("clicking on Add to cart button");
		
		//increase quantity of first product in cart
		GeneralOper.increaseQuantityOfFirPrd(this.wb);
		
		//login on shopping cart page
		GeneralOper.userLoginFromCart(LoginEmail, LoginPwd, this.wb);
		Assert.assertTrue("--Login failed", GeneralOper.isUserLogged(this.wb));
		
		//take screenshot for shopping cart
		takeScreenShot.takeScreenShot(this.wb);
		
		//move to wishlist after login
		GeneralOper.moveWishList(this.wb);
		
		//get shopping cart table data of all products
		cartPrdPriceValue = GeneralOper.subtotalPriceCart(prdsAttr, this.wb);
				
		//Verify order sub total  fee
		cartTotalPrice= this.wb.findElement(By.xpath("//table[@id='shopping-cart-totals-table']/tbody/tr[1]/td[2]/span[contains(@class,'price')]")).getText();
	    cartTotalPriceValue = GeneralOper.getDoublePriceValue(cartTotalPrice);
	    
	    System.out.println(cartPrdPriceValue+ "-------"+cartTotalPriceValue);  	
	    Assert.assertTrue("shopping cart subtotal fee doesn't equal to form value", cartTotalPriceValue ==cartPrdPriceValue);
	    
		//Verify order tax fee
	    TaxPrice= this.wb.findElement(By.xpath("//table[@id='shopping-cart-totals-table']/tbody/tr[3]/td[2]/span[contains(@class,'price')]")).getText();
	    taxPriceValue = GeneralOper.getDoublePriceValue(TaxPrice);
	    
	    System.out.println(taxPriceValue+ "-------"+ GeneralOper.taxValue(taxRate, cartPrdPriceValue));
	    Assert.assertTrue("shopping cart Tax fee doesn't equal to form value", taxPriceValue ==GeneralOper.taxValue(taxRate, cartPrdPriceValue));
	    
	   //Verify order Grand total fee
	    cartGrandTotal = this.wb.findElement(By.xpath("//table[@id='shopping-cart-totals-table']/tfoot/tr/td[2]/strong/span[contains(@class,'price')]")).getText();
	    GrandTotalValue = GeneralOper.getDoublePriceValue(cartGrandTotal);
	   
	    System.out.println(GrandTotalValue+ "-------"+ (taxPriceValue+cartTotalPriceValue));
	    Assert.assertTrue("shopping cart Grand total fee doesn't equal to form value", GrandTotalValue ==(taxPriceValue+cartTotalPriceValue));
	    
		//switch to checkout page
		WebElement chkOutEle = this.wb.findElement(By.xpath("//button[@title='Proceed to Checkout']"));
		chkOutEle.click();
		log.debug("click on Show cart button on adding successful ajax window");
	    
		Assert.assertTrue("Verdict checkout page url failed",this.wb.getCurrentUrl().equals("https://www.aosom.ca/onepage/"));
			
		/*user logged in, so no need to input billing information
		this.wb.findElement(By.xpath("//input[@id='billing:firstname']")).sendKeys(BillingFirstName);
		this.wb.findElement(By.xpath("//input[@id='billing:telephone']")).sendKeys(BillingTelephone);
		this.wb.findElement(By.xpath("//input[@id='billing:postcode']")).sendKeys(BillingZip);
		this.wb.findElement(By.xpath("//input[@id='billing:city']")).sendKeys(BillingCity);
		this.wb.findElement(By.xpath("//input[@id='billing:street1']")).sendKeys(BillingAddr);
		this.wb.findElement(By.xpath("//input[@id='billing:lastname']")).sendKeys(BillingLastName);
		this.wb.findElement(By.xpath("//input[@id='billing:email']")).sendKeys(BillingEmailAddr);
		log.debug("Filling information for buyer");*/
		
		//select payment for PayPalCheckout
	    WebElement PaypalRadioBtn =this.wb.findElement(By.xpath("//input[@id='p_method_paypal_express']"));  
	    PaypalRadioBtn.click();
	    log.debug("Selected on radio button of Payment method");
	    
	    //place order
	    this.wb.findElement(By.id("iwd_opc_place_order_button")).click();
	    
	    try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    WebDriverWait wait = new WebDriverWait(this.wb,60); 
	    log.debug("Switching to paypal webpage");
	    //wait.until(ExpectedConditions.visibilityOf(this.wb.findElement(By.xpath("//*[@id='paypalLogo']")))//p[@class ='paypal-logo paypal-logo-long']|);
	    
	    String country= wait.until(new ExpectedCondition<WebElement>(){  
            @Override  
            public WebElement apply(WebDriver d) {  
                return d.findElement(By.xpath("//div[@id='countryList']//span[@class='custom-select']/span/span"));  
            }
            }).getText();
	    
	    takeScreenShot.takeScreenShot(this.wb);
	    
	    Assert.assertTrue("----PayPal country is not Canada-----",country.contains(countrycs));

	}
	


}
