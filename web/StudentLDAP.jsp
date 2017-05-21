

<%@ page errorPage="\WEB-INF/jsp/ErrorPage.jsp"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, slGal.LiveEdu.ORM.*,
         slGal.LiveEdu.StudentLDAPControler" %>

<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


    <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>Студенты LDAP</title>
            

        </head>
        <body>
           
            <!--<div class="container">               

                <nav class="navbar navbar-default navbar-static">
                    <div class="container-fluid">
                        <div class="navbar-header">
                            <button class="navbar-toggle collapsed" type="button" data-toggle="collapse" data-target=".js-navbar">
                                <span class="sr-only">Toggle navigation</span>
                            </button>
                            <a class="navbar-brand" href="#" style="padding:15px 15px;">LiveEdu</a>
                        </div>
                        <div class="collapse navbar-collapse js-navbar">
                            <ul class="nav navbar-nav">
                                <li><a href="index.jsp">Главная</a></li>
                                <li><a href="TeacherServletControler">Преподаватели</a></li>
                                <li><a href="StudentServletControler">Студенты</a></li> 
                                <li><a href="">Сервисы Преподаватели</a></li>  
                                <li class="active"><a href="StudentLDAPControler">Сервисы Студенты</a></li>  
                            </ul>
                            
                        </ul>
                    </div>
                </div>
            </nav>

            <div class="row">
                <div class="col-md-12">
                    <h1 class="title">Фильтр по студентам</h1>
                    <form class="form-top" action="StudentServletControler">
                        <input type="hidden" name="action" value="<%=StudentLDAPControler.ACTION_FILTER%>" >    
                        <div id="filter">
                            <div class="col-md-6 teacher-list">
                                <div class="col-md-4">
                                    <label for="${teacherConst.PARAMETER_FACULTY}">Факультет</label>                     
                                    <select class="form-control" data-live-search="true" name="<%=StudentLDAPControler.PARAMETER_FACULTY%>">
                                        <option selected></option>
                                        <c:forEach var="facult" items="${requestScope.ATTRIBUTE_FACULTY_LIST}">   
                                            <option<c:if test="${facult eq pf}"> selected</c:if>>${facult}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="col-md-4">
                                    <label for="${teacherConst.PARAMETER_FACULTY}">Специальность</label>
                                    <select class="form-control" data-live-search="true" name="<%=StudentLDAPControler.PARAMETER_SPESIALITY%>">
                                        <option selected></option>
                                        <c:forEach var="speciality" items="${requestScope.ATTRIBUTE_SPECIALITY_LIST}">
                                            <option<c:if test="${speciality eq ps}"> selected</c:if>>${speciality}</option>
                                        </c:forEach>          
                                    </select>
                                </div>                                  
                                <div class="col-md-4">
                                    <label for="${teacherConst.PARAMETER_FACULTY}">Группа</label>                                                  
                                    <select class="form-control" data-live-search="true" name="<%=StudentLDAPControler.PARAMETER_GROUP%>" style="width: 150px;">
                                        <option selected></option>
                                        <c:forEach var="group" items="${requestScope.ATTRIBUTE_GROUP_LIST}">
                                            <option<c:if test="${group eq pg}"> selected</c:if>>${group}</option>
                                        </c:forEach>          
                                    </select>
                                </div>
                                <div class="col-md-4">
                                    <label for="${teacherConst.PARAMETER_FACULTY}">Украинец</label>                           
                                    <select class="form-control" data-live-search="true" name="<%=StudentLDAPControler.PARAMETER_UKRAINIAN%>" style="width: 150px;">                                
                                        <option<c:if test="${pu eq true}"> selected</c:if>>true</option>
                                        <option<c:if test="${pu eq false}"> selected</c:if>>false</option>
                                        </select>                        
                                    </div>                        
                                    <div class="col-md-12 filter-button">
                                        <input class="btn btn-danger" type="submit" value="Фильтр" />
                                    </div>                                    
                                </div>

                            </div>
                        </form>
                    </div> 
                </div>

            <c:if test="${ATTRIBUTE_PERSON != null}">
                <form action="StudentLDAPControler" method="post"  name="resultsForm" id="resultsForm">
                    <input type="hidden" name="<%= StudentLDAPControler.PARAMETER_COURSE_START%>" value="${pCourseStart}">
                    <input type="hidden" name="<%= StudentLDAPControler.PARAMETER_COURSE_END%>" value="${pCourseEnd}">
                    <input type="hidden" name="<%= StudentLDAPControler.PARAMETER_SPESIALITY%>" value="${ps}">
                    <input type="hidden" name="<%= StudentLDAPControler.PARAMETER_FACULTY%>" value="${pf}">
                    <input type="hidden" name="<%= StudentLDAPControler.PARAMETER_GROUP%>" value="${pg}">
                    <input type="hidden" name="<%= StudentLDAPControler.PARAMETER_UKRAINIAN%>" value="${pu}">
                    <table width="100%" class="table table-striped table-hover teacher-filer-list"> 
                        <thead>
                            <tr>
                                <td><input id="checkAll" type="checkbox" name="checkAll" value="${teacher.id}"
                                           onchange="
                                                   if ($('#checkAll').is(':checked')) {
                                                       $('#checkAll').attr('checked', 'checked');
                                                       if (markAllRows('resultsForm'))
                                                           return false;
                                                   } else {
                                                       $('#checkAll').removeAttr('checked');
                                                       if (unMarkAllRows('resultsForm'))
                                                           return false;
                                                   }">
                                </td>                 
                                <td>id</td>
                                <td>Имя</td>
                                <td>Фамилие</td>
                                <td>Отчество</td>
                                <td>Имя Eng</td>
                                <td>Фамилие Eng</td>
                                <td>Отчество Eng</td>
                                <td>Email</td>
                                <td>Email_1</td>
                                <td>Группа</td>
                                <td>Специальность</td>
                                <td>Факультет</td>                                
                            </tr>
                        </thead>
                        <c:forEach var="student" items="${ATTRIBUTE_PERSON}">
                            <tr>
                                <td><input type="checkbox" name="check" value="${student.idStudent}"></td>                        
                                <td><c:out value="${student.idStudent}" default="null" /> </td>
                                <td><c:out value="${student.personInf.firstname}" default="null" /></td>
                                <td><c:out value="${student.personInf.lastname}" default="null"/></td>
                                <td><c:out value="${student.personInf.patronymic}" default="null" /></td> 
                                <td><c:out value="${student.personInf.firstnameEn}" default="null" /></td>
                                <td><c:out value="${student.personInf.lastnameEn}" default="null"/></td>
                                <td><c:out value="${student.personInf.patronymicEn}" default="null" /></td>
                                <td><c:out value="${student.personInf.emailCorporate}" default="null" /></td>
                                <td><c:out value="${student.personInf.emailPersonal}" default="null" /></td>
                                <td><c:out value="${student.group}" default="null"/></td>
                                <td><c:out value="${student.specInf.spec}" default="null"/></td>
                                <td><c:out value="${student.specInf.facultyInf.faculty}" default="null"/></td>                                
                            </tr>
                        </c:forEach>
                        <div class="buttons">




                            <div class="buttons col-md-12">
                                <button type="submit" class=" btn btn-info" name="action" value="<%=StudentLDAPControler.ACTION_CLEAR_FIO_AND_EMAIL%>" title="Очистить ФИО">Очистить ФИО</button>  
                                <button type="submit" class=" btn btn-info" name="action" value="<%=StudentLDAPControler.ACTION_GENERATE_FIO%>" title="Сгенерировать ФИО">Сгенерировать ФИО</button> 
                            </div>

                            <div class="buttons col-md-12">     
                                <button type="submit" class=" btn btn-info" name="action" value="<%=StudentLDAPControler.ACTION_GENERATE_PDF%>" title="Создать PDF">Создать PDF</button>
                                <button type="submit" class=" btn btn-info" name="action" value="<%=StudentLDAPControler.ACTION_GENERATE_MSDN_IMPORT%>" title="Сгенерировать MSDN IMPORT">Сгенерировать MSDN IMPORT</button>                        
                                <button type="submit" class=" btn btn-info" name="action" value="<%=StudentLDAPControler.ACTION_GENERATE_OFFICE365_IMPORT%>" title="Сгенерировать Office365 IMPORT">Сгенерировать Office365 IMPORT</button>
                            </div>                            
                            <div class="buttons col-md-12">
                                <button type="submit" class=" btn btn-info" name="action" value="<%=StudentLDAPControler.ACTION_CLEAR_FIO_AND_EMAIL%>" title="Очистить ФИО">Очистить ФИО</button>  
                                <button type="submit" class=" btn btn-info" name="action" value="<%=StudentLDAPControler.ACTION_GENERATE_FIO%>" title="Сгенерировать ФИО">Сгенерировать ФИО</button> 
                                <button type="submit" class=" btn btn-info" name="action" value="<%=StudentLDAPControler.ACTION_CLEAR_EMAIL%>" title="Очистить EMAIL">Очиститить EMAIL</button>       
                                <button type="submit" class=" btn btn-info" name="action" value="<%=StudentLDAPControler.ACTION_GENERATE_EMAIL%>" title="Создать EMAIL">Создать EMAIL</button>
                                <button type="submit" class=" btn btn-info" name="action" value="<%=StudentLDAPControler.ACTION_GENERATE_PASSWORD%>" title="Перегенерировать password">Перегенерировать password</button>
                            </div>
                            <div class="buttons col-md-12">                                                   
                                <button type="submit" class=" btn btn-info" name="action" value="<%=StudentLDAPControler.ACTION_SET_MSDN%>" title="Установить MSDN">Установить MSDN</button> 
                                <button type="submit" class=" btn btn-info" name="action" value="<%=StudentLDAPControler.ACTION_CLEAR_MSDN%>" title="Снять MSDN">Снять MSDN</button>                                   
                                <button type="submit" class=" btn btn-info" name="action" value="<%=StudentLDAPControler.ACTION_SET_OFFICE365%>" title="Установить OFFICE365">Установить OFFICE</button> 
                                <button type="submit" class=" btn btn-info" name="action" value="<%=StudentLDAPControler.ACTION_CLEAR_OFFICE365%>" title="Clear OFFICE365">Clear OFFICE365</button> 
                            </div>                            
                        </div>

                        <a href="#" onclick="if (markAllRows('resultsForm'))
                                    return false;">Отметить все</a>
                        <a href="#" onclick="if (unMarkAllRows('resultsForm'))
                                    return false;">Снять выделени</a>

                </form>
            </c:if>

            <c:remove var="ATTRIBUTE_STUDENT" scope="request"/>
        </div>-->
    </body>    
</html>
