package edu.oregonstate.cs361.battleship;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by michaelhilton on 2/7/17.
 */
class BattleshipModelTest {

    @Test
    void getShip() {
        BattleshipModel model = new BattleshipModel();
        assertEquals("AircraftCarrier",model.getShip("AircraftCarrier").getName());
        assertEquals("Battleship",model.getShip("battleship").getName());
        assertEquals("Clipper",model.getShip("Clipper").getName());
        assertEquals("Dinghy",model.getShip("Dinghy").getName());
        assertEquals("Submarine",model.getShip("Submarine").getName());
        assertNull(model.getShip("SS Minnow"));
    }

    Boolean testIfCovers(BattleshipModel model,String shipName, String row, String col, String orientation,int intRow, int intCol){
      return  model.placeShip(shipName,row,col,orientation).getShip(shipName).covers(new Coordinate(intRow,intCol));
    }

    @Test
    void placeShip() {
        BattleshipModel model = new BattleshipModel();
        assertEquals(true,
                testIfCovers(model, "AircraftCarrier","1","1","horizontal",1,1));
        assertEquals(true,
                testIfCovers(model, "AircraftCarrier","1","1","vertical",1,1));
        assertEquals(false,
                testIfCovers(model, "AircraftCarrier","1","1","horizontal",9,9));
        assertEquals(false,
                testIfCovers(model, "AircraftCarrier","1","1","vertical",9,9));

        assertEquals(true,
                    testIfCovers(model, "Battleship","1","1","horizontal",1,1));
        assertEquals(true,
                testIfCovers(model, "Battleship","1","1","vertical",1,1));
        assertEquals(false,
                testIfCovers(model, "Battleship","1","1","horizontal",9,9));
        assertEquals(false,
                testIfCovers(model, "Battleship","1","1","vertical",9,9));

        assertEquals(true,
                testIfCovers(model, "Clipper","1","1","horizontal",1,1));
        assertEquals(true,
                testIfCovers(model, "Clipper","1","1","vertical",1,1));
        assertEquals(false,
                testIfCovers(model, "Clipper","1","1","horizontal",9,9));
        assertEquals(false,
                testIfCovers(model, "Clipper","1","1","vertical",9,9));

        assertEquals(true,
                testIfCovers(model, "Dinghy","1","1","horizontal",1,1));
        assertEquals(true,
                testIfCovers(model, "Dinghy","1","1","vertical",1,1));
        assertEquals(false,
                testIfCovers(model, "Dinghy","1","1","horizontal",9,9));
        assertEquals(false,
                testIfCovers(model, "Dinghy","1","1","vertical",9,9));

        assertEquals(true,
                testIfCovers(model, "Submarine","1","1","horizontal",1,1));
        assertEquals(true,
                testIfCovers(model, "Submarine","1","1","vertical",1,1));
        assertEquals(false,
                testIfCovers(model, "Submarine","1","1","horizontal",9,9));
        assertEquals(false,
                testIfCovers(model, "Submarine","1","1","vertical",9,9));

        assertNull(model.placeShip("Submarine","1","1","horizontal").getShip("USS Minnow"));


    }

    @Test
    void shootAtComputer() {
        BattleshipModel model = new BattleshipModel();
        model.shootAtComputer(1,1) ;
        assertEquals(true, model.computerHits.isEmpty());
        model.checkRepeatFireArray(new Coordinate(1, 1), model.computerHits, model.computerMisses);

        model.shootAtComputer(2,3) ;
        assertEquals(2, model.computerHits.get(0).getAcross());
        assertEquals(3, model.computerHits.get(0).getDown());
        model.checkRepeatFireArray(new Coordinate(2, 3), model.computerHits, model.computerMisses);

        model.shootAtComputer(2,2) ;
        assertEquals(2, model.computerHits.get(1).getAcross());
        assertEquals(2, model.computerHits.get(1).getDown());

        model.shootAtComputer(4,1) ;
        assertEquals(4, model.computerHits.get(2).getAcross());
        assertEquals(1, model.computerHits.get(2).getDown());

        assertEquals(4, model.computerHits.get(3).getAcross());
        assertEquals(2, model.computerHits.get(3).getDown());

        assertEquals(4, model.computerHits.get(4).getAcross());
        assertEquals(3, model.computerHits.get(4).getDown());
    }


    @Test
    void shootAtPlayer() {
        BattleshipModel model = new BattleshipModel();
        model.placeShip("Aircraftcarrier","1","5","horizontal");
        model.placeShip("Battleship","2","4","horizontal");
        model.placeShip("Clipper","3","3","horizontal");
        model.placeShip("Dinghy","4","2","horizontal");
        model.placeShip("Submarine","5","1","horizontal");

        model.playerShot(new Coordinate(9,9));
        assertEquals(true, model.playerHits.isEmpty());
        model.checkRepeatFire(new Coordinate(9,9));

        model.playerShot(new Coordinate(1,5));
        assertEquals(1, model.playerHits.get(0).getAcross());
        assertEquals(5, model.playerHits.get(0).getDown());
        model.checkRepeatFire(new Coordinate(1,5));

        model.playerShot(new Coordinate(2,4));
        assertEquals(2, model.playerHits.get(1).getAcross());
        assertEquals(4, model.playerHits.get(1).getDown());

        model.playerShot(new Coordinate(3,3));
        assertEquals(3, model.playerHits.get(2).getAcross());
        assertEquals(3, model.playerHits.get(2).getDown());
        assertEquals(3, model.playerHits.get(3).getAcross());
        assertEquals(4, model.playerHits.get(3).getDown());
        assertEquals(3, model.playerHits.get(4).getAcross());
        assertEquals(5, model.playerHits.get(4).getDown());

        model.playerShot(new Coordinate(4,2));
        assertEquals(4, model.playerHits.get(5).getAcross());
        assertEquals(2, model.playerHits.get(5).getDown());

        model.playerShot(new Coordinate(5,1));
        assertEquals(5, model.playerHits.get(6).getAcross());
        assertEquals(1, model.playerHits.get(6).getDown());
    }

    @Test
    void testsScan() {
        BattleshipModel model = new BattleshipModel();
        model.scan(2,2);
        assertEquals(1,model.getScanResult());

        model.scan(6,6);
        assertEquals(0,model.getScanResult());

    }

    @Test
    void  RandShips(){
        BattleshipModel model = new BattleshipModel();
        BattleshipModel model2= new BattleshipModel();
        int row=0;
        int column=0;

        //checks that both models start out empty
        assertEquals(model.checkCor("dinghy",0,0),model2.checkCor("dingy",0,0));

        model.RandShips("player");
        model2.RandShips("player");

        //tests that two independent classes created does in fact produce random
        //coordinates if they are unequal. however since the random numbers are
        //restrained to a specific range eventually this assert will fail when
        //both generate the same random numbers.

        //finds the cordinate for the dingy in model 1
        for(int i=0; i<9; i++) {
            for(int j=0; j<9; j++){
                if(model.checkCor("dinghy",i,j)==1){
                    row=i;
                    column=j;
                }
            }
        }

        //checks that dingy is in fact in the location found in model one
        //but it is not in the same location in model 2
        //therefore the randships function did in fact randomly place it somewhere else
        assertEquals(1,model.checkCor("dinghy",row,column));
        assertEquals(0,model2.checkCor("dinghy",row,column));
    }




}