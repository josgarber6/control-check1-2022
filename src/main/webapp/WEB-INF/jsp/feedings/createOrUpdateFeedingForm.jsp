<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="feedings">
    <jsp:attribute name="customScript">
        <script>
            $(function () {
                $("#startDate").datepicker({dateFormat: 'yy/mm/dd'});
            });
        </script>
    </jsp:attribute>
    <jsp:body>
        <h2>
        	<c:if test="${feeding['id'] == null}">New </c:if> Feeding
    	</h2>
        <form:form modelAttribute="feeding" class="form-horizontal" id="add-feeding-form">
            <div class="form-group has-feedback">
                <petclinic:inputField label="Start Date: " name="startDate"/>
                
                <petclinic:inputField label="Weeks Duration" name="weeksDuration"/>
              
                <%-- <div class="control-group">
                    <petclinic:selectField name="pet" label="Pet" names="${pets}" size="1"/>
                </div>
                
                <div class="control-group">
                    <petclinic:selectField name="feedingType" label="Feeding Type" names="${feedingTypes}" size="1"/>
                </div>    --%>
            </div>
                       
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <c:choose>
                        <c:when test="${feeding['id'] == null}">
                            <button class="btn btn-default" type="submit">Add Feeding</button>
                        </c:when>
                        <c:otherwise>
                            <button class="btn btn-default" type="submit">Update Feeding</button>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </form:form>
    </jsp:body>
</petclinic:layout>
