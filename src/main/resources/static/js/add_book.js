$(function(){
	$('#add_book_form').validate({
		rules: {
            name :{required:true},
            author :"required",
            num  :{
            	required: true,
            	digits:true
            },
            price :{
            	required: true,
            	number:true
            },
            c_id :"required"
            
		},messages:{
            name :{required:"必填"},
            author :{required:"必填"},
            num :{
            	required:"必填",
            	digits:"请输入整数数据"
            },
            price :{
            	required:"必填",
            	number:"请输入有效数字"
            },
            c_id :{required:"必填"}
        }
 	});
$('#add_book_submit').click(function(){
	
	//禁止jquery ajax缓存
	$.ajaxSetup({cache:false})
	
	
	 if($('#add_book_form').valid()){
         $.ajax({
             type: "POST",
             url: "/book/add",
             data: new FormData($('#add_book_form')[0]),
             processData: false,
             contentType: false,
             //headers: {"Content-type": "application/x-www-form-urlencoded;charset=UTF-8"},
             success: function (data) {
                 if (data == 1) {
                     alert("保存成功");
                     window.location.href="/";
                 } else {
                     alert(data);
                 }
             }
         });
		 }else{
			 alert('数据验证失败，请检查！');
		 }
	});
});

