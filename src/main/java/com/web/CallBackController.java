package com.web;

import com.anno.Cache;
import com.base.Result;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lizehao
 */
@RestController
@RequestMapping(value = "/rest/callback")
public class CallBackController {

    private static final Logger logger = LoggerFactory.getLogger(CallBackController.class);

    @ApiOperation(value = "测试")
    @GetMapping(value = "/test", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Cache(open = true,key = "'TEST:KEY:'+#key",expire = 10)
    public Result test(@RequestParam Integer key, @RequestParam("value") String value) throws Exception{
        return Result.success(key++);
    }
}
