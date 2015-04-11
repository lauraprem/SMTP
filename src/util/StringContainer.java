package util;

public class StringContainer {

	private String string;

	public StringContainer(String s) {
		setString(s);
	}

	public StringContainer() {
		setString("");
	}

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}
}
