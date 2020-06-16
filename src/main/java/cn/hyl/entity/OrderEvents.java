package cn.hyl.entity;
public enum OrderEvents {
	CREATE,//订单创建
	PAY, // 支付

	TAKE, // 接单

	ARRIVED, // 已送达

	PICK_UP_FINISH, // 自提完毕

	REFUND, // 退款
	CANCEL // 取消订单
}
