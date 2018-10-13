var myChatVersion = 0;
var refreshRate = 2000; //mili seconds
var myGameId = 0;
var myGameName = "";
var CHAT_USERS_URL = buildUrlWithContextPath("game/getChatUsers");
var GET_CHAT_URL = buildUrlWithContextPath("game/getChat");
var SEND_CHAT_URL = buildUrlWithContextPath("game/sendChat");
var EXIT_CAHT_URL = buildUrlWithContextPath("game/exitChat");

//users = a list of usernames, essentially an array of javascript strings:
// ["moshe","nachum","nachche"...]
function refreshUsersList(chatUsers) {
    //clear all current users
    $("#userslist").empty();

    // rebuild the list of users: scan all users and add them to the list of users
    $.each(chatUsers, function(index, user) {
        console.log("Adding user #" + index + ": " + user.name );
        //create a new <option> tag with a value in it and
        //appeand it to the #userslist (div with id=userslist) element
        $('<li>' + user.name + '</li>').appendTo($("#userslist"));
    });
}

//entries = the added chat strings represented as a single string
function appendToChatArea(entries) {
//    $("#chatarea").children(".success").removeClass("success");

    // add the relevant entries
    $.each(entries || [], appendChatEntry);

    // handle the scroller to auto scroll to the end of the chat area
    var scroller = $("#chatarea");
    var height = scroller[0].scrollHeight - $(scroller).height();
    $(scroller).stop().animate({ scrollTop: height }, "slow");
}

function appendChatEntry(index, entry){
    var entryElement = createChatEntry(entry);
    $("#chatarea").append(entryElement).append("<br>");
}

function createChatEntry (entry){
    entry.chatString = entry.chatString.replace (":)", "<span class='smiley'></span>");
    return $("<span class=\"success\">").append(entry.username + "> " + entry.chatString);
}

function ajaxUsersList() {
    $.ajax({
        url: CHAT_USERS_URL,
        data: {gameId: myGameId},
        success: function(users) {
            console.log("in users list call back");
            if (users.result === true) {
                refreshUsersList(users.chatUsers);
            }
        }
    });
}

//call the server and get the chat version
//we also send it the current chat version so in case there was a change
//in the chat content, we will get the new string as well
function ajaxChatContent() {
    $.ajax({
        url: GET_CHAT_URL,
        data: {gameId: myGameId, chatversion: myChatVersion},
        gameId: myGameId,
        dataType: 'json',
        success: function(data) {
            /*
             data is of the next form:
             {
                "entries": [
                    {
                        "chatString":"Hi",
                        "username":"bbb",
                        "time":1485548397514
                    },
                    {
                        "chatString":"Hello",
                        "username":"bbb",
                        "time":1485548397514
                    }
                ],
                "version":1
             }
             */
            console.log("Server chat version: " + data.version + ", Current chat version: " + myChatVersion);
            if (data.version !== myChatVersion) {
                myChatVersion = data.version;
                appendToChatArea(data.entries);
            }
            triggerAjaxChatContent();
        },
        error: function(error) {
            triggerAjaxChatContent();
        }
    });
}

//add a method to the button in order to make that form use AJAX
//and not actually submit the form
$(function() { // onload...do
    //add a function to the submit event
    $("#sendChat").submit(function() {
        var chatContent = $(this).serializeArray();
        chatContent.push({name: "gameId", value: myGameId});
        $.ajax({
            data: $.param(chatContent),
            url: SEND_CHAT_URL,
            timeout: 2000,
            error: function() {
                console.error("Failed to submit");
            },
            success: function(r) {
                //do not add the user string to the chat area
                //since it's going to be retrieved from the server
                //$("#result h1").text(r);
            }
        });

        $("#userstring").val("");
        // by default - we'll always return false so it doesn't redirect the user.
        return false;
    });
});

function triggerAjaxChatContent() {
    setTimeout(ajaxChatContent, refreshRate);
}

$(document).ready(function() {
    $("#exitChatBtn").click(function () {
        $.ajax({
            type: "POST",
            url: EXIT_CAHT_URL,
            data: {gameId: myGameId},
            dataType: "json",
            success: function (resp) {
                console.log(resp);
                var success = resp['result'];

                if (success == false) {
                    var error = resp['msg'];
                    tempAlert(error);
                }

                if (success == true) {
                    window.close();
                }
            }
        });
    });
});

//activate the timer calls after the page is loaded
$(function() {
    myGameId = getUrlParameter('gameId');
    myGameName = decodeURI(getUrlParameter('gameName'));
    document.title = myGameName;
    $("#chatTitle").html("Chat for the game: " + myGameName);
    //The users list is refreshed automatically every second
    setInterval(ajaxUsersList, refreshRate);

    //The chat content is refreshed only once (using a timeout) but
    //on each call it triggers another execution of itself later (1 second later)
    triggerAjaxChatContent();
});