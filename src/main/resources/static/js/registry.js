$(document).ready(function(){
	//禁止jquery ajax缓存
	$.ajaxSetup({cache:false})
	
	$('#login').click(function(){
		window.location.href="/login"
	})
	
	$("#username").blur(function(){
		 $.ajax({
             type: "POST",
             url: "/checkUserName",
             data: {
            	 username: $('#username').val()
             },
             success: function (data) {
                 if (data == 1) {
                	 
                 } else {
                	 alert("账号已存在！请重新注册");
                	 $('#username').val("");
                 }
             }
         });
	});
	
	$("#updateCode").click(function(){
		reloadImg()
	})
	
	function reloadImg(){
	    $("#validateImg").attr("src",$("#validateImg").attr("src").split("?")[0]+"?"+new Date().getTime());
	}
	
	function isEmail(str){
	    var reg = /^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$/;
	    return reg.test(str);
	}
	
	//登录表单提交事件
	$('#registry_form').submit(function(e){
		//禁止表单默认的提交
		e.preventDefault()
		//使用ajax提交用户登录数据并进行验证
		
		if($('#username').val().length==0){
			alert("账号不能为空！");
			$('#username').focus();
			return;
		}
		if($('#password').val().length==0){
			alert("密码不能为空！");
			$('#password').focus();
			return;
		}
		if($('#password').val().length<6){
			alert("密码至少为6位");
			$('#password').focus();
			return;
		}
		if($('#checkCode').val().length==0){
			alert("验证码不能为空！");
			$("#checkCode").focus();
			return;
		}
		if($('#password').val()!=$('#passwordConf').val()){
			alert("两次输入的密码不同！");
			$('#passwordConf').focus();
			return;
		}
        
		var email=$('#email').val();
		
		if(!isEmail(email)){
			alert("请输入正确邮箱！");
			$('#email').focus();
			return;
		}
		
		$.ajax({
			url:'/registry',
			method:'POST',
			data:{
				username:$('#username').val(),
				email:$('#email').val(),
				checkCode:$('#checkCode').val(),
				password:$('#password').val()
			},
			type:'json',
			success:function(data){
				if(data==1){
					alert('注册成功！ ！请你使用注册邮箱激活后登录！')
					window.location.href="/login"
				}
				else{
					alert(data)
				}
				
			},
			error:function(e){
				console.log(e)
			}
		})
	})
})