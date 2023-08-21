package com.zs.project.model.dto.page;

import com.zs.project.constant.Database;
import lombok.Data;


/**
 * @author 鱼皮
 * 支持分页时，用需要分页的DTO对象继承该类
 * 并使用@EqualsAndHashCode(callSuper = true)
 * 该类不可以单独使用，必须被继承，子类必须实现序列化接口
 */

@Data
public class PageRequest{

    /**
     * 当前页号
     */
    private long current = 1;

    /**
     * 页面大小
     */
    private long pageSize = 10;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序顺序（默认升序）
     */
    private String sortOrder = Database.SORT_ORDER_ASC;
}