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

<script src="${contextPath}/js/Highcharts/code/highcharts.js"></script>
<script src="${contextPath}/js/Highcharts/code/modules/series-label.js"></script>
<script src="${contextPath}/js/Highcharts/code/highcharts-more.js"></script>
<script src="${contextPath}/js/Highcharts/code/modules/exporting.js"></script>
<script src="${contextPath}/js/Highcharts/code/modules/variwide.js"></script>
<script src="${contextPath}/js/Highcharts/code/modules/accessibility.js"></script>
<script src="${contextPath}/js/Highcharts/code/modules/export-data.js"></script>

<script type="text/javascript">
	
	<%--공통 코드 사용시 include 한다. ※  document.ready 앞에 위치 ---%>
	<jsp:include page="../cmm/cmmCode_scriptor.jsp" />
	
	var arrSmpTpCd 	= ${lovSmpTpCd};	//시료유형Lov
	var arrSmpGrpCd = ${lovSmpGrpCd};	//시료그룹Lov
	var arrSmpCd 	= ${lovSmpCd};		//시료Lov
	var arrSmpItemCd = ${lovSmpItemCd}; // 검사항목
	var arrCalcTitle = [{posNo:"N"},{posNo:"Avg"},{posNo:"Max"},{posNo:"Min"},{posNo:"R"},{posNo:"Cv"},{posNo:"Sigma"},{posNo:"Cpk"}]; //계산식 항목
	
	var viewModule = {	//차트 기본설정
			chart: {
		//         type: 'spline',
		        animation: Highcharts.svg, // don't animate in old IE
		        marginRight: 10
		    },
		    title: {
		        text: ''
		    },
		    xAxis : {
// 	 			categories : new Array(),
// 				type: 'datetime',
				opposite : true,
		        crosshair : {width: 2,
		                    color: 'gray',
		                    dashStyle: 'shortdot'
		                   }
			},
			yAxis: {
		        title: {
		            text: null
		        }
		    },
		    legend: {
				enabled: false
		    },
		    plotOptions: {
		    	line: {
		    		connectNulls : true	// Null값 연결
		    	},
		        series: {
		            label: {
		                connectorAllowed: false
		            }
		        }
		    },
		    credits: {
		        enabled: false
		    },
		    exporting: {
		        enabled: true
		    }
		};
	
	$(document).ready(function(){
		
		<jsp:include page="../cmm/grid_scriptor.jsp" flush="false">
			<jsp:param name="gridType" value="main" />
			<jsp:param name="gridId" value="322" />
			<jsp:param name="gridLayerId" value="gridMain" />
		</jsp:include>
		
		<jsp:include page="../cmm/grid_scriptor.jsp" flush="false">
			<jsp:param name="gridType" value="modal" />
			<jsp:param name="gridId" value="328" />
			<jsp:param name="gridLayerId" value="gridPop" />
		</jsp:include>
		
		let gridMain = pq.grid("#gridMain");
		let gridPop = pq.grid("#gridPop");
		let colMdl = gridMain.option("colModel");
		
		//정렬 불가
		colMdl.forEach(item => {
			item.sortable = false;
		});
		
		//그리드 셀 클릭 이벤트 설정
		gridMain.on( "cellClick", (event, ui) => {
			//PosNo 클릭인 경우
			if(ui.dataIndx.startsWith("pn_")){
				if(!fc_isNull(ui.rowData.sortNo)) {
					let param = ui.rowData;
					param.posNo = ui.column.title;
					//팝업 호출
					fn_popup(param);
				}
			}
		});
		
		//그리드 헤더 클릭 이벤트
		gridMain.on("headerCellClick",(event,ui) => {
			//헤더 스타일이 있는 경우만
			if(ui.column.styleHead != undefined){
				fn_callChart(ui);
			}
		});
		
		gridPop.option( "showTop", false );
		
		//초기화
		initScreen();
	});
	
	<%--##############################################################
	# 코드셋팅, 날짜 셋팅등 초기화 함수는 initScreen() 안쪽에 작성
	################################################################--%>
	function initScreen() {
		//검사구분 공통코드 조회
		cmmCdUtil.fn_getCmmCd("INSP_TP" ,function(data){
			//검사구분 select 설정
			cmmCdUtil.fn_setSelectData($("#selInspTpCd"),data);
			
			$("#selInspTpCd").val("R"); //초기값 정기시험:R 으로 변경
		});		
		
		//시료유형 Select 설정
		cmmCdUtil.fn_setSelectData($("#selSmpTpCd"),arrSmpTpCd);
	}
	
	//조회조건 시료유형 변경에 따른 시료그룹 Option 변경
	function fn_changeSmpTp(obj){
		//시료그룹 select 설정
		let sSmpTp = $(obj).val();
		let filterData = arrSmpGrpCd.filter(item=>item.highSmpCd == sSmpTp);
		cmmCdUtil.fn_setSelectData($("#selSmpGrpCd"),filterData);
		
		fn_changeSmpGrp($("#selSmpGrpCd"));
	}
	
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
		
		let sInspTpCd = $("#selInspTpCd").val(); <%--검사구분--%>
		let sSmpTpCd  = $("#selSmpTpCd").val();	 <%--시료유형--%>
		let sSmpGrpCd = $("#selSmpGrpCd").val(); <%--시료그룹--%>
		let sSmpCd 	  = $("#selSmpCd").val(); 	 <%--시료--%>
		let sInspItem = $("#selInspItem").val(); <%--검사항목 --%>
		
		let sMsg = "";
			 if(fc_isNull(sInspTpCd)) sMsg = "<spring:message code='qms.msg_000004' text='{0}은(는) 필수 입력입니다.' arguments='${lb_insp_tp_nm}'/>";
		else if(fc_isNull(sSmpTpCd))  sMsg = "<spring:message code='qms.msg_000004' text='{0}은(는) 필수 입력입니다.' arguments='${lb_smp_cd_type}'/>";
		else if(fc_isNull(sSmpGrpCd)) sMsg = "<spring:message code='qms.msg_000004' text='{0}은(는) 필수 입력입니다.' arguments='${lb_smp_cd_group}'/>";
		else if(fc_isNull(sSmpCd)) 	  sMsg = "<spring:message code='qms.msg_000004' text='{0}은(는) 필수 입력입니다.' arguments='${lb_smp_cd}'/>";
		else if(fc_isNull(sInspItem)) sMsg = "<spring:message code='qms.msg_000004' text='{0}은(는) 필수 입력입니다.' arguments='${lb_insp_item_nm}'/>";
		
		if(!fc_isNull(sMsg)) {
			cmmMessage.alert(sMsg);
			return;
		}
		
		let cData = {
				 inspTpCd 	: sInspTpCd		<%--검사구분--%>
				,inspDtFr	: $("#dtInspDtFr").val().replace(/-/gi,"")	<%--등록일자From--%>
			 	,inspDtTo	: $("#dtInspDtTo").val().replace(/-/gi,"")	<%--등록일자To--%>
			 	,smpTpCd	: sSmpTpCd		<%--시료유형--%>
				,smpGrpCd	: sSmpGrpCd		<%--시료그룹--%>
				,smpCd		: sSmpCd		<%--시료--%>
				,inspItemCd : sInspItem		<%--검사항목 --%>
				,multiPosNo	: $("#txtPosNo").val()
				,chkN		: $("#chkN")	.is(":checked")? "Y" : "N" <%-- N수 표시여부 --%>
				,chkAvg		: $("#chkAvg")	.is(":checked")? "Y" : "N" <%-- Avg 표시여부 --%>
				,chkMax		: $("#chkMax")	.is(":checked")? "Y" : "N" <%-- Max 표시여부 --%>
				,chkMin		: $("#chkMin")	.is(":checked")? "Y" : "N" <%-- Min 표시여부 --%>
				,chkR		: $("#chkR")	.is(":checked")? "Y" : "N" <%-- R 표시여부 --%>
				,chkCv		: $("#chkCv")	.is(":checked")? "Y" : "N" <%-- Cv% 표시여부 --%>
				,chkSigma	: $("#chkSigma").is(":checked")? "Y" : "N" <%-- Sigma 표시여부 --%>
				,chkCpk		: $("#chkCpk")	.is(":checked")? "Y" : "N" <%-- Cpk 표시여부 --%>
			};
			
		let gridMain = pq.grid("#gridMain");
		let colMdl	 = gridMain.option("colModel");
		
		$.ajax({
			 url	: "getProcessPosInspRstList.do"
			,data	: cData
			,type	: 'POST'
			,beforeSend : function(xmlHttpRequest){
				gridMain.showLoading();
				gridMain.option("dataModel.data",[]);
				xmlHttpRequest.setRequestHeader("AJAX", "true"); // ajax 호출을  header에 기록
			}
			,success: function (result) {
				
				let dataHeader = result.listHeader;
				let data = result.listProcessPosInspRst;
				// colModel 고정 항목 외 제거
				colMdl.splice(9);
				
				//헤더 설정
				if(dataHeader != undefined && dataHeader.length > 0) {
					dataHeader.forEach(item => colMdl.push(fn_setColMdl(item,false)));
				}
				
				// 불합격 색상 변경
				colMdl.filter(item => item.dataIndx.startsWith("pn_")).forEach(mdl => {
		 			mdl.render = (ui) => {if(ui.rowData["iir_"+mdl.dataIndx.replace("pn_","")] == "F") return {style:{'color':'red'}}};
				});
				
				//계산식 항목 추가
				arrCalcTitle.forEach(item => {
					if(($("#chkN")	  .is(":checked") && item.posNo == "N")
					 ||($("#chkAvg")  .is(":checked") && item.posNo == "Avg")
					 ||($("#chkMax")  .is(":checked") && item.posNo == "Max")
					 ||($("#chkMin")  .is(":checked") && item.posNo == "Min")
					 ||($("#chkR")	  .is(":checked") && item.posNo == "R")
					 ||($("#chkCv")	  .is(":checked") && item.posNo == "Cv")
					 ||($("#chkSigma").is(":checked") && item.posNo == "Sigma")
					 ||($("#chkCpk")  .is(":checked") && item.posNo == "Cpk")) {
						colMdl.push(fn_setColMdl(item,true));
					}
				});
				
				gridMain.option("colModel",colMdl);
				
				if(data != undefined && data.length > 0) {
					gridMain.option("dataModel.data",data);
					
					//계산식 항목 인 경우 셀 병합
					let mc = [];
					for(let i in data) {
			        	if(fc_isNull(data[i].sortNo)) {
			        		mc.push({ r1: Number(i), c1: 0, rc: 1, cc: 9, style: 'background:#f8f8f8;'});
			        	}
			        }
					gridMain.option("mergeCells", mc);
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
	
	/* 그리드 ColModel 설정	*/
	function fn_setColMdl(obj,bCalcYn){
		let result = {};
		
		result.title = obj.posNo;
		result.width = obj.posNo.length*18;
		result.halign = "center"; //헤더 타이틀 정렬
		result.align = "right";	//그리드 값 정렬
		result.sortable = false; //정렬여부
		
		//계산식 항목 여부
		if(bCalcYn) {
			result.dataIndx = obj.posNo;
			if(obj.posNo == "Avg" || obj.posNo == "R") result.styleHead = {"color":"#004899"};
		}else {
			result.styleHead = {"color":"#004899"};
			result.dataIndx = "pn_"+obj.posNo;
		}
		
		return result;
	}
	
	<%--##############################################################
	# 팝업 호출
	################################################################--%>
	function fn_popup(param) {
		$("#spnPosNo").text(param.posNo);
		$("#spnInspItemNm").text(param.inspItemNm);
		
		$("#divPopup").dialog({
			height:160,
			width: 370,
			modal: true,
			open: function (evt, ui) {
				searchPopup(param);
			},
			close: function () {
				
			}
	 	});
	}
	
	<%--##############################################################
	# 팝업 조회
	################################################################--%>
	function searchPopup(param){
		
		let gridPop = pq.grid("#gridPop");
		
		$.ajax({
			 url : "getInspRstSpec.do"
			,data: param
			,type: 'POST'
			,beforeSend : function(xmlHttpRequest){
				gridPop.showLoading();
				gridPop.option("dataModel.data", []);
				xmlHttpRequest.setRequestHeader("AJAX", "true"); // ajax 호출을  header에 기록
			}
			,success: function (result) {
			 	var data = result.resultList;
				if (data != undefined && data.length > 0) {
					gridPop.option("dataModel.data", data);
				}
				
				gridPop.refreshDataAndView();
			 }
			,error:function(xhr, textStatus, error){
				if(xhr.status=="404"){ // watch
					alert("Login Session Expired");
					location.href = "${contextPath}/reDirectToMain.do";
				}
			}
			,complete : function() {
				gridPop.hideLoading();
			}
		 }); 
	}
	
	<%--##############################################################
	# 차트 호출
	################################################################--%>
	function fn_callChart(param){

		let dataIndx = param.column.dataIndx;
		let gridMain = pq.grid("#gridMain");
		let dataGrid = gridMain.option("dataModel.data");
		
		$("#divPopupChart").dialog({
			height:300,
			width: 800,
			modal: true,
			open: function (evt, ui) {
				
				let data = [];		//차트 Value
				let category = [];	//x축 카테고리
				
				//시료,검사항목,검사일자,LotNo 순으로 정렬
				dataGrid.sort((a,b) => {
					if(a.smpCd == b.smpCd) {
						if(a.sortNo == b.sortNo) {
							if(a.prdtnDt == b.prdtnDt) {
								return a.lotNo > b.lotNo ? 1 : -1;
							} else return a.prdtnDt > b.prdtnDt ? 1 : -1;
						} else return a.sortNo > b.sortNo ? 1 : -1;
					} else return a.smpCd > b.smpCd ? 1 : -1;
				});
				
				//차트 데이터, X축 카테고리 생성
				dataGrid.forEach(item => {
					if(!fc_isNull(item[dataIndx])) {
						if(!fc_isNull(item.sortNo)) {
							data.push(Number(item[dataIndx]));
							category.push(item.prdtnDt);
						}
					}
				});
				
				//차트명 설정[Pos No인 경우 라벨 추가]
				let sChartName = (param.dataIndx.startsWith("pn_") ? "Pos No. : " : "") + param.column.title;
				viewModule.title.text = sChartName;
				
				//X축 카테고리 설정
				viewModule.xAxis.categories = category;
				
				//차트 생성
 				let wvObjChart = new Highcharts.chart('divPopupChart', viewModule);
				
				//시리즈 추가
				wvObjChart.addSeries({
					name: param.column.title,
					data: data,
					label:{enabled:false}
				});
				
			},
			close: function () {
				//차트 초기화
				$("#divPopupChart").empty();
			}
	 	});
	}
	
	//엑셀 다운로드
	function fn_excelDownloadOnclick(){
		commonExcelOut2(pq.grid("#gridMain"), false);
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
										<option value=""><spring:message code='qms.lb_select_req' text='선택(필수)'/></option>
									</select>
								</td>
								<th><spring:message code='qms.lb_smp_cd_type' text='시료유형'/></th>
								<td style="width:120px">
									<select id="selSmpTpCd" class="form-control" onchange="fn_changeSmpTp(this);">
										<option value=""><spring:message code='qms.lb_select_req' text='선택(필수)'/></option>
									</select>
								</td>
								<th><spring:message code='qms.lb_smp_cd_group' text='시료그룹'/></th>
								<td style="width:140px">
									<select id="selSmpGrpCd" class="form-control" onchange="fn_changeSmpGrp(this);">
										<option value=""><spring:message code='qms.lb_select_req' text='선택(필수)'/></option>
									</select>
								</td>
								<th><spring:message code='qms.lb_smp_cd' text='시료'/></th>
								<td style="width:150px">
									<select id="selSmpCd" class="form-control" onchange="fn_changeSmpCd(this);">
										<option value=""><spring:message code='qms.lb_select_req' text='선택(필수)'/></option>
									</select>
								</td>
								<th><spring:message code='qms.lb_insp_item_nm' text='검사항목'/></th>
								<td style="width:150px">
									<select id="selInspItem" class="form-control">
										<option value=""><spring:message code='qms.lb_select_req' text='선택(필수)'/></option>
									</select>
								</td>
								<th><spring:message code='qms.lb_pos_end' text='Pos/End'/></th>
								<td>
									<input type="text" id="txtPosNo" value="" class="form-control" style="width:150px" placeholder="ex)1,3,5,10">
								</td>
							</tr>
							<tr>
								<th><spring:message code='qms.lb_insp_dt' text='검사일자'/></th>
								<td colspan="4">
									<input type="date" id="dtInspDtFr" class="form-control" style="width:150px;" value="${fromDt}"> ~ 
									<input type="date" id="dtInspDtTo" class="form-control" style="width:150px;" value="${toDt}">
								</td>
								<td colspan="8">
									<div class='checkList'>
										<label><input type="checkbox" id="chkN" style="width:10px" checked>&nbsp;N수</label>&nbsp;
										<label><input type="checkbox" id="chkAvg" style="width:10px" checked>&nbsp;Avg</label>&nbsp;
										<label><input type="checkbox" id="chkMax" style="width:10px" checked>&nbsp;Max</label>
										<label><input type="checkbox" id="chkMin" style="width:10px" checked>&nbsp;Min</label>&nbsp;
										<label><input type="checkbox" id="chkR" style="width:10px" checked>&nbsp;R</label>&nbsp;
										<label><input type="checkbox" id="chkCv" style="width:10px" checked>&nbsp;CV%</label>
										<label><input type="checkbox" id="chkSigma" style="width:10px" checked>&nbsp;Sigma</label>
										<label><input type="checkbox" id="chkCpk" style="width:10px" checked>&nbsp;Cpk</label>
									</div>
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
		                <jsp:param name="buttonList"        value="E2" />
		                <jsp:param name="buttonIdentity"    value="" />
		                <jsp:param name="buttonAuthGroup"   value="<%=btnAuthGroup%>" />
		            </jsp:include>
	            </div>
				<div id="gridMain"></div>
			</div>
		</div>
	</div>

	<div id="divPopup" title="" style="overflow: hidden; display: none;">
		<table style="width:100%;margin: 7px 0px;">
			<tr>
				<td style="width:60px">POS : </td>
				<td style="width:90px">
					<span id="spnPosNo"></span>
				</td>
				<td style="width:80px"><spring:message code='qms.lb_insp_item_nm' text='검사항목'/> : </td>
				<td style="width:110px">
					<span id="spnInspItemNm"></span>
				</td>
			</tr>
		</table>
		<div id="gridPop" style="float: left;"></div>
	</div>
	
	<div title="Chart" id="divPopupChart" style="overflow:hidden; display: none;">
	</div>
	
</body>
	
</html>
