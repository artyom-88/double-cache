package com.ganev.doublecache.model;

import java.io.Serializable;

/** Simple model for unit testing */
public record TestObject(String field1, int field2) implements Serializable {

  public static TestObjectBuilder builder() {
    return new TestObjectBuilder();
  }

  public static class TestObjectBuilder {
    private String field1;
    private int field2;

    private TestObjectBuilder() {}

    public TestObjectBuilder field1(String field1) {
      this.field1 = field1;
      return this;
    }

    public TestObjectBuilder field2(int field2) {
      this.field2 = field2;
      return this;
    }

    public TestObject build() {
      return new TestObject(this.field1, this.field2);
    }
  }
}
