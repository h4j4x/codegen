package ${data.packageName};

public enum ${data.enumName} {
    <#list items as item>${item.key}("${item.value}")<#sep>,
    </#sep></#list>;

    private final String value;

    ${data.enumName}(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
