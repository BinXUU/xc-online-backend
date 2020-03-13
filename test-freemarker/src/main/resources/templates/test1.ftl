<!DOCTYPE html>
<html>
<head>
    <meta charset="utf‐8">
    <title>Hello World!</title>
</head>
<body>
Hello ${name}!
<table>
    <tr>
        <td>序号</td>
        <td>姓名</td>
        <td>年龄</td>
        <td>钱包</td>
        <td>生日</td>
    </tr>
    <#if stus??>
        <#list stus as stu>
            <tr>
                <td>${stu_index + 1}</td>
                <td <#if stu.name == '小明'>style="background:red;"</#if>>${stu.name}</td>
                <td>${stu.age}</td>
                <td>${stu.money}</td>
                <#if stu.birthday??>
                    <td>${stu.birthday?string('yyyy年MM月')}</td>
                </#if>
            </tr>
        </#list>
    </#if>

</table>
</body>
</html>