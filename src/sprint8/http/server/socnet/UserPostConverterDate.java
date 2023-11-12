package sprint8.http.server.socnet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UserPostConverterDate {
    public static void main(String[] args) {
        UserPost post = new UserPost();
        post.setPhotoUrl("https://new-social-network.site/images/928476864.jpg");
        post.setUserId(97_748);
        post.setDescription("Классное фото!");
        post.setLikesQuantity(753);
        LocalDate publicationDate = LocalDate.of(2020, 12, 25);
        post.setPublicationDate(publicationDate);

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();

        // сериализуем объект в JSON
        String postSerialized = gson.toJson(post);
        // Дата теперь отображается как 25--12--2020
        System.out.println("Serialized post:\n" + postSerialized);

        // заменим дату в JSON на другой формат
        String jsonWithAnotherDateFormat = postSerialized.replace("25--12--2020", "25.12.2020");

        System.out.println("New json:\n" + jsonWithAnotherDateFormat);

        // сконвертируем дату в формате 25.12.2020 в объект LocalDate
        UserPost postDeserialized = gson.fromJson(jsonWithAnotherDateFormat, UserPost.class);
        System.out.println("Deserialized post:\n" + postDeserialized);
    }
}
class LocalDateAdapter extends TypeAdapter<LocalDate> {
    private static final DateTimeFormatter formatterWriter = DateTimeFormatter.ofPattern("dd--MM--yyyy");
    private static final DateTimeFormatter formatterReader = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Override
    public void write(final JsonWriter jsonWriter, final LocalDate localDate) throws IOException {
        jsonWriter.value(localDate.format(formatterWriter));
    }

    @Override
    public LocalDate read(final JsonReader jsonReader) throws IOException {
        return LocalDate.parse(jsonReader.nextString(), formatterReader);
    }
}
