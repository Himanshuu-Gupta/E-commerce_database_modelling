package edu.ncsu.projects.dbms2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
//import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import edu.ncsu.projects.dbms2.entity.Store;
import edu.ncsu.projects.dbms2.entity.StoreInventory;

@Component
public class StoreDao {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public List<Store> findAll() {
		String sql = " SELECT * FROM STORES ";
		
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Store>(Store.class));
	}
	

	
//	public Store findByAttribute(String attributeName, Object attributeValue) {
//		String sql = " SELECT * FROM MEMBERS WHERE "+ attributeName +" = ? ";
//		
//		List<Member> members = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Member>(Member.class), attributeValue);
//		
//		return members != null && !members.isEmpty() ? members.get(0) : null;
//	}
	
	public int addStore(Store store) {
		String sql = " INSERT INTO STORES VALUES (?,?,?,?,?) ";
		
		
		return jdbcTemplate.update(sql, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setObject(1, null);
				ps.setString(2, store.getAddress());
				ps.setString(3, store.getPhone());
				ps.setBoolean(4, store.getActiveStatus());
				ps.setInt(5, store.getManagerId());
				
			}
		});
	}
	
	public int updateByAttribute(String attributeName, Object attributeValue, Integer storeId) {
		String sql = " UPDATE STORES SET "+ attributeName +" = ? WHERE STORE_ID = ? ";
		
		return jdbcTemplate.update(sql, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setObject(1, attributeValue);
				ps.setInt(2, storeId);
			}
		});
	}
	
	public StoreInventory viewStoreInv(Integer storeId) {
		String sql = " SELECT * FROM STORE_INVENTORY WHERE STORE_ID = "+ storeId;
		
		List<StoreInventory> stores = jdbcTemplate.query(sql, new BeanPropertyRowMapper<StoreInventory>(StoreInventory.class));
		return !stores.isEmpty() ? stores.get(0) : null;
		
		
//		return jdbcTemplate.update(sql, new PreparedStatementSetter() {
			
//			@Override
//			public void setValues(PreparedStatement ps) throws SQLException {
//				ps.setInt(1, storeId);
//			}
//		});
	}
	
	public List<StoreInventory> storeToStore(Integer fromStoreId, Integer toStoreId, Integer quantity, Integer productId){
		String sql = " UPDATE STORE_INVENTORY SET STOCK_QUANTITY = CASE WHEN STORE_ID ="+fromStoreId
				+"THEN (STOCK_QUANTITY-"+quantity
				+ ") WHEN STORE_ID = "+ toStoreId
				+ "THEN (STOCK_QUANTITY +"+quantity+")"
						+ "WHERE PRODUCT_ID ="+productId+";";
				
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<StoreInventory>(StoreInventory.class)); 
	}
	
	public int updateStore(Store store) {
		String sql = " UPDATE STORES SET ADDRESS = ? , PHONE = ? , ACTIVE_STATUS = ? , MANAGER_ID = ?"
				+ " WHERE STORE_ID = ? ";
		 
		return jdbcTemplate.update(sql, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, store.getAddress());
				ps.setString(2, store.getPhone());
				ps.setBoolean(3, store.getActiveStatus());
				ps.setInt(4, store.getManagerId());
				ps.setInt(5, store.getStoreId());
			}
		});
	}
	
	public int deleteStore(Integer storeId) {
		
		String sql = " DELETE FROM STORES WHERE STORE_ID = ? ";
		return jdbcTemplate.update(sql, storeId);
	}
	
	public int removeFromDb(Integer storeId) {
		String sql = " UPDATE STORES SET ACTIVE_STATUS = false WHERE STORE_ID = ? ";
		
		return jdbcTemplate.update(sql, storeId);
		//TODO
//		return null;
	}

	
	
	public List<StoreInventory> returnStoreToWarehouse(Integer storeId, Integer productId, Integer quantity, Integer warehouseOpId) {
			
		String sql2 = " SELECT max(TRANSACTION_ID) as TRANSACTION_ID FROM WAREHOUSE_TRANSACTION WT INNER JOIN STORE_INVENTORY SI "
				+ " ON  WT.PRODUCTION_DATE = SI.PRODUCTION_DATE"
				+ " AND WT.EXPIRATION_DATE = SI.EXPIRATION_DATE"
				+ " AND WT.PRODUCT_ID = SI.PRODUCT_ID "
				+ " WHERE WT.PRODUCT_ID = "+ productId;
		
		Integer transactionId =  jdbcTemplate.queryForObject(sql2, Integer.class);
		
		String sql3 = "SELECT STAFF_ID FROM STAFF WHERE JOB_TITLE = 'WarehouseOperator'";
		
		Integer staffId =  jdbcTemplate.queryForObject(sql3, Integer.class);
		
		String sql = " INSERT INTO VIEW_TRANSFER_STOCK (TRANSFER_ID, PRODUCT_ID, WAREHOUSEOPERATOR_ID , STORE_ID , TRANSACTION_ID , QUANTITY, TRANSACTION_TYPE ) "
				+ " VALUES(null,"+productId+","+staffId+","+storeId+","+transactionId+","+quantity+",'RETURN')";
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		jdbcTemplate.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				return con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			}
		}, keyHolder);
		
		int transferId = keyHolder.getKey().intValue();		
//		jdbcTemplate.update(sql, new PreparedStatementSetter() {
//			
//			@Override
//			public void setValues(PreparedStatement ps) throws SQLException {
//				ps.setObject(1, null);
//
//				ResultSet rs = ps.getGeneratedKeys();
//				
//			}
//		});
		

		String sql1 = " UPDATE STORE_INVENTORY AS SI "
				+ " INNER JOIN VIEW_TRANSFER_STOCK AS VTS "
				+ " ON SI.STORE_ID = VTS.STORE_ID "
				+ " AND SI.PRODUCT_ID = VTS.PRODUCT_ID "
				+ " SET SI.STOCK_QUANTITY = (SI.STOCK_QUANTITY - VTS.QUANTITY) "
				+ " WHERE VTS.TRANSFER_ID = "+transferId
				+ " AND VTS.TRANSACTION_TYPE='RETURN'; ";
		
		
		jdbcTemplate.update(sql1);
		
		String sql4 = " SELECT * FROM STORE_INVENTORY WHERE STORE_ID = "+storeId;
		
		return jdbcTemplate.query(sql4, new BeanPropertyRowMapper<StoreInventory>(StoreInventory.class));
		
	}
	}
		
		

