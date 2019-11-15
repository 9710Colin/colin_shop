package com.wlkg.vo;

import com.wlkg.enums.ExceptionEnums;
import lombok.Data;

/**
 * @Auther: Colin
 * @Date: 2019/11/14 22:45
 * @Description:
 */
@Data
public class ExceptionResult {
    private int status;
    private String message;
    private Long timestamp;

    public ExceptionResult(ExceptionEnums exceptionEnums){
        this.status = exceptionEnums.getCode();
        this.message = exceptionEnums.getMsg();
        this.timestamp = System.currentTimeMillis();
    }
}
