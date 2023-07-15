import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Stack;

public class LR1 {
    private static int temp;
    private static boolean isFirst=true;
    private static Integer index = 0;
    private static Integer row = 1; // 执行到步数的行数
    private static Integer inputIndex = 0; // 执行到步数的列数
    private static Integer GrammarIndex = 0; // 规约的产生式的index
    private static String NowGrammar = ""; // 当前产生式的式子
    private static String NowGrammarLeft = ""; // 当前产生式左边的式子
    private static String NowGrammarRight = ""; // 当前产生式右边的式子
    private static Stack<Integer> stateStack = new Stack<>(); // 状态栈
    private static Stack<String> markStack = new Stack<>(); // 符号栈
    private static ArrayList<InputArr> inputArr = new ArrayList<>(); // 输入串
    private static Integer action = 1;// 1--移进 2--规约 0--接受
    private static ArrayList<String> Grammar = new ArrayList<>(); // 产生式
    private static ArrayList<String> GrammarLeft = new ArrayList<>(); // 产生式左边的式子集合
    private static ArrayList<String> GrammarRight = new ArrayList<>(); // 产生式右边的式子集合
    private static String[][] map = new String[500][100]; // lr1表映射
    private static Lr1Table lr1t = new Lr1Table();
    static class InputArr{
        String string;
        String to;
        public InputArr(String string, String to) {
            super();
            this.string = string;
            this.to = to;
        }
        @Override
        public String toString() {
            return "InputArr [string=" + string + ", to=" + to + "]";
        }

    }
    //计算空格数
    public static Integer cssLen(String s) {
        int num = 1;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ' ') {
                num++;
            }
        }
        return num;

    }

    public static void main(String[] args) throws IOException {
        try {
            String filePath = "src/Lexer.txt";
            FileInputStream fin = new FileInputStream(filePath);
            InputStreamReader reader = new InputStreamReader(fin);
            BufferedReader buffReader = new BufferedReader(reader);
            String strTmp = "";
            while ((strTmp = buffReader.readLine()) != null) {
                String[] string = strTmp.split(" ");
                String left=string[strTmp.split(" ").length - 2];
                String right=string[strTmp.split(" ").length - 1];
                InputArr arr;
                if(left.equals("18")) {
                    arr=new InputArr("id",right);
                    inputArr.add(arr);
                }else if(left.equals("19")) {
                    arr=new InputArr("digit",right);
                    inputArr.add(arr);
                }else {
                    arr=new InputArr(right,"");
                    inputArr.add(arr);//将right字符串存入inputArr集合中
                }

            }
            inputArr.add(new InputArr("$",""));
            buffReader.close();
        } catch (IOException ignored) {
        }
        map = lr1t.getMap();
        Grammar = lr1t.getGrammar();
        GrammarLeft = lr1t.getGLeft();
        GrammarRight = lr1t.getGRight();

        stateStack.push(1);
        markStack.push("#");
        while (action != 0) {
            for (int i = 0; i < map[0].length; i++) {
                if (map[0][i].equals(inputArr.get(inputIndex).string)) {
                    if ("S".equals(map[row][i].subSequence(0, 1))) { // 移进S*
                        index++; // 步骤+1
                        row = Integer.parseInt(map[row][i].substring(1, map[row][i].length()))+1;//读取跳转的状态
                        stateStack.push(row); // 改变状态栈
                        markStack.push(inputArr.get(inputIndex).string); // 改变符号栈
                        inputIndex++;
                        System.out.print("第" + index + "步是移进操作" +"\t"+ "状态栈为：" + stateStack +"\t"+ "符号栈为：" + markStack);
                        System.out.println("    下一个符号是："+inputArr.get(inputIndex).string);

                        switch (inputArr.get(inputIndex).string) {
                            case "for" -> Semantics.setForWhileIfElse(0);
                            case "while" -> Semantics.setForWhileIfElse(1);
                            case "if" -> Semantics.setForWhileIfElse(2);
                            case "else" -> Semantics.setForWhileIfElse(3);
                            case "do" -> Semantics.setForWhileIfElse(4);
                        }
                        break;
                    }
                    else if ("r".equals(map[row][i].subSequence(0, 1))) {
                        int count = 0;
                        GrammarIndex = Integer.parseInt(map[row][i].substring(1))-1 ; // 因为语法产生式从1开始算
                        NowGrammar = Grammar.get(GrammarIndex);

                        NowGrammarLeft = GrammarLeft.get(GrammarIndex);
                        NowGrammarRight = GrammarRight.get(GrammarIndex);
                        String[] string = NowGrammarRight.split(" ");
                        int NowGrammarRightLen = NowGrammarRight.split(" ").length - 1;
                        //符号出栈同时一致才出栈，记录一致的个数
                        for (int j = NowGrammarRightLen; j >= 0; j--) {
                            if (string[j].equals("ε")) { // 产生式右边为空，符号栈不需要出栈
                                count++;
                            } else if (string[j].equals(markStack.pop())) {
                                count++;
                            }
                        }
                        //状态出栈同时通过长度比较是否一致
                        if (string.length == count) { // 判断产生式右边的式子是否等于符号栈中出来的式子
                            markStack.push(NowGrammarLeft);
                            //如果不是空串弹出产生式个数个状态
                            if (!string[0].equals("ε")) {
                                for (int j = 0; j < cssLen(NowGrammarRight); j++) {
                                    stateStack.pop();
                                }
                            }

                            row = stateStack.lastElement();
                            for (int k = 0; k < map[0].length; k++) {
                                //查找goto表
                                if (map[0][k].equals(NowGrammarLeft)) {
                                    stateStack.push((int) Float.parseFloat(map[row][k]) + 1);
                                    index++;
                                    row = (int) Float.parseFloat(map[row][k]) + 1;//状态转换
                                    break;

                                }
                            }
                            System.out.println("第" + index + "步是规约操作" + "\t"+"状态栈为：" + stateStack + "\t"+"符号栈为：" + markStack + "\t"+"产生式为：" + NowGrammar);
                            if((GrammarIndex+1)==43) {//为数组定义时:F1->digit,F1
                                if(isFirst) {
                                    temp=inputIndex-3;
                                    isFirst=false;
                                }else {
                                    temp-=2;
                                }
                                Semantics.sems(GrammarIndex+1, inputArr.get(temp).to);
                            }else {
                                Semantics.sems(GrammarIndex+1, inputArr.get(inputIndex-1).to);
                            }
                        } else {
                            System.out.println("符号栈与产生式右部不一致");
                        }
                        break;
                    } else if ("acc".equals(map[row][i])) {
                        System.out.println("success!");
                        action = 0;
                        break;
                    }
                }
            }
        }
    }
}

