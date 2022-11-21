package edu.kh.project.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Test {

	public static void main(String[] args) throws ParseException {
		
		// Date : 날짜용 객체
		// Calendar : Date 업그레이드 객체
		// SimpleDateFormat : 날짜를 원하는 형태의 문자열로 변환

		// 오늘 23시 59분 59초까지 남은 시간을 초단위로 구하기
		
		// Date : 날짜용 객체
		
		Date a =  new Date(); // 현재 시간
		Date b =  new Date(1669006255327l); // 현재 시간
		System.out.println(a);
		System.out.println(b);
		
		
		// 기준 시간 : 1970년 1월 1일 09시 00분 00초
		// new Date(ms) : 기준 시간 + ms만큼 지난 시간
		
		
		// Calendar : Date 업그레이드 객체 (기본생성자 없음)
		Calendar cal = Calendar.getInstance();
		System.out.println(cal);
		
		// cal.add(단위, 추가할 값);
		cal.add(cal.DATE, 1); // 날짜에 1 추가
		
				
		// SimpleDateFormat : 날짜를 원하는 형태의 문자열로 변환
		// SimpleDateFormat을 이용해서 cal 날짜 중 시, 분, 초를 0:0:0으로 바꿈
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // mm 소문자로하면 Minutes
		
		Date temp = new Date(cal.getTimeInMillis());
		// 하루 증가한 내일 날짜의 ms값을 이용해 Date 객체 생성
		
		Date c = sdf.parse(sdf.format(temp));
				// parse : 날짜 형식 String -> Date로 변환 

		System.out.println(a);
		System.out.println(temp);
		System.out.println(c);
		
		// 내일 자정 ms - 현재 시간 ms
		  long diff = c.getTime() - a.getTime();
	      System.out.println( diff/1000 ); // 23:59:59초 까지 남은 시간 (s)

	}

}
