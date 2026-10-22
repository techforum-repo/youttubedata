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

/// <reference types="@fastly/js-compute" />

import { ConfigStore } from "fastly:config-store";

const logger = new Logger("customerHttps");

const log = (req, resp) => {
  let logLevel = 'info';
  try {
    // Serve as an example of how to use the config store
    const config = new ConfigStore('config_default');
    logLevel = config.get('LOG_LEVEL') || 'info';
  } catch (e) {
    console.warn('No config store found, using default log level');
  }

  const record = {
    method: req.method,
    url: req.url,
    status: resp.status
  };

  if (logLevel === 'debug') {
    record.requestHeaders = req.headers;
    record.responseHeaders = resp.headers;
  }

  logger.log(JSON.stringify(record));
  console.log(JSON.stringify(record));
}

export { log };
