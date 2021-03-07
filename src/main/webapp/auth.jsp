<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <!--Import Google Icon Font-->
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <!--Import materialize.css-->
    <link type="text/css" rel="stylesheet" href="css/materialize.min.css"  media="screen,projection"/>
    <title>Todo List</title>
</head>
<body>
<div>
    <nav>
        <div class="nav-wrapper container">
            <a class="brand-logo">Todo List</a>
            <ul id="nav-mobile" class="right hide-on-med-and-down">
                <li>
                    <a href='<c:url value="/auth" />'>Авторизация</a>
                </li>
                <li>
                    <a href='<c:url value="/reg" />'>Регистрация</a>
                </li>
            </ul>
        </div>
    </nav>
    <div class="container row">
        <div class="col s6 offset-s3">
            <h4 class="header center-align">Авторизация</h4>
            <div class="card horizontal">
                <div class="card-stacked row">
                    <form class="col s12" action="<c:url value="/auth" />" method="post">
                        <div class="card-content">
                            <div class="row">
                                <div class="input-field col s12">
                                    <input placeholder="" id="email" type="email" name="email" class="validate" required>
                                    <label class="active" for="email">Почта</label>
                                </div>
                            </div>
                            <div class="row">
                                <div class="input-field col s12">
                                    <input placeholder="" id="password" type="password" name="password" class="validate" required>
                                    <label class="active" for="password">Пароль</label>
                                </div>
                                <c:if test="${not empty error}">
                                    <span class="helper-text" data-error="wrong" data-success="right">
                                        <c:out value="${error}"/>
                                    </span>
                                </c:if>
                            </div>
                        </div>
                        <div class="card-action right-align">
                            <button type="submit" class="waves-effect waves-light btn">
                                войти<i class="material-icons right">send</i>
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>