$(document).ready(function () {

    $.ajax({
        url: "http://localhost:8080/api/user"
    }).then(function (value) {
        console.log("vrijednost: " + JSON.stringify(value));
        $(".user-name").prepend("<a>" + value.firstName + " " + value.lastName + "</a>");
        if (value.userImage == null) {
            $(".user-icon").prepend("<img class='user-icon-image' src='../images/default-avatar.png'/>");
        } else {
            $(".user-icon").prepend("<img class='user-icon-image' src='data:image/png;base64," + value.userImage + "'/>");
        }
    })


    $("selected").append("<div class='arrLeft'></div>");

    $(".sidebar-box-body nav a").click(function () {
        $(".dashboard-body").empty();
        var optionName = $(this).text();
        $(".header-text").text(optionName);
        if (optionName == "SCHEDULE") {
            myScheduler();
        }
        $(".sidebar-box-body nav a").removeClass();
        $('.arrLeft').remove();
        $(this).addClass('selected');
        $(this).append("<div class='arrLeft'></div>");

    });

    $(".menu-icon").click(function () {
        var className = $("body").attr('class');
        if (className === "expanded") {
            $("body").removeClass();
            $("body").addClass("contracted")
        } else {
            $("body").removeClass();
            $("body").addClass("expanded")
        }
    });


    function myScheduler() {
        $(function(){
            $(".dashboard-body").prepend("<div class='scheduler-box'></div>")
            $(".scheduler-box").dhx_scheduler({
                xml_date:"%m/%d/%Y %H:%i",
                date:new Date(),
                mode:"month",
                drag_create: false,
                dblclick_create: false,
                readonly: true,
            });

            var events = [
                {id:1, text:"Meeting",   start_date:"04/11/2018 14:00",end_date:"04/11/2018 17:00", color:"#FAA71B"},
                {id:2, text:"Conference",start_date:"04/15/2018 12:00",end_date:"04/18/2018 19:00", color:"#FAA71B"},
                {id:3, text:"Interview", start_date:"04/24/2018 09:00",end_date:"04/24/2018 10:00", color:"#FAA71B"}
            ];
            scheduler.parse(events, 'json');

        });

    }
});
    
