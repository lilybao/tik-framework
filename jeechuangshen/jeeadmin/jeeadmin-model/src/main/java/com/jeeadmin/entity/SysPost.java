package com.jeeadmin.entity;

import com.jeerigger.frame.base.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * <p>
 * 岗位表
 * </p>
 *
 * @author wangcy
 * @since 2019-01-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysPost extends BaseModel<SysPost> {

    private static final long serialVersionUID = 1L;


    /**
     * 岗位编码
     */
    @NotNull(message = "岗位编码不能为空！")
    @Size(max = 30, message = "岗位编码长度最大值为30！")
    private String postCode;

    /**
     * 岗位名称
     */
    @NotNull(message = "岗位名称不能为空！")
    @Size(max = 50, message = "岗位名称长度最大值为50！")
    private String postName;

    /**
     * 岗位类型
     */
    private String postType;

    /**
     * 岗位排序
     */
    private Integer postSort;

    /**
     * 岗位状态（0:正常 2:停用）
     */
    @Pattern(regexp = "[02]", message = "岗位状态值必须为0或1或2（0：正常 2：停用）！")
    private String postStatus;

    /**
     * 备注信息
     */
    @Size(max = 150, message = "备注长度最大值为150！")
    private String remarks;

}
