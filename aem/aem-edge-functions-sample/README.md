# aem-edge-functions-boilerplate

## Introduction

This boilerplate serves as an example of what is possible to achieve with AEM Edge Functions. The repository contains a simple server that exposes multiple endpoints and makes usage of:

- Edge Functions service provisioning
- Configurations usage (ConfigStore)
- Secrets usage (SecretStore)
- KV store usage (KVStore)
- Logging
- Log Tailing

Functions source code can be found under `./src`, tests can be found under `./test` and are executable through `mocha`.
The service configuration is defined in `config/edgeFunctions.yaml` and CDN routing in `config/cdn.yaml`.

## Setup

### Adobe CLI

First install Adobe CLI (aio) with the following command

```
npm install -g @adobe/aio-cli
```

Next install the aio-cli AEM Edge Functions plugin

```
aio plugins:install @adobe/aio-cli-plugin-aem-edge-functions
```

Finally run the following commands to finish setting up the aio AEM Edge Functions plugin.

```
aio login
aio aem edge-functions setup
```

This command will ask you to login and then to select your environment on which you want to use AEM Edge Functions.

Note: You will need to have the **AEM Administrator Product Profile** in order to be able to deploy code to your edge functions

### Boilerplate

Run the following command from the boilerplate project to setup your machine

```
npm install
```

## Create your edge functions

The first step to create your edge functions is to ensure that you have setup a configuration pipeline for your environment in Cloud Manager. If it is not the case, please follow this [documentation](https://experienceleague.adobe.com/en/docs/experience-manager-cloud-service/content/operations/config-pipeline) to create your configuration pipeline.

In case you are using a RDE, you can deploy your configuration using the command `aio aem:rde:install -t env-config ./config`.

Edge Functions creation is done via configuration file, you will need to create a YAML file (eg. edgeFunctions.yaml) with the following configuration:

```
kind: "EdgeFunctions"
version: "1"
data:
  functions:
    - name: my-edge-function
  # Uncomment to enable secrets
  # secrets:
  #   - key: API_TOKEN
  #     value: ${{ API_TOKEN_SECRET }}
  # Uncomment to enable a KV store
  # kvs: true
```

The configuration is composed of:

- **functions**: contains a list of edge functions, where a function is composed of a **name** and a set of **origins**. The function name must be at most **30 characters** long, start with a lowercase letter, end with a lowercase letter or digit, and contain only lowercase letters, digits, and hyphens. The default limit is 1 function for AEM as a Cloud Service environments and 3 for Edge Delivery Services sites.
- **configs**: contains a key/value configs arrays that will be exposed to all your edge functions
- **secrets**: contains a key/value secrets arrays that will be exposed to all your edge functions
- **kvs**: when set to `true`, provisions an empty KV store named `kv_default` that all your edge functions can read from and write to at runtime

Additionally, you will need to define routing rules in your CDN configuration file (eg. cdn.yaml) using CDN origin selectors rules:

```
kind: 'CDN'
version: '1'
data:
  originSelectors:
    rules:
      - name: route-weather-to-edge-function
        when: { reqProperty: path, equals: "/weather" }
        action:
          type: selectAemOrigin
          originName: edgefunction-my-edge-function
      - name: route-hello-world-to-edge-function
        when: { reqProperty: path, equals: "/hello-world" }
        action:
          type: selectAemOrigin
          originName: edgefunction-my-edge-function
```

**Note**: If you already have a CDN configuration file, just add the origin selectors rules to your existing configuration

The origin selector rule enables you to route your traffic to your edge functions under your own conditions (such as routing a specific domain or only under a certain path). See [official documentation](https://experienceleague.adobe.com/en/docs/experience-manager-cloud-service/content/implementing/content-delivery/cdn-configuring-traffic#origin-selectors) to learn more about Origin Selector.

Once you have created your configuration, you will need to commit your changes to your Git Repository and trigger the configuration pipeline. Once the configuration pipeline succeeds, you should be able to access your edge function endpoints:

- *publish-pXXXXX-eYYYYY.adobeaemcloud.com/weather* or *example.com/weather*
- *publish-pXXXXX-eYYYYY.adobeaemcloud.com/hello-world* or *example.com/hello-world*

where **pXXXXX-eYYYYY** is your environment coordinates.

## Build

The following command will package your edge function code for deployment to your edge function service.

```
aio aem edge-functions build
```

## Deploy

The following command will deploy your package to your edge function. You will have to set the argument function-name to the name you gave your function in the edge function configuration file.

```
aio aem edge-functions deploy <function-name>
```

## Local run

The following command will run your edge function code locally and exposed a server at `http://127.0.0.1:7676`

```
aio aem edge-functions serve
```

You can learn more about what is supported by Local runtime on [Fastly documentation](https://www.fastly.com/documentation/reference/cli/compute/serve/).

## Test

The following command will execute tests using `mocha`.

```
npm run test
```

## Remote debugging

Adobe Managed CDN only offers remote logging as a way to debug your program. The following command will tail your edge function standard output. You will be able to get runtime console.log from your edge function  directly in your terminal. You will have to set the argument function-name to the name you gave your function in the edge function configuration file.

```
aio aem edge-functions tail-logs <function-name>
```

## List Edge Functions

To list all Edge Functions configured for your environment:

```
aio aem edge-functions list
```

This displays a table with the name, active package ID, and timestamps of all available Edge Functions.

## Configuration

### Origins

Adobe Managed CDN allows edge function to access any origins by default. In case you want to restrict access to only defined origins (see [Fastly Documentation](https://js-compute-reference-docs.edgecompute.app/docs/fastly:backend/enforceExplicitBackends)), you will need to define your origins in your edge functions definition (in the edge function configuration file) as an array of origins similar to the [CDN Origin Selectors feature](https://experienceleague.adobe.com/en/docs/experience-manager-cloud-service/content/implementing/content-delivery/cdn-configuring-traffic#origin-selectors).

Given the following configuration

```
origins:
  - name: my-origin-name
    domain: example.com
```

you will be able to select the origin to use for your request as follow

```
const request = new Request("https://example.com/test");
const response = await fetch(request, { backend: "my-origin-name" });
```

> **Note:** Service stores (`configs`, `secrets`, and `kvs`) are not available in [sandbox programs](https://experienceleague.adobe.com/en/docs/experience-manager-cloud-service/content/implementing/using-cloud-manager/programs/introduction-sandbox-programs). Edge function services themselves run normally on sandbox environments — only the stores are not provisioned.

### Edge Function Environment Configuration

Adobe Managed CDN allows you to use environment variable in your code through config store. Those environment variables can be defined in the edge functions configuration file under configs as an array of objects containing a key and a value fields.

Given the following configuration

```
configs:
  - key: LOG_LEVEL
    value: DEBUG
```

you will be able to get your environment variable as follow

```
import { ConfigStore } from "fastly:config-store";
...
const config = new ConfigStore('config_default');
const logLevel = config.get('LOG_LEVEL') || 'info';
```

**Notes:**
- The config store will always be named config_default
- key name are case-sensitive
- The config store is shared between all your edge functions

### Edge Function Secrets

Adobe Managed CDN allows you to use secrets in your code through secret store. Those secrets can be defined in the edge functions configuration file under secrets as an array of objects containing a key and a value fields. Note that the value field **does not** contain the secret, but a reference to the secret (${{SECRET_REFERENCE}}). The secret needs to be defined in Cloud Manager as described in this [documentation](https://experienceleague.adobe.com/en/docs/experience-manager-cloud-service/content/operations/config-pipeline#secret-env-vars).

Given the following configuration

```
secrets:
  - key: API_TOKEN
    value: ${{ API_TOKEN_SECRET }}
```

you will be able to get your secret using the secret manager class available in the boilerplate. Secrets get be retrieved as follow

```
import { SecretStoreManager } from "./lib/config";
...
const apiToken = await SecretStoreManager.getSecret('API_TOKEN');
```

**Notes:**
- The secret store will always be named secret_default
- key name are case-sensitive
- **Secrets are immutable**
- The secret store is shared between all your edge functions

### Edge Function KV Store

Adobe Managed CDN allows you to read and write arbitrary key-value data at runtime through a KV store. To enable it, set `kvs: true` in the edge functions configuration file:

```
kvs: true
```

This provisions an empty KV store named `kv_default`. You can interact with the store from your edge function code using the [Fastly KV Store API](https://js-compute-reference-docs.edgecompute.app/docs/fastly:kv-store/KVStore):

```
import { KVStore } from "fastly:kv-store";
...
const kv = new KVStore('kv_default');

// Read a value
const entry = await kv.get('visit-count');
const count = entry ? Number(await entry.text()) : 0;

// Write a value
await kv.put('visit-count', String(count + 1));
```

**Notes:**
- The KV store will always be named kv_default
- The KV store is empty at provision time; populate it at runtime via the Fastly KV Store API (declarative key/value entries in `edgeFunctions.yaml` are not supported)
- The KV store is shared between all your edge functions

### Logging

AEM Edge Functions is compatible with the [log forwarding feature](https://experienceleague.adobe.com/en/docs/experience-manager-cloud-service/content/implementing/developing/log-forwarding).

You can define your logging configuration as follow (create a logForwarding.yaml file next to the edgeFunctions.yaml)

```
kind: "LogForwarding"
version: "1"
data:
  splunk:
    default:
      enabled: true
      host: "splunk-host.example.com"
      token: "${{SPLUNK_TOKEN}}"
      index: "AEMaaCS"
```

and use the logger in your code as follow:

```
import { Logger } from "fastly:logger";
...
const logger = new Logger("customerSplunk");
logger.log(JSON.stringify({
  method: event.request.method,
  url: event.request.url
}));
```

## Caching

AEM Edge Functions sit between the CDN and the origin. There are **two distinct caches** in the request flow:

```
Browser → AEM CDN (CDN Cache) → AEM Edge Functions (Fetch Cache) → Origin
```

| Cache | What it caches | Influenced by | How to purge |
|-------|---------------|---------------|--------------|
| **CDN Cache** | The Edge Function's response to the browser | Response headers set by the Edge Function (`Cache-Control`, `Surrogate-Control`, `Surrogate-Key`) | [CDN Cache Purge API](https://experienceleague.adobe.com/en/docs/experience-manager-cloud-service/content/implementing/content-delivery/cdn-cache-purge) |
| **Edge Function Fetch Cache** | The origin's response to `fetch()` calls within the Edge Function, **and** any data stored via the Core Cache API | Origin response headers or [`CacheOverride`](https://js-compute-reference-docs.edgecompute.app/docs/fastly:cache-override/CacheOverride/) on fetch calls; surrogate keys assigned programmatically for Core Cache entries | `aio aem edge-functions purge-cache` CLI command or `purgeSurrogateKey()` |

### CDN Cache (Outer)

The CDN cache sits between the browser and the Edge Function. It caches the Edge Function's **response**. You control its behavior by setting standard HTTP cache headers on your Edge Function responses:

```javascript
return new Response(body, {
  headers: {
    'Surrogate-Key': 'page-home product-123',
    'Cache-Control': 'public, max-age=3600'
  }
});
```

Multiple surrogate keys are separated by spaces. These surrogate keys can be used to purge the CDN cache using the [CDN Cache Purge API](https://experienceleague.adobe.com/en/docs/experience-manager-cloud-service/content/implementing/content-delivery/cdn-cache-purge).

### Edge Function Fetch Cache (Inner)

The Edge Function fetch cache sits between the Edge Function and the origin. It caches the **origin's response** to `fetch()` calls made within your Edge Function code. It also holds any data your code stores via the **Core Cache API** or **Simple Cache API**.

This cache is influenced by:
- The origin's response headers (e.g., `Cache-Control` returned by the origin)
- The [`CacheOverride`](https://js-compute-reference-docs.edgecompute.app/docs/fastly:cache-override/CacheOverride/) option on your fetch calls
- Surrogate keys assigned programmatically when writing to the Core Cache API

> **Important:** The `Surrogate-Key` header you set on your Edge Function's *outgoing response* to the browser controls the **outer CDN cache**, not this inner cache. Surrogate keys for the inner cache come from the origin's `Surrogate-Key` response header or from keys you assign when writing to the Core Cache API.

To cache origin responses for a specific duration:

```javascript
import { CacheOverride } from "fastly:cache-override";

const response = await fetch(request, {
  backend: "my-origin",
  cacheOverride: new CacheOverride({ ttl: 300 })
});
```

To bypass the fetch cache entirely and always fetch from the origin, use `pass` mode:

```javascript
import { CacheOverride } from "fastly:cache-override";

const response = await fetch(request, {
  backend: "my-origin",
  cacheOverride: new CacheOverride({ mode: "pass" })
});
```

### Purging the Edge Function Fetch Cache

The `purge-cache` CLI command purges the **Edge Function fetch cache** (the origin responses cached within the Edge Function). It does **not** purge the outer CDN cache.

```
# Purge by surrogate key
aio aem edge-functions purge-cache <function-name> --surrogateKey my-page-key

# Purge multiple surrogate keys
aio aem edge-functions purge-cache <function-name> -k key1 -k key2

# Purge all cached content (use with caution)
aio aem edge-functions purge-cache <function-name> --all

# Soft purge (stale content served while revalidating)
aio aem edge-functions purge-cache <function-name> --surrogateKey my-key --soft
```

You can also purge surrogate keys programmatically from within your Edge Function code using the [`purgeSurrogateKey`](https://js-compute-reference-docs.edgecompute.app/docs/fastly:compute/purgeSurrogateKey) function:

```javascript
import { purgeSurrogateKey } from "fastly:compute";

// Hard purge (immediate removal)
purgeSurrogateKey("my-page-key");

// Soft purge (retain stale entries for revalidation)
purgeSurrogateKey("my-page-key", true);
```

### Purging the CDN Cache

To purge the outer CDN cache (the Edge Function's response cached at the CDN layer), use the [CDN Cache Purge API](https://experienceleague.adobe.com/en/docs/experience-manager-cloud-service/content/implementing/content-delivery/cdn-cache-purge). This is the same purge mechanism used for all AEM Cloud Service content cached at the CDN.

In the AEM as a Cloud Service architecture, Edge Functions receive traffic from the CDN via [origin selectors](https://experienceleague.adobe.com/en/docs/experience-manager-cloud-service/content/implementing/content-delivery/cdn-configuring-traffic#origin-selectors). The complete request flow is:

```
Client → AEM CDN (VCL) → Origin Selector → Edge Function → Origin
```

The CDN caches the final response returned by the Edge Function. A CDN purge clears **only** that outer cached response — it has no effect on the Edge Function's internal caches. Similarly, the `purge-cache` command clears **only** the Edge Function's internal caches — it does not clear the CDN cache.

> **Coordinating purges across both layers:** Because the Edge Function sits behind the CDN as a backend origin (reachable through origin selectors), these two caches operate independently:
>
> 1. Purging only the CDN cache causes the next request to invoke the Edge Function via the origin selector. If the Edge Function's fetch cache still holds stale data, it will return old content — which the CDN then caches again.
> 2. Purging only the Edge Function cache clears internal state, but the CDN continues serving its previously cached copy until it expires or is separately purged.
>
> **Best practice:** When underlying data changes, purge **both** caches — use the CDN Cache Purge API for the outer layer and `purge-cache` (or `purgeSurrogateKey()`) for the inner Edge Function layer. Using consistent surrogate keys across both layers simplifies coordinated invalidation.

## References

https://www.fastly.com/documentation/guides/compute/