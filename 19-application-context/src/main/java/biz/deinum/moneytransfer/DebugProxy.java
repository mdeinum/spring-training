package biz.deinum.moneytransfer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DebugProxy implements InvocationHandler {

  private static final Logger LOG = LoggerFactory.getLogger(DebugProxy.class);
  private final Object target;

  private DebugProxy(Object target) {
    this.target = target;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    Object result;
    try {
      LOG.info("before method {}", method.getName());
      result = method.invoke(target, args);
    } catch (InvocationTargetException e) {
      throw e.getTargetException();
    } catch (Exception e) {
      throw new RuntimeException("unexpected invocation exception: " + e.getMessage(), e);
    } finally {
      LOG.info("after method {}", method.getName());
    }
    return result;
  }

  public static <T> T wrap(Object obj, T type) {
    return (T) Proxy.newProxyInstance(obj.getClass().getClassLoader(),
        obj.getClass().getInterfaces(), new DebugProxy(obj));
  }
}
