/*
Copyright 2025 Adobe. All rights reserved.
This file is licensed to you under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License. You may obtain a copy
of the License at http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under
the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR REPRESENTATIONS
OF ANY KIND, either express or implied. See the License for the specific language
governing permissions and limitations under the License.
*/
// Open-Meteo.com API data are offered under Attibution 4.0 International (CC BY 4.0) license.
// Please see https://open-meteo.com/en/licence for more information.

/// <reference types="@fastly/js-compute" />

import { getGeolocationForIpAddress } from "fastly:geolocation";
import { SecretStoreManager } from "./lib/config";

async function weatherHandler(req, client) {
    let apiToken = 'helloworld';
    try {
        // Serve as an example of how to use the secret store
        apiToken = await SecretStoreManager.getSecret('API_TOKEN');
    } catch (e) {
        console.warn('No API_TOKEN secret found, using default api token');
    }
    // Prefer the leftmost IP in X-Forwarded-For (original client) over
    // client.address, which is typically just the nearest CDN edge node.
    const xff = req.headers.get("x-forwarded-for");
    const clientIp = xff ? xff.split(",")[0].trim() : client?.address;
    console.log(`Received request for weather data from IP: ${clientIp} (X-Forwarded-For: ${xff ?? "not set"})`);
    const locationInfo = getGeolocationForIpAddress(clientIp);
    console.log("Location Information:\n", locationInfo);
    const request = new Request("https://api.open-meteo.com/v1/forecast?current=temperature_2m&latitude=" + locationInfo.latitude + "&longitude=" + locationInfo.longitude);
    request.headers.set("Authorization", "Bearer " + apiToken);
    const backendResponse = await fetch(request);
    if(backendResponse.status !== 200) {
        return new Response("Error fetching weather data", { status: 500 });
    } else {
        const data = await backendResponse.json();
        console.log("Weather API Response:\n", data);
        let resp = "No weather data available";
        if(data.current?.temperature_2m) {
        resp = `It seems you are based in ${locationInfo.city} (Weather data by Open-Meteo.com - https://open-meteo.com/) where the local temperature is ${data.current.temperature_2m}°C`;
        }
        return new Response(resp, { status: 200, headers: { "Cache-Control": "max-age=5m" } });
    }
}

export { weatherHandler };
