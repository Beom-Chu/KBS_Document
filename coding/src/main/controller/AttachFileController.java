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
public class AttachFileController extends MsCoreSupportController{
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource(name="cmmAttachFileService")
	private CmmAttachFileService cmmAttachFileService;
	
	@Resource(name="cmmCdTbdService")
	private CmmCdTbdService cmmCdTbdService;
	
	@Resource(name = "attachFileUtil")
	private AttachFileUtil attachFileUtil;
	

	@RequestMapping({"/getAttachFile.do"})
	public String getAttachFile(ModelMap model, CmmAttachFileVo vo, HttpServletRequest request) {
		
		try {
			
			model.addAttribute("resultList", cmmAttachFileService.getAttachFile(vo));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "jsonView";
	}
	
	
	@RequestMapping(value = "/uploadAttachFile.do", method = RequestMethod.POST)
	public String uploadAttachFile(ModelMap model, @RequestParam("fileFacId") String fileFacId, @RequestParam("programId") String programId, @RequestParam("fileSeq") String fileSeq, @RequestParam("file") MultipartFile file, HttpServletRequest request) {
		
		try 
		{
			//?????? ?????????
			String originFilename = file.getOriginalFilename();
			
			//?????? ?????????
			String extName = originFilename.substring(originFilename.lastIndexOf("."), originFilename.length());

			//????????? ????????? ?????????
			DateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			String saveFileName = format.format(Calendar.getInstance().getTime());
			saveFileName += extName;
			
			// ?????? ??????
			Boolean result = attachFileUtil.uploadFile(file, saveFileName);
			
			if(result) {
				
				CmmAttachFileVo vo = new CmmAttachFileVo();
				//????????? ????????????
				MsCoreLoginVo loginUser = (MsCoreLoginVo) MsSessionUtil.getSessionAttribute(request,"loginUser");

				//?????? ????????? ?????? ??????
				if(fileSeq == null || fileSeq.equals("")) {
					//???????????? ????????? ??????
					vo = cmmAttachFileService.getAttachFileSeq(null).get(0);
				}else {
					vo.setFileSeq(fileSeq);
				}
				
				//???????????? DB ?????? ?????? ??????
				vo.setFacId(fileFacId);
				vo.setFileNm(originFilename);
				vo.setFileHashNm(saveFileName);
				vo.setFileUploadPath(System.getProperty("attachFileDir"));
				vo.setProgramId(programId);
				vo.setCreatorId(loginUser.getUserId());
				vo.setModifierId(loginUser.getUserId());
				
				//???????????? DB ??????
				cmmAttachFileService.insertAttachFile(vo);
				
				model.addAttribute("fileSeq",vo.getFileSeq());
				model.addAttribute("resultMsg", "????????? ?????????????????????.");
				
			}else {
				model.addAttribute("resultCode","E");  
				model.addAttribute("resultMsg", "?????? ????????? ??????????????????.");
			}
		
		}catch (Exception e) {
			e.printStackTrace();
			
			model.addAttribute("resultCode","E");  
			model.addAttribute("resultMsg",e.getMessage());
		}
			
		return "jsonView";
	}

	@RequestMapping({"/downAttachFile.do"})
	public void downAttachFile(ModelMap model, CmmAttachFileVo vo, HttpServletRequest request, HttpServletResponse response) {
		
		//???????????? ????????????
		try {
			
			attachFileUtil.downLoadFile(vo,response);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	@RequestMapping(value = "/deleteAttachFile.do")
	public String deleteAttachFile(ModelMap model,  CmmAttachFileVo vo, HttpServletRequest request) {
		
		String jsonStr = MsCoreUtiles.readJSONStringFromRequestBody(request);		
		Gson gson = new Gson();
		
		try {
			
			List<CmmAttachFileVo> gmVoList = gson.fromJson(jsonStr, new TypeToken<List<CmmAttachFileVo>>(){}.getType());
			
			//???????????? DB ?????? ??????(Del Flag : Y)
			cmmAttachFileService.deleteWholeAttachFile(gmVoList,request);

			model.addAttribute("resultCode", "S");
			model.addAttribute("resultMsg", "???????????????????????????.");
			
		} catch (Exception e) {
			
			e.printStackTrace();
			model.addAttribute("resultCode","E");  
			model.addAttribute("resultMsg",e.getMessage());
		}
		
		return "jsonView";
		
	}
	
	
	@RequestMapping(value = "/uploadAttachFileOne.do", method = RequestMethod.POST)
	public String uploadAttachFileOne(ModelMap model, @RequestParam("fileFacId") String fileFacId, @RequestParam("programId") String programId, @RequestParam("fileSeq") String fileSeq, @RequestParam("fileOne") MultipartFile file, HttpServletRequest request) {

		try 
		{
			CmmAttachFileVo vo = new CmmAttachFileVo();
			MsCoreLoginVo loginUser = (MsCoreLoginVo) MsSessionUtil.getSessionAttribute(request,"loginUser"); //????????? ????????????
			
			//?????? ?????????
			String originFilename = file.getOriginalFilename();
			//?????? ?????????
			String extName = originFilename.substring(originFilename.lastIndexOf("."), originFilename.length());

			//????????? ????????? ?????????
			DateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			String saveFileName = format.format(Calendar.getInstance().getTime());
			saveFileName += extName;
			
			//?????? ????????? ??????
			if(fileSeq == null || fileSeq.equals("")) {
				//?????? ????????? ?????? ???????????? ????????? ??????
				vo = cmmAttachFileService.getAttachFileSeq(null).get(0);
				
			}else {
				vo.setFileSeq(fileSeq);
				//???????????? DB ??????
				List<CmmAttachFileVo> liVo = cmmAttachFileService.getAttachFile(vo);

				if(liVo.size() > 0) {
					for(CmmAttachFileVo delVo : liVo) {
						//????????? ?????? ?????? ?????? ?????? ??????
						attachFileUtil.deleteFile(delVo.getFileHashNm());
						
						//???????????? DB ??????
						delVo.setModifierId(loginUser.getUserId());
						delVo.setDelFlag("Y"); //???????????? : Y(??????)
						cmmAttachFileService.updateAttachFile(delVo);
					}
				}
			}
			
			// ?????? ??????
			Boolean result = attachFileUtil.uploadFile(file, saveFileName);
			
			if(result) {
				
				//???????????? DB ?????? ?????? ??????
				vo.setFacId(fileFacId);
				vo.setFileNm(originFilename);
				vo.setFileHashNm(saveFileName);
				vo.setFileUploadPath(System.getProperty("attachFileDir"));
				vo.setProgramId(programId);
				vo.setCreatorId(loginUser.getUserId());
				vo.setModifierId(loginUser.getUserId());
				
				//???????????? DB ??????
				cmmAttachFileService.insertAttachFile(vo);
				
				model.addAttribute("fileSeq",vo.getFileSeq());
				model.addAttribute("fileNm",vo.getFileNm());
				model.addAttribute("resultMsg", "????????? ?????????????????????.");
				
			}else {
				model.addAttribute("resultCode","E");  
				model.addAttribute("resultMsg", "?????? ????????? ??????????????????.");
			}
		
		}catch (Exception e) {
			
			e.printStackTrace();
			model.addAttribute("resultCode","E");  
			model.addAttribute("resultMsg",e.getMessage());
		}
			
		return "jsonView";
	}
}

