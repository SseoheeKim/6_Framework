package edu.kh.project.common;

import java.text.SimpleDateFormat;

// 유용한 기능은 모아둔 클래스
public class Util {
	   
	   // 파일명 변경 메소드 20221114123350_15134.png
	   public static String fileRename(String originFileName) {
	      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	      String date = sdf.format(new java.util.Date(System.currentTimeMillis()));
	      // 현재 시간을 기준으로해서 파일명으로 설정

	      int ranNum = (int) (Math.random() * 100000); // 5자리 랜덤 숫자 생성

	      String str = "_" + String.format("%05d", ranNum);

	      String ext = originFileName.substring(originFileName.lastIndexOf("."));

	      return date + str + ext;
	   }
}
