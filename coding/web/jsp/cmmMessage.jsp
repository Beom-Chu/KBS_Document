
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib  prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="msg-wrap">
	
	<%--
    <section>
        <button type="button" id="confirm">컨펌창</button>
        <button type="button" id="alert">경고창</button>
    </section>
     --%>
	
    <%-- confirm 모달을 쓸 페이지에 추가 start--%>
    <section class="msg-modal msg-modal-section type-confirm">
        <div class="enroll_box">
            <p class="menu_msg"></p>
        </div>
        <div class="enroll_btn">
            <button class="msg-btn gray_btn msg-btn-ok"><spring:message code="qms.lb_confirm" text="확인"/></button>
            <button class="msg-btn gray_btn msg-modal-close"><spring:message code="qms.lb_cancel" text="취소"/></button>
        </div>
    </section>

    <%-- alert 모달을 쓸 페이지에 추가 start--%>
    <section class="msg-modal msg-modal-section type-alert">
        <div class="enroll_box">
            <p class="menu_msg"></p>
        </div>
        <div class="enroll_btn">
            <button class="msg-btn gray_btn msg-modal-close"><spring:message code="qms.lb_confirm" text="확인"/></button>
        </div>
    </section>

</div>
    
<script type="text/javascript">


/**
 *  alert, confirm 대용 팝업 메소드 정의 <br/>
 *  timer : 애니메이션 동작 속도 <br/>
 *  alert : 경고창 <br/>
 *  confirm : 확인창 <br/>
 *  open : 팝업 열기 <br/>
 *  close : 팝업 닫기 <br/>
 */ 
var cmmMessage = {
    timer: 0,
    confirm: function (txt, callback) {
        if (txt == null || txt.trim() == "") {
            console.warn("confirm message is empty.");
            return;
        } else if (callback == null || typeof callback != 'function') {
            console.warn("callback is null or not function.");
            return;
        } else {
            $(".type-confirm .msg-btn-ok").on("click", function () {
            	cmmMessage.close(this);
            	$(this).unbind("click");
                callback(true);
            });
            this.open("type-confirm", txt);
        }
    },

    alert: function (txt) {
        if (txt == null || txt.trim() == "") {
            console.warn("confirm message is empty.");
            return;
        } else {
            this.open("type-alert", txt);
        }
    },

    open: function (type, txt) {
        var popup = $("." + type);
        popup.find(".menu_msg").html(txt);
        $("body").append("<div class='dimLayer'></div>");
        $(".dimLayer").css('height', $(document).height()).attr("target", type);
        popup.fadeIn(this.timer);
    },

    close: function (target) {
        var modal = $(target).closest(".msg-modal-section");
        var dimLayer;
        if (modal.hasClass("type-confirm")) {
            dimLayer = $(".dimLayer[target=type-confirm]");
        } else if (modal.hasClass("type-alert")) {
            dimLayer = $(".dimLayer[target=type-alert]")
        } else {
            console.warn("close unknown target.")
            return;
        }
        modal.fadeOut(this.timer);
        setTimeout(function () {
            dimLayer != null ? dimLayer.remove() : "";
        }, this.timer);
    }
}
 
 
 
 $(function () {
	 
	//메시지 창 닫기
	$(".msg-modal-close").on("click", function () {
		cmmMessage.close(this);
	});
	
	//ESC 입력으로 메시지창 닫기
	$(document).keydown(function(event) {
	    if ( event.keyCode == 27 || event.which == 27 ) {
	    	$(".msg-modal-close").each(function(){
	    		cmmMessage.close($(this));
	    	});
	    }
	});
	
	<%--
    $(document).on("click", "#confirm", function () {
    	
        cmmMessage.confirm("저장 하시겠습니까?", function (res) {
            if (res) {
                cmmMessage.alert("확인창을 눌렀습니다.");
            }
        });
       
    });
    
    $(document).on("click", "#alert", function () {
    
        cmmMessage.alert("조회가 완료되었습니다.");
        
    });
 	--%>
 	
});

</script>