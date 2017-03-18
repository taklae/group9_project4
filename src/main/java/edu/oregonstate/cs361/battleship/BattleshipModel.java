package edu.oregonstate.cs361.battleship;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by michaelhilton on 1/4/17.
 */
public class BattleshipModel {

    int AllShipsPlaced=0;

    public  Ship aircraftCarrier = new Ship("AircraftCarrier",5, new Coordinate(0,0),new Coordinate(0,0));
    public  MilitaryShip battleship = new MilitaryShip("Battleship",4, new Coordinate(0,0),new Coordinate(0,0));
    public  CivilianShip clipper = new CivilianShip("Clipper", 3, new Coordinate(0,0),new Coordinate(0,0)  );
    public  CivilianShip dinghy= new CivilianShip("Dinghy", 1, new Coordinate(0,0),new Coordinate(0,0)  );
    public  MilitaryShip submarine = new MilitaryShip("Submarine",2, new Coordinate(0,0),new Coordinate(0,0));

    public  Ship computer_aircraftCarrier = new Ship("Computer_AircraftCarrier",5, new Coordinate(2,2),new Coordinate(2,6));
    public  MilitaryShip computer_battleship = new MilitaryShip("Computer_Battleship",4, new Coordinate(2,8),new Coordinate(5,8));
    public  CivilianShip computer_clipper = new CivilianShip("Clipper", 3, new Coordinate(4,1),new Coordinate(4,3)  );
    public  CivilianShip computer_dinghy= new CivilianShip("Dinghy", 1, new Coordinate(7,3),new Coordinate(7,3)  );
    public  MilitaryShip computer_submarine = new MilitaryShip("Computer_Submarine",2, new Coordinate(9,6),new Coordinate(9,7));

    ArrayList<Coordinate> playerHits;
    ArrayList<Coordinate> playerMisses;
    ArrayList<Coordinate> computerHits;
    ArrayList<Coordinate> computerMisses;

    int scanResult = 2;
    int isGameOver = 0;
    int validPlace = 0;
    int repeatFire = 0;
    int shipsHit = 2;
    int shoots=0;
    int hitSearch = 0, direction = 1, inc = 0;
    int hardAI = 0;

    Coordinate originalHit = new Coordinate(0,0);
    Coordinate searchHit = new Coordinate(0, 0);


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
        ArrayList<Ship> shipList = new ArrayList<Ship>(Arrays.asList(computer_aircraftCarrier, computer_battleship, computer_clipper, computer_dinghy, computer_submarine));
        for (int i = 0; i< shipList.size(); i++) {
            Ship temp = shipList.get(i);
            if (temp.covers(coor)) {
                if (temp instanceof CivilianShip) {
                    if (((CivilianShip) temp).getArmorless().equals("armorless")) {
                        OneShootShip(temp.length, temp.start, temp.end, "comp");
                        shipsHit = 1;
                        return;
                    }
                } else {
                    shipsHit = 1;
                    computerHits.add(coor);
                    return;
                }
            }
        }
        shipsHit = 0;
        computerMisses.add(coor);
    }

    //AI for easy mode
    public void shootAtPlayer(){
        Coordinate coor=new Coordinate(0,0);
        int sum;

        sum=shoots;

        do{
            coor.setDown((sum/10+1));
            coor.setAcross((sum%10+1));
            sum++;
            shoots++;
        } while(checkRepeatFire(coor));

        playerShot(coor);
    }


    public int checkCor(String id, int x, int y){
        Coordinate cor = new Coordinate(y, x);

        //check for aircraft carrier collision
        if(!id.equals("aircraftCarrier") && aircraftCarrier.start.getAcross() != 0 && aircraftCarrier.covers(cor))
            return 1;
        //check for battleship collision
        if(!id.equals("battleship") && battleship.start.getAcross() != 0 && battleship.covers(cor))
            return 1;
        //check for clipper collision
        if(!id.equals("clipper") && clipper.start.getAcross() != 0 && clipper.covers(cor))
            return 1;
        // check for submarine collision
        if(!id.equals("submarine") && submarine.start.getAcross() != 0 && submarine.covers(cor))
            return 1;
        //check for dinghy collision
        if(!id.equals("dinghy") &&  dinghy.start.getAcross() != 0 && dinghy.covers(cor))
            return 1;
        return 0;
    }

    void playerShot(Coordinate coor) {
        ArrayList<Ship> shipList = new ArrayList<Ship>(Arrays.asList(aircraftCarrier, battleship, clipper, dinghy, submarine));
        for (int i = 0; i< shipList.size(); i++) {
            Ship temp = shipList.get(i);
            if (temp.covers(coor)) {
                if (temp instanceof CivilianShip) {
                    if (((CivilianShip) temp).getArmorless().equals("armorless")) {
                        OneShootShip(temp.length, temp.start, temp.end, "player");
                        return;
                    }
                } else {
                    playerHits.add(coor);
                    return;
                }
            }
        }

        playerMisses.add(coor);
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
        shipsHit = 2;
        String type;
        Coordinate coor = new Coordinate(rowInt,colInt);
        ArrayList<Ship> shipList = new ArrayList<Ship>(Arrays.asList(computer_aircraftCarrier, computer_battleship, computer_clipper, computer_dinghy, computer_submarine));
        for (int i = 0; i< shipList.size(); i++) {
            Ship temp = shipList.get(i);
            if (!(temp instanceof MilitaryShip) && temp.scan(coor)) {
                scanResult = 1;
                return;
            } else if (temp instanceof MilitaryShip) {
                 type = ((MilitaryShip) temp).getStealth();
                 if (type.equals("stealth"))
                     scanResult = 0;
            } else {
                scanResult = 0;
            }
        }
    }

    public int getScanResult() {
        return scanResult;
    }

    public void  RandShips(String who) {
        int[][] board = new int[10][10];
        int lengths[] = {2, 1, 3, 4, 5};
        boolean player=false;

        if(who.equals("player"))
            player=true;

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
                    orientation = rand.nextInt(2) + 1;
                else if (counter == 2) {
                    counter = 0;
                    orientation = rand.nextInt(2) + 1;
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
                if(player)
                    submarine.setLocation(new Coordinate(StartCord[0]+1,StartCord[1]+1),new Coordinate(EndCord[0]+1,EndCord[1]+1));
                else
                    computer_submarine.setLocation(new Coordinate(StartCord[0]+1,StartCord[1]+1),new Coordinate(EndCord[0]+1,EndCord[1]+1));
            }
            else if(k==1){
                if(player)
                    dinghy.setLocation(new Coordinate(StartCord[0]+1,StartCord[1]+1),new Coordinate(EndCord[0]+1,EndCord[1]+1));
                else
                    computer_dinghy.setLocation(new Coordinate(StartCord[0]+1,StartCord[1]+1),new Coordinate(EndCord[0]+1,EndCord[1]+1));
            }
            else if(k==2){
                if(player)
                    clipper.setLocation(new Coordinate(StartCord[0]+1,StartCord[1]+1),new Coordinate(EndCord[0]+1,EndCord[1]+1));
                else
                    computer_clipper.setLocation(new Coordinate(StartCord[0]+1,StartCord[1]+1),new Coordinate(EndCord[0]+1,EndCord[1]+1));
            }
            else if(k==3){
                if(player)
                    battleship.setLocation(new Coordinate(StartCord[0]+1,StartCord[1]+1),new Coordinate(EndCord[0]+1,EndCord[1]+1));
                else
                    computer_battleship.setLocation(new Coordinate(StartCord[0]+1,StartCord[1]+1),new Coordinate(EndCord[0]+1,EndCord[1]+1));
            }
            else if(k==4){
                if(player) {
                    aircraftCarrier.setLocation(new Coordinate(StartCord[0] + 1, StartCord[1] + 1), new Coordinate(EndCord[0] + 1, EndCord[1] + 1));
                    AllShipsPlaced = 1;
                }
                else
                    computer_aircraftCarrier.setLocation(new Coordinate(StartCord[0]+1,StartCord[1]+1),new Coordinate(EndCord[0]+1,EndCord[1]+1));

            }
        }
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

    public boolean checkRepeatFireArray(Coordinate coor, List<Coordinate> hit, List<Coordinate> miss) {
        for (Coordinate aHit : hit) {
            if (coor.getAcross() == aHit.getDown() && coor.getDown() == aHit.getAcross())
                return true;
        }
        for (Coordinate aMiss : miss) {
            if (coor.getAcross() == aMiss.getDown() && coor.getDown() == aMiss.getAcross())
                return true;
        }
        return false;
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

}
