// 상세조회에서 "목록으로" 버튼
const goToListBtn = document.getElementById("goToListBtn");

// 익명함수 안의 this는 window객체를 가리킴
goToListBtn.addEventListener("click", ()=>{
    // location : 주소, 주소창과 관련된 내장 객체

    // location.href : 현재 주소(전체)를 반환
    // 예)'http://localhost/board/1/1998?cp=1'

    // location.href = "주소" : 작성된 주소 요청으로 이동
    // 예) location.href = "http://localhost" 

    // location.pathname = 현재 요청 주소만 반환(프로토콜, ip, 포트 제외)
    // 예) /board/1/1998

    // location.search : 쿼리 스트링만 반환
    // 예) '?cp=3'

    const pathname = location.pathname; // /board/1/1998
    const queryString = location.search;  // ?cp=3
    const url = pathname.substring(0,pathname.lastIndexOf("/")) + queryString; //  /board/1?cp=3
    // subString & lastIndexOf 사용

    location.href = url;
});



// 좋아요 버튼 클릭 시 동작
// (전역변수 memberNo, boardNo 사용 - boardDetail.jsp )
const boardLike = document.getElementById("boardLike");
boardLike.addEventListener("click", e => {

    // 로그인 상태가 아닌경우
    if(memberNo == "") {
        alert("로그인 후 이용해주세요.");
        return;
    }

    const likeCount = e.target.nextElementSibling;

    // 로그인 상태이면서 좋아요 상태가 아닌 경우
    // contains : 포함하는지 확인
    if(e.target.classList.contains('fa-regular')) { // 빈 하트인 경우
        $.ajax({
            url: "/boardLikeUp",
            data: {"boardNo" : boardNo, "memberNo" : memberNo},
            type: "get",
            success : (result) => {
                if(result > 0) { // 성공
                    e.target.classList.remove('fa-regular'); // 빈하트 클래스 삭제
                    e.target.classList.add('fa-solid'); // 채워진 하트 클래스 추가
                    likeCount.innerText = Number(likeCount.innerText) + 1; // 갯수 1 증가
                } else { // 실패
                    console.log("하트업실패");
                }
            },
            error: () => { console.log("하트업 중 에러"); }
        }); 

    } 
    // 로그인 상태이면서 좋아요 상태인 경우
    else { // 채워진 하트인 경우

        $.ajax({
            url: "/boardLikeDown",
            data: {"boardNo" : boardNo, "memberNo" : memberNo},
            type: "get",
            success: (result) => {
                if(result > 0) {
                    e.target.classList.remove('fa-solid'); // 채워진 하트 클래스 제거
                    e.target.classList.add('fa-regular'); // 빈하트 클래스 추가
                    likeCount.innerText = Number(likeCount.innerText) -1 ; // 갯수 1 감소
                }
            },
            error:()=>{console.log("하트다운 중 에러");}
        });
    }
});



// 게시글 삭제
const deleteBtn = document.getElementById("deleteBtn");
deleteBtn.addEventListener("click", ()=>{

    if(confirm("게시글을 삭제하시겠습니까?")) {
        // /board/{boardCode}/{boardNo}/delete GET방식
        // 삭제 후 /board/{boardCode}

        location.href = location.pathname + "/delete";
                        // /board/1/1998/delete
    }
});