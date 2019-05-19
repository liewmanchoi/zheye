package com.liewmanchoi.zheye.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author wangsheng
 * @date 2019/5/16
 * status: 0有效，1无效
 */
@Getter
@Setter
public class LoginTicket {
    private int id;
    private int userId;
    private Date expired;
    private int status;
    private String ticket;
}
