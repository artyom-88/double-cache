package ru.ganev.doublecache.model;


import java.io.Serializable;

/**
 * Simple model for unit testing
 */

public class TestObject implements Serializable {

    private String field1;
    private int field2;

    private TestObject() {
    }

    public TestObject(String field1, int field2) {
        this.field1 = field1;
        this.field2 = field2;
    }

    public static TestObjectBuilder builder() {
        return new TestObjectBuilder();
    }

    public String getField1() {
        return field1;
    }

    public int getField2() {
        return field2;
    }

    public static class TestObjectBuilder {
        private String field1;
        private int field2;


        private TestObjectBuilder() {
        }

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
