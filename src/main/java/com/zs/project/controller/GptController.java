package com.zs.project.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/gpt")
public class GptController {
    @GetMapping("/")
    public void test(String prompt, HttpServletResponse res) throws IOException, InterruptedException {

        String str = "       什么是爱而不得? \n" +
                "东边日出西边雨，道是无晴却有晴。\n" +
                "他朝若是同淋雪，此生也算共白头。\n" +
                "我本将心向明月，奈何明月照沟渠。\n" +
                "此时相望不相闻，愿逐月华流照君。\n" +
                "衣带渐宽终不悔，为伊消得人憔悴。\n" +
                "此情可待成追忆，只是当时已惘然。\n" +
                "人生若只如初见，何事西风悲画扇。\n" +
                "曾经沧海难为水，除却巫山不是云。\n" +
                "何当共剪西窗烛，却话巴山夜雨时。\n" +
                "天长地久有时尽，此恨绵绵无绝期。\n" +
                "\n";
        // 响应流
        res.setHeader("Content-Type", "text/event-stream");
        res.setContentType("text/event-stream");
        res.setCharacterEncoding("UTF-8");
        res.setHeader("Pragma", "no-cache");
        ServletOutputStream out = null;
        try {
            out = res.getOutputStream();
            for (int i = 0; i < str.length(); i++) {
                out.write(String.valueOf(str.charAt(i)).getBytes());
                // 更新数据流
                out.flush();
                Thread.sleep(100);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
