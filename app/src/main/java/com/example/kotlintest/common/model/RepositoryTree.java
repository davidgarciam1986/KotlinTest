package com.example.kotlintest.common.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class RepositoryTree{

	@SerializedName("tree")
	private List<TreeItem> tree;

	@SerializedName("truncated")
	private boolean truncated;

	@SerializedName("sha")
	private String sha;

	@SerializedName("url")
	private String url;

	public void setTree(List<TreeItem> tree){
		this.tree = tree;
	}

	public List<TreeItem> getTree(){
		return tree;
	}

	public void setTruncated(boolean truncated){
		this.truncated = truncated;
	}

	public boolean isTruncated(){
		return truncated;
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
			"tree = '" + tree + '\'' + 
			",truncated = '" + truncated + '\'' + 
			",sha = '" + sha + '\'' + 
			",url = '" + url + '\'' + 
			"}";
		}
}