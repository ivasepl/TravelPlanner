var notLoaded = false;
var dashboardIsActive = false;
$(document).ready(function () {
    var loggedUser;
    var firstName;
    var lastName;
    var address;
    var email;
    var description;
    var image;

    $.ajax({
        url: "http://localhost:8080/api/user"
    }).then(function (value) {
        loggedUser = value;
        $(".user-name").prepend("<a>" + value.firstName + " " + value.lastName + "</a>");
        if (value.userImage == null) {
            $(".user-icon").prepend("<img class='user-icon-image' src='../images/default-avatar.png'/>");
        } else {
            $(".user-icon").prepend("<img class='user-icon-image' src='data:image/png;base64," + value.userImage + "'/>");
        }
    })
    dashboardPageLoad();
    dashboardIsActive = true;


    $("selected").append("<div class='arrLeft'></div>");

    $(".sidebar-box-body nav a").click(function () {
        dashboardIsActive = false;
        $(".timer-box").remove();
        $(".dashboard-body").empty();
        var optionName = $(this).text();
        $(".header-text").text(optionName);
        if (optionName.indexOf("SCHEDULE") >= 0) {
            myScheduler();
        } else if (optionName.indexOf("PROFILE") >= 0) {
            profileLoad();
        } else if (optionName.indexOf("DASHBOARD") >= 0) {
            dashboardIsActive = true;
            dashboardPageLoad();
        } else if (optionName.indexOf("MAP") >= 0) {
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


    function dateTimeBox() {
        $(".timer-box").remove();
        if (dashboardIsActive) {
            var monthNames = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
            var dayNames = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
            var dt = new Date();
            var seconds;
            var minutes;

            if (dt.getSeconds() < 10) {
                seconds = "0" + dt.getSeconds()
            } else {
                seconds = dt.getSeconds()
            }

            if (dt.getMinutes() < 10) {
                minutes = "0" + dt.getMinutes()
            } else {
                minutes = dt.getMinutes()
            }
            $(".dashboard-body").append("<div class='timer-box'>" +
                " <div class ='time-box'>" +
                dt.getHours() + ":" + minutes + ":" + seconds +
                "</div>" +
                " <div class = 'date-box'>" +
                dayNames[dt.getDay()] + ", " + monthNames[dt.getMonth()] + " " + dt.getDate() + ", " + dt.getFullYear() +
                "</div>" +
                "</div>");

            setTimeout(dateTimeBox, 1000)
        }
    }


    function profileLoad() {

        $.ajax({
            url: "http://localhost:8080/api/user"
        }).then(function (value) {
            loggedUser = value;

            $.get("profile", function (data) {
                $(".dashboard-body").html(data)

                $('#username').val(loggedUser.username).prop('disabled', true);
                $('#firstName').val(loggedUser.firstName).prop('disabled', true);
                $('#lastName').val(loggedUser.lastName).prop('disabled', true);
                $('#address').val(loggedUser.address).prop('disabled', true);
                $('#email').val(loggedUser.email).prop('disabled', true);
                $('#description').val(loggedUser.description).prop('disabled', true);
                $(".profile-image-box-bottom-name").html(loggedUser.firstName + " " + loggedUser.lastName);
                $('.profile-image-box-bottom-description').html(loggedUser.description);
                $(".user-name").empty();
                $(".user-name").prepend("<a>" + loggedUser.firstName + " " + loggedUser.lastName + "</a>");
                if (loggedUser.userImage != null) {
                    $(".profile-image").attr('src', 'data:image/png;base64, ' + loggedUser.userImage);
                    $(".user-icon-image").attr('src', 'data:image/png;base64, ' + loggedUser.userImage);
                } else {
                    $(".profile-image").attr('src', '../images/default-avatar.png');
                    $(".user-icon-image").attr('src', '../images/default-avatar.png');
                }
                loadEdit();

                function loadEdit() {
                    $('.profile-edit-button').on('click', function () {
                        firstName = $('#firstName').val();
                        lastName = $('#lastName').val();
                        address = $('#address').val();
                        email = $('#email').val();
                        description = $('#description').val();

                        $('.profile-edit-button').remove();
                        $('#firstName').prop('disabled', false);
                        $('#lastName').prop('disabled', false);
                        $('#address').prop('disabled', false);
                        $('#email').prop('disabled', false);
                        $('#description').prop('disabled', false);
                        $('.profile-form').append("<div class='image-upload'>" +
                            "<div class= 'image-upload-label'>Profile Image</div>" +
                            "<label for='file-input'><i class='attach-doc fa fa-camera '></i></label>" +
                            "<input id='file-input' type='file' name='file-input' accept='image/x-png,image/jpeg'></div>" +
                            "<button id='profile-submit-button' class = 'profile-form-submit' type='submit' name='image'><a><i class='fa fa-check'></i>Save</a></button>" +

                            "<button id='profile-cancel-button' type='button' class = 'profile-cancel-button'> <a><i class='fa fa-times'></i>Cancel</a></button> " +
                            "<button class = 'profile-form-submit' type='submit' name='saveDetails'><a><i class='fa fa-check'></i>Save</a</button>");
                        loadCancel();
                        loadSubmit();

                    });

                }

                function loadSubmit() {
                    $('.profile-form-submit').on('click', function (e) {
                        e.preventDefault();
                        var data;
                        firstName = $('#firstName').val();
                        lastName = $('#lastName').val();
                        address = $('#address').val();
                        email = $('#email').val();
                        description = $('#description').val();
                        if ($('#file-input') != null) {
                            file = $('#file-input')[0].files[0];
                        }
                        if (firstName == '' || lastName == '' || address == '' || email == '') {
                            alert("Error while saving data!", true);
                        } else {
                            if (file != null) {
                                var FR = new FileReader();
                                FR.addEventListener("load", function () {
                                    image = FR.result.split(',')[1];
                                    data = '{"id" : ' + loggedUser.id + ', "username" : "' + loggedUser.username + '","firstname": "' +
                                        firstName + '","lastname": "' + lastName + '" ,"address": "' +
                                        address + '" ,"email": "' + email + '","description": "' + description + '","image": "' +
                                        image + '","active": "' + loggedUser.active + '"}';
                                    $.ajax({
                                        url: "http://localhost:8080/api/profile",
                                        type: "POST",
                                        data: data,
                                        contentType: 'application/json',
                                        success: function (result) {
                                            alert("Profile updated", false);
                                            $(".dashboard-body").empty();
                                            profileLoad();
                                        },
                                        error: function (xhr, tStatus, err) {
                                            alert("Error while saving data!", true);
                                        }
                                    });
                                });

                                FR.readAsDataURL(file);
                            } else {
                                image = null;
                                data = '{"id" : ' + loggedUser.id + ', "username" : "' + loggedUser.username + '","firstname": "' +
                                    firstName + '","lastname": "' + lastName + '" ,"address": "' +
                                    address + '" ,"email": "' + email + '","description": "' + description + '","image": "' +
                                    image + '","active": "' + loggedUser.active + '"}';
                                $.ajax({
                                    url: "http://localhost:8080/api/profile",
                                    type: "POST",
                                    data: data,
                                    contentType: 'application/json',
                                    success: function (result) {
                                        alert("Profile updated", false);
                                        $(".dashboard-body").empty();
                                        profileLoad();
                                    },
                                    error: function (xhr, tStatus, err) {
                                        alert("Error while saving data!", true);
                                    }
                                });
                            }


                        }

                    });
                }

                function loadCancel() {
                    $('.profile-cancel-button').on('click', function () {
                        $('.profile-cancel-button').remove();
                        $('.profile-form-submit').remove();
                        $('.image-upload').remove();
                        $('#firstName').val(firstName).prop('disabled', true);
                        $('#lastName').val(lastName).prop('disabled', true);
                        $('#address').val(address).prop('disabled', true);
                        $('#email').val(email).prop('disabled', true);
                        $('#description').val(description).prop('disabled', true);
                        $(".profile-image-box-bottom-name").html(firstName + " " + lastName);
                        $('.profile-image-box-bottom-description').html(description);
                        $('.profile-form').append("<button id='profile-edit-button' type='button' class = 'profile-edit-button'><a><i class='fa fa-edit'></i>Edit</a></button>");
                        loadEdit();
                    });

                }

            });
        });


    }


    function dashboardPageLoad() {

        $.get("dashboard-page", function (data) {
            $(".dashboard-body").html(data);
            dateTimeBox();
            loadTable();
            loadStatistic();

            $("#add-trip").click(function () {
                $.get("dialog", function (data) {
                    $(data).appendTo("body");
                    addEvent();
                    $("#addButton").click(function (event) {
                        var address = $("#address").val();
                        var name = $("#name").val();
                        var type = $("#type").val();
                        var startDate = $("#startDate").val();
                        var endDate = $("#endDate").val();
                        var details = $("#details").val();
                        var data = '{"address":"' + address + '","name":"' + name + '","type":"' + type + '","dateFrom":"' + startDate + '","dateTo":"' + endDate + '","details":"' + details + '"}';
                        if (address == "" || name == "" || type == "" || startDate == "" || endDate == "" || details == "") {
                            alert("Fields cannot be empty!", true);
                        } else {
                            $.ajax({
                                url: "http://localhost:8080/api/add_trip",
                                type: "POST",
                                data: data,
                                contentType: 'application/json',
                                success: function () {
                                    $(".trip-dialog-box-container").remove();
                                    alert("Trip sucessfully added", false);
                                    loadTable();
                                },
                                error: function (xhr, tStatus, err) {
                                    alert("Error!", true);
                                }
                            });


                        }

                    });


                });

            });
        });
    }


    function googleMapsPageLoad() {
        $.get("map", function (data) {
            $(".dashboard-body").html(data);
            if (notLoaded == false) {
                $.getScript('https://maps.googleapis.com/maps/api/js?key=AIzaSyDRDqTFg9XuqEG7jRhrZpIMFzYh6ZzJIWA&callback=myMap');
                notLoaded = true;
            } else {
                myMap();
            }

        });

        $.ajax({
            url: "http://localhost:8080/api/user_location"
        }).then(function (value) {
            $.each(JSON.parse(value), function (i, item) {
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

    function loadAllMarkers() {
        $.ajax({
            url: "http://localhost:8080/api/locations"
        }).then(function (value) {
            $.each(value, function (i, item) {
                var pos = {lat: item.lat, lng: item.lng};
                var marker = new google.maps.Marker({
                    position: pos,
                    map: map,
                    title: item.tripName
                });
            });
        });
    }

    function customIcon(opts) {
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


function addEvent() {
    $("#cancel").click(function () {
        $(".trip-dialog-box-container").remove();
    });
}


function update(id) {
    $.get("dialog-update", function (data) {
        $(data).appendTo("body");
        addEvent();
        var json = '{"id":"' + id + '"}';
        $.ajax({
            url: "http://localhost:8080/api/get_trip",
            type: "POST",
            data: json,
            contentType: 'application/json',
            success: function (data) {
                var address = data.address;
                $.each(address, function () {
                    $("#address").val(this.address);
                    $("#name").val(data.name);
                    $("#type").val(data.type);
                    $("#startDate").val(moment(data.dateFrom).format("YYYY-MM-DD HH:mm:ss"));
                    $("#endDate").val(moment(data.dateTo).format("YYYY-MM-DD HH:mm:ss"));
                    $("#details").val(data.details)

                })

            },
            error: function (xhr, tStatus, err) {
            }
        });


        $("#updateButton").click(function (event) {
            var address = $("#address").val();
            var name = $("#name").val();
            var type = $("#type").val();
            var startDate = $("#startDate").val();
            var endDate = $("#endDate").val();
            var details = $("#details").val();
            var data = '{"id":"' + id + '","address":"' + address + '","name":"' + name + '","type":"' + type + '","dateFrom":"' + startDate + '","dateTo":"' + endDate + '","details":"' + details + '"}';
            if (address == "" || name == "" || type == "" || startDate == "" || endDate == "" || details == "") {
                alert("Fields cannot be empty!", true);
            } else {
                $.ajax({
                    url: "http://localhost:8080/api/update_trip",
                    type: "POST",
                    data: data,
                    contentType: 'application/json',
                    success: function () {
                        $(".trip-dialog-box-container").remove();
                        alert("Trip sucessfully updated!", false);
                        loadTable();
                    },
                    error: function (xhr, tStatus, err) {
                        alert("Error!", true);
                    }
                });


            }

        });
    });
}

function deleteTrip(id) {
    $.get("dialog-delete", function (data) {
        $(data).appendTo("body");
        addEvent();

        $("#deleteButton").click(function (event) {
            var json = '{"id":"' + id + '"}';
            $.ajax({
                url: "http://localhost:8080/api/delete_trip",
                type: "POST",
                data: json,
                contentType: 'application/json',
                success: function () {
                    $(".trip-dialog-box-container").remove();
                    alert("Trip sucessfully deleted!", false);
                    loadTable();
                },
                error: function (xhr, tStatus, err) {
                    alert("Error!", true);
                }
            });

        });
    });
}


function alert(message, error) {
    if (error) {
        $("body").append("<div class = 'alert-box alert-error'></div>");
    } else {
        $("body").append("<div class = 'alert-box alert-success'></div>");
    }
    $(".alert-box").append("<div class='alert-message'><a>" + message + "</a></div>");
    $(".alert-box").delay(1000).fadeOut(1000);
}

function loadTable() {
    $(".table-container").html("");
    var data = "<table><thead><tr><th> Name </th><th> Address </th><th> Type</th><th> Start date</th><th> End date</th><th>Details</th><th> Action</th></tr></thead>";
    data += "<tbody>";
    $.ajax({
        url: "http://localhost:8080/api/user_trips"
    }).then(function (value) {
        console.log(value);
        $.each(value, function (i, item) {
            var address = item.address;
            $.each(address, function () {
                data += "<tr><td>" + item.name + "</td><td>" + this.address + "</td><td>" + item.type + "</td><td>" + moment(item.dateFrom).format("YYYY-MM-DD HH:mm:ss") + "</td><td>" + moment(item.dateTo).format("YYYY-MM-DD HH:mm:ss") + "</td><td>" + item.details + "</td><td> <a id=\"" + item.tripId + "\" onclick=\"update(" + item.tripId + ")\"><i class=\"fa fa-pencil\"></i></a>\n" +
                    "<a id=\"" + item.tripId + "\" onclick=\"deleteTrip(" + item.tripId + ")\"><i class=\"fa fa-trash\"></i></a></td></tr>";
            });

        });

        data += "</tbody></table>";
        $(data).appendTo(".table-container");
    });


}

function loadStatistic(){
    var ctx = $("#chart");
    $.ajax({
        url: "http://localhost:8080/api/statistic"
    }).then(function (value) {
        console.log(value);
        var keys = Object.keys(value);
        var myChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: [keys[0], keys[1], keys[2], keys[3]],
                datasets: [{
                    label: '# of Trips',
                    data: [value[keys[0]], value[keys[1]], value[keys[2]], value[keys[3]]],
                    backgroundColor: [
                        'rgba(255, 99, 132, 0.2)',
                        'rgba(54, 162, 235, 0.2)',
                        'rgba(255, 206, 86, 0.2)',
                        'rgba(75, 192, 192, 0.2)'
                    ],
                    borderColor: [
                        'rgba(255,99,132,1)',
                        'rgba(54, 162, 235, 1)',
                        'rgba(255, 206, 86, 1)',
                        'rgba(75, 192, 192, 1)'
                    ],
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    yAxes: [{
                        ticks: {
                            beginAtZero:true
                        }
                    }]
                }
            }


        });
    });
}