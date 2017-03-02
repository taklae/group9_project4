var gameModel;
//var randbutton=document.getElementById('user-select');

$( document ).ready(function() {
  // Handler for .ready() called.
  $.getJSON("model", function( json ) {
  gameModel = json;
    console.log( "JSON Data: " + json );
   });
});

function NewGame(){

    var request = $.ajax({
         url: "/newGame",
         method: "post",
         data: JSON.stringify(gameModel),
         contentType: "application/json; charset=utf-8",
         dataType: "json"
       });

       request.done(function( currModel ) {
         displayGameState(currModel);
         gameModel = currModel;

       });

       request.fail(function( jqXHR, textStatus ) {
         alert( "Request failed: " + textStatus );
       });

}

function RandPlaceShips(){
    console.log("randomized ships");
    var request = $.ajax({
         url: "/placeShip/"+"random"+"/"+$( "#rowSelec" ).val()+"/"+$( "#colSelec" ).val()+"/"+$( "#orientationSelec" ).val(),
         method: "post",
         data: JSON.stringify(gameModel),
         contentType: "application/json; charset=utf-8",
         dataType: "json"
       });

       request.done(function( currModel ) {
         displayGameState(currModel);
         gameModel = currModel;
       });

       request.fail(function( jqXHR, textStatus ) {
         alert( "Request failed: " + textStatus );
       });
}

function placeShip() {
   console.log($( "#shipSelec" ).val());
   console.log($( "#rowSelec" ).val());
   console.log($( "#colSelec" ).val());
   console.log($( "#orientationSelec" ).val());

   //var menuId = $( "ul.nav" ).first().attr( "id" );
   var request = $.ajax({
     url: "/placeShip/"+$( "#shipSelec" ).val()+"/"+$( "#rowSelec" ).val()+"/"+$( "#colSelec" ).val()+"/"+$( "#orientationSelec" ).val(),
     method: "post",
     data: JSON.stringify(gameModel),
     contentType: "application/json; charset=utf-8",
     dataType: "json"
   });

   request.done(function( currModel ) {
     displayGameState(currModel);
     gameModel = currModel;

   });

   request.fail(function( jqXHR, textStatus ) {
     alert( "Request failed: " + textStatus );
   });
}




function fire(){
 console.log($( "#colFire" ).val());
   console.log($( "#rowFire" ).val());
//var menuId = $( "ul.nav" ).first().attr( "id" );
   var request = $.ajax({
     url: "/fire/"+$( "#rowFire" ).val()+"/"+$( "#colFire" ).val(),
     method: "post",
     data: JSON.stringify(gameModel),
     contentType: "application/json; charset=utf-8",
     dataType: "json"
   });

   request.done(function( currModel ) {
     displayGameState(currModel);
     gameModel = currModel;

   });

   request.fail(function( jqXHR, textStatus ) {
     alert( "Request failed: " + textStatus );
   });

}

function scan(){
 console.log($( "#colFire" ).val());
   console.log($( "#rowFire" ).val());
//var menuId = $( "ul.nav" ).first().attr( "id" );
   var request = $.ajax({
     url: "/scan/"+$( "#rowFire" ).val()+"/"+$( "#colFire" ).val(),
     method: "post",
     data: JSON.stringify(gameModel),
     contentType: "application/json; charset=utf-8",
     dataType: "json"
   });

   request.done(function( currModel ) {
     displayGameState(currModel);
     gameModel = currModel;

   });

   request.fail(function( jqXHR, textStatus ) {
     alert( "Request failed: " + textStatus );
   });

}


function log(logContents){
    console.log(logContents);
}

function isShipPlaced( ship ){

    if( ship.start.Across == 0 && ship.start.Down == 0 && ship.end.Across == 0 && ship.end.Down == 0){
        return false;
    }
    return true;
}

function displayGameState(gameModel){
$( '#MyBoard td'  ).css("background-color", "#42A5F5");
$( '#TheirBoard td'  ).css("background-color", "#42A5F5");

if(gameModel.scanResult == 1){
    alert("Scan found at least one Ship");
} else if (gameModel.scanResult == 0){
    alert("Scan found no Ships");
}

if(gameModel.validPlace == 1){
    alert("Invalid ship placement");
    gameModel.validPlace = 0;
}

if(gameModel.AllShipsPlaced>1)
{
    alert("All ships have already been placed");
}

if(gameModel.isGameOver == 1) {
    alert("Player wins!");
} else if (gameModel.isGameOver == 2){
    alert("Computer wins!");
}

displayShip(gameModel.aircraftCarrier);
displayShip(gameModel.battleship);
displayShip(gameModel.clipper);
displayShip(gameModel.dinghy);
displayShip(gameModel.submarine);

if( isShipPlaced(gameModel.aircraftCarrier) &&
 isShipPlaced(gameModel.battleship) &&
 isShipPlaced(gameModel.clipper) &&
 isShipPlaced(gameModel.dinghy) &&
 isShipPlaced(gameModel.submarine)){

    document.getElementById("fire").disabled = false;
    document.getElementById("scan").disabled = false;
    document.getElementById("place").disabled = true;
}
else{

    document.getElementById("fire").disabled = true;
    document.getElementById("scan").disabled = true;
    document.getElementById("place").disabled = false;
}

for (var i = 0; i < gameModel.computerMisses.length; i++) {
   $( '#TheirBoard #' + gameModel.computerMisses[i].Across + '_' + gameModel.computerMisses[i].Down ).css("background-color", "#4CAF50");
}
for (var i = 0; i < gameModel.computerHits.length; i++) {
   $( '#TheirBoard #' + gameModel.computerHits[i].Across + '_' + gameModel.computerHits[i].Down ).css("background-color", "#E64A19");
    //console.log( "red" );
}

for (var i = 0; i < gameModel.playerMisses.length; i++) {
   $( '#MyBoard #' + gameModel.playerMisses[i].Across + '_' + gameModel.playerMisses[i].Down ).css("background-color", "#4CAF50");
}
for (var i = 0; i < gameModel.playerHits.length; i++) {
    console.log( "red" );
   $( '#MyBoard #' + gameModel.playerHits[i].Across + '_' + gameModel.playerHits[i].Down ).css("background-color", "#E64A19");
}



}



function displayShip(ship, name){
 startCoordAcross = ship.start.Across;
 startCoordDown = ship.start.Down;
 endCoordAcross = ship.end.Across;
 endCoordDown = ship.end.Down;

 if(startCoordAcross > 0){
    if(startCoordAcross == endCoordAcross){
        for (i = startCoordDown; i <= endCoordDown; i++) {
            $( '#MyBoard #'+startCoordAcross+'_'+i  ).css("background-color", "#FDD835");
        }
    } else {
        for (i = startCoordAcross; i <= endCoordAcross; i++) {
            $( '#MyBoard #'+i+'_'+startCoordDown  ).css("background-color", "#FDD835");
        }
    }
 }



}
