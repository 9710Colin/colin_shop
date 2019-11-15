package com.wlkg.exception;

import com.wlkg.enums.ExceptionEnums;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Auther: Colin
 * @Date: 2019/11/14 22:43
 * @Description:
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WlkgException extends RuntimeException {
    private ExceptionEnums exceptionEnums;
}