package comp1110.ass2;


import java.util.regex.Matcher;
import java.util.regex.Pattern;


// This class are made by Yen Kuo.
public class Tile {
    public static int LengthOfTilePlusLocation = 6;

    /**
     * Count the amount for specific tiles on the board
     *
     * @param str     the tile you want to count the instances of
     * @param findStr the string of tiles you would like to search
     * @return the number of the specific tile in a string
     */
    public static int tilesOnBoard(String str, String findStr) {
        int count = 0;
        Pattern p = Pattern.compile(findStr);
        Matcher m = p.matcher(str);
        while (m.find()) {
            count++;
        }
        return count;
    }

    /**
     * @param str     the tile you want to count the instances of
     * @param findStr the string of tiles you would like to search (in this case the string of tiles in hands)
     * @return the number of instances of a tile in any of the hands
     */
    public static int tilesInHand(String str, String findStr) {
        int counter = 0;
        for (int i = 0; i < str.length(); i += 4) {
            //Counting the specific tiles appear times.
            if (str.substring(i, i + 4).contains(findStr)) {
                counter = counter + 1;
            }
        }
        return counter;
    }
}

