package edu.ncsu.projects.dbms2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import edu.ncsu.projects.dbms2.entity.AddRenewMembership;
import edu.ncsu.projects.dbms2.entity.CancelMembership;
import edu.ncsu.projects.dbms2.entity.Member;
import edu.ncsu.projects.dbms2.entity.MemberTransaction;
import edu.ncsu.projects.dbms2.entity.MemberTransactionsInvolve;

@Component
public class MemberDao {
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	/**
	 * Get list of all members.
	 * @return List of all members
	 */
	public List<Member> findAll() {
		String sql = " SELECT * FROM MEMBERS ";
		
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Member>(Member.class));
	}
	
	/**
	 * Find members given the value of a particular attribute.
	 * @param attributeName
	 * @param attributeValue
	 * @return
	 */
	public List<Member> findByAttribute(String attributeName, Object attributeValue) {
		String sql = " SELECT * FROM MEMBERS WHERE "+ attributeName +" = ? ";
		
		List<Member> members = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Member>(Member.class), attributeValue);
		
		return members;
	}
	
	/**
	 * Insert a new member in Members table.
	 * @param member
	 * @param membership
	 * @return
	 */
	public int addMember(Member member, AddRenewMembership membership) {
		String addMemberSql = " INSERT INTO MEMBERS VALUES (?,?,?,?,?,?,?,?) ";
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(addMemberSql, Statement.RETURN_GENERATED_KEYS);
				ps.setObject(1, null);
				ps.setString(2, member.getFirstName());
				ps.setString(3, member.getLastName());
				ps.setString(4, member.getAddress());
				ps.setString(5, member.getEmail());
				ps.setString(6, member.getPhone());
				ps.setString(7, member.getMembershipLevel());
				ps.setBoolean(8, member.getActiveStatus());
				
				return ps;
			}
		}, keyHolder);
		
		int addedMemberId = keyHolder.getKey().intValue();
		membership.setMemberId(addedMemberId);
		
		String addMembershipSql = " INSERT INTO ADD_RENEW_MEMBERSHIPS VALUES (null, ?, ?, ?, ?, ?, ?) ";
		
		jdbcTemplate.update(addMembershipSql, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, membership.getMemberId());
				ps.setString(2, membership.getMembershipLevel());
				ps.setInt(3, membership.getRegistrationOperatorId());
				ps.setInt(4, membership.getStoreId());
				ps.setDate(5, membership.getStartDate());
				ps.setDate(6, membership.getEndDate());
			}
		});
		
		return addedMemberId;
	}
	
	/**
	 * Update a member record attribute with the given value.
	 * @param attributeName
	 * @param attributeValue
	 * @param memberId
	 * @return
	 */
	public int updateByAttribute(String attributeName, Object attributeValue, Integer memberId) {
		String sql = " UPDATE MEMBERS SET "+ attributeName +" = ? WHERE MEMBER_ID = ? ";
		
		return jdbcTemplate.update(sql, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setObject(1, attributeValue);
				ps.setInt(2, memberId);
			}
		});
	}
	
	/**
	 * Update a member record given the new record.
	 * @param member
	 * @return
	 */
	public int updateMember(Member member) {
		String sql = " UPDATE MEMBERS SET FIRST_NAME = ? , LAST_NAME = ? , "
				+ " ADDRESS = ? , EMAIL = ? , PHONE = ? , MEMBERSHIP_LEVEL = ? , ACTIVE_STATUS = ? "
				+ " WHERE MEMBER_ID = ? ";
		 
		return jdbcTemplate.update(sql, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, member.getFirstName());
				ps.setString(2, member.getLastName());
				ps.setString(3, member.getAddress());
				ps.setString(4, member.getEmail());
				ps.setString(5, member.getPhone());
				ps.setString(6, member.getMembershipLevel());
				ps.setBoolean(7, member.getActiveStatus());
				ps.setInt(8, member.getMemberId());
			}
		});
	}
	
	/**
	 * Set member to inactive given its member ID.
	 * @param memberId
	 * @return
	 */
	public int deleteMember(Integer memberId) {
		String sql = " UPDATE MEMBERS SET ACTIVE_STATUS = false WHERE MEMBER_ID = ? ";
		
		return jdbcTemplate.update(sql, memberId);
	}
	
	/**
	 * Get active membership ID of a member.
	 * @param memberId
	 * @return
	 */
	public Integer getMembershipId(Integer memberId) {
		String sql = " SELECT MAX(MEMBERSHIP_ID) FROM ADD_RENEW_MEMBERSHIPS WHERE MEMBER_ID = ? ";
		
		return jdbcTemplate.queryForObject(sql, Integer.class, memberId);
	}
	
	/**
	 * Completely remove the member record from MEMBERS table.
	 * @param memberId
	 * @return
	 */
	public int removeFromDb(Integer memberId) {
		String sql = " DELETE FROM MEMBERS WHERE MEMBER_ID = ? ";
		
		return jdbcTemplate.update(sql, memberId);
	}
	
	/**
	 * Get all transactions made by a particular member.
	 * @param memberId
	 * @return
	 */
	public List<MemberTransaction> getMemberTransactions(Integer memberId) {
		String sql = " SELECT * FROM MEMBER_TRANSACTIONS WHERE MEMBER_ID = ? ";
		
		return jdbcTemplate.queryForList(sql, MemberTransaction.class, memberId);
	}
	
	/**
	 * Get transaction details of a particular member transaction.
	 * @param transactionId
	 * @return
	 */
	public List<MemberTransactionsInvolve> getMemberTransactionDetails(Integer transactionId) {
		String sql = " SELECT * FROM MEMBER_TRANSACTIONS_INVOLVE WHERE TRANSACTION_ID = ? ";
		
		return jdbcTemplate.queryForList(sql, MemberTransactionsInvolve.class, transactionId);
	}
	
	/**
	 * Get sum of all product prices of a member transaction.
	 * @param transactionId
	 * @return
	 */
	public Double generateMemberTransactionTotalPrice(Integer transactionId) {
		String sql = " SELECT SUM(TOTAL_PRICE) FROM MEMBER_TRANSACTIONS_INVOLVE WHERE TRANSACTION_ID = ? ";
		
		return jdbcTemplate.queryForObject(sql, Double.class, transactionId);
	}
	
	/**
	 * Add a new row to MEMBER_TRANSACTION table.
	 * @param transaction
	 * @return
	 */
	public Integer addMemberTransaction(MemberTransaction transaction) {
		String addTransaction = " INSERT INTO MEMBER_TRANSACTIONS VALUES (null, ? ,? ,? ,? ,?) ";
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		jdbcTemplate.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(addTransaction, Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, transaction.getMemberId());
				ps.setInt(2, transaction.getCashierId());
				ps.setInt(3, transaction.getStoreId());
				ps.setDate(4, transaction.getTransactionDate());
				ps.setString(5, transaction.getTransactionType());
				
				return ps;
			}
		}, keyHolder);
		
		return keyHolder.getKey().intValue();
	}
	
	/**
	 * Add new rows to MEMBER_TRANSACTION_DETAILS table.
	 * @param transactionDetails
	 * @return
	 */
	public int addMemberTransactionDetails(List<MemberTransactionsInvolve> transactionDetails) {
		
		String involveSql = " INSERT INTO MEMBER_TRANSACTIONS_INVOLVE VALUES (? ,? ,? ,? ,? ,? ,?) ";
		
		jdbcTemplate.batchUpdate(involveSql, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setInt(1, transactionDetails.get(i).getTransactionId());
				ps.setInt(2, transactionDetails.get(i).getProductId());
				ps.setInt(3, transactionDetails.get(i).getStoreId());
				ps.setInt(4, transactionDetails.get(i).getProductQuantity());
				ps.setInt(5, transactionDetails.get(i).getDiscountId());
				ps.setDouble(6, transactionDetails.get(i).getPrice());
				ps.setDouble(7, transactionDetails.get(i).getTotalPrice());
			}
			
			@Override
			public int getBatchSize() {
				return transactionDetails.size();
			}
		});
		
		return 0;
	}
	
	/**
	 * Add a new record to ADD_RENEW_MEMBERSHIPS table for new/renewed membership.
	 * @param membership
	 * @return
	 */
	public int addMembership(AddRenewMembership membership) {
		String sql = " INSERT INTO ADD_RENEW_MEMBERSHIPS VALUES (null, ?, ?, ?, ?, ?, ?) ";
		
		return jdbcTemplate.update(sql, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, membership.getMemberId());
				ps.setString(2, membership.getMembershipLevel());
				ps.setInt(3, membership.getRegistrationOperatorId());
				ps.setInt(4, membership.getStoreId());
				ps.setDate(5, membership.getStartDate());
				ps.setDate(6, membership.getEndDate());
			}
		});
	}
	
	/**
	 * Add a new record to CANCEL_MEMBERSHIPS table for cancelled membership.
	 * @param membership
	 * @return
	 */
	public int cancelMembership(CancelMembership membership) {
		String sql = " INSERT INTO CANCEL_MEMBERSHIPS VALUES (?, ?, ?, ?) ";
		
		return jdbcTemplate.update(sql, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, membership.getMembershipId());
				ps.setInt(2, membership.getMemberId());
				ps.setInt(3, membership.getRegistrationOperatorId());
				ps.setDate(4, membership.getCancelTime());
			}
		});
	}
	
}
