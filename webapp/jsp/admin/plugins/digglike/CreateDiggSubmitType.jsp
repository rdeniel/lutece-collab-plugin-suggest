<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />
<jsp:useBean id="diggSubmitType" scope="session" class="fr.paris.lutece.plugins.digglike.web.DiggSubmitTypeJspBean" />
<% diggSubmitType.init( request, fr.paris.lutece.plugins.digglike.web.ManageDigglikeJspBean.RIGHT_MANAGE_DIGG_LIKE ); %>
<%= diggSubmitType.getCreateDiggSubmitType( request ) %>
<%@ include file="../../AdminFooter.jsp" %>