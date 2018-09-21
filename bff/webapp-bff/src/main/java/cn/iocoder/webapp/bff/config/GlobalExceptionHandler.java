package cn.iocoder.webapp.bff.config;

import cn.iocoder.occeans.core.exception.ServiceException;
import cn.iocoder.webapp.bff.vo.RestResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    // TODO 芋艿，其他类型的异常。这里匹配是有问题的
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public RestResult serviceExceptionHandler(HttpServletRequest req, Exception e) {
        ServiceException ex = (ServiceException) e;
        return RestResult.error(ex.getCode(), ex.getMessage());
    }

}
