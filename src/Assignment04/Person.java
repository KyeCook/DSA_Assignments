package Assignment04;

public class Person {
	private final String name;
	private String phoneNumber;
	private int age;
	
	public Person(String n, String pn, int a) {
		name = n;
		setPhoneNumber(pn);
		setAge(a);
	}
	public Person(String n) {
		this(n, "", 0);
	}
	public String getName() {
		return name;
	}
	
	public int hashCode() {
		int total = 0;
		for(char c:name.toCharArray()) {
			total += c;
		}
		return total;
	}
	public String toString() {
		return name;
	}
	public static void main(String[] args) {
		System.out.println("Testing hashcode function..");
		Person p = new Person("ABC", "", 6);
		System.out.println(p.hashCode());
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
}
