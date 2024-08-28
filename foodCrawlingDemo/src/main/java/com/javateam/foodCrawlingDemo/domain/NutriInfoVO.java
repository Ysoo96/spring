package com.javateam.foodCrawlingDemo.domain;

import java.util.Map;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="NUTRI_INFO_TBL")
@SequenceGenerator(
	    name = "NUTRI_INFO_SEQ_GENERATOR",
	    sequenceName = "NUTRI_INFO_SEQ",
	    initialValue = 1,
	    allocationSize = 1)
@Data
public class NutriInfoVO {
		
	@Id
	@Column(name="NUM")
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
   					generator = "NUTRI_INFO_SEQ_GENERATOR") 
	private int num;
	
	@Column(name="FOOD_ID")
	private int foodId; // 사용자 추가
	
	@Column(name="FOOD_NAME")
	private String foodName; // 사용자 추가
	
	@Column(name="DESC_KOR")
	private String descKor;
	
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
	
	// JSONObject -> VO
	public NutriInfoVO(Map<String, String> map) {
		
		this.foodId    = Integer.parseInt(map.get("foodId"));
		this.foodName  = map.get("foodName");
		this.descKor   = map.get("descKor");
		this.makerName = map.get("makerName");
		this.nutrCont1 = map.get("nutrCont1").equals("") ? 0.0F : Float.parseFloat(map.get("nutrCont1"));
		this.nutrCont2 = map.get("nutrCont2").equals("") ? 0.0F : Float.parseFloat(map.get("nutrCont2"));
		this.nutrCont3 = map.get("nutrCont3").equals("") ? 0.0F : Float.parseFloat(map.get("nutrCont3"));
		this.nutrCont4 = map.get("nutrCont4").equals("") ? 0.0F : Float.parseFloat(map.get("nutrCont4"));
		this.nutrCont5 = map.get("nutrCont5").equals("") ? 0.0F : Float.parseFloat(map.get("nutrCont5"));
		this.nutrCont6 = map.get("nutrCont6").equals("") ? 0.0F : Float.parseFloat(map.get("nutrCont6"));
		this.nutrCont7 = map.get("nutrCont7").equals("") ? 0.0F : Float.parseFloat(map.get("nutrCont7"));
		this.nutrCont8 = map.get("nutrCont8").equals("") ? 0.0F : Float.parseFloat(map.get("nutrCont8"));
		this.nutrCont9 = map.get("nutrCont9").equals("") ? 0.0F : Float.parseFloat(map.get("nutrCont9"));
		
	} //

}
