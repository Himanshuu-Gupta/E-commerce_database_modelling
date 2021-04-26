package edu.ncsu.projects.dbms2.menu;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import edu.ncsu.projects.dbms2.dao.DiscountDao;
import edu.ncsu.projects.dbms2.dao.MemberDao;
import edu.ncsu.projects.dbms2.dao.MembershipDao;
import edu.ncsu.projects.dbms2.dao.ProductDao;
import edu.ncsu.projects.dbms2.entity.AddRenewMembership;
import edu.ncsu.projects.dbms2.entity.CancelMembership;
import edu.ncsu.projects.dbms2.entity.Member;
import edu.ncsu.projects.dbms2.entity.MemberTransaction;
import edu.ncsu.projects.dbms2.entity.MemberTransactionsInvolve;

@Component
public class MembersMenu {
	private final List<String> menuList = new ArrayList<>();
	private static Scanner scan = new Scanner(System.in);

	@Autowired
	private PlatformTransactionManager platformTransactionManager;
	
	@Autowired
	private DataSource ds;
	
	@Autowired
	private MemberDao memberDao;
	
	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private DiscountDao discountDao;
	
	@Autowired
	private MembershipDao membershipDao;
	
	public MembersMenu() {
		menuList.add("View all Members");
		menuList.add("View Member details by attribute");
		menuList.add("Add new Member");
		menuList.add("Update Member by ID");
		menuList.add("Delete Member by ID");
		menuList.add("Remove Member record from DB");
		menuList.add("Add Member Transaction");
		menuList.add("Generate Member Transaction Bill");
		menuList.add("Add Member Returns");
		menuList.add("Get member transactions");
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
		case 6:
			removeRecordFromDb();
			break;
		case 7:
			addMemberTransaction("ORDER");
			break;
		case 8:
			generateTransactionBill();
			break;
		case 9:
			addMemberTransaction("RETURN");
			break;
		case 10:
			getMemberTransactions();
			break;
		default:
			System.out.println("Invalid Choice");
		}
	}
	
	/**
	 * Get member transactions for a member ID input by the user.
	 */
	private void getMemberTransactions() {
		System.out.println("Enter member id: ");
		Integer memberId = scan.nextInt();
		
		List<MemberTransaction> memberTransactions = memberDao.getMemberTransactions(memberId);
		memberTransactions.forEach(System.out::println);
	}
	
	/**
	 * Generate bill and total bill price for a transaction ID input by the user.
	 */
	private void generateTransactionBill() {
		System.out.println("Enter transaction ID: ");
		Integer transactionId = scan.nextInt();
		
		List<MemberTransactionsInvolve> memberTransactionDetails = memberDao.getMemberTransactionDetails(transactionId);
		Double transactionTotalPrice = memberDao.generateMemberTransactionTotalPrice(transactionId);
		
		for (MemberTransactionsInvolve details : memberTransactionDetails) {
			System.out.println(details);
		}
		
		System.out.println("Total Price of the Transaction: "+ transactionTotalPrice);
	}
	
	/**
	 * Remove record from MEMBERS table for member ID input by the user. 
	 */
	private void removeRecordFromDb() {
		System.out.println("Enter member ID to remove: ");
		Integer memberId = scan.nextInt(); 
		
		int count = memberDao.removeFromDb(memberId);
		System.out.println("Successfully removed "+ count +" records from MEMBERS table.");
	}

	/**
	 * Workflow to add a new member transaction and transaction details. 
	 * @param transactionType ORDER/RETURN
	 */
	private void addMemberTransaction(String transactionType) {
		
		TransactionStatus checkpoint = platformTransactionManager.getTransaction(new DefaultTransactionDefinition()); // set a checkpoint using PlatformTransactionManager class
		
		try {
			System.out.println("Enter member ID: ");
			Integer memberId = scan.nextInt();
			
			System.out.println("Enter cashier ID: ");
			Integer cashierId = scan.nextInt();
			
			System.out.println("Enter store ID: ");
			Integer storeId = scan.nextInt();
			
			MemberTransaction transaction = new MemberTransaction();
			transaction.setCashierId(cashierId);
			transaction.setMemberId(memberId);
			transaction.setStoreId(storeId);
			transaction.setTransactionDate(new Date(System.currentTimeMillis()));
			transaction.setTransactionType(transactionType);
			
			Integer transactionId = memberDao.addMemberTransaction(transaction);
			
			System.out.println("Added transaction with transaction ID: "+ transactionId);
			
			this.addTransactionDetails(transactionId, storeId);
			
			System.out.println("Entered the transaction successfully!");
			
			platformTransactionManager.commit(checkpoint);	// commit the previous updates to the DB
		
		} catch (Exception e) {
			platformTransactionManager.rollback(checkpoint);	// rollback on error
			System.out.println("Error! Rolling back transaction!");
		}
	}
	
	private void addTransactionDetails(Integer transactionId, Integer storeId) throws Exception {
		System.out.println("Enter number of products involved: ");
		int productCount = scan.nextInt();
		
		if (productCount < 1) {
			throw new Exception("Cannot involve less than 1 product!");
		}
		
		List<MemberTransactionsInvolve> transactionDetailsList = new ArrayList<>();
		
		for (int i=0; i<productCount; i++) {
			System.out.println("Enter product ID for product "+ (i+1) +": ");
			Integer productId = scan.nextInt();
			
			System.out.println("Enter return quantity for product "+ (i+1) +": ");
			Integer quantity = scan.nextInt();
			if (quantity < 1) {
				throw new Exception("Cannot involve less than 1 product!");
			}
			
			MemberTransactionsInvolve details = new MemberTransactionsInvolve();
			details.setTransactionId(transactionId);
			details.setProductId(productId);
			details.setProductQuantity(quantity);
			details.setStoreId(storeId);
			
			details.setDiscountId(discountDao.getStoreProductDiscount(storeId, productId));
			
			Double storeProductPrice = productDao.getStoreProductPrice(productId, storeId);
			// details.setPrice(storeProductPrice);
			
			Double storeProductDiscountPercent = discountDao.getStoreProductDiscountPercent(storeId, productId);
			
			Double discountedStoreProductPrice = storeProductPrice - (storeProductPrice * storeProductDiscountPercent / 100);
			details.setTotalPrice(discountedStoreProductPrice * quantity);
			
			transactionDetailsList.add(details);
		}
		
		memberDao.addMemberTransactionDetails(transactionDetailsList);
	}
	
	/**
	 * Workflow to set a member status to inactive and cancel their membership.
	 */
	private void deleteMember() {
		System.out.println("Enter member ID to delete: ");
		Integer memberId = scan.nextInt();
		
		CancelMembership cancelMembership = new CancelMembership();
		cancelMembership.setCancelTime(new Date(System.currentTimeMillis()));
		cancelMembership.setMemberId(memberId);
		
		System.out.println("Enter Registration Operator ID: ");
		cancelMembership.setRegistrationOperatorId(scan.nextInt());
		
		cancelMembership.setMembershipId(memberDao.getMembershipId(memberId));
		
		int deletedMember = memberDao.deleteMember(memberId);
		
		memberDao.cancelMembership(cancelMembership);
		
		System.out.println("Deleted "+ deletedMember +" rows.");
	}
	
	/**
	 * Update given member attribute with given value for the member ID input by the user.
	 */
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
	
	/**
	 * Method to add a new user and a membership for the member.
	 */
	private void addMember() {
		Member member = new Member();
		AddRenewMembership membership = new AddRenewMembership();
		
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
		membership.setMembershipLevel(member.getMembershipLevel());
		
		System.out.println("Enter phone: ");
		member.setPhone(scan.next());
		
		System.out.println("Enter Registration Operator ID: ");
		membership.setRegistrationOperatorId(scan.nextInt());
		
		System.out.println("Enter Store ID: ");
		membership.setStoreId(scan.nextInt());
		
		membership.setStartDate(new Date(System.currentTimeMillis()));
		
		Double membershipDurationInMonths = membershipDao.getMembershipDurationInMonths(member.getMembershipLevel());
		membership.setEndDate(Date.valueOf(membership.getStartDate().toLocalDate().plusMonths(membershipDurationInMonths.longValue())));
		
		int addedMemberId = memberDao.addMember(member, membership);
		System.out.println("Added member with ID: "+ addedMemberId);
	}
	
	/**
	 * Get all members matching the given value of the given variable.  
	 */
	private void getMemberByAttribute() {
		System.out.print("Enter attribute name: ");
		String attributeName = scan.next().toUpperCase();
		
		scan.nextLine();
		System.out.println("Enter attribute value: ");
		String attributeValue = scan.nextLine();
		
		List<Member> members = memberDao.findByAttribute(attributeName, attributeValue);
		
		for (Member member : members) {
			System.out.println(member);
		}
	}
	
	/**
	 * Fetch all members from the database.
	 */
	private void getAllMembers() {
		List<Member> members = memberDao.findAll();
		
		for(Member member : members) {
			System.out.println(member);
		}
	}
	
	/**
	 * Load the Members menu.
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
	
	private void printMenu() {
		System.out.println("MEMBER ACTIONS:");
		for (int i=0; i<menuList.size(); i++) {
			System.out.println(i+1 +": "+ menuList.get(i));
		}
	}
}
