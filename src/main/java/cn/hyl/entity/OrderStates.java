package cn.hyl.entity;

public enum OrderStates {
	UNPAID, // 待支付
	WAITING_FOR_RECEIVE, // 待收货
	SHIPPING, // 发货中
	SHIPPED, // 已发货
	FINISH, // 完成
	REFUNDED, // 已退款
	CLOSED // 已关闭
	;

	public static OrderStates getState(Integer code) {
		OrderStates[] orderStates = values();
		for (OrderStates orderState : orderStates) {
			if (orderState.ordinal() == code) {
				return orderState;
			}
		}
		return null;
	}
}