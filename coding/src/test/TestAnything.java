package test;


import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.StringJoiner;

class TestAnything {
	
	public static void main(String[] args) {

//	  String key = "test/test2/test5.txt";
////	  String key = "test2.txt";
////	  String fileName = key.substring(key.lastIndexOf("/")+1);
//	  String fileName = key.substring(0,key.length()-1);
//	  
//	  Path p = Paths.get(key);
//	  System.out.println(p.getRoot());
//	  System.out.println(p.getParent());
////	  System.out.println(p.getParent().getParent());
////	  System.out.println(p.getParent().getParent().getParent());
//	  System.out.println(p.getFileName());
//	  System.out.println(p.getNameCount());
//	  System.out.println(p.getParent().getNameCount());
	  
	  
//	  StringJoiner sj = new StringJoiner("/", "", "/");
//	  System.out.println(sj);
//	  sj.add("test1");
//	  System.out.println(sj);
//	  sj.add("test2");
//    System.out.println(sj);
//    sj.add("test3");
//    System.out.println(sj);
    
    String[] split = "test/test2/test3/".split("/");
    
    for (String string : split) {
      System.out.println("[[["+string);
    }
	}
}

