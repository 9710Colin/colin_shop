package com.wlkg.advice;

import com.wlkg.enums.ExceptionEnums;
import com.wlkg.exception.WlkgException;
import com.wlkg.vo.ExceptionResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @Auther: Colin
 * @Date: 2019/11/14 22:42
 * @Description:
 */
@ControllerAdvice//控制层的一个通知 加上这个异常之后 这个类就相当于是个通用异常处理类了
public class CommonExceptionHandler {
    //该注解表示处理异常的类型，如果抛出WlkgException异常类型，我这个类就能处理
    @ExceptionHandler(WlkgException.class)
    public ResponseEntity<ExceptionResult> handleException(WlkgException e){
        ExceptionEnums exceptionEnums = e.getExceptionEnums();
        ExceptionResult result = new ExceptionResult(exceptionEnums);
        //我们暂定返回状态码为200， 然后从异常中获取友好提示信息
        // 如何拿到状态码：1、先获得枚举对象
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
