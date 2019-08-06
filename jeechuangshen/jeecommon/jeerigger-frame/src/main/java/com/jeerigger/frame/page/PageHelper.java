package com.jeerigger.frame.page;

import lombok.Data;

@Data
public class PageHelper<T> {
    /**
     * 当前页数
     */
    private int current = 0;
    /**
     * 每页记录数
     */
    private int size = 20;
    /**
     * 查询条件
     */
    private T data;
    /**
     * 设置每页记录数默认值
     *
     * @return
     */
    public int getSize() {
        if (size == 0) {
            return 20;
        } else {
            return size;
        }
    }
}
