package models;

import java.io.Serializable;

public class Color implements Serializable{

	private String color1;

	private String color2;

	private String color3;

	public String getColor1() {
		return color1;
	}

	public void setColor1(String color1) {
		this.color1 = color1;
	}

	public String getColor2() {
		return color2;
	}

	public void setColor2(String color2) {
		this.color2 = color2;
	}

	public String getColor3() {
		return color3;
	}

	public void setColor3(String color3) {
		this.color3 = color3;
	}

	@Override
	public String toString() {
		return "Color [color1=" + color1 + ", color2=" + color2 + ", color3=" + color3 + "]";
	}

}