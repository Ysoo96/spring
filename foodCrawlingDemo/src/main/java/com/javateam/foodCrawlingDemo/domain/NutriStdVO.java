package com.javateam.foodCrawlingDemo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="NUTRI_STD_TBL")
@Data
public class NutriStdVO {

	@Id
	@Column(name="NUM")
	private Integer num;

	@Column(name="GENDER")
	private char gender; // 성별

	@Column(name="AGE")
	private String age; // 나이대(연령)

	@Column(name="ENERGY")
	private int energy; // 에너지(kcal/일)

	@Column(name="CARBOHYDRATE")
	private int carbohydrate; // 탄수화물(g/일)

	@Column(name="PROTEIN")
	private int protein; // 단백질(g/일)

	@Column(name="SUGAR")
	private String sugar; // 당류(%) : 총에너지 대비

	@Column(name="NATRIUM")
	private int natrium; // 나트륨(mg)

	@Column(name="CHOLESTEROL")
	private int cholesterol; // 콜레스테롤(mg) : 지질(총계)

	@Column(name="FAT")
	private String fat; // 지방(%) : 지질

	@Column(name = "FATTY_ACID")
	private int fattyAcid; // 포화지방산(%) : 지질

	@Column(name = "TRANS_FATTY_ACID")
	private int transFattyAcid; // 트랜스지방산(%) : 지질

}