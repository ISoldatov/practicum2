package sprint8.http.server.json;

import com.google.gson.Gson;

public class FromJson {
    public static void main(String[] args) {
        String jsonString = "{\"name\":\"Тузик\",\"owner\":{\"name\":\"Игорь\",\"surname\":\"Петров\"},\"age\":3}";
        Gson gson = new Gson();
        Dog dog = gson.fromJson(jsonString, Dog.class);

        System.out.println("Собака:");
        System.out.println("Кличка: " + dog.getName());
        System.out.println("Возраст: " + dog.getAge());
        System.out.println("Владелец:");
        Owner owner = dog.getOwner();
        System.out.println("Имя: " + owner.getName());
        System.out.println("Фамилия: " + owner.getSurname());
    }
}

