package sprint8.http.server.socnet_task3;

class Comment {
    private  String user;
    private String text;

    private Comment() {
    }

    public Comment(String user, String text) {
        this.user = user;
        this.text = text;
    }

    public String getUser() {
        return user;
    }

    public String getText() {
        return text;
    }
}
