$(document).ready(function () {
    var loggedUser;
    var firstName;
    var lastName;
    var address;
    var email ;
    var description ;
    var image;

    $.ajax({
        url: "http://localhost:8080/api/user"
    }).then(function (value) {
        loggedUser = value;
        console.log("vrijednost: " + JSON.stringify(value));
        $(".user-name").prepend("<a>" + value.firstName + " " + value.lastName + "</a>");
        if (value.userImage == null) {
            $(".user-icon").prepend("<img class='user-icon-image' src='../images/default-avatar.png'/>");
        } else {
            $(".user-icon").prepend("<img class='user-icon-image' src='data:image/png;base64," + value.userImage + "'/>");
        }
    })
    dashboardPageLoad();


    $("selected").append("<div class='arrLeft'></div>");

    $(".sidebar-box-body nav a").click(function () {
        $(".dashboard-body").empty();
        var optionName = $(this).text();
        $(".header-text").text(optionName);
        if (optionName.indexOf("SCHEDULE") >=0) {
            myScheduler();
        }else if(optionName.indexOf("PROFILE") >=0){
            profileLoad();
        }else if(optionName.indexOf("DASHBOARD") >=0){
            dashboardPageLoad();
        }else if(optionName.indexOf("MAP") >=0){
            googleMapsPageLoad();
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
            if ($(".header-text").text() === "SCHEDULE") {
                scheduler.setCurrentView();
            }
        } else {
            $("body").removeClass();
            $("body").addClass("expanded")
            if ($(".header-text").text() === "SCHEDULE") {
                scheduler.setCurrentView();
            }
        }
    });


    function myScheduler() {
        $(function () {
            $(".dashboard-body").prepend("<div class='scheduler-box'></div>")
            $(".scheduler-box").dhx_scheduler({
                xml_date: "%m/%d/%Y %H:%i",
                date: new Date(),
                mode: "month",
                drag_create: false,
                dblclick_create: false,
                readonly: true,

            });

            $.ajax({
                url: "http://localhost:8080/api/events"
            }).then(function (value) {
                var events = value;
                scheduler.parse(events, 'json');
            })




        });

    }


    function alert(message, error){
        if(error){
            $("body").append("<div class = 'alert-box alert-error'></div>");
        }else{
            $("body").append("<div class = 'alert-box alert-success'></div>");
        }
        $(".alert-box").append("<div class='alert-message'><a>"+message+"</a></div>");
        $(".alert-box").delay(1000).fadeOut(1000);
    }

    function dateTimeBox(){
        $(".timer-box").remove();
        var monthNames = ["January", "February", "March", "April", "May","June", "July", "August", "September", "October", "November", "December" ];
        var dayNames = ["Sunday","Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
        var dt = new Date();
        var seconds;
        var minutes;

        if(dt.getSeconds()<10){
            seconds = "0" + dt.getSeconds()
        }else{
            seconds= dt.getSeconds()
        }

        if(dt.getMinutes()<10){
            minutes =  "0" + dt.getMinutes()
        }else{
            minutes =  dt.getMinutes()
        }
        $(".dashboard-page-box").append("<div class='timer-box'>"+
            " <div class ='time-box'>"+
            dt.getHours()+ ":" +minutes + ":" + seconds +
            "</div>"+
            " <div class = 'date-box'>"+
            dayNames[dt.getDay()]+", " + monthNames[dt.getMonth()]+ " " + dt.getDate() + ", " + dt.getFullYear()+
            "</div>"+
            "</div>");
        setTimeout(dateTimeBox, 1000)
    }


    function profileLoad(){

        $.ajax({
            url: "http://localhost:8080/api/user"
        }).then(function (value) {
            loggedUser = value;

            $.get("profile", function(data){
                $(".dashboard-body").html(data)

                $('#username').val(loggedUser.username).prop('disabled',true);
                $('#firstName').val(loggedUser.firstName).prop('disabled',true);
                $('#lastName').val(loggedUser.lastName).prop('disabled',true);
                $('#address').val(loggedUser.address).prop('disabled',true);
                $('#email').val(loggedUser.email).prop('disabled',true);
                $('#description').val(loggedUser.description).prop('disabled',true);
                $(".profile-image-box-bottom-name").html(loggedUser.firstName + " " + loggedUser.lastName);
                $('.profile-image-box-bottom-description').html(loggedUser.description);
                $(".user-name").empty();
                $(".user-name").prepend("<a>" + loggedUser.firstName + " " + loggedUser.lastName + "</a>");
                if (loggedUser.userImage != null) {
                    console.log(loggedUser.userImage);
                    $(".profile-image").attr('src','data:image/png;base64, '+ loggedUser.userImage);
                    $(".user-icon-image").attr('src','data:image/png;base64, '+ loggedUser.userImage);
                } else {
                    $(".profile-image").attr('src','../images/default-avatar.png');
                    $(".user-icon-image").attr('src','../images/default-avatar.png');
                }
                loadEdit();

                function loadEdit(){
                    $('.profile-edit-button').on('click',function(){
                        firstName =  $('#firstName').val();
                        lastName = $('#lastName').val();
                        address = $('#address').val();
                        email =  $('#email').val();
                        description = $('#description').val();

                        $('.profile-edit-button').remove();
                        $('#firstName').prop('disabled',false);
                        $('#lastName').prop('disabled',false);
                        $('#address').prop('disabled',false);
                        $('#email').prop('disabled',false);
                        $('#description').prop('disabled',false);
                        $('.profile-form').append("<div class='image-upload'>"+
                            "<div class= 'image-upload-label'>Profile Image</div>"+
                            "<label for='file-input'><i class='attach-doc fa fa-camera '></i></label>"+
                            "<input id='file-input' type='file' name='file-input' accept='image/x-png,image/jpeg'></div>"+
                            "<button id='profile-submit-button' class = 'profile-form-submit' type='submit' name='image'><a><i class='fa fa-check'></i>Save</a></button>"+

                            "<button id='profile-cancel-button' type='button' class = 'profile-cancel-button'> <a><i class='fa fa-times'></i>Cancel</a></button> "+
                            "<button class = 'profile-form-submit' type='submit' name='saveDetails'><a><i class='fa fa-check'></i>Save</a</button>");
                        loadCancel();
                        loadSubmit();

                    });

                }

                function loadSubmit(){
                    $('.profile-form-submit').on('click',function(e){
                        e.preventDefault();
                        var data;
                        firstName =  $('#firstName').val();
                        lastName = $('#lastName').val();
                        address = $('#address').val();
                        email =  $('#email').val();
                        description = $('#description').val();
                        if($('#file-input') != null){
                            file = $('#file-input')[0].files[0];
                        }
                        if(firstName == '' || lastName == '' || address == '' || email == '' ){
                            alert("Error while saving data!", true);
                        }else{
                            if(file != null){
                                var FR= new FileReader();
                                FR.addEventListener("load", function() {
                                    image = FR.result.split(',')[1];
                                    data = '{"id" : '+loggedUser.id+', "username" : "'+loggedUser.username+'","firstname": "'+
                                        firstName+'","lastname": "'+lastName+'" ,"address": "'+
                                        address+'" ,"email": "'+email+'","description": "'+description+'","image": "'+
                                        image+'","active": "'+loggedUser.active+'"}';
                                    $.ajax({
                                        url : "http://localhost:8080/api/profile",
                                        type : "POST",
                                        data : data,
                                        contentType: 'application/json',
                                        success : function(result) {
                                            alert("Profile updated", false);
                                            $(".dashboard-body").empty();
                                            profileLoad();
                                        },
                                        error : function(xhr, tStatus, err) {
                                            alert("Error while saving data!", true);
                                        }
                                    });
                                });

                                FR.readAsDataURL( file );
                            }else{
                                image = null;
                                data = '{"id" : '+loggedUser.id+', "username" : "'+loggedUser.username+'","firstname": "'+
                                    firstName+'","lastname": "'+lastName+'" ,"address": "'+
                                    address+'" ,"email": "'+email+'","description": "'+description+'","image": "'+
                                    image+'","active": "'+loggedUser.active+'"}';
                                $.ajax({
                                    url : "http://localhost:8080/api/profile",
                                    type : "POST",
                                    data : data,
                                    contentType: 'application/json',
                                    success : function(result) {
                                        alert("Profile updated", false);
                                        $(".dashboard-body").empty();
                                        profileLoad();
                                    },
                                    error : function(xhr, tStatus, err) {
                                        alert("Error while saving data!", true);
                                    }
                                });
                            }





                        }

                    });
                }

                function loadCancel(){
                    $('.profile-cancel-button').on('click',function(){
                        $('.profile-cancel-button').remove();
                        $('.profile-form-submit').remove();
                        $('.image-upload').remove();
                        $('#firstName').val(firstName).prop('disabled',true);
                        $('#lastName').val(lastName).prop('disabled',true);
                        $('#address').val(address).prop('disabled',true);
                        $('#email').val(email).prop('disabled',true);
                        $('#description').val(description).prop('disabled',true);
                        $(".profile-image-box-bottom-name").html(firstName + " " + lastName);
                        $('.profile-image-box-bottom-description').html(description);
                        $('.profile-form').append("<button id='profile-edit-button' type='button' class = 'profile-edit-button'><a><i class='fa fa-pencil'></i>Edit</a></button>");
                        loadEdit();
                    });

                }

            });
        });



    }



    function dashboardPageLoad(){

        $.get("dashboard-page", function(data) {
            $(".dashboard-body").html(data)
            dateTimeBox();
        });
    }



    function googleMapsPageLoad(){
        $.get("map", function(data) {
            $(".dashboard-body").html(data);
        });

        $.ajax({
            url: "http://localhost:8080/api/user_location"
        }).then(function (value) {
            $.each(JSON.parse(value), function(i,item){
                var pos = {lat: item.lat, lng: item.lng};
                var marker = new google.maps.Marker({
                    position: pos,
                    map: map,
                    title: 'Home',
                    icon: customIcon({
                        fillColor: '#0000FF'
                    }),
                });
                map.setCenter(pos);
                loadAllMarkers();
            });
        });
    }

    function loadAllMarkers(){
        $.ajax({
            url: "http://localhost:8080/api/locations"
        }).then(function (value) {
            $.each(value, function(i,item){
                var pos = {lat: item.lat, lng: item.lng};
                var marker = new google.maps.Marker({
                    position: pos,
                    map: map,
                    title: item.tripName
                });
            });
        });
    }

    function customIcon (opts) {
        return Object.assign({
            path: 'M 0,0 C -2,-20 -10,-22 -10,-30 A 10,10 0 1,1 10,-30 C 10,-22 2,-20 0,0 z M -2,-30 a 2,2 0 1,1 4,0 2,2 0 1,1 -4,0',
            fillColor: '#34495e',
            fillOpacity: 1,
            strokeColor: '#000',
            strokeWeight: 2,
            scale: 1,
        }, opts);
    }
});
    
