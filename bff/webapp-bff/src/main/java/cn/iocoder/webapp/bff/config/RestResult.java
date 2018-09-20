package cn.iocoder.webapp.bff.config;

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

}
