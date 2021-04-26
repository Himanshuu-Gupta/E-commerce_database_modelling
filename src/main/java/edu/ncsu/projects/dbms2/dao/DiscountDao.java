package edu.ncsu.projects.dbms2.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Component;

import edu.ncsu.projects.dbms2.entity.Discount;

@Component
public class DiscountDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * Get Discount Percent given store ID and product.
	 * @param storeiId Store ID 
	 * @param productId Product ID
	 * @return
	 */
	public Double getStoreProductDiscountPercent(Integer storeiId, Integer productId) {
		String sql = " SELECT DISCOUNT_PERCENT FROM DISCOUNT WHERE STORE_ID = ? AND PRODUCT_ID = ? "
				+ " AND FROM_DATE <= SYSDATE() AND END_DATE >= SYSDATE() ";
		
		return jdbcTemplate.queryForObject(sql, Double.class, storeiId, productId);
	}
	
	/**
	 * Get Product Discount ID given Store ID and Product ID
	 * @param storeiId Store ID 
	 * @param productId Product ID
	 * @return
	 */
	public Integer getStoreProductDiscount(Integer storeiId, Integer productId) {
		String sql = " SELECT DISCOUNT_ID FROM DISCOUNT WHERE STORE_ID = ? AND PRODUCT_ID = ? "
				+ " AND FROM_DATE <= SYSDATE() AND END_DATE >= SYSDATE() ";
		
		return jdbcTemplate.queryForObject(sql, Integer.class, storeiId, productId);
	}
	
	/**
	 * Get all discounts.
	 * @return List of all discounts.
	 */
	public List<Discount> findAll() {
		String sql = " SELECT * FROM DISCOUNT ";
		
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Discount>(Discount.class));
	}
	
	/**
	 * Add a new discount row in DISCOUNTS table.
	 * @param discount
	 * @return Number of rows added.
	 */
	public int addDiscount(Discount discount) {
		String sql = " INSERT INTO DISCOUNT VALUES (null, ?, ?, ?, ?, ?, ?) ";
		
		return jdbcTemplate.update(sql, discount.getStoreId(), discount.getProductId(), 
				discount.getManagerId(), discount.getDiscountPercent(), 
				discount.getFromDate(), discount.getEndDate());
	}
	
	/**
	 * Update a discount record by the given attribute and value.
	 * @param attributeName
	 * @param attributeValue
	 * @param discountId
	 * @return Number of records updates
	 */
	public int updateByAttribute(String attributeName, Object attributeValue, Integer discountId) {
		String sql = " UPDATE DISCOUNT SET "+ attributeName +" = ? WHERE DISCOUNT_ID = ? ";
		
		return jdbcTemplate.update(sql, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setObject(1, attributeValue);
				ps.setInt(2, discountId);
			}
		});
	}
	
	/**
	 * Delete discount row by making them invalid.
	 * @param discountId
	 * @return number of rows deleted.
	 */
	public int deleteDiscount(Integer discountId) {
		String sql = " UPDATE DISCOUNT SET END_DATE = SYSDATE()-1 WHERE DISCOUNT_ID = ? ";
		
		return jdbcTemplate.update(sql, discountId);
	}
	
	/**
	 * Delete row from database.
	 * @param discountId
	 * @return
	 */
	public int removeFromDb(Integer discountId) {
		String sql = " DELETE FROM DISCOUNT WHERE DISCOUNT_ID = ? ";
		
		return jdbcTemplate.update(sql, discountId);
	}
	
}
