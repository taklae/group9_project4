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
        //This will listen to POST requests for a new game, and then return a clean model for hard AI
        post("/hardAI", (req, res) -> hardAI());
        //This will listen to POST requests for a new game, and then return a clean model for easy AI
        post("/easyAI", (req, res) -> easyAI());
    }

    //This function returns a new model
    private static String newModel() {
        BattleshipModel bm = new BattleshipModel();
        Gson gson = new Gson();
        System.out.println(gson.toJson(bm));
        return gson.toJson(bm);
    }

    private static String hardAI() {
        HardAI bm = new HardAI();
        Gson gson = new Gson();
        bm.hardAI = 1;
        bm.RandShips("comp");
        System.out.println(gson.toJson(bm));
        return gson.toJson(bm);
    }

    private static String easyAI() {
        HardAI bm = new HardAI();
        Gson gson = new Gson();
        bm.hardAI = 0;
        System.out.println(gson.toJson(bm));
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
        if (modelFromReq.hardAI == 1)
            modelFromReq = gson.fromJson(result, HardAI.class);
        return modelFromReq;
    }

    private static boolean checkPlace(Request req){
        String id = req.params("id");
        String row = req.params("row");
        String col = req.params("col");
        String orientation = req.params("orientation");
        BattleshipModel model = getModelFromReq(req);

        int length;
        if(id.equals("aircraftCarrier"))
            length = 5;
        else if (id.equals("battleship"))
            length = 4;
        else if(id.equals("clipper"))
            length = 3;
        else if(id.equals("submarine"))
            length = 2;
        else
            length = 1;

        if(orientation.equals("vertical")  && (length-1) + Integer.parseInt(row) > 10)//check for out of bounds, vertically or horizontally
            return false;
        else if(orientation.equals("horizontal") && (length-1) + Integer.parseInt(col) > 10)
            return false;

        if(orientation.equals("vertical")){//check for ship overlap before placing
            for(int i = 0; i < length; i++)
                if(model.checkCor(id, Integer.parseInt(col), Integer.parseInt(row) + i) == 1)
                    return false;
        }else{
            for(int i = 0; i < length; i++)
                if(model.checkCor(id, Integer.parseInt(col) + i, Integer.parseInt(row)) == 1)
                    return false;
        }

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

        if(id.equals("random") && currModel.AllShipsPlaced == 0) {
            currModel.RandShips("player");
            //currModel.RandShips("comp");
        }

        else if(currModel.AllShipsPlaced == 0 && currModel.validPlace != 1)
            currModel = currModel.placeShip(id,row,col,orientation);

        Gson gson = new Gson();
        System.out.println(gson.toJson(currModel));
        return gson.toJson(currModel);
    }

    private static String fireAt(Request req) {
        BattleshipModel currModel = getModelFromReq(req);

        Gson gson = new Gson();
        System.out.println(gson.toJson(currModel));

        String row = req.params("row");
        String col = req.params("col");
        int rowInt = Integer.parseInt(row);
        int colInt = Integer.parseInt(col);
        currModel.scanResult = 2;
        Coordinate fire = new Coordinate(colInt, rowInt);
        if (! currModel.checkRepeatFireArray(fire, currModel.computerHits, currModel.computerMisses)) {
            currModel.shootAtComputer(rowInt, colInt);
            currModel.shootAtPlayer();
        } else
            currModel.repeatFire = 1;

        currModel.isGameOver = currModel.checkWin(currModel.computerHits, currModel.playerHits);
        //Gson gson = new Gson();
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
        System.out.println(gson.toJson(currModel));
        return gson.toJson(currModel);
    }
}