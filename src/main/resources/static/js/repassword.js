$(document).ready(function(){
	//禁止jquery ajax缓存
	$.ajaxSetup({cache:false})
	
	function isEmail(str){
	    var reg = /^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$/;
	    return reg.test(str);
	}
	
	$('#btn_email_code').click(function(){
		var email=$('#email').val();
		
		if(!isEmail(email)){
			alert("请输入正确邮箱！");
			$('#email').focus();
			return;
		}
		
		$.ajax({
			url:'/email_code',
			method:'POST',
			data:{
				email:email
			},
			type:'json',
			success:function(data){
				console.log(data)
				alert('验证码已发送至您的邮箱，请注意查收！')
			},
			error:function(e){
				console.log(e)
			}
		})
		return ;
	})
	//登录表单提交事件
	$('#registry_form').submit(function(e){
		//禁止表单默认的提交
		e.preventDefault()
		//使用ajax提交用户登录数据并进行验证

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
		if($('#code').val().length==0){
			alert("验证码不能为空！");
			$("#code").focus();
			return;
		}
		if($('#password').val()!=$('#passwordConf').val()){
			alert("两次输入的密码不同！");
			$('#passwordConf').focus();
		}
		
		$.ajax({
			url:'/repassword',
			method:'POST',
			data:{
				email:$('#email').val(),
				code:$('#code').val(),
				password:$('#password').val()
			},
			type:'json',
			success:function(data){
				if(data==1){
					alert('密码重置成功！ ！')
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