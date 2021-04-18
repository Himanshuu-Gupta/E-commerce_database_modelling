package edu.ncsu.projects.dbms2.menu;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.ncsu.projects.dbms2.dao.ProductActionsDao;

@Component
public class ProductActionsMenu {
	private final List<String> productActions = new ArrayList<>();
	private static Scanner scan = new Scanner(System.in);
	
	@Autowired
	private ProductActionsDao productActionsDao;
	
	/**
	 * Define Product Actions Menu
	 */
	public ProductActionsMenu() {
		productActions.add("Update Product");
	}
	
	/**
	 * Product Actions operations menu driver
	 * @param choice
	 */
	private void executeAction(int choice) {
		switch(choice) {
		case 1: 
			updateByAttribute();
			break;
		default:
			System.out.println("Invalid Choice");
		}
	}
	
	/**
	 *	Driver to update the product based on Product id
	 */
	private void updateByAttribute() {
		
		System.out.println("Enter product ID to update: ");
		Integer productID = scan.nextInt();
		
		System.out.println("Enter attribute name: ");
		String attributeName = scan.next().toUpperCase();
		
		scan.nextLine();
		System.out.println("Enter attribute value: ");
		String attributeValue = scan.nextLine();
		
		int updateCount = productActionsDao.updateByAttribute(attributeName, attributeValue, productID);
		
		System.out.println("Updated "+ updateCount +" rows in STORE_INVENTORY and WAREHOUSE_TRANSACTION table.");
	}
	
	/**
	 * Driver to load the menu screen for staff
	 */
	public void loadMenu() {
		while (true) {
			printCustomerMenu();
			
			System.out.print("Enter choice: ");
			int choice = scan.nextInt();
			
			if (choice == productActions.size()+1) {
				break;
			}
			
			try {
				executeAction(choice);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Driver to print the menu screen
	 */
	private void printCustomerMenu() {
		System.out.println("CUSTOMER ACTIONS:");
		for (int i=0; i<productActions.size(); i++) {
			System.out.println(i+1 +": "+ productActions.get(i));
		}
	}
}


