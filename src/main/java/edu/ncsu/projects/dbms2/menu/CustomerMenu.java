package edu.ncsu.projects.dbms2.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.ncsu.projects.dbms2.entity.Books;
import edu.ncsu.projects.dbms2.entity.Member;
import edu.ncsu.projects.dbms2.service.BooksService;
import edu.ncsu.projects.dbms2.service.MemberService;

@Component
public class CustomerMenu {
	private final List<String> menuList = new ArrayList<>();
	private static Scanner scan = new Scanner(System.in);
	
	@Autowired
	private BooksService booksService;
	
	@Autowired
	private MemberService memberService;

	public CustomerMenu() {
		menuList.add("View all Books");
		menuList.add("View customer details by ID");
		menuList.add("Search by customer name");
		menuList.add("Add new customer");
		menuList.add("Delete customer by ID");
		menuList.add("Back to Main Menu");
	}
	
	public void loadMenu() {
		while (true) {
			printCustomerMenu();
			
			System.out.print("Enter choice: ");
			int choice = scan.nextInt();
			
			if (choice == menuList.size()) {
				break;
			}
			
			executeAction(choice);
		}
	}

	private void executeAction(int choice) {
		switch(choice) {
		case 1: 
			// List<Books> allBooks = booksService.getAllBooks();
			List<Member> members = memberService.getAllMembers();
			for(Member member:members) {
				System.out.println(member);
			}
			break;
		default:
			System.out.println("Invalid Choice");
		}
	}

	private void printCustomerMenu() {
		System.out.println("CUSTOMER ACTIONS:");
		for (int i=0; i<menuList.size(); i++) {
			System.out.println(i+1 +": "+ menuList.get(i));
		}
	}

}
