				var currentUser = new User({});				
				var accounts = $("#account-panel").kendoAccounts({
					dropdown : false,
					authenticate : function( e ){
						currentUser = e.token;			
					},
					<#if CompanyUtils.isallowedSignIn(action.company) ||  !action.user.anonymous  || action.view! == "personalized" >
					template : kendo.template($("#account-template").html()),
					</#if>
					afterAuthenticate : function(){							
						//$('.dropdown-toggle').dropdown();
						if( currentUser.anonymous ){
							$("#account-panel .custom-external-login-groups button").each(function( index ) {
									var external_login_button = $(this);
									external_login_button.click(function (e){																												
										var target_media = external_login_button.attr("data-target");
										$.ajax({
											type : 'POST',
											url : "${request.contextPath}/community/get-socialnetwork.do?output=json",
											data: { media: target_media },
											success : function(response){
												if( response.error ){
													// 연결실패.
												} else {	
													window.open( response.authorizationUrl ,'popUpWindow','height=500,width=600,left=10,top=10,resizable=yes,scrollbars=yes,toolbar=yes,menubar=no,location=no,directories=no,status=yes')
												}
											},
											error:handleKendoAjaxError												
										});	
									});								
							});						
							var validator = $("#login-panel").kendoValidator({validateOnBlur:false}).data("kendoValidator");
							$("#login-btn").click(function() { 
								$("#login-status").html("");
								if( validator.validate() )
								{								
									accounts.login({
										data: $("form[name=login-form]").serialize(),
										success : function( response ) {
											var refererUrl = "/main.do";
											if( response.item.referer ){
												refererUrl = response.item.referer;
											}
											$("form[name='login-form']")[0].reset();    
											$("form[name='login-form']").attr("action", refererUrl ).submit();						
										},
										fail : function( response ) {  
											$("#login-password").val("").focus();												
											$("#login-status").kendoAlert({ 
												data : { message: "입력한 사용자 이름 또는 비밀번호가 잘못되었습니다." },
												close : function(){	
													$("#login-password").focus();										
												 }
											}); 										
										},		
										error : function( thrownError ) {
											$("form[name='login-form']")[0].reset();                    
											$("#login-status").kendoAlert({ data : { message: "잘못된 접근입니다." } }); 									
										}																
									});															
								}else{	}
							});	
						}
					}
				});	