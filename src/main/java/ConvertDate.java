import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

public class ConvertDate {

    public static  void main (String[] args){
        Scanner s = new Scanner(System.in);
        System.out.println(convert(s.nextLine()));
    }

    public static Date convert(String date){
        Date dat;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        try{
            dat = format.parse(date);
        }catch (Exception e){
            dat = null;
        }
        return dat;
    }

}
