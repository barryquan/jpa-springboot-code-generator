package com.github.barry.akali.base.ex.assertion;

import java.text.MessageFormat;

import com.github.barry.akali.base.ex.ArgumentException;
import com.github.barry.akali.base.ex.BaseException;
import com.github.barry.akali.base.ex.IResponseEnum;

/**
 * <pre>
 *
 * </pre>
 *
 * @author quansr
 */
public interface ArgumentExceptionAssert extends IResponseEnum, Assert {

    @Override
    default BaseException newException(Object... args) {
        String msg = MessageFormat.format(this.getMessage(), args);

        return new ArgumentException(this, args, msg);
    }

    @Override
    default BaseException newException(Throwable t, Object... args) {
        String msg = MessageFormat.format(this.getMessage(), args);

        return new ArgumentException(this, args, msg, t);
    }

}
