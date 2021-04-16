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
	private MembersMenu customerMenu;
	
	public MainMenu() {
		menuList.add("Customer Actions");
		menuList.add("Store Actions");
		menuList.add("Staff Actions");
		menuList.add("Supplier Actions");
		menuList.add("Warehouse Actions");
		menuList.add("Report Generation");
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
			customerMenu.loadMenu();
			break;
		default:
			System.out.println("Invalid choice!");
		}
	}

	private void printMainMenu() {
		System.out.println("MAIN MENU:");
		for (int i=0; i<menuList.size(); i++) {
			System.out.println(i+1 +": "+ menuList.get(i));
		}
	}
}
