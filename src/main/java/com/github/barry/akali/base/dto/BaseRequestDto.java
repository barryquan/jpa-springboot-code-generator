package com.github.barry.akali.base.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 请求基础Dto信息<br>
 * 所有的请求类Dto都继承该对象<br>
 * 
 * @author barry
 *
 */
@Setter
@Getter
public abstract class BaseRequestDto extends BaseDto {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
}
