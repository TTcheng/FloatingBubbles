
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyWindow extends JFrame implements ActionListener, KeyListener {
    JTextField inputField;//输入框
    JButton confirmBtn;//确认按钮
    JPanel panel;

    private Circle[] circles;//圆
    private boolean solid = false;//false for 空心，true for 实心
    private long freshMillis = 10;//默认10毫秒，可调用setter更改

    public MyWindow() throws HeadlessException {
        JLabel label = new JLabel("输入一个数字(1-20)：");
        inputField = new JTextField(20);
        confirmBtn = new JButton("确定");
        confirmBtn.addActionListener(this);
        panel = new JPanel();

        panel.add(label);
        panel.add(inputField);
        panel.add(confirmBtn);

        addKeyListener(this);
        this.getContentPane().add(panel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == confirmBtn) {
            String text = inputField.getText();
            String regex = "[0-9]+";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(text);
            if (matcher.matches()) {
                int num = Integer.parseInt(text);
                if (0 < num && num <= 20) {
                    //生成指定个数的随机圆
                    circles = new Circle[num];
                    for (int i = 0; i < num; i++) {
                        circles[i] = new Circle();
                        int x, y;
                        while (true) {
                            x = getRandomX();
                            y = getRandomY();
                            //如果没有越界
                            if (!isOutOfBorder(x, y, circles[i].getR())) {
                                circles[i].setX(x);
                                circles[i].setY(y);
                                break;
                            }
                        }
                    }
                    requestFocus();//按键事件才能响应
                    //新开一个线程，用于自动刷新跳跃
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (true) {
                                refresh();//每@freshMillis毫秒跳一次
//                                System.out.println(getKeyListeners());
                                try {
                                    Thread.sleep(freshMillis);
                                } catch (InterruptedException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                    }).start();
                } else {
                    inputField.setText("");
                    JOptionPane.showMessageDialog(getParent(), "输入有误！\n请重新输入1-20的整数。", "输入错误", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                inputField.setText("");
                JOptionPane.showMessageDialog(getContentPane(), "输入有误！\n请重新输入1-20的整数。", "输入错误", JOptionPane.ERROR_MESSAGE);
            }

        }
    }

    private void redraw() {
        //清空
        getContentPane().getGraphics().clearRect(0, 0, getWidth(), getHeight());
        //重画 圆
        for (Circle c : circles) {
            drawCircle(c);
        }
    }

    private void refresh() {
        for (int i = 0; i < circles.length; i++) {
            Circle circle = circles[i];
            while (isOutOfBorder(circle.getX() + circle.getOffsetX(), circle.getY() + circle.getOffsetY(), circle.getR())) {
                //如果该方向将越界，那么重定向
                if (circle.getX() - circle.getR() <= 0) {
                    circle.redirection(Border.LEFT);
                } else if (circle.getX() + circle.getR() >= getContentPane().getSize().getWidth()) {
                    circle.redirection(Border.RIGHT);
                } else if (circle.getY() - circle.getR() <= 0) {
                    circle.redirection(Border.TOP);
                } else if (circle.getY() + circle.getR() >= getContentPane().getSize().getHeight()) {
                    circle.redirection(Border.BOTTOM);
                }
            }
            //未越界，继续该方向弹跳
            circle.setX(circle.getX() + circle.getOffsetX());
            circle.setY(circle.getY() + circle.getOffsetY());
        }
        //新开一个线程重绘
        new Thread(new Runnable() {
            @Override
            public void run() {
                redraw();
            }
        }).start();
    }

    private void drawCircle(Circle circle) {
        Graphics g = getContentPane().getGraphics();
        g.setColor(circle.getColor());
        int r = circle.getR();
        if (isSolid()) {    //实心圆
            g.fillOval(circle.getX() - r, circle.getY() - r, r * 2, r * 2);//椭圆
        } else {        //空心圆
            g.drawOval(circle.getX() - r, circle.getY() - r, r * 2, r * 2);//椭圆
        }
    }

    private boolean isSolid() {
        return solid;
    }

    private boolean isOutOfBorder(int x, int y, int r) {
        if (x - r < 0 || x + r > getContentPane().getSize().getWidth() || y - r < 0 || y + r > getContentPane().getSize().getHeight())
            return true;
        return false;
    }

    private int getRandomX() {
        return (int) (Math.random() * getSize().width);
    }

    private int getRandomY() {
        return (int) (Math.random() * getSize().height);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
//        System.out.println(e.getKeyCode());
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                solid = true;
                break;
            case KeyEvent.VK_RIGHT:
                solid = false;
                break;
            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void setFreshMillis(long freshMillis) {
        this.freshMillis = freshMillis;
    }
}
