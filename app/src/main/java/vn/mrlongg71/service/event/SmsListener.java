package vn.mrlongg71.service.event;

public interface SmsListener {
    void messageReceived(String messageText);
}
