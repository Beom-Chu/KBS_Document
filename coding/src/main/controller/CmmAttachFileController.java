
package main.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itrion.msCore.common.service.CmmAttachFileService;
import com.itrion.msCore.common.service.CmmCdTbdService;
import com.itrion.msCore.common.vo.CmmAttachFileVo;
import com.itrion.msCore.login.vo.MsCoreLoginVo;

import msCore.cmm.controller.MsCoreSupportController;
import msCore.util.MsCoreAttachFileUtil;
import msCore.util.MsCoreUtiles;
import msCore.util.MsSessionUtil;

@Controller
public class CmmAttachFileController extends MsCoreSupportController{
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource(name="cmmAttachFileService")
	private CmmAttachFileService cmmAttachFileService;
	
	@Resource(name="cmmCdTbdService")
	private CmmCdTbdService cmmCdTbdService;
	
	// 로그 Action Name
	private String logActionName = "파일첨부";
	

	@RequestMapping({"/getAttachFile.do"})
	public String getAttachFile(ModelMap model, CmmAttachFileVo vo, HttpServletRequest request) throws Exception {
		
		model.addAttribute("resultList", cmmAttachFileService.getAttachFile(vo));
		return "jsonView";
	}
	
	
	@RequestMapping(value = "/uploadAttachFile.do", method = RequestMethod.POST)
	public String uploadAttachFile(ModelMap model, @RequestParam("fileFacId") String fileFacId, @RequestParam("programId") String programId, @RequestParam("fileSeq") String fileSeq, @RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {
		
		try 
		{
			//실제 파일명
			String originFilename = file.getOriginalFilename();
			//파일 확장자
			String extName = originFilename.substring(originFilename.lastIndexOf("."), originFilename.length());

			//서버에 저장될 파일명
			DateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			String saveFileName = format.format(Calendar.getInstance().getTime());
			saveFileName += extName;
			
			// 파일 첨부
			MsCoreAttachFileUtil attachFileUtil = new MsCoreAttachFileUtil();
			Boolean result = attachFileUtil.uploadFile(file, saveFileName);
			
			if(result) {
				
				CmmAttachFileVo vo = new CmmAttachFileVo();
				//접속자 세션정보
				MsCoreLoginVo loginUser = (MsCoreLoginVo) MsSessionUtil.getSessionAttribute(request,"loginUser");

				//파일 시퀀스 존재 유무
				if(fileSeq == null || fileSeq.equals("")) {
					//첨부파일 시퀀스 조회
					vo = cmmAttachFileService.getAttachFileSeq(null).get(0);
				}else {
					vo.setFileSeq(fileSeq);
				}
				
				//첨부파일 DB 등록 항목 설정
				vo.setFacId(fileFacId);
				vo.setFileNm(originFilename);
				vo.setFileHashNm(saveFileName);
				vo.setFileUploadPath(System.getProperty("attachFileDir"));
				vo.setProgramId(programId);
				vo.setCreatorId(loginUser.getUserId());
				vo.setModifierId(loginUser.getUserId());
				
				//첨부파일 DB 등록
				cmmAttachFileService.insertAttachFile(vo);
				
				model.addAttribute("fileSeq",vo.getFileSeq());
				model.addAttribute("resultMsg", "파일이 저장되었습니다.");
				
			}else {
				model.addAttribute("resultCode","E");  
				model.addAttribute("resultMsg", "파일 저장이 실패했습니다.");
			}
		
		}catch (Exception e) {
			System.out.println(e.toString());
			
			model.addAttribute("resultCode","E");  
			model.addAttribute("resultMsg",e.getMessage());
			return "jsonView";
		}
			
		return "jsonView";
	}

	@RequestMapping({"/downAttachFile.do"})
	public void downAttachFile(ModelMap model, CmmAttachFileVo vo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//첨부파일 다운로드
		MsCoreAttachFileUtil attachFileUtil = new MsCoreAttachFileUtil();
		attachFileUtil.downLoadFile(vo,response);
		
	}
	
	
	@RequestMapping(value = "/deleteAttachFile.do")
	public String deleteAttachFile(ModelMap model,  CmmAttachFileVo vo, HttpServletRequest request) throws Exception{
		
		String jsonStr = MsCoreUtiles.readJSONStringFromRequestBody(request);		
		Gson gson = new Gson();
		
		try {
			
			List<CmmAttachFileVo> gmVoList = gson.fromJson(jsonStr, new TypeToken<List<CmmAttachFileVo>>(){}.getType());
			
			//첨부파일 DB 정보 삭제(Del Flag : Y)
			cmmAttachFileService.deleteWholeAttachFile(gmVoList,request);

		} catch (Exception e) {
			
			System.out.println(e.toString());
				
			model.addAttribute("resultCode","E");  
			model.addAttribute("resultMsg",e.getMessage());
			return "jsonView";
		}
		
		model.addAttribute("resultCode", "S");
		model.addAttribute("resultMsg", "정상처리되었습니다.");
		
		return "jsonView";
		
	}
	
	
	@RequestMapping(value = "/uploadAttachFileOne.do", method = RequestMethod.POST)
	public String uploadAttachFileOne(ModelMap model, @RequestParam("fileFacId") String fileFacId, @RequestParam("programId") String programId, @RequestParam("fileSeq") String fileSeq, @RequestParam("fileOne") MultipartFile file, HttpServletRequest request) throws Exception {

		try 
		{
			CmmAttachFileVo vo = new CmmAttachFileVo();
			MsCoreAttachFileUtil attachFileUtil = new MsCoreAttachFileUtil();	//파일첨부 유틸
			MsCoreLoginVo loginUser = (MsCoreLoginVo) MsSessionUtil.getSessionAttribute(request,"loginUser"); //접속자 세션정보
			
			//실제 파일명
			String originFilename = file.getOriginalFilename();
			//파일 확장자
			String extName = originFilename.substring(originFilename.lastIndexOf("."), originFilename.length());

			//서버에 저장될 파일명
			DateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			String saveFileName = format.format(Calendar.getInstance().getTime());
			saveFileName += extName;
			
			//파일 기등록 여부
			if(fileSeq == null || fileSeq.equals("")) {
				//최초 등록인 경우 첨부파일 시퀀스 조회
				vo = cmmAttachFileService.getAttachFileSeq(null).get(0);
				
			}else {
				vo.setFileSeq(fileSeq);
				//첨부파일 DB 조회
				List<CmmAttachFileVo> liVo = cmmAttachFileService.getAttachFile(vo);

				if(liVo.size() > 0) {
					for(CmmAttachFileVo delVo : liVo) {
						//기등록 서버 파일 있는 경우 삭제
						attachFileUtil.deleteFile(delVo.getFileHashNm());
						
						//첨부파일 DB 수정
						delVo.setModifierId(loginUser.getUserId());
						delVo.setDelFlag("Y"); //삭제여부 : Y(삭제)
						cmmAttachFileService.updateAttachFile(delVo);
					}
				}
			}
			
			// 파일 첨부
			Boolean result = attachFileUtil.uploadFile(file, saveFileName);
			
			if(result) {
				
				//첨부파일 DB 등록 항목 설정
				vo.setFacId(fileFacId);
				vo.setFileNm(originFilename);
				vo.setFileHashNm(saveFileName);
				vo.setFileUploadPath(System.getProperty("attachFileDir"));
				vo.setProgramId(programId);
				vo.setCreatorId(loginUser.getUserId());
				vo.setModifierId(loginUser.getUserId());
				
				//첨부파일 DB 등록
				cmmAttachFileService.insertAttachFile(vo);
				
				model.addAttribute("fileSeq",vo.getFileSeq());
				model.addAttribute("fileNm",vo.getFileNm());
				model.addAttribute("resultMsg", "파일이 저장되었습니다.");
				
			}else {
				model.addAttribute("resultCode","E");  
				model.addAttribute("resultMsg", "파일 저장이 실패했습니다.");
			}
		
		}catch (Exception e) {
			System.out.println(e.toString());
			
			model.addAttribute("resultCode","E");  
			model.addAttribute("resultMsg",e.getMessage());
			return "jsonView";
		}
			
		return "jsonView";
	}
}

