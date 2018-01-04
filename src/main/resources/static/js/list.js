$(function () {
    $('#searchBtn').click(function(){
        pageaction();
    });
    $('#addUserInf').click(function(){
        create();
    });
});

var artdialog;

function create(){
    $.get("/new",function(data){
       art.dialog({
        	lock:true,
            opacity:0.3,
            title: "新增",
            width:'750px',
            height: 'auto',
            left: '50%',
            top: '50%',
            content:data,
            esc: true,
            init: function(){
                artdialog = this;
            },
            close: function(){
                artdialog = null;
            }
        });
    });
}

function closeDialog() {
    artdialog.close();
}