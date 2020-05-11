package Operation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import Operation.Operation;

public class Division extends Operation {

    static String ch = "/";

    public Division(int n) {
        super(ch,n);
    }

    @Override
    public void operation() {
        // TODO Auto-generated method stub
        correctAnswer = op1 / op2;
        remainder = op1 % op2;
    }

    public String isCorrect()
    {
        if(usersAnswer == correctAnswer && remainder == usersRemainder)
            return "回答正确";
        else
            return "回答错误";
    }

    public String ptintQA()
    {
        operation();
        return "答案："+op1+" "+ch+" "+op2+" = "+correctAnswer+" "+remainder;
    }

    @Override
    public void isNumRight() {
        while(op2 == 0)
            getRanNum();
    }

    public void setRange(){
        minRange = 0;
        maxRange = maxInt;
    }

    public void writeToFile(File aFile)
    {
        try
        {
            PrintWriter out = new PrintWriter(new FileWriter(aFile,true));
            out.println("题目："+op1+" "+ch+" "+op2);
            out.println("你的答案："+usersAnswer+" "+usersRemainder + "    "+ "正确答案："+correctAnswer+" "+remainder);
            out.close();
        }catch(FileNotFoundException e){
            System.err.println("File not found!" );
        }catch(IOException e2){
            e2.printStackTrace();
        }       
    }
}
