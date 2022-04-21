package com.example.kotlintest.common.model;

import com.google.gson.annotations.SerializedName;

public class TreeItem{

	@SerializedName("mode")
	private String mode;

	@SerializedName("path")
	private String path;

	@SerializedName("size")
	private int size;

	@SerializedName("type")
	private String type;

	@SerializedName("sha")
	private String sha;

	@SerializedName("url")
	private String url;

	public void setMode(String mode){
		this.mode = mode;
	}

	public String getMode(){
		return mode;
	}

	public void setPath(String path){
		this.path = path;
	}

	public String getPath(){
		return path;
	}

	public void setSize(int size){
		this.size = size;
	}

	public int getSize(){
		return size;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setSha(String sha){
		this.sha = sha;
	}

	public String getSha(){
		return sha;
	}

	public void setUrl(String url){
		this.url = url;
	}

	public String getUrl(){
		return url;
	}

	@Override
 	public String toString(){
		return 
			"{" +
			"mode = '" + mode + '\'' + 
			",path = '" + path + '\'' + 
			",size = '" + size + '\'' + 
			",type = '" + type + '\'' + 
			",sha = '" + sha + '\'' + 
			",url = '" + url + '\'' + 
			"}";
		}
}