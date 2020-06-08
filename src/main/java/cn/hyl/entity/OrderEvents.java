package cn.hyl.entity;
public enum OrderEvents {
	CREATE,//订单创建
	PAY, // 支付
	SHIP, // 发货
	LOGISTICS_DISPATCH, // 物流派送
	RECEIVE, // 确认收货
	REFUND, // 退款
	CANCEL // 取消订单
}
