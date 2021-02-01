package comp1110.ass2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * (0,0)(0,1)(0,2)(0,3)(0,4)(0,5)(0,6)(0,7)
 * (1,0)(1,1)(1,2)(1,3)(1,4)(1,5)(1,6)(1,7)
 * (2,0)(2,1)(2,2)(2,3)(2,4)(2,5)(2,6)(2,7)
 * (3,0)(3,1)(3,2)--+----+--(3,5)(3,6)(3,7)
 * (4,0)(4,1)(4,2)--+----+--(4,5)(4,6)(4,7)
 * (5,0)(5,1)(5,2)(5,3)(5,4)(5,5)(5,6)(5,7)
 * (6,0)(6,1)(6,2)(6,3)(6,4)(6,5)(6,6)(6,7)
 * (7,0)(7,1)(7,2)(7,3)(7,4)(7,5)(7,6)(7,7)
 */

// This class are made by Yen Kuo.
public class Location {

    public static ArrayList<String> EdgeLoc = new ArrayList<>(Arrays.asList("00", "01", "02", "03", "04", "05", "06", "07", "10", "20", "30", "40", "50", "60", "70", "71", "72", "73", "74", "75", "76", "77", "17", "27", "37", "47", "57", "67"));
    public static ArrayList<String> Top = new ArrayList<>(Arrays.asList("01", "02", "03", "04", "05", "06", "00", "07"));
    public static ArrayList<String> Right = new ArrayList<>(Arrays.asList("17", "27", "37", "47", "57", "67", "77", "07"));
    public static ArrayList<String> Left = new ArrayList<>(Arrays.asList("10", "20", "30", "40", "50", "60", "00", "70"));
    public static ArrayList<String> Bottom = new ArrayList<>(Arrays.asList("71", "72", "73", "74", "75", "76", "70", "77"));


    public static boolean valid(int x, int y) {
        if ((x == 3 && y == 3)|| (x == 3 && y == 4) || (x == 4 && y == 3) ||
                (x == 4 && y == 4)) {
            return false;
        }
        return x <= 8 && y <= 8 && x >= 0 && y >= 0;
    }


    /**
     * @param p A sequence of placements on the board.
     * @return true if the tiles' location isn't at the location of middle station.(3,3)(3,4)(4,3)(4,4)
     */
    public static boolean validCoordinates(String p) {
        //Check the tile location whether did it overlap the location of middle station or not.
        for (int i = 4; i < p.length(); i += Tile.LengthOfTilePlusLocation) {
            String TileLocation = p.substring(i, i + 2);
            if (TileLocation.equals("33") || TileLocation.equals("34") || TileLocation.equals("43") || TileLocation.equals("44")) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param p A sequence of placements on the board.
     * @return true if two tiles didn't place on the same location.
     */

    public static boolean overlap(String p) {
        //Check whether did two same location appear in the one string. If is, it's invalid so return false.
        for (int x = 4; x < p.length(); x += Tile.LengthOfTilePlusLocation) {
            for (int y = x + Tile.LengthOfTilePlusLocation; y < p.length(); y += Tile.LengthOfTilePlusLocation) {
                if (p.substring(x, x + 2).equals(p.substring(y, y + 2))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * @param p A sequence of placements on the board.
     * @return true if the tiles have placed in the correct order
     */

    public static boolean location(String p) {

        ArrayList<String> Loc1 = new ArrayList<>(Location.EdgeLoc);
        ArrayList<String> Appeared = new ArrayList<>();
        //Create another array list(tileListLoc) which contains all the location from string p.
        ArrayList<String> tileListLoc = new ArrayList<>();
        for (int i = 4; i < p.length(); i += Tile.LengthOfTilePlusLocation) {
            tileListLoc.add(p.substring(i, i + 2));
        }
        //Integer count represent the number of times that the array list(loc1) contains the location from array list(tileListLoc), and it start from 0.
        int count = 0;

        //Check every elements in tileListLoc, whether did Loc1 contain it or not. If Loc1 contains the element, then count will plus one.
        for (int i = 0; i < tileListLoc.size(); i++ ){
            String s = tileListLoc.get(i);
            if (Loc1.contains(s)) {
                count = count + 1;
            }
            //After placing each tile, the new location will valid and add it to the array list(Loc1).
            //x coordinate for Loc.
            char x = s.charAt(0);
            //y coordinate for Loc.
            char y = s.charAt(1);

            //For example, if the first tile on the board locate at (5,0), (5,1) will be the valid location and we will need to add it to the array list(Loc1).
            if (x != '0' && y == '0') {
                int c = Character.getNumericValue(y) + 1;
                String loc = x + Integer.toString(c);
                if(!Appeared.contains(s) && c >=0 && c<8) {
                    Loc1.add(loc);
                }
            }
            //For example, if the first tile on the board locate at (0,5), (1,5) will be the valid location and we will need to add it to the array list(Loc1).
            if (x == '0' && y != '0') {
                int c = Character.getNumericValue(x) + 1;
                String loc = c + Character.toString(y);
                if(!Appeared.contains(s) && c >=0 && c<8) {
                    Loc1.add(loc);
                }
            }
            //For example, if the first tile on the board locate at (5,7), (5,6) will be the valid location and we will need to add it to the array list(Loc1).
            if (x != '0' && y == '7') {
                int c = Character.getNumericValue(y) - 1;
                String loc = x + Integer.toString(c);
                if(!Appeared.contains(s) && c >=0 && c<8) {
                    Loc1.add(loc);
                }
            }
            //For example, if the first tile on the board locate at (7,5), (6,5) will be the valid location and we will need to add it to the array list(Loc1).
            if (x == '7' && y != '0') {
                int c = Character.getNumericValue(x) - 1;
                String loc = c + Character.toString(y);
                if(!Appeared.contains(s) && c >=0 && c<8) {
                    Loc1.add(loc);
                }
            }
            //For example, if the first tile on the board locate at (5,6), then (5,5), (6,6), (5,7) and (4,6) will be the valid location and we will need to add it to the array list(Loc1).
            if (x != '0' && x != '7' && y != '0' && y != '7') {
                int c = Character.getNumericValue(y) - 1;
                int d = Character.getNumericValue(y) + 1;
                int e = Character.getNumericValue(x) - 1;
                int f = Character.getNumericValue(x) + 1;
                String loc = x + Integer.toString(c);
                String loc1 = x + Integer.toString(d);
                String loc2 = e + Character.toString(y);
                String loc3 = f + Character.toString(y);
                if(!Appeared.contains(s) && c >=0 && c<8) {
                    Loc1.add(loc);
                }
                if(!Appeared.contains(s) && d >=0 && d<8) {
                    Loc1.add(loc1);
                }
                if(!Appeared.contains(s) && e >=0 && e<8) {
                    Loc1.add(loc2);
                }
                if(!Appeared.contains(s) && f >=0 && f<8) {
                    Loc1.add(loc3);
                }
            }
            Loc1.remove(s);
            Appeared.add(s);
        }
        //Return whether the count equal to the array list's(tileListLoc) size.
        //Expect equal and being true if all the tiles following the correct ordering.
        return count == tileListLoc.size();
    }

    /**
     * @param p A sequence of placements on the board.
     * @return true if the tack isn't length of one or not looping back itself, unless it's unavoidable.
     */

    public static boolean loop(String p) {
        //Create a new list(Valid) that contains all of the valid location.
        List<String> Valid = new ArrayList<>(Location.EdgeLoc);
        //Create a new list(Appeared) that contains all of the location that already been placed.
        List<String> Appeared = new ArrayList<>();

        //Create a new list(tileListLoc) that contains all of the tile from string p.
        List<String> tileListLoc = new ArrayList<>();
        for (int i = 0; i < p.length(); i += Tile.LengthOfTilePlusLocation) {
            tileListLoc.add(p.substring(i, i + Tile.LengthOfTilePlusLocation));
        }

        //Check every tiles from list(tileListLoc).
        for (String s : tileListLoc) {

            //If the first char of the tiles was d, remove the invalid spot from the list(Valid).
            if (s.contains("b") || s.contains("c") || s.contains("d")) {
                // The arraylist just for storing the valid location before removeing the invalid spot.
                List<String> temp = new ArrayList<>(Valid);
                if (s.startsWith("d")) {
                    Valid.removeAll(Location.Top);
                }
                //If the second char of the tiles was d, remove the invalid spot from the list(Valid).
                if (s.startsWith("d", 1)) {
                    Valid.removeAll(Location.Right);
                }
                //If the third char of the tiles was d, remove the invalid spot from the list(Valid).
                if (s.startsWith("d", 2)) {
                    Valid.removeAll(Location.Bottom);
                }
                //If the fourth char of the tiles was d, remove the invalid spot from the list(Valid).
                if (s.startsWith("d", 3)) {
                    Valid.removeAll(Location.Left);
                }
                //If the first char of the tiles was c or the fourth char of the tiles was b, remove the invalid spot from the list(Valid).
                if (s.startsWith("c") || s.startsWith("b", 3)) {
                    Valid.remove("00");
                }
                //If the second char of the tiles was c or the first char of the tiles was b, remove the invalid spot from the list(Valid).
                if (s.startsWith("c", 1) || s.startsWith("b")) {
                    Valid.remove("07");
                }
                //If the fourth char of the tiles was c or the third char of the tiles was b, remove the invalid spot from the list(Valid).
                if (s.startsWith("c", 3) || s.startsWith("b", 2)) {
                    Valid.remove("70");
                }
                //If the third char of the tiles was c or the second char of the tiles was b, remove the invalid spot from the list(Valid).
                if (s.startsWith("c", 2) || s.startsWith("b", 1)) {
                    Valid.remove("77");
                }
                //If we meet any situation that contains the tack is length of one or looping back itself, we will need to check if it is unavoidable or not.
                //Unavoidable means that the length of the list(Valid) must be 0, or it means that still have some valid spot for it.
                //Therefore, return false if the length of the list(Valid) isn't zero. Otherwise return true.
                if (s.startsWith("d") && s.startsWith("0", 4) || s.startsWith("d", 1) && s.startsWith("7", 5) ||
                        s.startsWith("d", 2) && s.startsWith("7", 4) || s.startsWith("d", 3) && s.startsWith("0", 5) ||
                        s.startsWith("b", 3) && s.startsWith("00", 4) || s.startsWith("b", 1) && s.startsWith("77", 4) ||
                        s.startsWith("b", 2) && s.startsWith("70", 4) || s.startsWith("b") && s.startsWith("07", 4) ||
                        s.startsWith("c", 3) && s.startsWith("70", 4) || s.startsWith("c", 1) && s.startsWith("07", 4) ||
                        s.startsWith("c", 2) && s.startsWith("77", 4) || s.startsWith("c") && s.startsWith("00", 4)) {
                    if (Valid.size() > 0) {
                        return false;
                    }
                }
                // If didn't return false. Make valid back to the one which didn't remove any location.
                Valid = temp;
            }
            // Remove the location from Valid which just placed.
            Valid.remove(s.substring(4, 6));
            // Add the location to Appeared which just placed.
            Appeared.add(s.substring(4, 6));

            //Add the new valid locations to the list(Valid).
            if (s.charAt(4) != '0' && s.charAt(5) == '0') {
                // The location on the right side of left edge.
                char a = s.charAt(4);
                char b = s.charAt(5);
                int c = Character.getNumericValue(b) + 1;
                String loc = a + Integer.toString(c);
                // Check did the location already been placed. If not, add it to Valid.
                if (!Appeared.contains(loc) && checkLoc(loc) && c >= 0 && c <= 7) {
                    Valid.add(loc);
                }
            }
            if (s.charAt(4) == '0' && s.charAt(5) != '0') {
                // The location on the down side of top edge.
                char a = s.charAt(4);
                char b = s.charAt(5);
                int c = Character.getNumericValue(a) + 1;
                String loc = c + Character.toString(b);
                // Check did the location already been placed. If not, add it to Valid.
                if (!Appeared.contains(loc) && checkLoc(loc) && c >= 0 && c <= 7) {
                    Valid.add(loc);
                }
            }
            if (s.charAt(4) != '0' && s.charAt(5) == '7' ) {
                // The location on the left side of right edge.
                char a = s.charAt(4);
                char b = s.charAt(5);
                int c = Character.getNumericValue(b) - 1;
                String loc = a + Integer.toString(c);
                // Check did the location already been placed. If not, add it to Valid.
                if (!Appeared.contains(loc) && checkLoc(loc) && c >= 0 && c <= 7) {
                    Valid.add(loc);
                }
            }
            if (s.charAt(4) == '7' && s.charAt(5) != '0') {
                // The location on the up side of bottom edge.
                char a = s.charAt(4);
                char b = s.charAt(5);
                int c = Character.getNumericValue(a) - 1;
                String loc = c + Character.toString(b);
                // Check did the location already been placed. If not, add it to Valid.
                if (!Appeared.contains(loc) && checkLoc(loc) &&  c >= 0 && c <= 7) {
                    Valid.add(loc);
                }
            }
            if (s.charAt(4) != '0' && s.charAt(4) != '7' && s.charAt(5) != '0' && s.charAt(5) != '7') {
                // The location not on the edge, check it's four side.
                char a = s.charAt(4);
                char b = s.charAt(5);
                int c = Character.getNumericValue(b) - 1;
                int d = Character.getNumericValue(b) + 1;
                int e = Character.getNumericValue(a) - 1;
                int f = Character.getNumericValue(a) + 1;
                String loc = a + Integer.toString(c);
                String loc1 = a + Integer.toString(d);
                String loc2 = e + Character.toString(b);
                String loc3 = f + Character.toString(b);
                // Check did the location already been placed. If not, add it to Valid.
                if (!Appeared.contains(loc) && checkLoc(loc) && c >= 0 && c <= 7) {
                    Valid.add(loc);
                }
                if (!Appeared.contains(loc1) && checkLoc(loc1)  && d >= 0 && d <= 7) {
                    Valid.add(loc1);
                }
                if (!Appeared.contains(loc2) && checkLoc(loc2) && e >= 0 && e <= 7) {
                    Valid.add(loc2);
                }
                if (!Appeared.contains(loc3) && checkLoc(loc3) && f >= 0 && f <= 7) {
                    Valid.add(loc3);
                }
            }
            //Delete the redundant elements.
            Valid = Valid.stream().distinct().collect(Collectors.toList());
        }
        return true;
    }

    public static boolean checkLoc(String p) {
        //Check the tile location whether did it overlap the location of middle station or not.
            String TileLocation = p.substring(0, 2);
        return !TileLocation.equals("33") && !TileLocation.equals("34") && !TileLocation.equals("43") && !TileLocation.equals("44");
    }
}