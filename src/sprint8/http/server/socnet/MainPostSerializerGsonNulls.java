package sprint8.http.server.socnet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MainPostSerializerGsonNulls {
    public static void main(String[] args) {
        UserPost post = new UserPost();
        post.setPhotoUrl("https://new-social-network.site/images/928476864.jpg");
        post.setUserId(97_748);
        post.setLikesQuantity(753);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();

        String postSerialized = gson.toJson(post);
        System.out.println(postSerialized);
    }
}
