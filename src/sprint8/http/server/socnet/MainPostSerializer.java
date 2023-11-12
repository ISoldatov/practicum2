package sprint8.http.server.socnet;

import com.google.gson.Gson;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MainPostSerializer {
    public static void main(String[] args) {
        UserPost post = new UserPost();
        post.setPhotoUrl("https://new-social-network.site/images/928476864.jpg");
        post.setUserId(97_748);
        post.setDescription("Классное фото!");
        post.setLikesQuantity(753);

        // сконвертируйте publicationDateString в экземпляр LocalDate
        String publicationDateString = "25--12--2020";

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd--MM--yyyy");

        LocalDate publicationDate = LocalDate.parse(publicationDateString, dtf);
        post.setPublicationDate(publicationDate);

        // создайте экземпляр Gson
        Gson gson = new Gson();
        // сериализуйте объект
        String postSerialized = gson.toJson(post);
        System.out.println("Serialized post: " + postSerialized);

        // десериализуйте объект
        UserPost postDeserialized = gson.fromJson(postSerialized, UserPost.class);
        System.out.println("Deserialized post: " + postDeserialized);
    }
}
