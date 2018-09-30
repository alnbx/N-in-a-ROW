$(function() {
    $("#login").submit(function(event) {
        $.ajax({
            data: $(this).serialize(),
            timeout: 2000,
            url: buildUrlWithContextPath("login"),
            error: function() {
                console.error("Failed to submit");
            },
            success: function(r) {
                jsonStr = JSON.stringify(r);
                $("#response1").text(jsonStr);
            }
        });
        return false;
    });
});

$(function() {
    $("#usersList").click(function(event) {
        $.ajax({
            data: $(this).serialize(),
            timeout: 2000,
            url: buildUrlWithContextPath("usersList"),
            error: function() {
                console.error("Failed to submit");
            },
            success: function(data) {
                jsonStr = JSON.stringify(data);
                $("#response2").text(jsonStr);
            }
        });

        return false;
    });
});

$(function() {
    $("#gamesList").click(function(event) {
        $.ajax({
            data: $(this).serialize(),
            timeout: 2000,
            url: buildUrlWithContextPath("gamesList"),
            error: function() {
                console.error("Failed to submit");
            },
            success: function(data) {
                jsonStr = JSON.stringify(data);
                $("#response4").text(jsonStr);
            }
        });

        return false;
    });
});

$(function() {
    $("#uploadFile").click(function(event) {
        $.ajax({
            data: $(this).serialize(),
            timeout: 2000,
            error: function() {
                console.error("Failed to submit");
            },
            success: function(data) {
                var file = document.getElementById('files').files[0];
                console.log("chosen file: " + file.name);
                var reader = new FileReader();
                reader.readAsText(file);
                reader.onload = function(e) {
                    var xmlContent = reader.result;
                    xmlContent = xmlContent.replace(/"/g, "'");
                    var postRequest = $.post(buildUrlWithContextPath("uploadGame"), { settingsfile: xmlContent});
                    postRequest.success(function(returnedData) {
                        console.log("in settings call back");
                        jsonStr = JSON.stringify(returnedData);
                        $("#response3").text(jsonStr);
                    });
                    postRequest.fail(function(xhr, status, error) {
                        alert("error: " + error);
                    });
                }
                return false;
            }
        });
        return false;
    });
});

$(function() {
    $("#registerToGame").submit(function(event) {
        $.ajax({
            data: $(this).serialize(),
            url: buildUrlWithContextPath("registerToGame"),
            error: function(r) {
                console.error("Failed to submit");
            },
            success: function(r) {
                jsonStr = JSON.stringify(r);
                $("#response5").text(jsonStr);
            }
        });
        return false;
    });
});