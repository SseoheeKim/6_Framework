// //////////////////////////////////////////////////////////////////////////////////////////////////////
//비밀번호 변경 유효성 검사

// 비밀번호 변경 Form 요소
// inline형식으로 이벤트 처리를 하지 않을 것

const changePwForm = document.getElementById("changePwForm");

if(changePwForm != null) { //changePwForm 요소가 존재할 때(현재 myPage-changePw.jsp에만 존재!)
    
    changePwForm.addEventListener("submit",function(event){
        // 이벤트 핸들러의 매개변수 event || e
        // -> 현재 발생한 이벤트 정보를 가지고 있는 event객체가 전달된다.

        console.log(event);

        // 비밀번호 변경에 사용되는 input요소 모두 얻어오기
        const currentPw = document.getElementById("currentPw");
        const newPw = document.getElementById("newPw");
        const newPwConfirm = document.getElementById("newPwConfirm");

        // 현재 비밀번호가 작성되지 않았을 때
        if( currentPw.value.trim().length == 0 ) {
            //  currentPw.value.trim() == ""  -> 문자비교로도 가능하지만 숫자비교가 더 빠르당

            // alert("현재 비밀번호가 작성되지 않았습니다. 현재 비밀번호를 입력해주세요.");
            // currentPw.focus();
            // currentPw.value="";
            alertAndFocus(currentPw,"현재 비밀번호가 작성되지 않았습니다. 현재 비밀번호를 입력해주세요.");
            // return false; --> 인라인 이벤트 모델 onsubmit ="return 함수명()"; 에서 사용


            event.preventDefault(); 
            // -> 이벤트를 수행하지 못하게 하는 함수 
            // -> Form의 기본 이벤트 onsubmit(제출하기)를 삭제
            return; 
            // 함수 종료
            // 세 개 다 입력하지 않았을 때 얼럿창이 세개 뜨니까 이런 점을 방지하기 위해 각각 함수를 종료 
        }

        // 새 비밀번호가 작성되지 않았을 때
        if( newPw.value.trim().length==0 ) {
            // alert("새 비밀번호가 작성되지 않았습니다.");
            // newPw.focus();
            // newPw.value="";
            alertAndFocus(newPw, "새 비밀번호가 작성되지 않았습니다.");
            event.preventDefault();
            return;
        }


        // 새 비밀번호가 확인이 작성되지 않았을 때
        if( newPwConfirm.value.trim().length==0 ) {
            
            // alert("새 비밀번호 확인이 작성되지 않았습니다.");
            // newPwConfirm.focus();
            // newPwConfirm.value="";
            alertAndFocus(newPwConfirm, "새 비밀번호 확인이 작성되지 않았습니다.");

            event.preventDefault();
            return;
        }


        // 비밀번호 정규식 검사 필요




        // 새 비밀번호, 새 비밀번호 확인이 같은지 검사
        if(newPw.value != newPwConfirm.value){
            alert("비밀번호 확인이 일치하지 않습니다.")
            newPwConfirm.focus();
            event.preventDefault(); // 기본 이벤트 제거
            return;
        }

    });
}


// 코드 중복 해결을 위해 경고창 출력 + 포커스 이동 + 입력창 값 삭제구문 함수를 추가
// 기본이벤트 제거를 추가하지 않은 이유는 매개변수로 또 받아서 이동해야하기 때문에 오류 발생가능성
// return은 현재 함수를 종료하는 것이기 때문에 해당안됨!
function alertAndFocus(input, str) {
    alert(str);

    input.focus();
    input.value="";
}



// //////////////////////////////////////////////////////////////////////////////////////////////////////
// 회원탈퇴 유효성 검사


// 1) 비번 미작성 시에 alert("비밀번호를 입력해주세요."); + 포커스 이동 + 입력창 값 내용 삭제
// 2) 동의 체크가 되지 않은 경우에 alert("탈퇴 동의하시면 체크를 눌러주세요."); + 포커스 이동
// 3) 1번과 2번이 유효할 때, 정말로 탈퇴할 것인지 확인하는 confirm 출력
//    (확인 클릭 -> 탈퇴 / 취소 -> 탈퇴 취소)   


// 표준 이벤트 모델
// const deleteForm = document.getElementById("deleteForm");

// if( deleteForm != null ) {

//     deleteForm.addEventListener("submit", function(event){
    
//         const memberPw = document.getElementById("memberPw");
        
//         if( memberPw.value.trim().length == 0 ){
//             alert("비밀번호를 입력해주세요.");
//             memberPw.focus();
//             memberPw.value = "";
//             event.preventDefault(); // 기본 이벤트 제거
//             return;
//         }
        
//         const agree = document.getElementById("agree")
        
//         if( !agree.checked ) {
//             alert("탈퇴 동의하시면 체크를 눌러주세요.");
//             agree.focus();
//             event.preventDefault(); // 기본 이벤트 제거
//             return;
//         }

//         // 정말 탈퇴할 것인지 검사
//         if( !confirm("정말 탈퇴하시겠습니까?") ) {
//             alert("탈퇴를 취소합니다.");
//             event.preventDefault(); // 기본 이벤트 제거
//             return;
//         }

//     });    
// }


// 인라인 모델로 회원 탈퇴 유효성 검사
// 인라인 모델보다는 표준 이벤트 모델로 작성하는 것이 좋다.
// -> 인라인 모델은 소스코드가 노출되어있기 때문에 보안성이 낮다.
function memberDeleteValidate(){

    const memberPw = document.getElementById("memberPw");
        

    if( memberPw.value.trim().length == 0 ){
        alert("비밀번호를 입력해주세요.");
        memberPw.focus();
        memberPw.value = "";
        return false; // 표준 이벤트 모델과 다른 부분
    }

    const agree = document.getElementById("agree")
        
    if( !agree.checked ) {
        alert("탈퇴 동의하시면 체크를 눌러주세요.");
        agree.focus();
        return false;
    }
    
    // 탈퇴 의사 확인
    if( !confirm("정말 탈퇴하시겠습니까?") ) {
        alert("탈퇴를 취소합니다.");
        return false;
    }


    return true;
}



// //////////////////////////////////////////////////////////////////////////////////////////////////////
// 프로필 이미지 변경

const profileImage = document.getElementById("profile-image");
const deleteImage = document.getElementById("delete-image");
const imageInput = document.getElementById("image-input");

// 프로필 수정 페이지에 초기 이미지 경로
const originalImage = profileImage.getAttribute("src");


// side메뉴에 탈퇴하기, 비번 수정하기 등 다양하게 존재하기때문에
// imageInput태그가 존재할 때(imageInput이 null이 아닐 때) 
if(imageInput != null) { // 프로필 이미지 수정 변경
    
    // input type="file" 요소의 값(value)이 없을 때는 ''(빈칸)
    // input type="file" 요소의 이전에 선택한 파일이 있었다가 취소하면 다시 ''(빈칸)
    // input type="file" 요소의 파일을 선택하면 change이벤트가 발생
    

    // 이미지가 선택되었을 때 미리보기
    // 파일선택의 값이 변했을 때(이미지 파일이 선택되었을 때)
    imageInput.addEventListener("change", e => {
        
        // e.target(이미지가 발생한 요소, imageInput)
        // 화살표 함수에서 this는 window의 객체를 의미하므로 사용XXXXX 
        
        console.log(e.target.files); // 선택된 파일의 목록(FileList)
        console.log(e.target.files[0]); // 파일 정보만 명시

        // 파일 정보를 선택했을 때는 ok
        // 파일 취소버튼을 누르면 마찬가지로 change이벤트가 발생하는데
        // 이때 읽어올 파일의 정보가 없는 오류가 발생하기 때문에
        if(e.target.files[0] != undefined) { // 선택된 파일이 있는 경우

            const reader = new FileReader();
            // FileReader(file을 읽는 객체)
            // - 웹 애플리케이션이 비동기적으로 데이터를 읽기 위해 읽을 파일을 가리키는 file 객체(자바스크립트에서 제공)
            // - 읽어들인 파일을 사용자 컴퓨터에 저장할 수 있다.
            
            
            reader.readAsDataURL(e.target.files[0]);
            // FileReader().readAsDataURL("파일정보");
            // -> 지정된 파일을 실제로 읽기 시작
            
            
            // FileReader().onload 
            // -> 파일 읽기가 완료되었을 때의 동작 지정
            reader.onload = e => {
                // console.log(e.target);
                // e.target.result : 읽어진 파일 결과(실제 이미지 파일)의 경로

                // img태그의 src속성으로 읽은 파일의 경로 추가
                // 이미지 미리보기
                profileImage.setAttribute("src", e.target.result)
            };

        } else { // 파일 선택에서 취소를 누른 경우
            
            // 초기 이미지로 변경
            profileImage.setAttribute("src", originalImage);

        }

        // x버튼이 클릭된 경우는 무조건 기본이미지로 변경
        deleteImage.addEventListener("click", ()=>{

            profileImage.setAttribute("src", "/resources/images/user.png");
            imageInput.value="";
        });

        

        
    })



}