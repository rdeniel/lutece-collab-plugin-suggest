<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:useBean id="digglikeDigg" scope="session" class="fr.paris.lutece.plugins.digglike.web.DiggJspBean" />


<% 
	digglikeDigg.init( request, fr.paris.lutece.plugins.digglike.web.ManageDigglikeJspBean.RIGHT_MANAGE_DIGG_LIKE );
    response.sendRedirect( digglikeDigg.doMoveUpEntry( request ) );
%>