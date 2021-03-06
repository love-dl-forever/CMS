package web;

import java.io.IOException;
import java.io.PrintWriter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import dao.empDao;
import dao.rmsDao;
import entity.Emp;
import entity.Rms;



/**
 * 作业
 */
public class CheckAdmin extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("chekadmin()");
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out=response.getWriter();
		//分析请求路径
		String uri=request.getRequestURI();
		System.out.println(uri);
		//拆解路径
		String action=uri.substring(uri.lastIndexOf("/"),uri.lastIndexOf("."));
		System.out.println("Action:"+action);
		if("/admin".equals(action)){
			rmsDao dao=new rmsDao();
			List<Rms> list=new ArrayList<Rms>();
			try {
				list=dao.findAll();
				//步骤1.先绑定数据到请求Request对象上面
				request.setAttribute("list", list);
				//步骤2.获得转发器
				System.out.println("admin_list");
			request.getRequestDispatcher("admin_list.jsp").forward(request, response);
			} catch (Exception e2) {
				e2.printStackTrace();
				out.println("<h1>系统繁忙,请稍后重试,谢谢</h1>");
			}
			
		}else if("/del".equals(action)){
			empDao dao=new empDao();
			try {
				//怎么删除的来着???
				dao.delete(Integer.parseInt(request.getParameter("id")));
				System.out.println("删除成功");
				//重定向回主页
				response.sendRedirect("admin.dp");
			} catch (Exception e) {
				e.printStackTrace();
				out.println("<h1>系统繁忙,请稍后重试,谢谢</h1>");
				throw new RuntimeException("系统异常,请稍后重试",e);
			}
		}else if("/load".equals(action)){
			Rms e=new Rms();
			try {
				//步骤1.先绑定数据到请求Request对象上面
				request.setAttribute("e", e);
				//步骤2.获得转发器  步骤3.转发
				request.getRequestDispatcher("updateAdmin.jsp").forward(request, response);

			} catch (Exception e1) {
				e1.printStackTrace();
				out.println("<p>系统异常,请稍后重试</p>");
				throw new RuntimeException("系统异常,请稍后重试",e1);
			}
		}else if("/update".equals(action)){
			String id=request.getParameter("id");
			String username=request.getParameter("username");
			String name =request.getParameter("name");
			String department=request.getParameter("department");
			String salary=request.getParameter("salary");
			String password =request.getParameter("password");
			//将员工信息插入到数据库
			rmsDao dao=new rmsDao();
			Rms e=new Rms();
			e.setId(Integer.parseInt(id));
			e.setUname(username);
			e.setName(name);
			e.setDepartment(department);
			e.setSalary(Double.parseDouble(salary));
			e.setPassword(password);
			try {
				dao.update(e);
				System.out.println("员工信息:"+id+" "+username);
				//浏览器输出成功提示
				out.println("<p>添加成功!</p><br>");
				/*out.println("<a href='list'>查看员工信息列表</a>");*/
				/*
				 * 重定向之前,容器会清空response对象上存放的所有数据
				 * 重定向的地址是任意的
				 */
				//重定向到员工列表
				response.sendRedirect("admin.dp");
				
				//容器会自动关闭out,这里不调用out.close也可以
				out.close();
			} catch (Exception e2) {
				e2.printStackTrace();
				out.println("<h1>系统繁忙,请稍后重试,谢谢</h1>");
			}
		}else if("/left".equals(action)){
			String username = request.getParameter("username");
			request.setAttribute("username", username);
			request.getRequestDispatcher("left.jsp").forward(request, response);
		}
		
		else if("/check_name".equals(action)){
			String username = request.getParameter("name");
			System.out.println(username);
			List list=new ArrayList<Rms>();
			rmsDao rms=new rmsDao();
			list=rms.findByUserName(username);
			request.setAttribute("list",list);
			request.getRequestDispatcher("admin_list.jsp").forward(request, response);
		}
		
		
		
		
	}

}
