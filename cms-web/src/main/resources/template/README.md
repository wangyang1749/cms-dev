```html
<!DOCTYPE html>
<html th:fragment="layout (head,content)" xmlns:th="http://www.thymeleaf.org">
<!-- th:replace="~{html/article/base :: layout(~{::head}, ~{::body})}" -->
<head th:replace="${head}">
    <!-- <title th:replace="${title}">Layout Title</title> -->
</head>

<body>
    <div th:insert="html/components/header :: #header"></div>
    <div th:replace="${content}">
        <p>Layout content</p>
    </div>
    <div th:insert="html/components/footer :: #footer"></div>

</body>

</html>
```