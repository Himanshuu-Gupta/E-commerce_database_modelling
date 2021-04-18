package edu.ncsu.projects.dbms2.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Component;

import edu.ncsu.projects.dbms2.entity.StoreInventory;
import edu.ncsu.projects.dbms2.entity.WarehouseTransaction;

@Component
public class ProductActionsDao {
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	/**
	 * Update information of a product based on the product id
	 * @param attributeName
	 * @param attributeValue
	 * @param productID
	 * @return
	 */
	public int updateByAttribute(String attributeName, Object attributeValue, Integer productID) {
		
		String sql1 = " UPDATE STORE_INVENTORY SET "+ attributeName +" = ? WHERE PRODUCT_ID = ? ";
		String sql2 = " UPDATE WAREHOUSE_INVENTORY SET "+ attributeName +" = ? WHERE PRODUCT_ID = ? ";
		
		Integer rows1 =  jdbcTemplate.update(sql1, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setObject(1, attributeValue);
				ps.setInt(2, productID);
			}
		});
		
		Integer rows2 = jdbcTemplate.update(sql2, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setObject(1, attributeValue);
				ps.setInt(2, productID);
			}
		});
		
		String sql3 = " SELECT * FROM STORE_INVENTORY WHERE PRODUCT_ID = ? ";
		String sql4 = " SELECT * FROM WAREHOUSE_TRANSACTION WHERE PRODUCT_ID = ? ";
		
		List<StoreInventory> storeinv = jdbcTemplate.query(sql3, new PreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, productID);
					}
				},
				new BeanPropertyRowMapper<StoreInventory>(StoreInventory.class));
		
		List<WarehouseTransaction> waretran = jdbcTemplate.query(sql4, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, productID);
				}
			},
			new BeanPropertyRowMapper<WarehouseTransaction>(WarehouseTransaction.class));
		
		System.out.println("Changes in STORE_INVENTORY:");
		System.out.println(storeinv);
		System.out.println("Changes in WAREHOUSE_TRANSACTIONS:");
		System.out.println(waretran);
		return rows1 + rows2;
	}
	
	
}
