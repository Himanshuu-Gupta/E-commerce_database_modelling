package edu.ncsu.projects.dbms2.menu;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.Scanner;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.ncsu.projects.dbms2.dao.ReportsDao;

@Component
public class ReportsMenu {

	private final List<String> menuList = new ArrayList<>();
	private static Scanner scan = new Scanner(System.in);
	
	@Autowired
	private ReportsDao reportsDao;
	
	public ReportsMenu() {
		menuList.add("Sales report for a store - month");
		menuList.add("Sales report for a store - year");
		menuList.add("Sales report for chain - month");
		menuList.add("Sales report for chain - year");
		menuList.add("Merchandise Store Inventory");
		menuList.add("Customer growth report by month");
		menuList.add("Customer growth report by year");
		menuList.add("Customer activity report for a period");

	}
	
	private void executeAction(int choice) {
		switch(choice) {

		case 1:
			getStoresMonth();
			break;
		case 2:
			getStoresYear();
			break;
		case 3:
			getStoresChainMonth();
			break;
		case 4:
			getStoresChainYear();
			break;
		case 5:
			merchandiseStoreInventory();
			break;
		case 6:
			customerGrowthMonth();
			break;
		case 7:
			customerGrowthYear();
			break;
		case 8:
			customerActivityReport();
			break;
		
		default:
			System.out.println("Invalid Choice");
		}
	}
	
	/**
	 * Method to get monthly sales report for a particular store.
	 */
	private void getStoresMonth() {
		
		System.out.println("Enter store id for store report:");
		Integer storeId = scan.nextInt();
		
		System.out.println("Enter From Date:");
		Date fromDate = Date.valueOf(scan.next());
		System.out.println(fromDate);
		
		System.out.println("Enter To Date:");
		Date toDate = Date.valueOf(scan.next());
		System.out.println(toDate);
		
		List<Map<String, Object>> report = reportsDao.monthlySalesByStoreReport(storeId,fromDate,toDate);
		printReport(report);
	}
	
	private void printReport(List<Map<String, Object>> report) {
		for (Map<String, Object> row : report) {
			String rowString = row.entrySet().stream().map(Object::toString).collect(Collectors.joining(" :: "));
			System.out.println(rowString);
		}
	}
	
	/**
	 * Method to get yearly sales report for a particular store.
	 */
	private void getStoresYear() {
		System.out.println("Enter store id for store report:");
		Integer storeId = scan.nextInt();
		
		System.out.println("Enter From Date:");
		Date fromDate = Date.valueOf(scan.next());
		
		System.out.println("Enter To Date:");
		Date toDate = Date.valueOf(scan.next());
		
		List<Map<String, Object>> report = reportsDao.yearlySalesByStoreReport(storeId,fromDate,toDate);
		printReport(report);
	}
	
	/**
	 * Method to get monthly sales report for the entire chain.
	 */
	private void getStoresChainMonth() {
		System.out.println("Enter From Date:");
		Date fromDate = Date.valueOf(scan.next());
		
		System.out.println("Enter To Date:");
		Date toDate = Date.valueOf(scan.next());
		
		List<Map<String, Object>> report = reportsDao.chainMonthlySalesReport(fromDate,toDate);
		printReport(report);
	}

	/**
	 * Method to get yearly sales report for the entire chain.
	 */
	private void getStoresChainYear() {
		
		System.out.println("Enter From Date:");
		Date fromDate = Date.valueOf(scan.next());
		
		System.out.println("Enter To Date:");
		Date toDate = Date.valueOf(scan.next());
		
		List<Map<String, Object>> report = reportsDao.chainYearlySalesReport(fromDate,toDate);
		printReport(report);
	}
	
	/**
	 * Method to get store inventory report for a particular store.
	 */
	private void merchandiseStoreInventory() {
		System.out.println("Enter store id for store report:");
		Integer storeId = scan.nextInt();
		
		List<Map<String, Object>> report = reportsDao.merchStoreReport(storeId);
		printReport(report);
	}
	
	/**
	 * Method to get monthly customer membership report in a given time range.
	 */
	private void customerGrowthMonth() {
		
		System.out.println("Enter From Date:");
		Date fromDate = Date.valueOf(scan.next());
		
		System.out.println("Enter To Date:");
		Date toDate = Date.valueOf(scan.next());
		
		List<Map<String, Object>> report = reportsDao.monthlyCustomerGrowthReport(fromDate,toDate);
		printReport(report);
	}
	
	/**
	 * Method to get yearly customer membership report in a given time range.
	 */
	private void customerGrowthYear() {
		System.out.println("Enter From Date:");
		Date fromDate = Date.valueOf(scan.next());
		
		System.out.println("Enter To Date:");
		Date toDate = Date.valueOf(scan.next());
		
		List<Map<String, Object>> report = reportsDao.yearlyCustomerGrowthReport(fromDate,toDate);
		printReport(report);
	}
	
	/**
	 * Method to get a particular customer spendings report.
	 */
	private void customerActivityReport() {
		
		System.out.println("Enter Member Id:");
		Integer customerId = scan.nextInt();
		
		System.out.println("Enter From Date:");
		Date fromDate = Date.valueOf(scan.next());
		
		System.out.println("Enter To Date:");
		Date toDate = Date.valueOf(scan.next());
		
		List<Map<String, Object>> report = reportsDao.customerActivityReport(customerId, fromDate, toDate);
		printReport(report);
	}
	
	public void loadMenu() {
		while (true) {
			printStoreMenu();
			
			System.out.print("Enter choice: ");
			int choice = scan.nextInt();
			
			if (choice == menuList.size()) {
				break;
			}
			
			try {
				executeAction(choice);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void printStoreMenu() {
		System.out.println("REPORTS ACTIONS:");
		for (int i=0; i<menuList.size(); i++) {
			System.out.println(i+1 +": "+ menuList.get(i));
		}
	}
	
}
