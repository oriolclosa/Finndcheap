import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

public class Abbre {

    private static final String FILENAME = "/root/Telegram/airports_codes.txt";

    public static void main (String[] args){
        Abbre a = new Abbre();
        Scanner s = new Scanner(System.in);
        System.out.println(a.ciutat(s.nextLine()));
    }

    public String ciutat(String code){
        File fileIn;
        Scanner s;
        try {
            fileIn = new File(FILENAME);
            s = new Scanner(fileIn);
        }catch (Exception e){
            System.out.println("No connecta");
            fileIn = null;
            s = null;
        }

        boolean trobat = false;

        while(s.hasNext() && !trobat)
        {
            String linia = s.nextLine();
            StringBuilder sb = new StringBuilder(3);
            sb.append(linia.charAt(0));
            sb.append(linia.charAt(1));
            sb.append(linia.charAt(2));
            String code2 = sb.toString();
            if (code2.equals(code)){
                trobat = true;
                StringBuilder ciutatBuild = new StringBuilder();
                boolean iscoma = false;
                int i = 5;
                while(!iscoma){
                    if (linia.charAt(i) == ',') iscoma = true;
                    else {
                        ciutatBuild.append(linia.charAt(i));
                        ++i;
                    }
                }
                return ciutatBuild.toString();
            }

        }
        return "no ha trobat";
    }

}
