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

<%--그리드의 인쇄,Excel저장 기능사용시 import ---%>
<script type="text/javascript" src="${contextPath}/js/cmm/cmmGrid.js"></script>

<script type="text/javascript">
	
	<%--############키캡처 이벤트  처리 ###################--%>
// 	$(window).bind('keydown', function(event) {
// 	    if (event.ctrlKey || event.metaKey) {
// 	        switch (String.fromCharCode(event.which).toLowerCase()) {
// 	        case 's':
// 	            event.preventDefault();
// 	            break;
// 	        }
// 	    }
// 	});

	var arrRptTpNo  = ${lovRptTpNo};//일보유형
	var arrRptNo  	= ${lovRptNo}; 	//일보
	var gvCondDtl;
	
	$(document).ready(function(){
	    
		<%--메인그리드 생성을 위한 include ---%>
		<jsp:include page="../cmm/grid_scriptor.jsp" flush="false">
			<jsp:param name="gridType" value="main" />
			<jsp:param name="gridId" value="367" />
			<jsp:param name="gridLayerId" value="gridMain" />
		</jsp:include>
		
		let grid = pq.grid("#gridMain");
		let colMdl = grid.option("colModel");
		
		colMdl.forEach(item => {
			item.hvalign = "center"; //수직정렬
		});
		
		grid.option("colModel", colMdl);
		grid.refreshView();
		
		initScreen();
		
	});

	<%--##############################################################
	# 코드셋팅, 날짜 셋팅등 초기화 함수는 initScreen() 안쪽에 작성
	################################################################--%>
    function initScreen() {
		//일보유형 Select 설정
		cmmCdUtil.fn_setSelectData($("#selRptTpNo"),arrRptTpNo);
    }
	//일보유형 변경에 따른 일보 Option 변경
	function fn_changeRptTpNo(obj){
		let filterData = arrRptNo.filter(item=>item.rptTpNo == $(obj).val());
		cmmCdUtil.fn_setSelectData($("#selRptNo"),filterData);
	}
	//일보 변경 이벤트
	function fn_changeRptNo(){
		let sRptTpNo = $("#selRptTpNo").val();
		let sRptNo = $("#selRptNo").val();
		
		if(fc_isNull(sRptNo)) return;
		
		gvCondDtl = arrRptNo.find(item=> item.rptTpNo == sRptTpNo && item.rptNo == sRptNo);
		
		$("#pDisValTp").text("("+gvCondDtl.disValTp+")");
	}
    
	<%--##############################################################
	# 그리드 마스터 조회
	################################################################--%>
    function fn_searchOnclick() {
    	
    	let sRptTpNo = $("#selRptTpNo").val(); 	<%--일보유형--%>
     	let sRptNo   = $("#selRptNo").val(); 	<%--일보--%>
    	
    	let sMsg = "";
			 if(fc_isNull(sRptTpNo)) 	sMsg = "<spring:message code='qms.lb_rpt_tp' text='일보유형'/>";
		else if(fc_isNull(sRptNo)) 		sMsg = "<spring:message code='qms.lb_rpt' text='일보'/>";
    	
		if(!fc_isNull(sMsg)) {
			cmmMessage.alert("<spring:message code='qms.msg_000004' text='{0}은(는) 필수 입력입니다.' arguments='"+sMsg+"'/>");
			return;
		}
		
        let cData = {
        		rptTpNo 	: sRptTpNo,
        		rptNo 		: sRptNo,
        		disValTp	: gvCondDtl.disValTp
        };
        
        let grid = pq.grid("#gridMain");
        let colMdl = grid.option("colModel");

        $.ajax({
            url: "getPpDailyReport.do"
            ,data: cData
            ,type: 'POST'
            ,beforeSend : function(xmlHttpRequest){
            	grid.showLoading();
				xmlHttpRequest.setRequestHeader("AJAX", "true"); // ajax 호출을  header에 기록
			}
            ,success: function (result) 
            {
                let data = result.listPpDailyReport;
                let header = result.resultHeader;
                
                //가변 추가 컬럼 삭제
                colMdl.splice(6);
				
                //고정항목 + 가변항목
				let newColMdl = [...colMdl, ...header];
				
				//숨김컬럼 설정
				newColMdl.find(item=>item.dataIndx=="shipSpec").hidden = gvCondDtl.shipSpecDisYn!="Y";
				newColMdl.find(item=>item.dataIndx=="mgmtSpec").hidden = gvCondDtl.mgmtSpecDisYn!="Y";
				newColMdl.find(item=>item.dataIndx=="plan").hidden = gvCondDtl.planDisYn!="Y";
				
				grid.option("colModel",newColMdl);
             	
                if (data == undefined || data.length < 1) {
               	 	grid.option("dataModel.data",[]);
                } else {
                	grid.option("dataModel.data",data);
                }
                
                //셀 자동병합
                autoMerge(grid,false);
				
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
				arrMainDel = [];
			}
        });
    }
    
	//셀병합
	function autoMerge(grid, refresh) {
        let mc = [],
            CM = grid.option("colModel"),
            i = 2, /*적용 최대 컬럼 Idx*/
            data = grid.option("dataModel.data");
		
        while (i--) {
        	let dataIndx = CM[i].dataIndx,
                rc = 1,
                j = data.length;
                ;
            while (j--) {
            	let cd = data[j][dataIndx],
                    cd_prev = data[j - 1] ? data[j - 1][dataIndx] : undefined;
					
				if (cd_prev !== undefined && cd == cd_prev) {
                    rc++;
                }else if (rc > 1) {
                    mc.push({ r1: j, c1: i, rc: rc, cc: 1, style: 'background:#f8f8f8;'});
                    rc = 1;
                }
            }
        }
        
        grid.option("mergeCells", mc);
        
        //병합셀 중간 정렬
        let obj = {	columnTemplate	: {align: 'center', valign: 'center'}}; 
        $("#gridMain").pqGrid( obj );
        
        if (refresh) {
            grid.refreshView();
        }
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
								<th><spring:message code='qms.lb_rpt_tp' text='일보유형'/></th>
								<td style="width:120px">
									<select id="selRptTpNo" class="form-control" onchange="fn_changeRptTpNo(this);">
										<option value=""><spring:message code='qms.lb_select_req' text='선택(필수)'/></option>
									</select>
								</td>
								<th><spring:message code='qms.lb_rpt' text='일보'/></th>
								<td style="width:120px">
									<select id="selRptNo" class="form-control" onchange="fn_changeRptNo(this);">
										<option value=""><spring:message code='qms.lb_select_req' text='선택(필수)'/></option>
									</select>
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
					<div> <p id="pDisValTp" style="text-align: right;padding: 8px;position: relative;right: 120px;color:red"></p></div>
					<jsp:include page="../cmm/msStdBtnInclude.jsp" flush="true" >
		                <jsp:param name="buttonList"        value="E2" />
		                <jsp:param name="buttonIdentity"    value="" />
		                <jsp:param name="buttonAuthGroup"   value="<%=btnAuthGroup%>" />
		            </jsp:include>
	            </div>
				<div id="gridMain" class="table table-bordered"></div>
			</div>
		</div>
		
	</div>
	
	<%--Modal START--%>
	<%--Modal END--%>
	
</body>
	
</html>
