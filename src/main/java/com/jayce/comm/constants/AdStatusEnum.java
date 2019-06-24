package com.jayce.comm.constants;

public enum AdStatusEnum {
	PAUSE("zt", "暂停"), START("tfz", "投放中"), ARREARS("yebz", "余额不足");

	private String key;
	private String value;

	AdStatusEnum(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
