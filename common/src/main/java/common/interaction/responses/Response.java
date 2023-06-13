package common.interaction.responses;

import common.data.Flat;
import common.interaction.User;

import java.io.Serializable;
import java.security.Key;
import java.util.Hashtable;

/**
 * Класс для получения значения ответа с сервера.
 */
public class Response implements Serializable {
    private ResponseCode responseCode;
    private String responseBody;
    private User user;
    private String[] responseBodyArgs;
    private Hashtable<Integer, Flat> flatCollection;

    public Response(ResponseCode responseCode, String responseBody, User user, String[] responseBodyArgs, Hashtable<Integer, Flat> flatCollection) {
        this.responseCode = responseCode;
        this.responseBody = responseBody;
        this.user = user;
        this.responseBodyArgs = responseBodyArgs;
        this.flatCollection = flatCollection;
    }

    /**
     * @return Код ответа с сервера
     */
    public ResponseCode getResponseCode() {
        return responseCode;
    }

    /**
     * @return Тело ответа с сервера
     */
    public String getResponseBody() {
        return responseBody;
    }

    public String[] getResponseBodyArgs() {
        return responseBodyArgs;
    }

    public Hashtable<Integer, Flat> getFlatCollection() {
        return flatCollection;
    }


    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "Response[" + responseCode + ", " + responseBody + "]";
    }
}
