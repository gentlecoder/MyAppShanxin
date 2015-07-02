package cn.dyc.myshanxin.common;

public class User implements java.io.Serializable{
	String operation;
	String username;
	String password;
	String sex;
	String hobby1;
	String hobby2;
	String hobby3;
	String city;
	String time;
	
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getusername() {
		return username;
	}
	public void setusername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String gethobby1() {
		return hobby1;
	}
	public void sethobby1(String hobby1) {
		this.hobby1 = hobby1;
	}
	public String gethobby2() {
		return hobby2;
	}
	public void sethobby2(String hobby2) {
		this.hobby2 = hobby2;
	}
	public String gethobby3() {
		return hobby3;
	}
	public void sethobby3(String hobby3) {
		this.hobby3 = hobby3;
	}
	public String getcity() {
		return city;
	}
	public void setcity(String city) {
		this.city = city;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	
}
