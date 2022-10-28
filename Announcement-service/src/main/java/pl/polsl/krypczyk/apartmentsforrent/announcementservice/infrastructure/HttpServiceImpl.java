package pl.polsl.krypczyk.apartmentsforrent.announcementservice.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.HttpService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class HttpServiceImpl implements HttpService {

    public static final String UNKNOWN_USER = "Nieznany użytkownik";
    @Value("${userHost}")
    private String USER_HOST;

    @Override
    public String retrieveUsernameFromUserService(Long userId) {
        return sendHttpRequestAndGetResponse(userId);
    }

    private String sendHttpRequestAndGetResponse(Long userId) {
        try {
            var url = new URL(USER_HOST + "/users/" + userId + "/username");
            var con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            var responseCode = con.getResponseCode();
            if (!isResponseCodeValid(responseCode))
                return UNKNOWN_USER;

            var username = this.readResponse(con);
            con.disconnect();
            return username;
        } catch (IOException e) {
            return UNKNOWN_USER;
        }

    }

    private Boolean isResponseCodeValid(Integer responseCode) {
        return responseCode >= 200 && responseCode <= 299;
    }

    private String readResponse(HttpURLConnection con) throws IOException {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        return content.toString();
    }
}
