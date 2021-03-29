<%--==============================================================================
*Copyright(c) 2021 HYOSUNG  ADVANCED  MATERIALS Co., Ltd. / ITRION Co., Ltd.  
*
*@ProcessChain : 물성검사등록
*
*@File	 : processInspReg.jsp
*
*@FileName : 물성검사등록 JSP
*
* Date             Ver.     Name     Description
*-----------------------------------------------------------------------------
* 2021.02.25		1.00	  KBS	 최초 생성
==============================================================================--%>

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

<script type="text/javascript">
	
	<%--############키캡처 이벤트  처리 ###################--%>
// 	$(window).bind('keydown', function(event) {
// 		if (event.ctrlKey || event.metaKey) {
// 			switch (String.fromCharCode(event.which).toLowerCase()) {
// 			case 's':
// 				event.preventDefault();
// 				fn_saveOnclick();
// 				break;
// 			}
// 		}
// 	});
	
	var nGrdMstBfIdx   = -1; 	//그리드 이전 클릭 Index
	var nGrdDtlBfIdx   = -1; 	//그리드 이전 클릭 Index
	var arrSmpTpCd 	   = ${lovSmpTpCd};	    //시료유형Lov
	var arrSmpGrpCd    = ${lovSmpGrpCd};	//시료그룹Lov
	var arrSmpCd       = ${lovSmpCd};		//시료Lov
	var arrSmpGrpPrdpc = ${lovSmpGrpPrdpc}; //공정Lov
	var arrInspItem	   = [];				//검사항목Lov
	var arrDayTp	   = [];				//요일구분
	
	<%--공통 코드 사용시 include 한다. ※  document.ready 앞에 위치 ---%>
	<jsp:include page="../cmm/cmmCode_scriptor.jsp" />
	
	$(document).ready(function(){
		
		<jsp:include page="../cmm/grid_scriptor.jsp" flush="false">
			<jsp:param name="gridType" value="main" />
			<jsp:param name="gridId" value="316" />
			<jsp:param name="gridLayerId" value="gridInspMst" />
		</jsp:include>
		
		<jsp:include page="../cmm/grid_scriptor.jsp" flush="false">
			<jsp:param name="gridType" value="main" />
			<jsp:param name="gridId" value="317" />
			<jsp:param name="gridLayerId" value="gridInspDtl" />
		</jsp:include>
		
		<jsp:include page="../cmm/grid_scriptor.jsp" flush="false">
			<jsp:param name="gridType" value="main" />
			<jsp:param name="gridId" value="318" />
			<jsp:param name="gridLayerId" value="gridInspRst" />
		</jsp:include>
		
		<jsp:include page="../cmm/grid_scriptor.jsp" flush="false">
			<jsp:param name="gridType" value="modal" />
			<jsp:param name="gridId" value="320" />
			<jsp:param name="gridLayerId" value="gridInspPlan" />
		</jsp:include>
		
		var gridInspMst  = pq.grid("#gridInspMst");
		var gridInspDtl  = pq.grid("#gridInspDtl");
		var gridInspRst  = pq.grid("#gridInspRst");
		var gridInspPlan = pq.grid("#gridInspPlan");
		var colMdlMst = gridInspMst.option("colModel");
		var colMdlDtl = gridInspDtl.option("colModel");
		
		//컬럼 포인터 및 색상 변경
		colMdlMst.find(item => item.dataIndx=="inspRegNm").style = "cursor:pointer;color:#004899;font-weight:500;";
		colMdlDtl.find(item => item.dataIndx=="doffNo").style = "cursor:pointer;color:#004899;font-weight:500;";
		colMdlDtl.find(item => item.dataIndx=="fileCnt").style = "cursor:pointer;color:#004899;font-weight:500;";
		
		//신규 등록인 경우만 수정 가능
		colMdlMst.find(item => item.dataIndx=="smpTpCd_front").editable = ui => ui.rowData.actionType == "insert";
		colMdlMst.find(item => item.dataIndx=="smpGrpCd_front").editable = ui => ui.rowData.actionType == "insert";
		
		//Pos/End정보 양식안내
		colMdlDtl.find(item => item.dataIndx=="posInfo").editor = {attr:"placeholder = 'ex)1,2,3~5'"};
		
		// 그리드 이벤트 설정
		gridInspMst.on("cellClick",function(event, ui) {
			
			if(ui.dataIndx == "inspRegNm") {
				//이전 선택 Row Index 설정
				nGrdMstBfIdx = ui.rowIndx;
				// Detail 조회
				searchDtl();	
			}
		});
		
		gridInspDtl.on("cellClick",function(event, ui) {
			if(ui.dataIndx == "doffNo") {
				//이전 선택 Row Index 설정
				nGrdDtlBfIdx = ui.rowIndx;
				// Rst 조회
				searchRst();	
			}else if(ui.dataIndx == "fileCnt") {
				//파일첨부 팝업
				fn_attachFilePop(ui);
			}
		});
		
		gridInspMst.on("change", fn_chgEvt);
		gridInspDtl.on("change", fn_chgEvt);
		gridInspRst.on("change", function(event, ui)
		{
			ui.updateList.forEach(function(item){
				if(item.rowData.actionType != "insert") item.rowData.actionType = "update";
				
				//검사항목 변경시 값 설정
				if(!fc_isNull(item.newRow.inspItemCd)) {
					let findData = arrInspItem.find(it => it.cdVal == item.newRow.inspItemCd);
					item.rowData.uomCd = findData.uomCd;
					item.rowData.uomNm = findData.uomNm;
					item.rowData.frmlNm = findData.frmlNm;
					item.rowData.frmlNo = findData.frmlNo;
					item.rowData.specAim = findData.shipSpecAim;
					item.rowData.specMax = findData.shipSpecMax;
					item.rowData.specMin = findData.shipSpecMin;
				}
			});
		});
		
		//팝업 그리드 더블클릭 이벤트
		gridInspPlan.on("cellDblClick",function(event, ui) {
			
			let mstGrid  = pq.grid("#gridInspMst");
			let mstData = mstGrid.option("dataModel.data");
			let addData = {};
			addData.state      = true;
			addData.actionType = "insert";
			addData.inspTpCd   = ui.rowData.inspTpCd;	/*검사유형*/
			addData.inspRegNm  = ui.rowData.inspPlanNm;	/*검사명*/
			addData.smpTpCd    = ui.rowData.smpTpCd;	/*시료유형*/
			addData.smpGrpCd   = ui.rowData.smpGrpCd;	/*시료구분*/
			addData.inspPlanNo = ui.rowData.inspPlanNo; /*검사계획번호*/
			mstData.push(addData);
			gridInspMst.option("dataModel.data",mstData);
			cmmCdUtil.fn_setGridSelText("gridInspMst");
			gridInspMst.refreshView();
			
			$("#popupInspPlan").dialog("close");
		});


		//select 한번 클릭으로 에딧모드 참고용
// 		gridInspMst.option("editModel",{clicksToEdit: 1 });
// 		gridInspDtl.option("editModel",{clicksToEdit: 1 });
// 		gridInspRst.option("editModel",{clicksToEdit: 1 });
		
// 		gridInspDtl.on("cellClick", function(event, ui)
// 		{
// 			console.log(ui);
// 			if(ui.dataIndx == "prdpcId_front" || ui.dataIndx == "smpCd_front") {
// 				this.editCell( { rowIndx: ui.rowIndx, dataIndx: ui.dataIndx } );
// 			}
// 		});
		
		
		gridInspMst.refreshView();
		gridInspDtl.refreshView();
		
		//초기화
		initScreen();
	});
	
	//그리드 변경 이벤트
	function fn_chgEvt(event, ui) {
		ui.updateList.forEach(function(item){
			if(item.rowData.actionType != "insert") item.rowData.actionType = "update";
		});
	}
	
	
	<%--##############################################################
	# 코드셋팅, 날짜 셋팅등 초기화 함수는 initScreen() 안쪽에 작성
	#
	################################################################--%>
	function initScreen() {
		
		//시료유형 그리드 Select 설정
		cmmCdUtil.fn_setGridSelectData("gridInspMst","smpTpCd_front",arrSmpTpCd);
		
		//시료유형 select 설정
		cmmCdUtil.fn_setSelectData($("#selSmpTpCd"),arrSmpTpCd);
		
		//시료그룹 그리드 Select 설정
		cmmCdUtil.fn_setGridSelectFn("gridInspMst","smpGrpCd_front",fn_setSelSmpGrp,arrSmpGrpCd);
		
		//시료 그리드 Select 설정
		cmmCdUtil.fn_setGridSelectFn("gridInspDtl","smpCd_front",fn_setSelSmp,arrSmpCd);
		
		//공정 그리드 Select 설정
		cmmCdUtil.fn_setGridSelectFn("gridInspDtl","prdpcId_front",fn_setSelPrdpc,arrSmpGrpPrdpc);
		
		//검사구분 공통코드 조회
		cmmCdUtil.fn_getCmmCd("INSP_TP" ,function(data){
			//검사구분 그리드 select 설정
			cmmCdUtil.fn_setGridSelectData("gridInspMst","inspTpCd_front",data);
			
			//검사구분 select 설정
			cmmCdUtil.fn_setSelectData($("#selInspTpCd"),data);
			
			//팝업 검사구분 select 설정
			cmmCdUtil.fn_setSelectData($("#selPopInspTpCd"),data);
		});
		
		//진행상태 공통코드 조회
		cmmCdUtil.fn_getCmmCd("INSP_STATUS" ,function(data){
			//진행상태 select 설정
			cmmCdUtil.fn_setSelectData($("#selInspStatus"),data);
		});
		
		//유제 공통코드 조회
		cmmCdUtil.fn_getCmmCd("EMUL_CD" ,function(data){
			//유제 그리드 select 설정
			cmmCdUtil.fn_setGridSelectData("gridInspDtl","emulCd_front",data);
		});
		
		//요일구분 공통코드 조회
		cmmCdUtil.fn_getCmmCd("DAY_TP" ,function(data){
			//요일구분 전역변수 선언[계획 팝업에서 사용]
			arrDayTp = data;

			//팝업 요일구분 select 설정
			cmmCdUtil.fn_setSelectData($("#selPopDayTpCd"),data);
		});
		
		//Rst 시료 그리드 select 설정
		cmmCdUtil.fn_setGridSelectData("gridInspRst","smpCd_front",arrSmpCd);
		
		//그리드 select 이벤트 설정
		cmmCdUtil.fn_setGridSelEvt("gridInspMst");
		cmmCdUtil.fn_setGridSelEvt("gridInspDtl");
		
		//조회기간 기본 설정
		let toDay = new Date();
		let yyyy = toDay.getFullYear().toString();
		let MM = String(toDay.getMonth()+1).padStart(2,"0");
// 		let frDd = String(toDay.getDate()-1).padStart( 2,"0") ;
		let toDd = String(toDay.getDate()).padStart( 2,"0") ;
		
		$("#dtRegDateFr").val(yyyy+"-"+MM+"-"+"01");
		$("#dtRegDateTo").val(yyyy+"-"+MM+"-"+toDd);
	}
	function fn_setSelSmpGrp(obj){
		//시료유형에 따른 시료그룹 Lov 필터
		return arrSmpGrpCd.filter(item => item.highSmpCd == obj.rowData.smpTpCd);
	}
	function fn_setSelSmp(obj){
		//시료그룹에 따른 시료 Lov 필터
		return arrSmpCd.filter(item => item.highSmpCd == obj.rowData.smpGrpCd);
	}
	function fn_setSelPrdpc(obj){
		//시료그룹에 따른 공정 Lov 필터
		return arrSmpGrpPrdpc.filter(item => item.smpGrpCd == obj.rowData.smpGrpCd);
	}
	//조회조건 시료유형 변경에 따른 시료그룹 Option 변경
	function fn_changeSmpTp(obj){
		//시료그룹 select 설정
		let sSmpTp = $(obj).val();
		let filterData = arrSmpGrpCd.filter(item => item.highSmpCd == sSmpTp);
		cmmCdUtil.fn_setSelectData($("#selSmpGrpCd"),filterData);
	}
	
	<%--##############################################################
	# Master 조회
	################################################################--%>
	function fn_searchOnclick() {
		var cData = {
			 inspTpCd 	: $("#selInspTpCd").val()	<%--검사구분--%>
			,inspStatus	: $("#selInspStatus").val()	<%--진행상태--%>
			,smpTpCd	: $("#selSmpTpCd").val()	<%--시료유형--%>
			,smpGrpCd	: $("#selSmpGrpCd").val()	<%--시료그룹--%>
		 	,inspRegNm	: $("#txtInspRegNm").val()	<%--검사명--%>
		 	,regUser	: $("#txtRegUser").val()	<%--등록자--%>
		 	,regDateFr	: $("#dtRegDateFr").val().replace(/-/gi,"")	<%--등록일자From--%>
		 	,regDateTo	: $("#dtRegDateTo").val().replace(/-/gi,"")	<%--등록일자To--%>
		};
		
		let gridInspMst = pq.grid("#gridInspMst");
		let gridInspDtl = pq.grid("#gridInspDtl");
		let gridInspRst = pq.grid("#gridInspRst");
		
		$.ajax({
			 url	: "getProcessInspMast.do"
			,data	: cData
			,type	: 'POST'
			,beforeSend : function(xmlHttpRequest){
				gridInspMst.showLoading();
				gridInspMst.option("dataModel.data",[]);
				gridInspDtl.option("dataModel.data",[]).refreshView();
				gridInspRst.option("dataModel.data",[]).refreshView();
				xmlHttpRequest.setRequestHeader("AJAX", "true"); // ajax 호출을  header에 기록
			}
			,success: function (result) {
				
				var data = result.resultList;
				
				if (data != undefined && data.length > 0) {
					
					gridInspMst.option("dataModel.data",data);
				}
				
				//select 데이터 설정
                cmmCdUtil.fn_setGridSelText("gridInspMst");
				
				gridInspMst.refreshDataAndView();
			}
			,error:function(xhr, textStatus, error){
				if(xhr.status=="404"){ // watch
					alert("Login Session Expired");
					location.href = "${contextPath}/reDirectToMain.do";
				}
			}
			,complete : function() {
				gridInspMst.hideLoading();
				
				//이전 그리드 선택 Index 초기화
				nGrdMstBfIdx = -1;
				nGrdDtlBfIdx = -1;
				
				//행삭제 대상 초기화
				deleteMst = [];
				deleteDtl = [];
				deleteRst = [];
			}
		});
	}
	
	<%--##############################################################
	# Detail 조회
	################################################################--%>
	function searchDtl() {
		
		let gridInspMst = pq.grid("#gridInspMst");
		let gridInspDtl = pq.grid("#gridInspDtl");
		let gridInspRst = pq.grid("#gridInspRst");
		let dataMst 	= gridInspMst.option("dataModel.data");
		
		let cData = dataMst[nGrdMstBfIdx];
		
		//Master가 저장되지 않은 경우
		if(cData.inspRegNo == undefined) {
			gridInspDtl.option("dataModel.data",[]);
			gridInspDtl.refreshView();
			return;
		}
		
		$.ajax({
			 url	: "getProcessInspDtl.do"
			,data	: cData
			,type	: 'POST'
			,beforeSend : function(xmlHttpRequest){
				gridInspMst.showLoading();
				gridInspDtl.showLoading();
				gridInspRst.showLoading();
				gridInspDtl.option("dataModel.data",[]);
				gridInspRst.option("dataModel.data",[]).refreshView();
				xmlHttpRequest.setRequestHeader("AJAX", "true"); // ajax 호출을  header에 기록
			}
			,success: function (result) {

				if (result.resultCode == "E") {
					cmmMessage.alert(result.resultMsg);
				}else{
					let resultList = result.resultList;
					if (resultList != undefined && resultList.length > 0) {
						gridInspDtl.option("dataModel.data", resultList);
					}
				}
				//select 데이터 설정
                cmmCdUtil.fn_setGridSelText("gridInspDtl");
				
				gridInspDtl.refreshDataAndView();
			}
			,error:function(xhr, textStatus, error){
				if(xhr.status=="404"){ // watch
					alert("Login Session Expired");
					location.href = "${contextPath}/reDirectToMain.do";
				}
			}
			,complete : function() {
				gridInspMst.hideLoading();
				gridInspDtl.hideLoading();
				gridInspRst.hideLoading();
				
				//이전 클릭 Index 초기화
				nGrdDtlBfIdx = -1;
				
				//행삭제 대상 초기화
				deleteDtl = [];
				deleteRst = [];
			}
		});
		
		
	}
	
	<%--##############################################################
	# 시료별 검사항목 조회
	################################################################--%>
	function searchRst(){
		let gridInspMst = pq.grid("#gridInspDtl");
		let gridInspDtl = pq.grid("#gridInspDtl");
		let gridInspRst = pq.grid("#gridInspRst");
		let dataDtl 	= gridInspDtl.option("dataModel.data");
		
		let cData = dataDtl[nGrdDtlBfIdx];
		
		//Dtl이 저장되지 않은 경우
		if(cData.inspSmpNo == undefined) {
			gridInspRst.option("dataModel.data",[]).refreshView();
			return;
		}
		
		$.ajax({
			 url	: "getProcessSmpInspItem.do"
			,data	: cData
			,type	: 'POST'
			,beforeSend : function(xmlHttpRequest){
				gridInspMst.showLoading();
				gridInspDtl.showLoading();
				gridInspRst.showLoading();
				gridInspRst.option("dataModel.data",[]);
				xmlHttpRequest.setRequestHeader("AJAX", "true"); // ajax 호출을  header에 기록
			}
			,success: function (result) {

				if (result.resultCode == "E") {
					cmmMessage.alert(result.resultMsg);
				}else{
					let resultList = result.resultList;
					arrInspItem = result.lovInspItem;
					if (resultList != undefined && resultList.length > 0) {
						gridInspRst.option("dataModel.data", resultList);
					}
					
					//검사항목코드 그리드 Select 설정
					cmmCdUtil.fn_setGridSelectData("gridInspRst","inspItemCd_front",arrInspItem);

					//그리드 select 이벤트 설정
					cmmCdUtil.fn_setGridSelEvt("gridInspRst");
					
					//select 데이터 설정
	                cmmCdUtil.fn_setGridSelText("gridInspRst");
				}
				//select 데이터 설정
                cmmCdUtil.fn_setGridSelText("gridInspRst");
				
                gridInspRst.refreshDataAndView();
			}
			,error:function(xhr, textStatus, error){
				if(xhr.status=="404"){ // watch
					alert("Login Session Expired");
					location.href = "${contextPath}/reDirectToMain.do";
				}
			}
			,complete : function() {
				gridInspMst.hideLoading();
				gridInspDtl.hideLoading();
				gridInspRst.hideLoading();
				
				//행삭제 대상 초기화
				deleteRst = [];
			}
		});
		
		
	}
		
	<%--##############################################################
	# 그리드마스터 행 추가
	################################################################--%>
	function fn_addRowOnclickMst(){
		addRow(pq.grid("#gridInspMst"));
	}
	function fn_addRowOnclickDtl(){
		if(nGrdMstBfIdx < 0) {
			cmmMessage.alert("<spring:message code='qms.msg_000005' text='{0}을(를) 선택하세요.' arguments='${lb_insp_nm}'/>");
			return;
		}
		addRow(pq.grid("#gridInspDtl"));
	}
	function fn_addRowOnclickRst(){
		if(nGrdDtlBfIdx < 0) {
			cmmMessage.alert("<spring:message code='qms.msg_000005' text='{0}을(를) 선택하세요.' arguments='${lb_doff_no}'/>");
			return;
		}
		addRow(pq.grid("#gridInspRst"));
	}
	function addRow(grid){
		
		let gid = grid.element[0].id;
		let data = grid.option("dataModel.data");
		let sInspRegNo, sSmpGrpCd, sInspTpCd, sInspSmpNo, sSmpCd, sLotNo, sDoffNo, sSmpNm;
		
		if(gid == "gridInspDtl") {
			let mstData = pq.grid("#gridInspMst").option("dataModel.data")[nGrdMstBfIdx];
			if(!fc_isNull(mstData.actionType)){
				cmmMessage.alert("<spring:message code='qms.msg_000022' text='{0}를(을) 먼저 저장해주시기 바랍니다.' arguments='${lb_smp_cd_group}'/>");
				return;
			}
			sInspRegNo = mstData.inspRegNo;	//검사등록번호
			sSmpGrpCd  = mstData.smpGrpCd;	//시료그룹코드
			sInspTpCd  = mstData.inspTpCd;	//검사구분
		} else if(gid == "gridInspRst") {
			let dtlData = pq.grid("#gridInspDtl").option("dataModel.data")[nGrdDtlBfIdx];
			if(!fc_isNull(dtlData.actionType)){
				cmmMessage.alert("<spring:message code='qms.msg_000022' text='{0}를(을) 먼저 저장해주시기 바랍니다.' arguments='${lb_smp_dtl}'/>");
				return;
			}
			if(fc_isNull(dtlData.lotNo)){
				cmmMessage.alert("<spring:message code='qms.msg_000004' text='{0}은(는) 필수 입력입니다.' arguments='${lb_lot_no}'/>");
				return;
			}
			if(fc_isNull(dtlData.doffNo)){
				cmmMessage.alert("<spring:message code='qms.msg_000004' text='{0}은(는) 필수 입력입니다.' arguments='${lb_doff_no}'/>");
				return;
			}
			sInspRegNo = dtlData.inspRegNo;	//검사등록번호
			sSmpGrpCd  = dtlData.smpGrpCd;	//시료그룹코드
			sInspSmpNo = dtlData.inspSmpNo;	//시료별검사번호
			sSmpCd 	   = dtlData.smpCd;		//시료코드
			sSmpNm 	   = dtlData.smpCd_front; //시료명
			sLotNo 	   = dtlData.lotNo;		//Lot No
			sDoffNo    = dtlData.doffNo;	//Doff No
		}
		
		data.push({  actionType : "insert"
					,state 		: true
					,inspRegNo  : sInspRegNo
					,smpGrpCd	: sSmpGrpCd
					,inspTpCd	: sInspTpCd
					,inspSmpNo	: sInspSmpNo 
					,smpCd		: sSmpCd 	 
					,lotNo		: sLotNo 	 
					,doffNo		: sDoffNo    
					,smpCd_front: sSmpNm
				  });
		
		grid.option("dataModel.data",data);
		grid.refreshView();
	}
	
	<%--##############################################################
	# 그리드마스터 행삭제
	################################################################--%>	
	var deleteMst = new Array; // 그리드변경후 삭제대상건 저장을 위한 변수 
	var deleteDtl = new Array; // 그리드변경후 삭제대상건 저장을 위한 변수 
	var deleteRst = new Array; // 그리드변경후 삭제대상건 저장을 위한 변수 
	function fn_deleteRowOnclickMst(){
		deleteRow(pq.grid("#gridInspMst"),deleteMst);
		//상위 그리드 삭제시 하위 그리드 및 이전 클릭 그리드 Index초기화 
		nGrdMstBfIdx = -1;
		pq.grid("#gridInspDtl").option("dataModel.data",[]).refreshView();
	}
	function fn_deleteRowOnclickDtl(){
		deleteRow(pq.grid("#gridInspDtl"),deleteDtl);
		//상위 그리드 삭제시 하위 그리드 및 이전 클릭 그리드 Index초기화 
		nGrdDtlBfIdx = -1;
		pq.grid("#gridInspRst").option("dataModel.data",[]).refreshView();
	}
	function fn_deleteRowOnclickRst(){
		deleteRow(pq.grid("#gridInspRst"),deleteRst);
	}
	function deleteRow(grid,arrDel) {
		
		let gridId = grid.element[0].id;
		let data = grid.option("dataModel.data");
 		let arrDelIdx = new Array();
 		
		data.forEach((item,i) => {
			if(item.state) {
				arrDelIdx.push({rowIndx:i});
				if (item.actionType != "insert") {
					arrDel.push(item);
				}
			}
		});
		
		grid.deleteRow({rowList:arrDelIdx});
	}
	
	<%--##############################################################
	# 행복사
	################################################################--%>
	function fn_copyMst(){
		fn_copyRow(pq.grid("#gridInspMst"));
	}
	function fn_copyDtl(){
		fn_copyRow(pq.grid("#gridInspDtl"));
	}
	function fn_copyRow(grid){
		
		let gridId = grid.element[0].id;
		let data = grid.option("dataModel.data");
		let chkData = JSON.parse(JSON.stringify(data.filter(item => item.state)));
		
		chkData.forEach(item => {
			item.state = true;
			item.actionType = "insert";
			item.inspStatus = "";
			item.inspStatus_front = "";
			if(gridId == "gridInspMst") item.inspRegNo = "";
			if(gridId == "gridInspDtl") item.inspSmpNo = "";
			data.push(item);
		});
		
		grid.refreshView();
	}
	
	<%--##############################################################
	# 그리드 마스터 저장
	################################################################--%>
	function fn_saveOnclickMst() {
		cmmMessage.confirm("<spring:message code='qms.msg_000002' text='저장 하시겠습니까?'/>", function (res) {
            if (res) {
            	fn_save(pq.grid("#gridInspMst"));
            }
        });
	}
	function fn_saveOnclickDtl() {
		cmmMessage.confirm("<spring:message code='qms.msg_000002' text='저장 하시겠습니까?'/>", function (res) {
            if (res) {
            	fn_save(pq.grid("#gridInspDtl"));
            }
        });
	}
	function fn_saveOnclickRst() {
		cmmMessage.confirm("<spring:message code='qms.msg_000002' text='저장 하시겠습니까?'/>", function (res) {
            if (res) {
            	fn_save(pq.grid("#gridInspRst"));
            }
        });
	}
	function fn_save(grid){
		
		let data = grid.option("dataModel.data");
		let gridId = grid.element[0].id;
		let arrDel, sUrl;
		
		 if(gridId == "gridInspMst") {
			 arrDel = deleteMst;
			 sUrl	= "saveProcessInspMast.do";
		}
		else if(gridId == "gridInspDtl") {
			arrDel	= deleteDtl;
			sUrl	= "saveProcessInspDtl.do";
		}
		else if(gridId == "gridInspRst") {
			arrDel	= deleteRst;
			sUrl	= "saveProcessInspRst.do";
		}
		 
		
		// 삭제 데이터 설정
		if (arrDel.length > 0) {
			arrDel.forEach(function(item){
				item.actionType = "delete";
				item.state = true;
				data.push(item);
			});
		}
		
		//등록,변경 데이터만 선별
		var arrData = data.filter(it => !fc_isNull(it.actionType) && it.state);
		
		if(arrData.length == 0) {
			cmmMessage.alert("<spring:message code='qms.msg_000023' text='저장 대상을 선택해주시기 바랍니다.'/>");
			return;
		}
		
		for(let idx in arrData) {
			
			//필수 항목 체크
			let sMsg = "";
			if(gridId=="gridInspMst") {
					 if(fc_isNull(arrData[idx].inspRegNm)) sMsg = ("<spring:message code='qms.msg_000004' text='{0}은(는) 필수 입력입니다.' arguments='${lb_insp_nm}'/>");
				else if(fc_isNull(arrData[idx].inspTpCd )) sMsg = ("<spring:message code='qms.msg_000004' text='{0}은(는) 필수 입력입니다.' arguments='${lb_insp_tp_nm}'/>");
				else if(fc_isNull(arrData[idx].smpTpCd  )) sMsg = ("<spring:message code='qms.msg_000004' text='{0}은(는) 필수 입력입니다.' arguments='${lb_smp_cd_type}'/>");
				else if(fc_isNull(arrData[idx].smpGrpCd )) sMsg = ("<spring:message code='qms.msg_000004' text='{0}은(는) 필수 입력입니다.' arguments='${lb_smp_cd_group}'/>");
			}else if(gridId=="gridInspDtl"){
				if(fc_isNull(arrData[idx].smpCd)) sMsg = ("<spring:message code='qms.msg_000004' text='{0}은(는) 필수 입력입니다.' arguments='${lb_smp_cd}'/>");
				else if(fc_isNull(arrData[idx].lotNo)) sMsg = ("<spring:message code='qms.msg_000004' text='{0}은(는) 필수 입력입니다.' arguments='${lb_lot_no}'/>");
				else if(fc_isNull(arrData[idx].doffNo)) sMsg = ("<spring:message code='qms.msg_000004' text='{0}은(는) 필수 입력입니다.' arguments='${lb_doff_no}'/>");
				else if(!fc_isNull(arrData[idx].posInfo) 
				  		&&/[^0-9~,]/g.test(arrData[idx].posInfo)) sMsg = ("<spring:message code='qms.msg_000025' text='Pos/End 정보 양식이 맞지 않습니다.[숫자/(~)물결표시/(,)콤마만 사용]'/>");
				else if(!fc_isNull(arrData[idx].posInfo) 
						&& arrData[idx].posInfo.indexOf(",,")>-1)  sMsg = ("<spring:message code='qms.msg_000025' text='Pos/End 정보 양식이 맞지 않습니다.[숫자/(~)물결표시/(,)콤마만 사용]'/>");
				else if(!fc_isNull(arrData[idx].posInfo) 
						&& arrData[idx].posInfo.indexOf("~~")>-1)  sMsg = ("<spring:message code='qms.msg_000025' text='Pos/End 정보 양식이 맞지 않습니다.[숫자/(~)물결표시/(,)콤마만 사용]'/>");
				else if(data.filter(it => it.smpCd == arrData[idx].smpCd 
									   && it.lotNo == arrData[idx].lotNo 
									   && it.doffNo == arrData[idx].doffNo ).length > 1) {
					sMsg = ("<spring:message code='qms.msg_000019' text='{0}이 중복되었습니다.' arguments='${lb_dup_chk}'/>");
				}
			}else if(gridId=="gridInspRst"){
				if(data.filter(it => it.inspItemCd == arrData[idx].inspItemCd).length > 1) {
					sMsg = ("<spring:message code='qms.msg_000019' text='{0}이 중복되었습니다.' arguments='${lb_insp_item_nm}'/>");
				}
			}
			
			if(!fc_isNull(sMsg)) {
				cmmMessage.alert(sMsg);
				return;
			}
			
			//프로그램ID 설정
			arrData[idx].modifierProgramId = arrData[idx].creatorProgramId = "processInspReg";
		};
		
		var cData = JSON.stringify(arrData);
		
		jQuery.ajaxSettings.traditional = true;
		
		$.ajax({
			 url		: sUrl
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
						 if(gridId == "gridInspMst") fn_searchOnclick();
					else if(gridId == "gridInspDtl") searchDtl();
					else if(gridId == "gridInspRst") searchRst();
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
	# 파일첨부 팝업 
	################################################################--%>
	function fn_attachFilePop(ui){
		
		if(ui.rowData.inspRegNo == undefined){
			cmmMessage.alert("<spring:message code='qms.msg_000022' text='{0}를(을) 먼저 저장해주시기 바랍니다.' arguments='${lb_smp_dtl}'/>");
			return;
		}
		
		let oParam = {
			fileSeq 	: ui.rowData.fileSeq	/* 첨부파일Seq */
			,facId 		: ui.rowData.facId 		/* 공장ID */
			,programId 	: "processInspReg"		/* 프로그램ID */
			,rowIdx 	: ui.rowIndx			/* 선택 Row Idx */
			,downOnly 	: false					/* 다운 only 여부*/
			,inspRegNo	: ui.rowData.inspRegNo	/* 검사등록번호 */
			,inspSmpNo	: ui.rowData.inspSmpNo	/* 시료별검사번호 */
		};
		cmmAttachFile.popup(oParam,attachFileCallback);
	}
	function attachFileCallback(res){

		res.modifierProgramId = res.programId;
		
		$.ajax({
			 url	: "attachFileProcessInsp.do"
			,data	: res
			,type	: 'POST'
			,success: function (result) {
				//재조회
				searchDtl();
			}
			,error:function(xhr, textStatus, error){
				if(xhr.status=="404"){ // watch
					alert("Login Session Expired");
					location.href = "${contextPath}/reDirectToMain.do";
				}
			}
		});
	}
	
	<%--##############################################################
	# 계획불러오기 팝업 
	################################################################--%>
	function fn_callMstPlan(){
		
		var $grid = $("#gridInspPlan");
		
		//조회조건 요일구분 : 당일 요일로 설정
		$("#selPopDayTpCd").val(arrDayTp[new Date().getDay()].cdVal);
		
		$("#popupInspPlan").dialog({
			height:500,
			width: 800,
			modal: true,
			open: function (evt, ui) {
				$grid.pqGrid("option", "dataModel.data", []);
				$grid.pqGrid("refreshDataAndView");
				
				//계획불러오기 팝업 조회
				searchInspPlanPopup();
			},
			close: function () {
				//팝업 조회조건 초기화
				$("#selPopInspTpCd").val("");
				$("#selPopDayTpCd").val("");
				$("#txtPopInspPlanNm").val("");
			}
	 	});
	}
	function searchInspPlanPopup(){
		
		var grid = pq.grid("#gridInspPlan");
    	
		var cData = {
						 inspTpCd 	: $("#selPopInspTpCd").val()
						,dayTpCd 	: $("#selPopDayTpCd").val()
						,inspPlanNm : $("#txtPopInspPlanNm").val()
					};
		
		$.ajax({
			 url : "getInspPlanMastPopup.do"
			,data: cData
			,type: 'POST'
			,beforeSend : function(xmlHttpRequest){
 				grid.showLoading();
 				grid.option("dataModel.data",[]);
 				xmlHttpRequest.setRequestHeader("AJAX", "true"); // ajax 호출을  header에 기록
 			}
			,success: function (result) {
			 	var data = result.resultList;
				if (data != undefined && data.length > 0) {
					grid.option("dataModel.data", data);
				}
			 	grid.refreshDataAndView();
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
								<td>
									<select id="selInspTpCd" class="form-control">
										<option value=""><spring:message code='qms.lb_all' text='전체'/></option>
									</select>
								</td>
								<th><spring:message code='qms.lb_progress' text='진행상태'/></th>
								<td>
									<select id="selInspStatus" class="form-control">
										<option value=""><spring:message code='qms.lb_all' text='전체'/></option>
									</select>
								</td>
								<th><spring:message code='qms.lb_smp_cd_type' text='시료유형'/></th>
								<td>
									<select id="selSmpTpCd" class="form-control" onchange="fn_changeSmpTp(this);">
										<option value=""><spring:message code='qms.lb_all' text='전체'/></option>
									</select>
								</td>
								<th><spring:message code='qms.lb_smp_cd_group' text='시료그룹'/></th>
								<td>
									<select id="selSmpGrpCd" class="form-control">
										<option value=""><spring:message code='qms.lb_all' text='전체'/></option>
									</select>
								</td>
							</tr>
							<tr>
								<th><spring:message code='qms.lb_insp_nm' text='검사명'/></th>
								<td>
									<input type="text" id="txtInspRegNm" value="" class="form-control">
								</td>
								<th><spring:message code='qms.lb_creator' text='등록자'/></th>
								<td>
									<input type="text" id="txtRegUser" value="" class="form-control">
								</td>
								<th><spring:message code='qms.lb_created_time' text='등록일자'/></th>
								<td colspan="3">
									<input style="width:150px" type="date" id="dtRegDateFr" value="" class="form-control">
									~
									<input style="width:150px" type="date" id="dtRegDateTo" value="" class="form-control">
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
				<div class="d-flex" style="height:50%;padding:5px">
					<div class="w45p pr5">
						<div class="btn-box"> 
							<jsp:include page="../cmm/msStdBtnInclude.jsp" flush="true" >
				                <jsp:param name="buttonList"        value="SR" />
				                <jsp:param name="buttonIdentity"    value="Mst" />
				                <jsp:param name="buttonAuthGroup"   value="<%=btnAuthGroup%>" />
				                
				                <jsp:param name="buttonIds"           value="btnMstCopy" />
								<jsp:param name="buttonNms"           value="Copy" />
								<jsp:param name="buttonActions"       value="javascript:fn_copyMst();" />
								<jsp:param name="buttonChkAuthGroups" value="MODIFY" />
								<jsp:param name="buttonAuthGroups"    value="<%=btnAuthGroup%>" />
								
								<jsp:param name="buttonIds"           value="btnMstPlanCall" />
								<jsp:param name="buttonNms"           value="${lb_call_plan}" />
								<jsp:param name="buttonActions"       value="javascript:fn_callMstPlan();" />
								<jsp:param name="buttonChkAuthGroups" value="MODIFY" />
								<jsp:param name="buttonAuthGroups"    value="<%=btnAuthGroup%>" />
				            </jsp:include>
			            </div>
						<div id=gridInspMst class="table table-bordered"></div>
					</div>
					<div class="w55p pl5">
						<div class="btn-box"> 
							<jsp:include page="../cmm/msStdBtnInclude.jsp" flush="true" >
				                <jsp:param name="buttonList"        value="SR" />
				                <jsp:param name="buttonIdentity"    value="Dtl" />
				                <jsp:param name="buttonAuthGroup"   value="<%=btnAuthGroup%>" />
				                
				                <jsp:param name="buttonIds"           value="btnDtlCopy" />
								<jsp:param name="buttonNms"           value="Copy" />
								<jsp:param name="buttonActions"       value="javascript:fn_copyDtl();" />
								<jsp:param name="buttonChkAuthGroups" value="MODIFY" />
								<jsp:param name="buttonAuthGroups"    value="<%=btnAuthGroup%>" />
				            </jsp:include>
			            </div>
						<div id="gridInspDtl" class="table table-bordered"></div>
					</div>
				</div>
				<div class="ms-grid center-area" style="height:50%;padding:5px">
					<div class="btn-box"> 
						<jsp:include page="../cmm/msStdBtnInclude.jsp" flush="true" >
			                <jsp:param name="buttonList"        value="SR" />
			                <jsp:param name="buttonIdentity"    value="Rst" />
			                <jsp:param name="buttonAuthGroup"   value="<%=btnAuthGroup%>" />
			            </jsp:include>
					</div>
					<div id="gridInspRst" class="table table-bordered"></div>
				</div>
			</div>
		</div>
	</div>

	<%--Modal START--%>
	<div id="popupInspPlan" title="물성검사계획" style="overflow: hidden; display: none;">
		<table style="width:100%;margin: 7px 0px;">
			<tr>
				<th><spring:message code='qms.lb_insp_tp_nm' text='검사구분'/></th>
				<td style="width:100px">
					<select id="selPopInspTpCd" class="form-control">
						<option value=""><spring:message code='qms.lb_all' text='전체'/></option>
					</select>
				</td>
				<th><spring:message code='qms.lb_insp_nm' text='검사명'/></th>
				<td style="width:120px">
					<input type="text" id="txtPopInspPlanNm" value="" class="form-control">
				</td>
				<th><spring:message code='qms.lb_day_tp' text='요일구분'/></th>
				<td style="width:100px">
					<select id="selPopDayTpCd" class="form-control">
						<option value=""><spring:message code='qms.lb_all' text='전체'/></option>
					</select>
				</td>
				<td style="width:230px">
					<button type="button" class="btn btn-default" style="float:right;" onclick="searchInspPlanPopup();">
						<i class="fa fa-sticky-note"></i> <spring:message code='core.lb_search' text='검색'/>
					</button>
				</td>
			</tr>
		</table>
		<div id="gridInspPlan" style="float: left;"></div>
	</div>
	<%--Modal END--%>	

</body>
	
</html>
