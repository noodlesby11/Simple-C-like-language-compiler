import java.io.*;
import java.util.ArrayList;

public class Lr1Table {
    private static ArrayList<String> Grammar = new ArrayList<>(); // 产生式
    private static ArrayList<String> GrammarLeft = new ArrayList<>(); // 产生式左边的式子集合
    private static ArrayList<String> GrammarRight = new ArrayList<>(); // 产生式右边的式子集合
    private static String[][] map = new String[500][100];//存储分析表

    Lr1Table() {
        initGrammar();
        initGrammarLeft();
        initGrammarRight();
        initMap();
    }
    private void initGrammar() {
        String filePath = "src/yufa2.txt";
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(filePath);
            InputStreamReader reader = new InputStreamReader(fin);
            BufferedReader buffReader = new BufferedReader(reader);
            String strTmp = "";
            while ((strTmp = buffReader.readLine()) != null) {
                Grammar.add(strTmp);
            }
            buffReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void initGrammarLeft() {
        for (String s :
                Grammar) {
            GrammarLeft.add(s.substring(0, s.indexOf(' ')));
        }
    }
    private void initGrammarRight() {
        for (String s :
                Grammar) {
            GrammarRight.add(s.substring(s.lastIndexOf("> ") + 2));
        }
    }
    private void initMap() {
        String filePath = "src/Table3.txt";
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(filePath);
            InputStreamReader reader = new InputStreamReader(fin);
            BufferedReader buffReader = new BufferedReader(reader);
            String str = "";
            int i = 0;
            while ((str = buffReader.readLine()) != null) {
                String[] string = str.split(" ");//以"'"分割字符串
                map[i] = string;
                i++;
            }
            buffReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public String[][] getMap() {
        return map;
    }

    public ArrayList<String> getGrammar() {
        return Grammar;
    }

    public ArrayList<String> getGLeft() {
        return GrammarLeft;
    }

    public ArrayList<String> getGRight() {
        return GrammarRight;
    }

    public static void main(String[] args) throws IOException {
        Lr1Table lr = new Lr1Table();
        System.out.println();
    }
}
