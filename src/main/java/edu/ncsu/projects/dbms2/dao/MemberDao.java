package edu.ncsu.projects.dbms2.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Component;

import edu.ncsu.projects.dbms2.entity.Member;

@Component
public class MemberDao {
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public List<Member> findAll() {
		String sql = " SELECT * FROM MEMBERS ";
		
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Member>(Member.class));
	}
	
	public Member findByAttribute(String attributeName, Object attributeValue) {
		String sql = " SELECT * FROM MEMBERS WHERE "+ attributeName +" = ? ";
		
		List<Member> members = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Member>(Member.class), attributeValue);
		
		return members != null && !members.isEmpty() ? members.get(0) : null;
	}
	
	public int addMember(Member member) {
		String sql = " INSERT INTO MEMBERS VALUES (?,?,?,?,?,?,?,?) ";
		
		return jdbcTemplate.update(sql, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setObject(1, null);
				ps.setString(2, member.getFirstName());
				ps.setString(3, member.getLastName());
				ps.setString(4, member.getAddress());
				ps.setString(5, member.getEmail());
				ps.setString(6, member.getPhone());
				ps.setString(7, member.getMembershipLevel());
				ps.setBoolean(8, member.getActiveStatus());
			}
		});
	}
	
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
	
	public int deleteMember(Integer memberId) {
		String sql = " UPDATE MEMBERS SET ACTIVE_STATUS = false WHERE MEMBER_ID = ? ";
		
		return jdbcTemplate.update(sql, memberId);
	}
	
	public Member removeFromDb(Integer memberId) {
		//TODO
		return null;
	}
	
}
