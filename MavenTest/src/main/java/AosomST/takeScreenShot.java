package AosomST;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class takeScreenShot {
public static String getCurrentDateTime(){
		
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
		return 	df.format(new Date());
	}
	
	public static String getCurrentDate(){
		
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		return 	df.format(new Date());
	}

	public static void takeScreenShot(WebDriver driver)
	{
		
		String curDate = getCurrentDate();
		String strPath = "D:\\AosomSmokeTest";
		
		File file = new File(strPath+"\\"+curDate);
		if(file.exists()==false){			
			try{
				file.mkdirs();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		
		File cartPic = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try{
			
			FileUtils.copyFile(cartPic, new File(strPath+"\\"+curDate+"\\"+getCurrentDateTime()+".png"));
			
		}catch(IOException e){
			e.printStackTrace();
		}
		
	}


}
