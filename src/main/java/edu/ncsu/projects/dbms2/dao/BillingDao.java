package edu.ncsu.projects.dbms2.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import edu.ncsu.projects.dbms2.entity.AllCustomerBills;
import edu.ncsu.projects.dbms2.entity.MemberReward;
import edu.ncsu.projects.dbms2.entity.MemberTransactionsInvolve;
import edu.ncsu.projects.dbms2.entity.StaffPaycheck;
import edu.ncsu.projects.dbms2.entity.WarehouseTransaction;

@Component
public class BillingDao {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public List<MemberReward> rewardChecks(){
		String sql = " SELECT * FROM MEMBER_REWARDS ";
		
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<MemberReward>(MemberReward.class));
	}
	
	public List<MemberReward> generateMemberRewards(Integer memberId, Integer billingStaffId, Integer year){
		String sql = " SELECT SUM(TOTAL_PRICE)*CASHBACK_REWARD FROM "
				+ "MEMBER_TRANSACTIONS_INVOLVE MTI "
				+ "INNER JOIN MEMBER_TRANSACTIONS MT "
				+ "ON MTI.TRANSACTION_ID = MT.TRANSACTION_ID "
				+ "INNER JOIN MEMBERS M"
				+ "ON MT.MEMBER_ID = M.MEMBER_ID"
				+ "WHERE YEAR(TRANSACTION_DATE) = "+year
				+ "AND MT.MEMBER_ID = "+memberId;
		
		float rewardAmount = jdbcTemplate.queryForObject(sql, Float.class);
		
		String sql2 = "INSERT INTO MEMBER_REWARDS VALUES (null,"+billingStaffId+","+memberId+","+rewardAmount+",SYSDATE();";
		
		jdbcTemplate.update(sql2);
		
		String sql3 = "SELECT * FROM MEMBER_REWARDS";
		return jdbcTemplate.query(sql3, new BeanPropertyRowMapper<MemberReward>(MemberReward.class));
	
	}
	
	public List<MemberReward> rewardChecksForMemberId(Integer memberId){
		String sql = " SELECT * FROM MEMBER_REWARDS WHERE MEMBER_ID ="+ memberId;
		
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<MemberReward>(MemberReward.class));
	}
	
	public List<WarehouseTransaction> supplierBills() {
		String sql = " SELECT TRANSACTION_ID, TRANSACTION_DATE, SUPPLIER_ID, SUM(PRICE*STOCK_BOUGHT) AS TOTAL_PRICE "
				+ "FROM WAREHOUSE_TRANSACTION "
				+ "GROUP BY TRANSACTION_ID, TRANSACTION_DATE, SUPPLIER_ID";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<WarehouseTransaction>(WarehouseTransaction.class));
		
	}
	
	public List<WarehouseTransaction> supplierBillsById(Integer supplierId) {
		String sql = " SELECT TRANSACTION_ID, TRANSACTION_DATE, SUPPLIER_ID, SUM(PRICE*STOCK_BOUGHT) AS TOTAL_PRICE "
				+ "FROM WAREHOUSE_TRANSACTION "
				+ "WHERE SUPPLIER_ID ="+supplierId
				+ "GROUP BY TRANSACTION_ID, TRANSACTION_DATE, SUPPLIER_ID";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<WarehouseTransaction>(WarehouseTransaction.class));
		
	}
	
	public List<StaffPaycheck> paychecks() {		
		String sql = " SELECT * FROM STAFF_PAYCHECKS ";
		
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<StaffPaycheck>(StaffPaycheck.class));
		
	}
	
	public List<StaffPaycheck> paychecksById(Integer staffId) {		
		String sql = " SELECT * FROM STAFF_PAYCHECKS WHERE STAFF_ID = "+staffId;
		
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<StaffPaycheck>(StaffPaycheck.class));
		
	}
	public List<MemberTransactionsInvolve> customerTransactions(Integer transactionId) {
		String sql = " SELECT * FROM MEMBER_TRANSACTIONS_INVOLVE WHERE TRANSACTION_ID ="+transactionId;
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<MemberTransactionsInvolve>(MemberTransactionsInvolve.class));
	}
	
	public Float getCustomerTransactionTotal(Integer transactionId) {
		String sql = " SELECT SUM(TOTAL_PRICE) AS TOTAL_PRICE "
				+ "FROM MEMBER_TRANSACTIONS_INVOLVE "
				+ "WHERE TRANSACTION_ID ="+transactionId;
		return jdbcTemplate.queryForObject(sql, Float.class);
	}
	
	public List<AllCustomerBills> allCustomerTransactions() {
		String sql = " SELECT MEMBER_ID, DATE, SUM(TOTAL_PRICE) AS TOTAL_PRICE "
				+ "FROM MEMBER_TRANSACTIONS_INVOLVE MTI"
				+ "INNER JOIN MEMBER_TRANSACTIONS MI"
				+ "ON MTI.TRANSACTION_ID = MI.TRANSACTION_ID "
				+ "GROUP BY MEMBER_ID, DATE;";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<AllCustomerBills>(AllCustomerBills.class));
		
	}
	
	public List<StaffPaycheck> generateSaffPaychecks(Integer staffId,Integer acStaffId, Double amount) {
		String sql = " INSERT INTO STAFF_PAYCHECKS VALUES (null,"+acStaffId+","+staffId+",SYSDATE(),"+amount+");";
		jdbcTemplate.update(sql);
		
		String sql2 = " SELECT * FROM STAFF_PAYCHECKS;"; 
		return jdbcTemplate.query(sql2, new BeanPropertyRowMapper<StaffPaycheck>(StaffPaycheck.class)); 
			
	}
	
	
}
