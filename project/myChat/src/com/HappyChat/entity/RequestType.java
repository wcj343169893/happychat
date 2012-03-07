package com.HappyChat.entity;

import java.io.Serializable;


public enum RequestType implements Serializable {
    login,//登录
    register,//注册
    sendMessage,//发送信息
    exit,//退出
    ti//被踢
}
