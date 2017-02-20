package edu.oregonstate.cs361.battleship;

import com.google.gson.Gson;
import spark.Request;
import java.util.List;
import java.io.UnsupportedEncodingException;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

public class Main {

    public static void main(String[] args) {

        staticFiles.location("/public");

        //This will listen to GET requests to /model and return a clean new model
        get("/model", (req, res) -> newModel());
        //This will listen to POST requests and expects to receive a game model, as well as location to fire to
        post("/fire/:row/:col", (req, res) -> fireAt(req));
        //This will listen to POST requests and expects to receive a game model, as well as location to scan
        post("/scan/:row/:col", (req, res) -> scan(req));
        //This will listen to POST requests and expects to receive a game model, as well as location to place the ship
        post("/placeShip/:id/:row/:col/:orientation", (req, res) -> placeShip(req));
        //This will listen to POST requests for a new game, and then return a clean model
        post("/newGame", (req, res) -> newModel());

    }

    //This function returns a new model
    private static String newModel() {
        BattleshipModel bm = new BattleshipModel();
        Gson gson = new Gson();
        return gson.toJson(bm);
    }

    //This function accepts an HTTP request and deseralizes it into an actual Java object.
    private static BattleshipModel getModelFromReq(Request req){
        Gson gson = new Gson();
        String result = "";
        try {
            result = java.net.URLDecoder.decode(req.body(),"US-ASCII");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        BattleshipModel modelFromReq = gson.fromJson(result, BattleshipModel.class);
        return modelFromReq;
    }

    private static boolean checkPlace(Request req){
        String id = req.params("id");
        String row = req.params("row");
        String col = req.params("col");
        String orientation = req.params("orientation");

        int length = 1;
        System.out.println(id);
        if(id.equals("aircraftCarrier"))
            length = 5;
        else if (id.equals("battleship"))
            length = 4;
        else if(id.equals("cruiser"))
            length = 3;
        else
            length = 2;

        System.out.println(length);
        if(orientation.equals("vertical")  && length + Integer.parseInt(row) > 10)
            return false;
        else if(orientation.equals("horizontal") && length + Integer.parseInt(col) > 10)
            return false;

        /*
        if(orientation.equals("vertical")){
            Coordinate tiles= new Coordinate();
        }else{

        }
        */
        return true;
    }

    //This controller
    private static String placeShip(Request req) {
        BattleshipModel currModel = getModelFromReq(req);
        String id = req.params("id");
        String row = req.params("row");
        String col = req.params("col");
        String orientation = req.params("orientation");
        currModel.scanResult = 2;

        if(checkPlace(req) ==  false)
            currModel.validPlace = 1;

        if(id.equals("random") && currModel.AllShipsPlaced == 0)
            currModel.RandShips();
        else if(currModel.AllShipsPlaced == 0 && currModel.validPlace != 1)
            currModel = currModel.placeShip(id,row,col,orientation);

        Gson gson = new Gson();
        return gson.toJson(currModel);
    }

    private static String fireAt(Request req) {

        BattleshipModel currModel = getModelFromReq(req);
        String row = req.params("row");
        String col = req.params("col");
        int rowInt = Integer.parseInt(row);
        int colInt = Integer.parseInt(col);
        currModel.scanResult = 2;
        Coordinate fire = new Coordinate(colInt, rowInt);
        if (! checkRepeatFire(fire, currModel.computerHits, currModel.computerMisses)) {
            currModel.shootAtComputer(rowInt, colInt);
        }
        currModel.shootAtPlayer();
        currModel.isGameOver = currModel.checkWin(currModel.computerHits, currModel.playerHits);
        Gson gson = new Gson();
        return gson.toJson(currModel);
    }


    private static String scan(Request req) {
        BattleshipModel currModel = getModelFromReq(req);
        String row = req.params("row");
        String col = req.params("col");
        int rowInt = Integer.parseInt(row);
        int colInt = Integer.parseInt(col);
        currModel.scan(rowInt,colInt);
        currModel.shootAtPlayer();
        Gson gson = new Gson();
        return gson.toJson(currModel);
    }

    static boolean checkRepeatFire(Coordinate cord, List<Coordinate> hit, List<Coordinate> miss) {
        for (Coordinate aHit : hit) {
            if (cord.getAcross() == aHit.getDown() && cord.getDown() == aHit.getAcross())
                return true;
        }
        for (Coordinate aMiss : miss) {
            if (cord.getAcross() == aMiss.getDown() && cord.getDown() == aMiss.getAcross())
                return true;
        }
        return false;
    }

}