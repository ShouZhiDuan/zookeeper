package com.example.zk.DTO;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Auther: ShouZhi@Duan
 * @Description:
 */
@ToString
public class ZKDTO implements Serializable {

    private static final long serialVersionUID = 362498820763181265L;

    private String name = "shouzhi";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
