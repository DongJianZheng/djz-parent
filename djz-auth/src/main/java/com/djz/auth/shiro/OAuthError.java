package com.djz.auth.shiro;

public class OAuthError {

	public static final String OAUTH_ERROR = "error";
	public static final String OAUTH_ERROR_DESCRIPTION = "error_description";
	public static final String OAUTH_ERROR_URI = "error_uri";

	public OAuthError() {
	}

	public static final class ResourceResponse {
		public static final String INVALID_REQUEST = "invalid_request";
		public static final String EXPIRED_TOKEN = "expired_token";
		public static final String INSUFFICIENT_SCOPE = "insufficient_scope";
		public static final String INVALID_ACCESS_TOKEN = "60006"; // 不合法access_token

		public ResourceResponse() {
		}
	}

	public static final class TokenResponse {
		public static final String INVALID_CLIENT = "60000"; // 不合法客户端ID（client_id ）
		public static final String UNAUTHORIZED_CLIENT = "60001"; // 未授权客户端 （client_secret）
		public static final String INVALID_REQUEST = "60002"; //
		public static final String INVALID_SCOPE = "60003"; // 不合法的授权范围 （scope）
		public static final String INVALID_USER_PASSWORD = "60004"; // 用户名、或密码错误
		public static final String UNSUPPORTED_GRANT_TYPE = "60005"; // 不合法的grant_type
		public static final String INVALID_REFRESH_TOKEN = "60007"; // 不合法refresh_token
		public static final String ACCOUNT_LOCKED = "60008"; // 账号被锁

		public TokenResponse() {
		}
	}

	public static final class CodeResponse {
		public static final String INVALID_REQUEST = "invalid_request"; // 不合法的请求
		public static final String UNAUTHORIZED_CLIENT = "unauthorized_client";
		public static final String ACCESS_DENIED = "access_denied";
		public static final String UNSUPPORTED_RESPONSE_TYPE = "unsupported_response_type";
		public static final String INVALID_SCOPE = "60002"; // 不合法的授权范围 （scope）
		public static final String SERVER_ERROR = "server_error";
		public static final String TEMPORARILY_UNAVAILABLE = "temporarily_unavailable";

		public CodeResponse() {
		}
	}
}
