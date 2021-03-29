	
$(document).ready(function() {

});
	
//공통코드 유틸	
var cmmCdUtil = {
	
	/* 공통코드 조회 */
	fn_getCmmCd : function(param, callback){
		
		var returnData = [];
		var selParam = {};
		
		//파라미터가 문자 타입인 경우 코드ID로 설정
		if(typeof param === "string"){
			selParam.cdTpId = param;
		} else {
			selParam = param;
		}
		
		$.ajax({
            url: "getQmsCmmCd.do",
            data: selParam,
            type: 'POST',
            beforeSend : function(xmlHttpRequest){
				xmlHttpRequest.setRequestHeader("AJAX", "true"); // ajax 호출을  header에 기록
			},
            success: function (result) {
            	
                if (result.resultList.length > 0) {
                	returnData = result.resultList;
                }
            },
            error: function (result) {
                console.log(result);
            },
            complete : function() {
            	callback(returnData);
			}
        });
	}	
	,

	/** 공통코드ID로 select box 설 정
	 * obj 		: Jquery Select객체
 	 * sCdTpId	: 공통코드ID  */
	fn_setSelectCode : function(obj, sCdTpId){
		
		//공통코드 조회
		cmmCdUtil.fn_getCmmCd(sCdTpId,function(data){
			cmmCdUtil.fn_setSelectData(obj, data);
		});
	}
	,
	
	/** Json Data로 select box 설정
	 * obj  : Jquery Select객체
	 * data : Json(Select option Data) */
	fn_setSelectData : function(obj, data){
		
		//Select Option 초기화 (첫번째 항목은 제외[선택,전체])
		obj.children("option").not(":first").remove();
		
		//Select Option 설정
		data.forEach(function(item){
			if(item.cdVal != undefined) obj.append("<option value='"+item.cdVal+"'>"+item.cdValNm+"</option>");
			else obj.append("<option value='"+item.value+"'>"+item.text+"</option>");
		});
	}
	,
	
	/** 공통코드ID로 그리드 Select 공통코드 설정 
	 * sGridId   : 그리드 ID
	 * sDataIndx : 대상 컬럼 ID(dataIndx)
	 * sCdTpId   : 공통코드ID */
	fn_setGridSelectCode : function(sGridId,sDataIndx,sCdTpId){
		
		//공통코드 조회
		cmmCdUtil.fn_getCmmCd(sCdTpId,function(data){
			cmmCdUtil.fn_setGridSelectData(sGridId,sDataIndx,data);
		});
	}
	,
	
	/** Json Data로 그리드 Select 공통코드 설정
	 * sGridId   : Grid ID 
	 * sDataIndx : 대상 컬럼 ID(dataIndx)
	 * data      : Json(Select option Data) */
	fn_setGridSelectData : function(sGridId,sDataIndx,data){
		
		let grid = pq.grid("#"+sGridId);
		let colModel = grid.option("colModel");
		let findColMdl = colModel.find(item => item.dataIndx==sDataIndx);

		if(findColMdl != undefined){
			findColMdl.editor.options = data;
		}
	}
	,
	
	/** function, Json Data로 그리드 Select 공통코드 설정
	 * sGridId   : Grid ID 
	 * sDataIndx : 대상 컬럼 ID(dataIndx)
	 * fn 		 : function
	 * data      : Json(Select option All Data) */
	fn_setGridSelectFn : function(sGridId,sDataIndx,fn,data){
		
		let grid = pq.grid("#"+sGridId);
		let colModel = grid.option("colModel");
		let findColMdl = colModel.find(item => item.dataIndx==sDataIndx);

		if(findColMdl != undefined){
			findColMdl.editor.options = fn;
			findColMdl.editor.allOptions = data;	//필터 전 모든 Data
		}
	}
	,
	
	/** 그리드 Data Value 값으로 Text 설정 
	 * sGridId : 그리드ID */
	fn_setGridSelText : function(sGridId){
		
		let grid = pq.grid("#"+sGridId);
		let colMdl = grid.option("colModel");
		let data = grid.option("dataModel.data");
		let filterColMdl = colMdl.filter(item => item.editor!=undefined && item.editor.type == "select");
		
		filterColMdl.forEach(function(colM){
			if(colM.editor.type == "select") {
				let optionData = colM.editor.options;
				//option 타입이 function인 경우
				if(typeof optionData == "function") optionData = colM.editor.allOptions;
				let mapIndices = colM.editor.mapIndices;
				if(optionData != undefined) {
					data.forEach(function(dat){
						dat[mapIndices.text] = getArrayText(optionData,dat[mapIndices.value]);
					});
				}
			}
		});
		
		grid.refreshDataAndView();
	}
	,
	
	/** 그리드 Select 컬럼 붙여넣기 이벤트 설정 
	 * 붙여넣기, Delete시 value값 자동 변경
	 * sGridId : 그리드 ID */
	fn_setGridSelEvt : function(sGridId){
		
		let grid = pq.grid("#"+sGridId);
		let colMdl = grid.option("colModel");
		let filterColMdl = colMdl.filter(item => item.editor != undefined && item.editor.type == "select");
		
		grid.on("change", function(event, ui)
		{
			filterColMdl.forEach(function(colM){
				let optionData = colM.editor.options;
				//option 타입이 function인 경우
				if(typeof optionData == "function") optionData = colM.editor.allOptions;
				let mapIndices = colM.editor.mapIndices;
				ui.updateList.forEach(function(item){
					if(ui.source == "paste"){ //붙여넣기
						if(!fc_isNull(item.newRow[mapIndices.text])) item.rowData[mapIndices.value] = getArrayValue(optionData,item.newRow[mapIndices.text]);
					}else if(ui.source == "clear"){	//삭제
						if(!fc_isNull(item.oldRow[mapIndices.text])) item.rowData[mapIndices.value] = null;
					}
					
				});
			});
		});
	}
}
