/*==============================================================================
*Copyright(c) 2021 HYOSUNG  ADVANCED  MATERIALS Co., Ltd. / ITRION Co., Ltd.
*
*@ProcessChain : (울산)생산계획관리
*
*@File     : ProdPlanMgmtServiceImpl.java
*
*@FileName : (울산)생산계획관리  ServiceImpl Class
*
* Date             Ver.      Name   Description
*-----------------------------------------------------------------------------
* 2021.03.22       1.00      KBS     최초 생성
==============================================================================*/

package main.service;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.itrion.msCore.common.contoller.MsCoreMessageSource;
import com.itrion.msCore.login.vo.MsCoreLoginVo;
import com.qms.qmei.service.ProdPlanMgmtService;
import com.qms.qmei.vo.ProdPlanMgmtVO;

import msCore.util.MsSessionUtil;

// TODO: Auto-generated Javadoc
@Service("prodPlanMgmtService")
public class ProdPlanMgmtServiceImpl implements ProdPlanMgmtService{
	
	@Resource(name="egovMessageSource")
    protected MsCoreMessageSource egovMessageSource;
	
	@Autowired
	private SessionLocaleResolver localeResolver; 
	
	@Resource(name = "prodPlanMgmtMapper")
	private ProdPlanMgmtMapper prodPlanMgmtMapper;
	

	/**
	 * Gets the prod plan mgmt.
	 *
	 * @param vo 　
	 * @return List<ProdPlanMgmtVO> 　
	 * @throws Exception 　
	 */
	@Override
	public List<ProdPlanMgmtVO> getProdPlanMgmt(ProdPlanMgmtVO vo) throws Exception {
		return prodPlanMgmtMapper.getProdPlanMgmt(vo);
	}

	/**
	 * Insert prod plan U.
	 *
	 * @param vo 　
	 * @return int 　
	 * @throws Exception 　
	 */
	@Override
	public int insertProdPlanU(ProdPlanMgmtVO vo) throws Exception {
		return prodPlanMgmtMapper.insertProdPlanU(vo);
	}

	/**
	 * Delete prod plan U.
	 *
	 * @param vo 　
	 * @return int 　
	 * @throws Exception 　
	 */
	@Override
	public int deleteProdPlanU(ProdPlanMgmtVO vo) throws Exception {
		return prodPlanMgmtMapper.deleteProdPlanU(vo);
	}
	

	/**
	 * Save prod plan U.
	 *
	 * @param model 　
	 * @param voList 　
	 * @param request 　
	 * @return ModelMap 　
	 * @throws Exception 　
	 */
	@Override
	public ModelMap saveProdPlanU(ModelMap model, List<ProdPlanMgmtVO> voList, HttpServletRequest request) throws Exception {
		
		//접속자 세션정보
		MsCoreLoginVo loginUser = (MsCoreLoginVo) MsSessionUtil.getSessionAttribute(request,"loginUser");
		
		try {
			
			boolean bFirst = true;
			for(ProdPlanMgmtVO vo : voList) {
				
				//로그인ID, 생산법인ID, 공장ID 설정
				vo.setCreatorId	 (loginUser.getUserId());
				vo.setModifierId (loginUser.getUserId());
				vo.setPrdtnCorpId(loginUser.getPrdtnCorpId());
				vo.setFacId		 (loginUser.getFacId());
				
				//처음 전체 삭제
				if(bFirst) {
					deleteProdPlanU(vo);
					bFirst = false;
				}
				
				insertProdPlanU(vo);
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
	 * Insert prev mon.
	 *
	 * @param vo 　
	 * @return int 　
	 * @throws Exception 　
	 */
	@Override
	public int insertPrevMon(ProdPlanMgmtVO vo) throws Exception {
		return prodPlanMgmtMapper.insertPrevMon(vo);
	}

}
