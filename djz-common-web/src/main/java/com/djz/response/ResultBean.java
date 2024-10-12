package com.djz.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ResultBean<T> implements Serializable {

    private static final long serialVersionUID = 3855355879003271193L;

    /**
     * 响应码
     */
    @Builder.Default
    private Integer code = 200;

    /**
     * 响应消息
     */
    @Builder.Default
    private String message = "处理成功";

    /**
     * 数据
     */
    private T data;

    /**
     * 是否成功
     * @return boolean
     */
    public boolean isSuccess() {
        return ResultEnum.SUCCESS.getCode() == code;
    }
}
