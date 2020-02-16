package yichen.yao.core.common.enums;

/**
 * @Author: siran.yao
 * @time: 2020/2/15:下午1:13
 */
public enum NodeEnum {
    Leader(1, "leader"),
    Follower(2, "follower"),
    Candidate(3, "candidate");
    private int code;
    private String name;

    NodeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
