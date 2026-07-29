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

import { SecretStore } from 'fastly:secret-store';

export class SecretStoreManager {
  static instance = null;

  constructor() {
    this.store = null;
    this.secretsMap = null;
    this.secretsMapLoaded = false;
  }

  static getInstance() {
    if (!SecretStoreManager.instance) {
      SecretStoreManager.instance = new SecretStoreManager();
    }
    return SecretStoreManager.instance;
  }

  async getSecret(key) {
    if (!this.store) {
      this.store = new SecretStore('secret_default');
    }

    // Try cloud format first: all secrets bundled as JSON under "secrets" key
    if (!this.secretsMapLoaded) {
      this.secretsMapLoaded = true;
      try {
        const secretsEntry = await this.store.get('secrets');
        if (secretsEntry) {
          this.secretsMap = JSON.parse(secretsEntry.plaintext());
        }
      } catch (e) {
        // Not available or not valid JSON — fall through to individual lookup
        this.secretsMap = null;
      }
    }

    if (this.secretsMap && key in this.secretsMap) {
      return this.secretsMap[key];
    }

    // Fallback: fetch secret individually by key (local development)
    try {
      const secret = await this.store.get(key);
      if (!secret) {
        throw new Error(`Secret '${key}' not found in store`);
      }
      return secret.plaintext();
    } catch (error) {
      console.error(`Failed to load secret '${key}':`, error);
      throw error;
    }
  }

  static async getSecret(key) {
    const instance = SecretStoreManager.getInstance();
    return instance.getSecret(key);
  }
}