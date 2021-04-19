package edu.ncsu.projects.dbms2.dao;

import java.sql.Date;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class ReportsDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * Report to view sales of a specified store for every month.
	 * @param storeId Store ID
	 * @param fromDate From Date
	 * @param toDate To Date
	 * @return
	 */
	public List<Map<String, Object>> monthlySalesByStoreReport(Integer storeId, Date fromDate, Date toDate) {
		String sql = " SELECT YEAR(TRANSACTION_DATE) AS TRANSACTION_YEAR, MONTH(TRANSACTION_DATE) AS TRANSACTION_MONTH, STORE_ID, SUM(TOTAL_PRICE) AS TOTAL_SALES "
				+ " FROM MEMBER_TRANSACTIONS_INVOLVE MI " + " NATURAL JOIN MEMBER_TRANSACTIONS M "
				+ " WHERE M.STORE_ID = ? AND M.TRANSACTION_DATE >= ? AND M.TRANSACTION_DATE <= ? "
				+ " GROUP BY TRANSACTION_YEAR, TRANSACTION_MONTH, STORE_ID ";

		return jdbcTemplate.queryForList(sql, new Object[] {storeId, fromDate, toDate}, new int[] {Types.INTEGER, Types.DATE, Types.DATE});
	}
	
	/**
	 * Report to view sales of a specified store for every year.
	 * @param storeId Store ID
	 * @param fromDate From Date 
	 * @param toDate To Date
	 * @return
	 */
	public List<Map<String, Object>> yearlySalesByStoreReport(Integer storeId, Date fromDate, Date toDate) {
		String sql = " SELECT YEAR(TRANSACTION_DATE) AS TRANSACTION_YEAR, STORE_ID, SUM(TOTAL_PRICE) AS TOTAL_SALES "
				+ " FROM MEMBER_TRANSACTIONS_INVOLVE MI " + " NATURAL JOIN MEMBER_TRANSACTIONS M "
				+ " WHERE M.STORE_ID = ? AND M.TRANSACTION_DATE >= ? AND M.TRANSACTION_DATE <= ? "
				+ " GROUP BY TRANSACTION_YEAR, STORE_ID ";
		
		return jdbcTemplate.queryForList(sql, new Object[] {storeId, fromDate, toDate}, new int[] {Types.INTEGER, Types.DATE, Types.DATE});
	}
	
	/**
	 * Report to view sales of all stores in chain for every month.
	 * @param fromDate From Date
	 * @param toDate To Date
	 * @return
	 */
	public List<Map<String, Object>> chainMonthlySalesReport(Date fromDate, Date toDate) {
		String sql = " SELECT MONTH(TRANSACTION_DATE) AS TRANSACTION_MONTH, STORE_ID, SUM(TOTAL_PRICE) AS TOTAL_SALES "
				+ " FROM MEMBER_TRANSACTIONS_INVOLVE MI " + " NATURAL JOIN MEMBER_TRANSACTIONS M "
				+ " WHERE M.TRANSACTION_DATE >= ? AND M.TRANSACTION_DATE <= ? "
				+ " GROUP BY TRANSACTION_MONTH, STORE_ID ";
		
		return jdbcTemplate.queryForList(sql, new Object[] {fromDate, toDate}, new int[] {Types.DATE, Types.DATE});
	}
	
	/**
	 * Report to view sales of all stores in chain for every year.
	 * @param fromDate From Date
	 * @param toDate To Date
	 * @return
	 */
	public List<Map<String, Object>> chainYearlySalesReport(Date fromDate, Date toDate) {
		String sql = " SELECT YEAR(TRANSACTION_DATE) AS TRANSACTION_YEAR, STORE_ID, SUM(TOTAL_PRICE) AS TOTAL_SALES "
				+ " FROM MEMBER_TRANSACTIONS_INVOLVE MI " + " NATURAL JOIN MEMBER_TRANSACTIONS M "
				+ " WHERE M.TRANSACTION_DATE >= ? AND M.TRANSACTION_DATE <= ? "
				+ " GROUP BY TRANSACTION_YEAR, STORE_ID ";
		
		return jdbcTemplate.queryForList(sql, new Object[] {fromDate, toDate}, new int[] {Types.DATE, Types.DATE});
	}
	
	/**
	 * Report to view merchandise inventory of a specified store.
	 * @param storeId Store ID
	 * @return
	 */
	public List<Map<String, Object>> merchStoreReport(Integer storeId) {
		String sql = " SELECT * FROM STORE_INVENTORY WHERE STORE_ID = ? ";
		
		return jdbcTemplate.queryForList(sql, new Object[] {storeId}, new int[] {Types.DATE});
	}
	
	/**
	 * Report to view the monthly customer memberships in the entire chain.
	 * @param fromDate From Date
	 * @param toDate To Date
	 * @return
	 */
	public List<Map<String, Object>> monthlyCustomerGrowthReport(Date fromDate, Date toDate) {
		String sql = " SELECT YEAR(STARTUP_DATE) AS SIGNUP_YEAR, MONTH(STARTUP_DATE) AS SIGNUP_MONTH, COUNT(DISTINCT MEMBER_ID) AS CUSTOMERS "
				+ " FROM ADD_RENEW_MEMBERSHIPS WHERE STARTUP_DATE >= ? AND STARTUP_DATE <= ? "
				+ " GROUP BY SIGNUP_YEAR, SIGNUP_MONTH ";
		
		return jdbcTemplate.queryForList(sql, new Object[] {fromDate, toDate}, new int[] {Types.DATE, Types.DATE});
	}
	
	/**
	 * Report to view the yearly customer memberships in the entire chain.
	 * @param fromDate From Date
	 * @param toDate To Date
	 * @return
	 */
	public List<Map<String,Object>> yearlyCustomerGrowthReport(Date fromDate, Date toDate) {
		String sql = " SELECT YEAR(STARTUP_DATE) AS SIGNUP_YEAR, COUNT(DISTINCT MEMBER_ID) AS CUSTOMERS "
				+ " FROM ADD_RENEW_MEMBERSHIPS WHERE STARTUP_DATE >= ? AND STARTUP_DATE <= ? "
				+ " GROUP BY SIGNUP_YEAR ";
		
		return jdbcTemplate.queryForList(sql, new Object[] {fromDate, toDate}, new int[] {Types.DATE, Types.DATE});
	}
	
	/**
	 * Report of a particular customer spendings in a given duration. 
	 * @param memberId Member ID
	 * @param fromDate From Date
	 * @param toDate To Date
	 * @return
	 */
	public List<Map<String,Object>> customerActivityReport(Integer memberId, Date fromDate, Date toDate) {
		String sql = " SELECT MEMBER_ID, SUM(TOTAL_PRICE) AS BILL_AMOUNT FROM MEMBER_TRANSACTIONS M NATURAL JOIN MEMBER_TRANSACTIONS_INVOLVE MI "
				+ " WHERE TRANSACTION_DATE BETWEEN ? AND ? AND MEMBER_ID = ? GROUP BY MEMBER_ID ";
		
		return jdbcTemplate.queryForList(sql, new Object[] {fromDate, toDate, memberId}, new int[] {Types.DATE, Types.DATE, Types.INTEGER});
	}
}
