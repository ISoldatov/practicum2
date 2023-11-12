package sprint8.http.server.socnet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

public class Practicum {

    public static void main(String[] args) throws IOException {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();

        String lastLikeInfoStr = "{ \"user\": \"Алексей\", \"hours\": 12, \"minutes\": 30}";
        LastLikeInfo lastLikeInfo = gson.fromJson(lastLikeInfoStr, LastLikeInfo.class); // код десериализации

        LikesInfo likesInfo = new LikesInfo();
        likesInfo.setRepostsCount(10);
        likesInfo.setHasOwnerLiked(true);
        likesInfo.setLastLikeInfo(lastLikeInfo);
        likesInfo.setLikes(new Like[]{
                new Like("Алексей", "http://example.com/avatars/aleksey.jpg"),
                new Like("Елена", "http://example.com/avatars/elena.jpg"),
                new Like("Света", "http://example.com/avatars/sveta.jpg"),
        });

        // код сериализации
        String jsonLikesInfo = gson.toJson(likesInfo);
        System.out.println(jsonLikesInfo);
    }
}