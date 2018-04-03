package AosomST;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.*;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.log4j.Logger;
import org.junit.Assert;

public class GeneralOper {
	
		
	String log4jConfPath = "E:\\workspace\\AosomChrome\\src\\log4j.properties";
	
	static Logger logGen = Logger.getLogger("SmokeTestclass");
	//user logged in and turn back to home page of website
	public static Boolean userLoginFromHeader(String userEmail, String userPassword, WebDriver driver){
		
		String curURL;
		boolean verdictPass = true;
	//	String welcomeMsg = "Hello, Laura Cen!";
		
		Actions actions = new Actions(driver);
		long pageLoadTimeout = 6000;
		driver.manage().timeouts().implicitlyWait(pageLoadTimeout,TimeUnit.MILLISECONDS);
		
		Pattern loginURLPattern  = Pattern.compile("https://www.aosom(\\.\\w+)+/customer/account/login");
		Pattern loginResultURLPattern  = Pattern.compile("https://www.aosom(\\.\\w+)+/customer/account/");		
		
		try{
			
			WebElement AccountLoc = driver.findElement(By.xpath("//div[@class='header-topcontainer']//div[@class='toplinks dropdown']"));
			actions.moveToElement(AccountLoc).perform();
			
			List<WebElement> dropdownMenus = driver.findElements(By.xpath("//div[@class='header-topcontainer']//div[contains(@class,'dropdown-menu')]/div/ul/li"));
			WebElement loginMenu = dropdownMenus.get(3);
			loginMenu.click();
			
			curURL=driver.getCurrentUrl();
			Matcher urlMatcher = loginURLPattern.matcher(curURL);
			if(!urlMatcher.find()){
		    	verdictPass = false;
		    }
		    Assert.assertTrue("Open login page failed, login url is not correct",!urlMatcher.find());
		    
		    //input user email and pwd
			driver.findElement(By.xpath("//input[@name='login[username]']")).sendKeys(userEmail);
			driver.findElement(By.xpath("//input[@name='login[password]']")).sendKeys(userPassword);
			
			driver.findElement(By.id("send2")).click();
			
			//login result check
			curURL=driver.getCurrentUrl();
			urlMatcher = loginResultURLPattern.matcher(curURL);
			if(!urlMatcher.find()||!driver.findElement(By.xpath("//div[@class='welcome-msg']")).isDisplayed()){
				verdictPass = false;
			}
			Assert.assertTrue("---Login fail!",!urlMatcher.find()&&driver.findElement(By.xpath("//div[@class='welcome-msg']")).isDisplayed());
			
			//back to Home page
			driver.get(homeURL(driver));
			
			
		}
		catch (Exception e){
			verdictPass = false;
			e.printStackTrace();
		}
		
		return verdictPass;
		
	}
	
	//check user logged in or not
	public static Boolean isUserLogged(WebDriver driver){
		boolean isLogged = false;
		CharSequence cs= "Welcome, Laura Cen!";
		
		String welMsg = driver.findElement(By.xpath("//div[@class='header-topcontainer']//span[@class='wel-text']")).getText();
		
		if(welMsg.replaceAll("&nbsp", "").contains(cs)){
			isLogged = true;
		}
		
		return isLogged;
	}
	
	//user logged out and turn back to home page of website
	public static void userLogoutFromHeader(WebDriver driver){
		
		//check is logged in status
		Assert.assertTrue("--User is not logged in, please check it again!",isUserLogged(driver)==true);
		
		Actions actions = new Actions(driver);
		WebElement AccountLoc = driver.findElement(By.xpath("//div[@class='header-topcontainer']//div[@class='toplinks dropdown']"));
		actions.moveToElement(AccountLoc).perform();
		
		List<WebElement> dropdownMenus = driver.findElements(By.xpath("//div[@class='header-topcontainer']//div[contains(@class,'dropdown-menu')]/div/ul/li"));
		WebElement loginMenu = dropdownMenus.get(3);
		loginMenu.click();
		
		//back to Home page
		driver.get(homeURL(driver));
		
	}
	
	public static void userLoginFromCart(String userEmail, String userPassword, WebDriver driver){
		
		//check is logged out status
		Assert.assertTrue("--User is already logged in, please check it again!",isUserLogged(driver)==false);
		try{			
			int i=0;
			while(i++<3){
				driver.findElement(By.id("btn-cart-login")).click();
				Thread.sleep(1000);	
				
				if(driver.findElement(By.id("login-form")).isDisplayed()){
					
					driver.findElement(By.id("email")).sendKeys(userEmail);
					Thread.sleep(1000);
					driver.findElement(By.id("pass")).sendKeys(userPassword);
					Thread.sleep(1000);
					driver.findElement(By.xpath("//div[@id ='modal-login']//div[@id ='login-button-set']//button[@class='button btn-login']")).sendKeys(Keys.ENTER);
					Thread.sleep(1000);
					break;
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	
	//empty all products in shopping cart and back to home page
 	public static Boolean emptyCart(WebDriver driver){
		
		boolean verdictPass = false;
		String cartURL = homeURL(driver)+"/checkout/cart/";
		driver.get(cartURL);
		
		//Check cart empty page is displayed
		try{
			if(driver.findElement(By.xpath("//div[@class ='iwd_empty_page_title']")).isDisplayed()){
				return verdictPass = true;
			}
			
		}catch(Exception e){
			logGen.info("User cart is not empty, now we will clear all products in shopping cart.");
		}
			
		try{
			//get shopping cart table data of all products
			List<WebElement> allRows= driver.findElements(By.xpath("//table[@id='shopping-cart-table']/tbody/tr"));
			if(allRows.size()>1){
				int i =0;
				while(i++<3){
				try{
					driver.findElement(By.xpath("//button[(@title='Clear Shopping Cart') and (@type='button')]")).click();
					Thread.sleep(1000);	
					//confirm window
					//Alert alert= driver.switchTo().alert();
					if(driver.findElement(By.xpath("//div[@class='alertcontent']")).isDisplayed()){
						driver.findElement(By.xpath("//div[@class='alertcontent']//a[@class='comfirm_box_remove']")).click();						
						break;
						}
					}
				catch(Exception e){
					Thread.sleep(1000);	
					continue;
					}
				}
			}
		 else{
			try{
			driver.findElement(By.xpath("//table[@id='shopping-cart-table']/tbody/tr[1]/td[6]/a[2]")).click();
			}
			catch(Exception e){
				e.printStackTrace();
			}				
		}	
			
		//Check cart empty page is displayed
		if(driver.findElement(By.xpath("//div[@class ='iwd_empty_page_title']")).isDisplayed()){
			verdictPass = true;
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		//back to Home page
		driver.get(homeURL(driver));
		
		return verdictPass;
	}
	
 	//go to shopping cart
 	public static void showCart(WebDriver driver){
 		
 		Actions actions = new Actions(driver);
		WebElement AccountLoc = driver.findElement(By.xpath("//div[@class='header-maincontainer']//div[@class='shopping_cart dropdown']"));
		actions.moveToElement(AccountLoc).perform();
 		
		driver.findElement(By.xpath("//div[@class='header-maincontainer']//div[@class='shopping_cart dropdown']//"));
 	}
 	
	//turn back to home page of website
	public static String homeURL(WebDriver driver){
		String homeURL ="";
		String curURL=driver.getCurrentUrl();
		
		Pattern HomeURLPattern = Pattern.compile("https://www.aosom(\\.\\w+)+");
		
		Matcher urlMatcher = HomeURLPattern.matcher(curURL);
		
		if(urlMatcher.find()){
			homeURL = urlMatcher.group();
	    }
		return homeURL;
		
	}
	
	//convert string type of price into double type
   public static Double getDoublePriceValue(String priceOrig){
	   double priceValueDouble=0.00;
	   String priceValueString="";
	   //regex for price, like 21.99
	   Pattern pricePattern1 = Pattern.compile("\\d*\\.\\d*");
	   Pattern pricePattern2 = Pattern.compile("\\d*\\,\\d*");
	   
	   Matcher priceMatcher1 = pricePattern1.matcher(priceOrig);
	   Matcher priceMatcher2 = pricePattern2.matcher(priceOrig);
	   if(priceMatcher1.find())
		{
		   priceValueString = priceMatcher1.group();
		}
	   else if(priceMatcher2.find())
		{
		   priceValueString = priceMatcher2.group();
		}
	   
	   priceValueDouble= Double.parseDouble(priceValueString);
	   
	   return priceValueDouble;
   }
   
   //add product by Search
   public static Map<String,String> addProductBySearch(String prdName,Map<String,String>prdsAttr, WebDriver driver){
	   
	   String prdtTitle;
	   String prdPrice;
	   String prdShippingFee;
	   double prdShipFeeValue = 0.00;
	   double prdPriceValue;
	   int j=prdsAttr.size()/3;
	   
	  //search product
	   WebElement searchEle= driver.findElement(By.name("q"));
	   searchEle.clear();
	   logGen.debug("clicking on Search button");
	   searchEle.sendKeys(prdName);
	   searchEle.submit();
		
	 //scroll down page by 1000 pixel vertical to show product
	   JavascriptExecutor js = (JavascriptExecutor)driver;
	   js.executeScript("window.scrollBy(0,200)");
	  
	   try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	 //save first element attribute for later comparison
	   prdtTitle = driver.findElement(By.xpath("//*[@id='divforcomparing0']/div[2]/h3/a[contains(@title,'sport'|'Sport')]")).getText();
	   prdPrice = driver.findElement(By.xpath("//*[@id='divforcomparing0']/div[2]//span[contains(@itemprop,'price')]")).getText();  
		 
	 //save price as double
   	  prdPriceValue = GeneralOper.getDoublePriceValue(prdPrice);
		
      prdsAttr.put("productTitle" + Integer.toString(j), prdtTitle);
      prdsAttr.put("productPrice"+ Integer.toString(j),prdPrice);
   	 
    //shipping fee is not present
	  if(driver.findElements(By.xpath("//*[@id='divforcomparing0']/div[2]//span[contains(@class,'price_free')]")).size()>0){
		   
		  prdShippingFee =driver.findElement(By.xpath("//*[@id='divforcomparing0']/div[2]//span[contains(@class,'price_free')]")).getText();
		   
		  //if shipping fee is not free, sum fee here
		  if( prdShippingFee.equals("+ Free Shipping")){
			  prdShipFeeValue =0.00;
		   }
		  else{
			  prdShipFeeValue +=Double.parseDouble(prdShippingFee);
   		  }
   	  
	   }
	   
	   //shipping fee save as string like "99.99" without currency symbol like "CA$99.99"!!!!
	   prdsAttr.put("productShippingFee"+ Integer.toString(j),Double.toString(prdShipFeeValue));
	   
	   logGen.debug(prdtTitle + "---- "+ prdPrice +" ----"+ prdShipFeeValue+"-------"+prdsAttr);
   	  
   	 //add to cart
   	  driver.findElement(By.xpath("//*[@id='divforcomparing0']/div[2]/button[@title='Add to Cart']")).click();
   	  logGen.debug("clicking on first product from searching result webpage");
   			
   	  try {
		Thread.sleep(2000);
   	  } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
   	  }
   	  
   	  return prdsAttr;
   }
   
   //add product by product detail page
   public static Map<String,String> addTwoProductsByProductPage(String prdName,Map<String,String>prdsAttr, WebDriver driver){
	   
	   //System.out.println(prdsAttr.size()+"--"+prdsAttr);
	   
	   String prdtTitle;
	   String prdPrice;
	   String prdShippingFee;
	   double prdShipFeeValue = 0.00;
	   double prdPriceValue;
	   int j=prdsAttr.size()/3;
	   CharSequence freeshippingcs = "+ Free Shipping";
	   
	   //search product
	   WebElement searchEle= driver.findElement(By.name("q"));
	   searchEle.clear();
	   logGen.debug("clicking on Search button");
	   searchEle.sendKeys(prdName);
	   searchEle.submit();
		
	   //scroll down page by 1000 pixel vertical to show product
	   JavascriptExecutor js = (JavascriptExecutor)driver;
	   js.executeScript("window.scrollBy(0,200)");
	  
	   try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	   //save second element attribute for later comparison
	   prdtTitle = driver.findElement(By.xpath("//*[@id='divforcomparing1']/div[2]/h3/a[contains(@title,'sport'|'Sport')]")).getText();
	   prdPrice = driver.findElement(By.xpath("//*[@id='divforcomparing1']/div[2]//span[contains(@itemprop,'price')]")).getText();
	   prdShippingFee =driver.findElement(By.xpath("//*[@id='divforcomparing1']/div[2]//span[contains(@class,'price_free')]")).getText();
		 
	  //save price as double
   	  prdPriceValue = GeneralOper.getDoublePriceValue(prdPrice);
   	  
      prdsAttr.put("productTitle" + Integer.toString(j), prdtTitle);
      prdsAttr.put("productPrice"+ Integer.toString(j),prdPrice);
   
      
      //shipping fee is not present
  	  if(driver.findElements(By.xpath("//*[@id='divforcomparing0']/div[2]//span[contains(@class,'price_free')]")).size()>0){
  		   
  		  prdShippingFee =driver.findElement(By.xpath("//*[@id='divforcomparing0']/div[2]//span[contains(@class,'price_free')]")).getText();
  		   
  		  //if shipping fee is not free, sum fee here
  		  if( prdShippingFee.equals("+ Free Shipping")){
  			  prdShipFeeValue =0.00;
  		   }
  		  else{
  			  prdShipFeeValue +=Double.parseDouble(prdShippingFee);
     		  }
     	  
  	   }
   	
   	  //shipping fee save as string like "99.99" without currency symbol like "CA$99.99"!!!!
      prdsAttr.put("productShippingFee"+ Integer.toString(j),Double.toString(prdShipFeeValue));
      
      j++;
      
      //open product description page and review information on page
      driver.findElement(By.xpath("//*[@id='divforcomparing1']/div[1]/a/img")).click();;
      
     // new WebDriverWait(driver,9000).until(webDriver->((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete"));
		
      //scroll down page to down review detail information on page and add product from recommended area
      int i =0;
      while(i++<9){
	   js.executeScript("window.scrollBy(0,200)");
	   
	   try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      }
      
      //add recommended products into cart
      if(driver.findElements(By.xpath("//div[@class ='box-additional box-tabs grid12-9']")).size()>0){ 		  
    	  
    	  //save product title into map
    	  prdsAttr.put("productTitle" + Integer.toString(j), driver.findElement(By.xpath("(//div[@class ='box-additional box-tabs grid12-9']//div[@class ='owl-item'])[1]//div[@class ='product-content-wrapper']//h3[@class='product-name']/a")).getText());
    	  
    	  //check special price,add product price into map
    	  if(driver.findElements(By.xpath("(//div[@class ='box-additional box-tabs grid12-9']//div[@class ='owl-item'])[1]//div[@class ='product-content-wrapper']/div[@class='price-box']/p[@class ='special-price']")).size()>0){
    		 
    		  prdsAttr.put("productPrice" + Integer.toString(j),driver.findElement(By.xpath("//div[@class ='box-additional box-tabs grid12-9']//div[@class ='owl-item'][1]//div[@class ='product-content-wrapper']//div[@class='price-box']/p[@class ='special-price']/span[2]")).getText());
    	  }
    	  else
    	  {
    		  prdsAttr.put("productPrice" + Integer.toString(j),driver.findElement(By.xpath("(//div[@class ='box-additional box-tabs grid12-9']//div[@class ='owl-item'][1]//div[@class ='product-content-wrapper']//div[@class='price-box']//span[@class='price']")).getText());
 	
    	  }
    	  
    	  //add product shipping fee into map
    	 //shipping free is not present
      	  if(driver.findElements(By.xpath("(//div[@class ='box-additional box-tabs grid12-9']//div[@class ='owl-item'])[1]//div[@class ='product-content-wrapper']//div[@class='price-box']//span[contains(@class,'price_free')]")).size()>0){
      		  
      		  prdShipFeeValue =0.00;
      		  }
      	  else if(driver.findElements(By.xpath("(//div[@class ='box-additional box-tabs grid12-9']//div[@class ='owl-item'])[1]//div[@class ='product-content-wrapper']//div[@class='price-box']//span[contains(@class,'prict_inkl')]")).size()>0){
    
      		 prdShipFeeValue =0.00;
      	  }
      	  else{
      		  //!!!!!not verify is working?????
      		  prdShipFeeValue +=Double.parseDouble(prdShippingFee);
      		  }
         	  
      	   
    	  prdsAttr.put("productShippingFee"+ Integer.toString(j),Double.toString(prdShipFeeValue));
    	  
    	  //click button to add product
    	  driver.findElement(By.xpath("(//div[@class ='box-additional box-tabs grid12-9']//div[@class ='owl-item'])[1]//div[@class ='product-content-wrapper']//button")).click();
    	  
    	  //check add to cart is successed or not
         Assert.assertTrue("Add recommended product into cart failed",   new WebDriverWait(driver,4000).until(new ExpectedCondition<WebElement>(){  
            @Override  
            public WebElement apply(WebDriver d) {  
                return d.findElement(By.xpath("//div[@class='success-msg']"));  
            }
            }).isDisplayed());
      }
      
      
      //scroll up to top to add to cart
	  
      while(i-->2){
   	   js.executeScript("window.scrollBy(0,-200)");
   	   
   	   try {
   			Thread.sleep(2000);
   		} catch (InterruptedException e) {
   			// TODO Auto-generated catch block
   			e.printStackTrace();
   		}
       }
   	  
      driver.findElement(By.id("addToCartButton")).click();
      
      Assert.assertTrue("Add product from detail page into cart failed",   new WebDriverWait(driver,4000).until(new ExpectedCondition<WebElement>(){  
          @Override  
          public WebElement apply(WebDriver d) {  
              return d.findElement(By.xpath("//div[@class='success-msg']"));  
          }
          }).isDisplayed());   
      
      logGen.debug(prdtTitle + "---- "+ prdPrice +" ----"+ prdShippingFee+"-------"+prdsAttr);
	   
	  return prdsAttr;
   }
	
   //calculate total price for all products in shopping cart
   public static Double subtotalPriceCart(Map<String, String> prdsAttr, WebDriver driver){
	   double subtotalPriceValue;
	   int j =1,k=0;
	   String cartPrdTitle;
	   String cartPrdPrice;
	   String cartPrdQuantity;
	   String cartPrdSubtPrice;
	   double cartPrdPriceValue;
	   double cartPrdSubtPriceValue;
	   double calTotalPriceValue;
	   double cartSubtTotle=0.00;
	   
	   Map<String, String> CartPrdsAttr = new HashMap<String, String>();
	   List<Double> subtPriceList = new ArrayList<Double>();
	   //regex for hashtable key
	   Pattern keyPattern = Pattern.compile("\\d+");
	   
	   
	   List<WebElement> allRows= driver.findElements(By.xpath("//table[@id='shopping-cart-table']/tbody/tr"));
		//fetch all product attribute value
		for(int i =1 ;i < allRows.size()+1; i++){
			
			cartPrdTitle = driver.findElement(By.xpath("//table[@id='shopping-cart-table']/tbody/tr["+i+"]/td[2]/h2/a")).getText();
			cartPrdPrice = driver.findElement(By.xpath("//table[@id='shopping-cart-table']/tbody/tr["+i+"]/td[3]/span[contains(@class,'price')]")).getText();
			cartPrdQuantity = driver.findElement(By.xpath("//table[@id='shopping-cart-table']/tbody/tr["+i+"]/td[4]/div[@class='quantity_counter']/input")).getAttribute("value");
			cartPrdSubtPrice = driver.findElement(By.xpath("//table[@id='shopping-cart-table']/tbody/tr["+i+"]/td[5]/span[contains(@class,'price')]")).getText();
		
			CartPrdsAttr.put("cartPrdTitle"+ Integer.toString(i),cartPrdTitle);
			CartPrdsAttr.put("cartPrdPrice"+ Integer.toString(i),cartPrdPrice);
			CartPrdsAttr.put("cartPrdQuantity"+ Integer.toString(i),cartPrdQuantity);
			CartPrdsAttr.put("cartPrdSubtPrice"+ Integer.toString(i),cartPrdSubtPrice);
			
			
			//Checking point: subtotal price same as unit price multiple quantity
			//get digital number of product price & save price as double
			cartPrdPriceValue = getDoublePriceValue(cartPrdPrice);
			
			cartPrdSubtPriceValue=getDoublePriceValue(cartPrdSubtPrice);
		    
		   
		   //add subtotal price of each product into list for later calculation
		   subtPriceList.add(cartPrdSubtPriceValue);
          
		   //Checking point: subtotal price same as unit price multiple quantity
		   calTotalPriceValue =  Double.parseDouble(cartPrdQuantity) * cartPrdPriceValue;
		   Assert.assertTrue("Subtotal Price of "+cartPrdTitle +" is correct!", calTotalPriceValue == cartPrdSubtPriceValue);
			   
		   
		   //Checking point: product tile same as added from searching
		   Set<String> keys = prdsAttr.keySet();
		   for(String key: keys){
			   if(prdsAttr.get(key).equals(cartPrdTitle)){
				   
				    Matcher keyMatcher = keyPattern.matcher(key);
				    if(keyMatcher.find()){
				    	k = Integer.parseInt(keyMatcher.group());
				    }
				   Assert.assertTrue("!!!Product " +cartPrdTitle +" price is correct!", prdsAttr.get("productPrice"+ Integer.toString(k)).equals(cartPrdPrice));
			   }
		   }
		   	   	   
		   logGen.debug("subtotalPriceCart: "+cartPrdTitle + "---- "+ cartPrdPrice +" ----"+ cartPrdQuantity+ "----"+cartPrdSubtPrice);
		}
		
		//calculate summary of subtotal price 
		for(int p=0;p<subtPriceList.size();p++){
			cartSubtTotle+=subtPriceList.get(p);
			System.out.println(subtPriceList.get(p));
		}
	   
		//keep 2 digits after decimal point
	   BigDecimal bg= new BigDecimal(cartSubtTotle).setScale(2, RoundingMode.UP);
	   subtotalPriceValue = bg.doubleValue();
		
	   return subtotalPriceValue;
   }
   
   //税费需要四舍五入，保留2位小数点点
   public static Double taxValue(double taxRate, double subtotalPrice){
	   
	   double taxValue =0.00;
	   
	   taxValue = taxRate*subtotalPrice;
	   
	   BigDecimal bg= new BigDecimal(taxValue).setScale(2, RoundingMode.UP);
	   
	   return bg.doubleValue();
   }
   
   
   public static void increaseQuantityOfFirPrd(WebDriver driver){
	   
	   try{
		   
		   driver.findElement(By.id("shopping-cart-table")).isDisplayed();
		   //increase quantity of first product in cart
		   String initQuantity = driver.findElement(By.xpath("//table[@id='shopping-cart-table']/tbody/tr[1]/td[4]/div[@class='quantity_counter']/input")).getAttribute("value");
		   driver.findElement(By.xpath("//table[@id='shopping-cart-table']/tbody/tr[1]/td[4]/div[@class='quantity_counter']//i[@class='mpx-arrow-up-sm']")).click();
		   
		   String modQuantity = driver.findElement(By.xpath("//table[@id='shopping-cart-table']/tbody/tr[1]/td[4]/div[@class='quantity_counter']/input")).getAttribute("value");
		   logGen.debug(initQuantity + "//" + modQuantity);
		   Thread.sleep(6000);
		   
		   Assert.assertTrue("---Increase shopping cart quantity by one failed!",modQuantity.equals(Integer.toString(Integer.parseInt(initQuantity)+1)));
		   
	   }catch(Exception e){
		   e.printStackTrace();
	   }
	   
   }
   
   public static void moveWishList(WebDriver driver){
	   
	   //user should be login previously
	   Assert.assertTrue("---User is not logged in, so move wishlist option is not displayed! ",isUserLogged(driver));
	   
	   try{
		   
		   List<WebElement> productsBefore = driver.findElements(By.tagName("tr"));
		   int i = productsBefore.size();
		   
		   if (i>2){   
			   driver.findElement(By.xpath("//table[@id='shopping-cart-table']/tbody//tr[@class='even']")).isDisplayed();
			   //move second one in three products
			   driver.findElement(By.xpath("//table[@id='shopping-cart-table']/tbody//tr[@class='even']//td[@class='last']//a[@class='link-wishlist use-ajax']")).click();
			   
			   Thread.sleep(3000);
			   List<WebElement> productsAfter = driver.findElements(By.tagName("tr"));
			   Assert.assertTrue("--- Products number is not decrease after moving to wishlist! ", productsAfter.size()==i-1);
		   }
		   else if(i==2){
			   driver.findElement(By.xpath("//table[@id='shopping-cart-table']/tbody//tr[@class='last even']")).isDisplayed();
			   //move second one in three products
			   driver.findElement(By.xpath("//table[@id='shopping-cart-table']/tbody//tr[@class='last even']//td[@class='last']//a[@class='link-wishlist use-ajax']")).click();
			   
			   Thread.sleep(3000);
		   }
		   else{
			   logGen.debug("---Only one product left, won't move to wishlist!");
		   }
		   
	   }catch(Exception e){
		   e.printStackTrace();
	   }
	   
	   
   }

public static void userLoginFromCart(CharSequence loginEmail, CharSequence loginPwd, WebDriver wb) {
	// TODO Auto-generated method stub
	
}
   
  /* public static void main(String[] args){
	  System.out.println(4/4);
   }*/
   
}
