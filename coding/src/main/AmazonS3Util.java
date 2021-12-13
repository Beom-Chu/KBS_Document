package main;

import static com.sk.ontology.coutil.FileUtil.filterByFileName;
import static com.sk.ontology.coutil.FileUtil.makeDir;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.UUID;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.sk.ontology.coutil.dto.S3FileDTO;

@Component
public class AmazonS3Util {

  Logger logger = LoggerFactory.getLogger(this.getClass());
  
  @Autowired
  private AmazonS3Client amazonS3Client;

  @Value("${cloud.aws.s3.bucket}")
  public String bucket; // S3 버킷 이름
  
  static final String ROOT_PATH = System.getProperty("user.dir");

  // S3로 파일 업로드하기
  public String upload(MultipartFile multipartFile, String filePath) throws IOException {
    
    File uploadFile = convert(multipartFile) // 파일 변환할 수 없으면 에러
        .orElseThrow(() -> {throw new IllegalArgumentException("error: MultipartFile -> File convert fail");});

    String fileName = filePath + "/" + UUID.randomUUID() + uploadFile.getName(); // S3에 저장된 파일 이름
    String uploadUrl = "";
    
    try {
      uploadUrl = putS3(uploadFile, fileName); // s3로 업로드
    }finally {
      //임시파일 삭제
      removeNewFile(uploadFile);
    }
    return uploadUrl;
  }


  // S3로 업로드
  private String putS3(File uploadFile, String fileName) {
    amazonS3Client
        .putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
    return amazonS3Client.getUrl(bucket, fileName).toString();
  }

  // 로컬에 저장된 임시 파일 지우기
  private void removeNewFile(File targetFile) {
    if (targetFile.delete()) {
      logger.info("File delete success");
      return;
    }
    logger.info("File delete fail");
  }

  // 로컬에 임시 파일 생성 하기
  private Optional<File> convert(MultipartFile file) throws IOException {
    File convertFile = new File(ROOT_PATH + File.separatorChar + UUID.randomUUID() + file.getOriginalFilename());
    if (convertFile.createNewFile()) { // 바로 위에서 지정한 경로에 File이 생성됨 (경로가 잘못되었다면 생성 불가능)
      try (FileOutputStream fos = new FileOutputStream(convertFile)) { // FileOutputStream 데이터를 파일에 바이트 스트림으로 저장하기 위함
        fos.write(file.getBytes());
      }
      return Optional.of(convertFile);
    }

    return Optional.empty();
  }
  
  
  /**
   * 디렉토리, 파일 목록 조회
   */
  public List<S3FileDTO> getS3FileList() {

    ListObjectsV2Result listObjects = amazonS3Client.listObjectsV2(bucket);

    List<S3FileDTO> result = new ArrayList<>();

    for (S3ObjectSummary objectSummary : listObjects.getObjectSummaries()) {
      
      String key = objectSummary.getKey();
      Long size = objectSummary.getSize();
      String[] paths = key.split("/");
      
      //최상위 여부
      if (paths.length == 1) {
        result.add(new S3FileDTO(key, size));
      } else {
        
        StringJoiner sj = new StringJoiner("/", "", "/");
        S3FileDTO parent = new S3FileDTO();

        //상위 디렉토리 찾아서 등록
        for (int i = 0; i < paths.length - 1; i++) {
          sj.add(paths[i]);
          if (i == 0) {
            parent = result.stream().filter(o -> o.getKey().equals(sj.toString())).toList().get(0);
          } else {
            parent = parent.getChildren().stream().filter(o -> o.getKey().equals(sj.toString())).toList().get(0);
          }
        }
        parent.addChildren(new S3FileDTO(key, size));
      }
    }

    return result;
  }
  
  
  /**
   * 클라우드 파일을 서버로 다운로드
   * @param key : 클라우드 파일 key
   * @param filePath : 서버 다운로드 경로
   */
  public String copyFileFromCloudToServer(String key, String filePath) {
    
    S3Object object = amazonS3Client.getObject(bucket, key);
    
    String extName = FileUtil.getExtName(key);
    String fileUniqueName = UUID.randomUUID() + extName;
    String fileFullPath = filePath + File.separatorChar +  fileUniqueName;
    
    //폴더 생성
    makeDir(filePath);
    
    FileOutputStream fos = null;
    
    try {
      fos = new FileOutputStream(new File(filterByFileName(fileFullPath)));
      IOUtils.copy(object.getObjectContent(), fos);
      
    } catch (FileNotFoundException e) {
      logger.info("FileNotFoundException 발생");
    } catch (IOException e) {
      logger.info("IOException 발생");
    } finally {
      try {
        if(fos != null) {
          fos.flush();
          fos.close();
        }
      } catch (IOException e) {
        logger.info("IOException 발생");
      }
    }
    
    return fileFullPath;
  }
}
