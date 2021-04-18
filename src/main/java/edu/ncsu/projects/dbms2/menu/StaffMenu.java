package edu.ncsu.projects.dbms2.menu;

import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.ncsu.projects.dbms2.dao.staffDao;
import edu.ncsu.projects.dbms2.entity.Staff;

@Component
public class StaffMenu {
	private final List<String> staffList = new ArrayList<>();
	private static Scanner scan = new Scanner(System.in);
	
	@Autowired
	private staffDao StaffDao;

	public StaffMenu() {
		staffList.add("View all Staff");
		staffList.add("View Staff details by attribute");
		staffList.add("Add new Staff");
		staffList.add("Update Staff by ID");
		staffList.add("Delete Staff by ID");
		staffList.add("Back to Main Menu");
	}

	private void executeAction(int choice) {
		switch(choice) {
		case 1: 
			getAllStaff();
			break;
		case 2:
			getStaffByAttribute();
			break;
		case 3:
			try {
				addStaff();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 4:
			updateStaff();
			break;
		case 5:
			deleteStaff();
			break;
		default:
			System.out.println("Invalid Choice");
		}
	}
	
	private void getStaffByAttribute() {
			System.out.print("Enter attribute name: ");
			String attributeName = scan.next().toUpperCase();
			
			scan.nextLine();
			System.out.println("Enter attribute value: ");
			String attributeValue = scan.nextLine();
			
			Staff staff = StaffDao.findByAttribute(attributeName, attributeValue);
			
			System.out.println(staff);
	}

	private void getAllStaff() {
			List<Staff> staffs = StaffDao.findAll();
			
			for(Staff staff : staffs) {
				System.out.println(staff);
		}
	}

	private void addStaff() throws ParseException {
		Staff staff = new Staff();
		
		scan.nextLine();
		System.out.println("Enter name: ");
		staff.setName(scan.nextLine());
		
		System.out.println("Enter phone: ");
		staff.setPhone(scan.next());
		
		scan.nextLine();
		System.out.println("Enter address: ");
		staff.setAddress(scan.nextLine());
		
		System.out.println("Enter email: ");
		staff.setEmail(scan.next());
		
		scan.nextLine();
		System.out.println("Enter job title: ");
		staff.setJobTitle(scan.nextLine());
		
		System.out.println("Enter age: ");
		staff.setAge(scan.nextInt());
		
		System.out.println("Enter start date (yyyy-MM-dd): ");
		
		String sDate1 = scan.next();
		Date date1 = Date.valueOf(sDate1);
		staff.setStartDate(date1);
		
		System.out.println("Enter store id: ");
		staff.setStoreId(scan.nextInt());
			
		int count = StaffDao.addStaff(staff);
		System.out.println("Added "+ count +" rows.");
	}
	
	private void updateStaff() {
		System.out.println("Enter attribute name: ");
		String attributeName = scan.next().toUpperCase();
		
		scan.nextLine();
		System.out.println("Enter attribute value: ");
		String attributeValue = scan.nextLine();
		
		System.out.println("Enter staff ID to update: ");
		Integer staffId = scan.nextInt();
		
		int updateCount = StaffDao.updateByAttribute(attributeName, attributeValue, staffId);
		
		System.out.println("Updated "+ updateCount +" rows in STAFF table.");
	}

	private void deleteStaff() {
		System.out.println("Enter staff ID to delete: ");
		Integer staffId = scan.nextInt();
		
		int deletedStaff = StaffDao.deleteStaff(staffId);
		
		System.out.println("Deleted "+ deletedStaff +" rows.");
	}
	
	public void loadMenu() {
		while (true) {
			printCustomerMenu();
			
			System.out.print("Enter choice: ");
			int choice = scan.nextInt();
			
			if (choice == staffList.size()) {
				break;
			}
			
			try {
				executeAction(choice);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void printCustomerMenu() {
		System.out.println("CUSTOMER ACTIONS:");
		for (int i=0; i<staffList.size(); i++) {
			System.out.println(i+1 +": "+ staffList.get(i));
		}
	}
}
	
	
