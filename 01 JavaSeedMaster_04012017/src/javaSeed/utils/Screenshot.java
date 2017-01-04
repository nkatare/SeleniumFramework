package javaSeed.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64OutputStream;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.relevantcodes.extentreports.ExtentTest;

import javaSeed.constants.Const;

public class Screenshot {
	
	private static ExtentTest TestLogger = Const.etTestCases;
	
	public static String ObjectSnap(WebDriver driver, WebElement PickMe) throws IOException{
		
		//String ScreenshotImg = ScreenshotPath+"LoggerScreenshot"+TimeStamp.RetunrDate("YYYYMMdd")+TimeStamp.RetunrDate("HHmmssSSS")+".png";
	
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		BufferedImage  fullImg = ImageIO.read(scrFile);
		
		// Get the location of element on the page
		Point point = PickMe.getLocation();
		
		// Crop the entire page screenshot to get only element screenshot
		BufferedImage trimScreenshot= fullImg.getSubimage(point.getX(), point.getY(),
				PickMe.getSize().getWidth(), PickMe.getSize().getHeight());
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		OutputStream b64 = new Base64OutputStream(os);
		
		//ImageIO.write(trimScreenshot, "png", scrFile);	
		ImageIO.write(trimScreenshot, "png", b64);	
	
		//FileUtils.copyFile(scrFile, new File(ScreenshotImg));
        String LoggerScreenshot = TestLogger.addBase64ScreenShot("data:image/png;base64,"+os.toString());//.addScreenCapture(ScreenshotImg);
		
		return LoggerScreenshot;		
	}
	
	public static String ObjectSnapFullPage(WebDriver driver) throws IOException{
		
		//String ScreenshotImg = ScreenshotPath+"LoggerScreenshot"+TimeStamp.RetunrDate("YYYYMMdd")+TimeStamp.RetunrDate("HHmmssSSS")+".png";
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		BufferedImage  fullImg = ImageIO.read(scrFile);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		OutputStream b64 = new Base64OutputStream(os);
		ImageIO.write(fullImg, "png", b64);	
		//FileUtils.copyFile(scrFile, new File(ScreenshotImg));
	    String LoggerScreenshot = TestLogger.addBase64ScreenShot("data:image/png;base64,"+os.toString());
		return LoggerScreenshot;		
	}
}
