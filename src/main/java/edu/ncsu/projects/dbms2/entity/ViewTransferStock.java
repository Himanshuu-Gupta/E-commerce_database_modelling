package edu.ncsu.projects.dbms2.entity;

import org.springframework.stereotype.Component;

@Component
public class ViewTransferStock {
	private Integer transferId;
	private Integer productId;
	private Integer warehouseOperatorId;
	private Integer storeId;
	private Integer transactionId;
	private Integer quantity;
	private String transactionType;
	public Integer getTransferId() {
		return transferId;
	}
	public void setTransferId(Integer transferId) {
		this.transferId = transferId;
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
	public Integer getStoreId() {
		return storeId;
	}
	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}
	public Integer getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(Integer transactionId) {
		this.transactionId = transactionId;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}


}
