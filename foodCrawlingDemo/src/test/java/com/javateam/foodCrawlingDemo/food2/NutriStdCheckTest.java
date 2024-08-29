package com.javateam.foodCrawlingDemo.food2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.javateam.foodCrawlingDemo.domain.NutriInfoVO;
import com.javateam.foodCrawlingDemo.domain.NutriStdVO;
import com.javateam.foodCrawlingDemo.repository.NutriInfoRepository;
import com.javateam.foodCrawlingDemo.repository.NutriStdRepository;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class NutriStdCheckTest {

//	@Autowired
//	CURepository cuRepository;

	@Autowired
	NutriInfoRepository nutriInfoRepository;

	@Autowired
	NutriStdRepository nutriStdRepository;

	/**
	 * 특정 나이의 나이대를 판정  ex) 20 ==> "19~29"
	 *
	 * @param age ex) 20
	 * @return
	 */
	private String calcAgeBand(int age) {

		String result;

		// ex) 19~29
		List<Object> ageRanges
			= Arrays.asList(new String[]{"6~8", "9~11", "12~14", "15~18", "19~29", "30~49", "50~64", "65~74", "75~"});

		// 리스트의 나이 최소 나이 추출하여 나이대의 최솟값의 리스트 생성
		// 나이대의 최솟값으로  6,9,12,15,19,30, .....
		List<Integer> ageRangeMins = new ArrayList<>();

		ageRanges.forEach(x->{
			ageRangeMins.add(Integer.parseInt(x.toString().split("~")[0]));
		});

		// ageRangeMins.forEach(x->log.info("{}", x));

		// 해당되는 나이(가령 20) 보다 이하 인(작거나 같은) 나이 중에서 최댓값을 구함 => 19
		String temp = ageRangeMins.stream()
							.filter(x-> x <= age)
							.max(Comparator.naturalOrder())
							.get()
							.toString();

		// log.info("temp = {}", temp);

		// 나이대 계산
		result = ageRanges.stream()
						  .filter(x->x.toString().contains(temp))
						  .findFirst()
						  .get().toString();

		return result;
	}

	/**
	 * 성별, 나이, 섭취 음식들을 입력하여 영양소(열량, 탄수화물, 단배질) 충족/부족 여부를 점검하여 출력
	 *
	 * @param gender 성별 ex) '남'
	 * @param age 나이 ex) 20
	 * @param foodNames 섭취 음식물 ex) "진짬뽕", "튀김우동큰사발컵", "킹뚜껑", "너비아니토스트"
	 * @return
	 */
	private Map<String, String> checkFoodByNutriStd(char gender, int age, String ... foodNames) {

		Map<String, String> result = new HashMap<>();

		String ageRange = calcAgeBand(age);

		// 특정 성별/나이대의 권장 섭취 영양정보 파악
		NutriStdVO nutriStdVO = nutriStdRepository.findByGenderAndAge(gender, ageRange).get();

		// NutriStdVO(num=5, gender=남, age=19~29, energy=2600,
		// carbohydrate=130, protein=65, sugar=10~20, natrium=1500, cholesterol=300,
		// fat=15~30, fattyAcid=7, transFattyAcid=1)
		log.info("권장 섭취량 : {}", nutriStdVO);

		// 식품 영양 정보
		// NutriInfoVO(num=3, foodId=121, foodName=진짬뽕, descKor=라면_진짬뽕, makerName=오뚜기라면(주),
		// nutrCont1=385.0, nutrCont2=60.77, nutrCont3=7.69, nutrCont4=12.31, nutrCont5=5.38,
		// nutrCont6=1369.0, nutrCont7=21.54, nutrCont8=5.38, nutrCont9=0.0)

		// 음식들의 양양성분 총계
		float energy = 0; // 칼로리
		int carbohydrate = 0; // 탄수화물
		int protein = 0; // 단백질

		for (String foodName : foodNames) {

			NutriInfoVO nutriInfoVO = nutriInfoRepository.findByFoodName(foodName);
			log.info("nutriInfoVO : {}", nutriInfoVO);

			energy += nutriInfoVO.getNutrCont1();
			carbohydrate += nutriInfoVO.getNutrCont2();
			protein += nutriInfoVO.getNutrCont3();
		} // for

		log.info("열량(칼로리 : kcal) = {}", energy);
		log.info("탄수화물(g) = {}", carbohydrate);
		log.info("단백질(g) = {}", protein);

		log.info("nutriStdVO : {}", nutriStdVO);

		// 열량(칼로리) 점검
		String msg = energy >= nutriStdVO.getEnergy() ? "충족" : "부족";
		result.put("열량(칼로리)", msg);

		// 탄수화물 점검
		msg = carbohydrate >= nutriStdVO.getCarbohydrate() ? "충족" : "부족";
		result.put("탄수화물", msg);

		// 단백질 점검
		msg = protein >= nutriStdVO.getProtein() ? "충족" : "부족";
		result.put("단백질", msg);

		return result;
	}

	@Test
	public void test() {

		Map<String, String> map = checkFoodByNutriStd('남', 20, "진짬뽕", "튀김우동큰사발컵", "킹뚜껑", "너비아니토스트");
		// Map<String, String> map = checkFoodByNutriStd('남', 67, "진짬뽕", "튀김우동큰사발컵", "킹뚜껑", "너비아니토스트", "와사비맛땅콩");
		// Map<String, String> map = checkFoodByNutriStd('남', 67, "와사비맛땅콩", "와사비맛땅콩", "와사비맛땅콩", "와사비맛땅콩", "와사비맛땅콩");
		map.entrySet().forEach(x -> log.info("{}", x));

	} //

}