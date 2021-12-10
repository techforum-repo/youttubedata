package com.core.oauth.provider.azureadb2c.models;

/**
 * 
 * @author albin
 *
 */

public class AzureADB2CConfig {

	private String b2CLoginDomain;
	private String b2CTenantName;
	private String b2CSignInSignUpPolicy;
	private String b2CEditPolicy;

	public String getB2CLoginDomain() {
		return b2CLoginDomain;
	}

	public void setB2CLoginDomain(String b2cLoginDomain) {
		b2CLoginDomain = b2cLoginDomain;
	}

	public String getB2CTenantName() {
		return b2CTenantName;
	}

	public void setB2CTenantName(String b2cTenantName) {
		b2CTenantName = b2cTenantName;
	}

	public String getB2CSignInSignUpPolicy() {
		return b2CSignInSignUpPolicy;
	}

	public void setB2CSignInSignUpPolicy(String b2cSignInSignUpPolicy) {
		b2CSignInSignUpPolicy = b2cSignInSignUpPolicy;
	}

	public String getB2CEditPolicy() {
		return b2CEditPolicy;
	}

	public void setB2CEditPolicy(String b2cEditPolicy) {
		b2CEditPolicy = b2cEditPolicy;
	}

	@Override
	public String toString() {
		return "[b2CLoginDomain: " + b2CLoginDomain + ",b2CTenantName: " + b2CTenantName + ",b2CSignInSignUpPolicy: "
				+ b2CSignInSignUpPolicy + ",b2CEditPolicy: " + b2CEditPolicy + "]";
	}

}
