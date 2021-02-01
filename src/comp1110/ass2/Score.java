package comp1110.ass2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// This class is made by Wei Ming Kuek.
public class Score {

    /**
     * @param placementSequence a String representing the sequence of piece placements made so far in the game
     * @param c The 'X' coordinate of the board
     * @param d The 'Y' coordinate of the board
     * @return The string of the tile that is currently occupying the coordinate specified in the parameter
     */
    public static String getTile(String placementSequence,int c, int d) {
        int sequenceCounter = -1;
        int nextTileInterval = 6;
        int rowPosition = 4;

        // This assigns the tiles to their location in an 2D array so example upon calling tilearray[2][1] will get the tile string at position (2,1)
        for (int i = rowPosition; i < placementSequence.length(); i += nextTileInterval) {
            sequenceCounter = sequenceCounter + 1;
            for (int x = 0; x < 8; x += 1) {
                if (Integer.valueOf(placementSequence.substring(i, i+1)).equals(x)) {
                    //for (int j = 5; j < placementSequence.length(); j += 6) {
                    for (int y = 0; y < 8; y += 1) {
                        if (Integer.valueOf(placementSequence.substring(i+1, i+2)).equals(y)) {
                            // Concat x and y to check against the user input
                            String matchInput = String.valueOf(x).concat(String.valueOf(y));

                            // Concat the user input to be check against x and y
                            String userInput = String.valueOf(c).concat(String.valueOf(d));

                            // Compare the two inputs and only true if they match
                            if (userInput.equals(matchInput)) {
                                return placementSequence.substring((nextTileInterval * sequenceCounter), rowPosition + (nextTileInterval * sequenceCounter));
                            }
                        }
                    }
                    //}
                }
            }
        }
        return null;
    }

    /**
     * @param currentLocation A string of the current tile location the train is currently at (e.g. "21")
     * @param placementSequence A String representing the sequence of piece placements made so far in the game
     * @param trainEnterSide A int that tell us which side the train is entering from (top: 0, right: 1, bottom: 2, left: 3)
     * @return The string of the tile that the train will travel next based on the current tile
     */
    public static String nextLocation(String currentLocation, String placementSequence, int trainEnterSide, int score) {
        String currTile = getTile(placementSequence, Integer.parseInt(String.valueOf(currentLocation.charAt(0))), Integer.parseInt(String.valueOf(currentLocation.charAt(1))));
        int tempScore = score;
        int exitSide = 0;
        int newtrainEnterSide = trainEnterSide;
        String newLocation = currentLocation;

        // If there is no tile leading out of the station, the station's score is 0
        if (currTile == null) {
            return "0";
        }

        if (trainEnterSide == 0 ) {
            if (currTile.charAt(0) == 'a') {
                exitSide = 2;
            } else if (currTile.charAt(0) == 'b') {
                exitSide = 1;
            } else if (currTile.charAt(0) == 'c') {
                exitSide = 3;
            } else if (currTile.charAt(0) == 'd') {
                exitSide = 0;
            }
        } else if (trainEnterSide == 1) {
            if (currTile.charAt(1) == 'a') {
                exitSide = 3;
            } else if (currTile.charAt(1) == 'b') {
                exitSide = 2;
            } else if (currTile.charAt(1) == 'c') {
                exitSide = 0;
            } else if (currTile.charAt(1) == 'd') {
                exitSide = 1;
            }
        } else if (trainEnterSide == 2) {
            if (currTile.charAt(2) == 'a') {
                exitSide = 0;
            } else if (currTile.charAt(2) == 'b') {
                exitSide = 3;
            } else if (currTile.charAt(2) == 'c') {
                exitSide = 1;
            } else if (currTile.charAt(2) == 'd') {
                exitSide = 2;
            }
        } else if (trainEnterSide == 3) {
            if (currTile.charAt(3) == 'a') {
                exitSide = 1;
            } else if (currTile.charAt(3) == 'b') {
                exitSide = 0;
            } else if (currTile.charAt(3) == 'c') {
                exitSide = 2;
            } else if (currTile.charAt(3) == 'd') {
                exitSide = 3;
            }
        }

        if (exitSide == 0) {
            newLocation = String.valueOf(Integer.parseInt(String.valueOf(currentLocation.charAt(0))) - 1).concat(String.valueOf(currentLocation.charAt(1)));
            newtrainEnterSide = 2;
        } else if (exitSide == 1) {
            newLocation = String.valueOf(currentLocation.charAt(0)).concat(String.valueOf(Integer.parseInt(String.valueOf(currentLocation.charAt(1))) + 1));
            newtrainEnterSide = 3;
        } else if (exitSide == 2) {
            newLocation = String.valueOf(Integer.parseInt(String.valueOf(currentLocation.charAt(0))) + 1).concat(String.valueOf(currentLocation.charAt(1)));
            newtrainEnterSide = 0;
        } else if (exitSide == 3) {
            newLocation = String.valueOf(currentLocation.charAt(0)).concat(String.valueOf(Integer.parseInt(String.valueOf(currentLocation.charAt(1))) - 1));
            newtrainEnterSide = 1;
        }

        // Checks for base case
        // 1. If newLocation consist of either -1 or 8 (The train has reached a station, thus return the finalised score)
        // 2. If newLocation is at the center stations (3,3) (3,4) (4,3) (4,4) (The trains has reached the centre station, thus double the score)
        // 3. If newLocation does not consist any tile (There is still new track that the train can go thus continuing calculating)
        if (newLocation.contains("-1") || newLocation.contains("8")) {
            return String.valueOf(tempScore);
        } else if (newLocation.contains("33") || newLocation.contains("34") || newLocation.contains("43") || newLocation.contains("44")) {
            // Finalise the points and double the score
            tempScore = tempScore * 2;
            return String.valueOf(tempScore);
        } else if (getTile(placementSequence, Integer.parseInt(String.valueOf(newLocation.charAt(0))), Integer.parseInt(String.valueOf(newLocation.charAt(1)))) == null) {
            return "0";
        } else {
            // Add 1 to score
            tempScore = tempScore + 1;
            // Continue recursing
            return nextLocation(newLocation, placementSequence, newtrainEnterSide, tempScore);
        }
    }

    /**
     * @param stationNumber Takes in a number from 1 - 32
     * @param placementSequence a String representing the sequence of piece placements made so far in the game
     * @return The total score for the train station specified in the parameter
     */
    public static int calStationScore(int stationNumber, String placementSequence) {
        int score = 0;

        if (stationNumber >= 1 && stationNumber <= 8) { // Start at top edge means entrance to the new tile is index '0'
            score = Integer.parseInt(nextLocation(String.valueOf(0).concat(String.valueOf(8-stationNumber)), placementSequence, 0, 1));
        } else if (stationNumber >= 9 && stationNumber <= 16) { // Start at left edge means entrance to the new tile is index '3'
            score = Integer.parseInt(nextLocation(String.valueOf(stationNumber - 9).concat(String.valueOf(0)), placementSequence, 3, 1));
        } else if (stationNumber >= 17 && stationNumber <= 24) { // Start at bottom edge means entrance to the new tile is index '2'
            score = Integer.parseInt(nextLocation(String.valueOf(7).concat(String.valueOf(stationNumber - 17)), placementSequence, 2, 1));
        } else if (stationNumber >= 25 && stationNumber <= 32) { // Start at right edge means entrance to the new tile is index '1'
            score = Integer.parseInt(nextLocation(String.valueOf(32 - stationNumber).concat(String.valueOf(7)), placementSequence, 1, 1));
        }
        return score;
    }
}
