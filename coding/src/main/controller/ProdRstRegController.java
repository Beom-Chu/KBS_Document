package main.controller;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itrion.msCore.login.vo.MsCoreLoginVo;
import com.qms.common.service.CmmPopupService;
import com.qms.qmei.service.ProdRstRegService;
import com.qms.qmei.vo.ProdRstRegVO;
import com.qms.qmmd.service.SmpMgmtService;
import com.qms.qmmd.vo.SmpMgmtVO;

import msCore.cmm.controller.MsCoreSupportController;
import msCore.util.MsCoreDateUtil;
import msCore.util.MsCoreUtiles;
import msCore.util.MsSessionUtil;

@Controller
public class ProdRstRegController extends MsCoreSupportController {
	
	@Resource(name ="smpMgmtService")
	private SmpMgmtService smpMgmtService;
	
	@Resource(name ="cmmPopupService")
	private CmmPopupService cmmPopupService;
	
	@Resource(name = "prodRstRegService")
	private ProdRstRegService prodRstRegService;
	

	/**
	 * Inits the prod rst reg.
	 *
	 * @param model 　
	 * @param vo 　
	 * @param request 　
	 * @return String 　
	 * @throws Exception 　
	 */
	@RequestMapping(value = "/initProdRstReg.do")
	public String initProdRstReg(ModelMap model, ProdRstRegVO vo, HttpServletRequest request) throws Exception {
		this.insertLog(request, "initProdRstReg", "initProdRstReg");
		
		initView(model, request);
		
		//접속자 세션정보
		MsCoreLoginVo loginUser = (MsCoreLoginVo) MsSessionUtil.getSessionAttribute(request,"loginUser");
				
		//시료 관리 LOV 조회
		Gson gson = new Gson();
		SmpMgmtVO lovVo = new SmpMgmtVO();
		
		lovVo.setPrdtnCorpId(loginUser.getPrdtnCorpId());
		lovVo.setFacId(loginUser.getFacId());
		
		lovVo.setDataTpCd("SMP_TYPE"); /*데이터유형코드[SMP_TYPE:시료유형,SMP_GROUP:시료그룹,SMP_ITEM:시료]*/
		model.addAttribute("lovSmpTpCd",gson.toJson(smpMgmtService.getSmpMgmtLov(lovVo)));
		
		lovVo.setDataTpCd("SMP_GROUP"); /*데이터유형코드[SMP_TYPE:시료유형,SMP_GROUP:시료그룹,SMP_ITEM:시료]*/
		model.addAttribute("lovSmpGrpCd",gson.toJson(smpMgmtService.getSmpMgmtLov(lovVo)));
		
		lovVo.setDataTpCd("SMP_ITEM"); /*데이터유형코드[SMP_TYPE:시료유형,SMP_GROUP:시료그룹,SMP_ITEM:시료]*/
		model.addAttribute("lovSmpCd",gson.toJson(smpMgmtService.getSmpMgmtLov(lovVo)));
		
		//공정 Lov 조회
		HashMap<String, Object> map = Maps.newHashMap();
		model.addAttribute("lovSmpGrpPrdpc",gson.toJson(cmmPopupService.getSmpGrpPrdpc(map,request)));
		
		//설비 Lov 조회
		model.addAttribute("lovEqpmn",gson.toJson(cmmPopupService.getEqpmnLov(map, request)));
		
		String toDt  = MsCoreDateUtil.todaYyyyMMddHHmmss().substring(0, 8);
		model.addAttribute("toDt",MsCoreDateUtil.dayDisplayHyphen(toDt));
		
		// 라벨 Argument용
		model.addAttribute("lb_btn_copy_prev_month"	, egovMessageSource.getMessage("qms.lb_btn_copy_prev_month"	, localeResolver.resolveLocale(request))); 
		
		return "qmei/prodRstReg";
	}
	
	/**
	 * Gets the prod rst reg.
	 *
	 * @param model 　
	 * @param vo 　
	 * @param request 　
	 * @return String 　
	 * @throws Exception 　
	 */
	@RequestMapping(value = "/getProdRstReg.do")
	public String getProdRstReg(ModelMap model, ProdRstRegVO vo, HttpServletRequest request) throws Exception{
	
		//접속자 세션정보
		MsCoreLoginVo loginUser = (MsCoreLoginVo) MsSessionUtil.getSessionAttribute(request,"loginUser");

		vo.setPrdtnCorpId(loginUser.getPrdtnCorpId());	/*생산법인ID*/
		vo.setFacId(loginUser.getFacId());				/*공장ID*/

		model.addAttribute("resultList", prodRstRegService.getProdRstReg(vo));
		

		return "jsonView";
	}
	
	
	/**
	 * Save prod rst reg.
	 *
	 * @param model 　
	 * @param vo 　
	 * @param request 　
	 * @return String 　
	 * @throws Exception 　
	 */
	@RequestMapping(value = "/saveProdRstReg.do")
	public String saveProdRstReg(ModelMap model, ProdRstRegVO vo, HttpServletRequest request) throws Exception {
		
		String jsonStr = MsCoreUtiles.readJSONStringFromRequestBody(request);
		Gson gson = new Gson();

		try {
			
			List<ProdRstRegVO> voList = gson.fromJson(jsonStr, new TypeToken<List<ProdRstRegVO>>() {}.getType());

			prodRstRegService.saveProdRstInfo(model, voList, request);

		} catch (Exception e) {
			model.addAttribute("resultCode", "E");
			model.addAttribute("resultMsg",e.toString());
			return "jsonView";
		}

		return "jsonView";
	}
	
	/**
	 * Send prod rst tracking.
	 *
	 * @param model 　
	 * @param vo 　
	 * @param request 　
	 * @return String 　
	 * @throws Exception 　
	 */
	@RequestMapping(value = "/sendProdRstTracking.do")
	public String sendProdRstTracking(ModelMap model, ProdRstRegVO vo, HttpServletRequest request) throws Exception {
		
		try {
			
			//접속자 세션정보
			MsCoreLoginVo loginUser = (MsCoreLoginVo) MsSessionUtil.getSessionAttribute(request,"loginUser");

			vo.setPrdtnCorpId(loginUser.getPrdtnCorpId());	/*생산법인ID*/
			vo.setFacId(loginUser.getFacId());				/*공장ID*/
			
			//재조회 후의 생산실적으로 전송
			List<ProdRstRegVO> voList = prodRstRegService.getProdRstReg(vo);

			prodRstRegService.sendProdRstTracking(model, voList, request);

		} catch (Exception e) {
			model.addAttribute("resultCode", "E");
			model.addAttribute("resultMsg",e.toString());
			return "jsonView";
		}

		return "jsonView";
	}
}
