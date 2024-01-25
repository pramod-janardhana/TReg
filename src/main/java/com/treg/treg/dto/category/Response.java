package com.treg.treg.dto.category;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class Response {

    @Getter
    @Setter
    public static class AddApi {
        private boolean ok;

        public AddApi() {
        }

        public AddApi(boolean ok) {
            this.ok = ok;
        }
    }

    @Getter
    @Setter
    public static class ListApi {
        private Long id;
        private String name;

        public ListApi() {
        }

        public ListApi(Long id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    @Getter
    @Setter
    public static class GetApi {
        private Long id;
        private String name;

        public GetApi() {
        }

        public GetApi(Long id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}
