package com.api.freeapi.common;

/***
 *
 * 错误响应枚举类
 * @Author chen
 * @Date  16:38
 * @Param
 * @Return
 * @Since version-11
 */
public enum ErrorCode {


    PARAMS_ERROR(403,"参数不合法"),
    USERNAME_ERROR(400,"用户名不存在"),
    PASSWORD_ERROR(400,"密码错误"),
    KEY_ERROR(403,"key不合法"),
    CODE_ERROR(403,"验证码错误"),
    SERVICE_ERROR(402,"服务超限"),
    FAST_ERROR(403,"访问频繁，请稍后再试"),
    SYSTEM_ERROR(500,"服务异常"),
    DATA_ERROR(501,"数据获取失败"),
    PERMISSION_ERROR(401,"权限不足"),
    USER_ERROR(403,"账户被禁用"),
    ILLEGAl_ERROR(403,"非法操作");


    private int ErrCode;
    private String ErrMsg;


    ErrorCode(int ErrCode , String ErrMsg){
        this.ErrCode = ErrCode;
        this.ErrMsg = ErrMsg;
    }

    public int getErrCode() {
        return ErrCode;
    }

    public void setErrCode(int errCode) {
        ErrCode = errCode;
    }

    public String getErrMsg() {
        return ErrMsg;
    }

    public void setErrMsg(String errMsg) {
        ErrMsg = errMsg;
    }
}