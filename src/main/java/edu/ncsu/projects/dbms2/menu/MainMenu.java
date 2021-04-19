package edu.ncsu.projects.dbms2.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MainMenu {
	private final List<String> menuList = new ArrayList<>();
	private static Scanner scan = new Scanner(System.in);
	
	@Autowired
	private MembersMenu membersMenu;
	
	@Autowired
	private SuppliersMenu suppliersMenu;
	
	@Autowired
	private DiscountsMenu discountsMenu;
	
	@Autowired
	private StaffMenu staffMenu;
	
	@Autowired
	private WarehouseActionMenu warehouseMenu;
	
	@Autowired
	private ProductActionsMenu productActionsMenu;
	
	@Autowired
	private BillingMenu billingMenu;
	
	@Autowired
	private StoreMenu storeMenu;
	
	@Autowired
	private ReportsMenu reportsMenu;
	
	public MainMenu() {
		menuList.add("Member Actions");
		menuList.add("Store Actions");
		menuList.add("Staff Actions");
		menuList.add("Supplier Actions");
		menuList.add("Warehouse Actions");
		menuList.add("Report Generation");
		menuList.add("Discount Actions");
		menuList.add("Product Actions");
		menuList.add("Billing Actions");		
		menuList.add("Exit");
	}
	
	@EventListener(ApplicationReadyEvent.class)
	public void loadMenu() {
		while (true) {
			printMainMenu();
			
			System.out.print("Enter choice: ");
			int choice = scan.nextInt();
			
			if (choice == menuList.size()) {
				System.out.println("Thank you!");
				break;
			}
			
			executeAction(choice);
		}
	}
	
	private void executeAction(int choice) {
		switch(choice) {
		case 1:
			membersMenu.loadMenu();
			break;
		case 2:
			storeMenu.loadMenu();
			break;
		case 3:
			staffMenu.loadMenu();
		case 4:
			suppliersMenu.loadMenu();
			break;
		case 5:
			warehouseMenu.loadMenu();
			break;
		case 6:
			reportsMenu.loadMenu();
			break;
		case 7:
			discountsMenu.loadMenu();
			break;
		case 8:
			productActionsMenu.loadMenu();
			break;
		case 9:
			billingMenu.loadMenu();
		default:
			System.out.println("Invalid choice!");
		}
	}

	private void printMainMenu() {
		System.out.println();
		System.out.println("MAIN MENU:");
		for (int i=0; i<menuList.size(); i++) {
			System.out.println(i+1 +": "+ menuList.get(i));
		}
	}
}
