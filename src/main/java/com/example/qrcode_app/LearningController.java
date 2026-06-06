package com.example.qrcode_app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LearningController {

    @Autowired
    private AiService aiService;

    @GetMapping("/")
    public String showHomepage(@RequestParam(value = "level", required = false) String level, Model model) {
        String title = "Chào mừng bạn đến với thế giới Lập trình C!";
        String aiStory = "Hãy chọn cấp học ở trên, Tân Lê AI sẽ tự động sáng tạo một câu chuyện phù hợp riêng cho bạn!";

        if (level != null) {
            String targetAudience = "";
            
            // Phân loại cấp học để ra lệnh (Prompt) cho Tân Lê tự chỉnh văn phong
            switch (level) {
                case "cap1":
                    title = "🤖 Câu chuyện cho bé Tiểu học (Cấp 1)";
                    targetAudience = "Học sinh Tiểu học lớp 1 đến lớp 5 (Văn phong như truyện cổ tích, ẩn dụ, hoạt hình, dễ thương, gọi ngôn ngữ C là chú rùa hoặc siêu anh hùng)";
                    break;
                case "cap2":
                    title = "🧠 Khám phá cho học sinh THCS (Cấp 2)";
                    targetAudience = "Học sinh Trung học cơ sở lớp 6 đến lớp 9 (Tư duy logic vừa phải, liên hệ đến lập trình game, robot, kích thích sáng tạo)";
                    break;
                case "cap3":
                    title = "📘 Định hướng cho học sinh THPT (Cấp 3)";
                    targetAudience = "Học sinh Trung học phổ thông lớp 10 đến lớp 12 (Cần tính học thuật, định hướng nghề nghiệp, giải quyết thuật toán)";
                    break;
                case "daihoc":
                    title = "🛠️ Kiến thức chuyên sâu cho Đại học";
                    targetAudience = "Sinh viên Đại học hoặc người đi làm (Chuyên sâu về kỹ thuật, hệ thống, quản lý bộ nhớ và hiệu năng cao)";
                    break;
            }

            // Gọi hàm từ AiService để bắt Tân Lê tự động viết câu chuyện
            aiStory = aiService.generateStoryWithAI(targetAudience);
        }

        model.addAttribute("title", title);
        model.addAttribute("content", aiStory); // Gửi câu chuyện của Tân Lê ra màn hình HTML
        model.addAttribute("selectedLevel", level);

        return "index"; // Trả về file giao diện index.html
    }
}