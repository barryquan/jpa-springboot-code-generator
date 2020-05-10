package com.github.barry.akali.base.ex.assertion;

import java.text.MessageFormat;

import com.github.barry.akali.base.ex.BaseException;
import com.github.barry.akali.base.ex.BusinessException;
import com.github.barry.akali.base.ex.IResponseEnum;

/**
 * <p>
 * 业务异常断言
 * </p>
 *
 * @author quansr
 */
public interface BusinessExceptionAssert extends IResponseEnum, Assert {

    @Override
    default BaseException newException(Object... args) {
        String msg = MessageFormat.format(this.getMessage(), args);

        return new BusinessException(this, args, msg);
    }

    @Override
    default BaseException newException(Throwable t, Object... args) {
        String msg = MessageFormat.format(this.getMessage(), args);

        return new BusinessException(this, args, msg, t);
    }

}
