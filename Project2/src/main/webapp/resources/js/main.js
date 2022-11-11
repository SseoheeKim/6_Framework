// console.log("main.js loaded.");

// ----------------------------- 아이디 저장 -------------------------------------

// 아이디 저장 체크박스가 체크되었을 때에 대한 동작
const saveId = document.getElementById("saveId");
if (saveId != null) {

    // radio, checkbox의 값이 변할 때 발생하는 이벤트는 change
    saveId.addEventListener("change", function(event){

        console.log(event);
        
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
}


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


// ------------------------------------------------------------------------------

// 이메일로 회원 정보 조회(AJAX)
const inputEamil = document.getElementById("inputEmail");
const selectEmail = document.getElementById("selectEmail");
selectEmail.addEventListener("click", (e) => {
    // 아무것도 입력되지 않은 상태일 때 
    if(inputEamil.value.trim().length==0){
        inputEamil.value = "";
        return;
    } 
    
    $.ajax({
        url : "/selectEmail",
        data : { "email" : inputEamil.value },
        type : "post",
        dataType : "JSON", // 응답데이터 형식이 JSON -> 자동으로 JS객체
        success : (member) => {
            // console.log(member);
            
            // 1. JSON형태의 문자열로 반환된 경우(JSON-> JS객체)
            // 방법1) JSON.parse(문자열)
            // console.log(JSON.parce(member));
            // 방법2) dataType : "JSON"추가
            
            // 2. Jackson라이브러리

            // ----------------------------------------------------------

            if(member == null) {
                // h4요소 생성
                const h4 = document.createElement("h4");
                
                // 내용 추가
                h4.innerText = inputEamil.value + "은/는 존재하지 않습니다.";

                // append(요소) : 마지막 자식으로 추가
                // prepend(요소) : 첫 번째 자식으로 추가
                // after(요소) : 다음(이후)에 추가
                // before(요소) : 이전에 추가

                // selectEmail에 다음 요소가 이미 존재한다면 삭제
                if( selectEmail.nextElementSibling != null) {
                    selectEmail.nextElementSibling.remove();
                } 
                
                // #selectEmail(button)의 다음 요소로 추가(.after(요소))
                selectEmail.after(h4);
                
            } else {  // 일치
                
                const ul = document.createElement("ul");

                const li1 = document.createElement("li");
                li1.innerHTML = "회원번호 : " + member.memberNo +"<br>";
                
                const li2 = document.createElement("li");
                li2.innerHTML = "이메일 : " + member.memberEmail +"<br>";
                
                const li3 = document.createElement("li");
                li3.innerHTML = "닉네임 : " + member.memberNickname +"<br>";
                
                const li4 = document.createElement("li");
                li4.innerHTML = "주소 : " + member.memberAddress +"<br>";

                const li5 = document.createElement("li");
                li5.innerHTML = "가입일 : " + member.enrollDate +"<br>";

                const li6 = document.createElement("li");
                li6.innerHTML = "탈퇴여부 : " + member.memberDeleteFlag+"<br>";

                ul.append(li1,li2,li3,li4,li5,li6);

                if( selectEmail.nextElementSibling != null) {
                    selectEmail.nextElementSibling.remove();
                } 
                // #selectEmail(button)의 다음 요소로 추가(.after(요소))
                selectEmail.after(ul);
            }
        },
        error: ()=>{
            console.log("이메일로 조회하기 실패");
        }
    });

});


// --------------------------------------------------------------------------
// 비동기로 회원 전체를 조회하는 함수 선언 및 정의
function selectMemberList() {
    
    const tbody = document.getElementById("tbody");
    
    // tbody 안의 이전 내용 삭제
    tbody.innerHTML = "";

    $.ajax({
        url : "/selectMemberList",
        dataType: "JSON", // 응답데이터는 JSON이다 자동으로 JS객체로 바꿔라
        success : (memberList) => {
            console.log(memberList);

            for(let member of memberList){
                const tr = document.createElement("tr");
                
                //탈퇴한 회원인경우
                if(member.memberDeleteFlag === 'Y'){
                    tr.classList.add("secession");
                }

                // 회원번호
                const th = document.createElement("th");
                th.innerText = member.memberNo;

                // 이메일
                const td1 = document.createElement("td");
                td1.innerText = member.memberEmail;

                // 탈퇴여부
                const td2 = document.createElement("td");
                td2.innerText = member.memberDeleteFlag;

                // 행 안에 자식요소로 th, td추가
                tr.append(th, td1, td2);

                // tbody안에 자식요소로 tr추가
                tbody.append(tr);
            }

            // 회원 수 출력
            document.getElementById("memberCount").innerText = memberList.length + "명";

        },
        error: ()=>{
            console.log("회원 목록 조회 실패");
        }
    });
}


// HTML문서가 모두 읽어진 후 selectMemberList() 바로 호출, 그 다음엔 10초마다 호출

// HTML로딩이 끝났을 때
document.addEventListener("DOMContentLoaded", ()=>{
    selectMemberList();

    setInterval(selectMemberList, 10000);

});
