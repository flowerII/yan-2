
$.ajaxSetup({
	contentType : "application/x-www-form-urlencoded;charset=utf-8",
    complete: function(XMLHttpRequest, textStatus) {
    },
    statusCode: {
        401: function() {
            var conf=confirm('您还未登录，或者您的登录已失效。是否需要重新登录？');
            if(conf==true){
                window.location.reload();
            }
        },
        403: function() {
            alert('请求的页面不存在或没有权限访问。请联系系统管理员，错误代码：403');
        },
        404: function() {
            alert('数据获取/输入失败，没有此服务。请联系系统管理员，错误代码：404');
        },
        504: function() {
            alert('数据获取/输入失败，服务器没有响应。请联系系统管理员，错误代码：504');
        },
        500: function() {
            alert('服务器有误。请联系系统管理员，错误代码：500');
        },
	    400:function(){
		    alert(' 由于语法格式有误，服务器无法理解此请求。请联系系统管理员，错误代码：400');
	    }
    }
});

$(function () {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });
});
