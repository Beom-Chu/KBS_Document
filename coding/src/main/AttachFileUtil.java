package main;

import java.io.File;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;


import sun.misc.IOUtils;

public class AttachFileUtil {
	
		
	/**
	 * Upload file.
	 *
	 * @param multipartFile 　
	 * @param saveFileName 　
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Boolean uploadFile(MultipartFile multipartFile, String saveFileName) throws Exception{
		
		Boolean result = true;
		String sAttachFileDir = System.getProperty("attachFileDir");	//첨부파일 경로
		
		try (
				FileOutputStream fos = new FileOutputStream(sAttachFileDir + saveFileName);
				)
		{
			File fileSaveDir = new File(sAttachFileDir);
			
			// 파일 경로 없으면 생성
			if (!fileSaveDir.exists()) {
				fileSaveDir.mkdirs();
			}
			
			byte[] data = multipartFile.getBytes();
			
			fos.write(data);
			
		}catch (Exception e) {
			e.printStackTrace();
		} 
		
		return result;
	}
	
	
	/**
	 * Down load file.
	 *
	 * @param vo 　
	 * @param response 　
	 * @throws Exception 　
	 */
	public void downLoadFile(CmmAttachFileVo vo,HttpServletResponse response) throws Exception {
		
		String filePathAndName = System.getProperty("attachFileDir");	//첨부파일 경로
		String fileName = new String(vo.getFileNm().getBytes("UTF-8"), "ISO-8859-1");	//다운받을 파일명(한글깨짐방지)
		
		filePathAndName += vo.getFileHashNm(); //첨부파일경로+첨부파일명
		
		final File fileToDownload = new File(filePathAndName);
		
		response.setContentType("application/octet-stream; charset=utf-8");
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.setHeader("Content-Disposition", "attachment; filename="+fileName);
		
		try (
				FileInputStream  fis = new FileInputStream(fileToDownload); 
				)
		{
			
			IOUtils.copy(fis, response.getOutputStream());
			response.flushBuffer(); 
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * Delete file.
	 *
	 * @param delFileName 　
	 * @return Boolean 　
	 * @throws Exception 　
	 */
	public Boolean deleteFile(String delFileName) throws Exception {
		
		Boolean result = true;
		
		String sAttachFileDir = System.getProperty("attachFileDir");	//첨부파일 경로
		String filePathAndName = sAttachFileDir + delFileName;			//첨부파일 경로 + 파일명
		
		try {
		
			File file = new File(filePathAndName);	
			
			if(file.exists()){
				file.delete();
//				logger.info("[[[[파일 삭제됨");
			}else{
//				logger.info("[[[[파일 없음");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
}