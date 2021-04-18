package edu.ncsu.projects.dbms2.dao;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Component;

@Component
public class ReportsDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public void monthlySalesByStoreReport(Integer storeId, Date fromDate, Date toDate) {
		String sql = " SELECT MONTH(TRANSACTION_DATE) AS TRANSACTION_MONTH, STORE_ID, SUM(TOTAL_PRICE) AS TOTAL_SALES "
				+ " FROM MEMBER_TRANSACTIONS_INVOLVE MI "
				+ " NATURAL JOIN MEMBER_TRANSACTIONS M "
				+ " WHERE M.STORE_ID = ? AND M.TRANSACTION_DATE >= ? AND M.TRANSACTION_DATE <= ? "
				+ " GROUP BY TRANSACTION_MONTH, STORE_ID ";
		
		jdbcTemplate.query(sql, new RowCallbackHandler() {
			
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				System.out.println("TRANSACTION_MONTH :: STORE_ID :: TOTAL_SALES");
				while(rs.next()) {
					System.out.print(rs.getDate("TRANSACTION_MONTH") +" :: "+ rs.getInt("STORE_ID") +" :: "+ rs.getDouble("TOTAL_SALES"));
				}
			}
		}, storeId, fromDate, toDate);
	}
	
	public void yearlySalesByStoreReport(Integer storeId, Date fromDate, Date toDate) {
		String sql = " SELECT YEAR(TRANSACTION_DATE) AS TRANSACTION_YEAR, STORE_ID, SUM(TOTAL_PRICE) AS TOTAL_SALES "
				+ " FROM MEMBER_TRANSACTIONS_INVOLVE MI "
				+ " NATURAL JOIN MEMBER_TRANSACTIONS M "
				+ " WHERE M.STORE_ID = ? AND M.TRANSACTION_DATE >= ? AND M.TRANSACTION_DATE <= ? "
				+ " GROUP BY TRANSACTION_YEAR, STORE_ID ";
		
		jdbcTemplate.query(sql, new RowCallbackHandler() {
			
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				System.out.println("TRANSACTION_YEAR :: STORE_ID :: TOTAL_SALES");
				while(rs.next()) {
					System.out.print(rs.getDate("TRANSACTION_YEAR") +" :: "+ rs.getInt("STORE_ID") +" :: "+ rs.getDouble("TOTAL_SALES"));
				}
			}
		}, storeId, fromDate, toDate);
	}
	
	public void chainMonthlySalesReport(Date fromDate, Date toDate) {
		String sql = " SELECT MONTH(TRANSACTION_DATE) AS TRANSACTION_MONTH, STORE_ID, SUM(TOTAL_PRICE) AS TOTAL_SALES "
				+ " FROM MEMBER_TRANSACTIONS_INVOLVE MI "
				+ " NATURAL JOIN MEMBER_TRANSACTIONS M "
				+ " WHERE M.TRANSACTION_DATE >= ? AND M.TRANSACTION_DATE <= ? "
				+ " GROUP BY TRANSACTION_MONTH, STORE_ID ";
		
		jdbcTemplate.query(sql, new RowCallbackHandler() {
			
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				System.out.println("TRANSACTION_MONTH :: STORE_ID :: TOTAL_SALES");
				while(rs.next()) {
					System.out.print(rs.getDate("TRANSACTION_MONTH") +" :: "+ rs.getInt("STORE_ID") +" :: "+ rs.getDouble("TOTAL_SALES"));
				}
			}
		}, fromDate, toDate);
	}
	
	public void chainYearlySalesReport(Date fromDate, Date toDate) {
		String sql = " SELECT YEAR(TRANSACTION_DATE) AS TRANSACTION_YEAR, STORE_ID, SUM(TOTAL_PRICE) AS TOTAL_SALES "
				+ " FROM MEMBER_TRANSACTIONS_INVOLVE MI "
				+ " NATURAL JOIN MEMBER_TRANSACTIONS M "
				+ " WHERE M.TRANSACTION_DATE >= ? AND M.TRANSACTION_DATE <= ? "
				+ " GROUP BY TRANSACTION_YEAR, STORE_ID ";
		
		jdbcTemplate.query(sql, new RowCallbackHandler() {
			
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				System.out.println("TRANSACTION_YEAR :: STORE_ID :: TOTAL_SALES");
				while(rs.next()) {
					System.out.print(rs.getDate("TRANSACTION_YEAR") +" :: "+ rs.getInt("STORE_ID") +" :: "+ rs.getDouble("TOTAL_SALES"));
				}
			}
		}, fromDate, toDate);
	}
	
	public void merchStoreReport(Integer storeId) {
		String sql = " SELECT * FROM STORE_INVENTORY SI WHERE STORE_ID = ? ";
		
		jdbcTemplate.query(sql, new RowCallbackHandler() {
			
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				System.out.println("PRODUCT_ID :: PRODUCT_NAME :: PRICE :: STOCK_QUANTITY :: PRODUCTION_DATE :: c");
				
				while(rs.next()) {
					System.out.print(rs.getInt("PRODUCT_ID") +" :: "+ rs.getString("PRODUCT_NAME") +" :: "+ rs.getDouble("PRICE")
					+" :: "+ rs.getInt("STOCK_QUANTITY") +" :: "+ rs.getDate("PRODUCTION_DATE") +" :: "+ rs.getDate("PRODUCTION_DATE"));
				}
			}
		}, storeId);
	}
	
	public void monthlyCustomerGrowthReport(Date fromDate, Date toDate) {
		String sql = " SELECT YEAR(STARTUP_DATE) AS SIGNUP_YEAR, MONTH(STARTUP_DATE) AS SIGNUP_MONTH, COUNT(DISTINCT MEMBER_ID) AS CUSTOMERS "
				+ " FROM ADD_RENEW_MEMBERSHIPS WHERE STARTUP_DATE >= ? AND STARTUP_DATE <= ? "
				+ " GROUP BY SIGNUP_YEAR, SIGNUP_MONTH ";
		
		jdbcTemplate.query(sql, new RowCallbackHandler() {
			
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				System.out.println("SIGNUP_YEAR :: SIGNUP_MONTH :: CUSTOMERS");
				
				while (rs.next()) {
					System.out.println(rs.getInt("SIGNUP_YEAR") +" :: "+ rs.getInt("SIGNUP_MONTH") +" :: "+ rs.getInt("CUSTOMERS"));
				}
			}
		}, fromDate, toDate);
	}
	
	public void yearlyCustomerGrowthReport(Date fromDate, Date toDate) {
		String sql = " SELECT YEAR(STARTUP_DATE) AS SIGNUP_YEAR, COUNT(DISTINCT MEMBER_ID) AS CUSTOMERS "
				+ " FROM ADD_RENEW_MEMBERSHIPS WHERE STARTUP_DATE >= ? AND STARTUP_DATE <= ? "
				+ " GROUP BY SIGNUP_YEAR ";
		
		jdbcTemplate.query(sql, new RowCallbackHandler() {
			
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				System.out.println("SIGNUP_YEAR :: CUSTOMERS");
				
				while (rs.next()) {
					System.out.println(rs.getInt("SIGNUP_YEAR") +" :: "+ rs.getInt("CUSTOMERS"));
				}
			}
		}, fromDate, toDate);
	}
	
	public void customerActivityReport(Integer memberId, Date fromDate, Date toDate) {
		String sql = " SELECT MEMBER_ID, SUM(TOTAL_PRICE) AS BILL_AMOUNT FROM MEMBER_TRANSACTIONS M NATURAL JOIN MEMBER_TRANSACTIONS_INVOLVE MI "
				+ " WHERE TRANSACTION_DATE BETWEEN ? AND ? AND MEMBER_ID = ? GROUP BY MEMBER_ID ";
		
		jdbcTemplate.query(sql, new RowCallbackHandler() {
			
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				System.out.println("MEMBER_ID :: BILL_AMOUNT");
				
				while (rs.next()) {
					System.out.println(rs.getInt("MEMBER_ID") +" :: "+ rs.getDouble("BILL_AMOUNT"));
				}
			}
		}, fromDate, toDate);
	}
}
