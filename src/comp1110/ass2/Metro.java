package comp1110.ass2;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class Metro {
    /**
     * Task 2
     * Determine whether a piece placement is well-formed. For a piece
     * placement to be well-formed, it must:
     * - contain exactly six characters;
     * - have as its first, second, third and fourth characters letters between
     * 'a' and 'd' inclusive (tracks); and
     * - have as its fifth and sixth characters digits between 0 and 7 inclusive
     * (column and row respectively).
     *
     * @param piecePlacement A String representing the piece to be placed
     * @return True if this string is well-formed
     */
    //By Yen Kuo.
    public static boolean isPiecePlacementWellFormed(String piecePlacement) {
        // Following the tile's pattern.
        Pattern pattern = Pattern.compile("[abcd][abcd][abcd][abcd][0-7][0-7]");
        Matcher matcher = pattern.matcher(piecePlacement);
        return matcher.matches();
    }

    /**
     * Task 3
     * Determine whether a placement sequence string is well-formed.
     * For a placement sequence to be well-formed, it must satisfy the
     * following criteria:
     * - It must be composed of well-formed tile placements.
     * - For any piece x, there can exist no more instances of x on the board
     * than instances of x in the deck.
     *
     * @param placement A String representing the placement of all tiles on the
     *                  board
     * @return true if this placement sequence is well-formed
     */
    //By Yen Kuo.
    public static boolean isPlacementSequenceWellFormed(String placement) {
        //The total count of tiles will be 60, so the maximum length of the "placement" will be 360.
        //Each type of tiles can't reach the specific amount.
        //"aacb", "cbaa", "acba", "baac", "aaaa" have 4 copies.
        //"cbcb", "bcbc" have 3 copies.
        //"cccc", "bbbb", "dddd", "dacc", "cdac", "ccda", "accd", "dbba", "adbb", "badb", "bbad", "ddbc", "cddb", "bcdd", "dbcd", "adad", "dada" have 2 copies.
        //Return true if the length doesn't excess the maximum requirement, its substring be well-formed and the specific tiles doesn't reach their required amount.
        //Else return false.
        // placement can't exceed 360.
        if (placement.length() <= 360) {
            // LengthOfTilePlusLocation is 6.
            for (int x = 0; x + Tile.LengthOfTilePlusLocation <= placement.length(); x += Tile.LengthOfTilePlusLocation) {
                if (isPiecePlacementWellFormed(placement.substring(x, x + Tile.LengthOfTilePlusLocation))) {
                    if (Tile.tilesOnBoard(placement, "aacb") <= 4 && Tile.tilesOnBoard(placement, "cbaa") <= 4 && Tile.tilesOnBoard(placement, "acba") <= 4 && Tile.tilesOnBoard(placement, "baac") <= 4 && Tile.tilesOnBoard(placement, "aaaa") <= 4) {
                        if (Tile.tilesOnBoard(placement, "cbcb") <= 3 && Tile.tilesOnBoard(placement, "bcbc") <= 3) {
                            if (Tile.tilesOnBoard(placement, "cccc") <= 2 && Tile.tilesOnBoard(placement, "bbbb") <= 2 && Tile.tilesOnBoard(placement, "dddd") <= 2 && Tile.tilesOnBoard(placement, "dacc") <= 2 && Tile.tilesOnBoard(placement, "cdac") <= 2 && Tile.tilesOnBoard(placement, "ccda") <= 2 && Tile.tilesOnBoard(placement, "accd") <= 2 && Tile.tilesOnBoard(placement, "dbba") <= 2 && Tile.tilesOnBoard(placement, "adbb") <= 2) {
                                if (Tile.tilesOnBoard(placement, "badb") <= 2 && Tile.tilesOnBoard(placement, "bbad") <= 2 && Tile.tilesOnBoard(placement, "ddbc") <= 2 && Tile.tilesOnBoard(placement, "cddb") <= 2 && Tile.tilesOnBoard(placement, "bcdd") <= 2 && Tile.tilesOnBoard(placement, "dbcd") <= 2 && Tile.tilesOnBoard(placement, "adad") <= 2 && Tile.tilesOnBoard(placement, "dada") <= 2) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        // empty placement is allowed.
        return placement.length() == 0;
    }


    /**
     * Task 5
     * Draw a random tile from the deck.
     *
     * @param placementSequence a String representing the sequence of tiles
     *                          that have already been played
     * @param totalHands        a String representing all tiles (if any) in
     *                          all players' hands
     * @return a random tile from the deck
     */
    //By Yen Kuo.
    public static String drawFromDeck(String placementSequence, String totalHands) {
        //Create a new ArrayList and add all type of tiles into the list.
        List<String> list = new ArrayList<>(Arrays.asList("cbaa", "aacb", "acba", "baac", "aaaa", "cbcb", "bcbc", "bbbb",
                "cccc", "dddd", "dacc", "cdac", "ccda", "accd", "dbba", "adbb", "badb", "bbad", "ddbc", "cddb", "bcdd", "dbcd", "adad", "dada"));

        //combine the total count of specific tile from placementSequence and totalHands
        //Remove the tile from the list which have reach the maximum count. E.g "aacb" tile can only have 4 in the game, so if the total amount of "aacb" tile reach 4.
        //It will be remove from the list and can't drawn from the deck anymore.
        if (Tile.tilesOnBoard(placementSequence, "aacb") + Tile.tilesInHand(totalHands, "aacb") == 4) {
            list.remove("aacb");
        }
        if (Tile.tilesOnBoard(placementSequence, "cbaa") + Tile.tilesInHand(totalHands, "cbaa") == 4) {
            list.remove("cbaa");
        }
        if (Tile.tilesOnBoard(placementSequence, "acba") + Tile.tilesInHand(totalHands, "acba") == 4) {
            list.remove("acba");
        }
        if (Tile.tilesOnBoard(placementSequence, "baac") + Tile.tilesInHand(totalHands, "baac") == 4) {
            list.remove("baac");
        }
        if (Tile.tilesOnBoard(placementSequence, "aaaa") + Tile.tilesInHand(totalHands, "aaaa") == 4) {
            list.remove("aaaa");
        }
        if (Tile.tilesOnBoard(placementSequence, "cbcb") + Tile.tilesInHand(totalHands, "cbcb") == 3) {
            list.remove("cbcb");
        }
        if (Tile.tilesOnBoard(placementSequence, "bcbc") + Tile.tilesInHand(totalHands, "bcbc") == 3) {
            list.remove("bcbc");
        }
        if (Tile.tilesOnBoard(placementSequence, "cccc") + Tile.tilesInHand(totalHands, "cccc") == 2) {
            list.remove("cccc");
        }
        if (Tile.tilesOnBoard(placementSequence, "bbbb") + Tile.tilesInHand(totalHands, "bbbb") == 2) {
            list.remove("bbbb");
        }
        if (Tile.tilesOnBoard(placementSequence, "dddd") + Tile.tilesInHand(totalHands, "dddd") == 2) {
            list.remove("dddd");
        }
        if (Tile.tilesOnBoard(placementSequence, "dacc") + Tile.tilesInHand(totalHands, "dacc") == 2) {
            list.remove("dacc");
        }
        if (Tile.tilesOnBoard(placementSequence, "cdac") + Tile.tilesInHand(totalHands, "cdac") == 2) {
            list.remove("cdac");
        }
        if (Tile.tilesOnBoard(placementSequence, "ccda") + Tile.tilesInHand(totalHands, "ccda") == 2) {
            list.remove("ccda");
        }
        if (Tile.tilesOnBoard(placementSequence, "accd") + Tile.tilesInHand(totalHands, "accd") == 2) {
            list.remove("accd");
        }
        if (Tile.tilesOnBoard(placementSequence, "dbba") + Tile.tilesInHand(totalHands, "dbba") == 2) {
            list.remove("dbba");
        }
        if (Tile.tilesOnBoard(placementSequence, "adbb") + Tile.tilesInHand(totalHands, "adbb") == 2) {
            list.remove("adbb");
        }
        if (Tile.tilesOnBoard(placementSequence, "badb") + Tile.tilesInHand(totalHands, "badb") == 2) {
            list.remove("badb");
        }
        if (Tile.tilesOnBoard(placementSequence, "bbad") + Tile.tilesInHand(totalHands, "bbad") == 2) {
            list.remove("bbad");
        }
        if (Tile.tilesOnBoard(placementSequence, "ddbc") + Tile.tilesInHand(totalHands, "ddbc") == 2) {
            list.remove("ddbc");
        }
        if (Tile.tilesOnBoard(placementSequence, "cddb") + Tile.tilesInHand(totalHands, "cddb") == 2) {
            list.remove("cddb");
        }
        if (Tile.tilesOnBoard(placementSequence, "bcdd") + Tile.tilesInHand(totalHands, "bcdd") == 2) {
            list.remove("bcdd");
        }
        if (Tile.tilesOnBoard(placementSequence, "dbcd") + Tile.tilesInHand(totalHands, "dbcd") == 2) {
            list.remove("dbcd");
        }
        if (Tile.tilesOnBoard(placementSequence, "adad") + Tile.tilesInHand(totalHands, "adad") == 2) {
            list.remove("adad");
        }
        if (Tile.tilesOnBoard(placementSequence, "dada") + Tile.tilesInHand(totalHands, "dada") == 2) {
            list.remove("dada");
        }

        //return the random tile still in the deck(neither on the board nor the player's hand).
        int index = (int) (Math.random() * list.size());
        return list.get(index);
    }


    /**
     * Task 6
     * Determine if a given placement sequence follows the rules of the game.
     * These rules are:
     * - No tile can overlap another tile, or any of the central stations.
     * - A tile cannot be placed next to one of the central squares unless it
     * continues or completes an existing track.
     * - If a tile is on an edge of the board, it cannot contain a track that
     * results in a station looping back to itself, UNLESS that tile could not
     * have been placed elsewhere.
     * - If a tile is on a corner of the board, it cannot contain a track that
     * links the two stations adjacent to that corner, UNLESS that tile could
     * not have been placed elsewhere.
     *
     * @param placementSequence A sequence of placements on the board.
     * @return Whether this placement string is valid.
     */
    //By Yen Kuo.
    public static boolean isPlacementSequenceValid(String placementSequence) {
        // FIXME Task 6: determine whether a placement sequence is valid
        // Empty Board is valid
        if (placementSequence.length() == 0) {
            return true;
        }
        //If tile isn't on the middle station location.
        if(Location.validCoordinates(placementSequence) == true) {
            //If tile isn't on the same location.
            if (Location.overlap(placementSequence) == true) {
                //If tile is in the correct order.
                if (Location.location(placementSequence) == true) {
                    //If tile didn't against the rule(e.g. loop back itself).
                    if (Location.loop(placementSequence) == true) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Task 7
     * Determine the current score for the game.
     *
     * @param placementSequence a String representing the sequence of piece placements made so far in the game
     * @param numberOfPlayers   The number of players in the game
     * @return an array containing the scores for all players
     */
    //By Wei Ming Kuek.
    public static int[] getScore(String placementSequence, int numberOfPlayers) {
        // FIXME Task 7: determine the current score for the game
        int[] score = new int[6];
        // Calculation method for different number of players
        if (numberOfPlayers == 2) {
            int[] player1Stations = {1, 3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23, 25, 27, 29, 31};
            int[] player2Stations = {2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32};
            int player1Score = 0;
            int player2Score = 0;

            for (int player1Station : player1Stations) {
                player1Score = player1Score + Score.calStationScore(player1Station, placementSequence);
            }
            for (int player2Station : player2Stations) {
                player2Score = player2Score + Score.calStationScore(player2Station, placementSequence);
            }
            return new int[]{player1Score, player2Score};
        } else if (numberOfPlayers == 3) {
            int[] player1Stations = {1, 4, 6, 11, 15, 20, 23, 25, 28, 31};
            int[] player2Stations = {2, 7, 9, 12, 14, 19, 22, 27, 29, 32};
            int[] player3Stations = {3, 5, 8, 10, 13, 18, 21, 24, 26, 30};
            int player1Score = 0;
            int player2Score = 0;
            int player3Score = 0;

            for (int player1Station : player1Stations) {
                player1Score = player1Score + Score.calStationScore(player1Station, placementSequence);
            }
            for (int player2Station : player2Stations) {
                player2Score = player2Score + Score.calStationScore(player2Station, placementSequence);
            }
            for (int player3Station : player3Stations) {
                player3Score = player3Score + Score.calStationScore(player3Station, placementSequence);
            }
            return new int[]{player1Score, player2Score, player3Score};
        } else if (numberOfPlayers == 4) {
            int[] player1Stations = {4, 7, 11, 16, 20, 23, 27, 32};
            int[] player2Stations = {3, 8, 12, 15, 19, 24, 28, 31};
            int[] player3Stations = {1, 6, 10, 13, 18, 21, 25, 30};
            int[] player4Stations = {2, 5, 9, 14, 17, 22, 26, 29};
            int player1Score = 0;
            int player2Score = 0;
            int player3Score = 0;
            int player4Score = 0;

            for (int player1Station : player1Stations) {
                player1Score = player1Score + Score.calStationScore(player1Station, placementSequence);
            }
            for (int player2Station : player2Stations) {
                player2Score = player2Score + Score.calStationScore(player2Station, placementSequence);
            }
            for (int player3Station : player3Stations) {
                player3Score = player3Score + Score.calStationScore(player3Station, placementSequence);
            }
            for (int player4Station : player4Stations) {
                player4Score = player4Score + Score.calStationScore(player4Station, placementSequence);
            }
            return new int[]{player1Score, player2Score, player3Score, player4Score};
        } else if (numberOfPlayers == 5) {
            int[] player1Stations = {1, 5, 10, 14, 22, 28};
            int[] player2Stations = {6, 12, 18, 23, 27, 32};
            int[] player3Stations = {3, 7, 15, 19, 25, 29};
            int[] player4Stations = {2, 9, 13, 21, 26, 30};
            int[] player5Stations = {4, 8, 11, 20, 24, 31};
            int player1Score = 0;
            int player2Score = 0;
            int player3Score = 0;
            int player4Score = 0;
            int player5Score = 0;

            for (int player1Station : player1Stations) {
                player1Score = player1Score + Score.calStationScore(player1Station, placementSequence);
            }
            for (int player2Station : player2Stations) {
                player2Score = player2Score + Score.calStationScore(player2Station, placementSequence);
            }
            for (int player3Station : player3Stations) {
                player3Score = player3Score + Score.calStationScore(player3Station, placementSequence);
            }
            for (int player4Station : player4Stations) {
                player4Score = player4Score + Score.calStationScore(player4Station, placementSequence);
            }
            for (int player5Station : player5Stations) {
                player5Score = player5Score + Score.calStationScore(player5Station, placementSequence);
            }
            return new int[]{player1Score, player2Score, player3Score, player4Score, player5Score};
        } else if (numberOfPlayers == 6) {
            int[] player1Stations = {1, 5, 10, 19, 27};
            int[] player2Stations = {2, 11, 18, 25, 29};
            int[] player3Stations = {4, 8, 14, 21, 26};
            int[] player4Stations = {6, 15, 20, 24, 31};
            int[] player5Stations = {3, 9, 13, 23, 30};
            int[] player6Stations = {7, 12, 22, 28, 32};
            int player1Score = 0;
            int player2Score = 0;
            int player3Score = 0;
            int player4Score = 0;
            int player5Score = 0;
            int player6Score = 0;

            for (int player1Station : player1Stations) {
                player1Score = player1Score + Score.calStationScore(player1Station, placementSequence);
            }
            for (int player2Station : player2Stations) {
                player2Score = player2Score + Score.calStationScore(player2Station, placementSequence);
            }
            for (int player3Station : player3Stations) {
                player3Score = player3Score + Score.calStationScore(player3Station, placementSequence);
            }
            for (int player4Station : player4Stations) {
                player4Score = player4Score + Score.calStationScore(player4Station, placementSequence);
            }
            for (int player5Station : player5Stations) {
                player5Score = player5Score + Score.calStationScore(player5Station, placementSequence);
            }
            for (int player6Station : player6Stations) {
                player6Score = player6Score + Score.calStationScore(player6Station, placementSequence);
            }
            return new int[]{player1Score, player2Score, player3Score, player4Score, player5Score, player6Score};
        } else {
            return score;
        }
    }

    public static int nextPlayer(int currentPlayer, int numberOfPlayers){
        if (numberOfPlayers<2 || numberOfPlayers>6){
            return currentPlayer+1;
        }
        if (currentPlayer==numberOfPlayers) {
            return 1;
        }
        else{
            return currentPlayer+1;
        }
    }

    /**
     * Task 9
     * Given a placement sequence string, generate a valid next move.
     *
     * @param placementSequence a String representing the sequence of piece placements made so far in the game
     * @param piece             a four-character String representing the tile to be placed
     * @param numberOfPlayers   The number of players in the game
     * @return A valid placement of the given tile
     */
    //By Yen Kuo.
    public static String generateMove(String placementSequence, String piece, int numberOfPlayers) {
        // FIXME Task 9: generate a valid move
        //Create a list(Valid) and add all the location on the edge.
        List<String> Valid = new ArrayList<>(Location.EdgeLoc);
        List<String> exist = new ArrayList<>();
        for (int i = 4; i < placementSequence.length(); i += Tile.LengthOfTilePlusLocation) {
            exist.add(placementSequence.substring(i, i + 2));
        }
        for(int i = 0; i < exist.size(); i++){
            if (exist.get(i).charAt(0) != '0' && exist.get(i).charAt(1) == '0') {
                // The location on the right side of left edge.
                char a = exist.get(i).charAt(0);
                char b = exist.get(i).charAt(1);
                int c = Character.getNumericValue(b) + 1;
                String loc = a + Integer.toString(c);
                // Check did the location already been placed. If not, add it to Valid.
                if (!exist.contains(loc) && Location.checkLoc(loc) && c >= 0 && c < 8) {
                    Valid.add(loc);
                }
            }
            if (exist.get(i).charAt(0) == '0' && exist.get(i).charAt(1) != '0') {
                // The location on the down side of top edge.
                char a = exist.get(i).charAt(0);
                char b = exist.get(i).charAt(1);
                int c = Character.getNumericValue(a) + 1;
                String loc = c + Character.toString(b);
                // Check did the location already been placed. If not, add it to Valid.
                if (!exist.contains(loc) && Location.checkLoc(loc) && c >= 0 && c < 8) {
                    Valid.add(loc);
                }
            }
            if (exist.get(i).charAt(0) != '0' && exist.get(i).charAt(1) == '7') {
                // The location on the left side of right edge.
                char a = exist.get(i).charAt(0);
                char b = exist.get(i).charAt(1);
                int c = Character.getNumericValue(b) - 1;
                String loc = a + Integer.toString(c);
                // Check did the location already been placed. If not, add it to Valid.
                if (!exist.contains(loc) && Location.checkLoc(loc) && c >= 0 && c < 8) {
                    Valid.add(loc);
                }
            }
            if (exist.get(i).charAt(0) == '7' && exist.get(i).charAt(1) != '0') {
                // The location on the up side of bottom edge.
                char a = exist.get(i).charAt(0);
                char b = exist.get(i).charAt(1);
                int c = Character.getNumericValue(a) - 1;
                String loc = c + Character.toString(b);
                // Check did the location already been placed. If not, add it to Valid.
                if (!exist.contains(loc) && Location.checkLoc(loc) && c >= 0 && c < 8) {
                    Valid.add(loc);
                }
            }
            if (exist.get(i).charAt(0) != '0' && exist.get(i).charAt(1) != '7' && exist.get(i).charAt(0) != '0' && exist.get(i).charAt(1) != '7') {
                // The location not on the edge, check it's four side.
                char a = exist.get(i).charAt(0);
                char b = exist.get(i).charAt(1);
                int c = Character.getNumericValue(b) - 1;
                int d = Character.getNumericValue(b) + 1;
                int e = Character.getNumericValue(a) - 1;
                int f = Character.getNumericValue(a) + 1;

                String loc = Character.toString(a) + Integer.toString(c);
                String loc1 = a + Integer.toString(d);
                String loc2 = e + Character.toString(b);
                String loc3 = f + Character.toString(b);
                // Check did the location already been placed. If not, add it to Valid.
                if (!exist.contains(loc) && Location.checkLoc(loc) && c >= 0 && c < 8) {
                    Valid.add(loc);
                }
                if (!exist.contains(loc1) && Location.checkLoc(loc1) && d >= 0 && d < 8) {
                    Valid.add(loc1);
                }
                if (!exist.contains(loc2) && Location.checkLoc(loc2) && e >= 0 && e < 8) {
                    Valid.add(loc2);
                }
                if (!exist.contains(loc3) && Location.checkLoc(loc3) && f >= 0 && f < 8) {
                    Valid.add(loc3);
                }
            }
        }
        Valid = Valid.stream().distinct().collect(Collectors.toList());

        //Remove the locations from Valid which already exists.
        for (int i = 4; i < placementSequence.length(); i += Tile.LengthOfTilePlusLocation) {
            Valid.remove(placementSequence.substring(i, i + 2));
        }
        String First = piece.substring(0, 1);
        String Second = piece.substring(1, 2);
        String Third = piece.substring(2, 3);
        String Fourth = piece.substring(3, 4);
        //If the first char of the tiles was d, remove the invalid spot from the list(Valid).
        if (piece.contains("b") || piece.contains("c") || piece.contains("d")) {
            List<String> temp = new ArrayList<>(Valid);
            if (First.equals("d")) {
                Valid.removeAll(Location.Top);
            }
            //If the second char of the tiles was d, remove the invalid spot from the list(Valid).
            if (Second.equals("d")) {
                Valid.removeAll(Location.Right);
            }
            //If the third char of the tiles was d, remove the invalid spot from the list(Valid).
            if (Third.equals("d")) {
                Valid.removeAll(Location.Bottom);
            }
            //If the fourth char of the tiles was d, remove the invalid spot from the list(Valid).
            if (Fourth.equals("d")) {
                Valid.removeAll(Location.Left);
            }
            //If the first char of the tiles was c or the fourth char of the tiles was b, remove the invalid spot from the list(Valid).
            if (First.equals("c") || Fourth.equals("b")) {
                Valid.remove("00");
            }
            //If the second char of the tiles was c or the first char of the tiles was b, remove the invalid spot from the list(Valid).
            if (Second.equals("c") || First.equals("b")) {
                Valid.remove("07");
            }
            //If the fourth char of the tiles was c or the third char of the tiles was b, remove the invalid spot from the list(Valid).
            if (Fourth.equals("c") || Third.equals("b")) {
                Valid.remove("70");
            }
            if (Third.equals("c") || Second.equals("b")) {
                Valid.remove("77");
            }
        }


        Random r = new Random();
        //If the size of the array list(Valid) is zero. Means the situation for the specific tiles unavoidable to put in the invalid location.
        if (Valid.size() == 0) {
            //Create a list and add all the existing locations.
            List<String> exist1 = new ArrayList<>();
            for (int i = 4; i < placementSequence.length(); i += Tile.LengthOfTilePlusLocation) {
                exist1.add(placementSequence.substring(i, i + 2));
            }
            //Create a new list which contains all location on the board.
            List<String> All = new ArrayList<>(Arrays.asList("00", "01", "02", "03", "04", "05", "06", "07",
                                                            "10", "11", "12", "13", "14", "15", "16", "17",
                                                            "20", "21", "22", "23", "24", "25", "26", "27",
                                                            "30", "31", "32", "35", "36", "37",
                                                            "40", "41", "42", "45", "46", "47",
                                                            "50", "51", "52", "53", "54", "55", "56", "57",
                                                            "60", "61", "62", "63", "64", "65", "66", "67",
                                                            "70", "71", "72", "73", "74", "75", "76", "77"));
            //Remove all existing locations from list(All). The rest of the locations will be the valid locations.
            All.removeAll(exist);
            int index = r.nextInt(All.size());
            //Randomly return the location from list(All).
            return piece + All.get(index);
        } else {

            int index = r.nextInt(Valid.size());
            //Randomly return the location from list(Valid).

            return piece + Valid.get(index);
        }
    }

}


