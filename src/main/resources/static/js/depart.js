$(document).ready(function(){
	
   $('#depart').click(function(){
	
	    //禁止jquery ajax缓存
		$.ajaxSetup({cache:false})
		
		$.ajax({
	        type: "GET",
	        url: "/depart",
	        success: function (data) {
	            $('#content').html(data)
	        }
	    });
	});
})