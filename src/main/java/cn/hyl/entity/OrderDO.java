package cn.hyl.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 商品订单表
 */
@Data
public class OrderDO implements Serializable {
    private Long id;

    /**
     * 订单状态（0：未支付 1：取消支付 2：支付成功 3：已发货 4：完成  5：已退款）
     */
    private Integer status;

}