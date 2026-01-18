package com.studybot.backend.DTOs;


import lombok.Data;

@Data
public class WhatsappMessageDto {
    private String messaging_product = "whatsapp";
    private String to;
    private String type = "text";
    private Text text;

    @Data
    public static class Text {
        private String body;
        public Text(String body) { this.body = body; }
    }
}
