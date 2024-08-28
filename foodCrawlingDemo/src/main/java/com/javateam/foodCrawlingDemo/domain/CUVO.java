package com.javateam.foodCrawlingDemo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@SequenceGenerator(
	    name = "CU_SEQ_GENERATOR",
	    sequenceName = "CU_SEQ",
	    initialValue = 1,
	    allocationSize = 1)
@Table(name="cu_tbl")
@Data
@NoArgsConstructor // 추가
@AllArgsConstructor // 추가
@Builder
public class CUVO {
	
	@Id
    @Column(nullable=false, precision=10, scale=0) // number(10,0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
    			   generator = "CU_SEQ_GENERATOR") // 오타 교정
	private long id;
	
	/** 식품명 */
	@Column(nullable = false, name="food_name")
	private String foodName;
	
	/** 식품 이미지 */
	@Column(name="food_img")
	private String foodImg;
	
	/** 식품 대분류 */
	@Column(name="food_type1")
	private String foodType1; // foodTag ex) 간편식사
	
	/** 식품 소분류 */
	@Column(name="food_type2")
	private String foodType2; // foodTag ex) 샌드위치
		
	/** 상품 설명 */
	@Column(name="food_detail")
	private String foodDetail; // ex) 담백한 참치마요와 풍미 가득한 더블업치즈, 아삭한 야채가 더해진 자이언트 샌드위치
	
	/** 상품 가격 */
	@Column(name="food_price")
	private int foodPrice; // 2000
	
	
}