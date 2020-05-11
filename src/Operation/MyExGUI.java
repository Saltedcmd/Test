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

    private JFrame mainWindow = new JFrame("0809�ӳ����������");

    //���
    private JPanel selectPanel = new JPanel();
    private JPanel mainPanel = new JPanel();
    private JPanel commandP = new JPanel();

    private JButton JBRedo = new JButton("����");
    private JButton JBStart = new JButton("��ʼ����");

    private JLabel JLUsersName = new JLabel("����������û�����");
    private JLabel JLChooseOp = new JLabel("��ѡ���������ͣ�");
    private JLabel JLNumberDigit = new JLabel("��ѡ������λ����");
    private JLabel JLBAnsTip = new JLabel("�����");
    private JLabel JLBRemainTip = new JLabel("����");

    private JTextField JTFUserName = new JTextField(8);//10�ĵ�λ����px ����ָ������
    private String[] operationType = {"+","-","*","/","���","һԪ����"};
    private String[] numberOfDigitType = {"1","2","3","4"};
    private JComboBox<String> JCBOperationSelect = new JComboBox<String>(operationType);//JComboBox ���� ��Ҫ����<E>
    private JComboBox<String> JCBNumberOfDigit = new JComboBox<String>(numberOfDigitType);

    //��ʾ��Ŀ��JLabel
    private JLabel[] JLBQuestions= new JLabel[10];
    //��ʾ��ȷ�𰸵�JLabel
    private JLabel[] JLBAnswers = new JLabel[10];
    //��ʾ�û����Ƿ���ȷ��JLabel
    private JLabel[] JLBIsTrue = new JLabel[10];

    private JTextField[] JTFUsersAnswer = new JTextField[10];//�������ʱ��Ҫ����ֵ����Ȼ����ֿ�ָ���쳣����
    private JTextField[] JTFRemainder = new JTextField[10];

    //����Font
    private Font buttonFont = new Font("΢���ź�",Font.PLAIN,16);
    private Font JLBFont = new Font("΢���ź�",Font.BOLD,18);
    private Font JTFFont = new Font("΢���ź�",Font.PLAIN,18);
    private Font JLBAnsFont = new Font("΢���ź�",Font.PLAIN,16);

    //����ΪOperation��questions���飬ֻ������ź�Operation��ȵ���Щ���������
    private Operation[] questions = new Operation[10];
    //�û�������
    private int[] userAnswer = new int[10];
    //�û���������
    private int[] remainder = new int[10];

    private int scores ,n = 1;
    private JLabel JLBScores = new JLabel("��ĳɼ�Ϊ:");
    private JButton JBOpenFile = new JButton("�鿴��¼");
    private String chara = "+";
    private File pFile = new File("���������¼");

    private int usedTime;
    boolean runFlag = false;//runFlagĬ��Ϊfalse
    private JPanel PTime = new JPanel();
    private JLabel JLBRemainTime = new JLabel("ʣ��ʱ�䣺");
    private JTextField JTFWtime = new JTextField("120");
    private JLabel JLBTime = new JLabel("��ʱ��");
    //LimitTime t = new LimitTime();//�̲߳�����������new

    //����ʱ�߳�
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
                    JOptionPane.showMessageDialog(jf,"������δ֪���⣬����������");
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
            printAnswer();//����ʱ�����������printAnswer()����
            JBStart.setText("��ʼ����");
            JLBTime.setText("��ʱ��"+usedTime);
        }
    }

    public MyExGUI(){

        //�����û���&ѡ�����
        selectPanel.setPreferredSize(new Dimension(700,50));
        //selectPanel.setLayout(new GridLayout(1,6,25,20));
        JLUsersName.setFont(JLBFont);
        selectPanel.add(JLUsersName);
        JTFUserName.setFont(JLBFont);
        selectPanel.add(JTFUserName);
        JLChooseOp.setFont(JLBFont);
        selectPanel.add(JLChooseOp);
        JCBOperationSelect.setPreferredSize(new Dimension(50,25));       //��selectPanel.setLayout�Ǿ����ʱ�������������Ч
        selectPanel.add(JCBOperationSelect);
        JLNumberDigit.setFont(JLBFont);
        selectPanel.add(JLNumberDigit);
        JCBNumberOfDigit.setPreferredSize(new Dimension(50,25));
        selectPanel.add(JCBNumberOfDigit); 

        //���������
        mainPanel.setPreferredSize(new Dimension(700,400));
        //mainPanel.setLayout(new GridLayout(10,3,5,10));
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints GBC = new GridBagConstraints();
        GBC.weightx = 1;//����������֮���ı���Ĵ�С��Ͳ���ʱ��ͬ ��Ϊ���������������仯����� �����������趨ֵ��800*500 ��˲�ͬ(�����趨��СΪĬ��ֵ)
        GBC.weighty = 1;
        //GBC.fill = GridBagConstraints.BOTH;//weightx������������Ĵ�С������С�仯������Ĵ�С��������֮�仯 Ҫʹ�����֮�仯��Ҫ������������λ�õ���䷽ʽ
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
            JLBQuestions[i] = new JLabel("�������ʼ���⡯��ʾ��Ŀ");
            JLBQuestions[i].setFont(JLBFont);
            JTFUsersAnswer[i] = new JTextField(5);                      //һ��Ҫ������ ��Ȼ����ֿ�ָ�����
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
            JLBQuestions[i] = new JLabel("�������ʼ���⡯��ʾ��Ŀ");
            JLBQuestions[i].setFont(JLBFont);
            JTFUsersAnswer[i] = new JTextField(5);                      //һ��Ҫ������ ��Ȼ����ֿ�ָ�����
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

        //�����������
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

        //ʹ������Ƕ����ķ�ʽע�Ὺʼ��ť���¼��������������
        JBStart.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(JBStart.getText().equals("��ʼ����"))
                        {
                            if(JTFUserName.getText().trim().equals(""))
                            {
                                JFrame nullNameWarning = new JFrame();
                                JOptionPane.showMessageDialog(nullNameWarning,"�������û���");//ȷ���û������û���
                            }
                            else{
                                start(); //�����ť�����������"��ʼ����"�������start()��������
                                JBStart.setText("�ύ��"); 
                                //����ʱ�߳̿�ʼ
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
                            runFlag = false;//��runFlag����Ϊfalse���߳̾ͻ᲻��ִ��whileѭ��������ݣ�
                            //printAnswer();//���ﲻ���ٵ���printWriter�����ˣ���Ϊ�߳��Ǳ߽�����ʱ���������е��á�
                            JLBTime.setText("��ʱ��"+usedTime);
                            JBStart.setText("��ʼ����");

                            /*int flag = 1;//�Ƿ����ÿ����
                            for(int i = 0;i < 10;i++)
                            {
                                if(JTFUsersAnswer[i].getText().equals(""))
                                {
                                    JFrame nullAns = new JFrame();
                                    JOptionPane.showMessageDialog(nullAns,"��ȷ�������ÿ����");
                                    flag = 0;//����Ŀû���
                                    break;
                                }
                            }
                            if(flag == 1)
                            {
                                printAnswer();//�����ť�����������"�ύ��"�������printAnswer()����
                                JBStart.setText("��ʼ����");
                            }*/
                            //ʹ�ü�ʱ���Ļ�����Ҫ���ÿ����

                        }
                    }
                }
        );

        //����������ť
        JBRedo.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(JBStart.getText().equals("��ʼ����"))//�����ύ�� ����Խ�������
                {
                    for(int i = 0;i < 10;i++)
                    {
                        JTFUsersAnswer[i].setText("");
                        JTFRemainder[i].setText("");
                        JLBAnswers[i].setText("");
                        JLBIsTrue[i].setText("");
                        JLBScores.setText("");
                    }
                    JLBTime.setText("��ʱ��");
                    LimitTime t = new LimitTime();
                    t.start();
                    JBStart.setText("�ύ��"); 
                }
                else//��δ�ύ ��������
                {
                    JFrame notSubmit = new JFrame();
                    JOptionPane.showMessageDialog(notSubmit,"�ύ��ſ����������ύǰ����ֱ�Ӹ��Ĵ𰸣�");
                }
            }
        });

        //�鿴���������¼�İ�ť������
        JBOpenFile.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){

                if(JTFUserName.getText().trim().equals(""))
                {
                    JFrame nullNameWarning = new JFrame();
                    JOptionPane.showMessageDialog(nullNameWarning,"�������û���");//ȷ���û������û���
                }
                else{
                    //һ�㲻��ʵ����һ��Runtime����Ӧ�ó���Ҳ���ܴ����Լ���Runtime ��ʵ����������ͨ��getRuntime ������ȡ��ǰRuntime����ʱ��������á�һ���õ���һ����ǰ��Runtime��������ã��Ϳ��Ե���Runtime����ķ���ȥ����Java�������״̬����Ϊ��
                     Runtime ce=Runtime.getRuntime();
                     pFile.mkdirs();
                     String filename = JTFUserName.getText()+".his";
                     File aUserRec = new File(pFile,filename);
                     if(aUserRec.exists())
                     {
                         try{
                            //ce.exec("cmd   /c   start  "+aUserRec.getAbsolutePath());//�����ǲ��ܴ򿪵� ��Ϊû�ж����ܴ�.his�ļ� ������������Ӧ���̵�
                         ce.exec("notepad.exe "+aUserRec.getAbsolutePath());
                         }catch(IOException exc){
                             exc.printStackTrace();
                         }
                     }else{
                     JFrame nullFileWarning = new JFrame();
                     JOptionPane.showMessageDialog(nullFileWarning,"���û����޼�¼!");
                     }
                }   
            }
        });

        //����������������ö��ŵ����
        mainWindow.add(selectPanel,BorderLayout.NORTH);
        mainWindow.add(mainPanel,BorderLayout.CENTER);
        mainWindow.add(commandP, BorderLayout.SOUTH);
        mainWindow.pack();
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setSize(800,500);//���ô����С
        mainWindow.setLocationRelativeTo(null);//������������Ļ�м�
        mainWindow.setVisible(true);//����Ϊ�ɼ� Ҫ������� ����ǰ����ֻ�ܿ����û�����ѡ����� ��������Ҫ�϶����ڴ�С���ܿ���
    }

    public void start(){
        //���TextField�ʹ𰸱�ǩ������
        for(int i = 0;i < 10;i++)
        {
            JTFUsersAnswer[i].setText("");
            JTFRemainder[i].setText("");
            JLBAnswers[i].setText("");
            JLBIsTrue[i].setText("");
            JLBScores.setText("");
            JLBTime.setText("��ʱ��");
        }

        //��ȡComboBox��ѡ��ֵ
        chara = (String) JCBOperationSelect.getSelectedItem();
        n = Integer.valueOf((String)JCBNumberOfDigit.getSelectedItem());

         //����ѡ����������
        int flag = 0;
        if(chara.equals("���")) {
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
                    JOptionPane.showMessageDialog(jf,"����δ֪��������������");
            }
        }
    }

    //���������ʾÿ�����ȷ�𰸡��÷ֺ���ʱ�����ҽ�ÿ������ļ�¼д���ļ�
    public void printAnswer()
    {
        //�ɼ���ʼֵΪ100
        scores = 100;

        //����ÿ����
        for(int i = 0; i < 10;i++)
        {
            //���û��Ĵ���һ���鸳ֵ��getText�Ľ��ΪString��
            userAnswer[i] = Integer.valueOf(JTFUsersAnswer[i].getText());

            //���û������������Ĭ���û���Ϊ����Ϊ0�������������鸳ֵ
            if(JTFRemainder[i].getText().equals(""))
            {
                remainder[i] = 0;
            }

            //�������û�����������������鸳ֵ
            else
            {
                remainder[i] = Integer.valueOf(JTFRemainder[i].getText());
            }


            //questions��������operation���ô𰸺����������������questions������鸳ֵ
            questions[i].setUsersAnswer(userAnswer[i],remainder[i]);

            //�������ַֿ��Ƿ��������������Ǵ���ģ���Ϊ������Ϊ���ʱchara��ֵ�ǿ���ʮ��ģ���ʮ���chara�Ḳ�ǵ�ǰ��ģ��� ��һ����û�������ͻᱨ��
            /*if(chara.equals("/"))
            {

                remainder[i] = Integer.valueOf(JTFRemainder[i].getText());
                questions[i].setUsersAnswer(userAnswer[i],remainder[i]);
            }
            else
            {
                questions[i].setUsersAnswer(userAnswer[i]);
            }*/

            //ʹ��ȷ����ʾ�������
            JLBAnswers[i].setText(questions[i].ptintQA());

            //���������ʾ���Ƿ���ȷ
            JLBIsTrue[i].setText(questions[i].isCorrect());

            //��������򽫴𰸺��Ƿ���ȷ������ǩ��������ɫ����Ϊ��ɫ
            if(JLBIsTrue[i].getText().equals("�ش����"))
            {
                JLBAnswers[i].setForeground(Color.RED);
                JLBIsTrue[i].setForeground(Color.RED);
                scores-=10;
            }
            //��ȷΪ��ɫ
            else
            {
                JLBAnswers[i].setForeground(Color.BLACK);
                JLBIsTrue[i].setForeground(Color.BLACK);
            }
        }
        //��ʾ�ɼ�
        JLBScores.setText("��ĳɼ�Ϊ��"+ scores);

        //�����û��ļ�
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
                JOptionPane.showMessageDialog(jf,"�û��ļ�����ʧ��");
            }   
        }

        //��ÿ�������ȷ�𰸺��û���д���ļ�
        for(int i = 0;i < 10;i++)
        {
            questions[i].writeToFile(aUserRec);
        }

        //���÷ֺ���ʱд���ļ�
        try
        {
            PrintWriter out = new PrintWriter(new FileWriter(aUserRec,true));
            out.println("");
            out.println("��˴εĵ÷��ǣ�"+scores+"    "+"����ʱ��Ϊ��"+usedTime+"��");
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