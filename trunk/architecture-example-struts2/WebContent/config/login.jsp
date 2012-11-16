<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"
	import="architecture.ee.web.util.StringUtils"
	import="fuse21.fw.service.model.ModelMap"
	import="fuse21.fw.web.servlet.VelocityRendererServlet"
	import="javax.servlet.http.HttpServletRequest"
	import="javax.servlet.http.HttpServletResponse"
	import="org.apache.struts.action.ActionForm"
	import="org.apache.struts.action.ActionForward"
	import="org.apache.struts.action.ActionMapping"
	import="fuse21.fw.web.struts.action.DispatchActionSupport"
	import="fuse21.fw.web.util.ParamUtils"
%>
<%
	String companyNum = StringUtils.noNull(request.getParameter("cn"), "");
	String userName = StringUtils.noNull(request.getParameter("username"), "");
	String pwd = StringUtils.noNull(request.getParameter("password"), "");
	
	int count = wiznel.mtp.MyHttpSessionListener.getCount();
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>정보화 역량진단 시스템</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<script language="JavaScript" type="text/JavaScript">
	<!--
		function MM_swapImgRestore() { //v3.0
		  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
		}
	
		function MM_preloadImages() { //v3.0
		  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
		    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
		    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
		}
	
		function MM_findObj(n, d) { //v4.01
		  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
		    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
		  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
		  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
		  if(!x && d.getElementById) x=d.getElementById(n); return x;
		}
	
		function MM_swapImage() { //v3.0
		  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
		   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
		}
	
		//-------------------------------------------------------------
		//회사번호를 받아온다.
		var company_num = '<%= companyNum %>';
		//-------------------------------------------------------------

		 /**
		  * 쿠키 설정
		  * @param cookieName 쿠키명
		  * @param cookieValue 쿠키값
		  * @param expireDay 쿠키 유효날짜
		  */
		function setCookie( cookieName, cookieValue )
		{
			var today = new Date();
			today.setDate( today.getDate() + 1 );
			document.cookie = cookieName + "=" + escape( cookieValue ) + "; path=/; expires=" + today.toGMTString() + ";"
		}
		  /**
		  * 자신이 지정한 값으로 쿠키 설정
		  */
		function setMyCookie()
		{
			if(company_num != ""){
				setCookie( "companyNum", company_num);
			}
		}

		/**
		 * 쿠키값 추출
		 * @param cookieName 쿠키명
		 */
		function getCookie( cookieName ){
			var search = cookieName + "=";
			var cookie = document.cookie;

			// 현재 쿠키가 존재할 경우
			if( cookie.length > 0 ){
				// 해당 쿠키명이 존재하는지 검색한 후 존재하면 위치를 리턴.
				startIndex = cookie.indexOf( cookieName );

				// 만약 존재한다면
				if( startIndex != -1 ){
					// 값을 얻어내기 위해 시작 인덱스 조절
					startIndex += cookieName.length;

					// 값을 얻어내기 위해 종료 인덱스 추출
					endIndex = cookie.indexOf( ";", startIndex );

					// 만약 종료 인덱스를 못찾게 되면 쿠키 전체길이로 설정
					if( endIndex == -1) endIndex = cookie.length;

					// 쿠키값을 추출하여 리턴
					return unescape( cookie.substring( startIndex + 1, endIndex ) );
				}else{
					// 쿠키 내에 해당 쿠키가 존재하지 않을 경우
					return false;
				}
			}else{
				// 쿠키 자체가 없을 경우
				return false;
			}
		}

		

		function init(){
			//var wi = 640;
		 	 //var hi = 630;
			//var winli = (screen.width - wi) / 2;
		  	//var winti = (screen.height - hi) / 2;
		        
			//popobject = window.open("/notice.html", "notice", "width="+wi+", height="+hi+", scrollbars=yes, toolbar=no, location=no, resizable=no, status=no, menubar=no, titlebar=no,left="+winli+", top="+winti, false)
			//popobject.focus();

			//var wii = 480;
			//  var hii = 240;
			//	var winlii = (screen.width -wii) / 2;
			//  var wintii = (screen.height - hii) / 2;
			//window.open("/notice2.html", "notice2", "width="+wii+", height="+hii+", scrollbars=no, toolbar=no, location=no, resizable=no, status=no, menubar=no, titlebar=no,left="+winlii+", top="+wintii, false)
			
			//document.login_form.username.focus();

			<%
			try{
				ModelMap model = new ModelMap();
				model =(ModelMap)request.getAttribute(VelocityRendererServlet.MODEL_ATTRIBUTE);
				String gubun = "";
				int nowCnt = wiznel.mtp.MyHttpSessionListener.getCount();
				if(model!=null){
					gubun = (String)model.get("gubun");
				}
				if(gubun == null){
					gubun = "";
				}
				if(gubun.equals("1")){
					out.println("alert('회사번호가 넘어오지 않습니다.');");
				}else if(gubun.equals("2")){
					out.println("alert('아이디가 존재하지 않습니다.');");
				}else if(gubun.equals("3")){
					out.println("alert('비밀번호가 잘못되었습니다.');");
				}else if(gubun.equals("4")){
					out.println("sorrypop();");
				}//else if(gubun.equals("5")){
				//	out.println("alert('행정안전본부 산하 기관만 로그인이 가능한 기간입니다.');");
				//}
				//else{
				//	out.println("noticePop();");	
				//}

			}catch(Throwable e){

			}
			%>
			
			setMyCookie();

			//var w = 650;
	        	//var h = 580;
			//var winl = (screen.width - w) / 2;
	        	//var wint = (screen.height - h) / 2;
	        
			//popobject = window.open("/popup_klid.htm", "klid", "width="+w+", height="+h+", scrollbars=no, toolbar=no, location=no, resizable=no, status=no, menubar=no, titlebar=no,left="+winl+", top="+wint, false);
			//popobject.focus();
			

		}
		//-------------------------------------------------------------
	
		var popobject;
		function noticePop() {
			var wi = 500;
		 	var hi = 235;
			var winli = (screen.width - wi) / 2;
		  	var winti = (screen.height - hi) / 2;
			popobject = window.open("/popup3.htm", "notice", "width="+wi+", height="+hi+", scrollbars=0, toolbar=no, location=0, resizable=no, status=0, menubar=0, titlebar=no,left="+winli+", top="+winti, false)
			popobject.focus();
		}
		var popobject2;
		function sorrypop() {
			var w = 580;
	        	var h = 295;
			var winl = (screen.width - w) / 2;
	      		var wint = (screen.height - h) / 2;
			popobject2 = window.open("/userFullNoticePop.htm", "icap", "width="+w+", height="+h+", scrollbars=no, toolbar=no, location=no, resizable=no, status=no, menubar=no, titlebar=no,left="+winl+", top="+wint, false);
			popobject2.focus();
		}
	
		function enter(){
			var P = window.event.keyCode;
			if (P == 13 || P == 9) {
				document.login_form.password.focus();
			}
		}

		function enter2(){
			var P = window.event.keyCode;
			if (P == 13) {
				if(document.login_form.username.value==""){
					alert("아이디를 입력해주세요.");
					document.login_form.username.focus();
					return false;
				}
				if(document.login_form.password.value==""){
					alert("비밀번호를 입력해주세요.");
					document.login_form.password.focus();
					return false;
				}

				document.login_form.submit();
			}
		}

		function goLogin(){
			if(document.login_form.username.value==""){
				alert("아이디를 입력해주세요.");
				document.login_form.username.focus();
				return false;
			}
			if(document.login_form.password.value==""){
				alert("비밀번호를 입력해주세요.");
				document.login_form.password.focus();
				return false;
			}
			
			document.login_form.submit();
		}

		var join = function(){
			var w = 450;
	        var h = 450;
			var winl = (screen.width - w) / 2;
	        var wint = (screen.height - h) / 2;
	        
			winobject = window.open("/includes/flex/join/join.html", "icap", "width="+w+", height="+h+", scrollbars=no, toolbar=no, location=no, resizable=no, status=no, menubar=no, titlebar=no,left="+winl+", top="+wint, false);
		};

		var winobject;
		function pwdSearch(){
			var w = 450;//340;
	        var h = 450;//180;
			var winl = (screen.width - w) / 2;
	        var wint = (screen.height - h) / 2;
	        
			winobject = window.open("/includes/flex/join/pwdSearch.html", "icap", "width="+w+", height="+h+", scrollbars=no, toolbar=no, location=no, resizable=no, status=no, menubar=no, titlebar=no,left="+winl+", top="+wint, false);
			winobject.focus();
		}
	//-->
	</script>
	<style type="text/css">
	input {HEIGHT: 18px;FONT-SIZE: 11px; COLOR: #103b8f; border: 1px solid #d4d4d4;FONT-FAMILY: "돋움", "굴림", Arial, "Verdana";}
	body {margin-left: 0px;margin-top: 0px;margin-right: 0px;margin-bottom: 0px; }
	</style>
	<link href="/images/css/css.css" rel="stylesheet" type="text/css">
</head>
<%
	String token = (String) session.getAttribute(org.apache.struts.Globals.TRANSACTION_TOKEN_KEY);
%>
<body onload="init();" background="images/login/bg_00008.jpg" onLoad="MM_preloadImages('images/login/login_btn_over.gif','images/login_btn_over.gif')">
<p class="text_input_000">&nbsp;</p>
	<form action="login.do" method="post" name="login_form" id="login_form" >
	<table width="608" border="0" height="427" align="center" cellpadding="0" cellspacing="0">
	  <tr><td>&nbsp;</td></tr>
	  <tr><td>&nbsp;</td></tr>
	  <tr><td>&nbsp;</td></tr>
	  <tr><td>&nbsp;</td></tr>
	  <tr><td>&nbsp;</td></tr>
	  <tr>
		<td width="606">
		<table width="772" height="400" border="0" cellpadding="0" cellspacing="0" background="images/login/bg00001.gif">
	    <tr>
			  <td width="158" height="271" rowspan="2" align="right" valign="bottom">&nbsp;</td>
			  <td width="288" height="154" align="right" valign="bottom">
		  		<a href="javascript:join();" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('memjoin','','images/login/btn_memjoin_over.gif',1)"><img src="images/login/btn_memjoin_off.gif" alt="회원가입" name="memjoin" width="80" height="21" hspace="0" vspace="7" border="0"></a>
  				<a href="javascript:pwdSearch();" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('pwfind','','images/login/btn_pwfind_over.gif',1)"><img src="images/login/btn_pwfind_off.gif" alt="비밀번호찾기" name="pwfind" width="80" height="21" hspace="0" vspace="7" border="0"></a>
  				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			  </td>
		    <td width="326" rowspan="2" valign="bottom"><img src="images/login/image.gif" width="312" height="232"></td>
		  </tr>
			<tr>
			  <td height="119" valign="top"><table width="263" height="69" border="0" cellpadding="0" cellspacing="1">
				  <tr>
					<td width="175"><table width="175" height="61" border="0" cellpadding="0" cellspacing="2">
						<tr>
						  <td width="170" height="25" background="images/login/text01.gif"><div align="center">
							  <input name="username" type="text" class="text_input_000" size="20" value="ljh855" onKeyPress="enter();" >
							</div></td>
						</tr>
						<!--  
						<tr>
						  <td width="0" height="0"><div align="center">
							  <input name="maxCount" type="hidden" value="100" >
							</div></td>
						</tr>
						-->
						<tr>
						  <td width="170" height="25" background="images/login/text01.gif"><div align="center">
							  <input name="password" type="password" class="text_input_000" size="20" value="mpik2081" onKeyPress="enter2();" >
							</div></td>
						</tr>
					  </table></td>
					<td width="85"><a href="#" onClick="javascript:goLogin();"  onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image4','','images/login/login_btn_over.gif',1)">
					<img src="images/login/login_btn_01.gif" name="Image4" width="68" height="71" border="0"></a>
					</td>
				  </tr>
				</table></td>
			</tr>
			<tr>
			  <td height="130" colspan="3"><div align="center">
			  	<table border="0" cellspacing="0" cellpadding="0">
					  <tr>
					    <td><img src="images/login/text02.gif" width="531" height="116" border="0" usemap="#Map" /><map name="Map" id="Map"><area shape="rect" coords="230,43,307,67" href="#" onClick="window.open('popup.htm','coordi','resizable=no,scrollbars=yes,status=0,width=685,height=500');"/><area shape="rect" coords="362,64,439,85" href="#" onClick="window.open('popup01.htm','coordi','resizable=no,scrollbars=yes,status=0,width=685,height=500');"/></map></td>
					    <td valign="top"><br />
					      <table width="100%" border="0" cellspacing="0" cellpadding="0">
					      <tr>
					        <td><a href="diagnosis_information.pdf" target="_blank"><img src="images/login/btn_info.gif" width="164" height="36" border="0" /></a></td>
					      </tr>
					      <tr>
					        <td><img src="images/login/title_download.gif" width="164" height="23" /></td>
					      </tr>
					      <tr>
					        <td><table width="164" border="0" cellpadding="0" cellspacing="0">
					          <tr>
					            <td><a href="user_manual.hwp"><img src="images/login/btn_01.gif" width="74" height="36" border="0" /></a></td>
					            <td><a href="organization_manual.hwp"><img src="images/login/btn_02.gif" width="90" height="36" border="0" /></a></td>
					          </tr>
					        </table></td>
					      </tr>
					    </table>
					    </td>
					  </tr>
					</table>
				</div></td>
			</tr>
		</table>
		</td></tr>
		<tr valign="top">
			  <td colspan="3" valign="top"><div align="center" valign="top"><img src="images/login/bottom.gif" border="0" width="772" height="26" usemap="#iMap" ><map name="iMap" id="iMap"><area shape="rect" coords="590,0,710,25" href="#" onClick="window.open('privacy.htm','coordi','resizable=no,scrollbars=yes,status=0,width=730,height=500');"/></map></div></td>
			</tr>
		</table>
	</form>
</body>
</html>