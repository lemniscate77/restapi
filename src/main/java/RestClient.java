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
        userDTO = new UserDTO(3L, "James", "Brown", Byte.parseByte("33"));
        cookieID = getCookieID(); //getCookieID

        headers.add("Cookie", cookieID);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        String resultCode =
                addNewUser(userDTO).getBody().toString() +
                        updateUser().getBody().toString() +
                        deleteUser().getBody().toString();

        System.out.println(resultCode);
    }

    //getCookieID
    static String getCookieID() {
        ResponseEntity<List<UserDTO>> userResponse = restTemplate.exchange(
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