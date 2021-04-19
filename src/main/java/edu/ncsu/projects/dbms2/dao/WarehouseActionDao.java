package edu.ncsu.projects.dbms2.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Component;

import edu.ncsu.projects.dbms2.entity.WarehouseInventory;

@Component
public class WarehouseActionDao{
	@Autowired
	JdbcTemplate jdbcTemplate;
	/**
	 * 
	 * @param attributeValue 
	 * @param productID
	 * @param storeID
	 * @param warehouseOperatorID
	 * @return
	 */
	public int WarehouseToStoreTransfer(Object attributeValue, Integer productID, Integer storeID, Integer warehouseOperatorID) {
		String sql = "Update WAREHOUSE_INVENTORY SET CURRENT_STOCK  = CURRENT_STOCK  - ? "
				+ "WHERE PRODUCT_ID = ? ";
		
		String sql1 = "Update STORE_INVENTORY SET STOCK_QUANTITY = STOCK_QUANTITY + ? "
				+ "WHERE PRODUCT_ID = ? and STORE_ID = ?";
		
		String sql2 = "SELECT MAX(TRANSACTION_ID) FROM WAREHOUSE_TRANSACTION WHERE PRODUCT_ID = ? ";
		Integer transactionID = jdbcTemplate.queryForObject(sql2, Integer.class , productID);
		
		String sql3 = "Insert into VIEW_TRANSFER_STOCK VALUES (?,?,?,?,?,?,?)";
		
		jdbcTemplate.update(sql, attributeValue, productID);
		jdbcTemplate.update(sql1, attributeValue, productID, storeID);
		
		return jdbcTemplate.update(sql3, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setObject(1, null);
				ps.setInt(2, warehouseOperatorID);
				ps.setInt(3, productID);
				ps.setInt(4, storeID);
				ps.setInt(5, transactionID);
				ps.setObject(6, attributeValue);
				ps.setString(7, "SUPPLY");
			}
		});
	}
	
	public int WarehousetoSupplierReturn(Integer productId, Integer supplierId, Integer warehouseOperatorID, Integer quantity) {
		String sql = "Insert into ORDER_AND_RETURN_STOCKS VALUES (?,?,?,?,?)";
		jdbcTemplate.update(sql, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setObject(1, null);
				ps.setString(2, "RETURN");
				ps.setInt(3, supplierId);
				ps.setInt(4, productId);
				ps.setInt(5, warehouseOperatorID);
			}
		});
		
		String sql1 = "Update WAREHOUSE_INVENTORY SET CURRENT_STOCK = CURRENT_STOCK - ? "
				+ "WHERE PRODUCT_ID = ? ";
		return jdbcTemplate.update(sql1, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setObject(1, quantity);
				ps.setInt(2, productId);
			}
		});
	}
	
	public List<WarehouseInventory> ViewInventory() {
		String sql = " SELECT * FROM WAREHOUSE_INVENTORY";
		
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<WarehouseInventory>(WarehouseInventory.class));
	}
	
	public int addWarehouseTransaction(Integer productId, Integer supplierId, Integer warehouseOperatorID, Integer Stock, Integer Price, Date productionDate, Date expDate, String Product_name, Date Transactiondate) {
		String sql = "Insert into ORDER_AND_RETURN_STOCKS VALUES (?,?,?,?,?)";
		jdbcTemplate.update(sql, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setObject(1, null);
				ps.setString(2, "ORDER");
				ps.setInt(3, supplierId);
				ps.setInt(4, productId);
				ps.setInt(5, warehouseOperatorID);
			}
		});
		
		String sql3 = "SELECT MAX(TRANSACTION_ID) FROM ORDER_AND_RETURN_STOCKS WHERE PRODUCT_ID = ? ";
		Integer transactionID = jdbcTemplate.queryForObject(sql3, Integer.class , productId);
		
		System.out.println("Transaction Date AND Transaction ID"+ Transactiondate + "----" +transactionID);
		
		String sql1 = "Insert into WAREHOUSE_TRANSACTION VALUES (?,?,?,?,?,?,?,?,?)";
		jdbcTemplate.update(sql1, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setObject(1, transactionID);
				ps.setInt(2, productId);
				ps.setInt(3, supplierId);
				ps.setInt(4, Stock);
				ps.setInt(5, Price);
				ps.setDate(6, productionDate);
				ps.setDate(7, expDate);
				ps.setString(8, Product_name);
				ps.setDate(9, Transactiondate);
			}
		});
		
		String sql2 = "INSERT INTO WAREHOUSE_INVENTORY VALUES (?,?,?) ON DUPLICATE KEY UPDATE CURRENT_STOCK = CURRENT_STOCK + ? ";
		return jdbcTemplate.update(sql2, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, productId);
				ps.setString(2, Product_name);
				ps.setInt(3, Stock);
				ps.setInt(4, Stock);
			}
		});
	}
	
}
