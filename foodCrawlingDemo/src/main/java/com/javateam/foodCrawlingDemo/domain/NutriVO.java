package com.javateam.foodCrawlingDemo.domain;

import org.json.JSONObject;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="NUTRI_TBL")
@SequenceGenerator(
	    name = "NUTRI_SEQ_GENERATOR",
	    sequenceName = "NUTRI_SEQ",
	    initialValue = 1,
	    allocationSize = 1)
@Data
public class NutriVO {
	
	// 필드 설명 : https://www.foodsafetykorea.go.kr/api/newDatasetDetail.do
	
	@Id
	@Column(name="NUM")
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
   					generator = "NUTRI_SEQ_GENERATOR") 
	private int num;
	
	@Column(name="FOOD_ID")
	private int foodId; // 사용자 추가
	
	@Column(name="FOOD_NAME")
	private String foodName; // 사용자 추가
	
	@Column(name="FOOD_SEARCH_NAME")
	private String foodSearchName; // 사용자 추가
	
	@Column(name="FOOD_CD")
	private String foodCd;
	
	@Column(name="SAMPLING_REGION_NAME")
	private String samplingRegionName;
	
	@Column(name="SAMPLING_MONTH_NAME")
	private String samplingMonthName;
	
	@Column(name="SAMPLING_REGION_CD")
	private String samplingRegionCd;
	
	@Column(name="SAMPLING_MONTH_CD")
	private String samplingMonthCd;
	
	@Column(name="GROUP_NAME")
	private String groupName;
	
	@Column(name="DESC_KOR")
	private String descKor;
	
	@Column(name="RESEARCH_YEAR")
	private String researchYear;
	
	@Column(name="MAKER_NAME")
	private String makerName;
	
	@Column(name="SUB_REF_NAME")
	private String subRefName;
	
	@Column(name="SERVING_SIZE")
	private float servingSize;
	
	@Column(name="SERVING_UNIT")
	private String servingUnit;
	
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
	public NutriVO(JSONObject json) {
		
		this.samplingRegionName = json.getString("SAMPLING_REGION_NAME");
		this.samplingRegionCd = json.getString("SAMPLING_REGION_CD");		
		this.samplingMonthCd = json.getString("SAMPLING_MONTH_CD");
		this.groupName = json.getString("GROUP_NAME");
		this.descKor = json.getString("DESC_KOR");
		this.researchYear = json.getString("RESEARCH_YEAR");
		this.makerName = json.getString("MAKER_NAME");
		this.subRefName = json.getString("SUB_REF_NAME");
		this.servingSize = json.getString("SERVING_SIZE").equals("") ? 0.0F : json.getFloat("SERVING_SIZE");
		this.servingUnit = json.getString("SERVING_UNIT");
		this.nutrCont1 = json.getString("NUTR_CONT1").equals("") ? 0.0F : json.getFloat("NUTR_CONT1");
		this.nutrCont2 = json.getString("NUTR_CONT2").equals("") ? 0.0F : json.getFloat("NUTR_CONT2");
		this.nutrCont3 = json.getString("NUTR_CONT3").equals("") ? 0.0F : json.getFloat("NUTR_CONT3");
		this.nutrCont4 = json.getString("NUTR_CONT4").equals("") ? 0.0F : json.getFloat("NUTR_CONT4");
		this.nutrCont5 = json.getString("NUTR_CONT5").equals("") ? 0.0F : json.getFloat("NUTR_CONT5");
		this.nutrCont6 = json.getString("NUTR_CONT6").equals("") ? 0.0F : json.getFloat("NUTR_CONT6");
		this.nutrCont7 = json.getString("NUTR_CONT7").equals("") ? 0.0F : json.getFloat("NUTR_CONT7");
		this.nutrCont8 = json.getString("NUTR_CONT8").equals("") ? 0.0F : json.getFloat("NUTR_CONT8");
		this.nutrCont9 = json.getString("NUTR_CONT9").equals("") ? 0.0F : json.getFloat("NUTR_CONT9");
		
		this.foodCd = json.getString("FOOD_CD");
	} //

}
