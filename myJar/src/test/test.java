package test;

import java.sql.SQLException;
import java.util.ArrayList;

import mine.allen.util.sql.JdbcContextL;

public class test {

	public static void main(String[] args) {
		ArrayList<String> al = new ArrayList<String>();
		al.add("0");
		al.add("1");
		al.add("2");
		al.add("3");
		System.out.println(al.get(1));
		System.out.println(al.get(3));
		al.remove(1);
		System.out.println(al.get(1));
		System.out.println(al.get(3));
	}

}
