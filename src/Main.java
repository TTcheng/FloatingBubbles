public class Main {
    public static void main(String[] args) {
        MyWindow myWindow = new MyWindow();
        //全屏
        myWindow.getGraphicsConfiguration().getDevice().setFullScreenWindow(myWindow);
        myWindow.setVisible(true);
    }
}
