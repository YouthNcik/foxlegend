/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package cn.edu.tju.rico.utils;

/**
 * 常量枚举HTTP状态码。
 * @author lsj
 * @since 4.0
 */
public interface CustomStatus {

    // --- 1xx 信息状态码 ---

    /** {@code 100 继续} (HTTP/1.1 - RFC 2616) */
    public static final int SC_CONTINUE = 100;
    /** {@code 101 交换协议} (HTTP/1.1 - RFC 2616)*/
    public static final int SC_SWITCHING_PROTOCOLS = 101;
    /** {@code 102 处理中} (WebDAV - RFC 2518) */
    public static final int SC_PROCESSING = 102;

    // --- 2xx 请求成功状态码 ---

    /** {@code 200 OK} (HTTP/1.0 - RFC 1945) */
    public static final int SC_OK = 200;
    /** {@code 201 创建} (HTTP/1.0 - RFC 1945) */
    public static final int SC_CREATED = 201;
    /** {@code 202 已接受} (HTTP/1.0 - RFC 1945) */
    public static final int SC_ACCEPTED = 202;
    /** {@code 203 非权威信息} (HTTP/1.1 - RFC 2616) */
    public static final int SC_NON_AUTHORITATIVE_INFORMATION = 203;
    /** {@code 204 无内容} (HTTP/1.0 - RFC 1945) */
    public static final int SC_NO_CONTENT = 204;
    /** {@code 205 内容重置} (HTTP/1.1 - RFC 2616) */
    public static final int SC_RESET_CONTENT = 205;
    /** {@code 206 部分内容} (HTTP/1.1 - RFC 2616) */
    public static final int SC_PARTIAL_CONTENT = 206;
    /**
     * {@code 207 多状态} (WebDAV - RFC 2518)
     * or
     * {@code 207 部分更新ok } (HTTP/1.1 - draft-ietf-http-v11-spec-rev-01?)
     */
    public static final int SC_MULTI_STATUS = 207;

    // --- 3xx Redirection ---

    /** {@code 300 Mutliple Choices} (HTTP/1.1 - RFC 2616) */
    public static final int SC_MULTIPLE_CHOICES = 300;
    /** {@code 301 Moved Permanently} (HTTP/1.0 - RFC 1945) */
    public static final int SC_MOVED_PERMANENTLY = 301;
    /** {@code 302 Moved Temporarily} (Sometimes {@code Found}) (HTTP/1.0 - RFC 1945) */
    public static final int SC_MOVED_TEMPORARILY = 302;
    /** {@code 303 See Other} (HTTP/1.1 - RFC 2616) */
    public static final int SC_SEE_OTHER = 303;
    /** {@code 304 Not Modified} (HTTP/1.0 - RFC 1945) */
    public static final int SC_NOT_MODIFIED = 304;
    /** {@code 305 Use Proxy} (HTTP/1.1 - RFC 2616) */
    public static final int SC_USE_PROXY = 305;
    /** {@code 307 Temporary Redirect} (HTTP/1.1 - RFC 2616) */
    public static final int SC_TEMPORARY_REDIRECT = 307;

    // --- 4xx 客户端响应错误 ---

    /** {@code 400 错误请求} (HTTP/1.1 - RFC 2616) */
    public static final int SC_BAD_REQUEST = 400;
    /** {@code 401 未经授权} (HTTP/1.0 - RFC 1945) */
    public static final int SC_UNAUTHORIZED = 401;
    /** {@code 402 Payment Required} (HTTP/1.1 - RFC 2616) */
    public static final int SC_PAYMENT_REQUIRED = 402;
    /** {@code 403 禁止行为} (HTTP/1.0 - RFC 1945) */
    public static final int SC_FORBIDDEN = 403;
    /** {@code 404 未找到对应结果} (HTTP/1.0 - RFC 1945) */
    public static final int SC_NOT_FOUND = 404;
    /** {@code 405 该行为被禁止} (HTTP/1.1 - RFC 2616) */
    public static final int SC_METHOD_NOT_ALLOWED = 405;
    /** {@code 406 无法接受的请求} (HTTP/1.1 - RFC 2616) */
    public static final int SC_NOT_ACCEPTABLE = 406;
    /** {@code 407 代理身份要求验证} (HTTP/1.1 - RFC 2616)*/
    public static final int SC_PROXY_AUTHENTICATION_REQUIRED = 407;
    /** {@code 408 请求超时} (HTTP/1.1 - RFC 2616) */
    public static final int SC_REQUEST_TIMEOUT = 408;
    /** {@code 409 请求冲突} (HTTP/1.1 - RFC 2616) */
    public static final int SC_CONFLICT = 409;
    /** {@code 410 请求已经完成} (HTTP/1.1 - RFC 2616) */
    public static final int SC_GONE = 410;
    /** {@code 411 长度要求不合法} (HTTP/1.1 - RFC 2616) */
    public static final int SC_LENGTH_REQUIRED = 411;
    /** {@code 412 前置条件失败} (HTTP/1.1 - RFC 2616) */
    public static final int SC_PRECONDITION_FAILED = 412;
    /** {@code 413 请求实体太大} (HTTP/1.1 - RFC 2616) */
    public static final int SC_REQUEST_TOO_LONG = 413;
    /** {@code 414 URI过长} (HTTP/1.1 - RFC 2616) */
    public static final int SC_REQUEST_URI_TOO_LONG = 414;
    /** {@code 415 不支持的媒体类型} (HTTP/1.1 - RFC 2616) */
    public static final int SC_UNSUPPORTED_MEDIA_TYPE = 415;
    /** {@code 416 请求超出限制} (HTTP/1.1 - RFC 2616) */
    public static final int SC_REQUESTED_RANGE_NOT_SATISFIABLE = 416;
    /** {@code 417 未达到预期结果} (HTTP/1.1 - RFC 2616) */
    public static final int SC_EXPECTATION_FAILED = 417;

    /**
     *  常量418 错误
     * {@code 418 Unprocessable实体} (WebDAV drafts?)
     * or {@code 418 Reauthentication Required} (HTTP/1.1 drafts?)
     */
    // 未使用
    // public static final int SC_UNPROCESSABLE_ENTITY = 418;

    /**
     * Static constant for a 419 error.
     * {@code 419 空间资源不足}
     * (WebDAV - draft-ietf-webdav-protocol-05?)
     * or {@code 419 代理Reauthentication所需}
     * (HTTP/1.1 drafts?)
     */
    public static final int SC_INSUFFICIENT_SPACE_ON_RESOURCE = 419;
    /**
     * Static constant for a 420 error.
     * {@code 420 方法失败}
     * (WebDAV - draft-ietf-webdav-protocol-05?)
     */
    public static final int SC_METHOD_FAILURE = 420;
    /** {@code 422 Unprocessable实体} (WebDAV - RFC 2518) */
    public static final int SC_UNPROCESSABLE_ENTITY = 422;
    /** {@code 423 被锁定} (WebDAV - RFC 2518) */
    public static final int SC_LOCKED = 423;
    /** {@code 424 依赖失败} (WebDAV - RFC 2518) */
    public static final int SC_FAILED_DEPENDENCY = 424;

    // --- 5xx 服务器错误 ---

    /** {@code 500 服务器内容错误 } (HTTP/1.0 - RFC 1945) */
    public static final int SC_INTERNAL_SERVER_ERROR = 500;
    /** {@code 501 不可执行 } (HTTP/1.0 - RFC 1945) */
    public static final int SC_NOT_IMPLEMENTED = 501;
    /** {@code 502 无效网关 } (HTTP/1.0 - RFC 1945) */
    public static final int SC_BAD_GATEWAY = 502;
    /** {@code 503 服务无效 } (HTTP/1.0 - RFC 1945) */
    public static final int SC_SERVICE_UNAVAILABLE = 503;
    /** {@code 504 网关超时 } (HTTP/1.1 - RFC 2616) */
    public static final int SC_GATEWAY_TIMEOUT = 504;
    /** {@code 505 HTTP 版本不支持 } (HTTP/1.1 - RFC 2616) */
    public static final int SC_HTTP_VERSION_NOT_SUPPORTED = 505;

    /** {@code 507 存储不足} (WebDAV - RFC 2518) */
    public static final int SC_INSUFFICIENT_STORAGE = 507;

}
