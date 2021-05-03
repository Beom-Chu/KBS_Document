package property;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class PropertiesDemo {
//	private final String FilePath = System.getProperty("user.dir") + "\\application.properties";
	private final String FilePath = "D:\\KBS_Document\\coding\\src\\property\\test.properites";
	private Properties properties;

	public PropertiesDemo() throws IOException {
		properties = new Properties();
		createFile();
	}

	public Properties getProperties() {
		return properties;
	}

	private void loadProperty() throws IOException {
		try(FileInputStream inputStream = new FileInputStream(FilePath)){
			properties.load(new BufferedInputStream(inputStream));
		}
	}

	private void createFile() throws IOException {
		File file = new File(FilePath); // 현재 프로젝트 베이스 경로
		if (!file.exists())
			file.createNewFile();
	}
	
	private void newProperty() throws Exception {
		try(FileOutputStream stream = new FileOutputStream(FilePath)){
			properties.store(stream, "프로퍼티 파일");
		}
	}
	
	private void newPropertyBackup() throws Exception {
		try(FileOutputStream stream = new FileOutputStream(FilePath+"_"+System.currentTimeMillis())){
			properties.store(stream, "프로퍼티 파일");
		}
	}
	
	private void setProperties(List<HashMap<String,String>> listMap) {
		for(HashMap<String,String> map : listMap) {
			properties.setProperty(map.get("key"),map.get("value"));
		}
	}

	public static void main(String[] args) throws Exception {
		
		// 프로퍼티 클래스 생성하며 프로퍼티 파일 생성
		PropertiesDemo propertiesDemo = new PropertiesDemo();
		
		// 프로퍼티 파일 읽기
		propertiesDemo.loadProperty();
		
		// 프로퍼티 객체
		Properties properties = propertiesDemo.getProperties();
		
		// 프로퍼티 파일 백업
		propertiesDemo.newPropertyBackup();
		
		// 프로퍼티 초기화
//		properties.clear();
		
		// 프로퍼티 key-value 설정
		for (int i = 0; i < 10; i++) {
			properties.setProperty("KEY" + i, "VALUE" + Math.round(Math.random()*10));
		}
		
		// 프로퍼티 ListMap 설정
//		List<HashMap<String,String>> listMap = new ArrayList<HashMap<String,String>>();
//		propertiesDemo.setProperties(listMap);
	
		
		properties.list(System.out); // 프로퍼티 값 출력
		
		System.out.println("Key3 포함여부 =>> " + properties.containsKey("KEY3"));
		System.out.println("키값2 포함여부 =>> " + properties.containsValue("키값2"));
		
		String msg = properties.getProperty("KEY3");
		System.out.println("Result =>> " + msg);
		
		// 프로퍼티 파일 생성
		propertiesDemo.newProperty();
		
	}

}