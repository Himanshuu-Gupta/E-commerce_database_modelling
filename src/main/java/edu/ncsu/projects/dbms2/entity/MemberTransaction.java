package edu.ncsu.projects.dbms2.entity;

import java.sql.Date;

import org.springframework.stereotype.Component;

@Component
public class MemberTransaction {
	
	private Integer transactionId;
	private Integer memberId;
	private Integer cashierId;
	private Date transactionDate;
	private String transactionType;
	private Integer storeId;
	
	public Integer getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(Integer transactionId) {
		this.transactionId = transactionId;
	}
	public Integer getMemberId() {
		return memberId;
	}
	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}
	public Integer getCashierId() {
		return cashierId;
	}
	public void setCashierId(Integer cashierId) {
		this.cashierId = cashierId;
	}
	public Date getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public Integer getStoreId() {
		return storeId;
	}
	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}
	
}
