package com.trekiz.admin.modules.reviewflow.entity;

import java.util.List;
import java.util.Map;

public class ReviewInfo {

	private Review review;
	private List<ReviewDetail> list;
	private Map<Object,Object>map;
	
	public ReviewInfo(){
		
	}
	public ReviewInfo(Review review){
		this.review=review;
	}
	public ReviewInfo(Review review,List<ReviewDetail> list){
		this.review=review;
		this.list=list;
	}
	public ReviewInfo(Review review,List<ReviewDetail> details,Map<Object,Object>map){
		this.review=review;
		this.list=details;
		this.map=map;
	}
	
	public Review getReview() {
		return review;
	}
	public void setReview(Review review) {
		this.review = review;
	}
	public List<ReviewDetail> getList() {
		return list;
	}
	public void setList(List<ReviewDetail> list) {
		this.list = list;
	}
	public Map<Object, Object> getMap() {
		return map;
	}
	public void setMap(Map<Object, Object> map) {
		this.map = map;
	}
	
	
}
