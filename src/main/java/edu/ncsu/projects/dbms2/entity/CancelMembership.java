package edu.ncsu.projects.dbms2.entity;

import java.sql.Date;

import org.springframework.stereotype.Component;

@Component
public class CancelMembership {
	private Integer membershipId;
	private Integer memberId;
	private String membershipLevel;
	private Integer registrationOperatorId;
	private Date cancelTime;
	
	public Integer getMembershipId() {
		return membershipId;
	}
	public void setMembershipId(Integer membershipId) {
		this.membershipId = membershipId;
	}
	public Integer getMemberId() {
		return memberId;
	}
	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}
	public String getMembershipLevel() {
		return membershipLevel;
	}
	public void setMembershipLevel(String membershipLevel) {
		this.membershipLevel = membershipLevel;
	}
	public Integer getRegistrationOperatorId() {
		return registrationOperatorId;
	}
	public void setRegistrationOperatorId(Integer registrationOperatorId) {
		this.registrationOperatorId = registrationOperatorId;
	}
	public Date getCancelTime() {
		return cancelTime;
	}
	public void setCancelTime(Date cancelTime) {
		this.cancelTime = cancelTime;
	}
	
}
