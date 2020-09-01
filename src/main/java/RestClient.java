import org.springframework.core.ParameterizedTypeReference;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;


// Rest-template entry point
public class RestClient {
    static UserDTO userDTO;
    static String URL = "http://91.241.64.178:7081/api/users";
    static String cookieID;

    static HttpHeaders headers = new HttpHeaders();
    static RestTemplate restTemplate = new RestTemplate();

    //5ebfebe7cb975dfcf9
    public static void main(String[] args) {
        userDTO = new UserDTO(3L, "James", "Brown", Byte.parseByte("33")); //NEW
        cookieID = getCookieID(); //getCookieID

        headers.add("Cookie", cookieID);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        String resultCode =
                addNewUser(userDTO).getBody().toString() + //addNewUser
                        updateUser().getBody().toString() + //updateUser
                        deleteUser().getBody().toString(); //deleteUser

        System.out.println(resultCode);
    }

    //getCookieID
    static String getCookieID() {
        ResponseEntity<List<UserDTO>> userResponse = restTemplate
                .exchange(
                        URL,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<UserDTO>>() {
                });

        return userResponse
                .getHeaders()
                .getFirst("set-cookie");
    }

    //addNewUser
    static ResponseEntity<?> addNewUser(UserDTO userDTO) {
        HttpEntity<?> entity = new HttpEntity<>(userDTO, headers);

        return restTemplate
                .postForEntity(URL, entity, String.class);
    }

    //updateUser
    static ResponseEntity<?> updateUser() {
        userDTO.setName("Thomas");
        userDTO.setLastName("Shelby");

        HttpEntity<?> entity = new HttpEntity<>(userDTO, headers);

        return restTemplate
                .exchange(URL, HttpMethod.PUT, entity, String.class);
    }

    //deleteUser
    static ResponseEntity deleteUser() {
        HttpEntity<?> entity = new HttpEntity<>(null, headers);

        return restTemplate
                .exchange(URL + "/" + userDTO.getId(), HttpMethod.DELETE, entity, String.class);
    }
}


// HTTP GET http://91.241.64.178:7081/api/users
// Accept=[application/json, application/*+json]
// Response 200 OK

// Reading to [java.util.List<UserDTO>]
// HTTP POST http://91.241.64.178:7081/api/users
// Accept=[text/plain, application/json, application/*+json, */*]
// Writing [UserDTO@31c88ec8] as "application/json"
// Response 200 OK

// Reading to [java.lang.String] as "application/json"
// HTTP PUT http://91.241.64.178:7081/api/users
// Accept=[text/plain, application/json, application/*+json, */*]
// Writing [UserDTO@31c88ec8] as "application/json"
// Response 200 OK

// Reading to [java.lang.String] as "application/json"
// HTTP DELETE http://91.241.64.178:7081/api/users/3
// Accept=[text/plain, application/json, application/*+json, */*]
// Response 200 OK

// Reading to [java.lang.String] as "application/json"
// 5ebfebe7cb975dfcf9
