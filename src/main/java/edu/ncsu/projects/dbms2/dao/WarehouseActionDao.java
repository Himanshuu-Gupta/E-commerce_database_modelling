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
	 * WarehousetoStoreTransfer handles the request to transfer product from stores to the main warehouse.
	 * @param quantity - quantity of the stock needs to be transferred from store to warehouse.
	 * @param productID - ID of the product needs to be transferred
	 * @param storeID - ID of the Store
	 * @param warehouseOperatorID - WareHouse Operator ID doing the transaction
	 * @return The number of products added in the warehouse
	 */
	public int WarehouseToStoreTransfer(Integer quantity, Integer productID, Integer storeID, Integer warehouseOperatorID) {
		String sql = "Update WAREHOUSE_INVENTORY SET CURRENT_STOCK  = CURRENT_STOCK  - ? "
				+ "WHERE PRODUCT_ID = ? ";
		
		String sql1 = "Update STORE_INVENTORY SET STOCK_QUANTITY = STOCK_QUANTITY + ? "
				+ "WHERE PRODUCT_ID = ? and STORE_ID = ?";
		
		String sql2 = "SELECT MAX(TRANSACTION_ID) FROM WAREHOUSE_TRANSACTION WHERE PRODUCT_ID = ? ";
		Integer transactionID = jdbcTemplate.queryForObject(sql2, Integer.class , productID);
		
		String sql3 = "Insert into VIEW_TRANSFER_STOCK VALUES (?,?,?,?,?,?,?)";
		
		jdbcTemplate.update(sql, quantity, productID);
		jdbcTemplate.update(sql1, quantity, productID, storeID);
		
		return jdbcTemplate.update(sql3, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setObject(1, null);
				ps.setInt(2, warehouseOperatorID);
				ps.setInt(3, productID);
				ps.setInt(4, storeID);
				ps.setInt(5, transactionID);
				ps.setObject(6, quantity);
				ps.setString(7, "SUPPLY");
			}
		});
	}
	
	/**
	 *  WarehousetoSupplierReturn handles the request to return any product to the supplier from the warehouse
	 * @param productId - ID of the product which needs to be returned to the store
	 * @param supplierId - Id of the supplier we are returning the product to
	 * @param warehouseOperatorID - WareHouse Operator ID doing the transaction
	 * @param quantity - quantity of the stock needs to be returned
	 * @return - The number of product returned to the supplier
	 */
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
	/**
	 * ViewInventory handles the request to display all the products in the warehouse inventory.
	 * The function doesnt require any inputs.
	 * @return The list of the all the products present in WAREHOUSE_INVENTORY
	 */
	public List<WarehouseInventory> ViewInventory() {
		String sql = " SELECT * FROM WAREHOUSE_INVENTORY";
		
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<WarehouseInventory>(WarehouseInventory.class));
	}
	/**
	 * addWarehouseTransaction handles the request to add any new product coming in our inventory.
	 * @param productId - Id of the new incoming product
	 * @param supplierId - ID of the supplier who is providing the product
	 * @param warehouseOperatorID - WareHouse Operator ID doing the transaction
	 * @param Stock - The quantity of the product 
	 * @param Price - The market price of the product
	 * @param productionDate - Production Date of the Product
	 * @param expDate - Expiration Date of the product
	 * @param Product_name - Name of the product 
	 * @param Transactiondate - Date of the transaction we are receiving the product into inventory
	 * @return The number of the products added into the inventory 
	 */
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
