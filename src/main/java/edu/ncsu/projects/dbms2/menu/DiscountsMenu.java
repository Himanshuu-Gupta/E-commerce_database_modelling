package edu.ncsu.projects.dbms2.menu;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.ncsu.projects.dbms2.dao.DiscountDao;
import edu.ncsu.projects.dbms2.entity.Discount;

@Component
public class DiscountsMenu {
	private final List<String> menuList = new ArrayList<>();
	private static Scanner scan = new Scanner(System.in);
	
	@Autowired
	private DiscountDao discountDao;
	
	/**
	 * List of all menu options for Discount action
	 */
	public DiscountsMenu() {
		menuList.add("View all Discounts");
		menuList.add("Add new Discount");
		menuList.add("Update Discount by ID");
		menuList.add("Delete Discount by ID");
		menuList.add("Remove Discount record from DB");
		menuList.add("Back to Main Menu");
	}
	
	/**
	 * The API will trigger the desired function based on user choice
	 * @param choice
	 */
	private void executeAction(int choice) {
		switch(choice) {
		case 1: 
			getAllEntities();
			break;
		case 2:
			addEntity();
			break;
		case 3:
			updateEntity();
			break;
		case 4:
			deleteEntity();
			break;
		case 5:
			removeRecordFromDb();
			break;
		default:
			System.out.println("Invalid Choice");
		}
	}
	
	/**
	 * Trigger function in  DiscountDao to remove the records from the DB
	 */
	private void removeRecordFromDb() {
		System.out.println("Enter discount ID to remove: ");
		Integer discountId = scan.nextInt(); 
		
		int count = discountDao.removeFromDb(discountId);
		System.out.println("Successfully removed "+ count +" records from DISCOUNT table.");
	}

	/**
	 * Trigger function in  DiscountDao to delete the records from the DB
	 */
	private void deleteEntity() {
		System.out.println("Enter discount ID to delete: ");
		Integer discountId = scan.nextInt();
		
		int deletedDiscount = discountDao.deleteDiscount(discountId);
		
		System.out.println("Deleted "+ deletedDiscount +" rows.");
	}
	
	/**
	 * Trigger function in  DiscountDao to update the discount in the DB
	 */
	private void updateEntity() {
		System.out.println("Enter attribute name: ");
		String attributeName = scan.next().toUpperCase();
		
		scan.nextLine();
		System.out.println("Enter attribute value: ");
		String attributeValue = scan.nextLine();
		
		System.out.println("Enter discount ID to update: ");
		Integer discountId = scan.nextInt();
		
		int updateCount = discountDao.updateByAttribute(attributeName, attributeValue, discountId);
		
		System.out.println("Updated "+ updateCount +" rows in DISCOUNT table.");
	}
	
	/**
	 * Trigger function in  Add new Discount in the DB
	 */
	private void addEntity() {
		Discount discount = new Discount();
		
		System.out.println("Enter Store Id: ");
		discount.setStoreId(scan.nextInt());
		
		
		System.out.println("Enter Product Id: ");
		discount.setProductId(scan.nextInt());
		
		System.out.println("Enter Manager Id: ");
		discount.setManagerId(scan.nextInt());
		
		System.out.println("Enter Discount Percent: ");
		discount.setDiscountPercent(scan.nextDouble());
		
		System.out.println("Enter Discount From Date (YYYY-MM-dd): ");
		discount.setFromDate(Date.valueOf(scan.next()));
		
		System.out.println("Enter Discount End Date (YYYY-MM-dd): ");
		discount.setEndDate(Date.valueOf(scan.next()));
		
		int addedEntities = discountDao.addDiscount(discount);
		System.out.println("Added "+ addedEntities +" rows.");
	}
	
	/**
	 * List all the discount present on the products
	 */
	private void getAllEntities() {
		List<Discount> discounts = discountDao.findAll();
		
		for(Discount discount : discounts) {
			System.out.println(discount);
		}
	}
	
	/**
	 * Load the main menu for the discount action
	 */
	public void loadMenu() {
		while (true) {
			System.out.println();
			printMenu();
			
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
	
	/**
	 * Print the Menu for the Discount Action
	 */
	private void printMenu() {
		System.out.println("DISCOUNT ACTIONS:");
		for (int i=0; i<menuList.size(); i++) {
			System.out.println(i+1 +": "+ menuList.get(i));
		}
	}
}
