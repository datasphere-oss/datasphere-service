package com.datasphere.engine.core.utils;


import com.datasphere.engine.common.exception.JIllegalOperationException;

public class JAssert
{
    public static void isTrue(final Boolean b) {
        isTrue(b, "Illegal argument!");
    }
    
    public static void isTrue(final Boolean b, final String msg) {
        if (b == null || !b) {
            throw new JIllegalOperationException(msg);
        }
    }

    public static void isTrue(Boolean b,Integer code, String msg) {
        if(b == null || b == false) {
            throw new JIllegalOperationException(code,msg);
        }
    }
}
