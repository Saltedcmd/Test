package Operation;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import Operation.Operation;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class MyExGUI {

    private JFrame mainWindow = new JFrame("0809队出题做题软件");

    //面板
    private JPanel selectPanel = new JPanel();
    private JPanel mainPanel = new JPanel();
    private JPanel commandP = new JPanel();

    private JButton JBRedo = new JButton("重做");
    private JButton JBStart = new JButton("开始做题");

    private JLabel JLUsersName = new JLabel("请输入你的用户名：");
    private JLabel JLChooseOp = new JLabel("请选择运算类型：");
    private JLabel JLNumberDigit = new JLabel("请选择运算位数：");
    private JLabel JLBAnsTip = new JLabel("输入答案");
    private JLabel JLBRemainTip = new JLabel("余数");

    private JTextField JTFUserName = new JTextField(8);//10的单位不是px 而是指定列数
    private String[] operationType = {"+","-","*","/","混合","一元方程"};
    private String[] numberOfDigitType = {"1","2","3","4"};
    private JComboBox<String> JCBOperationSelect = new JComboBox<String>(operationType);//JComboBox 泛型 需要加上<E>
    private JComboBox<String> JCBNumberOfDigit = new JComboBox<String>(numberOfDigitType);

    //显示题目的JLabel
    private JLabel[] JLBQuestions= new JLabel[10];
    //显示正确答案的JLabel
    private JLabel[] JLBAnswers = new JLabel[10];
    //显示用户答案是否正确的JLabel
    private JLabel[] JLBIsTrue = new JLabel[10];

    private JTextField[] JTFUsersAnswer = new JTextField[10];//定义变量时需要赋初值，不然会出现空指针异常问题
    private JTextField[] JTFRemainder = new JTextField[10];

    //设置Font
    private Font buttonFont = new Font("微软雅黑",Font.PLAIN,16);
    private Font JLBFont = new Font("微软雅黑",Font.BOLD,18);
    private Font JTFFont = new Font("微软雅黑",Font.PLAIN,18);
    private Font JLBAnsFont = new Font("微软雅黑",Font.PLAIN,16);

    //类型为Operation的questions数组，只有这个才和Operation类等等那些类关联起来
    private Operation[] questions = new Operation[10];
    //用户答案数组
    private int[] userAnswer = new int[10];
    //用户余数数组
    private int[] remainder = new int[10];

    private int scores ,n = 1;
    private JLabel JLBScores = new JLabel("你的成绩为:");
    private JButton JBOpenFile = new JButton("查看记录");
    private String chara = "+";
    private File pFile = new File("四则运算记录");

    private int usedTime;
    boolean runFlag = false;//runFlag默认为false
    private JPanel PTime = new JPanel();
    private JLabel JLBRemainTime = new JLabel("剩余时间：");
    private JTextField JTFWtime = new JTextField("120");
    private JLabel JLBTime = new JLabel("用时：");
    //LimitTime t = new LimitTime();//线程不可以在这里new

    //倒计时线程
    class LimitTime extends Thread{
        public void run()
        {
            runFlag = true;
            int i = 120;
            usedTime = 0;
            while(runFlag && i >= 0)
            {
                JTFWtime.setText(""+i);
                try {
                    sleep(1000);
                    usedTime++;
                } catch (InterruptedException e) {
                    JFrame jf = new JFrame();
                    JOptionPane.showMessageDialog(jf,"出现了未知问题，请重启程序");
                }
                i--;
            }
            //runFlag = false;
            for(int j = 0;j < 10;j++)
            {
                if(JTFUsersAnswer[j].getText().equals(""))
                {
                    JTFUsersAnswer[j].setText("0");
                }
            }
            printAnswer();//倒计时结束，则调用printAnswer()方法
            JBStart.setText("开始做题");
            JLBTime.setText("用时："+usedTime);
        }
    }

    public MyExGUI(){

        //布局用户名&选择面板
        selectPanel.setPreferredSize(new Dimension(700,50));
        //selectPanel.setLayout(new GridLayout(1,6,25,20));
        JLUsersName.setFont(JLBFont);
        selectPanel.add(JLUsersName);
        JTFUserName.setFont(JLBFont);
        selectPanel.add(JTFUserName);
        JLChooseOp.setFont(JLBFont);
        selectPanel.add(JLChooseOp);
        JCBOperationSelect.setPreferredSize(new Dimension(50,25));       //当selectPanel.setLayout那句存在时，这里的设置无效
        selectPanel.add(JCBOperationSelect);
        JLNumberDigit.setFont(JLBFont);
        selectPanel.add(JLNumberDigit);
        JCBNumberOfDigit.setPreferredSize(new Dimension(50,25));
        selectPanel.add(JCBNumberOfDigit); 

        //布局主面板
        mainPanel.setPreferredSize(new Dimension(700,400));
        //mainPanel.setLayout(new GridLayout(10,3,5,10));
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints GBC = new GridBagConstraints();
        GBC.weightx = 1;//加上这两行之后文本框的大小会和不加时不同 因为它描述的是随面板变化的情况 而现在面板的设定值是800*500 因此不同(不以设定大小为默认值)
        GBC.weighty = 1;
        //GBC.fill = GridBagConstraints.BOTH;//weightx描述的是网格的大小随面板大小变化，组件的大小并不会随之变化 要使组件随之变化需要控制它对所在位置的填充方式
        //GBC.insets = new Insets(1,1,2,2);
        GBC.gridx = 1;
        GBC.gridy = 0;
        GBC.anchor = GridBagConstraints.WEST;
        gridbag.setConstraints(JLBAnsTip, GBC);
        JLBAnsTip.setFont(JLBFont);
        mainPanel.add(JLBAnsTip);

        GBC.gridx = 2;
        gridbag.setConstraints(JLBRemainTip, GBC);
        JLBRemainTip.setFont(JLBFont);
        mainPanel.add(JLBRemainTip);

        GBC.gridx = 4;
        GBC.gridwidth = 2;
        GBC.anchor = GridBagConstraints.CENTER;
        gridbag.setConstraints(JLBScores, GBC);
        JLBScores.setFont(JLBFont);
        mainPanel.add(JLBScores);

        for(int i = 0;i < 5;i++)
        {
            JLBQuestions[i] = new JLabel("点击‘开始做题’显示题目");
            JLBQuestions[i].setFont(JLBFont);
            JTFUsersAnswer[i] = new JTextField(5);                      //一定要加这行 不然会出现空指针错误
            JTFUsersAnswer[i].setFont(JTFFont);
            JTFRemainder[i] = new JTextField(3);
            JTFRemainder[i].setFont(JTFFont);
            JLBAnswers[i] = new JLabel("");
            JLBAnswers[i].setFont(JLBAnsFont);
            JLBIsTrue[i] = new JLabel("");
            JLBIsTrue[i].setFont(JLBAnsFont);

            //gridbag.setConstraints(JLBQuestions[i],new GridBagConstraints(i,0,5,10,1,1,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0));
            //gridbag.setConstraints(JTFUsersAnswer[i],new GridBagConstraints(i,1,5,10,1,1,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0));
            //gridbag.setConstraints(JTFRemainder[i],new GridBagConstraints(i,2,5,10,1,1,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0));
            GBC.gridwidth = 1;
            GBC.gridx = 0;
            GBC.gridy = 2*i+1;
            GBC.anchor = GridBagConstraints.EAST;
            gridbag.setConstraints(JLBQuestions[i], GBC);
            mainPanel.add(JLBQuestions[i]);
            GBC.anchor = GridBagConstraints.CENTER;
            GBC.gridy = 2*i+2;
            GBC.gridwidth = 2;
            gridbag.setConstraints(JLBAnswers[i], GBC);
            mainPanel.add(JLBAnswers[i]);

            GBC.gridwidth = 1;
            GBC.gridx = 1;
            GBC.gridy = 2*i+1;
            GBC.anchor = GridBagConstraints.WEST;
            gridbag.setConstraints(JTFUsersAnswer[i],GBC);
            mainPanel.add(JTFUsersAnswer[i]);

            GBC.gridx = 2;
            gridbag.setConstraints(JTFRemainder[i],GBC);
            mainPanel.add(JTFRemainder[i]);
            GBC.gridy = 2*i+2;
            gridbag.setConstraints(JLBIsTrue[i], GBC);
            mainPanel.add(JLBIsTrue[i]);
        }

        for(int i = 5;i < 10;i++)
        {
            JLBQuestions[i] = new JLabel("点击‘开始做题’显示题目");
            JLBQuestions[i].setFont(JLBFont);
            JTFUsersAnswer[i] = new JTextField(5);                      //一定要加这行 不然会出现空指针错误
            JTFUsersAnswer[i].setFont(JTFFont);
            JTFRemainder[i] = new JTextField(3);
            JTFRemainder[i].setFont(JTFFont);
            JLBAnswers[i] = new JLabel("");
            JLBAnswers[i].setFont(JLBAnsFont);
            JLBIsTrue[i] = new JLabel("");
            JLBIsTrue[i].setFont(JLBAnsFont);

            GBC.gridx = 4;
            GBC.gridy = 2*i-9;
            GBC.anchor = GridBagConstraints.EAST;
            gridbag.setConstraints(JLBQuestions[i], GBC);
            mainPanel.add(JLBQuestions[i]);
            GBC.anchor = GridBagConstraints.CENTER;
            GBC.gridy = 2*i-8;
            GBC.gridwidth = 2;
            gridbag.setConstraints(JLBAnswers[i], GBC);
            mainPanel.add(JLBAnswers[i]);

            GBC.gridwidth = 1;
            GBC.gridx = 5;
            GBC.gridy = 2*i-9;
            GBC.anchor = GridBagConstraints.WEST;
            gridbag.setConstraints(JTFUsersAnswer[i],GBC);
            mainPanel.add(JTFUsersAnswer[i]);

            GBC.gridx = 6;
            gridbag.setConstraints(JTFRemainder[i],GBC);
            mainPanel.add(JTFRemainder[i]);
            GBC.gridy = 2*i-8;
            gridbag.setConstraints(JLBIsTrue[i], GBC);
            mainPanel.add(JLBIsTrue[i]);

        }
        mainPanel.setLayout(gridbag);

        //布局命令面板
        commandP.setLayout(new FlowLayout(FlowLayout.CENTER,60,20)); 
        JLBRemainTime.setFont(JLBFont);
        JLBTime.setFont(JLBFont);
        JTFWtime.setFont(JTFFont);
        PTime.setLayout(new FlowLayout(FlowLayout.LEFT,10,20));
        PTime.add(JLBRemainTime);
        PTime.add(JTFWtime);
        PTime.add(JLBTime);
        commandP.add(PTime);
        JBStart.setFont(buttonFont);
        commandP.add(JBStart);
        JBRedo.setFont(buttonFont);
        commandP.add(JBRedo);
        JBOpenFile.setFont(buttonFont);
        commandP.add(JBOpenFile);

        //使用匿名嵌套类的方式注册开始按钮的事件处理监听器对象
        JBStart.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(JBStart.getText().equals("开始做题"))
                        {
                            if(JTFUserName.getText().trim().equals(""))
                            {
                                JFrame nullNameWarning = new JFrame();
                                JOptionPane.showMessageDialog(nullNameWarning,"请输入用户名");//确保用户输入用户名
                            }
                            else{
                                start(); //如果按钮上面的文字是"开始做题"，则调用start()方法出题
                                JBStart.setText("提交答案"); 
                                //倒计时线程开始
                                LimitTime t = new LimitTime();
                                t.start();
                            } 
                        }
                        else
                        {
                            for(int i = 0;i < 10;i++)
                            {
                                if(JTFUsersAnswer[i].getText().equals(""))
                                {
                                    JTFUsersAnswer[i].setText("0");
                                }
                            }
                            runFlag = false;//将runFlag设置为false（线程就会不再执行while循环里的内容）
                            //printAnswer();//这里不用再调用printWriter方法了，因为线程那边结束的时候会对它进行调用。
                            JLBTime.setText("用时："+usedTime);
                            JBStart.setText("开始做题");

                            /*int flag = 1;//是否完成每道题
                            for(int i = 0;i < 10;i++)
                            {
                                if(JTFUsersAnswer[i].getText().equals(""))
                                {
                                    JFrame nullAns = new JFrame();
                                    JOptionPane.showMessageDialog(nullAns,"请确保已完成每道题");
                                    flag = 0;//有题目没完成
                                    break;
                                }
                            }
                            if(flag == 1)
                            {
                                printAnswer();//如果按钮上面的文字是"提交答案"，则调用printAnswer()方法
                                JBStart.setText("开始做题");
                            }*/
                            //使用计时器的话不需要完成每道题

                        }
                    }
                }
        );

        //监听重做按钮
        JBRedo.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(JBStart.getText().equals("开始做题"))//若已提交答案 则可以进行重做
                {
                    for(int i = 0;i < 10;i++)
                    {
                        JTFUsersAnswer[i].setText("");
                        JTFRemainder[i].setText("");
                        JLBAnswers[i].setText("");
                        JLBIsTrue[i].setText("");
                        JLBScores.setText("");
                    }
                    JLBTime.setText("用时：");
                    LimitTime t = new LimitTime();
                    t.start();
                    JBStart.setText("提交答案"); 
                }
                else//答案未提交 不能重做
                {
                    JFrame notSubmit = new JFrame();
                    JOptionPane.showMessageDialog(notSubmit,"提交后才可以重做！提交前可以直接更改答案！");
                }
            }
        });

        //查看以往做题记录的按钮监听器
        JBOpenFile.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){

                if(JTFUserName.getText().trim().equals(""))
                {
                    JFrame nullNameWarning = new JFrame();
                    JOptionPane.showMessageDialog(nullNameWarning,"请输入用户名");//确保用户输入用户名
                }
                else{
                    //一般不能实例化一个Runtime对象，应用程序也不能创建自己的Runtime 类实例，但可以通过getRuntime 方法获取当前Runtime运行时对象的引用。一旦得到了一个当前的Runtime对象的引用，就可以调用Runtime对象的方法去控制Java虚拟机的状态和行为。
                     Runtime ce=Runtime.getRuntime();
                     pFile.mkdirs();
                     String filename = JTFUserName.getText()+".his";
                     File aUserRec = new File(pFile,filename);
                     if(aUserRec.exists())
                     {
                         try{
                            //ce.exec("cmd   /c   start  "+aUserRec.getAbsolutePath());//这样是不能打开的 因为没有东西能打开.his文件 会跳出来搜索应用商店
                         ce.exec("notepad.exe "+aUserRec.getAbsolutePath());
                         }catch(IOException exc){
                             exc.printStackTrace();
                         }
                     }else{
                     JFrame nullFileWarning = new JFrame();
                     JOptionPane.showMessageDialog(nullFileWarning,"该用户暂无记录!");
                     }
                }   
            }
        });

        //尽量把主窗体的设置都放到最后
        mainWindow.add(selectPanel,BorderLayout.NORTH);
        mainWindow.add(mainPanel,BorderLayout.CENTER);
        mainWindow.add(commandP, BorderLayout.SOUTH);
        mainWindow.pack();
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setSize(800,500);//设置窗体大小
        mainWindow.setLocationRelativeTo(null);//将窗口置于屏幕中间
        mainWindow.setVisible(true);//设置为可见 要放在最后 放在前面则只能看见用户名和选择面板 主面板等需要拖动窗口大小才能看见
    }

    public void start(){
        //清除TextField和答案标签的内容
        for(int i = 0;i < 10;i++)
        {
            JTFUsersAnswer[i].setText("");
            JTFRemainder[i].setText("");
            JLBAnswers[i].setText("");
            JLBIsTrue[i].setText("");
            JLBScores.setText("");
            JLBTime.setText("用时：");
        }

        //获取ComboBox的选中值
        chara = (String) JCBOperationSelect.getSelectedItem();
        n = Integer.valueOf((String)JCBNumberOfDigit.getSelectedItem());

         //根据选择的运算出题
        int flag = 0;
        if(chara.equals("混合")) {
            flag = 1;
        }
        for(int i = 0;i < 10;i++)
        {
            if(flag == 1)
            {
                int tempCh = (int)(Math.random()*4+1);
                switch(tempCh)
                {
                case 1:
                    chara = "+";
                    break;
                case 2:
                    chara = "-";
                    break;
                case 3:
                    chara = "*";
                    break;
                case 4:
                    chara = "/";
                    break;
                }
            }

            switch(chara)
            {
             case "+":
                 questions[i] = new Addition(n);
                 JLBQuestions[i].setText(questions[i].printQuestion());
                 break;
             case "-":
                 questions[i] = new Subtraction(n);
                 JLBQuestions[i].setText(questions[i].printQuestion());
                 break;
             case "*":
                 questions[i] = new Multiplication(n);
                 JLBQuestions[i].setText(questions[i].printQuestion());
                 break;
             case "/":
                 questions[i] = new Division(n);
                 JLBQuestions[i].setText(questions[i].printQuestion());
                 break;
             default:
                    JFrame jf = new JFrame();
                    JOptionPane.showMessageDialog(jf,"出现未知错误，请重启程序。");
            }
        }
    }

    //在面板上显示每题的正确答案、得分和用时，并且将每次做题的记录写入文件
    public void printAnswer()
    {
        //成绩初始值为100
        scores = 100;

        //对于每道题
        for(int i = 0; i < 10;i++)
        {
            //给用户的答案这一数组赋值（getText的结果为String）
            userAnswer[i] = Integer.valueOf(JTFUsersAnswer[i].getText());

            //如果没有填余数，则默认用户认为余数为0，并给余数数组赋值
            if(JTFRemainder[i].getText().equals(""))
            {
                remainder[i] = 0;
            }

            //否则用用户填的余数给余数数组赋值
            else
            {
                remainder[i] = Integer.valueOf(JTFRemainder[i].getText());
            }


            //questions的类型是operation，用答案和余数这两个数组给questions这个数组赋值
            questions[i].setUsersAnswer(userAnswer[i],remainder[i]);

            //以下这种分开是否有余数的做法是错误的，因为在运算为混合时chara的值是看第十题的（第十题的chara会覆盖掉前面的）。 万一除法没有余数就会报错
            /*if(chara.equals("/"))
            {

                remainder[i] = Integer.valueOf(JTFRemainder[i].getText());
                questions[i].setUsersAnswer(userAnswer[i],remainder[i]);
            }
            else
            {
                questions[i].setUsersAnswer(userAnswer[i]);
            }*/

            //使正确答案显示在面板上
            JLBAnswers[i].setText(questions[i].ptintQA());

            //在面板上显示答案是否正确
            JLBIsTrue[i].setText(questions[i].isCorrect());

            //如果错误则将答案和是否正确两个标签的字体颜色设置为红色
            if(JLBIsTrue[i].getText().equals("回答错误"))
            {
                JLBAnswers[i].setForeground(Color.RED);
                JLBIsTrue[i].setForeground(Color.RED);
                scores-=10;
            }
            //正确为黑色
            else
            {
                JLBAnswers[i].setForeground(Color.BLACK);
                JLBIsTrue[i].setForeground(Color.BLACK);
            }
        }
        //显示成绩
        JLBScores.setText("你的成绩为："+ scores);

        //创建用户文件
        pFile.mkdirs();
        String filename = JTFUserName.getText()+".his";
        File aUserRec = new File(pFile,filename);
        if(! (aUserRec.exists()))
        {
            try{
                aUserRec.createNewFile();
            }catch(Exception e){
                e.printStackTrace();
                JFrame jf = new JFrame();
                JOptionPane.showMessageDialog(jf,"用户文件创建失败");
            }   
        }

        //将每道题的正确答案和用户答案写入文件
        for(int i = 0;i < 10;i++)
        {
            questions[i].writeToFile(aUserRec);
        }

        //将得分和用时写入文件
        try
        {
            PrintWriter out = new PrintWriter(new FileWriter(aUserRec,true));
            out.println("");
            out.println("你此次的得分是："+scores+"    "+"所用时间为："+usedTime+"秒");
            out.println("");
            out.println("");
            out.close();
        }catch(FileNotFoundException e){
            System.err.println("File not found!" );
        }catch(IOException e2){
            e2.printStackTrace();
        }       
    }   
}