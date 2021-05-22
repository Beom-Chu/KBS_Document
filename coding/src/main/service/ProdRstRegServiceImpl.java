
package main.service;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.itrion.msCore.common.contoller.MsCoreMessageSource;
import com.itrion.msCore.login.vo.MsCoreLoginVo;
import com.qms.qmei.service.ProdRstRegService;
import com.qms.qmei.vo.ProdRstRegVO;

import msCore.util.MsCoreDateUtil;
import msCore.util.MsCoreUtiles;
import msCore.util.MsSessionUtil;


@Service("prodRstRegService")
public class ProdRstRegServiceImpl implements ProdRstRegService{
	
	@Resource(name="egovMessageSource")
    protected MsCoreMessageSource egovMessageSource;
	
	@Autowired
	private SessionLocaleResolver localeResolver; 
	
	@Resource(name = "prodRstRegMapper")
	private ProdRstRegMapper prodRstRegMapper;
	


	/**
	 * Gets the prod rst reg.
	 *
	 * @param vo 　
	 * @return List<ProdRstRegVO> 　
	 * @throws Exception 　
	 */
	@Override
	public List<ProdRstRegVO> getProdRstReg(ProdRstRegVO vo) throws Exception {
		return prodRstRegMapper.getProdRstReg(vo);
	}


	/**
	 * Insert prod rst info.
	 *
	 * @param vo 　
	 * @return int 　
	 * @throws Exception 　
	 */
	@Override
	public int insertProdRstInfo(ProdRstRegVO vo) throws Exception {
		return prodRstRegMapper.insertProdRstInfo(vo);
	}
	
	/**
	 * Update prod rst info.
	 *
	 * @param vo 　
	 * @return int 　
	 * @throws Exception 　
	 */
	@Override
	public int updateProdRstInfo(ProdRstRegVO vo) throws Exception {
		return prodRstRegMapper.updateProdRstInfo(vo);
	}


	/**
	 * Delete prod rst info.
	 *
	 * @param vo 　
	 * @return int 　
	 * @throws Exception 　
	 */
	@Override
	public int deleteProdRstInfo(ProdRstRegVO vo) throws Exception {
		return prodRstRegMapper.deleteProdRstInfo(vo);
	}
	
	/**
	 * Save prod rst info.
	 *
	 * @param model 　
	 * @param voList 　
	 * @param request 　
	 * @return ModelMap 　
	 * @throws Exception 　
	 */
	@Override
	public ModelMap saveProdRstInfo(ModelMap model, List<ProdRstRegVO> voList, HttpServletRequest request) throws Exception {
		
		//접속자 세션정보
		MsCoreLoginVo loginUser = (MsCoreLoginVo) MsSessionUtil.getSessionAttribute(request,"loginUser");
		
		try {
			
			for(ProdRstRegVO vo : voList) {
				
				//로그인ID, 생산법인ID, 공장ID 설정
				vo.setCreatorId	 (loginUser.getUserId());
				vo.setModifierId (loginUser.getUserId());
				vo.setPrdtnCorpId(loginUser.getPrdtnCorpId());
				vo.setFacId		 (loginUser.getFacId());
				
				//실적전송 여부 체크[실적 전송한 경우 수정 및 삭제 불가]
				if(!"insert".equals(vo.getActionType())) {
					List<ProdRstRegVO> liChk = getProdRstReg(vo);
					for(ProdRstRegVO voChk : liChk) {
						if(!MsCoreUtiles.isNull(voChk.getMstNo())) {
							model.addAttribute("resultCode", "E");
							model.addAttribute("resultMsg",egovMessageSource.getMessage("qms.msg_000036",localeResolver.resolveLocale(request)));
							return model;
						}
					}
				}
				
				//update, insert, delete 판별 
				if("insert".equals(vo.getActionType())) 
				{
					insertProdRstInfo(vo);
				}
				else if("delete".equals(vo.getActionType())) 
				{
					deleteProdRstInfo(vo);
				}
				else 
				{
					updateProdRstInfo(vo);
				}
			}
			
		} catch (DuplicateKeyException e) {
			model.addAttribute("resultCode", "E");
			model.addAttribute("resultMsg",egovMessageSource.getMessageArgsLocale("qms.msg_000019",new String[]{"Data"},localeResolver.resolveLocale(request)));
			return model;
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
	 * Insert prod rst tracking.
	 *
	 * @param vo 　
	 * @return int 　
	 * @throws Exception 　
	 */
	@Override
	public int insertProdRstTracking(ProdRstRegVO vo) throws Exception {
		return prodRstRegMapper.insertProdRstTracking(vo);
	}


	/**
	 * Send prod rst tracking.
	 *
	 * @param model 　
	 * @param voList 　
	 * @param request 　
	 * @return ModelMap 　
	 * @throws Exception 　
	 */
	@Override
	public ModelMap sendProdRstTracking(ModelMap model, List<ProdRstRegVO> voList, HttpServletRequest request) throws Exception {
		//접속자 세션정보
		MsCoreLoginVo loginUser = (MsCoreLoginVo) MsSessionUtil.getSessionAttribute(request,"loginUser");
		
		try {
			
			for(ProdRstRegVO vo : voList) {
				
				//재료번호가 Null인 경우만 처리
				if(MsCoreUtiles.isNull(vo.getMstNo())) {
				
					//로그인ID, 생산법인ID, 공장ID 설정
					vo.setCreatorId	 (loginUser.getUserId());
					vo.setModifierId (loginUser.getUserId());
					vo.setPrdtnCorpId(loginUser.getPrdtnCorpId());
					vo.setFacId		 (loginUser.getFacId());
					
					//재료번호 생성
					StringBuffer sbMstNo = new StringBuffer();
					sbMstNo.append(vo.getFacId()).append("_").append(vo.getLotNo()).append("_").append(vo.getPrdtnDt())
						.append("_").append(vo.getDoffNo()).append("_").append(vo.getPosNo());
					
					vo.setMstNo(sbMstNo.toString());		//재료번호 설정
					vo.setTrackSendDt(MsCoreDateUtil.todaYyyyMMddHHmmss());	//실적송신날짜
					
					updateProdRstInfo(vo);
					
					insertProdRstTracking(vo);
				}
			}
			
		} catch (Exception e) {
			throw e;
		}
		
		model.addAttribute("resultCode", "S");
		model.addAttribute("resultMsg", egovMessageSource.getMessage("qms.msg_000009",localeResolver.resolveLocale(request))); //정상 처리되었습니다
		return model;
	}

}
