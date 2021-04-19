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
	
	public List<MemberReward> rewardChecks(){
		String sql = " SELECT * FROM MEMBER_REWARDS ";
		
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<MemberReward>(MemberReward.class));
	}
	
	public List<MemberReward> generateMemberRewards(Integer memberId, Integer billingStaffId, Integer year){
		String sql = " SELECT MEMBER_ID, (TOTAL_TRANSACTION_AMOUNT* CASHBACK_REWARD) AS CASHBACK  "
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
		
		String sql2 = "INSERT INTO MEMBER_REWARDS VALUES (null,"+billingStaffId+","+memberId+","+rewardAmount+",SYSDATE();";
		
		jdbcTemplate.update(sql2);
		
		String sql3 = "SELECT * FROM MEMBER_REWARDS";
		return jdbcTemplate.query(sql3, new BeanPropertyRowMapper<MemberReward>(MemberReward.class));
	
	}
	
	public List<MemberReward> rewardChecksForMemberId(Integer memberId){
		String sql = " SELECT * FROM MEMBER_REWARDS WHERE MEMBER_ID ="+ memberId;
		
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<MemberReward>(MemberReward.class));
	}
	
	
	
	
	
//	List<WarehouseTransaction>
	public  void supplierBills() {
		String sql = " SELECT TRANSACTION_ID, TRANSACTION_DATE, SUPPLIER_ID, SUM(PRICE*STOCK_BOUGHT) AS TOTAL_PRICE "
				+ " FROM WAREHOUSE_TRANSACTION "
				+ " GROUP BY TRANSACTION_ID, TRANSACTION_DATE, SUPPLIER_ID";
				
		List<Map<String, Object>> res = jdbcTemplate.queryForList(sql);
		
//		System.out.println(res);
		
//		this.simpleTable(res);
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
	
	public List<WarehouseTransaction> supplierBillsById(Integer supplierId) {
		String sql = " SELECT TRANSACTION_ID, TRANSACTION_DATE, SUPPLIER_ID, SUM(PRICE*STOCK_BOUGHT) AS TOTAL_PRICE "
				+ " FROM WAREHOUSE_TRANSACTION "
				+ " WHERE SUPPLIER_ID ="+supplierId
				+ " GROUP BY TRANSACTION_ID, TRANSACTION_DATE, SUPPLIER_ID";
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
				+ " FROM MEMBER_TRANSACTIONS_INVOLVE "
				+ " WHERE TRANSACTION_ID ="+transactionId;
		return jdbcTemplate.queryForObject(sql, Float.class);
	}
	
	public List<AllCustomerBills> allCustomerTransactions() {
		String sql = " SELECT MEMBER_ID, TRANSACTION_DATE, SUM(TOTAL_PRICE) AS TOTAL_PRICE "
				+ " FROM MEMBER_TRANSACTIONS_INVOLVE MTI "
				+ " INNER JOIN MEMBER_TRANSACTIONS MI "
				+ " ON MTI.TRANSACTION_ID = MI.TRANSACTION_ID "
				+ " GROUP BY MEMBER_ID, TRANSACTION_DATE;";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<AllCustomerBills>(AllCustomerBills.class));
		
	}
	
	public List<StaffPaycheck> generateSaffPaychecks(Integer staffId,Integer acStaffId, Double amount) {
		String sql = " INSERT INTO STAFF_PAYCHECKS VALUES (null,"+acStaffId+","+staffId+",SYSDATE(),"+amount+");";
		jdbcTemplate.update(sql);
		
		String sql2 = " SELECT * FROM STAFF_PAYCHECKS;"; 
		return jdbcTemplate.query(sql2, new BeanPropertyRowMapper<StaffPaycheck>(StaffPaycheck.class)); 
			
	}
	
//	public static void simpleTable(List table) {
//		 
//		boolean leftJustifiedRows = false;
//	 
//		Map<Integer, Integer> columnLengths = new HashMap<>();
//		List.stream(table).forEach(a -> Stream.iterate(0, (i -> i < a.length), (i -> ++i)).forEach(i -> {
//			if (columnLengths.get(i) == null) {
//				columnLengths.put(i, 0);
//			}
//			if (columnLengths.get(i) < a[i].length()) {
//				columnLengths.put(i, a[i].length());
//			}
//		}));
//		System.out.println("columnLengths = " + columnLengths);
//	 
//		/*
//		 * Prepare format String
//		 */
//		final StringBuilder formatString = new StringBuilder("");
//		String flag = leftJustifiedRows ? "-" : "";
//		columnLengths.entrySet().stream().forEach(e -> formatString.append("| %" + flag + e.getValue() + "s "));
//		formatString.append("|\n");
//		System.out.println("formatString = " + formatString.toString());
//	 
//		Stream.iterate(0, (i -> i < table.length()), (i -> ++i))
//				.forEach(a -> System.out.printf(formatString.toString(), table[a]));
//	 
//	}
//	
//	  
	
	
}
