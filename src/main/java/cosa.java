import java.util.*;

public class cosa {


    public static void main (String[] args){
        cosa c = new cosa();
        List<String> temps = new ArrayList<>();
        Scanner s = new Scanner(System.in);
        for (int i = 0; i < 7; ++i){
            temps.add(s.nextLine());
        }
        System.out.println(c.FredOCalor(temps));
    }

    public String FredOCalor(List<String> Temps){
        String dia = Temps.get(0);
        int tempMin = Integer.parseInt(Temps.get(1));
        int tempMax = Integer.parseInt(Temps.get(2));
        String s = Temps.get(3);
        double pluja = Double.parseDouble(Temps.get(4));
        double vent = Double.parseDouble(Temps.get(5));
        String direc = Temps.get(6);
        String result = temperatura(tempMin, tempMax, vent) + ", also " + clouding(s, pluja);
        return result;
    }

    private String clouding(String s, double pluja) {
        String valor1 = null;
        String valor2 = null;
        String valor3 = null;

        switch (s.charAt(1)){
            case '0':
                valor1 = "clear";
                break;
            case '1':
                valor1 = "almost clear";
                break;
            case '2':
                valor1 = "half cloudy";
                break;
            case '3':
                valor1 = "broken";
                break;
            case '4':
                valor1 = "overcast";
                break;
            case '5':
                valor1 = "thin high clouds";
                break;
            case '6':
                valor1 = "fog";
                break;
            default:
                break;
        }

        switch (s.charAt(2)) {
            case '0':
                valor2 = "no precipitation";
                break;
            case '1':
                valor2 = "slight precipitation";
                break;
            case '2':
                valor2 = "showers";
                break;
            case '3':
                valor2 = "precipitation";
                break;
            case '4':
                valor2 = "thunder";
                break;
            default:
                break;
        }

        switch (s.charAt(3)) {
            case '0':
                valor3 = "rain";
                break;
            case '1':
                valor3 = "sleet";
                break;
            case '2':
                valor3 = "snow";
                break;
            default:
                break;
        }
        if (s.charAt(2) > 0) return "the sky is gonna be " + valor1 + ",\nand there will be " + valor2 + " of " + valor3 + " with an exactly value of " + pluja + "mm";
        else return "the sky is gonna be " + valor1 + ", and there will be " + valor2;
    }

    public String temperatura(int m, int M, double v) {
        if (m < 0 && M < 0) {
            if (m < -10 && M < -10) return "It's really cold";
            else if (M > -10 && m < -10) return "It's really cold but sometimes it's only cold";
            else return "It's cold";
        }
        else if (m < 0 && M > 0) {
            if (M < 20) return "It's sometimes cold but other moments it's OK";
            else return "It's sometimes cold but other moments hot";
        }
        else {
            if (m > 20 && M > 20) return "It's hot";
            else if (m < 20 && M > 20) return "It's hot but sometimes it's OK";
            else return "It's OK";
        }
    }

}
