$(document).ready(function(){
	//禁止jquery ajax缓存
	$.ajaxSetup({cache:false})
	
	$('#add_cart_form').validate({
		rules: {
            num  :{
            	required: true,
            	digits:true
            }
		},messages:{
            num :{
            	required:"必填",
            	digits:"请输入整数数据"
            }
        }
 	});
   $('#addcart').click(function(){
	
	//禁止jquery ajax缓存
	$.ajaxSetup({cache:false})
	
	 var store_num=parseInt($('#store_num').val());
	 var num=parseInt($('#num').val());
	
	 if(num>store_num){
		 alert("库存不足，请重新选择！")
		 return ;
	 }
	
	 if($('#add_cart_form').valid()){
		 $('#add_cart_form').submit()
	 }else{
		 alert('数据验证失败，请检查！');
	 }
	});
})