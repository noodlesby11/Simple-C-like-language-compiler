import java.util.regex.Pattern;

public class Tools {
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
    public static boolean transform(String left,String right) {
        if(left .equals("double")&&(right.equals("int")||right.equals("int36")||right.equals("float or double"))) {
            return true;
        }else if(left .equals("float")&&right.equals("int")||left .equals("float")&&right.equals("int36")) {
            return true;
        }else return left.equals("int") && right.equals("int36") || left.equals("int36") && right.equals("int");
    }
    public static String highType(String type1,String type2) {
        if(type1.equals("int")&&type2.equals("int")) {
            return "int";
        }
        return "float or double";
    }
    public static int Arithmetic(String op,int reg1,int reg2){
        int r = Integer.MAX_VALUE;
        switch (op){
            case "+" -> r = reg1 + reg2;
            case "-" -> r = reg1 - reg2;
            case "*" -> r = reg1 * reg2;
            case "/" -> {
                if(reg2!=0) r = reg1 / reg2;
            }
        }
        return r;
    }
    public static boolean isOp(String op){
        return op.equals("+")||op.equals("-")||op.equals("*")||op.equals("/");
    }
    public static boolean BArithmetic(String op,int reg1,int reg2){
        boolean b = false;
        switch (op){
            case "=" -> b = reg1==reg2;
            case ">" -> b = reg1 > reg2;
            case "<" -> b = reg1 < reg2;
            case "!=" -> b = reg1 != reg2;
            case ">=" -> b = reg1 >= reg2;
            case "<=" -> b = reg1 <= reg2;
        }
        return b;
    }
}
