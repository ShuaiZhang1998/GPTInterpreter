package com.zs.project.util;

import com.zs.project.exception.ErrorCode;
import com.zs.project.model.response.BaseResponse;

/**
 * Let the result to Response entry
 */
public class ResultUtils {
    /**
     * successful
     * @param data
     * @return
     * @param <T>
     */
    public static <T>BaseResponse<T> success(T data){
        return new BaseResponse<>(0,data,"ok");
    }

    public static <T>BaseResponse<T> success(T data,String message){
        return new BaseResponse<>(0,data,message);
    }

    /**
     * fault
     * @param errorCode
     * @return
     */

    public static BaseResponse  error(ErrorCode errorCode){
        return new BaseResponse<>(errorCode);
    }

    public static BaseResponse error(int code, String message) {
        return new BaseResponse(code, null, message);
    }


    public static BaseResponse error(ErrorCode errorCode, String message) {
        return new BaseResponse(errorCode.getCode(), null, message);
    }

}
