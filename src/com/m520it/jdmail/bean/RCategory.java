package com.m520it.jdmail.bean;

public class RCategory{
	private long id;//分类id
	private long parentId;//分类父id
	private String bannerUrl;//分类图片路径
	private String name;//分类名称
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getParentId() {
		return parentId;
	}
	public void setParentId(long parentId) {
		this.parentId = parentId;
	}
	public String getBannerUrl() {
		return bannerUrl;
	}
	public void setBannerUrl(String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "RCategory [id=" + id + ", parentId=" + parentId
				+ ", bannerUrl=" + bannerUrl + ", name=" + name + "]";
	}
	
}
