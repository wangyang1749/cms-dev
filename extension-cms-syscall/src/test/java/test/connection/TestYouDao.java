package test.connection;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.wangyang.syscall.http.FanyiV3Demo;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

public class TestYouDao {

//    @Test
    public void testExplain(){
        JSONObject jsonObject = JSONObject.parseObject(FanyiV3Demo.getExplains("treatment"))
                .getJSONObject("basic");
//        StringBuffer sb = new StringBuffer();
        List<Object> list = jsonObject.getJSONArray("explains").stream().collect(Collectors.toList());
        String content = Joiner.on(",").join(list);
//        jsonObject.getJSONArray("explains").forEach(explain->{
//            sb.append(explain);
//       });
        System.out.println(content);
    }
}
