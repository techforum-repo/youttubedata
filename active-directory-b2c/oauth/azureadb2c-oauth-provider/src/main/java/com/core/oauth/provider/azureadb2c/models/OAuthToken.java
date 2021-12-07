package com.core.oauth.provider.azureadb2c.models;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

public class OAuthToken {
	
	  private int _state;
	  
	  private String _ck;
	  
	  private String _key;
	  
	  private String _secret;
	  
	  private Map<String, Object> _attributes;
	
	public OAuthToken(String ck, String key, String secret, int state) {
	    this._ck = ck;
	    this._key = key;
	    this._secret = secret;
	    this._state = state;
	  }
	
	  
	  public void setAttribute(String key, Object value) {
	    if (this._attributes == null)
	      this._attributes = new HashMap<>(); 
	    this._attributes.put(key, value);
	  }
	 
	  public String toJSON() throws IOException {
		    try {
		      JSONObject json = new JSONObject();
		      json.put("st", this._state);
		      json.put("ck", this._ck);
		      if (this._key != null)
		        json.put("k", this._key); 
		      if (this._secret != null)
		        json.put("sk", this._secret); 
		      if (this._attributes != null)
		        json.put("a", this._attributes); 
		      return json.toString();
		    } catch (JSONException je) {
		      throw new IOException(je.getMessage());
		    } 
		  }
}
