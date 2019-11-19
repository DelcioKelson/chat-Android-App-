package pmd.di.ubi.pt.chatamigavel;

public class Message {
    private String messageText;
    private int idSender;
    private String Id;

    public Message() {
    }
    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public Message(String messageText, int idSender) {
        this.messageText = messageText;
        this.idSender = idSender;
    }



    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }



    public int getIdSender() {
        return idSender;
    }

    public void setIdSender(int idSender) {
        this.idSender = idSender;
    }


}
