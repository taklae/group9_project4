package edu.oregonstate.cs361.battleship;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by Will on 3/16/17.
 */
public class HardAI extends BattleshipModel {

    public void shootAtPlayer() {
        int max = 10;
        int min = 1;
        Random random = new Random(1);

        if (hitSearch == 1) {//follow new shoot search pattern
            if (direction == 1) {
                System.out.println("Firing up again");
                searchHit.setAcross(originalHit.getAcross() - inc);
                searchHit.setDown(originalHit.getDown());
                if (searchHit.getDown() < 1) {
                    searchHit.setDown(1);
                    direction += 1;
                    inc = 1;
                }
                playerShot(searchHit);
            } else if (direction == 2) {
                System.out.println("firing right again");
                searchHit.setDown(originalHit.getDown() + inc);
                searchHit.setAcross(originalHit.getAcross());
                if (searchHit.getAcross() > 10) {
                    searchHit.setAcross(10);
                    direction += 1;
                    inc = 1;
                }
                playerShot(searchHit);
            } else if (direction == 3) {
                System.out.println("firing down again");
                searchHit.setDown(originalHit.getDown());
                searchHit.setAcross(originalHit.getAcross() + inc);
                if (searchHit.getDown() > 10) {
                    searchHit.setDown(10);
                    direction += 1;
                    inc = 1;
                }
                playerShot(searchHit);
            } else if (direction == 4) {
                System.out.println("firing left again");
                searchHit.setDown(originalHit.getDown() - inc);
                searchHit.setAcross(originalHit.getAcross());
                if (searchHit.getAcross() < 1) {
                    searchHit.setAcross(1);
                    direction = 1;
                    hitSearch = 0;
                    inc = 0;
                }
                playerShot(searchHit);
            }
        } else {//fire randomly
            int randRow = random.nextInt(max - min + 1) + min;
            int randCol = random.nextInt(max - min + 1) + min;
            Coordinate coor = new Coordinate(randCol, randRow);

            while (checkRepeatFire(coor)) {
                randRow = random.nextInt(max - min + 1) + min;
                randCol = random.nextInt(max - min + 1) + min;
                coor.setAcross(randCol);
                coor.setDown(randRow);
            }
            playerShot(coor);
        }
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
                    if(hitSearch == 0) {
                        System.out.println("changing to targeted fire mode");
                        originalHit.setDown(coor.getDown());
                        originalHit.setAcross(coor.getAcross());
                        hitSearch = 1;
                    }
                    inc += 1;

                    playerHits.add(coor);
                    return;
                }
            }
        }

        playerMisses.add(coor);
        if(hitSearch == 1) {
            if (direction < 4) {
                direction += 1;
                inc = 1;
            }else{
                direction = 1;
                inc = 0;
                hitSearch = 0;
                System.out.println("ending targeted fire, returning to random");
            }
        }
    }
}
