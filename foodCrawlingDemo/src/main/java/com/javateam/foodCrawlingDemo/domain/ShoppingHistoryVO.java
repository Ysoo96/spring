package com.javateam.foodCrawlingDemo.domain;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@SequenceGenerator(
		name = "SOPPING_HISTORY_SEQ_GENERATOR",
		sequenceName = "SHOPPING_HISTORY_SEQ",
		initialValue = 1,
		allocationSize = 1)
@Table(name="SHOPPING_HISTORY_TBL")
@Data
public class ShoppingHistoryVO {
	
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
					 generator = "SHOPPING_HISTORY_SEQ_GENERATOR")
	private int id;
	
	@Column(name="USER_ID")
	private String userId;
	
	@Column(name="FOOD_NAME")
	private String foodName;
	
	@Column
	private int quantity;
	
	@Column(name="PURCHASE_DATE")
	private Date purchaseDate;
	
}
