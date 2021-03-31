

package main.service;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.itrion.msCore.common.service.CmmAttachFileService;
import com.itrion.msCore.common.vo.CmmAttachFileVo;
import com.itrion.msCore.login.vo.MsCoreLoginVo;

import msCore.util.MsCoreAttachFileUtil;
import msCore.util.MsSessionUtil;

// TODO: Auto-generated Javadoc
@Service("cmmAttachFileService")
public class CmmAttachFileServiceImpl implements CmmAttachFileService {

	@Resource(name = "cmmAttachFileMapper")
	private CmmAttachFileMapper cmmAttachFileMapper;

	/**
	 * Gets the attach file.
	 *
	 * @param vo 　
	 * @return List<CmmAttachFileVo> 　
	 * @throws Exception 　
	 */
	@Override
	public List<CmmAttachFileVo> getAttachFile(CmmAttachFileVo vo) throws Exception {
		return cmmAttachFileMapper.getAttachFile(vo);
	}
	
	/**
	 * Gets the attach file seq.
	 *
	 * @param vo 　
	 * @return List<CmmAttachFileVo> 　
	 * @throws Exception 　
	 */
	@Override
	public List<CmmAttachFileVo> getAttachFileSeq(CmmAttachFileVo vo) throws Exception {
		return cmmAttachFileMapper.getAttachFileSeq(vo);
	}

	/**
	 * Insert attach file.
	 *
	 * @param vo 　
	 * @return int 　
	 * @throws Exception 　
	 */
	@Override
	public int insertAttachFile(CmmAttachFileVo vo) throws Exception {
		return cmmAttachFileMapper.insertAttachFile(vo);
	}

	/**
	 * Update attach file.
	 *
	 * @param vo 　
	 * @return int 　
	 * @throws Exception 　
	 */
	@Override
	public int updateAttachFile(CmmAttachFileVo vo) throws Exception {
		return cmmAttachFileMapper.updateAttachFile(vo);
	}
	
	
	/**
	 * Delete whole attach file.
	 *
	 * @param voList 　
	 * @param request 　
	 * @throws Exception 　
	 */
	@Override
	public void deleteWholeAttachFile(List<CmmAttachFileVo> voList, HttpServletRequest request) throws Exception {

		//접속자 세션정보
		MsCoreLoginVo loginUser = (MsCoreLoginVo) MsSessionUtil.getSessionAttribute(request,"loginUser");
		
		//파일첨부 Util
		MsCoreAttachFileUtil attachFileUtil = new MsCoreAttachFileUtil();
		
		try {
			
			for(CmmAttachFileVo gmVo : voList) 
			{
				//서버 파일 삭제
				Boolean delYn = attachFileUtil.deleteFile(gmVo.getFileHashNm());
				
				if(delYn) {
					gmVo.setModifierId(loginUser.getUserId());	//로그인 Id 설정
					gmVo.setDelFlag("Y");						//삭제구분 : Y(삭제)
					
					//파일첨부 DB 수정
					updateAttachFile(gmVo);
				}
			}
			
		} catch (Exception e) {
			throw e;
		}
		
	}
	
	/**
	 * 파일 시퀀스로 전체 파일 삭제
	 *
	 * @param listMap 　
	 * @param request 　
	 * @throws Exception 　
	 */
	@Override
	public void deleteWholeAttachFileAll(String sFileSeq, HttpServletRequest request) throws Exception {

		//접속자 세션정보
		MsCoreLoginVo loginUser = (MsCoreLoginVo) MsSessionUtil.getSessionAttribute(request,"loginUser");
		
		//파일첨부 Util
		MsCoreAttachFileUtil attachFileUtil = new MsCoreAttachFileUtil();
		CmmAttachFileVo	voParam = new CmmAttachFileVo();
		
		try {
			
			//첨부파일 DB 조회 Param 설정
			voParam.setFileSeq(sFileSeq);
			List<CmmAttachFileVo> listVo = cmmAttachFileMapper.getAttachFile(voParam);
			
			for(CmmAttachFileVo gmVo : listVo) 
			{
				//서버 파일 삭제
				Boolean delYn = attachFileUtil.deleteFile(gmVo.getFileHashNm());
				
				if(delYn) {
					gmVo.setModifierId(loginUser.getUserId());	//로그인 Id 설정
					gmVo.setDelFlag("Y");						//삭제구분 : Y(삭제)
					
					//파일첨부 DB 수정
					updateAttachFile(gmVo);
				}
			}
			
			
		} catch (Exception e) {
			throw e;
		}
		
	}

	
}
