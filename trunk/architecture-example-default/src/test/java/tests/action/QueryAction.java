package tests.action;


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tests.dao.TestDao;
import architecture.ee.jdbc.sqlquery.SqlQuery;
import architecture.ee.services.SqlQueryCallback;
import architecture.ee.services.SqlQueryClient;
import architecture.ee.web.struts.action.FrameworkDispatchActionSupport;
import architecture.ee.web.util.ModelMap;
import architecture.ee.web.util.ParamUtils;
import architecture.ee.web.util.WebApplicatioinConstants;

public class QueryAction extends FrameworkDispatchActionSupport {

	public ActionForward getList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String statement = ParamUtils.getParameter(request, "statement",
				"ICAP.SELECT_ALL_TABLE_NAMES");

		if (log.isDebugEnabled())
			log.debug("logging here ..");

		TestDao service = getComponent(TestDao.class);
		List<Map<String, Object>> list = service.queryForList(statement);

		ModelMap model = new ModelMap();
		model.put("list", list);

		request.setAttribute(WebApplicatioinConstants.MODEL_ATTRIBUTE, model);
		return (mapping.findForward("success"));

	}

	public ActionForward exportToExcel(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String tableName = ParamUtils.getParameter(request, "table_name");

		if (log.isDebugEnabled())
			log.debug("export:" + tableName );

		SqlQueryClient client = getComponent(SqlQueryClient.class);
		
		client.execute(new SqlQueryCallback<List<Map<String, Object>>> (){
				public List<Map<String, Object>> doInSqlQuery(SqlQuery sqlQuery) {
				    
					// sqlQuery 를 사용하여 필요한 작업을 수행한다. 
					
					return null;
				}			
			}
		);
		
		return (mapping.findForward("exproted-success"));

	}
}
