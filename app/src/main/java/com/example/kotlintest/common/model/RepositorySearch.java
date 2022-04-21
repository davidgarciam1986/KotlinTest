package com.example.kotlintest.common.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class RepositorySearch{

	@SerializedName("total_count")
	private int totalCount;

	@SerializedName("incomplete_results")
	private boolean incompleteResults;

	@SerializedName("items")
	private List<SearchItem> items;

	public void setTotalCount(int totalCount){
		this.totalCount = totalCount;
	}

	public int getTotalCount(){
		return totalCount;
	}

	public void setIncompleteResults(boolean incompleteResults){
		this.incompleteResults = incompleteResults;
	}

	public boolean isIncompleteResults(){
		return incompleteResults;
	}

	public void setItems(List<SearchItem> items){
		this.items = items;
	}

	public List<SearchItem> getItems(){
		return items;
	}

	@Override
 	public String toString(){
		return 
			"{" +
			"total_count = '" + totalCount + '\'' + 
			",incomplete_results = '" + incompleteResults + '\'' + 
			",items = '" + items + '\'' + 
			"}";
		}
}