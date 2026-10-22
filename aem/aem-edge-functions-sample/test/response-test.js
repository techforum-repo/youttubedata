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

import assert from 'assert';
import * as response from '../src/lib/response.js';

describe('Test response functions', function () {
    describe('response.notFound', function () {
        it('should return 404', function () {
            const resp = response.notFound();
            assert.equal(resp.status, 404);
        });
    });
    describe('response.error', function () {
        it('should return 503', function () {
            const resp = response.error();
            assert.equal(resp.status, 500);
        });
    });
});
