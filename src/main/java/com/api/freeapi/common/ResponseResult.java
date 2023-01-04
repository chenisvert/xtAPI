package com.api.freeapi.common;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.val;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
 
/**
 * 统一应答对象
 *
 * @author zhaohui
 */
@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ResponseResult<T> implements Serializable {
 
	public static final Integer ERROR_CODE_SUCCESS = 200;

	@JsonProperty("code")
	private Integer errorCode;
 
	@JsonProperty("msg")
	private String errorMessage;
 
	@JsonProperty("data")
	private T data;
 
	@JsonIgnore
	private final Map<String, Object> attachments = new HashMap<>();
 
	/**
	 * 直接返回成功
	 *
	 * @return 应答对象
	 */
	public static ResponseResult<Void> success() {
		val result = new ResponseResult<Void>();
		result.setErrorCode(ERROR_CODE_SUCCESS);
		result.setErrorMessage("");
		return result;
	}
 
	/**
	 * 返回成功，并携带一个应答数据对象
	 *
	 * @param data 应答数据对象
	 * @param <T>  数据对象泛型
	 * @return 应答对象
	 */
	public static <T> ResponseResult<T> success(T data) {
		val result = new ResponseResult<T>();
		result.setErrorCode(ERROR_CODE_SUCCESS);
		result.setErrorMessage("");
 
		return result.setData(data);
	}
 
	/**
	 * 返回成功，携带一个应答数据对象，并允许扩展额外的应答扩展状态
	 *
	 * @param data    应答数据对象
	 * @param attachments 应答扩展状态
	 * @param <T>     数据对象泛型
	 * @return 应答对象
	 */
	public static <T> ResponseResult<T> success(T data, Map<String, Object> attachments) {
		val result = success(data);
		result.getAttachments().putAll(attachments);
 
		return result;
	}
 
	/**
	 * 返回失败，携带错误码和错误消息
	 *
	 * @param code    错误码
	 * @param message 消息
	 * @return 应答对象
	 */
	public static ResponseResult<Void> error(@NonNull Integer code, @NonNull String message) {
		val result = new ResponseResult<Void>();
		result.setErrorCode(code);
		result.setErrorMessage(message);
 
		return result;
	}
 
	public static ResponseResult<Void> error(@NonNull Integer code, @NonNull String message, Map<String, Object> attachments) {
		val result = error(code, message);
		result.getAttachments().putAll(attachments);
		return result;
	}
 
	@JsonAnySetter
	public void setAttachment(String key, String value) {
		attachments.put(key, value);
	}
 
	@JsonAnyGetter
	public Map<String, Object> getAttachments() {
		return attachments;
	}
}