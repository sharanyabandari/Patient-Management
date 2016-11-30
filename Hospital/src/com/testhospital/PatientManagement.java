package com.testhospital;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class PatientManagement extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public PatientManagement() {
        super();
        // TODO Auto-generated constructor stub
    }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("------doGet----Start--------------");
		
		PrintWriter out = response.getWriter();
		
		String searchName = request.getParameter("searchName");
		
		request.setAttribute("searchName", searchName);
		
		//request.getRequestDispatcher(path)
		if(searchName!=null && !searchName.trim().isEmpty())
		{
			System.out.println("Search word: "+searchName);
			List<PatientPojo> patientList = searchPatientDetails(searchName);
			if(patientList!=null)
			{
			System.out.println("patientList: "+patientList.size());
			request.setAttribute("patientList", patientList);
			
			}
		}
		request.getRequestDispatcher("/WEB-INF/patientSearch.jsp").forward(request, response);
		
		
		System.out.println("------doGet----End--------------");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		System.out.println("------doPost----Start--------------");
		
		PrintWriter out = response.getWriter();
		
		String fname = request.getParameter("fname"); 
		String lname = request.getParameter("lname");
		String age = request.getParameter("age");
		String dd = request.getParameter("dd");
		String mmm = request.getParameter("mmm");
		String yyyy = request.getParameter("yyyy");
		String gender = request.getParameter("gender"); //texta1
		String phone = request.getParameter("phone");
		String text = request.getParameter("text");
		
		System.out.println( fname 	);
		System.out.println( lname 	);
		System.out.println( age 	);
		System.out.println( dd 		);
		System.out.println( mmm 	);
		System.out.println( yyyy 	);
		System.out.println( gender 	);
		System.out.println( phone 	);
		System.out.println( text 	);
		
		PatientPojo patientObj = new PatientPojo();
		
		String dob = formatedDateStr(dd, mmm, yyyy);
		System.out.println("-------------"+dob);
		patientObj.setFname(fname);
		patientObj.setLname(lname);
		patientObj.setAge(age);
		patientObj.setDob(dob);
		patientObj.setGender(gender);
		patientObj.setPhone(phone);
		patientObj.setText(text);
		
		//method to save Patient data in DB
		boolean isDataSaved = savePatientDetails(patientObj);
		
		if(isDataSaved == true)
		{
			//out.println("Patient details have been saved successfully.");
			 response.sendRedirect("/Hospital/success.html");
		}
		else
		{
			//out.println("Internal problem. Patient details not stored.");
			response.sendRedirect("/Hospital/failure.html");
		}
		
		System.out.println("------doPost----End--------------");
		
		
		
	}
	
	public boolean savePatientDetails(PatientPojo patientObj)
	{
		boolean recordStored = false;
		Connection conn  = getDBConnection();
		//ResultSet rs1 = null;
		PreparedStatement stmt = null;
		try{
			stmt = conn.prepareStatement("INSERT INTO T_HPTL_PATIENT( REC_NUM, FNAME, LNAME, AGE, DOB, GENDER, PHONE, INFO  )  VALUES ( SQ_T_HPTL_PATIENT.NEXTVAL,?,?,?,?,?,?,?)");
			stmt.setString(1, patientObj.getFname());
			stmt.setString(2, patientObj.getLname());
			stmt.setString(3, patientObj.getAge());
			stmt.setDate(4, java.sql.Date.valueOf(patientObj.getDob()));
			stmt.setString(5, patientObj.getGender());
			stmt.setString(6, patientObj.getPhone());
			stmt.setString(7, patientObj.getText());
			stmt.executeUpdate();
			stmt.close();
			recordStored = true;
			
	}catch(Exception e)
    {
    	e.printStackTrace();
    }finally{
    	try { 
    		
    		stmt.close(); 
    		conn.close();
    	} catch (Exception e) { }
    }
    
		return recordStored;
		
	}
	
	
	public List<PatientPojo> searchPatientDetails(String patientName)
	{
		List<PatientPojo> resultList = null;
		Connection conn  = getDBConnection();
		ResultSet rs1 = null;
		PreparedStatement stmt = null;
		try{

			stmt = conn.prepareStatement("SELECT REC_NUM,  FNAME,  LNAME,  AGE,  DOB,  GENDER,  PHONE,  INFO FROM T_HPTL_PATIENT WHERE upper(FNAME) like upper('%"+patientName+"%') or upper(LNAME) like upper('%"+patientName+"%')");
			rs1 = stmt.executeQuery();
			
			if(rs1.next())
			{
				resultList= new ArrayList<PatientPojo>();
				do
				{
					PatientPojo tempObj = new PatientPojo();
					tempObj.setRecNum(rs1.getString("REC_NUM"));
					tempObj.setFname(rs1.getString("FNAME"));
					tempObj.setLname(rs1.getString("LNAME"));
					tempObj.setAge(rs1.getString("AGE"));
					tempObj.setDob(formatedDateStr(rs1.getString("DOB")));
					tempObj.setGender(rs1.getString("GENDER"));
					tempObj.setPhone(rs1.getString("PHONE"));
					tempObj.setText(rs1.getString("INFO"));
					
					resultList.add(tempObj);
				}while(rs1.next());
				
			}
			
	}catch(Exception e)
    {
    	e.printStackTrace();
    }finally{
    	try { 
    		
    		stmt.close(); 
    		conn.close();
    	} catch (Exception e) { }
    }
    
		return resultList;
		
	}
	
	//To format dd, mmm and yyyy as date string
	public String formatedDateStr(String dd, String mmm, String yyyy)
	{
		System.out.println("dd "+dd);
		System.out.println("mmm "+mmm);
		System.out.println("yyyy "+yyyy);
		
		String dateStr = null;
		String date = null;
		try {
		if(dd!=null && mmm!=null &&  yyyy!=null)
		{
			dateStr = dd+"-"+mmm+"-"+yyyy;
		}		
		System.out.println("1 "+dateStr);
		//Calendar cal = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
		
		Date date2 =	(Date) dateFormat.parse(dateStr);			
			 date = dateFormat2.format(date2.getTime());
		} 
		catch (ParseException e) {
			
			e.printStackTrace();
		}
		
		
		return date;
	}
	
	public String formatedDateStr(String dtStr)
	{
	
		String date = null;
		try {

		//Calendar cal = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
		
		Date date2 =	(Date) dateFormat2.parse(dtStr);			
			 date = dateFormat.format(date2.getTime());
		} 
		catch (ParseException e) {
			
			e.printStackTrace();
		}
		
		
		return date;
	}
	
	
	//To get DB connection 
	public Connection getDBConnection()
	{
		Connection conn = null;		
		try{			
			conn = DriverManager.getConnection(	"jdbc:oracle:thin:@localhost:1521:XE",	"HOSPITAL",	"HOSPITAL");
			
			System.out.println("DB connection success.");
		}
		catch ( Exception e ){
			e.printStackTrace();
		}		
		
		return conn;	
	}

}
