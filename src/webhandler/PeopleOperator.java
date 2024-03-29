package webhandler;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sun.xml.internal.bind.v2.runtime.reflect.ListIterator;

import csvhandler.CsvScanner;
import csvhandler.PeopleScanner;
import db.LocalDBHandler;
import pojo.Company;
import pojo.People;
import pojo.WorkType;
import scrapper.Parser;
import scrapper.PeopleConvert;
import scrapper.PeopleList;


public class PeopleOperator extends FireFoxOperator{
	
	private String type = "People";
	private Parser parser = null; 
	private CsvScanner scanner = null; 
	
	public PeopleOperator() {
		parser = new PeopleList();
		scanner = new PeopleScanner();
	}
	
	public void setWorkType(WorkType mode) {
		if(mode == WorkType.LIST)
			parser = new PeopleList();
		if(mode == WorkType.CONVERT)
			parser = new PeopleConvert();
	}
	
	public int scanCsv(String path) {
		LinkedList<?> list = scanner.dataScan(path);
		System.out.println("list -- "+ list.size());
		return parser.writeToDb(list);
	}
	
/// ----------------------- List ---------------------------------- ///	
	@Override
	public String checkPageStatus() {
		//button[contains(@class,'selected') and contains(., 'People')] // good one
		//button[contains(@class,'search-reusables__filter') and contains(., 'People')]
		By pageElementBy = By.xpath("//button[contains(@class,'selected') and contains(., '"+ type +"')]");
		return isElementPresent(pageElementBy) ? "error:false" : "error:OPPS! You are in wrong page";
	}
	
	@Override
	public String takeList() {
		fullPageScroll();
		//salesPageScroll();
		int count = parser.parseList();
		return "data:"+count;  //"data:10" // "error:msg"
	}
		
	@Override
	public int clearList() {
		parser.deleteAllData();
		return 0;
	}
	@Override
	public int getTotalCounts() {
		return parser.getTotalCounts();
	}
	
	public int currentPageNumber() {
		String currentSelector = "//button[contains(@aria-label,'current page')]/span[1]"; 
		WebElement element;
		if (isElementPresent(By.xpath(currentSelector))) {
			element = driver.findElement(By.xpath(currentSelector));
			return Integer.parseInt(element.getText());
		} else
			return -1;
	}

	public int openNextPage() {
		//button[contains(@class,'button--next')]
		System.out.println(" <- openNextPage clicked");
		int responsepage = 0;
		By nextPageSelectorBy = By.xpath("//button[contains(@class,'button--next')]");
		if(isElementPresent(nextPageSelectorBy) && driver.findElement(nextPageSelectorBy).isEnabled()) {
			responsepage = switchingPage(nextPageSelectorBy);
		}			
		return responsepage;

	}

	public int openPreviousPage() {
		int responsepage = 0;
		By prevPageSelectorBy = By.xpath("//button[contains(@class,'button--previous')]");
		if(isElementPresent(prevPageSelectorBy) && driver.findElement(prevPageSelectorBy).isEnabled()) {
			responsepage = switchingPage(prevPageSelectorBy);
		}	
		
		return responsepage;

	}
	@Override
	public int switchingPage(By by) {
		// driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		try {
			driver.findElement(by).click();
			fullPageScroll();
			
		} catch (NoSuchElementException e) {
			System.out.println(e.getMessage());;
		}
		
		System.out.println(currentPageNumber() + " <- currentPageNumber");

		return currentPageNumber();
	}
	
	public String findUrlInSourcePage() {
		String txtMatch = "https://www.linkedin.com/in/";
		String source = getSourseCode();

		if(source.contains(txtMatch)) {
			String fristSubString = source.substring(source.indexOf(txtMatch), source.length());
			String urltxt = fristSubString.substring(0, fristSubString.indexOf(",")-1);
			
			return (urltxt.contains(txtMatch)) ? urltxt : "";
		}
		
		return "";
	}
	
/// ----------------------- Convert ---------------------------------- ///	
	
	public int getDetailsInfo(int index) {
		return parser.parseData(index)? 1 : 0;

	}





}
