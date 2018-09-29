var refreshRate = 2000; //mili seconds

function ajaContent() {
    $.ajax({
        dataType: 'json',
        url: buildUrlWithContextPath("login"),
        success: function(data) {
            jsonStr = JSON.stringify(data);
            $("#response1").text(jsonStr);
            triggerAjaxResponseContent();
        },
    });
}

$(function() {
    $("#request").submit(function(event) {
        $.ajax({
            data: $(this).serialize(),
            timeout: 2000,
            url: buildUrlWithContextPath("login"),
            error: function() {
                console.error("Failed to submit");
            },
            success: function(r) {
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

function triggerAjaxResponseContent() {
    setTimeout(ajaContent, refreshRate);
}

//activate the timer calls after the page is loaded
$(function() {
    triggerAjaxResponseContent();
});