package com.javateam.foodCrawlingDemo.domain;

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
	    name = "FOOD_SEQ_GENERATOR",
	    sequenceName = "FOOD_SEQ",
	    initialValue = 1,
	    allocationSize = 1)
@Table(name="food_tbl")
@Data
public class FoodVO {
	
	// ex) https://www.nongsaro.go.kr/portal/ps/psr/psrc/areaCkRyDtl.ps?menuId=PS03934&pageIndex=1&pageSize=10&pageUnit=10&cntntsNo=90400&type=02&schText=%EA%B0%80
	
	@Id
    @Column(nullable=false, precision=10, scale=0) // number(10,0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
    			   generator = "FOOD_SEQ_GENERATOR") 
	private long id;
	
	/** 식품명 */
	@Column(nullable = false, name="food_name")
	private String foodName;
	
	/** 식품 이미지 */
	@Column(nullable = false, name="food_img")
	private String foodImg;
	
	/** 식품 유형 / 분류별 */
	@Column(nullable = false, name="food_type")
	private String foodType;
	
	/** 조립법 */
	@Column(nullable = false, name="recipe")
	private String recipe;
	
	/** 식재료 : 길이 보완 */
	@Column(nullable = false, name="ingredient", length = 1000)
	// ex) ORA-12899: value too large for column "FOOD"."FOOD_TBL"."INGREDIENT" 
	// (actual: 320, maximum: 255)
	private String ingredient;
	
	/** 부재료 : 길이 보완*/
	// ex) ORA-12899: value too large for column "FOOD"."FOOD_TBL"."MINOR_INGREDIENT" 
	// (actual: 337, maximum: 255)
	@Column(name="minor_ingredient", length = 1000)
	private String minorIngredient;
	
	/** 조리방법 */
	/** 교정 : 길이 보완 */
	// ex) ORA-12899: value too large for column "FOOD"."FOOD_TBL"."COOKING_INSTRUCTION" 
	// (actual: 332, maximum: 255)
	@Column(name="cooking_instruction", length = 1000)
	private String cookingInstruction;
	
	/** 조리시연자 */
	@Column(name="cook")
	private String cook;
	
	/** 참고사항 : 길이 보완 */
	@Column(name="reference", length = 1000)	
	// ex) ORA-12899: value too large for column "FOOD"."FOOD_TBL"."REFERENCE" 
	// (actual: 296, maximum: 255)
	private String reference;
	
	/** 출처 */
	@Column(name="cooking_origin")
	private String cookingOrigin;
	
	/** 정보제공자 */
	@Column(name="information_provider")
	private String informationProvider ;
	
}
