package com.prmgo.view.activities.login.model.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponseData {

@SerializedName("isRequestSuccessfull")
@Expose
private Boolean isRequestSuccessfull;
@SerializedName("message")
@Expose
private String message;
@SerializedName("response")
@Expose
private Response response;

public Boolean getIsRequestSuccessfull() {
return isRequestSuccessfull;
}

public void setIsRequestSuccessfull(Boolean isRequestSuccessfull) {
this.isRequestSuccessfull = isRequestSuccessfull;
}

public String getMessage() {
return message;
}

public void setMessage(String message) {
this.message = message;
}

public Response getResponse() {
return response;
}

public void setResponse(Response response) {
this.response = response;
}

}