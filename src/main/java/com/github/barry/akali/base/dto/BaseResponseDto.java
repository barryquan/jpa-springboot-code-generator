package com.github.barry.akali.base.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 响应基础Dto信息<br>
 * 所有的响应类Dto都继承该对象<br>
 * 
 * @author barry
 *
 */
@Setter
@Getter
@ToString(callSuper = true)
public abstract class BaseResponseDto extends BaseDto {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
}
