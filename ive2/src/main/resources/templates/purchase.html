<%--
  Created by IntelliJ IDEA.
  User: LG
  Date: 2023-10-06
  Time: 오후 8:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.util.List" %>
<%@ page import="shopping.MenuItem" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>구매 페이지</title>
</head>
<body>
<form method="post">
    <%
        List<MenuItem> basketList =(List<MenuItem>) request.getAttribute("basketList");
    %>

    <% if (basketList != null && !basketList.isEmpty()) { %>
    <table border="1">
        <tr>
            <th>제품명</th>
            <th>수량</th>
            <!-- 체크박스 추가 -->
            <th>선택</th>
        </tr>
        <% for (MenuItem menuItem : basketList) { %>
        <tr>
            <td><%= menuItem.getProductName() %></td>
            <td><%= menuItem.getQuantity() %></td>
            <!-- 각 항목에 체크박스 추가 -->
            <!-- 제품명과 수량을 콤마로 구분하여 값으로 설정 -->
            <!-- 체크시 이 값을 서버로 전송함 -->
            <td><input type="checkbox" name="basketList" value="<%=menuItem.getProductName()+","+menuItem.getQuantity()%>" /></td>
        </tr>
        <% } %>
    </table>
    <!-- 구매하기 버튼 -->
    <input type="submit" value="구매하기">
    <% } else { %>
    <!-- 장바구니가 비어있는 경우 메시지 출력 -->
    <p><%= request.getAttribute("errorMessage") != null ? request.getAttribute("errorMessage") : "" %> </p>
    <p>장바구니가 비어 있습니다.</p>
    <input type="button" value="돌아가기" onclick="location.href='/shop_Web_exploded/shopping/userMenu.jsp'">
    <% } %>
</form>

<!-- 구매 결과 메시지 출력 -->
<p><%= request.getAttribute("message") != null ? request.getAttribute("message") : "" %> </p>

<!-- 에러 메시지가 있다면 출력 -->
<script type='text/javascript'>
    var errorMessage = '<%= request.getAttribute("errorMessage") != null ? request.getAttribute("errorMessage") : ""%>';
    if(errorMessage !== ''){
        alert(errorMessage);
        window.location.href = '/shop_Web_exploded/shopping/Purchase';
    }
</script>
</body>
</html>
