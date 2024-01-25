package com.treg.treg.dto.category;

public class Request {
    public static class AddApi {
        private String name;

        public AddApi() {
        }

        public AddApi(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
