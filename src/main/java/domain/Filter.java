package main.java.domain;

import main.java.common.Utils;

public class Filter {
    private final Utils.FilterType type;

    private Object from, to, name;

    public Filter(Utils.FilterType type, Object from, Object to) {
        assert type != Utils.FilterType.MANUFACTURE;
        this.type = type;
        this.from = from;
        this.to = to;
    }

    public Filter(Utils.FilterType type, Object name) {
        assert type == Utils.FilterType.MANUFACTURE;
        this.type = type;
        this.name = name;
    }

    public Utils.FilterType getType() {
        return type;
    }

    public Object getFrom() {
        return from;
    }

    public Object getTo() {
        return to;
    }

    public Object getName() {
        return name;
    }

    public String[] toStringArray() {
        if (getType() == Utils.FilterType.MANUFACTURE) {
            return new String[]{getType().toString(), "{" + getName().toString() + "}"};
        } else {
            if (from.toString().equals(to.toString())) {
                return new String[]{getType().toString(), "{" + getFrom().toString() + "}"};
            } else {
                return new String[]{getType().toString(), "[" + getFrom().toString() + ";" + getTo().toString() + "]"};
            }
        }
    }
}
