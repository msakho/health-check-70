/**
 * @author Meissa
 */
package com.redhat.consulting.jdg.healthcheck.refapp;

import java.io.Serializable;

/**
 * @author Meissa
 */
public class CustomKey implements Serializable{
	
	/**
	 * @param key
	 */
	public CustomKey(String key) {
		super();
		this.key = key;
	}

	private String key;

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}
	

}
