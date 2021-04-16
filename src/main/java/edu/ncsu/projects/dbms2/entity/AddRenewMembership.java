package edu.ncsu.projects.dbms2.entity;

import java.sql.Date;

import org.springframework.stereotype.Component;

@Component
public class AddRenewMembership {
	
	private Integer storeId;
	private Date startDate;
	private Date endDate;
	private Integer memberId;
	private Integer membershipId;
	private Integer registrationOperatorId;
	private String membershipLevel;
	
	public Integer getStoreId() {
		return storeId;
	}
	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Integer getMemberId() {
		return memberId;
	}
	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}
	public Integer getMembershipId() {
		return membershipId;
	}
	public void setMembershipId(Integer membershipId) {
		this.membershipId = membershipId;
	}
	public Integer getRegistrationOperatorId() {
		return registrationOperatorId;
	}
	public void setRegistrationOperatorId(Integer registrationOperatorId) {
		this.registrationOperatorId = registrationOperatorId;
	}
	public String getMembershipLevel() {
		return membershipLevel;
	}
	public void setMembershipLevel(String membershipLevel) {
		this.membershipLevel = membershipLevel;
	}
	
}
