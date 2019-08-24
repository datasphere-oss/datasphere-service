package com.datasphere.engine.core.validator;

public class ErrorMsgBean
{
    StringBuffer sb;
    int count;
    
    public ErrorMsgBean() {
        this.sb = new StringBuffer();
        this.count = 0;
    }
    
    public void append(final String msg) {
        this.sb.append(String.valueOf(++this.count) + ".");
        this.sb.append(msg);
        this.sb.append("\n");
    }
    
    public String toString() {
        return this.sb.toString();
    }
    
    public boolean hasMessage() {
        return this.count != 0;
    }
}
