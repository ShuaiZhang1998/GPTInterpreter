package com.zs.project.exception;

public class ServiceException extends RuntimeException{
    private final int code;

    public ServiceException(int code,String message) {
        super(message);
        this.code = code;
    }

    public ServiceException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public ServiceException(ErrorCode errorCode,String message){
        super(message);
        this.code = errorCode.getCode();
    }



    public int getCode(){
        return this.code;
    }

}
