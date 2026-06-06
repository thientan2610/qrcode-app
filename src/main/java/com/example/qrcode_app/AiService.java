package com.example.qrcode_app;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Map;
import java.util.List;

@Service
public class AiService {

    // ĐÃ SỬA: Thay chuỗi cố định bằng cách đọc biến môi trường bảo mật trên hệ thống đám mây
    private final String apiKey = System.getenv("GROQ_API_KEY"); 
    private final WebClient webClient = WebClient.builder().build();

    public String generateStoryWithAI(String targetAudience) {
        // 1. Đường dẫn Endpoint chính thức của Groq Cloud API
        String url = "https://api.groq.com/openai/v1/chat/completions";
        
        String promptText = "Hãy viết một bài giới thiệu hoặc một câu chuyện ngắn, hấp dẫn, dễ hiểu nói về 'Ngôn ngữ lập trình C'. "
                          + "Yêu cầu: Nội dung và văn phong bắt buộc phải tự động điều chỉnh sao cho hoàn toàn phù hợp với đối tượng là: " 
                          + targetAudience + ". Viết bằng tiếng Việt, độ dài khoảng 150-200 từ.";

        try {
            // Kiểm tra an toàn đề phòng bạn quên chưa cấu hình biến môi trường khi chạy local
            if (apiKey == null || apiKey.isEmpty()) {
                return "Lỗi: Hệ thống chưa tìm thấy biến môi trường GROQ_API_KEY. Vui lòng kiểm tra lại cấu hình.";
            }

            // 2. Đóng gói cấu trúc dữ liệu JSON theo chuẩn Groq (Dùng model Llama 3.3 70B đỉnh nhất của Meta)
            Map<String, Object> requestBody = Map.of(
                "model", "llama-3.3-70b-versatile",
                "messages", List.of(
                    Map.of(
                        "role", "user",
                        "content", promptText
                    )
                )
            );

            // 3. Gửi Request kèm Header Authorization mang mã Key của Groq
            Map<String, Object> response = webClient.post()
                    .uri(url)
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey) // Xác thực chìa khóa kết nối
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            // 4. Bóc tách dữ liệu chữ trả về từ mô hình Llama của Groq
            if (response != null && response.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
                Map<String, Object> firstChoice = choices.get(0);
                Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
                return (String) message.get("content");
            }
        } catch (Exception e) {
            return "Hệ thống Tân Lê AI đang xử lý dữ liệu, bạn vui lòng đợi vài giây rồi bấm lại nhé! (Chi tiết lỗi: " + e.getMessage() + ")";
        }
        return "Không thể lấy nội dung từ Tân Lê Cloud AI.";
    }
}