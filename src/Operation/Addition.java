package Operation;

import Operation.Operation;

public class Addition extends Operation {

    static String ch = "+";

    public Addition(int n) {
        super(ch,n);
    }

    @Override
    public void operation() {
        correctAnswer = op1 + op2;
    }

    public void isNumRight(){}

    public void setRange(){
        minRange = 0;
        maxRange = maxInt + maxInt;
    }
}
