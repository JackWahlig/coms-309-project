var ws;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#playerData").html("");
}

function connect() {
    ws = new WebSocket("ws://" + document.location.host + "/websocket/" + $("#playerId").val());
    setConnected(true);
    ws.onmessage = function(event) {
        console.log(JSON.parse(event.data));
        data = JSON.parse(event.data);
        $("#playerData").append("<tr><td>gameId: " + data.gameId +  ", playerId: " + data.playerId + ", turretAngle: " + data.turretAngle + ", shieldAngle: " + data.shieldAngle + "</td></tr>");
    };
}

function sendPlayerData() {
    var data = {
        gameId: $("#gameId").val(),
        playerId: $("#playerId").val(),
        turretAngle: $("#turretAngle").val(),
        shieldAngle: $("#shieldAngle").val()
    };
    ws.send(JSON.stringify(data));
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#send" ).click(function() { sendPlayerData(); });
});