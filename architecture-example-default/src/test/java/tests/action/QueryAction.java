package tests.action;


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tests.service.ICapService;
import architecture.ee.services.SqlQueryClient;
import architecture.ee.web.struts.action.FrameworkDispatchActionSupport;
import architecture.ee.web.util.WebApplicatioinConstants;
import architecture.ee.web.util.ModelMap;
import architecture.ee.web.util.ParamUtils;

public class QueryAction extends FrameworkDispatchActionSupport {

	public ActionForward getList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String statement = ParamUtils.getParameter(request, "statement",
				"ICAP.SELECT_ALL_TABLE_NAMES");

		if (log.isDebugEnabled())
			log.debug("logging here ..");

		ICapService service = getComponent(ICapService.class);
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

		SqlQueryClient service = getComponent(SqlQueryClient.class);
		service.exportToExcel(null, null, tableName);

		
		//ModelMap model = new ModelMap();
		//model.put("list", list);
		//request.setAttribute(ApplicatioinConstants.MODEL_ATTRIBUTE, model);
		
		return (mapping.findForward("exproted-success"));

	}
}
