package test.connection;


import org.junit.Ignore;
import org.junit.jupiter.api.Test;

public class TestCmd {

    @Test
    @Ignore
    public void test() throws Exception{
//        Runtime rt = Runtime.getRuntime();
//        Process p = rt.exec("node  /home/wy/Documents/cms/src/test/resources/03.js  c=\\pm\\sqrt{a^2+b^2}");//这里我的codes.js是保存在c盘下面的phantomjs目录
//
//        InputStream is = p.getInputStream();
//        BufferedReader br = new BufferedReader(new InputStreamReader(is));
//        StringBuffer sbf = new StringBuffer();
//        String tmp = "";
//        while((tmp = br.readLine())!=null){
//            sbf.append(tmp);
//        }
//        //System.out.println(sbf.toString());
//        System.out.println(sbf);
    }

    @Test
    public void test1(){
//        User user = new User();
//        user.setUsername("1749");
//        User user1 = new User();
//        BeanUtils.copyProperties(user,user1);
//        user1.setUsername("789");
//        System.out.println(user.getUsername());
//        System.out.println(user1.getUsername());
    }

    @Test
    public void  test2() throws Exception{
//        PropertyEnum[] values = PropertyEnum.values();
//        for(PropertyEnum propertyEnum : values){
//            System.out.println(propertyEnum.getValue());
//        }
//        Object cast = PropertyEnum.CATEGORY_PAGE_SIZE.getClz().cast();
//        Integer cast1 = Integer.class.cast(PropertyEnum.CATEGORY_PAGE_SIZE.getValue());
//        Object cast = PropertyEnum.CATEGORY_PAGE_SIZE.getClz().cast(7);
//        Class<Integer> clz = Integer.class;
//        Integer integer = clz.cast(7);
//
//        if( PropertyEnum.CATEGORY_PAGE_SIZE.getDefaultValue() instanceof Integer){
//            System.out.println(PropertyEnum.CATEGORY_PAGE_SIZE.getDefaultValue());
//        }
//        PropertyEnum.CATEGORY_PAGE_SIZE.getClz().newInstance()
//        String name = PropertyEnum.CATEGORY_PAGE_SIZE.name();
//        System.out.println(PropertyEnum.valueOf(name).getDefaultValue());
//        System.out.println(PropertyEnum.CATEGORY_PAGE_SIZE.getClz().getName());
    }

    public static  <T> T get(Class<T> clz,Object o){
        if(clz.isInstance(o)){
            return clz.cast(o);
        }
        return null;
    }

}
