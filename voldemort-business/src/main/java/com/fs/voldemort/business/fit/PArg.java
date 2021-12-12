package com.fs.voldemort.business.fit;

import java.util.Objects;

public class PArg {
    public final String name;
    public Object value;

    public PArg(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PArg args = (PArg) o;
        return name.equals(args.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
