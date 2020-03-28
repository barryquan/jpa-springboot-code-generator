package com.github.barry.akali.base.advice;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.github.barry.akali.base.dto.ResponseDto;
import com.github.barry.akali.base.utils.IConstants;

import lombok.extern.slf4j.Slf4j;

/***
 * 统一封装异常、统一处理出参
 * 
 * @author barry
 *
 */
@Slf4j
@RestControllerAdvice
public class RestExceptionHandler implements ResponseBodyAdvice<Object> {

    @Value("${spring.application.name:}")
    private String serviceName;

    @ExceptionHandler(value = Exception.class)
    public ResponseDto<?> resolveException(HttpServletRequest request, HttpServletResponse response, Exception e) {
        log.error("调用={}服务出现异常了，请求的url是={}，请求的方法是={}，原因={}", serviceName, request.getRequestURL(), request.getMethod(),
                e);
        int errorCode = 500; // 默认错误码都是500
        if (e instanceof NullPointerException) {
            errorCode = 400; // 空指针大都是由请求参数造成的，返回400错误
        }
        // else if (e instanceof AccessDeniedException) {
        // errorCode = 403; // spring security 安全校验不通过
        // }
        return ResponseDto.error(errorCode, IConstants.MSG_ERROR);
    }

    /***
     * 统一封装返回值，如果返回值是void,自动构造请求成功的返回值<br>
     * 如果返回值本身是ResponseDto，不做处理<br>
     * 其他的统一做添加请求成功的返回码。
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType mediaType,
            Class<? extends HttpMessageConverter<?>> arg3, ServerHttpRequest arg4, ServerHttpResponse arg5) {
        // 1.获取返回参数的全类名，如：com.rong.entity.User
        final String returnTypeName = returnType.getParameterType().getName();

        if (Objects.equals("void", returnTypeName)) {
            return ResponseDto.success(null);
        }
        if (body instanceof ResponseDto || Objects.equals(ResponseDto.class.getName(), returnTypeName)) {
            return body;
        }

        return ResponseDto.success(body);
    }

    /**
     * 是否支持做的统一返回值处理，<br>
     * 如果返回false，将不会进入beforeBodyWrite方法
     */
    @Override
    public boolean supports(MethodParameter arg0, Class<? extends HttpMessageConverter<?>> arg1) {
        return true;
    }
}
