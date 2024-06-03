import java.awt.*;
import java.awt.TrayIcon.MessageType;

public class NotificationManager {

    public static void showNotification(String title, String message) {
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().createImage("icon.png"); // 可自定义图标路径
            TrayIcon trayIcon = new TrayIcon(image, "通知");
            trayIcon.setImageAutoSize(true);
            trayIcon.setToolTip("通知");
            try {
                tray.add(trayIcon);
                trayIcon.displayMessage(title, message, MessageType.INFO);
            } catch (AWTException e) {
                e.printStackTrace();
            }
        }
    }
}
