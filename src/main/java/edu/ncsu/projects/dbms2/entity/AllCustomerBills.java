package edu.ncsu.projects.dbms2.entity;

import java.sql.Date;

import org.springframework.stereotype.Component;

@Component
public class AllCustomerBills {
	private Integer memberId;
	private Date transactionDate;
	private Double transactionPrice;
	
	
	public Integer getMemberId() {
		return memberId;
	}
	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}
	public Date getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}
	public Double getTransactionPrice() {
		return transactionPrice;
	}
	public void setTransactionPrice(Double transactionPrice) {
		this.transactionPrice = transactionPrice;
	}
	
	

}
