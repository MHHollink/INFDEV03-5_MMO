import java.util.Random;

/**
 * This Class was created by marcel on 29-9-2015
 * Time of creation : 16:13
 */
public class main {


    public static void main(String[] args) {
        float randomGeneratedNumber = new Random().nextFloat();
        System.out.println((int) map(randomGeneratedNumber,0,1,1,16));
    }

    public static float map(float value, float smallestValue, float largestValue, float smallestReturn, float largestReturn) {
        return smallestReturn + (largestReturn - smallestReturn) * ((value - smallestValue) / (largestValue - smallestValue));
    }
}
