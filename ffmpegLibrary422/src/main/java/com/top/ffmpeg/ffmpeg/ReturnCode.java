package com.top.ffmpeg.ffmpeg;

/**
 * @author leo
 * @version 1.0
 * @className ReturnCode
 * @description TODO
 * @date 2022/4/8 15:47
 **/
public class ReturnCode {
    public static int SUCCESS = 0;

    public static int CANCEL = 255;

    private  int value;

    public ReturnCode( int value) {
        this.value = value;
    }

    public static boolean isSuccess(final ReturnCode returnCode) {
        return (returnCode != null && returnCode.getValue() == SUCCESS);
    }

    public static boolean isCancel(final ReturnCode returnCode) {
        return (returnCode != null && returnCode.getValue() == CANCEL);
    }

    public int getValue() {
        return value;
    }

    public boolean isValueSuccess() {
        return (value == SUCCESS);
    }

    public boolean isValueError() {
        return ((value != SUCCESS) && (value != CANCEL));
    }

    public boolean isValueCancel() {
        return (value == CANCEL);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

}
