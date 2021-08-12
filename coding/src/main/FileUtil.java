package main;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.util.UUID;
import javax.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.multipart.MultipartFile;

public class FileUtil {

  /**
   * 파일 업로드
   * 
   * @param file : 파일
   * @param filePath : 파일경로
   * @return fileUniqueName : 업로드 실패시 null
   */
  static public String uploadFile(MultipartFile file, String filePath) {

    String originFilename = file.getOriginalFilename();
    String extName = originFilename.substring(originFilename.lastIndexOf("."), originFilename.length());
    String fileUniqueName = UUID.randomUUID() + extName;

    try {

      File fileSaveDir = new File(filePath);

      // 파일 경로 없으면 생성
      if (!fileSaveDir.exists()) {
        fileSaveDir.mkdirs();
      }

      // 디렉토리 권한
      fileSaveDir.setReadable(true);
      fileSaveDir.setWritable(true);
      fileSaveDir.setExecutable(true);

      file.transferTo(new File(filePath, fileUniqueName));

    } catch (Exception e) {
      e.printStackTrace();
      fileUniqueName = null;
    }

    return fileUniqueName;
  }

  /**
   * 파일 다운로드
   * 
   * @param fileName : 파일명
   * @param filePath : 파일경로
   * @param fileUniqueName : 유니크파일명
   * @param response
   * @return boolean 다운 성공여부
   */
  static public boolean downloadFile(String fileName, String filePath, String fileUniqueName, HttpServletResponse response) {

    boolean success = true;

    if (filePath == null || fileUniqueName == null) {
      return false;
    }

    if (fileName == null) {
      fileName = "noname";
    }

    final File fileToDownload = new File(filePath, fileUniqueName);

    try (FileInputStream fis = new FileInputStream(fileToDownload);) {

      // 한글깨짐 방지 , 공백이 '+'로 변경 방지
      String encodeFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");

      response.setContentType("application/octet-stream; charset=utf-8");
      response.setHeader("Content-Transfer-Encoding", "binary");
      response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + encodeFileName);

      IOUtils.copy(fis, response.getOutputStream());
      response.flushBuffer();

    } catch (Exception e) {

      e.printStackTrace();
      success = false;
    }

    return success;
  }

  /**
   * 파일 삭제
   * 
   * @param filePath : 파일경로
   * @param fileUniqueName : 유니크파일명
   * @return
   */
  static public Boolean deleteFile(String filePath, String fileUniqueName) {

    boolean success = true;

    if (filePath == null || fileUniqueName == null) {
      return false;
    }

    try {
      File file = new File(filePath, fileUniqueName);

      if (file.exists()) {
        file.delete();

      } else {
        // 파일이 존재하지 않음
        success = false;
      }

    } catch (Exception e) {
      e.printStackTrace();
      success = false;
    }

    return success;
  }
}
