package Operation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public abstract class Operation {
    protected int op1,op2,op3,remainder,usersRemainder,n,correctAnswer,usersAnswer,maxInt=1;
    protected String ch;
    protected long minRange,maxRange;

    public Operation(String ch,int n) {
        super();
        this.ch = ch;
        this.n = n;
    }

    public abstract void operation();
    public abstract void isNumRight();
    public abstract void setRange();

    protected void getRanNum()
    {
        op1 = (int)(Math.random()*Math.pow(10,n));
        op2 = (int)(Math.random()*Math.pow(10,n));
        op3 = (int)(Math.random()*Math.pow(10,n));
    }

    public void setUsersAnswer(int usersAnswer,int usersRemainder) //throws Exception
    {
        /*setRange();
        if(usersAnswer < minRange || usersAnswer > maxRange)
            throw new NumberTooBigException("答案范围应为"+minRange+"到"+maxRange);*/
        this.usersAnswer = usersAnswer;
        this.usersRemainder = usersRemainder;
    }

    public void setUsersAnswer(int usersAnswer) //throws Exception
    {
        setUsersAnswer(usersAnswer,0);
    }

    public String isCorrect()
    {
        if(usersAnswer == correctAnswer)
            return "回答正确";
        else
            return "回答错误";
    }

    public String printQuestion()
    {
        getRanNum();
        isNumRight();
        return op1+" "+ch+" "+op2+" =";
    }

    public String ptintQA() 
    {
        operation();
        return "答案："+op1+" "+ch+" "+op2+" = "+correctAnswer;
    }

    public void writeToFile(File aFile)
    {
        try
        {
            PrintWriter out = new PrintWriter(new FileWriter(aFile,true));
            out.println("题目："+op1+" "+ch+" "+op2);
            out.println("你的答案："+usersAnswer + "    "+ "正确答案："+correctAnswer);
            out.close();
        }catch(FileNotFoundException e){
            System.err.println("File not found!" );
        }catch(IOException e2){
            e2.printStackTrace();
        }       
    }
}
