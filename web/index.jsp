<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <title>LiveEdu - Главная</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="http://cdnjs.cloudflare.com/ajax/libs/jquery/1.6.2/jquery.min.js"></script>
        <script type="text/javascript" src="scripts/jquery-ui-1.8.16.custom.min.js"></script>      
        <script type="text/javascript" src="scripts/myFunction.js"></script>
        <link rel="stylesheet" type="text/css" href="css/style.css" >

        <link rel="stylesheet" type="text/css" href="css/jquery-ui-1.8.16.custom.css" >

        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">

        <!-- jQuery library -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>

        <!-- Latest compiled JavaScript -->
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    </head>
    <body>
        <div class="container">             
            <nav class="navbar navbar-default navbar-static">
                <div class="container-fluid">
                    <div class="navbar-header">                            
                        <a class="navbar-brand" href="#">LiveEdu</a>
                    </div>
                    <div class="collapse navbar-collapse js-navbar">
                        <ul class="nav navbar-nav">
                            <li class="active"><a href="index.jsp">Главная</a></li>
                            <li><a href="TeacherServletControler">Преподаватели</a></li>
                            <li><a href="StudentServletControler">Студенты</a></li> 
                            <li class="dropdown">
                                <a class="dropdown-toggle" data-toggle="dropdown" href="#">LDAP
                                    <span class="caret"></span></a>
                                <ul class="dropdown-menu">
                                    <li><a href="TeacherLDAPController">Преподаватели</a></li>
                                    <li><a href="StudentLDAPControler">Студенты</a></li>
                                </ul>
                            </li>                                
                        </ul>
                        <ul class="nav navbar-nav navbar-right">                            
                            <li><a href="LiveEdu/export/">PDF</a></li>
                            <li><a href="#"><span class="glyphicon glyphicon-log-out"></span> Logout</a></li>
                        </ul>
                    </div>
                </div>
            </nav>
            
            <div id="myCarousel" class="carousel slide" data-ride="carousel">
                <!-- Indicators -->
                <ol class="carousel-indicators">
                    <li data-target="#myCarousel" data-slide-to="0" class="active"></li>
                    <li data-target="#myCarousel" data-slide-to="1"></li>
                    <li data-target="#myCarousel" data-slide-to="2"></li>
                    <li data-target="#myCarousel" data-slide-to="3"></li>
                </ol>

                <!-- Wrapper for slides -->
                <div class="carousel-inner">

                    <div class="item active">
                        <img src="git.jpg" alt="GitLab" style="width:100%;">
                        <div class="carousel-caption">
                        </div>
                    </div>

                    <div class="item">
                        <img src="moodle.jpg" alt="Moodle" style="width:100%;">
                        <div class="carousel-caption">
                        </div>
                    </div>

                    <div class="item">
                        <img src="office.jpg" alt="Office365" style="width:100%;">
                        <div class="carousel-caption">
                        </div>
                    </div>
                    
                    <div class="item">
                        <img src="msdn.jpg" alt="MSDN" style="width:100%;">
                        <div class="carousel-caption">
                        </div>
                    </div>

                </div>

                <!-- Left and right controls -->
                <a class="left carousel-control" href="#myCarousel" data-slide="prev">
                    <span class="glyphicon glyphicon-chevron-left"></span>
                    <span class="sr-only">Previous</span>
                </a>
                <a class="right carousel-control" href="#myCarousel" data-slide="next">
                    <span class="glyphicon glyphicon-chevron-right"></span>
                    <span class="sr-only">Next</span>
                </a>
            </div>            
        </div>
    </body>
</html>
