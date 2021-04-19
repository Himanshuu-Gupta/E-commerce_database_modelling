package edu.ncsu.projects.dbms2.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.ncsu.projects.dbms2.dao.BillingDao;
import edu.ncsu.projects.dbms2.entity.AllCustomerBills;
import edu.ncsu.projects.dbms2.entity.MemberReward;
import edu.ncsu.projects.dbms2.entity.MemberTransactionsInvolve;
import edu.ncsu.projects.dbms2.entity.StaffPaycheck;
import edu.ncsu.projects.dbms2.entity.WarehouseTransaction;

@Component
public class BillingMenu {
	private final List<String> menuList = new ArrayList<>();
	private static Scanner scan = new Scanner(System.in);
	
	@Autowired
	private BillingDao billingDao;

	
	public BillingMenu() {
		menuList.add("View Satff paychecks");
		menuList.add("View Satff paycheck for a Staff Id");
		menuList.add("Generate Staff Paycheck");
		menuList.add("View Supplier bills");
		menuList.add("View Supplier bills for a supplier");
		menuList.add("View Customer bills");
		menuList.add("View Customer bills for a customer");
		menuList.add("View customer reward details");
		menuList.add("View customer reward details for a customer");
		menuList.add("Generate reward for customer");
		menuList.add("Back to Main Menu");
	}

	private void executeAction(int choice) {
		switch(choice) {
		case 1: 
			getAllSaffPaychecks();
			break;
		case 2:
			getSaffPaycheckById();
			break;
		case 3:
			generateSaffPaycheck();
			break;
		case 4:
			getAllSupplierBills();
			break;
		case 5:
			getSupplierBillById();
			break;
		case 6:
			getAllCustomerBills();
			break;
		case 7:
			getAllCustomerBillById();
			break;
		case 8:
			getCustomerRewardDetails();
			break;
		case 9:
			getCustomerRewardDetailsById();
			break;
		case 10:
			getReward();
			break;
		default:
			System.out.println("Invalid Choice");
		}
	}
	
	/* The method returns the already generated paychecks for all staff members */
	private void getAllSaffPaychecks() {
		
		List<StaffPaycheck> paychecks = billingDao.paychecks();
		
		System.out.println(paychecks);
		}
	
	
	/* The method returns the already generated paychecks for a particular staff member */
	
	private void getSaffPaycheckById() {
		System.out.println("Enter staff ID to fetch paycheck: ");
		Integer staffId = scan.nextInt();
		
		List<StaffPaycheck> paychecks = billingDao.paychecksById(staffId);
		
		System.out.println(paychecks);
	}
	
	/*Get bills for all the suppliers basis existing transactions*/
	private void getAllSupplierBills() {
		billingDao.supplierBills();
		
	}
	
	/*Get bills for a specific supplier id basis existing transactions*/
	private void getSupplierBillById() {
		System.out.println("Enter supplier ID to fetch bills: ");
		Integer supplierId = scan.nextInt();
		
		List<WarehouseTransaction> supplierBills = billingDao.supplierBillsById(supplierId);
		System.out.println(supplierBills);
	}
	
	/*Get customer bills generated for all the transactions for any store*/	
	private void getAllCustomerBills() {
		List<AllCustomerBills> allcustomerTransactions = billingDao.allCustomerTransactions();
		System.out.println(allcustomerTransactions);
		
	}
	
	/*Get customer bills generated for all the transactions for any store for a specific customer*/
	private void getAllCustomerBillById() {
		System.out.println("Enter transaction ID to fetch customer bill: ");
		Integer transactionId = scan.nextInt();
		
		List<MemberTransactionsInvolve> customerTransaction = billingDao.customerTransactions(transactionId);
		System.out.println(customerTransaction);
		
		Float customerTransactionTotal = billingDao.getCustomerTransactionTotal(transactionId);
		System.out.println("The total bill for the transaction after discount is "+ customerTransactionTotal);
		
		
	}
	
	/*Get already generated reward details for all customers*/
	private void getCustomerRewardDetails() {
		List<MemberReward> rewards = billingDao.rewardChecks();
		System.out.println(rewards);
	}
	
	/*Get already generated reward details for all customers*/
	private void getCustomerRewardDetailsById() {
		System.out.println("Enter customerID: ");
		Integer memberId = scan.nextInt();
		
		List<MemberReward> rewards = billingDao.rewardChecksForMemberId(memberId);
		System.out.println(rewards);
	}

	/*Generate rewards for given customer for all the transactions in the mentioned year*/
	private void getReward() {
		System.out.println("Enter member id to generate reward:");
		Integer memberId = scan.nextInt();
		
		System.out.println("Enter billing staff id:");
		Integer billingStaffId = scan.nextInt();
		
		System.out.println("Enter year for reward generation:");
		Integer year = scan.nextInt();
		
		List<MemberReward> rewards = billingDao.generateMemberRewards(memberId,billingStaffId,year);
		System.out.println(rewards);
		
	}
	
	/*Generate paycheck for specific staff member*/
	private void generateSaffPaycheck() {
		System.out.println("Enter staff id for which to generate paycheck:");
		Integer staffId = scan.nextInt();
		
		System.out.println("Enter accountant staff id:");
		Integer acStaffId = scan.nextInt();
		
		System.out.println("Enter amount for paycheck:");
		Double amount = scan.nextDouble();
		
		List<StaffPaycheck> staffPay = billingDao.generateSaffPaychecks(staffId,acStaffId,amount);
		System.out.println(staffPay);
		
	}
	
	/*Loads the menu options listed at the start of the program for each section*/
	public void loadMenu() {
		while (true) {
			printStoreMenu();
			
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
	
	private void printStoreMenu() {
		System.out.println("BILLING ACTIONS:");
		for (int i=0; i<menuList.size(); i++) {
			System.out.println(i+1 +": "+ menuList.get(i));
		}
	}
	
	
	
	
	


}
