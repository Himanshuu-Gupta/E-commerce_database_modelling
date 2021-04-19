package edu.ncsu.projects.dbms2.menu;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import edu.ncsu.projects.dbms2.dao.WarehouseActionDao;
import edu.ncsu.projects.dbms2.entity.WarehouseInventory;

@Component
public class WarehouseActionMenu {
	private final List<String> menuList = new ArrayList<>();
	private static Scanner scan = new Scanner(System.in);
	
	@Autowired
	private WarehouseActionDao WarehouseActionDao;
	
	public WarehouseActionMenu() {
		menuList.add("Warehouse to Store Transfer");
		menuList.add("Add Warehouse Transaction");
		menuList.add("Warehouse to Supplier Return");
		menuList.add("View Inventory");
		menuList.add("Back to Main Menu");
		menuList.add("Exit");
	}
	
	private void executeAction(int choice) {
		switch(choice) {
		case 1: 
			warehousetransfer();
			break;
		case 2:
			addtransaction();
			break;
		case 3:
			warehousesupplierreturn();
			break;
		case 4:
			viewinventory();
			break;
		default:
			System.out.println("Invalid Choice");
		}
	}
	
	private void warehousetransfer() {
		System.out.println("Enter the Warehouse Operator ID : ");
		Integer warehouseId = scan.nextInt();
		
		System.out.println("Enter the Product ID you want to transfer: ");
		Integer productId = scan.nextInt();
		
		System.out.println("Enter the Quantity of the product you want to transfer: ");
		Integer quantity = scan.nextInt();
		
		System.out.println("Enter the Store ID you want to transfer the product to: ");
		Integer storeId = scan.nextInt();
		
		int transfered = WarehouseActionDao.WarehouseToStoreTransfer(quantity, productId, storeId, warehouseId);
		
		System.out.println("Transfered"+ transfered +" rows.");
	}
	
	private void addtransaction() {
		System.out.println("Enter the Warehouse Operator ID : ");
		Integer warehouseId = scan.nextInt();
		
		System.out.println("Enter the Product ID : ");
		Integer productId = scan.nextInt();
		
		scan.nextLine();
		System.out.println("Enter the Product name : ");
		String productname = scan.nextLine();
		
		System.out.println("Enter the Supplier ID from where we received the product: ");
		Integer supplierId = scan.nextInt();
		
		System.out.println("Enter the Quantity of the product we received: ");
		Integer quantity = scan.nextInt();
		
		System.out.println("Enter the price of the product: ");
		Integer price = scan.nextInt();
		
		System.out.println("Enter the production Date of the product in (YYYY-MM-DD) format : ");
		String prodate = scan.next();
		Date proddate = Date.valueOf(prodate);    // Converting the string to SQL date format
		
		System.out.println("Enter the expiration Date of the product in (YYYY-MM-DD) format: ");
		String expdate = scan.next();
		Date expidate = Date.valueOf(expdate);    // Converting the string to SQL date format
		
		long millis=System.currentTimeMillis();  
        java.sql.Date tdate = new java.sql.Date(millis);
		
		int added = WarehouseActionDao.addWarehouseTransaction(productId, supplierId, warehouseId, quantity, price, proddate, expidate, productname, tdate);
		
		System.out.println("New Transacation Recorded for product - "+ productId);
	}
	
	private void warehousesupplierreturn() {
		System.out.println("Enter the Warehouse Operator ID : ");
		Integer warehouseId = scan.nextInt();
		
		System.out.println("Enter the Product ID : ");
		Integer productId = scan.nextInt();
		
		System.out.println("Enter the Supplier ID to whom we want to return the product: ");
		Integer supplierId = scan.nextInt();
		
		System.out.println("Enter the Quantity to be returned: ");
		Integer quantity = scan.nextInt();
		
		int returned = WarehouseActionDao.WarehousetoSupplierReturn(productId, supplierId, warehouseId, quantity);
		
		System.out.println("Returned the product "+ productId);
	}
	
	private void viewinventory() {
		List<WarehouseInventory> inventory = WarehouseActionDao.ViewInventory();
		
		for(WarehouseInventory item : inventory) {
			System.out.println(item);
		}
	}
	
	
	public void loadMenu() {
		while (true) {
			printWarehouseActionMenu();
			
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
	
	private void printWarehouseActionMenu() {
		System.out.println("WAREHOUSE ACTIONS:");
		for (int i=0; i<menuList.size(); i++) {
			System.out.println(i+1 +": "+ menuList.get(i));
		}
	}

}
