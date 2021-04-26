package edu.ncsu.projects.dbms2.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.ncsu.projects.dbms2.dao.SupplierDao;
import edu.ncsu.projects.dbms2.entity.Supplier;

@Component
public class SuppliersMenu {
	private final List<String> menuList = new ArrayList<>();
	private static Scanner scan = new Scanner(System.in);
	
	@Autowired
	private SupplierDao supplierDao;
	
	public SuppliersMenu() {
		menuList.add("View all Suppliers");
		menuList.add("View Supplier details by attribute");
		menuList.add("Add new Supplier");
		menuList.add("Update Supplier by ID");
		menuList.add("Delete Supplier by ID");
		menuList.add("Remove Supplier record from DB");
		menuList.add("Back to Main Menu");
	}
	
	private void executeAction(int choice) {
		switch(choice) {
		case 1: 
			getAllEntities();
			break;
		case 2:
			getEntityByAttribute();
			break;
		case 3:
			addEntity();
			break;
		case 4:
			updateEntity();
			break;
		case 5:
			deleteEntity();
			break;
		case 6:
			removeRecordFromDb();
			break;
		default:
			System.out.println("Invalid Choice");
		}
	}
	
	/**
	 * Deletes the mentioned supplier from the table
	 */
	private void removeRecordFromDb() {
		System.out.println("Enter supplier ID to remove: ");
		Integer supplierId = scan.nextInt(); 
		
		int count = supplierDao.removeFromDb(supplierId);
		System.out.println("Successfully removed "+ count +" records from SUPPLIERS table.");
	}
	
	/**
	 * Marks the mentioned supplier as inactive
	 */
	private void deleteEntity() {
		System.out.println("Enter supplier ID to delete: ");
		Integer supplierId = scan.nextInt();
		
		int deletedSupplier = supplierDao.deleteSupplier(supplierId);
		
		System.out.println("Deleted "+ deletedSupplier +" rows.");
	}
	
	/**
	 * Updates the supplier details for the mentioned attribute name i.e. 
	 * e.g. to update name attribute name =Supplier_name and attribute_value = New supplier name 
	 */
	private void updateEntity() {
		System.out.println("Enter attribute name: ");
		String attributeName = scan.next().toUpperCase();
		
		scan.nextLine();
		System.out.println("Enter attribute value: ");
		String attributeValue = scan.nextLine();
		
		System.out.println("Enter supplier ID to update: ");
		Integer supplierId = scan.nextInt();
		
		int updateCount = supplierDao.updateByAttribute(attributeName, attributeValue, supplierId);
		
		System.out.println("Updated "+ updateCount +" rows in SUPPLIERS table.");
	}
	
	/**
	 * Adds a new supplier info to the table
	 */
	private void addEntity() {
		Supplier supplier = new Supplier();
		
		scan.nextLine();
		System.out.println("Enter Supplier Name: ");
		supplier.setSupplierName(scan.nextLine());
		
		scan.nextLine();
		System.out.println("Enter Supplier Address: ");
		supplier.setAddress(scan.nextLine());
		
		System.out.println("Enter Supplier Phone: ");
		supplier.setPhone(scan.next());
		
		System.out.println("Enter Supplier Email: ");
		supplier.setEmail(scan.next());
		
		System.out.println("Enter Supplier Active Status: ");
		supplier.setActiveStatus(scan.nextBoolean());
		
		int addedSuppliers = supplierDao.addSupplier(supplier);
		System.out.println("Added "+ addedSuppliers +" rows.");
	}
	
	/**
	 * It fetches the supplier details given an primary attribute value
	 */
	private void getEntityByAttribute() {
		System.out.print("Enter attribute name: ");
		String attributeName = scan.next().toUpperCase();
		
		scan.nextLine();
		System.out.println("Enter attribute value: ");
		String attributeValue = scan.nextLine();
		
		List<Supplier> suppliers = supplierDao.findByAttribute(attributeName, attributeValue);
		
		for (Supplier supplier : suppliers) {
			System.out.println(supplier);
		}
	}
	
	/**
	 * Displays all the supplier details
	 */
	private void getAllEntities() {
		List<Supplier> suppliers = supplierDao.findAll();
		
		for(Supplier supplier : suppliers) {
			System.out.println(supplier);
		}
	}

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
	
	private void printMenu() {
		System.out.println("SUPPLIER ACTIONS:");
		for (int i=0; i<menuList.size(); i++) {
			System.out.println(i+1 +": "+ menuList.get(i));
		}
	}
}
