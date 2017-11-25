import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

public class Abbre {

    private static final String FILENAME = "C:\\Users\\Oriol\\Dropbox\\Universidad\\haks\\Finndcheap\\src\\main\\java\\airports_codes.txt";

    public static void main (String[] args){
        Abbre a = new Abbre();
        a.ciutat();
    }

    public String ciutat(/*String code*/){
        File fileIn;
        Scanner s;
        try {
            fileIn = new File(FILENAME);
            System.out.println("ho fa1");
            s = new Scanner(fileIn);
            System.out.println("ho fa");
        }catch (Exception e){
            System.out.println("No connecta");
            fileIn = null;
            s = null;
        }

        while(s.hasNext())
        {
            System.out.println(s.nextLine());
        }

        return "a";
    }

}
