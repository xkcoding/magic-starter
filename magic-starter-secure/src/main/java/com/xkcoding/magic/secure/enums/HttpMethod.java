/*
 * Copyright (c) 2019-2029, xkcoding & Yangkai.Shen & 沈扬凯 (237497819@qq.com & xkcoding.com).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xkcoding.magic.secure.enums;

/**
 * <p>
 * 扩展 {@link org.springframework.http.HttpMethod}
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/9/19 11:30
 */
public enum HttpMethod {
    /**
     * GET 方法
     */
    GET,
    /**
     * HEAD 方法
     */
    HEAD,
    /**
     * POST 方法
     */
    POST,
    /**
     * PUT 方法
     */
    PUT,
    /**
     * PATCH 方法
     */
    PATCH,
    /**
     * DELETE 方法
     */
    DELETE,
    /**
     * OPTIONS 方法
     */
    OPTIONS,
    /**
     * TRACE 方法
     */
    TRACE,
    /**
     * ANY 方法
     */
    ANY;
}
