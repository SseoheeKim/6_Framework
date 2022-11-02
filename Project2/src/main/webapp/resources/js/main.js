console.log("main.js loaded.");

// ----------------------------- 아이디 저장 -------------------------------------

// 아이디 저장 체크박스가 체크되었을 때에 대한 동작
const saveId = document.getElementById("saveId");

// radio, checkbox의 값이 변할 때 발생하는 이벤트는 change
saveId.addEventListener("change", function(){

    // change는 체크되거나, 해제될 때 이벤트가 발생
    // -> 체크되었는지 별도 검사가 필요

    // 이벤트 핸들러 내부의 this == 이벤트가 발생한 요소( 아이디 저장 checkbox )
    // console.log(this.checked);
    // (getter)체크박스.checked -> 체크된 경우 true, 체크 안된 경우false
    
    // (setter)체크박스.checked = true || false


    if(this.checked) {
        const str = "개인 정보 보호를 위해 개인 PC에서의 사용을 권장합니다."
                    + "개인 PC가 아닌 경우 취소버튼을 눌러주세요. ";

        // confirm -> 확인을 누르면 true, 취소 누르면 false            
        if( !confirm(str) ) { // 취소버튼 눌렀을 때

            // 취소누르면 체크박스의 체크 해제
            this.checked = false;
        } 
    }
});


// ------------------------- 로그인 유효성 검사 ---------------------------------
// 로그인 form태그 submit이벤트 취소하기


function loginValidate() {
    // Validate : 유효하다.

    // 요소 접근 방법 
    // document.querySelector("css선택자")
    // - css선택자와 일치하는 요소를 선택(얻어옴)
    // - 여러 요소가 배열 형태이지만 이 중 첫번째 요소만 얻어옴!

    // 이메일과 비밀번호 input요소
    const memberEmail = document.querySelector("[name='memberEmail']");
    const memberPw = document.querySelector("[name='memberPw']");

    // 이메일이 입력되지 않은 경우 false 반환
    if(memberEmail.value.trim().length == 0) {
        // 입력 시 공백이 존재할 수 있으므로 
        // 이메일의 양쪽 공백을 제거한 후 길이가 0이면 이메일 미작성
        
        // 알림창이 뜨고
        alert("이메일을 입력해주세요.");
        
        memberEmail.focus();  // 확인 버튼을 누르면 다시 아이디 입력란으로 커서가 위치
        memberEmail.value=""; // input요소에 작성된 값을 모두 삭제(공백)

        return false;
    } 

    // 비밀번호가 입력되지 않는 경우 false반환
    if(memberPw.value.trim().length == 0) {
        
        alert("비밀번호를 입력해주세요.");
        
        memberPw.focus();  // 확인 버튼을 누르면 다시 비밀번호 입력란으로 커서가 위치
        memberPw.value=""; // input요소에 작성된 값을 모두 삭제(공백)

        return false;
    } 


    return true;
}

