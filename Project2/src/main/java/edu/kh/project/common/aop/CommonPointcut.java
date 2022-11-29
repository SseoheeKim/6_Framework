package edu.kh.project.common.aop;

import org.aspectj.lang.annotation.Pointcut;

// JoinPoint : 공통 코드가 삽입될 수 있는 advice 후보
// PointCut : 실제로 코드가 삽입될 부분

public class CommonPointcut { // PointCut을 모아둘 클래스
	
	// *	모든
	// .	바로 밑의 하위 경로
	// ..	모든 후손, 0개 이상
	// @Pointcut("[접근제한자] 반환형의 패키지명.클래스명.메서드명([파라미터])") 
	@Pointcut("execution(* edu.kh.project..*ServiceImpl.*(..))") // 모든 ServiceImpl
	public void serviceImplPointcut() { }
	
}
