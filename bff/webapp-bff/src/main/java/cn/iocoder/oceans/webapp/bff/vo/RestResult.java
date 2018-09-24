package cn.iocoder.oceans.webapp.bff.vo;

import lombok.Data;

@Data
public class RestResult {

    private Integer code;
    private String message;
    private Object data;

    public static RestResult error(Integer code, String message) {
        RestResult result = new RestResult();
        result.code = code;
        result.message = message;
        return result;
    }

    public static RestResult ok(Object data) {
        RestResult result = new RestResult();
        result.code = 0;
        result.data = data;
        return result;
    }

}