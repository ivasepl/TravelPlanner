$(document).ready(function (){
    $(".user-icon").prepend("<img class='user-icon-image' src='../images/user.jpg'/>");
    $(".user-name").prepend("<a>Ivan Šepl</a>");
    $("selected").append("<div class='arrLeft'></div>");
    
    $(".sidebar-box-body nav a").click(function (){
        var optionName = $(this).text();
        $(".header-text").text(optionName);
        $(".sidebar-box-body nav a").removeClass();
        $('.arrLeft').remove();
        $(this).addClass('selected');
        $(this).append("<div class='arrLeft'></div>");
    
    });
    
    $(".menu-icon").click(function (){
       var className = $("body").attr('class');
        if(className === "expanded"){
            $("body").removeClass();
            $("body").addClass("contracted")
        }else{
            $("body").removeClass();
            $("body").addClass("expanded") 
        }
    });

 
});
    
