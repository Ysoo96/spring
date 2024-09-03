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
@Table(name="CU_NUTRI_INFO_TBL")
@SequenceGenerator(
	    name = "CU_NUTRI_INFO_SEQ_GENERATOR",
	    sequenceName = "CU_NUTRI_INFO_SEQ",
	    initialValue = 1,
	    allocationSize = 1)
@Data
@NoArgsConstructor // 패치
@AllArgsConstructor
@Builder
public class CuNutriInfoVO {

	@Id
	@Column(name="NUM")
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
   					generator = "CU_NUTRI_INFO_SEQ_GENERATOR")
	private int num;

	@Column(name="FOOD_ID")
	private int foodId; // 사용자 추가

	@Column(name="FOOD_NAME")
	private String foodName; // 사용자 추가

	@Column(name="MAKER_NAME")
	private String makerName;

	@Column(name="NUTR_CONT1")
	private float nutrCont1; // 열량(kcal)(1회제공량당)

	@Column(name="NUTR_CONT2")
	private float nutrCont2; // 탄수화물(g)(1회제공량당)

	@Column(name="NUTR_CONT3")
	private float nutrCont3; // 단백질(g)(1회제공량당)

	@Column(name="NUTR_CONT4")
	private float nutrCont4; // 지방(g)(1회제공량당)

	@Column(name="NUTR_CONT5")
	private float nutrCont5; // 당류(g)(1회제공량당)

	@Column(name="NUTR_CONT6")
	private float nutrCont6; // 나트륨(mg)(1회제공량당)

	@Column(name="NUTR_CONT7")
	private float nutrCont7; // 콜레스테롤(mg)(1회제공량당)

	@Column(name="NUTR_CONT8")
	private float nutrCont8; // 포화지방산(g)(1회제공량당)

	@Column(name="NUTR_CONT9")
	private float nutrCont9; // 트랜스지방(g)(1회제공량당)

}