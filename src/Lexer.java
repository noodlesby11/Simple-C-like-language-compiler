import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
public class Lexer {
    static int i = 0;
    static String keywords[] = {"#include","int","float","int36", "double","void", "stdio.h", "return","for","do","while", "if","else","break","printf","%d","%f"};
    static List<Character> sourceCode= new ArrayList<>();
    static String filename="src/Lexer.txt";
    boolean int36have = false;//判断是否出现过int36
    public static void getTokenOperation(char c)   {
        i++;
    }
    public static void getTokenNumber1(String c) {
        i++;
    }

    public void lexicalAnalysis() throws IOException{
        BufferedWriter writer=new BufferedWriter(new FileWriter(filename));
        String file = "src/input.txt";
        Reader reader = null;
        try {
            reader = new InputStreamReader(Files.newInputStream(Paths.get(file)));
            int i1;
            while ((i1 = reader.read()) != -1) {
                if (((char) i1) != '\r') {
                    sourceCode.add((char) i1);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        while (i < sourceCode.size()) {
            char c = sourceCode.get(i);
            String s = String.valueOf(c);
            String string="";
            boolean flag=true;//是单字符
            boolean kw=false;//是标识符不是关键字
            boolean findoneid=false;//符号正确为true若为false则报错
            while(true) {
                //匹配标识符和%#开头的关键字
                if (s.matches("[A-Za-z%#]")) {
                    string=string+s;
                    i++;
                    s=String.valueOf(sourceCode.get(i));
                    while(s.matches("[A-Za-z0-9.]")) {
                        flag=false;
                        string=string+s;
                        i++;
                        s=String.valueOf(sourceCode.get(i));
                    }
                    if(flag) {
                        if(int36have) writer.write(20+" "+string);
                        else writer.write(keywords.length+1+" "+string);
                        writer.newLine();
                        findoneid=true;
                        break;
                    }
                    for (int i=0;i<keywords.length;i++) {
                        if(string.equals(keywords[i])) {
                            if(keywords[i].equals("int36")) int36have = true;
                            writer.write(i+1+" "+string);
                            writer.newLine();
                            kw=true;
                            break;
                        }
                    }
                    if(!kw) {
                        if(int36have) {
                            writer.write(20+" "+string);
                            int36have = false;
                        }
                        else writer.write(keywords.length+1+" "+string);
                        writer.newLine();
                        break;
                    }
                }
                else {
                    break;
                }
            }

            flag=true;
            string="";

            //判断点后面有无数字
            boolean singlepoint = false;
            boolean findpoint = false;

            while(true) {
                if(s.matches("[0]")) {
                    string=string+s;
                    i++;
                    s=String.valueOf(sourceCode.get(i));
                    if(s.matches("[0-9]")){
                        writer.close();
                        throw new LexicalError("unexpected char ${c}");
                    }
                    if(s.matches("[.]")) {//是0.
                        singlepoint = true;
                        string=string+s;
                        i++;
                        s=String.valueOf(sourceCode.get(i));
                        while(s.matches("[0-9]")){
                            if(s.matches("[0-9]")) {
                                singlepoint = false;
                                string = string + s;
                                i++;
                                s = String.valueOf(sourceCode.get(i));
                            }
                            else{
                                singlepoint = true;
                            }
                        }
                        if(singlepoint) {//小数点后留空直接写数字以外的抛出异常
                            writer.close();
                            throw new LexicalError("unexpected char ${c}");
                        }else {
                            getTokenNumber1(string);//输出0.xxxx
                            writer.write(keywords.length+2+" "+string);
                            writer.newLine();
                            i--;
                            break;
                        }
                    }else {//仅仅是0
                        getTokenNumber1(string);
                        writer.write(keywords.length+2+" "+string);
                        writer.newLine();
                        i--;
                        break;
                    }
                }
                //1-9开头的int或double
                if (s.matches("[1-9]")) {
                    string=string+s;
                    i++;
                    s=String.valueOf(sourceCode.get(i));
                    while(s.matches("[0-9.]")) {//int
                        flag=false;
                        string=string+s;
                        if(s.matches("[.]")) {
                            findpoint = true;
                            singlepoint = true;
                            break;
                        }
                        i++;
                        s=String.valueOf(sourceCode.get(i));
                    }
                    if(findpoint) {
//						string=string+s;
                        i++;
                        s=String.valueOf(sourceCode.get(i));
                        while(s.matches("[0-9]")) {//double
                            if(s.matches("[0-9]")) {
                                singlepoint = false;
                                string = string + s;
                                i++;
                                s = String.valueOf(sourceCode.get(i));
                            }
                            else{
                                singlepoint = true;
                            }
                        }
                        if(singlepoint) {//小数点后留空直接写数字以外的抛出异常
                            throw new LexicalError("unexpected char ${c}");
                        }else if(singlepoint){
                            getTokenNumber1(string);
                            writer.write(keywords.length+2+" "+string);
                            writer.newLine();
                            i--;
                            break;
                        }
                    }
                    if(flag) {
                        getTokenNumber1(string);//仅仅是1-9单数字
                        writer.write(keywords.length+2+" "+string);
                        writer.newLine();
                        i--;
                        findoneid=true;
                        break;
                    }
                    else{
                        getTokenNumber1(string);//int多数字
                        writer.write(keywords.length+2+" "+string);
                        writer.newLine();
                        i--;
                        break;
                    }
                }
                else {
                    break;
                }
            }

            c = sourceCode.get(i);
            //38++
            string="";
            while(true) {
                if(c=='+') {
                    string=string+s;
                    i++;
                    if(sourceCode.get(i)=='+') {
                        string=string+'+';
                        i++;
                        writer.write(38+" "+string);
                        writer.newLine();
                        findoneid=true;
                        break;
                    }
                    else {
                        i--;
                        break;
                    }
                }
                else{
                    break;
                }
            }
            //--39
            string="";
            while(true) {
                if(c=='-') {
                    string=string+s;
                    i++;
                    if(sourceCode.get(i)=='-') {
                        string=string+'-';
                        i++;
                        writer.write(39+" "+string);
                        writer.newLine();
                        findoneid=true;
                        break;
                    }
                    else {
                        i--;
                        break;
                    }
                }
                else{
                    break;
                }
            }
            //&&35
            string="";
            while(true) {
                if(c=='&') {
                    string=string+s;
                    i++;
                    if(sourceCode.get(i)=='&') {
                        string=string+'&';
                        //getTokenLogicand(string);
                        i++;
                        writer.write(35+" "+string);
                        writer.newLine();
                        findoneid=true;
                        break;
                    }
                    else {
                        i--;
                        break;
                    }
                }
                else{
                    break;
                }
            }
            //||36
            string="";
            while(true) {
                if(c=='|') {
                    string=string+s;
                    i++;
                    if(sourceCode.get(i)=='|') {
                        string=string+'|';
                        //getTokenLogicor(string);
                        i++;
                        writer.write(36+" "+string);
                        writer.newLine();
                        findoneid=true;
                        break;
                    }
                    else {
                        i--;
                        break;
                    }
                }
                else{
                    break;
                }
            }
            //!=30
            string="";
            while(true) {
                if(c=='!') {
                    string=string+s;
                    i++;
                    if(sourceCode.get(i)=='=') {
                        string=string+'=';
                        //getTokenLogicalnon(string);
                        i++;
                        writer.write(30+" "+string);
                        writer.newLine();
                        findoneid=true;
                        break;
                    }
                    else {
                        i--;
                        break;
                    }
                }
                else{
                    break;
                }
            }
            //==25
            string="";
            while(true) {
                if(c=='=') {
                    string=string+s;
                    i++;
                    if(sourceCode.get(i)=='=') {
                        string=string+'=';
                        //getTokenLogicequality(string);
                        i++;
                        writer.write(25+" "+string);
                        writer.newLine();
                        findoneid=true;
                        break;
                    }
                    else {
                        i--;
                        break;
                    }
                }
                else{
                    break;
                }
            }
            //>=28
            string="";
            while(true) {
                if(c=='>') {
                    string=string+s;
                    i++;
                    if(sourceCode.get(i)=='=') {
                        string=string+'=';
                        //getTokenGreaterequal(string);
                        i++;
                        writer.write(28+" "+string);
                        writer.newLine();
                        findoneid=true;
                        break;
                    }
                    else {
                        i--;
                        break;
                    }
                }
                else{
                    break;
                }
            }
            //<=29
            string="";
            while(true) {
                if(c=='<') {
                    string=string+s;
                    i++;
                    if(sourceCode.get(i)=='=') {
                        string=string+'=';
                        //getTokenLessequal(string);
                        i++;
                        writer.write(29+" "+string);
                        writer.newLine();
                        findoneid=true;
                        break;
                    }
                    else {
                        i--;
                        break;
                    }
                }
                else{
                    break;
                }
            }

            c = sourceCode.get(i);
            switch (c) {
                case '$':{
                    writer.write('$');
                    writer.newLine();
                    writer.close();
                    return;}
                case '\n':
                case '\t':
                case ' ': {
                    i++;
                    break;
                }
                case '=':{
                    getTokenOperation(c);
                    writer.write(24+" "+c);
                    writer.newLine();
                    break;
                }
                case '*':
                {
                    getTokenOperation(c);
                    writer.write(33+" "+c);
                    writer.newLine();
                    break;
                }
                case '+':
                {
                    getTokenOperation(c);
                    writer.write(31+" "+c);
                    writer.newLine();
                    break;
                }
                case '-':
                {
                    getTokenOperation(c);
                    writer.write(32+" "+c);
                    writer.newLine();
                    break;
                }
                case '/':
                {
                    getTokenOperation(c);
                    writer.write(34+" "+c);
                    writer.newLine();
                    break;
                }
                case '^':
                case ':':{
                    getTokenOperation(c);
                    writer.write(22+" "+c);
                    writer.newLine();
                    break;
                }
                case '[':
                {
                    getTokenOperation(c);
                    writer.write(44+" "+c);
                    writer.newLine();
                    break;
                }
                case ']':
                {
                    getTokenOperation(c);
                    writer.write(45+" "+c);
                    writer.newLine();
                    break;
                }
                case '!':
                {
                    getTokenOperation(c);
                    writer.write(37+" "+c);
                    writer.newLine();
                    break;
                }
                case '<':
                {
                    getTokenOperation(c);
                    writer.write(26+" "+c);
                    writer.newLine();
                    break;
                }
                case '>':
                {
                    getTokenOperation(c);
                    writer.write(27+" "+c);
                    writer.newLine();
                    break;
                }
                case '}':
                {
                    getTokenOperation(c);
                    writer.write(42+" "+c);
                    writer.newLine();
                    break;
                }
                case '{':
                {
                    getTokenOperation(c);
                    writer.write(43+" "+c);
                    writer.newLine();
                    break;
                }
                case '"':
                {
                    getTokenOperation(c);
                    writer.write(48+" "+c);
                    writer.newLine();
                    break;
                }
                case '(':
                {
                    getTokenOperation(c);
                    writer.write(40+" "+c);
                    writer.newLine();
                    break;
                }
                case ')':
                {
                    getTokenOperation(c);
                    writer.write(41+" "+c);
                    writer.newLine();
                    break;
                }
                case ';':
                {
                    getTokenOperation(c);
                    writer.write(47+" "+c);
                    writer.newLine();
                    break;
                }
                case ',':
                {
                    getTokenOperation(c);
                    writer.write(46+" "+c);
                    writer.newLine();
                    break;
                }
                case '&': {
                    i++;
                    writer.write(c);
                    writer.newLine();
                    break;
                }
                case '?':{
                    getTokenOperation(c);
                    writer.write(21+" "+c);
                    writer.newLine();
                    break;
                }
                default:
                    if(!findoneid) {
                        throw new LexicalError("错误的符号");
                    }
                    else {
                        break;
                    }

            }
        }
        writer.close();
    }
    public static void main(String[] args)throws IOException{
        Lexer lexer=new Lexer();
        lexer.lexicalAnalysis();
        System.out.println("词法分析成功");
    }
}
class LexicalError extends Error {
    public LexicalError(String string) {
    }
}

