import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;//emptyborder
import java.awt.event.*;//actionlistener
import java.util.*;//calendar
import java.text.*;//date
import java.net.*;
import java.io.*;//datainputstream

public class Server implements ActionListener{

    JTextField text;
    JPanel p2;

    static Box vertical=Box.createVerticalBox();

    static JFrame f=new JFrame();

    static DataOutputStream dout;

    Server(){ 
        f.setLayout(null);


        JPanel p1=new JPanel();
        p1.setBackground(new Color(7,94,84));
        p1.setBounds(0,0,450,70);
        p1.setLayout(null);
        f.add(p1);



        //arrow image
        ImageIcon i1=new ImageIcon(ClassLoader.getSystemResource("icons/3.png"));
        Image i2=i1.getImage().getScaledInstance(25,25,Image.SCALE_DEFAULT);
        ImageIcon i3=new ImageIcon(i2);
        JLabel back=new JLabel(i3);
        back.setBounds(5,20,25,25);
        p1.add(back);

        //arrow click->close the frame
        back.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent ae){
                System.exit(0);
                //setVisible(false);
            }
        });


        //add profile picture
        ImageIcon i4=new ImageIcon(ClassLoader.getSystemResource("icons/1.png"));
        Image i5=i4.getImage().getScaledInstance(50,50,Image.SCALE_DEFAULT);
        ImageIcon i6=new ImageIcon(i5);
        JLabel profile=new JLabel(i6);
        profile.setBounds(40,10,50,50);
        p1.add(profile);


        //add video call pic
        ImageIcon i7=new ImageIcon(ClassLoader.getSystemResource("icons/video.png"));
        Image i8=i7.getImage().getScaledInstance(30,30,Image.SCALE_DEFAULT);
        ImageIcon i9=new ImageIcon(i8);
        JLabel video=new JLabel(i9);
        video.setBounds(300,20,30,30);
        p1.add(video);



        //add telephone pic
        ImageIcon i10=new ImageIcon(ClassLoader.getSystemResource("icons/phone.png"));
        Image i11=i10.getImage().getScaledInstance(35,30,Image.SCALE_DEFAULT);
        ImageIcon i12=new ImageIcon(i11);
        JLabel phone=new JLabel(i12);
        phone.setBounds(360,20,35,30);
        p1.add(phone);



        //add 3icon
        ImageIcon i13=new ImageIcon(ClassLoader.getSystemResource("icons/3icon.png"));
        Image i14=i13.getImage().getScaledInstance(10,25,Image.SCALE_DEFAULT);
        ImageIcon i15=new ImageIcon(i14);
        JLabel morvert=new JLabel(i15);
        morvert.setBounds(420,20,10,25);
        p1.add(morvert);


        //add name of profile
        JLabel name=new JLabel("Chintu");
        name.setBounds(110,15,100,18);
        name.setForeground(Color.WHITE);
        name.setFont(new Font("SAN_SERIF",Font.BOLD,18));
        p1.add(name);


        //add status
        JLabel status=new JLabel("Active Now");
        status.setBounds(110,35,100,18);
        status.setForeground(Color.WHITE);
        status.setFont(new Font("SAN_SERIF",Font.PLAIN,14));
        p1.add(status);



        p2=new JPanel();
        p2.setBounds(5,75,440,570);
        f.add(p2);



        //add textbox
        text=new JTextField();
        text.setBounds(5,655,310,40);
        text.setFont(new Font("SAN_SERIF",Font.PLAIN,16));
        f.add(text);



        //add send button
        JButton send=new JButton("Send");
        send.setBounds(320,655,123,40);
        send.setBackground(new Color(7,94,84));
        send.setForeground(Color.WHITE);
        send.setFont(new Font("SAN_SERIF",Font.PLAIN,16));
        send.addActionListener(this);
        f.add(send);



        f.setSize(450,700);
        f.setLocation(200,50);
        f.setUndecorated(true);
        f.getContentPane().setBackground(Color.WHITE);
        f.setVisible(true);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent ae){

        try{

        String out=text.getText();

       // JLabel output=new JLabel(out); no need now
       //JPanel p3=new JPanel();

        JPanel p3= formatLabel(out);//alternative of above
        //p3.add(output); no need now

        p2.setLayout(new BorderLayout());

        //message align in right side
        JPanel right=new JPanel(new BorderLayout());
        right.add(p3, BorderLayout.LINE_END);

        //if in case of multiple cases,vertically one by one it appears
        vertical.add(right);


        //distance between two verticals
        vertical.add(Box.createVerticalStrut(15));

        p2.add(vertical,BorderLayout.PAGE_START);

        dout.writeUTF(out);

        text.setText("");//after sending,textbox should be empty


        f.repaint();
        f.invalidate();
        f.validate();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public static JPanel formatLabel(String out){
        JPanel panel=new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));


        //JLabel output=new Jlabel("<html><p style=\"width: 150px\">" + out + "</p><html>"); for fixed size outline
        JLabel output=new JLabel(out);
        output.setFont(new Font("Tahoma",Font.PLAIN,16));
        output.setBackground(new Color(37,211,102));
        output.setOpaque(true);
        output.setBorder(new EmptyBorder(15,15,15,50));

        panel.add(output);

        //to show time
        Calendar cal=Calendar.getInstance();
        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");

        JLabel time=new JLabel();
        //for dynamic value set(calculate)
        time.setText(sdf.format(cal.getTime()));

        panel.add(time);


        return panel;
    }
    public static void main(String[] args) {
        new Server();

        try{
            try (ServerSocket skt = new ServerSocket(6001)) {
                //can send & also receive message.
                //infinitely message accept.
                while(true){
                    Socket s=skt.accept();
                    DataInputStream din=new DataInputStream(s.getInputStream());
                    dout=new DataOutputStream(s.getOutputStream());

                    while(true){
                        String msg=din.readUTF();//msg send by client to server 
                        JPanel panel=formatLabel(msg);

                        JPanel left=new JPanel(new BorderLayout());
                        left.add(panel,BorderLayout.LINE_START);
                        vertical.add(left);
                        f.validate();//to extend this non static method,have to create jframe object instead of extending it.
                    }
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
//validate()&vertical.add() is non static ,so we can't call it from static function