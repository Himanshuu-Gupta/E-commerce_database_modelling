package edu.ncsu.projects.dbms2.menu;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
	
	
	private void getStoresMonth() {
		
		System.out.println("Enter store id for store report:");
		Integer storeId = scan.nextInt();
		
		System.out.println("Enter From Date:");
		Date fromDate = Date.valueOf(scan.next());
		
		System.out.println("Enter To Date:");
		Date toDate = Date.valueOf(scan.next());
		
		reportsDao.monthlySalesByStoreReport(storeId,fromDate,toDate);
//		monthlyStoreReport();
		
	}
	
	private void getStoresYear() {
		
		System.out.println("Enter store id for store report:");
		Integer storeId = scan.nextInt();
		
		System.out.println("Enter From Date:");
		Date fromDate = Date.valueOf(scan.next());
		
		System.out.println("Enter To Date:");
		Date toDate = Date.valueOf(scan.next());
		
		reportsDao.yearlySalesByStoreReport(storeId,fromDate,toDate);
		
		
	}
	
	private void getStoresChainMonth() {
		System.out.println("Enter From Date:");
		Date fromDate = Date.valueOf(scan.next());
		
		System.out.println("Enter To Date:");
		Date toDate = Date.valueOf(scan.next());
		
		reportsDao.chainMonthlySalesReport(fromDate,toDate);
	}
	
	private void getStoresChainYear() {
		
		System.out.println("Enter From Date:");
		Date fromDate = Date.valueOf(scan.next());
		
		System.out.println("Enter To Date:");
		Date toDate = Date.valueOf(scan.next());
		
		reportsDao.chainYearlySalesReport(fromDate,toDate);
		
	}
	
	
	private void merchandiseStoreInventory() {
		System.out.println("Enter store id for store report:");
		Integer storeId = scan.nextInt();
		
		reportsDao.merchStoreReport(storeId);
		
	}
	
	private void customerGrowthMonth() {
		
		System.out.println("Enter From Date:");
		Date fromDate = Date.valueOf(scan.next());
		
		System.out.println("Enter To Date:");
		Date toDate = Date.valueOf(scan.next());
		
		reportsDao.monthlyCustomerGrowthReport(fromDate,toDate);
		
	}
	
	private void customerGrowthYear() {
		System.out.println("Enter From Date:");
		Date fromDate = Date.valueOf(scan.next());
		
		System.out.println("Enter To Date:");
		Date toDate = Date.valueOf(scan.next());
		
		reportsDao.yearlyCustomerGrowthReport(fromDate,toDate);
		
	}
	
	private void customerActivityReport() {
		
		System.out.println("Enter Member Id:");
		Integer customerId = scan.nextInt();
		
		System.out.println("Enter From Date:");
		Date fromDate = Date.valueOf(scan.next());
		
		System.out.println("Enter To Date:");
		Date toDate = Date.valueOf(scan.next());
		
		reportsDao.customerActivityReport(customerId, fromDate, toDate);
		
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
