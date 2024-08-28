package com.javateam.foodCrawlingDemo.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import kr.bydelta.koala.POS;
import kr.bydelta.koala.data.Morpheme;
import kr.bydelta.koala.data.Sentence;
import kr.bydelta.koala.data.Word;
import kr.bydelta.koala.okt.SentenceSplitter;
import kr.bydelta.koala.okt.Tagger;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RealFoodMakerFinder {
	
	// 참고) 식약처 식품 영양소 검색 : https://various.foodsafetykorea.go.kr/nutrient/simple/search/firstList.do
	
	public String extractPureMakerName(String productName) {
		
		String result;
		
		String words[] = productName.split("\\)");
		result = words.length == 1 ? "" : words[0];
		
		return result;
	}
	
	// 버그) 롯데)빅팜 => "빅"만 명사로 형태소 분석함
	public String extractPureProductName(String productName) {
		
		List<String> unusedWords = new ArrayList<>();
		unusedWords = Arrays.asList("컵");
		
		String result = "";
		
		// https://docs.komoran.kr/index.html
		// Java 지원 한글 형태소 분석 라이브러리 
		Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);
		
		String words[] = productName.split("\\)");
		
		String realProductName = words.length == 1 ? words[0] : words[1];
		
		KomoranResult komoranResult = komoran.analyze(realProductName);
		
		// List<Token> nounList = komoranResult.getTokenList();
		
		List<String> nounList = komoranResult.getNouns();
		
		log.info("----------- 명사 추출 ---------");
		nounList.forEach(x->{ log.info("{}", x);});
		log.info("----------- 명사 추출 ---------");
		
		int limit = nounList.size();
		
		for (int i=0; i<limit; i++) {
			
			if (i == limit-1) {
		
				// 검색 불용어 제거 ex) ---컵 (접미어)
				// 마지막 단어에 검색 불용어 포함시 배제(미포함)
				if (unusedWords.contains(nounList.get(i)) == false) {
					result += nounList.get(i);
				}
				
			} else {
				result += nounList.get(i);
			} //
		}
		
		return result;
	}
	
	// koalanlp 라이브러리 사용시 위 문제 해결) 
	// 롯데)빅팜 => "빅팜" 전체를 명사로 형태소 분석함
	// 다른 문제점) 롯데)키스틱 => 롯데) "키스"로 분석하여 엉뚱하게 음료수(밀키스)로 검색함. "틱"을 NNP(고유명사) 성분이 아닌 XSO(접미어) 성분으로 인식
	// 참고 사이트 : 형태소 라이브러리 간의 비교) https://koalanlp.github.io/sample/comparison.html
	// 참고 소스) https://github.com/koalanlp/sample/blob/master/java/src/main/java/sample/TaggerJava.java
	// 
	// API reference) https://javadoc.io/doc/kr.bydelta/koalanlp-core_2.10/1.3.3/kr/bydelta/koala/POS$.html
	// 
	public String extractPureProductNameByKoalaNLP(String productName) {
		
		List<String> unusedWords = new ArrayList<>();
		unusedWords = Arrays.asList("컵");
		
		String result = "";

		// KoalaNLP
		// https://koalanlp.github.io/koalanlp/
		// 한국어 형태소 및 구문 분석기
		// SentenceSplitter splitter = new SentenceSplitter();
		Tagger tagger = new Tagger();
		
		String words[] = productName.split("\\)");		
		String realProductName = words.length == 1 ? words[0] : words[1];
		 
		// List<String> nounList = splitter.sentences(realProductName);
		List<Sentence> sentList = tagger.tag(realProductName);
		
//		List<String> nounList = komoranResult.getNouns();
		
		log.info("----------- 명사 추출 ---------");
		
		List<String> nounList = new ArrayList<>();
		
		// 
		for (Word word : sentList.get(0)) {
			
			log.info("{}", word.getSurface());
			
			for (Morpheme morph : word) {
				
				// 상품 검색어에 명사/접미사 포함
				if (morph.getTag().equals(POS.NNG) == true || morph.getTag().equals(POS.XSO)) { // 명사 성분 뿐만아니라 접미사 성분도 포함하여 검색 문제 패치 
					
					log.info("명사 : {}", morph.getTag());
					nounList.add(morph.getSurface());
				}
				
				log.info("{} / {}", morph.getSurface(), morph.getTag());
			} //
		} //
		
		log.info("------------------------------------");
		
		nounList.forEach(x->{ log.info("{}", x);});
		
		log.info("----------- 명사 추출 ----------");
		
		int limit = nounList.size();
		
		for (int i=0; i<limit; i++) {
			
			if (i == limit-1) {
		
				// 검색 불용어 제거 ex) ---컵 (접미어)
				// 마지막 단어에 검색 불용어 포함시 배제(미포함)
				if (unusedWords.contains(nounList.get(i)) == false) {
					result += nounList.get(i);
				}
				
			} else {
				result += nounList.get(i);
			} //
		}
		
		return result;
	}
	
	public String findRealMaker(String productName, String makerName) {
		
		String result = "";
		String url = "https://various.foodsafetykorea.go.kr/nutrient/simple/search/companyCode/listPop.do?searchText="+makerName;
		
		RestTemplate restTemplate = new RestTemplate();
		
		// HTTP header 설정
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		 
		// 응답(response) 정보
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class); 
		
		// https://jsoup.org/cookbook/input/parse-document-from-string
		Document doc = Jsoup.parse(response.getBody().trim()); // response.getBody();
		
		Elements elements = doc.select("div#pop-wrap div#content form#listForm table.table td input");
		
		// 검색된 사항이 없으면.... 
		if (elements.size() == 0) {
			
			result = "없음";
			
		} else {
			log.info("요소 인쇄 : " + elements.get(0).attr("id"));
			// result = elements.get(0).attr("id");
			// 버그 패치 => 동원시스템즈가 우선 검색됨 => 불용 검색어 => "시스템즈"을 빼고 동원으로 교체하여 유사 겅색하도록 유도
			result = elements.get(0).attr("id").replace("시스템즈", "");			
		}
		
		return result;
	}
	
} 