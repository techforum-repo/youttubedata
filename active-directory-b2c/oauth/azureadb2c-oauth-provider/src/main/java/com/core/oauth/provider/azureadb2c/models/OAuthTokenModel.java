package com.core.oauth.provider.azureadb2c.models;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

public class OAuthTokenModel {

	private int _state;

	private String _ck;

	private String _key;

	private String _secret;

	private Map<String, Object> _attributes;

	public OAuthTokenModel() {
	}

	public OAuthTokenModel(String ck, String key, String secret, int state) {
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

	public Map<String, Object> getAttributes() {
		return this._attributes;
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

	public static OAuthTokenModel fromJSON(String jsonString) {
		try {
			JSONObject json = new JSONObject(jsonString);
			OAuthTokenModel token = new OAuthTokenModel();
			token._state = json.optInt("st");
			token._ck = json.optString("ck");
			token._key = json.optString("k");
			token._secret = json.optString("sk");
			JSONObject attr = json.optJSONObject("a");
			if (attr != null) {
				Map<String, Object> attributes = new HashMap<>();
				for (Iterator<String> keys = attr.keys(); keys.hasNext();) {
					String key = keys.next();
					attributes.put(key, attr.get(key));
				}
				token._attributes = attributes;
			}
			return token;
		} catch (JSONException jSONException) {
			return null;
		}
	}

}
