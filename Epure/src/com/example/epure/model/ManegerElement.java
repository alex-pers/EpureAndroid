package com.example.epure.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ManegerElement {
	Map<Integer, Element> allElement = new HashMap<Integer, Element>();
	
	
	public ManegerElement() {
		
	}
	public void setElement(int type, Element element){
		allElement.put(type, element);
	}
	public ArrayList<Element> getElementByKey(int type){
		
		return new ArrayList<Element>();
	}
	
//	public 
	
	public int getType(Element element){
		
		return 1;
	}
}
