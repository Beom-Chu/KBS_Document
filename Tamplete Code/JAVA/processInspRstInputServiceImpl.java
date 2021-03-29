/*==============================================================================
*Copyright(c) 2020 HYOSUNG  ADVANCED  MATERIALS Co., Ltd. / ITRION Co., Ltd.
*
*@ProcessChain : 물성검사결과입력
*
*@File     : processInspRstInputServiceImpl.java
*
*@FileName : 물성검사결과입력  ServiceImpl Class
*
* Date             Ver.      Name   Description
*-----------------------------------------------------------------------------
* 2021.03.04       1.00      KBS     최초 생성
==============================================================================*/

package com.qms.qmmi.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.itrion.msCore.common.contoller.MsCoreMessageSource;
import com.itrion.msCore.login.vo.MsCoreLoginVo;
import com.qms.common.util.QmsUtils;
import com.qms.qmcm.common.CalcFormulaCommon;
import com.qms.qmmi.service.ProcessInspRegService;
import com.qms.qmmi.service.ProcessInspRstInputService;
import com.qms.qmmi.vo.ProcessInspRegVO;
import com.qms.qmmi.vo.ProcessInspRstInputVO;

import msCore.util.MsCoreUtiles;
import msCore.util.MsSessionUtil;

// TODO: Auto-generated Javadoc
@Service("processInspRstInputService")
public class processInspRstInputServiceImpl implements ProcessInspRstInputService{
	
	@Resource(name="egovMessageSource")
    protected MsCoreMessageSource egovMessageSource;
	
	@Autowired
	private SessionLocaleResolver localeResolver; 
	
	@Resource(name = "processInspRstInputMapper")
	private ProcessInspRstInputMapper processInspRstInputMapper;
	
	@Resource(name = "processInspRegService")
	private ProcessInspRegService processInspRegService;
	
	@Autowired
	private CalcFormulaCommon calcFormulaCommon;
	
	/**
	 * Gets the process insp rst input.
	 *
	 * @param vo 　
	 * @return List<ProcessInspRstInputVO> 　
	 */
	@Override
	public List<ProcessInspRstInputVO> getProcessInspRstInput(ProcessInspRstInputVO vo) {
		return processInspRstInputMapper.getProcessInspRstInput(vo);
	}

	/**
	 * Update process insp rst input.
	 *
	 * @param vo 　
	 * @return int 　
	 * @throws Exception 　
	 */
	@Override
	public int updateProcessInspRstInput(ProcessInspRstInputVO vo) throws Exception {
		return processInspRstInputMapper.updateProcessInspRstInput(vo);
	}
	
	/**
	 * Update process insp pos dtl rst.
	 *
	 * @param vo 　
	 * @return int 　
	 * @throws Exception 　
	 */
	@Override
	public int updateProcessInspPosDtlRst(ProcessInspRstInputVO vo) throws Exception {
		return processInspRstInputMapper.updateProcessInspPosDtlRst(vo);
	}
	
	/**
	 * Update process insp dtl stat.
	 *
	 * @param vo 　
	 * @return int 　
	 * @throws Exception 　
	 */
	@Override
	public int updateProcessInspDtlStat(ProcessInspRstInputVO vo) throws Exception {
		return processInspRstInputMapper.updateProcessInspDtlStat(vo);
	}

	/**
	 * Update process insp mast stat.
	 *
	 * @param vo 　
	 * @return int 　
	 * @throws Exception 　
	 */
	@Override
	public int updateProcessInspMastStat(ProcessInspRstInputVO vo) throws Exception {
		return processInspRstInputMapper.updateProcessInspMastStat(vo);
	}
	
	/**
	 * Update process insp dtl stat pos.
	 *
	 * @param vo 　
	 * @return int 　
	 * @throws Exception 　
	 */
	@Override
	public int updateProcessInspDtlStatPos(ProcessInspRstInputVO vo) throws Exception {
		return processInspRstInputMapper.updateProcessInspDtlStatPos(vo);
	}

	/**
	 * Save process insp rst input.
	 *
	 * @param model 　
	 * @param voList 　
	 * @param request 　
	 * @return ModelMap 　
	 * @throws Exception 　
	 */
	@Override
	public ModelMap saveProcessInspRstInput(ModelMap model, List<ProcessInspRstInputVO> voList, HttpServletRequest request) throws Exception {
		//접속자 세션정보
		MsCoreLoginVo loginUser = (MsCoreLoginVo) MsSessionUtil.getSessionAttribute(request,"loginUser");
		
		try {
			
			for(ProcessInspRstInputVO vo : voList) {
				
				//로그인ID, 생산법인ID, 공장ID 설정
				vo.setCreatorId	 (loginUser.getUserId());
				vo.setModifierId (loginUser.getUserId());
				vo.setPrdtnCorpId(loginUser.getPrdtnCorpId());
				vo.setFacId		 (loginUser.getFacId());
				
				//검사 진행상태 체크
				ProcessInspRegVO chkParam = new ProcessInspRegVO(vo);
				if(processInspRegService.checkStatus(chkParam,"D","C"))
				{
					model.addAttribute("resultCode", "E");
					model.addAttribute("resultMsg", egovMessageSource.getMessage("qms.msg_000024",localeResolver.resolveLocale(request)));
					return model;
				}
				
				//결과값 설정
				vo.setRstVal(vo.getInspVal());
				
				//결과값이 Null인 경우 합부판정 Null
				if(MsCoreUtiles.isNull(vo.getRstVal())) {
					vo.setInspItemRst(null);
				} else {
					//합부판정 설정
					vo.setInspItemRst(QmsUtils.getInspItemRst(vo.getMgmtStndTpCd(), vo.getSpecMax(), vo.getSpecMin(), vo.getRstVal()));
				}
				
				//검사결과 수정
				updateProcessInspRstInput(vo);
				
				//검사POS상세 합부판정 수정 [검사결과 모두 합격인 경우 합격(P),그외 불합격(F)]
				updateProcessInspPosDtlRst(vo);
				
				//검사 상세 진행상태 수정 [검사POS상세 합부판정이 모두 입력된 경우 결과입력(B), 그외 검사등록(A)]
				updateProcessInspDtlStatPos(vo);
				
				//검사 마스터 진행상태 수정 [검사상세 진행상태 모두 결과입력(B)인 경우 결과입력(B)]
				updateProcessInspMastStat(vo);
			}
			
			// 계산식 적용
			for(ProcessInspRstInputVO vo : voList) {
				//수정 항목 연관 검사항목 조회
				List<ProcessInspRstInputVO> liItemCd = getProcessInspRstInputItemCdList(vo);
				
				for(ProcessInspRstInputVO voItem : liItemCd) {
					
					//계산식번호 Not Null인 경우
					if(!MsCoreUtiles.isNull(voItem.getFrmlNo())){
						
						//검사항목별 분석값 Json Data 추출
						String sItemCdValJson = getProcessInspRstInputItemCdValJson(voItem);
						
						//계산식 수행
						String rs = calcFormulaCommon.getCalcFormulaResult(voItem.getPrdtnCorpId(), voItem.getFrmlNo(), sItemCdValJson);
						
						//계산 결과값 설정
						voItem.setRstVal(rs);
						
						//합부판정 설정
						voItem.setInspItemRst(QmsUtils.getInspItemRst(voItem.getMgmtStndTpCd(), voItem.getSpecMax(), voItem.getSpecMin(), voItem.getRstVal()));
						
						//수정 항목의 검사자, 검사일자로 설정
						voItem.setInspUserId(vo.getInspUserId());
						voItem.setInspDt(vo.getInspDt());
						
						//수정자, 수정 프로그램ID 설정
						voItem.setModifierId(vo.getModifierId());
						voItem.setModifierProgramId(vo.getModifierProgramId());
						
						//검사결과 수정
						updateProcessInspRstInput(voItem);
						
						//검사POS상세 합부판정 수정 [검사결과 모두 합격인 경우 합격(P),그외 불합격(F)]
						updateProcessInspPosDtlRst(voItem);
						
						//검사 상세 진행상태 수정 [검사POS상세 합부판정이 모두 입력된 경우 결과입력(B), 그외 검사등록(A)]
						updateProcessInspDtlStatPos(voItem);
						
						//검사 마스터 진행상태 수정 [검사상세 진행상태 모두 결과입력(B)인 경우 결과입력(B)]
						updateProcessInspMastStat(voItem);
					}
				}
			}
			
		} catch (Exception e) {
			model.addAttribute("resultCode", "E");
			model.addAttribute("resultMsg",e.toString());
			return model;
		}
		
		model.addAttribute("resultCode", "S");
		model.addAttribute("resultMsg", egovMessageSource.getMessage("qms.msg_000009",localeResolver.resolveLocale(request))); //정상 처리되었습니다
		return model;
	}

	
	/**
	 * Save process insp judg conf.
	 *
	 * @param model 　
	 * @param voList 　
	 * @param request 　
	 * @return ModelMap 　
	 * @throws Exception 　
	 */
	@Override
	public ModelMap saveProcessInspJudgConf(ModelMap model, List<ProcessInspRstInputVO> voList, HttpServletRequest request) throws Exception {

		//접속자 세션정보
		MsCoreLoginVo loginUser = (MsCoreLoginVo) request.getSession().getAttribute("loginUser");
		
		try {
			
			for(ProcessInspRstInputVO vo : voList) {
				//로그인ID, 생산법인ID, 공장ID 설정
				vo.setModifierId (loginUser.getUserId());
				vo.setPrdtnCorpId(loginUser.getPrdtnCorpId());
				vo.setFacId		 (loginUser.getFacId());
				
				//검사 진행상태 체크
				ProcessInspRegVO chkParam = new ProcessInspRegVO(vo);
				String chkStat = "";
				
				/* 확정 : 진행상태가 [B:결과입력]인 경우만 확정 가능 */
				/* 확정취소 : 진행상태가 [C:판정확정]인 경우만 취소 가능 */
				if(vo.getInspDtlStatus().equals("C")) chkStat = "B";
				else if(vo.getInspDtlStatus().equals("B")) chkStat = "C";
				
				if(!processInspRegService.checkStatus(chkParam,"D",chkStat)) { 
					model.addAttribute("resultCode", "E");
					model.addAttribute("resultMsg", egovMessageSource.getMessage("qms.msg_000029",localeResolver.resolveLocale(request)));
					return model;
				}
				
				//진행상태 수정
				updateProcessInspDtlStat(vo);
				
				//검사 마스터 진행상태 수정 [검사상세 진행상태 모두 결과입력(B)인 경우 결과입력(B)]
				updateProcessInspMastStat(vo);
			}
			
		}catch (Exception e) {
			model.addAttribute("resultCode", "E");
			model.addAttribute("resultMsg",e.toString());
			return model;
		}
		
		model.addAttribute("resultCode", "S");
		model.addAttribute("resultMsg", egovMessageSource.getMessage("qms.msg_000009",localeResolver.resolveLocale(request))); //정상 처리되었습니다
		return model; 	
	}
	
	/**
	 * Gets the process insp rst input item cd list.
	 *
	 * @param vo 　
	 * @return List<ProcessInspRstInputVO> 　
	 * @throws Exception 　
	 */
	@Override
	public List<ProcessInspRstInputVO> getProcessInspRstInputItemCdList(ProcessInspRstInputVO vo) throws Exception {
		return processInspRstInputMapper.getProcessInspRstInputItemCdList(vo);
	}
	
	/**
	 * Gets the process insp rst input item cd val json.
	 *
	 * @param vo 　
	 * @return String 　
	 */
	@Override
	public String getProcessInspRstInputItemCdValJson(ProcessInspRstInputVO vo) {
		return processInspRstInputMapper.getProcessInspRstInputItemCdValJson(vo);
	}

	
}
