package com.wsk.parent.vod.controller;

import com.wsk.parent.vod.service.VodService;
import com.wsk.parent.vod.utils.ConstantPropertiesUtil;
import com.wsk.parent.vod.utils.Signature;
import com.wsk.serviceutil.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Random;

/**
 * @author:WuShangke
 * @create:2022/8/20-17:22
 */
@RestController
@RequestMapping("/admin/vod")
public class VodController {
    @Autowired
    VodService vodService;

    //上传视频，参考官方文档https://cloud.tencent.com/document/product/266/10276
    //TODO 感觉文件上传有点问题
    @PostMapping("/upload")
    public Result uploadVideo(@RequestParam MultipartFile file) throws Exception{
        InputStream inputStream = file.getInputStream();
        String originalFilename = file.getOriginalFilename();
        String videoId = vodService.uploadVideo(inputStream, originalFilename);
        return Result.ok(videoId);
    }

    //删除视频
    @DeleteMapping("/remove/{id}")
    public Result removeVideo(@PathVariable String id){
        vodService.removeVideo(id);
        return Result.ok();
    }

    //签名
    @GetMapping("sign")
    public Result sign() {
        Signature sign = new Signature();
        // 设置 App 的云 API 密钥
        sign.setSecretId(ConstantPropertiesUtil.ACCESS_KEY_ID);
        sign.setSecretKey(ConstantPropertiesUtil.ACCESS_KEY_SECRET);
        sign.setCurrentTime(System.currentTimeMillis() / 1000);
        sign.setRandom(new Random().nextInt(java.lang.Integer.MAX_VALUE));
        sign.setSignValidDuration(3600 * 24 * 2); // 签名有效期：2天
        try {
            String signature = sign.getUploadSignature();
            System.out.println("signature : " + signature);
            return Result.ok(signature);
        } catch (Exception e) {
            System.out.print("获取签名失败");
            e.printStackTrace();
            return Result.fail(null);
        }
    }
}
