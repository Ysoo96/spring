package com.javateam.SpringJPA2.domain;

import java.util.Objects;

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
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name="TEST_TBL")
@SequenceGenerator(name = "TEST_TBL_Generator",
				   sequenceName = "TEST_TBL_SEQ",
				   initialValue = 1,
				   allocationSize = 1)
@Data
// @EqualsAndHashCode
@NoArgsConstructor // 기본 생성자
@RequiredArgsConstructor // (필수 인자를 대입)오버로딩 생성자
@AllArgsConstructor // (모든 필드의 인자를 대입)오버로딩 생성자
@Builder
public class TestVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TEST_TBL_Generator")
	@Column(name="MEMBER_ID") // , precision = 10, scale = 0) // number(10,0)
	private long id;
	
	@NonNull
	@Column(name="MEMBER_NAME", nullable = false, length = 20)
	private String name;
	
	@NonNull
	@Column(name="MEMBER_ADDRESS", nullable = true, length = 20)
	private String address;
	
	@NonNull
	@Column(name="MEMBER_AGE", nullable = true)
	// private int age;
	private Integer age;

	/*
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TestVO other = (TestVO) obj;
		return Objects.equals(address, other.address) && Objects.equals(age, other.age)
				&& Objects.equals(name, other.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(address, age, name);
	}
	*/
	
	/*
	public TestVO() {}

	public TestVO(String name, String address, int age) {
		this.name = name;
		this.address = address;
		this.age = age;
	}

	public TestVO(long id, String name, String address, int age) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.age = age;
	}
	*/
}