package com.mmall.common;

/**
 * @author: whua
 * @create: 2019/04/26 17:26
 */
public enum ResponseCode {

    //显式地列出枚举实例(枚举值)，系统会自动添加 public static final 修饰
    SUCCESS(0, "SUCCESS"),
    ERROR(1, "ERROR"),
    NEED_LOGIN(10, "NEED_LOGIN"),
    ILLEGAL_ARGUMENT(2, "ILLEGAL_ARGUMENT");

    private final int code;
    private final String desc;

    // 定义一个带参数的构造器，枚举类的构造器只能使用 private 修饰
    ResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
