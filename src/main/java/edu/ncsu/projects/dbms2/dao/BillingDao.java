package edu.ncsu.projects.dbms2.dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Component;

import edu.ncsu.projects.dbms2.entity.AllCustomerBills;
import edu.ncsu.projects.dbms2.entity.MemberReward;
import edu.ncsu.projects.dbms2.entity.MemberTransactionsInvolve;
import edu.ncsu.projects.dbms2.entity.StaffPaycheck;
import edu.ncsu.projects.dbms2.entity.WarehouseTransaction;
import java.util.*;

@Component
public class BillingDao {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public void rewardChecks(){
		String sql = " SELECT * FROM MEMBER_REWARDS ";
		System.out.println(jdbcTemplate.queryForList(sql));

	}
	
	
	/*Generate rewards for given customer basis transactions for given transaction year.*/
	public void generateMemberRewards(Integer memberId, Integer billingStaffId, Integer year){
		String sql = " SELECT (TOTAL_TRANSACTION_AMOUNT* CASHBACK_REWARD_PERCENT) AS CASHBACK  "
				+ " FROM(SELECT MT.MEMBER_ID,MEMBERSHIP_LEVEL,SUM(TOTAL_PRICE) AS TOTAL_TRANSACTION_AMOUNT FROM "
				+ " MEMBER_TRANSACTIONS_INVOLVE MTI "
				+ " INNER JOIN MEMBER_TRANSACTIONS MT "
				+ " ON MTI.TRANSACTION_ID = MT.TRANSACTION_ID "
				+ " INNER JOIN MEMBERS M "
				+ " ON MT.MEMBER_ID = M.MEMBER_ID "
				+ " WHERE YEAR(TRANSACTION_DATE) = "+year
				+ " AND MT.MEMBER_ID = "+memberId
				+ " GROUP BY "
				+ " MEMBER_ID, "
				+ " MEMBERSHIP_LEVEL) M "
				+ " LEFT JOIN MEMBERSHIPS MS "
				+ " ON M.MEMBERSHIP_LEVEL = MS.MEMBERSHIP_LEVEL ";
			
		
		float rewardAmount = jdbcTemplate.queryForObject(sql, Float.class);
		
		String sql2 = "INSERT INTO MEMBER_REWARDS VALUES (null,"+billingStaffId+","+memberId+","+rewardAmount+",SYSDATE())";
		
		jdbcTemplate.update(sql2);
		
		String sql3 = "SELECT * FROM MEMBER_REWARDS";
		
		System.out.println(jdbcTemplate.queryForList(sql3));

//		return jdbcTemplate.query(sql3, new BeanPropertyRowMapper<MemberReward>(MemberReward.class));
	
	}
	
	
	/*Generate rewards for given customer basis transactions for given transaction year.*/
	public void rewardChecksForMemberId(Integer memberId){
		String sql = " SELECT * FROM MEMBER_REWARDS WHERE MEMBER_ID ="+ memberId;
		
		System.out.println(jdbcTemplate.queryForList(sql));
//		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<MemberReward>(MemberReward.class));
	}
	
	/*Get bills for all the suppliers basis existing transactions*/
	public  void supplierBills() {
		String sql = " SELECT TRANSACTION_ID, TRANSACTION_DATE, SUPPLIER_ID, SUM(BUY_PRICE*STOCK_BOUGHT) AS TOTAL_PRICE "
				+ " FROM WAREHOUSE_TRANSACTION "
				+ " GROUP BY TRANSACTION_ID, TRANSACTION_DATE, SUPPLIER_ID";
				
		List<Map<String, Object>> res = jdbcTemplate.queryForList(sql);
		
		jdbcTemplate.query(sql, new RowCallbackHandler() {

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				System.out.println("TRANSACTION_ID :: TRANSACTION_DATE :: SUPPLIER_ID :: TOTAL_PRICE");

				while (rs.next()) {
					System.out.println(rs.getInt("TRANSACTION_ID") + " :: " + rs.getDate("TRANSACTION_DATE") + " :: " + rs.getInt("SUPPLIER_ID") + " :: " + rs.getDouble("TOTAL_PRICE"));
				}
			}
		});
		
	}
	
	/*Get bills for a specific supplier id basis existing transactions*/
	public void supplierBillsById(Integer supplierId) {
		String sql = " SELECT TRANSACTION_ID, TRANSACTION_DATE, SUPPLIER_ID, SUM(BUY_PRICE*STOCK_BOUGHT) AS TOTAL_PRICE "
				+ " FROM WAREHOUSE_TRANSACTION "
				+ " WHERE SUPPLIER_ID ="+supplierId
				+ " GROUP BY TRANSACTION_ID, TRANSACTION_DATE, SUPPLIER_ID";
				
		jdbcTemplate.query(sql, new RowCallbackHandler() {

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				System.out.println("TRANSACTION_ID :: TRANSACTION_DATE :: SUPPLIER_ID :: TOTAL_PRICE");

				while (rs.next()) {
					System.out.println(rs.getInt("TRANSACTION_ID") + " :: " + rs.getDate("TRANSACTION_DATE") + " :: " + rs.getInt("SUPPLIER_ID") + " :: " + rs.getDouble("TOTAL_PRICE"));
				}
			}
		});
//		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<WarehouseTransaction>(WarehouseTransaction.class));
		
	}
	
	/*Fetch paycheck details for all the staff members*/
	public void paychecks() {		
		String sql = " SELECT * FROM STAFF_PAYCHECKS ";
		
		System.out.println(jdbcTemplate.queryForList(sql));
		
		// return jdbcTemplate.query(sql, new BeanPropertyRowMapper<StaffPaycheck>(StaffPaycheck.class));
		
	}
	
	/*Fetch paycheck details for a given staff member*/
	public void paychecksById(Integer staffId) {		
		String sql = " SELECT * FROM STAFF_PAYCHECKS WHERE STAFF_ID = "+staffId;
		
		System.out.println(jdbcTemplate.queryForList(sql));
		
		// return jdbcTemplate.query(sql, new BeanPropertyRowMapper<StaffPaycheck>(StaffPaycheck.class));
		
	}
	
	/*Get details for customer bill generated for a given transaction id */	
	public List<MemberTransactionsInvolve> customerTransactions(Integer transactionId) {
		String sql = " SELECT * FROM MEMBER_TRANSACTIONS_INVOLVE WHERE TRANSACTION_ID ="+transactionId;
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<MemberTransactionsInvolve>(MemberTransactionsInvolve.class));
	}
	
	/*Get total customer bill amount for the given transaction id */	
	public Float getCustomerTransactionTotal(Integer transactionId) {
		String sql = " SELECT SUM(TOTAL_PRICE) AS TOTAL_PRICE "
				+ " FROM MEMBER_TRANSACTIONS_INVOLVE "
				+ " WHERE TRANSACTION_ID ="+transactionId;
		return jdbcTemplate.queryForObject(sql, Float.class);
	}
	
	/*Get customer bills generated for all the transactions for any store*/	
	public void allCustomerTransactions() {
		String sql = " SELECT MEMBER_ID, TRANSACTION_DATE, SUM(TOTAL_PRICE) AS TOTAL_PRICE "
				+ " FROM MEMBER_TRANSACTIONS_INVOLVE MTI "
				+ " INNER JOIN MEMBER_TRANSACTIONS MI "
				+ " ON MTI.TRANSACTION_ID = MI.TRANSACTION_ID "
				+ " GROUP BY MEMBER_ID, TRANSACTION_DATE ";
		
		System.out.println(jdbcTemplate.queryForList(sql));
		
		// return jdbcTemplate.query(sql, new BeanPropertyRowMapper<AllCustomerBills>(AllCustomerBills.class));
		
	}
	
	/*Generate paycheck for specific staff member*/
	public void generateSaffPaychecks(Integer staffId,Integer acStaffId, Double amount) {
		String sql = " INSERT INTO STAFF_PAYCHECKS VALUES (null,"+acStaffId+","+staffId+",SYSDATE(),"+amount+");";
		jdbcTemplate.update(sql);
		
		this.paychecks();
	}
	

	
}
