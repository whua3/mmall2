package UtilTest;

import com.google.common.collect.Lists;
import com.mmall.pojo.User;
import com.mmall.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.type.TypeReference;

import java.util.List;

/**
 * @author: whua
 * @create: 2019/05/10 20:39
 */
@Slf4j
public class JsonUtilTest {
    public static void main(String[] args) {
        User user = new User();
        user.setId(1);
        user.setEmail("whua");

        User user1 = new User();
        user1.setId(2);
        user1.setEmail("whua2");

        String userJson = JsonUtil.obj2String(user);
        String userJsonPretty = JsonUtil.obj2StringPretty(user);

        log.info("userJson:{}", userJson);

        log.info("userJsonPretty:{}", userJsonPretty);

        User user2 = JsonUtil.string2Obj(userJsonPretty, User.class);

        log.info("===================");

        List<User> userList = Lists.newArrayList();
        userList.add(user);
        userList.add(user1);

        String userListStr = JsonUtil.obj2StringPretty(userList);
        log.info(userListStr);

        List<User> userList1 = JsonUtil.string2Obj(userListStr, new TypeReference<List<User>>() {
        });

        List<User> userList2 = JsonUtil.string2Obj(userListStr, List.class, User.class);

        System.out.println("end");

    }
}
