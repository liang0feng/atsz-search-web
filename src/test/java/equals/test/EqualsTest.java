package equals.test;

import java.util.HashMap;

import org.junit.Test;

public class EqualsTest {

	@Test
	public void test(){
		String str1 = new String("1");
		String str2 = "1";
		System.out.println(str1==str2);
		
		HashMap<String,String> hashMap = new HashMap<String,String>();
		
		hashMap.put("key", "1");
		String str3 = hashMap.get("key").toString();
		System.out.println(str2 == str3);
	}
	
	@Test
	public void test01(){
		HashMap<String,String> map = new HashMap<String, String>();
		map.put("key", "1");
		String str1 = "1";
		System.out.println(str1 == map.get("key").toString());
	}
}
