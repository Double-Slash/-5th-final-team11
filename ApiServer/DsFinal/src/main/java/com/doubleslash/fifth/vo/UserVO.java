package com.doubleslash.fifth.vo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@Entity
@Table(name = "Users")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class UserVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@NonNull
	private String uid;
	
	private String nickname;
	
	private Float drink;
	
	private Integer hangover;
	
}