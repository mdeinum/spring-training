package biz.deinum.moneytransfer;

public class Main {

  public static void main(String[] args) {
    HelloWorld hw = new HelloWorldImpl();
    hw = DebugProxy.wrap(hw, HelloWorld.class);
    hw.sayHello();
  }
}

class HelloWorldImpl implements HelloWorld {

  @Override
  public void sayHello() {
    System.out.println("Hello World!");
  }
}
