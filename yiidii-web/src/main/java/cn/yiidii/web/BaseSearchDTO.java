package cn.yiidii.web;

import lombok.Data;

/**
 * BaseSearchDTO
 *
 * @author ed w
 * @since 1.0
 */
@Data
public abstract class BaseSearchDTO {
    private String keyword;
    private Integer page;
    private Integer size;

    public BaseSearchDTO() {
        this.keyword = "";
        this.page = 1;
        this.size = 30;
    }
}
