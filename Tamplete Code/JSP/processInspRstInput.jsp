<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- 세션정보 및 권한정보  --%>
<%@ include file="../cmm/msTopInclude.jsp" %> 

<!DOCTYPE html>
<html lang="ko" xmlns="http://www.w3.org/1999/xhtml">
<head>
<%--공통 CSS, 공통 JS 정의 ---%>
<jsp:include page="../cmm/header_inner.jsp" />

<!-- Project CSS -->
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<link rel="stylesheet" type="text/css" href="${contextPath}/pages/css/font.css" />
<link rel="stylesheet" type="text/css" href="${contextPath}/pages/css/reset.css" />
<link rel="stylesheet" type="text/css" href="${contextPath}/pages/css/contents.css" />

<script src="${contextPath}/js/jqWing/msCore.jquery.common.js"></script>

<script src="${contextPath}/pages/js/ms-ui.js"></script>

<%-- 공통코드 유틸 --%>
<script src="${contextPath}/js/cmm/cmmCdUtil.js"></script>

<%-- 파일첨부 --%>
<jsp:include page="../cmm/cmmAttachFile.jsp" />

<script type="text/javascript" src="${contextPath}/js/cmm/cmmMsc.js?ver=1.4"></script>

<script type="text/javascript">
	
	<%--공통 코드 사용시 include 한다. ※  document.ready 앞에 위치 ---%>
	<jsp:include page="../cmm/cmmCode_scriptor.jsp" />
	
	var arrSmpTpCd 	= ${lovSmpTpCd};	//시료유형Lov
	var arrSmpGrpCd = ${lovSmpGrpCd};	//시료그룹Lov
	var arrSmpCd 	= ${lovSmpCd};		//시료Lov
	var arrSmpItemCd = ${lovSmpItemCd}; //검사항목
	var gridRowindex = -1;
	
	$(document).ready(function(){
		
		<jsp:include page="../cmm/grid_scriptor.jsp" flush="false">
			<jsp:param name="gridType" value="main" />
			<jsp:param name="gridId" value="325" />
			<jsp:param name="gridLayerId" value="gridMain" />
		</jsp:include>
		
		let gridMain = pq.grid("#gridMain");
		let colMdl= gridMain.option("colModel");
		
		gridMain.on("change", function(event, ui)
		{
			ui.updateList.forEach(function(item){
				if(item.rowData.actionType != "insert") item.rowData.actionType = "update";
				if(item.newRow.state == undefined) item.rowData.state = true;
			});
		});
		
		//검사원 팝업 설정
		colMdl.find(it=>it.dataIndx == "inspUserNm").render = (ui) => {
			let rowData = ui.cellData;
			if(rowData == null)	rowData = "";
			return "<a href='javascript:fn_empInfoPopup(" + ui.rowIndx + ");' class='ui-state-default ui-corner-all'><span class='ui-icon ui-icon-search'></span></a>"+ rowData;  
		}
		
		// 불합격 색상 변경
		colMdl.find(item => item.dataIndx=="rstVal")	   .render = (ui) => {if(ui.rowData.inspItemRst == "F") return {style:{'color':'red'}}};
		colMdl.find(item => item.dataIndx=="inspItemRstNm").render = (ui) => {if(ui.rowData.inspItemRst == "F") return {style:{'color':'red'}}};

		gridMain.refreshView();
		
		//초기화
		initScreen();
	});
	
	
	<%--##############################################################
	# 코드셋팅, 날짜 셋팅등 초기화 함수는 initScreen() 안쪽에 작성
	#
	################################################################--%>
	function initScreen() {
		//검사구분 공통코드 조회
		cmmCdUtil.fn_getCmmCd("INSP_TP" ,function(data){
			//검사구분 select 설정
			cmmCdUtil.fn_setSelectData($("#selInspTpCd"),data);
		});		
		
		//시료유형 그리드 Select 설정
		cmmCdUtil.fn_setSelectData($("#selSmpTpCd"),arrSmpTpCd);
		
		//진행상태 공통코드 조회
		cmmCdUtil.fn_getCmmCd("INSP_STATUS" ,function(data){
			//진행상태 select 설정
			cmmCdUtil.fn_setSelectData($("#selInspDtlStatus"),data);
		});
	}
	
	//조회조건 시료유형 변경에 따른 시료그룹 Option 변경
	function fn_changeSmpTp(obj){
		//시료그룹 select 설정
		let sSmpTp = $(obj).val();
		let filterData = arrSmpGrpCd.filter(item=>item.highSmpCd == sSmpTp);
		cmmCdUtil.fn_setSelectData($("#selSmpGrpCd"),filterData);
		
		fn_changeSmpGrp($("#selSmpGrpCd"));
	}
	
	//조회조건 시료그룹 변경에 따른 시료 Option 변경
	function fn_changeSmpGrp(obj){
		//시료그룹 select 설정
		let sSmpTp = $(obj).val();
		let filterData = arrSmpCd.filter(item=>item.highSmpCd == sSmpTp);
		cmmCdUtil.fn_setSelectData($("#selSmpCd"),filterData);
		
		fn_changeSmpCd($("#selSmpCd"));
	}
	
	// 조회조건 시료 변경에 따른 검사항목
	function fn_changeSmpCd(obj){		
		let sSmpTp = $(obj).val();
		let filterData = arrSmpItemCd.filter(item=>item.smpCd == sSmpTp);
		cmmCdUtil.fn_setSelectData($("#selInspItem"),filterData);	
	}
		
	<%--##############################################################
	# Master 조회
	################################################################--%>
	function fn_searchOnclick() {
		var cData = {
				 inspTpCd 	: $("#selInspTpCd").val()	<%--검사구분--%>
				,inspDtlStatus	: $("#selInspDtlStatus").val()	<%--진행상태--%>
				,inspRegNm	: $("#txtInspRegNm").val()	<%--검사명--%>
				,regUserId	: $("#txtRegUserId").val()	<%--등록자--%>
				,regUserNm	: $("#txtRegUserNm").val()	<%--등록자--%>
				,prdtnDt	: $("#dtPrdtnDt").val().replace(/-/gi,"")	<%--생산일자--%>
				,regDateFr	: $("#dtRegDateFr").val().replace(/-/gi,"")	<%--등록일자From--%>
			 	,regDateTo	: $("#dtRegDateTo").val().replace(/-/gi,"")	<%--등록일자To--%>
			 	,smpTpCd	: $("#selSmpTpCd").val()	<%--시료유형--%>
				,smpGrpCd	: $("#selSmpGrpCd").val()	<%--시료그룹--%>
				,smpCd		: $("#selSmpCd").val()		<%--시료--%>
				,inspItemCd	: $("#selInspItem").val()	<%--검사항목--%>
				,lotNo		: $("#txtLotNo").val()		<%--Lot No--%>
				,doffNo		: $("#txtDoffNo").val()		<%--Doff No--%>
				,posNo		: $("#txtPosNo").val()		<%--Pos No--%>
			};
			
			let gridMain = pq.grid("#gridMain");
			
			$.ajax({
				 url	: "getProcessInspRstInput.do"
				,data	: cData
				,type	: 'POST'
				,beforeSend : function(xmlHttpRequest){
					gridMain.showLoading();
					gridMain.option("dataModel.data",[]);
					xmlHttpRequest.setRequestHeader("AJAX", "true"); // ajax 호출을  header에 기록
				}
				,success: function (result) {
					
					var data = result.resultList;
					
					if (data != undefined && data.length > 0) {
						
						gridMain.option("dataModel.data",data);
					}
					
					gridMain.refreshDataAndView();
				}
				,error:function(xhr, textStatus, error){
					if(xhr.status=="404"){ // watch
						alert("Login Session Expired");
						location.href = "${contextPath}/reDirectToMain.do";
					}
				}
				,complete : function() {
					gridMain.hideLoading();
				}
			});
	}
	
	<%--##############################################################
	# 그리드 마스터 저장
	################################################################--%>
	function fn_saveOnclick() {
		cmmMessage.confirm("<spring:message code='qms.msg_000002' text='저장 하시겠습니까?'/>", function (res) {
            if (res) {
            	fn_save(pq.grid("#gridMain"));
            }
        });
	}
	function fn_save(grid){
		
		let data = grid.option("dataModel.data");
		let gridId = grid.element[0].id;
		
		//등록,변경 데이터만 선별
		let arrData = data.filter(it => !fc_isNull(it.actionType) && it.state);
		
		if(arrData.length == 0) {
			cmmMessage.alert("<spring:message code='qms.msg_000023' text='저장 대상을 선택해주시기 바랍니다.'/>");
			return;
		}
		
		for(let idx in arrData) {
			
			//필수 항목 체크
			let sMsg = "";

// 			if(fc_isNull(arrData[idx].inspRegNm)) sMsg = ("<spring:message code='qms.msg_000004' text='{0}은(는) 필수 입력입니다.' arguments='${lb_insp_nm}'/>");
			
			if(!fc_isNull(sMsg)) {
				cmmMessage.alert(sMsg);
				return;
			}
			
			//프로그램ID 설정
			arrData[idx].modifierProgramId = arrData[idx].creatorProgramId = "processInspRstInput";
		};
		
		var cData = JSON.stringify(arrData);
		
		jQuery.ajaxSettings.traditional = true;
		
		$.ajax({
			 url		: "saveProcessInspRstInput.do"
			,data		: cData
			,type		: 'POST'
			,dataType	: "json"
			,contentType: "application/json;charset=utf-8"
			,async		: false
			,beforeSend : function(xmlHttpRequest){
				grid.showLoading();
 				xmlHttpRequest.setRequestHeader("AJAX", "true"); // ajax 호출을  header에 기록
 			}
			,success: function (result) {
				
				var resultCode = result.resultCode;
				if (resultCode == "E") {
					cmmMessage.alert("저장중 오류가 발생하였습니다.<br>" + result.resultMsg);
					return;
				} else {
					cmmMessage.alert(result.resultMsg);

					//재조회
					fn_searchOnclick();

				}
			}
			,error:function(xhr, textStatus, error){
				if(xhr.status=="404"){ // watch
					alert("Login Session Expired");
					location.href = "${contextPath}/reDirectToMain.do";
				}
			}
			,complete : function() {
				grid.hideLoading();
			}
		}); 
	}
	
	<%--##############################################################
	# 판정확정, 취소
	################################################################--%>
	function fn_judgConf(flag){
		
		let grid = pq.grid("#gridMain")
		let data = grid.option('dataModel.data');
		let filterData = data.filter(it=> it.state);	
		
		if(filterData.length == 0) {
			cmmMessage.alert("<spring:message code='qms.msg_000026' text='대상을 선택해주시기 바랍니다.'/>");
			return;
		} 
		
		let message;
			 if(flag == "conf") 	message = "<spring:message code='qms.msg_000027' text='확정 하시겠습니까?'/>";
		else if(flag == "cancel") 	message = "<spring:message code='qms.msg_000028' text='확정취소 하시겠습니까?'/>";
		
		cmmMessage.confirm(message, (res) => {
			if (res) {
				// 중복 검사등록번호/시료별검사번호 제거
				let confData = [];
				for(let idx in filterData){ 
					if(confData.find(item => item.inspRegNo == filterData[idx].inspRegNo 
										  && item.inspSmpNo == filterData[idx].inspSmpNo) == undefined) {
						confData.push(filterData[idx]);
					}
				}
				
				for(let idx in confData){ 
					// 진행상테  A:검사등록 B:결과입력 C:판정완료
						 if(flag == "conf") 	confData[idx].inspDtlStatus = "C";
					else if(flag == "cancel") 	confData[idx].inspDtlStatus = "B";
				
					//프로그램ID 설정
					confData[idx].modifierProgramId = "processInspRstInput";
				}
				
				let cData = JSON.stringify(confData);
				
				jQuery.ajaxSettings.traditional = true;
				
				$.ajax({
					 url		: "saveProcessInspJudgConf.do"
					,data		: cData
					,type		: 'POST'
					,dataType	: "json"
					,contentType: "application/json;charset=utf-8"
					,async		: false
					,beforeSend : function(xmlHttpRequest){
						grid.showLoading();
		 				xmlHttpRequest.setRequestHeader("AJAX", "true"); // ajax 호출을  header에 기록
		 			}
					,success: function (result) {
						
						var resultCode = result.resultCode;
						if (resultCode == "E") {
							cmmMessage.alert("저장중 오류가 발생하였습니다.<br>" + result.resultMsg);
							return;
						} else {
							cmmMessage.alert(result.resultMsg);

							//재조회
							fn_searchOnclick();
						}
					}
					,error:function(xhr, textStatus, error){
						if(xhr.status=="404"){ // watch
							alert("Login Session Expired");
							location.href = "${contextPath}/reDirectToMain.do";
						}
					}
					,complete : function() {
						grid.hideLoading();
					}
				}); 
			}
  		});
   		

	}
	
	<%--##############################################################
	# 직원정보 PopUp
	################################################################--%>
    function fn_empInfoPopup(idx) {
    	gridRowindex = idx;
 		gfn_empInfoPopUp("divUserPopUp", "fn_setEmpInfo", "<%=btnAuthGroup%>");
    }
    // 직원정보 추가
	function fn_setEmpInfo(rtn) {
    	
    	if(fc_isNull(gridRowindex)){
    		$("#txtRegUserNm").val(rtn.rowData.userNm);
    		$("#txtRegUserId").val(rtn.rowData.userId);
    	} else {
    		let grid = pq.grid("#gridMain");
        	let data = grid.option("dataModel.data");
        	
        	data[gridRowindex].inspUserId = rtn.rowData.userId;
        	data[gridRowindex].inspUserNm = rtn.rowData.userNm;
    		data[gridRowindex].state = true;
    		
    		grid.option("dataModel.data",data);
    		grid.refreshDataAndView();
    	}
	}
    //등록자명 수정시
	function fn_chgRegUserNm(){
		$("#txtRegUserId").val("");
	}
	
</script>

</head>

	
<body>
	
	<div id="content" class="sub_content">
		<div class="search-area">
			<fieldset>
				<div>
					<table class="table" summary="">
						<tbody>
							<tr>
								<th><spring:message code='qms.lb_insp_tp_nm' text='검사구분'/></th>
								<td style="width:120px">
									<select id="selInspTpCd" class="form-control">
										<option value=""><spring:message code='qms.lb_all' text='전체'/></option>
									</select>
								</td>
								<th><spring:message code='qms.lb_progress' text='진행상태'/></th>
								<td style="width:120px">
									<select id="selInspDtlStatus" class="form-control">
										<option value=""><spring:message code='qms.lb_all' text='전체'/></option>
									</select>
								</td>
								<th><spring:message code='qms.lb_insp_nm' text='검사명'/></th>
								<td style="width:120px">
									<input type="text" id="txtInspRegNm" value="" class="form-control" style="width:120px">
								</td>
								<th><spring:message code='qms.lb_creator' text='등록자'/></th>
								<td style="width:120px">
									<input type="text" id="txtRegUserNm" value="" class="form-control" style="width:80px" onchange="fn_chgRegUserNm();">
									<input type="text" id="txtRegUserId" class="form-control" style="display: none;">
									<button type="button" class="btn btn-default" onclick="fn_empInfoPopup(null);"><i class="fa fa-search"></i></button>
								</td>
								<th><spring:message code='qms.lb_prdtn_dt' text='생산일자'/></th>
								<td>
									<input type="date" id="dtPrdtnDt" class="form-control" style="width:150px;" value="${toDt}">
								</td>
								<th><spring:message code='qms.lb_reg_date' text='등록일자'/></th>
								<td colspan=3>
									<input type="date" id="dtRegDateFr" class="form-control" style="width:150px;" value="${fromDt}"> ~ 
									<input type="date" id="dtRegDateTo" class="form-control" style="width:150px;" value="${toDt}">
								</td>																															
							</tr>
							<tr>
								<th><spring:message code='qms.lb_smp_cd_type' text='시료유형'/></th>
								<td style="width:120px">
									<select id="selSmpTpCd" class="form-control" onchange="fn_changeSmpTp(this);">
										<option value=""><spring:message code='qms.lb_all' text='전체'/></option>
									</select>
								</td>
								<th><spring:message code='qms.lb_smp_cd_group' text='시료그룹'/></th>
								<td style="width:120px">
									<select id="selSmpGrpCd" class="form-control" onchange="fn_changeSmpGrp(this);">
										<option value=""><spring:message code='qms.lb_all' text='전체'/></option>
									</select>
								</td>
								<th><spring:message code='qms.lb_smp_cd' text='시료'/></th>
								<td style="width:120px">
									<select id="selSmpCd" class="form-control" onchange="fn_changeSmpCd(this);">
										<option value=""><spring:message code='qms.lb_all' text='전체'/></option>
									</select>
								</td>
								<th><spring:message code='qms.lb_insp_item_nm' text='검사항목'/></th>
								<td style="width:120px">
									<select id="selInspItem" class="form-control">
										<option value=""><spring:message code='qms.lb_all' text='전체'/></option>
									</select>
								</td>
								<th><spring:message code='qms.lb_lot_no' text='Lot No.'/></th>
								<td>
									<input type="text" id="txtLotNo" value="" class="form-control" style="width:150px">
								</td>
								<th><spring:message code='qms.lb_doff_no' text='Doff. No.'/></th>
								<td>
									<input type="text" id="txtDoffNo" value="" class="form-control" style="width:120px">
								</td>
								<th><spring:message code='qms.lb_pos_end' text='Pos/End'/></th>
								<td>
									<input type="text" id="txtPosNo" value="" class="form-control" style="width:120px">
								</td>																
							</tr>
						</tbody>
					</table>
					<jsp:include page="../cmm/msStdBtnInclude.jsp" flush="true" >
		                <jsp:param name="buttonList"        value="I" />
		                <jsp:param name="buttonIdentity"    value="" />
		                <jsp:param name="buttonAuthGroup"   value="<%=btnAuthGroup%>" />
		            </jsp:include>
				</div>
			</fieldset>
		</div>
		
		<div class="ms-grid-wrap">
			<div class="ms-grid center-area">
				<div class="btn-box">
					<jsp:include page="../cmm/msStdBtnInclude.jsp" flush="true" >
		                <jsp:param name="buttonList"        value="S" />
		                <jsp:param name="buttonIdentity"    value="" />
		                <jsp:param name="buttonAuthGroup"   value="<%=btnAuthGroup%>" />
		                
		                <jsp:param name="buttonIds"           value="btnConfirm" />
						<jsp:param name="buttonNms"           value="${lb_JudgConf}" />
						<jsp:param name="buttonActions"       value="javascript:fn_judgConf('conf');" />
						<jsp:param name="buttonChkAuthGroups" value="CUST1" />
						<jsp:param name="buttonAuthGroups"    value="<%=btnAuthGroup%>" />
						
						<jsp:param name="buttonIds"           value="btnCancel" />
						<jsp:param name="buttonNms"           value="${lb_JudgConfCancel}" />
						<jsp:param name="buttonActions"       value="javascript:fn_judgConf('cancel');" />
						<jsp:param name="buttonChkAuthGroups" value="CUST1" />
						<jsp:param name="buttonAuthGroups"    value="<%=btnAuthGroup%>" />
		            </jsp:include>
	            </div>
				<div id="gridMain"></div>
			</div>
		</div>
	</div>

	<%--Modal START--%>
	<div title="<spring:message code="core.lb_emp_info" text="직원정보"/>" id="divUserPopUp" style="overflow: hidden; display: none;"></div>
	<%--Modal END--%>	

</body>
	
</html>
