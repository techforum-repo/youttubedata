package com.cloudmersive;

import com.cloudmersive.client.*;
import com.cloudmersive.client.invoker.*;
import com.cloudmersive.client.invoker.auth.*;
import java.io.*;
import java.math.BigDecimal;

public class Main {

	public static void main(String[] args) {

		try {

			ApiClient defaultClient = Configuration.getDefaultApiClient();

			ApiKeyAuth Apikey = (ApiKeyAuth) defaultClient.getAuthentication("Apikey");
			Apikey.setApiKey("YOUR API KEY");

			EditPdfApi apiInstance = new EditPdfApi();
			File inputFile = new File("input pdf path"); 
			
			try {
				
				apiInstance.editPdfWatermarkTextAsync("DRAFT", inputFile, null, new BigDecimal(100),
						"#808080", null, new Callback<byte[]>());

			} catch (ApiException e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
