import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
public  class Semantics {
    private static boolean JVarible=true;
    private static boolean isPrint=false;
    private static final Stack<String> valueStack=new Stack<>();
    private static String tempVar;
    private static String tempVar1="";
    private static String priviousTempVar;
    private static final Stack<Integer> NXQForWhile=new Stack<>();
    private static Upro UTemp=new Upro();
    private static StatementFor statefor=new StatementFor();
    private static final Stack<Integer> listForIfTemp=new Stack<>();//else地址
    private static final Xpro X=new Xpro();
    private static boolean Xava = false;//标记X是否可用算出
    private static final Stack<Upro> stacku=new Stack<>();
    private static boolean ifIndex=false; //标记是if代码块
    private static final Stack<Boolean> stackif=new Stack<>();
    private static int existFor=0;
    private static int existWhile = 0;
    private static int existIf = 0;
    private static int existElse = 0;
    private static Stack<Integer> addrDo = new Stack<>();
    private static Integer addrStart;//一块代码块的四元式起始位置
    private static Integer addrStartTemp;//一块代码块的四元式起始位置
    private static final Stack<Integer> stackAddrStart=new Stack<>();//真地址开始位置
    private static Integer addrEnd;//一块代码块的四元式结束位置
    private static String VOP;//比较运算符
    private static Integer NXQ=1;//下一个四元式地址索引
    private static Iden temp;//专为第87产生式设计的临时存储变量
    private static int indexOpr=0;//记录临时变量个数
    private static int indexOpn=0;//记录该算术表达式进行了多少次运算
    private static final List<String> addrs=new ArrayList<>();//四元式表
    private static final List<Iden> ids=new ArrayList<>();//变量表
    private static final List<TempIden> tempids=new ArrayList<>();//赋值临时变量表
    private static final Ipro I=new Ipro();
    private static final Fpro F=new Fpro();
    private static final Tpro T=new Tpro();
    private static final Epro E=new Epro("","");
    private static final Epro E1=new Epro("","",Integer.MAX_VALUE);
    private static final Epro anotherE=new Epro("","");
    private static final Stack<String> stackt=new Stack<>();//为算术表达式+-使用
    private static final Stack<String> stackv=new Stack<>();//为算术表达式+-使用
    private static final Stack<Integer> stackV=new Stack<>();//为算术表达式+-使用
    private static final Stack<String> stackt1=new Stack<>();//为算术表达式*/使用
    private static final Stack<String> stackv1=new Stack<>();//为算术表达式*/使用
    private static final Stack<Integer> stackV1 =new Stack<>();//为算术表达式*/使用
    static class StatementFor{
        boolean[] sym=new boolean[2];
        String varValue;//存变量值
        String varTemp;//存变量
        Stack<String> varList = new Stack<>();
        int varNum = 0;//存变量数
        Stack<Integer> addr = new Stack<>();//记录for语句的起始地址
        boolean[] flag=new boolean[3];//标记
        StatementFor(){
            varValue=varTemp="";
            sym[0]=sym[1]=flag[0]=flag[1]=flag[2]=false;
        }
        void init() {
            varValue=varTemp="";
            flag[0]=flag[1]=flag[2]=false;
        }
        public int getAddr(){
            if(!addr.empty()) {
                return addr.peek();
            }
            return 0;
        }
        public void popAddr(){
            if(!addr.empty()) {
                addr.pop();
            }
        }
        public String getVarTemp(){
            if(!varList.empty()){
                return varList.peek();
            }
            return "";
        }
        public void popVarTemp(){
            if(!varList.empty()){
                varList.pop();
            }
        }
    }
    static class Iden{
        String name;//标识符名称
        String type;//标识符数据类型
        int value = Integer.MAX_VALUE;//标识符的值如果有的话
        boolean inLoop = false;//标识符是否在循环中赋值过
        boolean flag1;//标记该标识符是否正在等待被处理，默认为true,专为定义变量设计
        boolean flag2;//标记该标识符是否正在等待被处理，默认为false，专为变量赋值设计
        int flag2i = 0;//专为变量自赋值设计
        boolean flag3;//标记该标识符是接受边定义边赋值处理，默认为false，专为定义变量同时赋值设计
        public Iden(String name, String type) {
            super();
            this.name = name;
            this.type = type;

            this.flag1=true;
            this.flag2=false;
            this.flag3=false;
        }
        public Iden(String name, String type,int value) {
            this(name,type);
            this.value = value;
        }
    }
    static class TempIden{
        String name;
        int value;
        public TempIden(String name,int value){
            this.name = name;
            this.value = value;
        }
    }
    static class Upro{
        Integer TC=0;
        Integer FC=0;
    }
    static class Xpro{
        Integer TC=0;
        Integer FC=0;
    }
    static class Ipro{
        String type;
        public Ipro() {
            super();
        }
    }
    static class Fpro{
        String type;
        String value;
        int Value;
        public Fpro(String type, String value) {
            super();
            this.type = type;
            this.value = value;
        }
        public Fpro(String type,String value,int Value){
            this(type,value);
            this.Value = Value;
        }
        public Fpro() {
            super();
        }
    }
    static class Tpro{
        String type;
        String value;
        int Value;
        public Tpro(String type, String value) {
            super();
            this.type = type;
            this.value = value;
        }
        public Tpro(String type,String value,int Value){
            this(type,value);
            this.Value = Value;
        }
        public Tpro() {
            super();
        }

    }
    static class Epro{
        String type;
        String value;
        int Value;
        public Epro(String type, String value) {
            super();
            this.type = type;
            this.value = value;
        }
        public Epro(String type, String value,int Value) {
            this(type,value);
            this.Value = Value;
        }
        public Epro() {
            super();
        }
    }
    private static void gen(String op,String reg1,String reg2,String result,int index) throws IOException {
        addrs.add(index+" "+"("+op+","+reg1+","+reg2+","+result+")");
        NXQ++;
    }
    /**
     * 往变量表ids添加新的id，前提是id.name尚未存在，如果存在，则认为已声明的id等待被操作
     */
    private static void addTo(Iden id) {
        for(Iden i:ids) {
            if(id.name.equals(i.name)&& !i.flag1 && !i.flag2) {
                i.flag2=true;
                i.flag2i++;
                temp=i;
                return;
            }else if(id.name.equals(i.name)&& !i.flag1){
                i.flag2i++;
                temp=i;
                return;
            }
            else if(id.name.equals(i.name)) {
                temp=i;
                return;
            }
        }
        ids.add(id);
    }
    private static void setInLoop(String name) {
        for(Iden i:ids) {
            if(name.equals(i.name) && (existFor>0 || existWhile>0)) {
                i.inLoop =true;
            }
        }
    }
    public static boolean NotInLoop(String name){
        for (Iden i:
             ids) {
            if(i.name.equals(name)){
                return !i.inLoop;
            }
        }
        return true;
    }
    public static void setForWhileIfElse(int flag){
        if(flag == 0 ) {
            statefor.sym[0] = false;
            existFor++;
            statefor.varNum++;
        }
        else if (flag == 1) existWhile ++;
        else if (flag == 2) {
            existIf ++;
            stackif.push(true);
        }
        else if (flag == 3) {
            existElse ++;
            stackif.push(false);
        }
        else if (flag == 4) {
            addrDo.push(NXQ);
        }
    }
    public static void addValue(String id , String value){
        for (Iden ids: ids) {
            if (ids.name.equals(id)) {
                if(Tools.isInteger(value)){
                    ids.value = Integer.parseInt(value);
                }else {
                    for (TempIden ti: tempids) {
                        if(ti.name.equals(value)&& ti.value!=Integer.MAX_VALUE){
                            ids.value = ti.value;
                        }
                    }
                }
            }
        }
    }
    public static int nameToValue(String name){
        int value = Integer.MAX_VALUE;
        for (Iden i:
             ids) {
            if(i.name.equals(name) && i.value!=Integer.MAX_VALUE) value = i.value;
        }
        for (TempIden i:
                tempids) {
            if(i.name.equals(name) && i.value!=Integer.MAX_VALUE) value = i.value;
        }
        return value;
    }
    public static boolean haveResult77(String addr){
        int d1 = addr.indexOf(",");
        int d2 = addr.indexOf(",",d1+1);
        int d3 = addr.indexOf(",",d2+1);
        String op = addr.substring(addr.indexOf("j")+1,d1);
        String r1 = addr.substring(d1+1,d2);
        String r2 = addr.substring(d2+1,d3);
        int reg1 = nameToValue(r1);
        int reg2 = nameToValue(r2);
        return Tools.BArithmetic(op,reg1,reg2);
    }
    public static void sems(int index,String to) throws IOException {
        BufferedWriter bw=new BufferedWriter(new FileWriter("src/SemanticOutput.txt"));
        switch (index) {
            case 11 -> {//D->ID
                JVarible = true;
                if (!to.equals("") && !to.equals("main")) {
                    if (isPrint) isPrint = false;
                    priviousTempVar = tempVar;
                    tempVar = to;
                    addTo(new Iden(to, "",0));
                    setInLoop(to);
                    statefor.varTemp = to;
                    if (!statefor.flag[1]) {
                        statefor.flag[0] = true;
                    }
                    if (statefor.sym[0] && !statefor.sym[1]) {
                        ids.remove(ids.size() - 1);
                        statefor.sym[1] = true;
                        statefor.varTemp = to;
                    }
                    if(statefor.varNum>0){
                        statefor.varList.add(to);
                        statefor.sym[1] = false;
                        statefor.varNum--;
                    }
                }
            }
            case 12 -> {//D->D[E]
                JVarible = true;//
                ids.remove(ids.size() - 1);
                if (Tools.isInteger(E.value)) {
                    for (int i = 0; i < Integer.parseInt(E.value); i++) {
                        String var = tempVar + "[" + i + "]";
                        addTo(new Iden(var, ""));
                    }
                } else {
                    tempVar1 = priviousTempVar;
                    tempVar1 += "[" + E.value + "]";
                    addTo(new Iden(tempVar1, ""));
                    ids.get(ids.size() - 1).flag1 = false;
                    ids.get(ids.size() - 1).type = "int";
                }
            }
            case 16 -> {//I->int
                JVarible = true;
                I.type = "int";
            }
            case 17 -> {//I->float
                JVarible = true;
                I.type = "float";
            }
            case 18 -> {//I -> double
                JVarible = true;
                I.type = "double";
            }
            case 19 -> {//I -> int36
                JVarible = true;
                I.type = "int36";
            }
            case 20 -> {//J -> K J
                if (!JVarible) {
                    break;
                }
                if (!stackif.isEmpty()) {
                    ifIndex = stackif.pop();
                }
                if (ifIndex && !stackAddrStart.isEmpty() && !stacku.isEmpty()) {
                    gen("j", "/", "/", Integer.valueOf(NXQ + 1).toString(), NXQ);
                    listForIfTemp.push(NXQ-2);
                    addrEnd = NXQ;
                    Upro U = stacku.pop();
                    int addr = U.TC;
                    addrStart = stackAddrStart.pop();
                    addrStartTemp = addrStart;
                    U.TC = addrStart;
                    U.FC = addrEnd;
                    String str = addrs.get(addr - 1);
                    str = str.replaceAll("[,]\\d+[)]", "," + U.TC.toString() + ")");//修改真出口地址
                    addrs.set(addr - 1, str);

                    str = addrs.get(addr);
                    str = str.replaceAll("[,]\\d+[)]", "," + U.FC.toString() + ")");//修改假出口地址
                    addrs.set(addr, str);

                    ifIndex = false;
                    JVarible = false;
                }
            }
            case 28 -> {//P -> I a ;
                JVarible = true;//
                for (Iden id : ids) {
                    if (id.flag1 && !id.flag2) {//仅赋值
                        if (id.type.equals("")) {
                            id.type = I.type;
                        } else if (I.type.equals(id.type) || id.type.contains(I.type) || Tools.transform(I.type, id.type)) {
                            id.type = I.type;
                        } else {
                            System.out.println("变量类型不符:" + id.name);
                            System.exit(-1);
                        }
                        id.flag1 = false;
                    }
                }
            }
            case 30, 31 -> {//a -> a1,a -> a1 , a
                JVarible = true;
                statefor.init();
            }
            case 33 -> {//a1 -> D = E
                JVarible = true;
                statefor.init();//E已经被使用不会在for语句中使用了
                for (Iden id : ids) {
                    if (!id.flag1 && (id.flag2|| id.flag2i>0)) {//只是赋值
                        id.flag2 = false;
                        id.flag2i--;
                        if(E.Value!=Integer.MAX_VALUE && existWhile==0 && existFor==0) {
                            while (indexOpn>0){
                                addrs.remove(addrs.size()-1);
                                NXQ--;
                                indexOpn--;
                                indexOpr--;
                            }
                            gen("=", String.valueOf(E.Value), "/", id.name, NXQ);
                        }
                        else
                            gen("=", E.value, "/", id.name, NXQ);
                        addValue(id.name,E.value);
                        stackt.clear();
                        stackv.clear();
                        stackt1.clear();
                        stackv1.clear();
                    } else if (!id.flag2 && id.flag1 && !id.flag3 ) {//边定义边赋值
                        id.type = E.type;
                        id.flag3 = true;
                        if(E.Value!=Integer.MAX_VALUE && existWhile==0 && existFor==0) {
                            while (indexOpn>0){
                                addrs.remove(addrs.size()-1);
                                NXQ--;
                                indexOpn--;
                                indexOpr--;
                            }
                            gen("=", String.valueOf(E.Value), "/", id.name, NXQ);
                        }
                        else
                            gen("=", E.value, "/", id.name, NXQ);
                        addValue(id.name,E.value);
                        stackt.clear();
                        stackv.clear();
                        stackt1.clear();
                        stackv1.clear();
                        indexOpn = 0;
                    }
                }
            }
            case 34 ->{//a1 -> D = E1
                if(E1.Value!=Integer.MAX_VALUE && NotInLoop(priviousTempVar) && NotInLoop(tempVar) && Xava) {
                    gen("=", String.valueOf(E1.Value), "/", ids.get(ids.size() - 1).name, NXQ);
                }
                else {
                    String string1 = addrs.get(addrs.size() - 1);
                    String string2 = addrs.get(addrs.size() - 2);
                    string1 = string1.replaceAll("[,]\\d+[)]", "," + ids.get(ids.size() - 1).name + ")");
                    string2 = string2.replaceAll("[,]\\d+[)]", "," + ids.get(ids.size() - 1).name + ")");
                    addrs.set(addrs.size() - 1, string1);
                    addrs.set(addrs.size() - 2, string2);
                }
            }
            case 35 -> {//a1 -> D = { F1 }
                JVarible = true;//
                statefor.init();
                for (Iden id : ids) {
                    if (!id.flag2 && id.flag1 && !id.flag3) {//边定义边赋值
                        id.flag3 = true;
                        E.value = valueStack.pop();
                        gen("=", E.value, "/", id.name, NXQ);
                        indexOpr = 0;
                        stackt.clear();
                        stackv.clear();
                        stackt1.clear();
                        stackv1.clear();
                    }
                }
            }
            case 36 -> {//a1 -> D ++
                JVarible = true;//
                String resultTemp = "T" + Integer.valueOf(++indexOpr).toString();
                gen("+", tempVar, "1", resultTemp, NXQ);
                gen("=", resultTemp, "/", tempVar, NXQ);
            }
            case 37 -> {//a1 -> D --
                JVarible = true;//
                String resultTemp = "T" + Integer.valueOf(++indexOpr).toString();
                gen("-", tempVar, "1", resultTemp, NXQ);
                gen("=", resultTemp, "/", tempVar, NXQ);
            }
            case 42, 43 -> {//F1 -> digit,F1 -> digit , F1
                JVarible = true;//
                valueStack.push(to);
            }
            case 45, 46 -> {//d -> %d,d -> %f
                JVarible = true;
                isPrint = true;
            }
            case 49 ->{//O -> do { J } while c ;
                addrs.remove(addrs.size()-1);
                String string = addrs.get(addrs.size()-1);
                string = string.replaceAll("[,]\\d+[)]", "," + addrDo.pop() + ")");
                addrs.set(addrs.size()-1, string);
                NXQForWhile.pop();
            }
            case 50 -> {//O -> while c { J }
                JVarible = true;
                int addrUpd = NXQForWhile.pop();
                gen("j", "/", "/", Integer.valueOf(addrUpd).toString(), NXQ);
                addrs.set(addrUpd, ++addrUpd + " (j,/,/," + NXQ + ")");
                if(statefor.sym[0]) statefor.sym[0] =false;
                existWhile --;
            }
            case 51,52 -> {//O -> for ( D = E ; U ; D ++ ) { J },O -> for ( I D = E ; U ; D ++ ) { J }
                JVarible = true;
                String result = "T" + Integer.valueOf(++indexOpr).toString();
                gen("+", statefor.getVarTemp(), "1", result, NXQ);
                gen("=", result, "/", statefor.getVarTemp(), NXQ);
                gen("j", "/", "/", String.valueOf(statefor.getAddr()), NXQ);
                String string = addrs.get(statefor.getAddr());
                string = string.replaceAll("[,]\\d+[)]", "," + NXQ.toString() + ")");
                addrs.set(statefor.getAddr(), string);
                statefor.init();
                statefor.popAddr();
                statefor.popVarTemp();
                existFor--;
                indexOpn = 0;
            }
            case 53,54 -> {//O -> for ( D = E ; U ; D --) { J }
                JVarible = true;
                String result = "T" + Integer.valueOf(++indexOpr).toString();
                gen("-", statefor.getVarTemp(), "1", result, NXQ);
                gen("=", result, "/", statefor.getVarTemp(), NXQ);
                gen("j", "/", "/", String.valueOf(statefor.getAddr()), NXQ);
                String string = addrs.get(statefor.getAddr());
                string = string.replaceAll("[,]\\d+[)]", "," + NXQ.toString() + ")");
                addrs.set(statefor.getAddr(), string);
                statefor.init();
                statefor.popAddr();
                statefor.popVarTemp();
                existFor --;
                indexOpn = 0;
            }
            case 55 -> {//M -> if c { J } M1
                JVarible = true;
                NXQForWhile.pop();
                existIf --;
            }
            case 56 -> {//M1 -> else M2
                JVarible = true;
                int elem = listForIfTemp.pop();
//                if (elem == addrs.size()) {
//                    elem = NXQ - 2;
//                }
                String str1 = addrs.get(elem);
                str1 = str1.replaceAll("[,]\\d+[)]", "," + NXQ + ")");
                addrs.set(elem, str1);
                existElse --;
            }
            case 57 -> {// M1 -> ε
                JVarible = true;                                                                                                                                                          addrs.remove(NXQ-2);
                listForIfTemp.pop();
                stackAddrStart.push(addrStartTemp);
                stacku.push(UTemp);
            }
//            case 58 -> {//M2 -> { J }
//                JVarible = true;
//                if (stackif.isEmpty() && listForIfTemp.get(listForIfTemp.size() - 1) != NXQ - 2) {
//                    break;
//                }
//                //listForIfTemp.pop();
//                //stackAddrStart.push(addrStartTemp);
//                //stacku.push(UTemp);
//            }
            case 60 -> {//c -> ( U )
                JVarible = true;
                stackAddrStart.push(NXQ);
            }
            case 62 -> {//U -> X
                JVarible = true;
                Upro U = new Upro();
                U.TC = X.TC;
                U.FC = X.FC;
                stacku.push(U);
                if (statefor.sym[0] && !statefor.sym[1]) {
                    sems(60, "");
                }
            }
            case 66 -> {//X -> E V E
                JVarible = true;
                if (statefor.flag[0] && statefor.flag[1] && statefor.flag[2] && !statefor.sym[0] && existFor>0) {
                    statefor.sym[0] = true;
                    gen("=", statefor.varValue, "/", statefor.varTemp, NXQ);
                    statefor.addr.push(NXQ);
                }else {
                    NXQForWhile.push(NXQ);
                }
                X.TC = NXQ;
                X.FC = NXQ + 1;

                gen("j" + VOP, anotherE.value, E.value, String.valueOf(NXQ+2), NXQ);
                gen("j", "/", "/", "0", NXQ);
                indexOpn = 0;
            }
            case 68 -> {//V -> >=
                //System.out.println("----" + E.value);
                JVarible = true;
                anotherE.value = E.value;
                VOP = ">=";
            }
            case 69 -> {//V -> <=
                //System.out.println("----" + E.value);
                JVarible = true;
                anotherE.value = E.value;
                VOP = "<=";
            }
            case 70 -> {//V -> <
                //System.out.println("----" + E.value);
                JVarible = true;//
                anotherE.value = E.value;
                VOP = "<";
            }
            case 71 -> {//V -> ==
                //System.out.println("----" + E.value);
                JVarible = true;
                anotherE.value = E.value;
                VOP = "==";
            }
            case 72 -> {//V -> !=
                //System.out.println("----" + E.value);
                JVarible = true;
                anotherE.value = E.value;
                VOP = "!=";
            }
            case 73 -> {//V -> >
                //System.out.println("----" + E.value);
                JVarible = true;
                anotherE.value = E.value;
                VOP = ">";
            }
            case 74 -> {//E -> T + E
                JVarible = true;
                stackt.pop();
                T.type = stackt.pop();
                stackv.pop();
                T.value = stackv.pop();
                stackV.pop();
                T.Value = stackV.pop();
                E.type = Tools.highType(E.type, T.type);
                indexOpr++;
                String result1 = "T" + Integer.valueOf(indexOpr).toString();
                tempids.add(new TempIden(result1, T.Value+E.Value));
                gen("+", T.value, E.value, result1, NXQ);
                E.value = result1;
                E.Value = nameToValue(result1);
                if (!(statefor.flag[0] && statefor.flag[1] && statefor.flag[2])) {
                    statefor.varValue = E.value;
                }
                if (statefor.flag[0] && statefor.flag[1]) {
                    statefor.flag[2] = true;
                }
                stackt.push(E.type);
                stackv.push(E.value);
                stackV.push(E.Value);
                indexOpn++;
            }
            case 75 -> {//E -> T - E
                JVarible = true;
                stackt.pop();
                T.type = stackt.pop();
                stackv.pop();
                T.value = stackv.pop();
                stackV.pop();
                T.Value = stackV.pop();
                E.type = Tools.highType(E.type, T.type);
                indexOpr++;
                String result2 = "T" + Integer.valueOf(indexOpr++).toString();
                gen("-", T.value, E.value, result2, NXQ);
                E.value = result2;
                if (!(statefor.flag[0] && statefor.flag[1] && statefor.flag[2])) {
                    statefor.varValue = E.value;
                }
                if (statefor.flag[0] && statefor.flag[1]) {
                    statefor.flag[2] = true;
                }
                stackt.push(E.type);
                stackv.push(E.value);
                stackV.push(E.Value);
                indexOpn++;
            }
            case 76 -> {//E -> T
                JVarible = true;
                E.type = T.type;
                E.value = T.value;
                E.Value = T.Value;
                if (!(statefor.flag[0] && statefor.flag[1] && statefor.flag[2])) {
                    statefor.varValue = E.value;
                }
                if (statefor.flag[0] && statefor.flag[1]) {
                    statefor.flag[2] = true;
                }
            }
            case 77 -> {//E1 -> X ? D : D
                String string = addrs.get(addrs.size()-1);
                Xava = chargeIsAvailable(addrs.get(addrs.size()-2));
                if(nameToValue(priviousTempVar)!=Integer.MAX_VALUE && nameToValue(tempVar)!=Integer.MAX_VALUE && NotInLoop(priviousTempVar) && NotInLoop(tempVar) && Xava) {
                    addrs.remove(addrs.size()-1);
                    addrs.remove(addrs.size()-1);
                    NXQ = NXQ-2;
                    if(haveResult77(addrs.get(addrs.size()-2))){
                        E1.value = priviousTempVar;
                        E1.Value = nameToValue(priviousTempVar);
                    }else{
                        E1.value = tempVar;
                        E1.Value = nameToValue(tempVar);
                    }
                }
                else {
                    string = string.replaceAll("[,]\\d+[)]", "," + (NXQ + 1) + ")");
                    addrs.set(addrs.size() - 1, string);
                    gen("=", priviousTempVar, "/", "0", NXQ);
                    gen("=", tempVar, "/", "0", NXQ);
                }
            }
            case 78 -> {//T -> F * T
                JVarible = true;
                stackt.pop();
                stackv.pop();
                stackV.pop();
                F.type = stackt1.pop();
                F.value = stackv1.pop();
                F.Value = stackV1.pop();
                T.type = Tools.highType(T.type, F.type);
                indexOpr++;
                String result3 = "T" + Integer.valueOf(indexOpr).toString();
                tempids.add(new TempIden(result3,F.Value*T.Value));
                gen("*", F.value, T.value, result3, NXQ);
                T.value = result3;
                T.Value = nameToValue(result3);
                stackt.push(T.type);
                stackv.push(T.value);
                stackV.push(T.Value);
                indexOpn++;
            }
            case 79 -> {//T -> F / T
                JVarible = true;
                stackt.pop();
                stackv.pop();
                stackV.pop();
                F.type = stackt1.pop();
                F.value = stackv1.pop();
                F.Value = stackV1.pop();
                T.type = Tools.highType(T.type, F.type);
                indexOpr++;
                String result4 = "T" + Integer.valueOf(indexOpr).toString();
                gen("/", F.value, T.value, result4, NXQ);
                T.value = result4;
                T.Value = nameToValue(result4);
                stackt.push(T.type);
                stackv.push(T.value);
                stackV.push(T.Value);
                indexOpn++;
            }
            case 80 -> {//T -> F
                JVarible = true;
                T.type = F.type;
                T.value = F.value;
                T.Value = F.Value;
                stackt.push(T.type);
                stackv.push(T.value);
                stackV.push(T.Value);
                stackt1.pop();
                stackv1.pop();
                stackV1.pop();
            }
            case 81 -> {//F -> ( E )
                JVarible = true;
                F.value = E.value;
                F.type = E.type;
                stackt.pop();
                stackv.pop();
                stackt1.push(F.type);
                stackv1.push(F.value);
            }
            case 82 -> {//F -> digit
                JVarible = true;
                if (to.contains(".") && to.length() > 1) {
                    F.type = "float or double";
                } else {
                    F.type = "int";
                    if (!Character.isDigit(to.charAt(0))) {
                        F.type = "int36";
                    }
                }
                F.value = to;
                F.Value = Integer.parseInt(to);
                stackt1.push(F.type);
                stackv1.push(F.value);
                stackV1.push(F.Value);
                if (statefor.flag[0] && !statefor.flag[1]) {
                    statefor.flag[1] = true;
                }
            }
            case 83 -> {//F -> D
                JVarible = true;
                if (!tempVar1.equals("")) {
                    F.type = "int";
                    F.value = tempVar1;
                    tempVar1 = "";
                } else {
                    F.type = temp.type;
                    F.value = temp.name;
                    F.Value = nameToValue(temp.name);
                }
                stackt1.push(F.type);
                stackv1.push(F.value);
                stackV1.push(F.Value);
                for (Iden i : ids) {
                    if (F.value.equals(i.name)) {
                        i.flag2 = false;
                        i.flag2i--;
                    }
                }
            }
            default -> JVarible = true;
        }
        for(String str:addrs) {
            bw.write(str);
            bw.flush();
            bw.newLine();
        }
        System.out.println(addrs);
        bw.close();
    }
    private static boolean chargeIsAvailable(String s) {
        int d1 = s.indexOf(",");
        int d2 = s.indexOf(",",d1+1);
        int d3 = s.indexOf(",",d2+1);
        String r1 = s.substring(d1+1,d2);
        String r2 = s.substring(d2+1,d3);
        return NotInLoop(r1) && NotInLoop(r2);
    }
}
