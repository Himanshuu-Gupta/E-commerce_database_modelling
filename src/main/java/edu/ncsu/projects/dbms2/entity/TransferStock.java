package edu.ncsu.projects.dbms2.entity;

import org.springframework.stereotype.Component;

@Component
public class TransferStock {

	private Integer interTransferId;
	private Integer warehouseOperatorId;
	private Integer productId;
	private Integer fromStoreId;
	private Integer toStoreId;
	private Integer quantity;
	public Integer getInterTransferId() {
		return interTransferId;
	}
	public void setInterTransferId(Integer interTransferId) {
		this.interTransferId = interTransferId;
	}
	public Integer getWarehouseOperatorId() {
		return warehouseOperatorId;
	}
	public void setWarehouseOperatorId(Integer warehouseOperatorId) {
		this.warehouseOperatorId = warehouseOperatorId;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public Integer getFromStoreId() {
		return fromStoreId;
	}
	public void setFromStoreId(Integer fromStoreId) {
		this.fromStoreId = fromStoreId;
	}
	public Integer getToStoreId() {
		return toStoreId;
	}
	public void setToStoreId(Integer toStoreId) {
		this.toStoreId = toStoreId;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
}
