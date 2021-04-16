package edu.ncsu.projects.dbms2.entity;

import org.springframework.stereotype.Component;

@Component
public class OrderAndReturnStocks {

	private Integer transactionId;
	private String transactionType;
	private Integer supplierId;
	private Integer productId;
	private Integer warehouseOperatorId;
	public Integer getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(Integer transactionId) {
		this.transactionId = transactionId;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public Integer getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public Integer getWarehouseOperatorId() {
		return warehouseOperatorId;
	}
	public void setWarehouseOperatorId(Integer warehouseOperatorId) {
		this.warehouseOperatorId = warehouseOperatorId;
	}
	
}
