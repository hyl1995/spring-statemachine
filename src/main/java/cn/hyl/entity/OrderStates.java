package cn.hyl.entity;

public enum OrderStates {
	UNPAID(0, "待支付"),

	WAIT_TAKE(1, "待接单"),
	SELECT_PICK_TYPE(20, "选择提货方式"),

	DELIVERING(3, "配送中"), WAIT_PICK_UP(8, "待自提"),

	FINISH(4, "已完成"),
	REFUNDED(5, "退款"),
	CLOSED(6, "已关闭");


	private Integer code;
	private String name;

	OrderStates(int code, String name) {
		this.code = code;
		this.name = name;
	}

	public Integer getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public static String getName(Integer code) {
		OrderStates[] orderStatusEnums = values();
		for (OrderStates carTypeEnum : orderStatusEnums) {
			if (carTypeEnum.getCode().equals(code)) {
				return carTypeEnum.getName();
			}
		}
		return null;
	}

	public static Integer getCode(String name) {
		OrderStates[] orderStatusEnums = values();
		for (OrderStates orderStatusEnum : orderStatusEnums) {
			if (orderStatusEnum.getName().equals(name)) {
				return orderStatusEnum.getCode();
			}
		}
		return null;
	}


	public static OrderStates getState(Integer code) {
		OrderStates[] orderStates = values();
		for (OrderStates orderState : orderStates) {
			if (orderState.getCode().equals(code)) {
				return orderState;
			}
		}
		return null;
	}
}