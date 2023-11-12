

package sprint8.http.server.json;

import com.google.gson.Gson;

public class ToJson {
    public static void main(String[] args) {

        Owner owner = new Owner("Name","Suname");
        // создайте экземпляр класса Dog (собака)
        Dog dog = new Dog("friend", owner, 5);

        Gson gson = new Gson();
        // сериализуйте объект класса Dog в JSON
        String jsonString = gson.toJson(dog);

        System.out.println(jsonString);

    }
}

