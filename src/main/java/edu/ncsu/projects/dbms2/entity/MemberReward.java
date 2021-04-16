package edu.ncsu.projects.dbms2.entity;

import java.sql.Date;

import org.springframework.stereotype.Component;

@Component
public class MemberReward {
	private Integer rewardId;
	private Integer billingStaffId;
	private Integer memberId;
	private Double rewardAmount;
	private Date rewardDate;
	
	public Integer getRewardId() {
		return rewardId;
	}
	public void setRewardId(Integer rewardId) {
		this.rewardId = rewardId;
	}
	public Integer getBillingStaffId() {
		return billingStaffId;
	}
	public void setBillingStaffId(Integer billingStaffId) {
		this.billingStaffId = billingStaffId;
	}
	public Integer getMemberId() {
		return memberId;
	}
	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}
	public Double getRewardAmount() {
		return rewardAmount;
	}
	public void setRewardAmount(Double rewardAmount) {
		this.rewardAmount = rewardAmount;
	}
	public Date getRewardDate() {
		return rewardDate;
	}
	public void setRewardDate(Date rewardDate) {
		this.rewardDate = rewardDate;
	}
}
