package edu.ncsu.projects.dbms2.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProductDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * Returns price for a specific product at the given store
	 * @param productId
	 * @param storeId
	 * @return Returns product price 
	 */
	public Double getStoreProductPrice(Integer productId, Integer storeId) {
		String sql = " SELECT PRICE FROM STORE_INVENTORY WHERE STORE_ID = ? AND PRODUCT_ID = ? ";
		
		return jdbcTemplate.queryForObject(sql, Double.class, storeId, productId);
	}
}
