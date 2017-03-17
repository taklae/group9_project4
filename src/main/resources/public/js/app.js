var gameModel;
//var randbutton=document.getElementById('user-select');

var ship = class {
  constructor(startAcross, startDown, endAcross, endDown) {
    this.startAcross = startAcross;
    this.endAcross = endAcross;
    this.startDown = startDown;
    this.endAcross = endAcross;
  }
};

//Create ships
var ships = new Array();
for( var i = 0; i < 5; i++)
    ships.push(new ship(0,0,0,0));

$( document ).ready(function() {
  // Handler for .ready() called.
  $.getJSON("model", function( json ) {
  gameModel = json;
    console.log( "JSON Data: " + json );
   });
});

function NewGame(){

    for( var i = 0; i < 5; i++ ){

        ships[i].startAcross = 0;
        ships[i].endAcross = 0;
        ships[i].startDown = 0;
        ships[i].endDown = 0;
    }

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

function HardAI(){

    for( var i = 0; i < 5; i++ ){

        ships[i].startAcross = 0;
        ships[i].endAcross = 0;
        ships[i].startDown = 0;
        ships[i].endDown = 0;
    }

    var request = $.ajax({
        url: "/hardAI",
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
         url: "/placeShip/"+"random"+"/"+"0"+"/"+"0"+"/"+"0",
         method: "post",
         data: JSON.stringify(gameModel),
         contentType: "application/json; charset=utf-8",
         dataType: "json"
       });

       request.done(function( currModel ) {
         displayGameState(currModel);
         gameModel = currModel;

     ships[0].startAcross    = gameModel.aircraftCarrier.start.Across;
     ships[0].startDown      = gameModel.aircraftCarrier.start.Down;
     ships[0].endAcross      = gameModel.aircraftCarrier.end.Across;
     ships[0].endDown        = gameModel.aircraftCarrier.end.Down;

     ships[1].startAcross    = gameModel.battleship.start.Across;
     ships[1].startDown      = gameModel.battleship.start.Down;
     ships[1].endAcross      = gameModel.battleship.end.Across;
     ships[1].endDown        = gameModel.battleship.end.Down;

     ships[2].startAcross    = gameModel.clipper.start.Across;
     ships[2].startDown      = gameModel.clipper.start.Down;
     ships[2].endAcross      = gameModel.clipper.end.Across;
     ships[2].endDown        = gameModel.clipper.end.Down;

     ships[3].startAcross    = gameModel.dinghy.start.Across;
     ships[3].startDown      = gameModel.dinghy.start.Down;
     ships[3].endAcross      = gameModel.dinghy.end.Across;
     ships[3].endDown        = gameModel.dinghy.end.Down;

     ships[4].startAcross    = gameModel.submarine.start.Across;
     ships[4].startDown      = gameModel.submarine.start.Down;
     ships[4].endAcross      = gameModel.submarine.end.Across;
     ships[4].endDown        = gameModel.submarine.end.Down;
       });

       request.fail(function( jqXHR, textStatus ) {
         alert( "Request failed: " + textStatus );
       });
}

function placeShip(row,column) {

   console.log(row);
   console.log(column);

   //var menuId = $( "ul.nav" ).first().attr( "id" );
   var request = $.ajax({
     url: "/placeShip/"+$( "#shipSelec" ).val()+"/"+String(row)+"/"+String(column)+"/"+$( "#orientationSelec" ).val(),
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




function fire(row, col){
    console.log(row);
    console.log(col);
//var menuId = $( "ul.nav" ).first().attr( "id" );
   var request = $.ajax({
     url: "/fire/"+String(row)+"/"+String(col),
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

function scan(row, col){
    console.log(row);
    console.log(col);
//var menuId = $( "ul.nav" ).first().attr( "id" );
    var request = $.ajax({
        url: "/scan/"+String(row)+"/"+String(col),
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

function isShipPlacedModel( ship ){

    if( ship.start.Across == 0 && ship.start.Down == 0 && ship.end.Across == 0 && ship.end.Down == 0){
        return false;
    }
    return true;
}

function displayGameState(gameModel){
$( '#MyBoard td'  ).css("background-color", "#42A5F5");
$( '#TheirBoard td'  ).css("background-color", "#42A5F5");

if(gameModel.scanResult == 1){

    document.getElementById("mBox").innerHTML = "Scan found at least one ship!"
    document.getElementById("mBox").style.color = "black";
    document.getElementById("mBox").style.borderColor = "#FDD835";

} else if (gameModel.scanResult == 0){

    document.getElementById("mBox").innerHTML = "Scan found no ships!"
    document.getElementById("mBox").style.color = "black";
    document.getElementById("mBox").style.borderColor = "#e7e7e7";
}

if(gameModel.validPlace == 1){

    document.getElementById("mBox").innerHTML = "Invalid ship placement!";
    document.getElementById("mBox").style.color = "#E64A19";
    document.getElementById("mBox").style.borderColor = "#e7e7e7";

    gameModel.validPlace = 0;
}

if(gameModel.AllShipsPlaced>1)
{
    document.getElementById("mBox").innerHTML = "All ships have been placed already!";
    document.getElementById("mBox").style.color = "#E64A19";
    document.getElementById("mBox").style.borderColor = "#e7e7e7";
}

if(gameModel.shipsHit == 1){

    document.getElementById("mBox").innerHTML = "Hit!";
    document.getElementById("mBox").style.color = "black";
    document.getElementById("mBox").style.borderColor = "#FDD835";

} else if (gameModel.shipsHit == 0){

    document.getElementById("mBox").innerHTML = "Miss.";
    document.getElementById("mBox").style.color = "black";
    document.getElementById("mBox").style.borderColor = "#e7e7e7";
}

if(gameModel.repeatFire == 1){

    document.getElementById("mBox").innerHTML = "You have already fired at that location.";
    document.getElementById("mBox").style.color = "#E64A19";
    document.getElementById("mBox").style.borderColor = "#e7e7e7";
    gameModel.repeatFire = 0;
}

if(gameModel.isGameOver == 1) {

    document.getElementById("mBox").innerHTML = "You win!";
    document.getElementById("mBox").style.borderColor = "#FDD835";

} else if (gameModel.isGameOver == 2){

    document.getElementById("mBox").innerHTML = "Computer wins!";
    document.getElementById("mBox").style.borderColor = "#FDD835";
}



displayShip(gameModel.aircraftCarrier);
displayShip(gameModel.battleship);
displayShip(gameModel.clipper);
displayShip(gameModel.dinghy);
displayShip(gameModel.submarine);

if( isShipPlacedModel(gameModel.aircraftCarrier) ||
 isShipPlacedModel(gameModel.battleship) ||
 isShipPlacedModel(gameModel.clipper) ||
 isShipPlacedModel(gameModel.dinghy) ||
 isShipPlacedModel(gameModel.submarine)){

    document.getElementById("newGame").disabled = false;
    document.getElementById("random").disabled = true;
}
else if( isShipPlacedModel(gameModel.aircraftCarrier) &&
 isShipPlacedModel(gameModel.battleship) &&
 isShipPlacedModel(gameModel.clipper) &&
 isShipPlacedModel(gameModel.dinghy) &&
 isShipPlacedModel(gameModel.submarine)){

}
else{

    document.getElementById("newGame").disabled = true;
    document.getElementById("random").disabled = false;

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

function checkShipIntersection(startA, startD, endA, endD, length){

    //Check against each ship
    for(var i = 0; i < 5; i++){

        //If the ship is actually placed
        if(isShipPlaced(i)){

            //Test vertical ship again horizonal
            if(startD == endD && ships[i].startAcross == ships[i].endAcross){

                if(startD >= ships[i].startDown && startD <= ships[i].startDown + (ships[i].endDown-ships[i].startDown)
                   && ships[i].startAcross >= startA && ships[i].startAcross < startA + length )
                    return true;
            }
            //Test horizontal against vertical
            else if(startA == endA && ships[i].startDown == ships[i].endDown){

                if(ships[i].startDown >= startD && ships[i].startDown < startD + length
                   && startA >= ships[i].startAcross && startA <= ships[i].startAcross + (ships[i].endAcross-ships[i].startAcross))
                    return true;
            }
            //Test horizontal against horizontal
            else if( startA == ships[i].startAcross ){

                if(startD >= ships[i].startDown && startD <= ships[i].startDown + (ships[i].endDown-ships[i].startDown)
                || endD >= ships[i].startDown && endD <= ships[i].startDown + (ships[i].endDown-ships[i].startDown))
                    return true;
            }
            //Test vertical against vertical
            else if(startD == ships[i].startDown){

                if(startA >= ships[i].startAcross && startA <= ships[i].startAcross + (ships[i].endAcross-ships[i].startAcross)
                || endA >= ships[i].startAcross && endA <= ships[i].startAcross + (ships[i].endAcross-ships[i].startAcross))
                    return true;
            }
        }
    }

    return false;
}

function isShipPlaced(index){

    if(ships[index].startAcross != 0 && ships[index].startDown != 0
    && ships[index].endAcross != 0 && ships[index].endDown != 0){

        return true;

    }

    return false;
}

function previewShip(coordinates, erase, place){

    var shipLength = 0;
    var orientation = document.getElementById("orientationSelec").selectedIndex;
    var shipType = document.getElementById("shipSelec").selectedIndex;

    if( !isShipPlaced(shipType) ){

    //Get the length of the ship
    switch(shipType){

        case 0:
            shipLength = 5;
        break;
        case 1:
            shipLength = 4;
        break;
        case 2:
            shipLength = 3;
        break;
        case 3:
            shipLength = 1;
        break;
        case 4:
            shipLength = 2;
        break;

    }

    startCoordAcross = Number(coordinates[0]);
    startCoordDown = Number(coordinates[1]);

    if( orientation == 1 ){

        endCoordAcross = Number(startCoordAcross) + Number(shipLength) - 1;
        endCoordDown = startCoordDown;

    }
    else{
        endCoordAcross = startCoordAcross;
        endCoordDown = Number(startCoordDown) + Number(shipLength) - 1;

    }

     if(startCoordAcross > 0 && startCoordAcross <= 10
     && startCoordDown > 0 && startCoordDown <= 10
     && endCoordAcross > 0 && endCoordAcross <= 10
     && endCoordDown > 0 && endCoordDown <= 10
     && checkShipIntersection(startCoordAcross, startCoordDown, endCoordAcross, endCoordDown, shipLength) == false){

        if(startCoordAcross == endCoordAcross){
            for (i = startCoordDown; i <= endCoordDown; i++) {

                if( !erase )
                    $( '#MyBoard #'+startCoordAcross+'_'+i  ).css("background-color", "#FDD835");
                else
                    $( '#MyBoard #'+startCoordAcross+'_'+i  ).css("background-color", "#42A5F5");
            }
        } else {
            for (i = startCoordAcross; i <= endCoordAcross; i++) {

                if( !erase )
                    $( '#MyBoard #'+i+'_'+startCoordDown  ).css("background-color", "#FDD835");
                else
                    $( '#MyBoard #'+i+'_'+startCoordDown  ).css("background-color", "#42A5F5");
            }
        }
        if( place ){
            placeShip(coordinates[0],coordinates[1]);

            //Update ship coordinates
            ships[shipType].startAcross = startCoordAcross;
            ships[shipType].startDown   = startCoordDown;
            ships[shipType].endAcross   = endCoordAcross;
            ships[shipType].endDown     = endCoordDown;
        }

     }
     }
}

function previewShoot(coordinates, erase, shoot) {
    coordAcross = Number(coordinates[0]);
    coordDown = Number(coordinates[1]);

    if (shoot) {

        if( isShipPlaced(0) && isShipPlaced(1) && isShipPlaced(2) && isShipPlaced(3) && isShipPlaced(4)){

            if (document.getElementById("fire").checked == true)
                fire(coordinates[0],coordinates[1]);
            if (document.getElementById("scan").checked == true)
                scan(coordinates[0],coordinates[1]);
            return;
        }
        else{

            document.getElementById("mBox").innerHTML = "You must first place all your ships before you can fire or scan!"
            document.getElementById("mBox").style.color = "#E64A19";
        }
    }


    if( !erase)
        $( '#TheirBoard #'+coordAcross+'_'+coordDown  ).css("background-color", "#FDD835");
    else {
        $('#TheirBoard #' + coordAcross + '_' + coordDown).css("background-color", "#42A5F5");

        for (var i = 0; i < gameModel.computerMisses.length; i++) {
            $( '#TheirBoard #' + gameModel.computerMisses[i].Across + '_' + gameModel.computerMisses[i].Down ).css("background-color", "#4CAF50");
        }
        for (var i = 0; i < gameModel.computerHits.length; i++) {
            $( '#TheirBoard #' + gameModel.computerHits[i].Across + '_' + gameModel.computerHits[i].Down ).css("background-color", "#E64A19");
        }
    }
}
