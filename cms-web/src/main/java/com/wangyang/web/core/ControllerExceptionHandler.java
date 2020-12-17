package com.wangyang.web.core;

import com.wangyang.common.exception.CmsException;
import com.wangyang.common.utils.ExceptionUtils;
import com.wangyang.common.utils.ValidationUtils;
import com.wangyang.common.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.Assert;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@RestControllerAdvice({"com.wangyang"})
public class ControllerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BaseResponse<Map<String, String>> baseResponse = handleBaseException(e);
        baseResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        baseResponse.setMessage("字段验证错误，请完善后重试！");
        Map<String, String> errMap = ValidationUtils.mapWithFieldError(e.getBindingResult().getFieldErrors());
        baseResponse.setData(errMap);
        return baseResponse;
    }
    @ExceptionHandler(CmsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse cmsException(CmsException e) {
        BaseResponse<Map<String, String>> baseResponse = handleBaseException(e);
        baseResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        return baseResponse;
    }

//    @ExceptionHandler(InternalAuthenticationServiceException.class)
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    public BaseResponse userException(InternalAuthenticationServiceException e) {
//        BaseResponse<Map<String, String>> baseResponse = handleBaseException(e);
//        baseResponse.setStatus(HttpStatus.BAD_REQUEST.value());
//        return baseResponse;
//    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse httpMessageNotReadableException(HttpMessageNotReadableException e) {
        BaseResponse<Map<String, String>> baseResponse = handleBaseException(e);
        baseResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        return baseResponse;
    }

    // TODO 针对动态页面错误的处理；
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse handleGlobalException(Exception e) {
        BaseResponse baseResponse = handleBaseException(e);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        baseResponse.setStatus(status.value());
        baseResponse.setMessage(e.getMessage());
        return baseResponse;
    }
    private <T> BaseResponse<T> handleBaseException(Throwable t) {
        Assert.notNull(t, "Throwable must not be null");

        log.error("Captured an exception", t);

        BaseResponse<T> baseResponse = new BaseResponse<>();
        baseResponse.setMessage(t.getMessage());

        if (log.isDebugEnabled()) {
            baseResponse.setDevMessage(ExceptionUtils.getStackTrace(t));
        }

        return baseResponse;
    }
}
