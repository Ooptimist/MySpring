package com.zc.spring;

import com.zc.demo.AppConfig;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MySpringApplicationContext {
    private Class configClass;

    Map<String,BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>();
    // 单例池，存放单例对象
    Map<String,Object> singletonObjects = new ConcurrentHashMap<String,Object>();


    public MySpringApplicationContext(Class configClass) {
        this.configClass = configClass;

        // 扫描，得到Class
        List<Class> classList = scan(configClass);

        // 解析得到的Class ---> BeanDefinition ---> beanDefinitionMap
        for (Class clazz : classList) {
            BeanDefinition beanDefinition = new BeanDefinition();
            beanDefinition.setBeanClass(clazz);

            // 判断该类是否有@Component注解
            if(clazz.isAnnotationPresent(Component.class)) {
                // 取得@Component注解的value
                Component componentAnnotation = (Component) clazz.getAnnotation(Component.class);
                String beanName = componentAnnotation.value();

                // 判断类上是否有@Scope注解
                if(clazz.isAnnotationPresent(Scope.class)) {
                    Scope scopeAnnotation = (Scope) clazz.getAnnotation(Scope.class);
                    beanDefinition.setScope(scopeAnnotation.value());
                } else {    // 单例
                    beanDefinition.setScope("singleton");
                }
                // 将生成的BeanDefinition存放到map中
                beanDefinitionMap.put(beanName,beanDefinition);
            }
        }

        // 基于Class去创建单例bean
        instanceSingletonBean();
    }

    private List<Class> scan(Class configClass) {
        // 将扫描的到的Class存放到List中
        List<Class> classList= new ArrayList<Class>();
        // 获得要扫描的路径，通过@ComponentScan注解中的value值获取
        ComponentScan componentScanAnnotation = (ComponentScan) configClass.getAnnotation(ComponentScan.class);
        String path = componentScanAnnotation.value(); // 得到的包路径 com.zc.demo.service
        path = path.replace(".","/");   // com/zc/demo/service

        // 如何获取Class，因为存放在编译后的目录下，这里使用类加载器
        ClassLoader classLoader = MySpringApplicationContext.class.getClassLoader();
        URL url = classLoader.getResource(path);
        File files = new File(url.getFile());   // 到这里获得com/zc/demo/service文件夹（classpath下的）

        if(files.isDirectory()) {
            File[] listFiles = files.listFiles();
            for (File f : listFiles) {
                // 得到目录下的文件路径（如com.zc.demo.service）
                String absolutePath = f.getAbsolutePath();
                absolutePath = absolutePath.substring(absolutePath.indexOf("com"),absolutePath.indexOf(".class"));
                absolutePath = absolutePath.replace("\\",".");
                // 通过类加载器去加载类
                try {
                    Class<?> clazz = classLoader.loadClass(absolutePath);
                    classList.add(clazz);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return classList;
    }

    private void instanceSingletonBean() {
        // 对于单例的类，需要先创建好存放在单例池中
        for (String beanName : beanDefinitionMap.keySet()) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if(beanDefinition.getScope().equals("singleton")) {
                // 创建bean
                if(!singletonObjects.containsKey(beanName)) {
                    Object bean = doCreate(beanName,beanDefinition);
                    singletonObjects.put(beanName,bean);
                }
            }
        }
    }

    private Object doCreate(String beanName, BeanDefinition beanDefinition) {
        try {
            // 1.实例化
            Class beanClass = beanDefinition.getBeanClass();
            Object bean = beanClass.getDeclaredConstructor().newInstance();
            // 2.属性填充
            Field[] fields = beanClass.getDeclaredFields();
            for (Field field : fields) {
                // 当类中属性被@Autowired注解时
                if(field.isAnnotationPresent(Autowired.class)) {
                    // 因为被@Autowired注解的属性类也要被注入到容器中，即属性类上使用@Component注解等
                    // 所以这里直接getBean，即可获得容器中要填充到属性的bean
                    Object o = getBean(field.getName());
                    field.setAccessible(true);
                    field.set(bean,o);
                }
            }


            // 3.初始化
            return bean;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Object getBean(String beanName) {
        // bean是单例的还是原型的，可以通过BeanDefinitionMap中存储的BeanDefinition信息进行判断
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);

        if(beanDefinition.getScope().equals("prototype")) {
            // 直接创建bean
            Object bean = doCreate(beanName, beanDefinition);
            return bean;
        } else {    // 单例
            // 直接去单例池中拿
            Object bean = singletonObjects.get(beanName);
            if(bean == null) {
                bean = doCreate(beanName,beanDefinition);
                singletonObjects.put(beanName,bean);
            }
            return bean;
        }
    }
}
