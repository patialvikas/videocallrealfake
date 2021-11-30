
package com.slvrvideocallapp.trndvideo.models;


import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class ValidationRoot {

    @Expose
    private Data data;
    @Expose
    private String message;
    @Expose
    private Long status;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    @SuppressWarnings("unused")
    public static class Data {

        @Expose
        private Boolean notFound;
        @Expose
        private User user;

        public Boolean getNotFound() {
            return notFound;
        }

        public void setNotFound(Boolean notFound) {
            this.notFound = notFound;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

    }
}
