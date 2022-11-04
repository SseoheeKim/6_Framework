// 제출form 전체 유효성 검사 

// 이메일 검사 플래그를 사용하는 방법도 있지만,
// let emailCheck = false;

// JS객체를 이용!! 유효성 검사 결과 저장 객체를 만들어 한번에 사용
// JS객체 = {"K":V, "K":V, ...} -> Map형식 / Key는 무조건 String

// 변수명.key 또는 변수명["Key"]를 이용하면 객체 속성 접근 가능
const checkObj = {
    "memberEmail"     :false,
    "memberPw"        :false,
    "memberPwConfirm" :false,
    "memberNickname"  :false,
    "memberTel"       :false
};

// 회원 가입 양식이 제출되었을 때
document.getElementById("signUp-frm").addEventListener("submit", function(e){
    
    // 이메일이 유효한가?
    // if(!emailCheck) {
    //     alert("이메일이 유효하지 않습니다.");
    //     e.preventDefault(); // 제출되지 않도록 
    // }

    // 위에서 지정한 JS객체 사용하여 유효성 확인하기
    // checkObj 속성 중 하나라도 false가 있다면 제출 이벤트를 제거

    // for in : 객체의 key값을 순서대로 접근하는 반복문
    // for(let 변수명 in 객체명)
    // -> 객체에서 순서대로 key를 하나씩 꺼내 왼쪽 변수에 저장(변수명이 key)

    for(let key in checkObj){
        
        let str;

        // checkObj 속성 하나를 꺼내 값을 검사했는데 false인 경우 
        if(!checkObj[key]) {

            switch(key){
                case "memberEmail" : str = "이메일이 유효하지 않습니다."; break;
                case "memberPw" : str = "비밀번호가 유효하지 않습니다."; break;
                case "memberPwConfirm" : str = "비밀번호가 유효하지 않습니다."; break;
                case "memberNickname" : str = "닉네임이 유효하지 않습니다."; break;
                case "memberTel" : str = "전화번호가 유효하지 않습니다."; break;
            }
            
            alert(str); // 대화상대 출력

            document.getElementById(key).focus(); // 유효하지 않은 입력포커스로 이동
            e.preventDefault(); // 제출 이벤트 제거
            return;
        }
    }
});



// //////////////////////////////////////////////////////////////////////////////////////////////////
// 이메일 유효성 검사 
const memberEmail = document.getElementById("memberEmail"); //input
const emailMessage = document.getElementById("emailMessage"); //span

//input 이벤트 : input태그에 입력이 되었을 경우(모든 입력 인식)
memberEmail.addEventListener("input", function(){

    // 실제 문자가 입력되지 않는 경우(방향키로 조절, 스페이스 등 제외)
    if(memberEmail.value.trim().length==0){
        emailMessage.innerText = "메일을 받을 수 있는 이메일을 입력해주세요.";
        memberEmail.value = "";

        // 초록글씨, 빨간글씨를 제거하고 검정 글씨 만들기
        emailMessage.classList.remove("confirm", "error");

        // 유효성 검사 확인 객체에 현재 상태를 저장
        checkObj.memberEmail = false;
        return;
    }

    // 정규표현식을 이용한 유효성 검사
    const regEx = /^[a-zA-Z\d\-\_]{4,}\@[가-힣\w\-\_]+(\.\w+){1,3}$/;

    if(regEx.test(memberEmail.value)){ // 이메일이 유효한 경우
        emailMessage.innerText = "유효한 형식의 이메일입니다.";
        emailMessage.classList.add("confirm");
        emailMessage.classList.remove("error");

        // 유효성 검사 확인 객체에 현재 상태를 저장
        checkObj.memberEmail = true;


    } else { // 유효하지 않은 경우
        emailMessage.innerText = "이메일 형식이 유효하지 않습니다.";
        emailMessage.classList.add("error");
        emailMessage.classList.remove("confirm");

        // 유효성 검사 확인 객체에 현재 상태를 저장
        checkObj.memberEmail = false;

    }

});

// //////////////////////////////////////////////////////////////////////////////////////////////////
// 비밀번호 유효성 검사

const memberPw = document.getElementById("memberPw");
const memberPwConfirm = document.getElementById("memberPwConfirm");
const pwMessage = document.getElementById("pwMessage");


// 비밀번호 입력 시
memberPw.addEventListener("input", function(){

    // 비밀번호가 하나도 입력되지 않은 경우
    if( memberPw.value.trim().length == 0 ) {
        pwMessage.innerText = "영어, 숫자, 특수문자(!,@,#,-,_)를 포함한 6~20글자 사이로 입력해주세요.";
        memberPw.value="";
        pwMessage.classList.remove("confirm", "error");
        checkObj.memberPw = false;
        return;
    }

    // 정규표현식을 통한 비밀번호 유효성 검사
    const regEx = /^[a-zA-Z\d!@#-_]{6,20}$/;

    if(regEx.test(memberPw.value)){
        checkObj.memberPw = true;

        // 유효한 비밀번호 + 확인 작성 X
        if( memberPwConfirm.value.trim().length == 0 ) {
            pwMessage.innerText = "비밀번호가 유효합니다.";
            pwMessage.classList.add("confirm");
            pwMessage.classList.remove("error");
        } else { // 유효한 비밀번호 + 확인 작성 O
            // 비밀번호가 입력될 때 비밀번호 확인에 작성된 값과 일치하는 경우
            if( memberPw.value == memberPwConfirm.value ) {
                pwMessage.innerText = "입력하신 비밀번호가 일치합니다.";
                pwMessage.classList.add("confirm");
                pwMessage.classList.remove("error");
                checkObj.memberPwConfirm = true;

            } else {
                pwMessage.innerText = "입력하신 비밀번호가 일치하지 않습니다.";
                pwMessage.classList.add("error");
                pwMessage.classList.remove("confirm");
                checkObj.memberPwConfirm = false;
            }
        }

    } else { 
        pwMessage.innerText = "입력하신 비밀번호가 유효하지 않습니다.";
        pwMessage.classList.add("error");
        pwMessage.classList.remove("confirm");

        checkObj.memberPw = false;
    }
});



// 비밀번호 확인하기 입력 유효성 검사 == 앞의 비밀번호와 일치하는지 검사
memberPwConfirm.addEventListener("input", function(){
    
    // 비밀번호가 유효한 경우에만 비밀번호와 비밀번호 확인이 같은지 비교
    if(checkObj.memberPw==true){
        
        // 비밀번호와 비밀번호 확인이 같은지 검사
        if( memberPwConfirm.value == memberPw.value ) {
            pwMessage.innerText = "입력하신 비밀번호가 일치합니다."
            pwMessage.classList.add("confirm");
            pwMessage.classList.remove("error");
            checkObj.memberPwConfirm = true;
    
    
        } else {
            pwMessage.innerText = "입력하신 비밀번호가 일치하지 않습니다."
            pwMessage.classList.add("error");
            pwMessage.classList.remove("confirm");
            checkObj.memberPwConfirm = false;
    
        }

    } else { // 비밀번호가 유효하지 않은 경우
        checkObj.memberPwConfirm = false;
    }

});



// //////////////////////////////////////////////////////////////////////////////////////////////////
// 닉네임 유효성 검사
const memberNickname = document.getElementById("memberNickname");
const nickMessage = document.getElementById("nickMessage");

memberNickname.addEventListener("input", function(){
    
    // 닉네임에 문자가 입력되지 않은 경우
    if(memberNickname.value.trim().length == 0 ){
        nickMessage.innerText = "닉네임은 한글, 영어, 숫자로만 2~10글자로 작성하세요."
        nickMessage.classList.remove("confirm","error");
        checkObj.memberNickname = false;
        return; 
    }

    // 정규표현식으로 닉네임 확인
    const regEx = /^[가-힣\w]{2,10}$/;
                        /* a-zA-Z0-9 */

    
    if(regEx.test(memberNickname.value)){ // 닉네임이 유효한 경우

        // ************ 닉네임 중복 검사 코드 추가 예정 ************

        nickMessage.innerText ="유효한 닉네임입니다."
        nickMessage.classList.add("confirm");
        nickMessage.classList.remove("error");
        checkObj.memberNickname = true;
        
    } else { // 유효하지 않을 경우
        nickMessage.innerText ="유효하지 않은 닉네임입니다."
        nickMessage.classList.add("error");
        nickMessage.classList.remove("confirm");
        checkObj.memberNickname = false;

    }
});


// //////////////////////////////////////////////////////////////////////////////////////////////////
// 전화번호 유효성 검사

const memberTel = document.getElementById("memberTel");
const telMessage = document.getElementById("telMessage");

memberTel.addEventListener("input", function(){

    if(memberTel.value.trim().length == 0) {
        telMessage.innerText = "전화번호를 입력해주세요.(-제외)"
        telMessage.classList.remove("confirm", "error");
        checkObj.memberTel = false;
        return;
    }

    // 전화번호 정규 표현식 검사
    // 010 011 016 017 019 등
    // 중간이 3자리 321 123 등

    const regEx = /^0(1[01679]|2|[3-6][1-5]|70)[1-9]\d{2,3}\d{4}$/;

    if(regEx.test(memberTel.value)){
        telMessage.innerText = "유효한 전화번호입니다.";
        telMessage.classList.add("confirm");
        telMessage.classList.remove("error");
        checkObj.memberTel = true;
    } else {
        telMessage.innerText = "유효하지 않은 형식의 전화번호입니다.";
        telMessage.classList.add("error");
        telMessage.classList.remove("confirm");
        checkObj.memberTel = false;
    }



});
