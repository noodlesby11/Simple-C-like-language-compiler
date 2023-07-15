# Simple-C-like-language-compiler
简易的类C语言编译器可以从源程序翻译至汇编程序
# 说明

该项目参考至：[ 广西大学课程设计，使用java编写的c语言编译原理玩具，翻译到简单汇编指令。 (github.com)](https://github.com/arrowbingo/Curriculum-design-of-Compilers-Principles)
并作出了许多修改：

* 修改了部分词法和语法规则以及分析表
* 改变了语法规则和分析表的读取规则不需要从excel文件中读取
* 补充了未编写的语义规则
* 修改了部分bug

在use 文件夹中存放根据LR1语法规则生成分析表的程序，参考至：[LR(1)语法分析器生成器](https://www.cnblogs.com/vizdl/p/11331278.html)

# 文件说明

Lexer.java:词法分析程序，接受来自input.txt的数据，输出词法分析结果到Lexer.txt

Lexer.txt:存放词法分析的结果.

LR1.java:语法分析程序，调用Semantics.java完成语义分析。

Lr1Table.java：存储语法和分析表，供LR1.java调用

SemanticOutput.txt：存储最后生成的四元式。

Semantics.java：语义分析程序，由LR1.java调用.

Tool.java:存放一些语义分析需要的工具类。

Yufa2.txt：存放语法表达式。

词法对照表：存放词法对照表

input.txt：存放源程序

# 使用说明

1. 将源程序写在input.txt
2. 运行Lexer.java可在Lexer.txt查看结果
3. 运行LR1.java可以在SemanticOutput.txt查看结果
4. 运行Complie.java可以在控制台上查看结果
