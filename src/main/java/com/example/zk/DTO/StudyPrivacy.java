package com.example.zk.DTO;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @Author：Zuochao（Edward）Dou
 * @Date: 05/01/2019
 */

public enum StudyPrivacy {
    /**
     * 公开可见类型
     */
    PUBLIC("Public"),
    /**
     * 私有类型
     */
    PRIVATE("Private");

    private String privacy;

    StudyPrivacy(String privacy) {
        this.privacy = privacy;
    }

    @JsonValue
    public String getPrivacy() {
        return this.privacy;
    }

    @Override
    public String toString() {
        return this.privacy;
    }
}
