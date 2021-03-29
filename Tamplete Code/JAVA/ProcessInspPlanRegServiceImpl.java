
package com.qms.qmmi.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.itrion.msCore.common.service.CmmAttachFileService;
import com.itrion.msCore.login.vo.MsCoreLoginVo;
import com.qms.qmmi.service.ProcessInspPlanRegService;
import com.qms.qmmi.vo.ProcessInspPlanRegVO;

import msCore.util.MsSessionUtil;

// TODO: Auto-generated Javadoc
@Service("processInspPlanRegService")
public class ProcessInspPlanRegServiceImpl implements ProcessInspPlanRegService{
	
	@Resource(name = "processInspPlanRegMapper")
	private ProcessInspPlanRegMapper ProcessInspPlanRegMapper;
	
	@Resource(name="cmmAttachFileService")
	private CmmAttachFileService cmmAttachFileService;
	
	/**
	 * Gets the insp plan mast.
	 *
	 * @param vo 　
	 * @return List<ProcessInspPlanRegVO> 　
	 * @throws Exception 　
	 */
	@Override
	public List<ProcessInspPlanRegVO> getInspPlanMast(ProcessInspPlanRegVO vo) throws Exception {
		return ProcessInspPlanRegMapper.getInspPlanMast(vo);
	}

	/**
	 * Insert insp plan mast.
	 *
	 * @param vo 　
	 * @return int 　
	 * @throws Exception 　
	 */
	@Override
	public int insertInspPlanMast(ProcessInspPlanRegVO vo) throws Exception {
		
		return ProcessInspPlanRegMapper.insertInspPlanMast(vo);
	}

	/**
	 * Update insp plan mast.
	 *
	 * @param vo 　
	 * @return int 　
	 * @throws Exception 　
	 */
	@Override
	public int updateInspPlanMast(ProcessInspPlanRegVO vo) throws Exception {
		
		return ProcessInspPlanRegMapper.updateInspPlanMast(vo);
	}

	/**
	 * Delete insp plan mast.
	 *
	 * @param vo 　
	 * @return int 　
	 * @throws Exception 　
	 */
	@Override
	public int deleteInspPlanMast(ProcessInspPlanRegVO vo) throws Exception {
		
		return ProcessInspPlanRegMapper.deleteInspPlanMast(vo);
	}

	/**
	 * Gets the insp plan dtl.
	 *
	 * @param vo 　
	 * @return List<ProcessInspPlanRegVO> 　
	 * @throws Exception 　
	 */
	@Override
	public List<ProcessInspPlanRegVO> getInspPlanDtl(ProcessInspPlanRegVO vo) throws Exception {
		
		return ProcessInspPlanRegMapper.getInspPlanDtl(vo);
	}

	/**
	 * Insert insp plan dtl.
	 *
	 * @param vo 　
	 * @return int 　
	 * @throws Exception 　
	 */
	@Override
	public int insertInspPlanDtl(ProcessInspPlanRegVO vo) throws Exception {
		
		return ProcessInspPlanRegMapper.insertInspPlanDtl(vo);
	}

	/**
	 * Update insp plan dtl.
	 *
	 * @param vo 　
	 * @return int 　
	 * @throws Exception 　
	 */
	@Override
	public int updateInspPlanDtl(ProcessInspPlanRegVO vo) throws Exception {
		
		return ProcessInspPlanRegMapper.updateInspPlanDtl(vo);
	}

	/**
	 * Delete insp plan dtl.
	 *
	 * @param vo 　
	 * @return int 　
	 * @throws Exception 　
	 */
	@Override
	public int deleteInspPlanDtl(ProcessInspPlanRegVO vo) throws Exception {
		
		return ProcessInspPlanRegMapper.deleteInspPlanDtl(vo);
	}

	/**
	 * Save insp plan mast.
	 *
	 * @param model 　
	 * @param voList 　
	 * @param request 　
	 * @return int 　
	 * @throws Exception 　
	 */
	@Override
	public int saveInspPlanMast(ModelMap model, List<ProcessInspPlanRegVO> voList, HttpServletRequest request) throws Exception {
		
		int transactionCount = 0;
		
		//접속자 세션정보
		MsCoreLoginVo loginUser = (MsCoreLoginVo) MsSessionUtil.getSessionAttribute(request,"loginUser");
		
		for(ProcessInspPlanRegVO vo : voList) {
			
			//로그인ID, 생산법인ID, 공장ID 설정
			vo.setCreatorId(loginUser.getUserId());
			vo.setModifierId(loginUser.getUserId());
			vo.setPrdtnCorpId(loginUser.getPrdtnCorpId());
			vo.setFacId(loginUser.getFacId());
			
			//update, insert, delete 판별 
			if("insert".equals(vo.getActionType())) 
			{
				transactionCount += insertInspPlanMast(vo);
			}
			else if("delete".equals(vo.getActionType())) 
			{
				//Master 삭제 전에 Detail 전부 삭제
				deleteInspPlanDtl(vo);
				
				//첨부파일 일괄 삭제
				cmmAttachFileService.deleteWholeAttachFileAll(vo.getFileSeq(), request);
				
				transactionCount += deleteInspPlanMast(vo);
			}
			else 
			{
				transactionCount += updateInspPlanMast(vo);
			}
		}
		
		return transactionCount;
	}

	/**
	 * Save insp plan dtl.
	 *
	 * @param model 　
	 * @param voList 　
	 * @param request 　
	 * @return int 　
	 * @throws Exception 　
	 */
	@Override
	public int saveInspPlanDtl(ModelMap model, List<ProcessInspPlanRegVO> voList, HttpServletRequest request) throws Exception {
		
		int transactionCount = 0;
		
		//접속자 세션정보
		MsCoreLoginVo loginUser = (MsCoreLoginVo) MsSessionUtil.getSessionAttribute(request,"loginUser");
		
		for(ProcessInspPlanRegVO vo : voList) {
			
			//로그인ID, 생산법인ID, 공장ID 설정
			vo.setCreatorId(loginUser.getUserId());
			vo.setModifierId(loginUser.getUserId());
			vo.setPrdtnCorpId(loginUser.getPrdtnCorpId());
			vo.setFacId(loginUser.getFacId());
			
			//update, insert, delete 판별 
			if("insert".equals(vo.getActionType())) 
			{
				transactionCount += insertInspPlanDtl(vo);
			}
			else if("delete".equals(vo.getActionType())) 
			{
				transactionCount += deleteInspPlanDtl(vo);
			}
			else 
			{
				transactionCount += updateInspPlanDtl(vo);
			}
		}
		
		return transactionCount;
	}

	/**
	 * Update insp plan mast file seq.
	 *
	 * @param vo 　
	 * @return int 　
	 * @throws Exception 　
	 */
	@Override
	public int updateInspPlanMastFileSeq(ProcessInspPlanRegVO vo) throws Exception {
		return ProcessInspPlanRegMapper.updateInspPlanMastFileSeq(vo);
	}

	/**
	 * Gets the insp plan mast popup.
	 *
	 * @param vo 　
	 * @return List<ProcessInspPlanRegVO> 　
	 * @throws Exception 　
	 */
	@Override
	public List<ProcessInspPlanRegVO> getInspPlanMastPopup(ProcessInspPlanRegVO vo) throws Exception {
		return ProcessInspPlanRegMapper.getInspPlanMastPopup(vo);
	}


}
