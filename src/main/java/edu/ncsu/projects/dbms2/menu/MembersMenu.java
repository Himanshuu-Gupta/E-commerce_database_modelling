package edu.ncsu.projects.dbms2.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.ncsu.projects.dbms2.dao.MemberDao;
import edu.ncsu.projects.dbms2.entity.Books;
import edu.ncsu.projects.dbms2.entity.Member;
import edu.ncsu.projects.dbms2.service.BooksService;
import edu.ncsu.projects.dbms2.service.MemberService;

@Component
public class MembersMenu {
	private final List<String> menuList = new ArrayList<>();
	private static Scanner scan = new Scanner(System.in);
	
	@Autowired
	private MemberDao memberDao;

	public MembersMenu() {
		menuList.add("View all Members");
		menuList.add("View Member details by attribute");
		menuList.add("Add new Member");
		menuList.add("Update Member by ID");
		menuList.add("Delete customer by ID");
		menuList.add("Back to Main Menu");
	}

	private void executeAction(int choice) {
		switch(choice) {
		case 1: 
			getAllMembers();
			break;
		case 2:
			getMemberByAttribute();
			break;
		case 3:
			addMember();
			break;
		case 4:
			updateMember();
			break;
		case 5:
			deleteMember();
			break;
		default:
			System.out.println("Invalid Choice");
		}
	}
	
	private void deleteMember() {
		System.out.println("Enter member ID to delete: ");
		Integer memberId = scan.nextInt();
		
		int deletedMember = memberDao.deleteMember(memberId);
		
		System.out.println("Deleted "+ deletedMember +" rows.");
	}

	private void updateMember() {
		System.out.println("Enter attribute name: ");
		String attributeName = scan.next().toUpperCase();
		
		scan.nextLine();
		System.out.println("Enter attribute value: ");
		String attributeValue = scan.nextLine();
		
		System.out.println("Enter member ID to update: ");
		Integer memberId = scan.nextInt();
		
		int updateCount = memberDao.updateByAttribute(attributeName, attributeValue, memberId);
		
		System.out.println("Updated "+ updateCount +" rows in MEMBERS table.");
	}

	private void addMember() {
		Member member = new Member();
		
		scan.nextLine();
		System.out.println("Enter first name: ");
		member.setFirstName(scan.nextLine());
		
		scan.nextLine();
		System.out.println("Enter last name: ");
		member.setLastName(scan.nextLine());
		
		System.out.println("Enter active status (true/false): ");
		member.setActiveStatus(scan.nextBoolean());
		
		scan.nextLine();
		System.out.println("Enter address: ");
		member.setAddress(scan.nextLine());
		
		System.out.println("Enter email: ");
		member.setEmail(scan.next());
		
		System.out.println("Enter membership level: ");
		member.setMembershipLevel(scan.next());
		
		System.out.println("Enter phone: ");
		member.setPhone(scan.next());
		
		int count = memberDao.addMember(member);
		System.out.println("Added "+ count +" rows.");
	}

	private void getMemberByAttribute() {
		System.out.print("Enter attribute name: ");
		String attributeName = scan.next().toUpperCase();
		
		scan.nextLine();
		System.out.println("Enter attribute value: ");
		String attributeValue = scan.nextLine();
		
		Member member = memberDao.findByAttribute(attributeName, attributeValue);
		
		System.out.println(member);
	}

	private void getAllMembers() {
		// List<Member> members = memberService.getAllMembers();
		List<Member> members = memberDao.findAll();
		
		for(Member member : members) {
			System.out.println(member);
		}
	}
	
	public void loadMenu() {
		while (true) {
			printCustomerMenu();
			
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
	
	private void printCustomerMenu() {
		System.out.println("CUSTOMER ACTIONS:");
		for (int i=0; i<menuList.size(); i++) {
			System.out.println(i+1 +": "+ menuList.get(i));
		}
	}
}
