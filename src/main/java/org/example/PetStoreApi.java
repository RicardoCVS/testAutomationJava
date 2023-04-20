package org.example;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import com.google.gson.Gson;

public class PetStoreApi {
    private static final String BASE_URL = "https://petstore.swagger.io/v2";
    private static final String USERNAME = "your-username";
    private static final String PASSWORD = "your-password";

    public static void main(String[] args) {
        try {
            // create user
            String createUserResponse = sendPostRequest(BASE_URL + "/user", "{\"id\":0,\"username\":\"" + USERNAME + "\",\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"johndoe@example.com\",\"password\":\"" + PASSWORD + "\",\"phone\":\"1234567890\",\"userStatus\":0}");
            System.out.println("Create User Response: " + createUserResponse);

            // retrieve user data
            String userDataResponse = sendGetRequest(BASE_URL + "/user/" + USERNAME);
            System.out.println("User Data Response: " + userDataResponse);

            // get pets by status
            String petsJson = sendGetRequest(BASE_URL + "/pet/findByStatus?status=sold");
            Gson gson = new Gson();
            List<Pet> petsList = Arrays.asList(gson.fromJson(petsJson, Pet[].class));

            // list pet names and ids
            List<PetNames> petNamesList = new ArrayList<>();
            for (Pet pet : petsList) {
                petNamesList.add(new PetNames(pet.getId(), pet.getName()));
            }
            System.out.println("Pet Names and IDs List: " + petNamesList);

            // count pet names
            Map<String, Integer> petNamesCount = new HashMap<>();
            for (PetNames petName : petNamesList) {
                String name = petName.getName();
                if (petNamesCount.containsKey(name)) {
                    petNamesCount.put(name, petNamesCount.get(name) + 1);
                } else {
                    petNamesCount.put(name, 1);
                }
            }
            System.out.println("Pet Names Count: " + petNamesCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String sendGetRequest(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", getAuthorizationHeader());
        int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

    private static String sendPostRequest(String urlString, String data) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", getAuthorizationHeader());
        con.setDoOutput(true);
        con.getOutputStream().write(data.getBytes("UTF-8"));
        int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

    private static String getAuthorizationHeader() {
        String auth = USERNAME + ":" + PASSWORD + ":";
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        return "Basic " + encodedAuth;
    }

    private static class Pet {
        private long id;
        private String name;

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    private static class PetNames {
        private long id;
        private String name;

        public PetNames(long id, String name) {
            this.id = id;
            this.name = name;
        }

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "{id: " + id + ", name: " + name + "}";
        }
    }
}