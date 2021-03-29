
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib  prefix="spring" uri="http://www.springframework.org/tags" %>

	<div id="popupCmmAttachFile" title="파일 첨부" style="display:none">
		<div>
			<div class="ms-grid center-area">
				<table style="width:100%;margin: 7px 0px;">
					<tr>
						<td>
							<form method="post" id="fileForm" enctype="multipart/form-data" style="display: none;">
					        	<input type="hidden" id="fileFacId" name="fileFacId">
					        	<input type="hidden" id="fileSeq" name="fileSeq">
					        	<input type="hidden" id="programId" name="programId">
					        	<input type="file" name="file" id="file" onchange="cmmAttachFile.selectFile();">
					        	<input type="file" name="fileOne" id="fileOne" onchange="cmmAttachFileOne.selectFile();">
							</form>
						</td>
						<td style="text-align: right;">
							<button type="button" class="btn btn-default attachFile-downonly" onclick="cmmAttachFile.upload();">
								<i class="fa fa-save"></i>&nbsp;<spring:message code="button.create" text="등록"/>
							</button>
							<button type="button" class="btn btn-default attachFile-downonly" onclick="cmmAttachFile.deleteFile();">
								<i class="fa fa-trash-alt"></i>&nbsp; <spring:message code="button.delete" text="삭제"/>
							</button>
							<button type="button" class="btn btn-default" onclick="cmmAttachFile.close();">
								<i class="fa fa-check"></i>&nbsp; <spring:message code="qms.lb_complete" text="완료"/>
							</button>
						</td>
					</tr>
				</table>
				<div id="gridAttachFile"></div>
			</div>
		</div>
	</div>
	
<script type="text/javascript">

	$(document).ready(function() {

		/*그리드 생성*/
		cmmAttachFile.fn_createGrid();

	});

	
	
var cmmAttachFile = {
	/*그리드 생성*/
	fn_createGrid : function() {

		var colModel = [{ dataIndx: "state", maxWidth: 50, minWidth: 50, align: "center", resizable: false,
			            title: "",
			            menuIcon: false,
			            type: 'checkBoxSelection', cls: 'ui-state-default', sortable: false, editor: false,
			            dataType: 'bool',
			            cb: { all: false, //checkbox selection in the header affect current page only.
			                header: true //show checkbox in header. 
			            	}
						}
		               ,{title    : "File No",	dataIndx : "fileNo", 	dataType : "string",	halign   : "center",	align    : "center",	hvalign  : "center",	width    : 100,	editable : false}
		               ,{title    : "파일명",		dataIndx : "fileNm",	dataType : "string",	halign   : "center",	align    : "left",		hvalign  : "center",	width    : 470, editable : false, style:"color:#004899"}
		               ];
		
		$("#gridAttachFile").pqGrid({
			collapsible : {
				           on : false,
				           toggle : false
			              },
			width : 660,
			height : 290,
			colModel : colModel,
			menuIcon : false,
			showTitle : false
			// , editable  : false
			,
			showTop : false,
			numberCell : {
				show : true
			}
			
            , rowDblClick : function(event, ui) {
            	//파일 다운로드
             	cmmAttachFile.download(ui.rowData);
			}

		});

	}
	,
	oParamAttachFile : {}
	,
	/* 파일첨부 팝업 */
	popup : function(param, callback){

		oParamAttachFile = param;
		
		if(oParamAttachFile.downOnly != undefined && oParamAttachFile.downOnly){
			$(".btn.btn-default.attachFile-downonly").hide();
		}else{
			$(".btn.btn-default.attachFile-downonly").show();
		}
		
		$("#popupCmmAttachFile").dialog({
	        width: 700,
			height:400,
			modal: true,
			open: function (evt, ui) {
				var grid = pq.grid("#gridAttachFile");
				grid.refreshView();

				cmmAttachFile.search();
			},
			close: function () {
				callback(oParamAttachFile);
			}
	 	});
	}
	,
	/* 파일첨부 조회 */
	search : function(){
	        
        var grid = pq.grid("#gridAttachFile");

        $.ajax({
            url: "getAttachFile.do",
            data: oParamAttachFile,
            type: 'POST',
            beforeSend : function(xmlHttpRequest){
            	grid.showLoading();
				xmlHttpRequest.setRequestHeader("AJAX", "true"); // ajax 호출을  header에 기록
			},
            success: function (result) {
                var resultList = result.resultList;
                
                if (resultList.length < 1) {
                	grid.option("dataModel.data",[]);
                	oParamAttachFile.fileSeq = "";
                } else {
                	grid.option("dataModel.data",resultList);
                	
                	//조회 파일 시퀀스 설정
                	oParamAttachFile.fileSeq = resultList[0].fileSeq;
                }
                
                grid.refreshDataAndView();
            },
            error: function (result) {
                console.log(result);
            },
            complete : function() {
            	grid.hideLoading();
			}
        });
	}
	,
	/* 완료(닫기) */
	close : function(){
		$("#popupCmmAttachFile").dialog("close");
	}
	,
	/* 업로드 */
	upload : function(){
		//파일 Input 초기화
		$("#popupCmmAttachFile").find("#file").val("");
		document.getElementById("file").click();
	}
	,
	/* 파일 선택 */
	selectFile : function(){
		
		$('#fileFacId').val(oParamAttachFile.facId);		<%--공장 ID --%>
		$('#programId').val(oParamAttachFile.programId);	<%--프로그램 ID --%>
		$('#fileSeq').val(oParamAttachFile.fileSeq); 		<%-- 파일 시퀀스 --%>
		
		var form = new FormData(document.getElementById('fileForm'));
		
		var grid = pq.grid("#gridAttachFile");
		
		$.ajax({
			  url: "uploadAttachFile.do"
			, data: form
			, dataType: 'text'
			, processData: false
			, contentType: false
			, type: 'POST'
			, beforeSend : function(xmlHttpRequest){
            	grid.showLoading();
				xmlHttpRequest.setRequestHeader("AJAX", "true"); // ajax 호출을  header에 기록
			}
			, success: function (response) { 
				
				var result = JSON.parse(response);
// 				cmmMessage.alert(result.resultMsg);
				
				let resultCode = result.resultCode;
				if(resultCode == "E") {
					cmmMessage.alert(result.resultMsg);
				} else {
					oParamAttachFile.fileSeq = result.fileSeq;
					cmmAttachFile.search();
				}
			}
			, error: function (jqXHR) { 
				grid.hideLoading();
				console.log('error'); 
			}
			, complete : function() {
            	grid.hideLoading();
			}
		});
	}
	,
	/*파일 다운로드*/
	download : function(param){
		
		location.href = "downAttachFile.do?"
					+ "fileHashNm=" + param.fileHashNm + "&"
					+ "fileNm=" 	+ param.fileNm ;
	}
	,
	/* 파일 삭제 */
	deleteFile : function(){
		
		var grid = pq.grid("#gridAttachFile");
		var data = grid.option("dataModel.data");

		//체크 데이터만 선별
		var filterData = data.filter(it=>it.state==true);
		
		if(filterData.length == 0) {
			cmmMessage.alert("<spring:message code='qms.msg_000006' text='삭제 대상을 선택해주시기 바랍니다.'/>");
			return;
		}
		
		var cData = JSON.stringify(filterData);
		
		jQuery.ajaxSettings.traditional = true;
		
		$.ajax({
			url: "deleteAttachFile.do"
			,data: cData
			,type: 'POST'
			,dataType: "json"
			,contentType: "application/json;charset=utf-8"
			,async: false
			,beforeSend : function(xmlHttpRequest){
				grid.showLoading();
 				xmlHttpRequest.setRequestHeader("AJAX", "true"); // ajax 호출을 header에 기록
 			}
			,success: function (result) {
				
				var resultCode = result.resultCode;
				if (resultCode == "E") {
					cmmMessage.alert("삭제 중 오류가 발생하였습니다.\n" + result.resultMsg);
				} else {
// 					cmmMessage.alert(result.resultMsg);
					
					//재조회
					cmmAttachFile.search();
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
}


/* 단건 첨부파일 */
var cmmAttachFileOne = {
		
	oParamAttachFileOne : {}
	,
	callbackOne : new Object()
	,
	/* 업로드 */
	upload : function(param,callback){
		
		oParamAttachFileOne 	= param;
		callbackOne = callback;
		
		//파일 Input 초기화
		$("#popupCmmAttachFile").find("#fileOne").val("");
		document.getElementById("fileOne").click();
	}
	,
	/* 파일 선택 */
	selectFile : function(){

		$('#fileFacId')	.val(oParamAttachFileOne.facId);		<%--공장 ID --%>
		$('#programId')	.val(oParamAttachFileOne.programId);	<%--프로그램 ID --%>
		$('#fileSeq')	.val(oParamAttachFileOne.fileSeq); 	<%-- 파일 시퀀스 --%>
		
		var form = new FormData(document.getElementById('fileForm'));

		$.ajax({
			  url: "uploadAttachFileOne.do"
			, data: form
			, dataType: 'text'
			, processData: false
			, contentType: false
			, type: 'POST'
			, beforeSend : function(xmlHttpRequest){
				xmlHttpRequest.setRequestHeader("AJAX", "true"); // ajax 호출을  header에 기록
			}
			, success: function (response) { 
				
				let result = JSON.parse(response);
// 				cmmMessage.alert(result.resultMsg);
				
				let resultCode = result.resultCode;
				if(resultCode == "E") {
					cmmMessage.alert(result.resultMsg);
				} else {
					oParamAttachFileOne.fileSeq = result.fileSeq;
					oParamAttachFileOne.fileNm = result.fileNm;
					callbackOne(oParamAttachFileOne);
				}
				
			}
			, error: function (jqXHR) { 
				console.log('error'); 
			}
			, complete : function() {

			}
		});
	}
	,
	/*Hash파일명, 파일명 조회*/
	findHashFileNm : function(param,callback){
		$.ajax({
            url: "getAttachFile.do",
            data: param,
            type: 'POST',
            beforeSend : function(xmlHttpRequest){
				xmlHttpRequest.setRequestHeader("AJAX", "true"); // ajax 호출을  header에 기록
			},
            success: function (result) {
                var resultList = result.resultList;
                
                if (resultList.length < 1) {
                	cmmMessage.alert("첨부파일이 존재하지 않습니다.");
                } else {
                	callback(resultList);
                }
            },
            error: function (result) {
                console.log(result);
            },
            complete : function() {

			}
        });
	}
	,
	/*파일 다운로드*/
	download : function(param){
		cmmAttachFileOne.findHashFileNm(param, function(data){
			location.href = "downAttachFile.do?"
    			+ "fileHashNm=" + data[0].fileHashNm + "&"
    			+ "fileNm=" 	+ data[0].fileNm ;
		});
	}
	,
	/* 파일 삭제 */
	deleteFile : function(param,callback){
		
		cmmAttachFileOne.findHashFileNm(param, function(data){
			
			var cData = JSON.stringify(data);
			
			jQuery.ajaxSettings.traditional = true;
			
			$.ajax({
				url: "deleteAttachFile.do"
				,data: cData
				,type: 'POST'
				,dataType: "json"
				,contentType: "application/json;charset=utf-8"
				,async: false
				,beforeSend : function(xmlHttpRequest){
	 				xmlHttpRequest.setRequestHeader("AJAX", "true"); // ajax 호출을 header에 기록
	 			}
				,success: function (result) {
					
					var resultCode = result.resultCode;
					if (resultCode == "E") {
						cmmMessage.alert("삭제 중 오류가 발생하였습니다.\n" + result.resultMsg);
					} else {
// 						cmmMessage.alert(result.resultMsg);
						param.fileSeq = "";
						callback(param);
					}
				}
				,error:function(xhr, textStatus, error){
					if(xhr.status=="404"){ // watch
						alert("Login Session Expired");
						location.href = "${contextPath}/reDirectToMain.do";
					}
				}
				,complete : function() {
					
				}
			}); 
		});
	}
}
	
	
	
</script>