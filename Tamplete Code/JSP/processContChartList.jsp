<%--==============================================================================
*Copyright(c) 2021 HYOSUNG  ADVANCED  MATERIALS Co., Ltd. / ITRION Co., Ltd.  
*
*@ProcessChain :(울산)관리도 조회	
*
*@File	 : processContChartList.jsp
*
*@FileName : (울산)관리도 조회	 JSP
*
* Date             Ver.     Name     Description
*-----------------------------------------------------------------------------
* 2021.03.04		1.00	  KHS	 최초 생성
* 2021.03.15		1.01	  KBS	 수정
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

<script src="${contextPath}/js/Highcharts/code/highcharts.js"></script>
<script src="${contextPath}/js/Highcharts/code/modules/stock.js"></script>
<script src="${contextPath}/js/Highcharts/code/modules/heatmap.js"></script>
<script src="${contextPath}/js/Highcharts/code/modules/boost-canvas.js"></script>
<script src="${contextPath}/js/Highcharts/code/modules/boost.js"></script>
<script src="${contextPath}/js/Highcharts/code/modules/series-label.js"></script>
<script src="${contextPath}/js/Highcharts/code/highcharts-more.js"></script>
<script src="${contextPath}/js/Highcharts/code/modules/exporting.js"></script>
<script src="${contextPath}/js/Highcharts/code/modules/accessibility.js"></script>


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
	
	<%--공통 코드 사용시 include 한다. ※  document.ready 앞에 위치 ---%>
	<jsp:include page="../cmm/cmmCode_scriptor.jsp" />
	
	var arrSmpTpCd 	= ${lovSmpTpCd};	//시료유형Lov
	var arrSmpGrpCd = ${lovSmpGrpCd};	//시료그룹Lov
	var arrSmpCd 	= ${lovSmpCd};		//시료Lov
	var arrSmpGrpPrdpc = ${lovSmpGrpPrdpc}; // line(공정)
	
	$(document).ready(function(){
		
		<jsp:include page="../cmm/grid_scriptor.jsp" flush="false">
			<jsp:param name="gridType" value="main" />
			<jsp:param name="gridId" value="333" />
			<jsp:param name="gridLayerId" value="gridMain" />
		</jsp:include>
		
		<jsp:include page="../cmm/grid_scriptor.jsp" flush="false">
			<jsp:param name="gridType" value="modal" />
			<jsp:param name="gridId" value="334" />
			<jsp:param name="gridLayerId" value="gridPopup" />
		</jsp:include>
		
		let grid = pq.grid("#gridMain");
		let gridPopup = pq.grid("#gridPopup");
		let colModel = grid.option("colModel");
		
		let specColMdl = [
			 {title:"Min",dataIndx:"min",width:"100",align:"right",halign:"center",type:"string",editable:false}
		 	,{title:"Max",dataIndx:"max",width:"100",align:"right",halign:"center",type:"string",editable:false}
		];
		//스펙 하위 ColModel로 설정
		colModel.find(it => it.dataIndx=="spec").colModel = specColMdl;
		
		colModel.forEach(item => {
			item.hvalign = "center"; //수직정렬
		});
		
		grid.option("colModel",colModel);
		
		grid.refreshView();
		
		//팝업 그리드 더블클릭 이벤트
		gridPopup.on("cellDblClick",function(event, ui) {
			ui.rowData.state = !ui.rowData.state;
			this.refreshView();
		});
		
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
			
			$("#selInspTpCd").val("R"); //초기값 정기시험:R 으로 변경
		});		
		
		//시료유형 Select 설정
		cmmCdUtil.fn_setSelectData($("#selSmpTpCd"),arrSmpTpCd);
	}
	
	//시료유형 변경 이벤트
	function fn_changeSmpTp(obj){
		//시료그룹 select 설정
		let sSmpTp = $(obj).val();
		let filterData = arrSmpGrpCd.filter(item=>item.highSmpCd == sSmpTp);
		cmmCdUtil.fn_setSelectData($("#selSmpGrpCd"),filterData);
		
		fn_changeSmpGrp($("#selSmpGrpCd"));
	}
	
	//시료그룹 변경 이벤트
	function fn_changeSmpGrp(obj){
		//시료그룹 select 설정
		let sSmpTp = $(obj).val();
		let filterData = arrSmpCd.filter(item=>item.highSmpCd == sSmpTp);
		cmmCdUtil.fn_setSelectData($("#selSmpCd"),filterData);
		
		//라인 select 설정
		let filterData2 = arrSmpGrpPrdpc.filter(item=>item.smpGrpCd == sSmpTp);
		cmmCdUtil.fn_setSelectData($("#selLineCd"),filterData2);	
	}
	
	//시료 변경 이벤트
	function fn_changeSmp(obj){
		$("#txtInspItem").val("");
		$("#txtInspItemCd").val("");
	}
	
	<%--##############################################################
	# Master 조회
	################################################################--%>
	function fn_searchOnclick() {
		
		let sInspTpCd 	= $("#selInspTpCd").val(); 	<%--검사구분--%>
		let sSmpTpCd  	= $("#selSmpTpCd").val();	<%--시료유형--%>
		let sSmpGrpCd 	= $("#selSmpGrpCd").val(); 	<%--시료그룹--%>
		let sSmpCd 	  	= $("#selSmpCd").val(); 	<%--시료--%>
		let selLineCd	= $("#selLineCd").val();	<%--Line--%>
		let sInspItemCd = $("#txtInspItemCd").val();<%--검사항목--%>

		let sMsg = "";
			 if(fc_isNull(sInspTpCd)) 	sMsg = "<spring:message code='qms.lb_insp_tp' text='검사구분'/>";
		else if(fc_isNull(sSmpTpCd))  	sMsg = "<spring:message code='qms.lb_smp_cd_type' text='시료유형'/>";
		else if(fc_isNull(sSmpGrpCd)) 	sMsg = "<spring:message code='qms.lb_smp_cd_group' text='시료그룹'/>";
		else if(fc_isNull(sSmpCd)) 	  	sMsg = "<spring:message code='qms.lb_smp_cd' text='시료'/>";
		else if(fc_isNull(selLineCd)) 	sMsg = "Line";
		else if(fc_isNull(sInspItemCd)) sMsg = "<spring:message code='qms.lb_insp_item_nm' text='검사항목'/>";
		
		if(!fc_isNull(sMsg)) {
			cmmMessage.alert("<spring:message code='qms.msg_000004' text='{0}은(는) 필수 입력입니다.' arguments='"+sMsg+"'/>");
			return;
		}
		
		let cData = {
				 inspTpCd 	: sInspTpCd		<%--검사구분--%>
				,smpTpCd	: sSmpTpCd		<%--시료유형--%>
				,smpGrpCd	: sSmpGrpCd		<%--시료그룹--%>
				,smpCd		: sSmpCd		<%--시료--%>
				,prdpcId	: $("#selLineCd").val()		<%--Line--%>
				,prdtnDtFr	: $("#dtPrdtnDtFr").val().replace(/-/gi,"")	<%--생산일자From--%>
			 	,prdtnDtTo	: $("#dtPrdtnDtTo").val().replace(/-/gi,"")	<%--생산일자To--%>
			 	,inspItemCd : sInspItemCd			<%--검사항목--%>
				,posNo		: $("#txtPosNo").val()	<%--Pos No--%>
				,inspItemRst: $("#chkBad").is(":checked")? "P" : "" <%-- 불량 제외 : 합격여부 P --%>
			};
			
		let gridMain = pq.grid("#gridMain");
		let colMdl = gridMain.option("colModel");
		
		$.ajax({
			 url	: "getProcessContChartList.do"
			,data	: cData
			,type	: 'POST'
			,beforeSend : function(xmlHttpRequest){
				gridMain.showLoading();
				gridMain.option("dataModel.data",[]);
				xmlHttpRequest.setRequestHeader("AJAX", "true"); // ajax 호출을  header에 기록
			}
			,success: function (result) {

				let dataHeader = result.listHeader;
				let data = result.listProcessContChart;
				let chartData = result.listProcessContData;
				let chartPlotLine = result.listProcessContChartPlotLines;
				
				// colModel 고정 항목 외 제거
				colMdl.splice(3);
				
				//헤더 설정
				if(dataHeader != undefined && dataHeader.length > 0) {
					dataHeader.forEach(item => colMdl.push(fn_setColMdl(item)));
				}
				
				gridMain.option("colModel",colMdl);
				
				if(data != undefined && data.length > 0) {
					gridMain.option("dataModel.data",data);
				}
				
				gridMain.refreshDataAndView();
				
				//차트 생성
				$.fn_createChart(data,chartData,chartPlotLine);
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
	
	//그리드 ColModel 설정
	function fn_setColMdl(obj){
		let result = {};
		
		result.title = obj.prdtnMd;
		result.dataIndx = obj.prdtnDt;
		result.width = 100;
		result.halign = "center"; //헤더 타이틀 정렬
		result.align = "right";	//그리드 값 정렬
		result.hvalign = "center"; //수직정렬
		result.sortable = false; //정렬여부
// 		result.styleHead = {"color":"#004899"};
		
		return result;
	}
	
	
	// 차트 생성
	$.fn_createChart = function(dataInspItem,chartData,chartPlotLine) {
		
		/*********************************************** Range Chart 생성 Start *****************************************************/
		Highcharts.seriesType('lowmedhigh', 'boxplot', {
		    keys: ['low', 'median', 'high'],
		    tooltip: {
		        pointFormat: '<span style="color:{point.color}">\u25CF</span> {series.name}: ' +
		            'Low <b>{point.low}</b> - Median <b>{point.median}</b> - High <b>{point.high}</b><br/>'
		    }
		}, {
		    // Change point shape to a line with three crossing lines for low/median/high
		    // Stroke width is hardcoded to 1 for simplicity
		    drawPoints: function () {
		        var series = this;
		        
		        Highcharts.each(this.points, function (point) {
		        	var graphic = point.graphic,
		                verb = graphic ? 'animate' : 'attr',
		                shapeArgs = point.shapeArgs,
		                width = shapeArgs.width,
		                left = Math.floor(shapeArgs.x) + 0.5,
		                right = left + width,
		                crispX = left + Math.round(width / 2) + 0.5,
		                highPlot = Math.floor(point.highPlot) + 0.5,
		                medianPlot = Math.floor(point.medianPlot) + 0.5,
		                lowPlot = Math.floor(point.lowPlot) + 0.5 - (point.low === 0 ? 1 : 0); // Sneakily draw low marker even if 0

		            if (point.isNull) {
		                return;
		            }

		            if (!graphic) {
		                point.graphic = graphic = series.chart.renderer.path('point').add(series.group);
		            }

		            graphic.attr({
		                stroke: point.color || series.color,
		                "stroke-width": 1
		            });

		            graphic[verb]({
		                d: [
		                    'M', left, highPlot,
		                    'H', right,
		                    'M', left, medianPlot,
		                    'H', right,
		                    'M', left, lowPlot,
		                    'H', right,
		                    'M', crispX, highPlot,
		                    'V', lowPlot
		                ]
		            });
		        });
		    }
		});
		
		/*********************************************** Range Chart 생성 End *****************************************************/
		
// 		console.log(dataInspItem);
// 		console.log(chartData);
// 		console.log(chartPlotLine);
		
		/*********************************************** line Chart 생성 Start *****************************************************/
		//차트 div 영역 초기화
		$("#divVarChart").children().remove();
		
		/* 검사항목 수만큼 차트 생성 */
		dataInspItem.forEach( InspItem => {

			//검사항목코드
			let sInstItemCd = InspItem.inspItemCd;
			
			//차트 div 영역 동적 생성
			$("#divVarChart").append("<div id='div_"+sInstItemCd+"'></div>");
			
			let arrCtgr   = [];	// categories
			let arrAvg 	  = [];	// 평균 데이터
			let arrMinMax = [];	// 상하한 데이터
			
			chartData.forEach(cData => {
				if(cData[sInstItemCd]!=null){
					arrCtgr.push(cData.prdtnDtDoff);
					arrAvg.push(cData[sInstItemCd]);
					arrMinMax.push([cData[sInstItemCd+"_min"],cData[sInstItemCd],cData[sInstItemCd+"_max"]]);
				}
			});
			
			//Plot Line Data
			let findPlotLine = chartPlotLine.find(item => item.inspItemCd == sInstItemCd);
			let vCl  = findPlotLine.mgmtSpecAim; //CL:관리Target
			let vLcl = findPlotLine.mgmtSpecMin; //LCL:관리하한값
			let vUcl = findPlotLine.mgmtSpecMax; //UCL:관리상한값
			let vLsl = findPlotLine.shipSpecMin; //LSL:출하하한값
			let vUsl = findPlotLine.shipSpecMax; //USL:출하상한값
			let lineColor = {ULSL:"#FF2424",ULCL:"#0030DB",CL:"#FFD732",VAL:"#434348"};	//Line 색상
			
			Highcharts.chart("div_"+sInstItemCd, {
				title: {
			        text: InspItem.inspItemNm
			        ,style : {fontSize : '14px'}
			    }
			  , chart: {  zoomType: 'xy'
				        , marginLeft: 50  
				        , height:350
			    }
			  , xAxis: {
				  		categories: arrCtgr 	  					
				}
			  , yAxis: {
// 				            max : 1450
// 						  , min : 1200 
							title: {
					            text: null
					      	}
				  		  ,plotLines: [
									  	  {value:vUsl, color:lineColor.ULSL, width:1.5} // USL
									  	, {value:vLsl, color:lineColor.ULSL, width:1.5} // LSL
									  	, {value:vUcl, color:lineColor.ULCL, width:1.5, dashStyle:'shortdash'} // UCL
									  	, {value:vLcl, color:lineColor.ULCL, width:1.5, dashStyle:'shortdash'} // LCL
									  	, {value:vCl , color:lineColor.CL, width:1.5} // CL
						    ]
			    }
	// 		  , legend: {verticalAlign: 'top'}
			  , series: [
				  		 /*************  Line Chart Start *************/
				  		   {name:"USL"	 , color:lineColor.ULSL, marker:{enabled: false}}
			  			 , {name:"LSL"	 , color:lineColor.ULSL, marker:{enabled: false}}
			  			 , {name:"UCL"	 , color:lineColor.ULCL, marker:{enabled: false}, dashStyle:'shortdash'}
			  			 , {name:"LCL"	 , color:lineColor.ULCL, marker:{enabled: false}, dashStyle:'shortdash'}
			  			 , {name:"CL"	 , color:lineColor.CL, marker:{enabled: false}}
			  			 , {name:"AVG"	 , color:lineColor.VAL, data: arrAvg /*[  1324, 1324, 1354, 1300, 1314]*/}
			  			 /*************  Line Chart End *************/
			  			 
			  			 /*************  Range Chart Start *************/
			  			 , {name: 'MIN/MAX', color:lineColor.VAL, type:'lowmedhigh', data : arrMinMax 
			  				 												/*[[1350, 1324, 1300]
			  			 													, [1380, 1324, 1290]
			  			 													, [1400, 1354, 1320]
			  			 													, [1350, 1300, 1250]
			  			 													, [1360, 1314, 1250]]*/ }		  			
			  			/*************  Range Chart End *************/
			  			 
			  			 ]
			  , tooltip: {
			        shared: true,
			        useHTML: true,
			        formatter: function () {
			        	var returnValue = "";
			        		returnValue += " "+ this.x ;
			        	returnValue += '<table>';
			            
			        	$.each(this.points, function () {
			            	returnValue += '<tr>';
			            	returnValue += '<td style="color:' + this.color + '"> ' +this.series.name+' : </td>';
			            	if(this.series.name == "MIN/MAX") {
			            		returnValue += '<td style="text-align: right"><b> Min('+Highcharts.numberFormat(this.point.low, 2)+') , Max('+Highcharts.numberFormat(this.point.high, 2)+')</b></td>';
			            	}else{
			            		returnValue += '<td style="text-align: right"><b>'+Highcharts.numberFormat(this.y, 2)+'</b></td>';
			            	}
			            	returnValue += '</tr>';
			            });
			            returnValue += '</table>';
			            return returnValue;
			        }
			    }
			  , plotOptions: {
			    	 line: { connectNulls : true} // Null값 연결
			  		, series: {
					        	  stickyTracking: true
					        	, whiskerWidth: 2
					        	, label: {connectorAllowed: false}
			        }
			    }
			  ,credits: {
			        enabled: false
			    }
			});
		
		});
		/*********************************************** line Chart 생성 End *****************************************************/
	}
	
	
	<%--##############################################################
	# 검사항목 팝업
	################################################################--%>
	function fn_popupInspItemCd(){
		
		if(fc_isNull($("#selSmpCd").val())){
			let sMsg = "<spring:message code='qms.lb_smp_cd' text='시료'/>";
			cmmMessage.alert("<spring:message code='qms.msg_000004' text='{0}은(는) 필수 입력입니다.' arguments='"+sMsg+"'/>");
			return;
		}
		
		let $grid = $("#gridPopup");
		
		$("#divPopup").dialog({
			height:400,
			width: 550,
			modal: true,
			open: function (evt, ui) {
				$grid.pqGrid("option", "dataModel.data", []);
				$grid.pqGrid("refreshDataAndView");
				
				//검사항목 팝업 조회
				searchInspItemPopup();
			},
			close: function () {
				//팝업 조회조건 초기화
				$("#txtPopInspItem").val("");
			}
	 	});
	}
	<%--##############################################################
	# 검사항목 팝업 조회
	################################################################--%>
	function searchInspItemPopup(){
		
		let grid = pq.grid("#gridPopup");
    	
		let cData = {
				smpCd : $("#selSmpCd").val()
		};
		
		$.ajax({
			 url : "getCmmInspItem.do"
			,data: cData
			,type: 'POST'
			,beforeSend : function(xmlHttpRequest){
 				grid.showLoading();
 				grid.option("dataModel.data",[]);
 				xmlHttpRequest.setRequestHeader("AJAX", "true"); // ajax 호출을  header에 기록
 			}
			,success: function (result) {
				let data = result.resultList;
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
	
	<%--##############################################################
	# 검사항목 팝업 선택
	################################################################--%>
	function selectInspItemPopup(){
		
		let grid = pq.grid("#gridPopup");
		let data = grid.option("dataModel.data");
		let chkData = data.filter(item=>item.state);
		let sInspItemCd = "";	//검사항목코드
		let sInstItemNm = "";	//검사항목명
		
		chkData.forEach(item=>{
			sInspItemCd += ","+item.cdVal;
			sInstItemNm += ","+item.cdValNm;
		});
		
		sInspItemCd = sInspItemCd.substring(1);
		sInstItemNm = sInstItemNm.substring(1);
		
		$("#txtInspItemCd").val(sInspItemCd);
		$("#txtInspItem").val(sInstItemNm);
		
		$("#divPopup").dialog("close");
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
								<td style="width:130px">
									<select id="selSmpTpCd" class="form-control" onchange="fn_changeSmpTp(this);">
										<option value=""><spring:message code='qms.lb_select_req' text='선택(필수)'/></option>
									</select>
								</td>
								<th><spring:message code='qms.lb_smp_cd_group' text='시료그룹'/></th>
								<td style="width:160px">
									<select id="selSmpGrpCd" class="form-control" onchange="fn_changeSmpGrp(this);">
										<option value=""><spring:message code='qms.lb_select_req' text='선택(필수)'/></option>
									</select>
								</td>
								<th><spring:message code='qms.lb_smp_cd' text='시료'/></th>
								<td style="width:120px">
									<select id="selSmpCd" class="form-control" onchange="fn_changeSmp(this);">
										<option value=""><spring:message code='qms.lb_select_req' text='선택(필수)'/></option>
									</select>
								</td>
								<th><spring:message code='qms.lb_line' text='Line'/></th>
								<td style="width:120px">
									<select id="selLineCd" class="form-control">
										<option value=""><spring:message code='qms.lb_select_req' text='선택(필수)'/></option>
									</select>
								</td>
							</tr>
							<tr>
								<th><spring:message code='qms.lb_prdtn_dt' text='생산일자'/></th>
								<td colspan="3">
									<input type="date" id="dtPrdtnDtFr" class="form-control" style="width:145px;" value="${fromDt}"> ~ 
									<input type="date" id="dtPrdtnDtTo" class="form-control" style="width:145px;" value="${toDt}">
								</td>
								<th><spring:message code='qms.lb_insp_item_nm' text='검사항목'/></th>
								<td>
									<input type="text" id="txtInspItem" value="" class="form-control" style="width:120px" readonly>
									<input type="text" id="txtInspItemCd" value="" class="form-control" style="display: none;">
									<button type="button" class="btn btn-default" onclick="fn_popupInspItemCd();"><i class="fa fa-search"></i></button>
								</td>
								<th><spring:message code='qms.lb_pos_end' text='Pos/End'/></th>
								<td>
									<input type="text" id="txtPosNo" value="" class="form-control" style="width:120px">
								</td>
								<th></th>
								<td>
									<div class='checkList'>
										<label><input type="checkbox" id="chkBad" style="width:10px"> <spring:message code='qms.lb_excluding_defects' text='불량제외'/></label>
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
	            <div class="ms-grid center-area">
					<div id="gridMain"></div>
						
					<figure style="top-margin:10px;">
						<!-- 가변 차트 영역 -->
						<div id="divVarChart">
						</div>
					</figure>											
				</div>
			</div>			
		</div>
	</div>

	<%--Modal START--%>
	<div id="divPopup" title="<spring:message code='qms.lb_insp_item_nm' text='검사항목'/>" style="overflow: hidden; display: none;">
		<table style="width:100%;margin: 7px 0px;">
			<tr>
				<td>
				</td>
				<td style="width:160px">
					<button type="button" class="btn btn-default" style="float:right;" onclick="searchInspItemPopup();">
						<i class="fa fa-sticky-note"></i> <spring:message code='core.lb_search' text='검색'/>
					</button>
				</td>
				<td style="width:65px">
					<button type="button" class="btn btn-default" style="float:right;" onclick="selectInspItemPopup();">
						<i class="ui-icon ui-icon-check"></i> <spring:message code='core.lb_select' text='선택'/>
					</button>
				</td>
			</tr>
		</table>
		<div id="gridPopup" style="float: left;"></div>
	</div>
	<%--Modal END--%>	

</body>
	
</html>
