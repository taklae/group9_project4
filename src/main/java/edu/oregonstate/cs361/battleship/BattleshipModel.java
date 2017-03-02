package edu.oregonstate.cs361.battleship;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by michaelhilton on 1/4/17.
 */
public class BattleshipModel {

    int[][] board = new int[10][10];
    int AllShipsPlaced=0;

    private Ship aircraftCarrier = new Ship("AircraftCarrier",5, new Coordinate(0,0),new Coordinate(0,0));
    private MilitaryShip battleship = new MilitaryShip("Battleship",4, new Coordinate(0,0),new Coordinate(0,0));
    private CivilianShip clipper = new CivilianShip("Clipper", 3, new Coordinate(0,0),new Coordinate(0,0)  );
    private CivilianShip dinghy= new CivilianShip("Dinghy", 1, new Coordinate(0,0),new Coordinate(0,0)  );
    private MilitaryShip submarine = new MilitaryShip("Submarine",2, new Coordinate(0,0),new Coordinate(0,0));

    private Ship computer_aircraftCarrier = new Ship("Computer_AircraftCarrier",5, new Coordinate(2,2),new Coordinate(2,6));
    private MilitaryShip computer_battleship = new MilitaryShip("Computer_Battleship",4, new Coordinate(2,8),new Coordinate(5,8));
    private CivilianShip computer_clipper = new CivilianShip("Clipper", 3, new Coordinate(4,1),new Coordinate(4,3)  );
    private CivilianShip computer_dinghy= new CivilianShip("Dinghy", 1, new Coordinate(7,3),new Coordinate(7,3)  );
    private MilitaryShip computer_submarine = new MilitaryShip("Computer_Submarine",2, new Coordinate(9,6),new Coordinate(9,7));

    ArrayList<Coordinate> playerHits;
    ArrayList<Coordinate> playerMisses;
    ArrayList<Coordinate> computerHits;
    ArrayList<Coordinate> computerMisses;

    int scanResult = 2;
    int isGameOver = 0;
    int validPlace = 0;

    public BattleshipModel() {
        playerHits = new ArrayList<>();
        playerMisses= new ArrayList<>();
        computerHits = new ArrayList<>();
        computerMisses= new ArrayList<>();
    }


    public Ship getShip(String shipName) {
        if (shipName.equalsIgnoreCase("aircraftcarrier")) {
            return aircraftCarrier;
        } if(shipName.equalsIgnoreCase("battleship")) {
            return battleship;
        } if(shipName.equalsIgnoreCase("clipper")) {
        return clipper;
        } if(shipName.equalsIgnoreCase("dinghy")) {
            return dinghy;
        }if(shipName.equalsIgnoreCase("submarine")) {
            return submarine;
        } else {
            return null;
        }
    }

    public BattleshipModel placeShip(String shipName, String row, String col, String orientation) {
        int rowint = Integer.parseInt(row);
        int colInt = Integer.parseInt(col);
        if(orientation.equals("horizontal")){
            if (shipName.equalsIgnoreCase("aircraftcarrier")) {
                this.getShip(shipName).setLocation(new Coordinate(rowint,colInt),new Coordinate(rowint,colInt+4));
            } if(shipName.equalsIgnoreCase("battleship")) {
                this.getShip(shipName).setLocation(new Coordinate(rowint,colInt),new Coordinate(rowint,colInt+3));
            } if(shipName.equalsIgnoreCase("clipper")) {
                this.getShip(shipName).setLocation(new Coordinate(rowint,colInt),new Coordinate(rowint,colInt+2));
            } if(shipName.equalsIgnoreCase("dinghy")) {
                this.getShip(shipName).setLocation(new Coordinate(rowint,colInt),new Coordinate(rowint,colInt));
            }if(shipName.equalsIgnoreCase("submarine")) {
                this.getShip(shipName).setLocation(new Coordinate(rowint, colInt), new Coordinate(rowint, colInt + 1));
            }
        }else{
            //vertical
                if (shipName.equalsIgnoreCase("aircraftcarrier")) {
                    this.getShip(shipName).setLocation(new Coordinate(rowint,colInt),new Coordinate(rowint+4,colInt));
                } if(shipName.equalsIgnoreCase("battleship")) {
                    this.getShip(shipName).setLocation(new Coordinate(rowint,colInt),new Coordinate(rowint+3,colInt));
                } if(shipName.equalsIgnoreCase("clipper")) {
                    this.getShip(shipName).setLocation(new Coordinate(rowint,colInt),new Coordinate(rowint+2,colInt));
                } if(shipName.equalsIgnoreCase("dinghy")) {
                    this.getShip(shipName).setLocation(new Coordinate(rowint,colInt),new Coordinate(rowint,colInt));
                }if(shipName.equalsIgnoreCase("submarine")) {
                    this.getShip(shipName).setLocation(new Coordinate(rowint, colInt), new Coordinate(rowint + 1, colInt));
                }
        }
        return this;
    }

    public void shootAtComputer(int row, int col) {
        Coordinate coor = new Coordinate(row,col);
        if (computer_aircraftCarrier.covers(coor)) {
            if(computer_aircraftCarrier.armorless)
            {
                OneShootShip(computer_aircraftCarrier.length,computer_aircraftCarrier.start,computer_aircraftCarrier.end,"comp");
            }
            else
                computerHits.add(coor);
        }
        else if (computer_battleship.covers(coor)) {
            if(computer_battleship.armorless)
            {
                OneShootShip(computer_battleship.length,computer_battleship.start,computer_battleship.end,"comp");
            }
            else
                computerHits.add(coor);
        }
        else if (computer_clipper.covers(coor)) {
            if(computer_clipper.armorless)
            {
                OneShootShip(computer_clipper.length,computer_clipper.start,computer_clipper.end,"comp");
            }
            else
                computerHits.add(coor);
        }
        else if (computer_dinghy.covers(coor)) {
            if(computer_dinghy.armorless)
            {
                OneShootShip(computer_dinghy.length,computer_dinghy.start,computer_dinghy.end,"comp");
            }
            else
                computerHits.add(coor);
        }
        else if (computer_submarine.covers(coor)) {
            if(computer_submarine.armorless)
            {
                OneShootShip(computer_submarine.length,computer_submarine.start,computer_submarine.end,"comp");
            }
            else
                computerHits.add(coor);
        }
        else {
            computerMisses.add(coor);
        }
    }

    public void shootAtPlayer() {
        int max = 10;
        int min = 1;
        Random random = new Random();

        int randRow = random.nextInt(max - min + 1) + min;
        int randCol = random.nextInt(max - min + 1) + min;
        Coordinate coor = new Coordinate(randCol,randRow);

        while (checkRepeatFire(coor)) {
            randRow = random.nextInt(max - min + 1) + min;
            randCol = random.nextInt(max - min + 1) + min;
            coor.setAcross(randCol);
            coor.setDown(randRow);
        }
        playerShot(coor);
    }

    void playerShot(Coordinate coor) {

        if (aircraftCarrier.covers(coor)) {
            if(aircraftCarrier.armorless)
            {
                OneShootShip(aircraftCarrier.length,aircraftCarrier.start,aircraftCarrier.end,"player");
            }
            else
                playerHits.add(coor);
        }
        else if (battleship.covers(coor)) {
            if(battleship.armorless)
            {
                OneShootShip(battleship.length,battleship.start,battleship.end,"player");
            }
            else
                playerHits.add(coor);
        }
        else if (clipper.covers(coor)) {
            if(clipper.armorless)
            {
                OneShootShip(clipper.length,clipper.start,clipper.end,"player");
            }
            else
                playerHits.add(coor);
        }
        else if (dinghy.covers(coor)) {
            if(dinghy.armorless)
            {
                OneShootShip(dinghy.length,dinghy.start,dinghy.end,"player");
            }
            else
                playerHits.add(coor);
        }
        else if (submarine.covers(coor)) {
            if(submarine.armorless)
            {
                OneShootShip(submarine.length,submarine.start,submarine.end,"player");
            }
            else
                playerHits.add(coor);
        }
        else {
            playerMisses.add(coor);
        }
    }

    void OneShootShip(int length, Coordinate StartCord, Coordinate EndCord ,String who){
        int SAcross= StartCord.getAcross();
        int SDown=StartCord.getDown();
        int EAcross=EndCord.getAcross();
        Coordinate cord= StartCord;

        //horizontal
        if(SAcross==EAcross) {
            if(who.equals("player"))
                playerHits.add(cord);
            else
                computerHits.add(cord);

            for (int i =0; i <length-1; i++) {
                cord= new Coordinate(SAcross,(cord.getDown()+1));
                if(who.equals("player"))
                    playerHits.add(cord);
                else
                    computerHits.add(cord);
            }
        }

        //vertical
        else{
            if(who.equals("player"))
                playerHits.add(cord);
            else
                computerHits.add(cord);

            for (int i =0; i <length-1; i++) {
                cord= new Coordinate((cord.getAcross()+1),SDown);
                if(who.equals("player"))
                    playerHits.add(cord);
                else
                    computerHits.add(cord);
            }
        }
    }

    public void scan(int rowInt, int colInt) {
        Coordinate coor = new Coordinate(rowInt,colInt);
        ArrayList<Ship> shipList = new ArrayList<Ship>(Arrays.asList(computer_aircraftCarrier, computer_battleship, computer_clipper, computer_dinghy, computer_submarine));
        for (int i = 0; i< shipList.size(); i++) {
            Ship temp = shipList.get(i);
            if (temp.stealth == false && temp.scan(coor)) {
                scanResult = 1;
                return;
            } else {
                scanResult = 0;
            }
        }
    }

    public int getScanResult() {
        return scanResult;
    }

    public void  RandShips() {
        int lengths[] = {2, 1, 3, 4, 5};

        for (int k = 0; k < 5; k++) {

            int xcord = 0;
            int ycord = 0;
            int orientation = 0;
            int overlap = 0;
            int counter = 0;
            int size= lengths[k];
            int[] StartCord=new int[2];
            int[] EndCord=new int[2];

            Random rand = new Random();
            boolean crash = true;
            boolean sameloc = true;

            while (crash) {
                overlap = 0;
                if (counter == 0)
                    orientation = rand.nextInt(1) + 1;
                else if (counter == 2) {
                    counter = 0;
                    orientation = rand.nextInt(1) + 1;
                    sameloc = true;
                }
                else
                    orientation++;


                while (sameloc) {
                    xcord = rand.nextInt(9);
                    ycord = rand.nextInt(9);

                    if (board[xcord][ycord] == 0)
                        sameloc = false;
                }

                //horizontal right orientation check
                if (orientation %2 == 0) {
                    if ((ycord + size) <= 9) {
                        for (int i = ycord; i < (ycord + size); i++) {
                            if (board[xcord][i] == 1)
                                overlap = 1;
                        }

                        if (overlap == 0) {
                            for (int i = ycord; i < (ycord + size); i++) {
                                board[xcord][i] = 1;
                                EndCord[0]=xcord;
                                EndCord[1]=i;
                            }
                            crash = false;
                        }
                    }
                }

                //vertical down orientation check
                if (orientation % 2 == 1) {
                    if ((xcord + size) <= 9) {
                        for (int i = xcord; i < (xcord + size); i++) {
                            if (board[i][ycord] == 1)
                                overlap = 1;
                        }

                        if (overlap == 0) {
                            for (int i = xcord; i < (xcord + size); i++) {
                                board[i][ycord] = 1;
                                EndCord[0]=i;
                                EndCord[1]=ycord;
                            }
                            crash = false;
                        }
                    }
                }

                counter++;
                StartCord[0]=xcord;
                StartCord[1]=ycord;
            }


            //records the starting and ending coordinates for each ship
            if(k==0){
                this.getShip("submarine").setLocation(new Coordinate(StartCord[0]+1,StartCord[1]+1),new Coordinate(EndCord[0]+1,EndCord[1]+1));
            }
            else if(k==1){
                this.getShip("dinghy").setLocation(new Coordinate(StartCord[0]+1,StartCord[1]+1),new Coordinate(EndCord[0]+1,EndCord[1]+1));
            }
            else if(k==2){
                this.getShip("clipper").setLocation(new Coordinate(StartCord[0]+1,StartCord[1]+1),new Coordinate(EndCord[0]+1,EndCord[1]+1));
            }
            else if(k==3){
                this.getShip("battleship").setLocation(new Coordinate(StartCord[0]+1,StartCord[1]+1),new Coordinate(EndCord[0]+1,EndCord[1]+1));
            }
            else if(k==4){
                this.getShip("aircraftcarrier").setLocation(new Coordinate(StartCord[0]+1,StartCord[1]+1),new Coordinate(EndCord[0]+1,EndCord[1]+1));
                AllShipsPlaced=1;
            }
        }
    }

    public void printf() {
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                System.out.print(board[x][y]);
            }
            System.out.print("\n");
        }
        System.out.print(submarine);
        System.out.print("\n");
        System.out.print(dinghy);
        System.out.print("\n");
        System.out.print(clipper);
        System.out.print("\n");
        System.out.print(battleship);
        System.out.print("\n");
        System.out.print(aircraftCarrier);
        System.out.print("\n");
    }

    public int checkWin(List<Coordinate> phits, List<Coordinate> chits) {
        if (phits.size() == 15) {
            return 1;
        } else if (chits.size() == 15) {
            return 2;
        } else {
            return 0;
        }
    }

    public boolean checkRepeatFire(Coordinate coor){
        for (Coordinate aHit : playerHits) {
            if (coor.getDown() == aHit.getDown() && coor.getAcross() == aHit.getAcross())
                return true;
        }
        for (Coordinate aMiss : playerMisses) {
            if (coor.getDown() == aMiss.getDown() && coor.getAcross() == aMiss.getAcross())
                return true;
        }
        return false;
    }


    static boolean checkRepeatFireArray(Coordinate cord, List<Coordinate> hit, List<Coordinate> miss) {
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
