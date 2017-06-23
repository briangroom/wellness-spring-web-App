package com.hitched.controller;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.sql.DataSource;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.hitched.model.UserLogin;
import com.hitched.model.Hitched;
import com.hitched.service.LocationService;
import com.hitched.service.SendMail;
import com.hitchedUtils.HitchedUtils;
import com.hitchedUtils.States;
import com.hitched.service.HitchedService;

@Controller
@SessionAttributes("emailAddress")
public class HitchedController {
		
		    public HitchedController() {
				System.out.println("in HitchedController servelet");
			}
			    
			    
			    
		     @Autowired
			private HitchedService hitchedService; 
			     
		     @Autowired
			    DataSource dataSource;
			     
		     @Autowired
			private LocationService locationService;
			    
			   
		    SendMail sendMail =new SendMail();
		    HitchedUtils hitchedUtils=new HitchedUtils();
			    
		    @RequestMapping(value = "/jdbcCrudes", method = RequestMethod.GET)
		    public String services(Model model) {
		         model.addAttribute("attribs", hitchedService.jdbcDbConnect("bluesfan31@outlook.com"));
		         //model.addAttribute("attribs", hitchedService.jdbcDbConnect("kenn.juma@yahoo.com"));
			              
		        return "jdbcCrudes";
		    }
			    
		    @RequestMapping("email-lookup")
		    public String handlepost(@ModelAttribute("email-lookup") UserLogin userLogin, BindingResult bindingResult, ModelMap model) throws ParseException{    	
			    		    	 
			    	 try {
				    		 if(hitchedService.findUser(userLogin.getEmailAddress())== true){
				    			 model.addAttribute("attribs", hitchedService.getUserByEmailId(userLogin.getEmailAddress()));
					    		//UserLogin userdetail= hitchedService.getUserByEmailId(userLogin.getEmailAddress());
					    		//model.addAttribute("attribs", userdetail);
				    		 }else{
				    			 model.addAttribute("error", "User email does noe exist in the Data base"); 
				    		 }		    		 
					     }
						 catch(EmptyResultDataAccessException e){
							
						 }catch(RuntimeException e) {						
								model.addAttribute("error", "Unexpected error occured");
								e.printStackTrace();
								    
						  }
			    	 
			    	 
		         return "services";
			    	
		    }
			    
	// this is a test copy of the signups function (uses registration.jsp) using Spring validation
			@RequestMapping(value = "/registration", method = RequestMethod.GET)
			public String registration(Model model) {
				model.addAttribute("registration", new UserLogin());
					return "registration";
				}
				
			@RequestMapping(value = "/registration", method = RequestMethod.POST)
		       public String registration(@Valid @ModelAttribute("registration")
		       UserLogin userLogin, BindingResult result, Model model) {
			       
		       if (result.hasErrors()) {
		    return "registration";
		       }
		        if(hitchedService.findUser(userLogin.getEmailAddress())== true){
		       model.addAttribute("error", "A user exists with the supplied email "+userLogin.getEmailAddress()+" <a href='forgotpassword'> Forgot Password click here </a>"); 
		                  return "registration";  
		         }
		          if(userLogin.getPassword().equals(userLogin.getPasswordConfirm())){
		    hitchedService.createUser(userLogin);
			    
		    }else{
		    model.addAttribute("error", "These Passwords don't matching ");
		    }
			          
		             return "redirect:/login";
		       }	
				
			// end of code added for test copy

		    @RequestMapping("LookUpByLastnameOrFirstname")
		    public String lookUpname(@ModelAttribute("LookUpByLastnameOrFirstname") UserLogin userLogin, ModelMap model){    	
		    	
	    	     //List<UserLogin> userdetail= hitchedService.lookupMembers(userLogin.getLname());	
		    	     try {
		    	    	     model.addAttribute("users", lookupMembers(userLogin.getLname()));
				    		  		 
					     }
						 catch(EmptyResultDataAccessException e){
							 model.addAttribute("error", "User "+userLogin.getLname()+" does not exist in the Data base"); 
		    	    	     model.addAttribute("attribs", lookupMembers(userLogin.getLname()));
				    		 
							 }catch(RuntimeException e) {						
								model.addAttribute("error", "Unexpected error occured"+e);
								e.printStackTrace();
								    
						  }
			    	 
			    	 
		         return "services";
			    	
		    }
			    
		/*	    @RequestMapping("lookUpname")
			    public String lookUpname(@ModelAttribute("lookUpname") UserLogin userLogin, BindingResult bindingResult, ModelMap model){    	
			    	
			    	     try {
			    	    	     UserLogin userdetail= getUserByNane(userLogin.getLname());	
			    	    	     model.addAttribute("attribs", userdetail);
					    		  		 
						     }
							 catch(EmptyResultDataAccessException e){
								 model.addAttribute("error", "User "+userLogin.getLname()+" does noe exist in the Data base"); 
					    		 
								 }catch(RuntimeException e) {						
									model.addAttribute("error", "Unexpected error occured"+e);
									e.printStackTrace();
								    
							  }
			    	 
			    	 
			         return "services";
			    	
			    }  */
			    
		    @RequestMapping(value = "/logins", method = RequestMethod.GET)
		    public String login(Model model) {
		    	model.addAttribute("loginUser", new UserLogin());
		    	model.addAttribute("msg", "Login to explore the complete features!");
		        return "logins";
		    }
		    
			    
			    
		    @RequestMapping("bio")
		    public String bio(ModelMap model, @RequestParam("id") long id){    	
		    	
		    	try {
		    		UserLogin userdetail= hitchedService.getUserById(id);
		    		model.addAttribute("attribs", userdetail);
    		        model.addAttribute("user", userdetail.getFname());
    		        model.addAttribute("role", userdetail.getRoles());
				    }
					catch(RuntimeException e) {
						model.addAttribute("error", "Unexpected error occured");
					    e.printStackTrace();					    
				  }
		    		
		    	return "bio";
		    }
		    
		   
		    
		    @RequestMapping("LookupMembers")
		    public String searchMembers(ModelMap model, @RequestParam("LookupMembers") String names, @RequestParam(value="id",required=false) long id){    	
		    	
		    	try {
		    		long membersId=id;
		    		List<UserLogin> memberList = hitchedService.lookupMembers(names);
		    		if(hitchedService.getUserByName(names).isEmpty()){          
		                model.addAttribute("error",  "There are no matches for this Look-up");
		               }
		    		else		    				
		    			model.addAttribute("success", memberList.size()+ " members found with name " +names);
		    		    model.addAttribute("users", memberList);		    		    
		    		    model.addAttribute("user", hitchedService.getUserById(membersId));
		    		    
				    }
					catch(RuntimeException e) {
						model.addAttribute("error", "Unexpected error occured");
					    e.printStackTrace();					    
				  }
		    		
		    	return "members";
		    }
		    
		    @RequestMapping("lookupArtist")
		    public String lookartist(ModelMap model, @RequestParam("lookupArtist") String names){    	
		    	
		    	try {
			    		
		    		List<UserLogin> memberList = hitchedService.getUserByName(names);
		    		if(hitchedService.getUserByName(names).isEmpty()){          
		                model.addAttribute("error",  "There are no matches for this Look-up");
		               }
		    		else		    				
		    			model.addAttribute("success", memberList.size()+ " members found with name " +names);
		    		    model.addAttribute("users", memberList);		    		    
		    		     
				    }
					catch(RuntimeException e) {
						model.addAttribute("error", "Unexpected error occured");
					    e.printStackTrace();					    
				  }
		    		
		    	return "services";
		    }
		    
		    @RequestMapping("Lookupcity")
		    public String searchCity(ModelMap model, @RequestParam("Lookupcity") String cityorZip, @RequestParam(value="id",required=false) long id){    	
		    	
		    	
		    	try { 
		    		//List<UserLogin> memberList = hitchedService.getUserByName(cityorZip);
		    		if(lookupCity(cityorZip).isEmpty()){          
		                model.addAttribute("error",  "There are no matches for this Look-up");
		               }
		    		   else{if(!(id==-1)){
		    			    model.addAttribute("users", lookupCity(cityorZip));
			    		    model.addAttribute("user", hitchedService.getUserById(id));
			    		    if(Pattern.matches("^\\d+$", cityorZip)){
			    		    model.addAttribute("success", lookupCity(cityorZip).size()+ " Members found in Postal ZIP  " +cityorZip);
			    		    }else
			    		    	model.addAttribute("success", lookupCity(cityorZip).size()+ " Members found in the city of " +cityorZip);
			    		    return "members";
		    		   }else	    				
			    		   
		    		    model.addAttribute("users", lookupCity(cityorZip));
		    		    model.addAttribute("success", lookupCity(cityorZip).size()+ " Members found in " +cityorZip);
		    		   
		    		}
		    		    
				    } 
					catch(RuntimeException e) {
						model.addAttribute("error", "Unexpected error occured");
					    e.printStackTrace();					    
				  }
		    		
		    	return "services";
		    }
		    
		    
			@SuppressWarnings("rawtypes")
			public List<UserLogin> lookupCity(String cityOrZip) {
				JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource); 
					
		       	final String sql  = "SELECT m.* FROM hitched.users m WHERE m.city like '%"+ cityOrZip +"%'"
		       			+ " UNION "
		       			+ "SELECT m.* FROM hitched.users m WHERE m.zip like '%"+ cityOrZip +"%'";
		       	@SuppressWarnings("unchecked")
		   		List<UserLogin> memberList = jdbcTemplate.query(sql, new BeanPropertyRowMapper( UserLogin.class ));						
		   		return memberList;
		   		 
			}
   
	       
	    
	       @RequestMapping(value = {"index", "/"}, method = RequestMethod.GET)
		    public String index(Model model){ 
		    model.addAttribute("msg", "Hi welcome to the Gettin' Hitched website");
		    model.addAttribute("m", "Select Units");
	    	model.addAttribute("wt", "lb/kg");
	    	model.addAttribute("ht", "ft/cm");
		    
		    	return "index";				 
			} 
	       
	       @RequestMapping(value ="bmi",  method = RequestMethod.POST)
	       public String bmi(@RequestParam Map<String,String> requestParams,Model model) throws Exception{
	       	   String w=requestParams.get("w");
	       	   String h=requestParams.get("h");
	       	   String m=requestParams.get("system");    	   
	       	   Integer wt = Integer.valueOf(w);
	       	   Integer ht = Integer.valueOf(h);
	       	
	       		 if(m.equals("metric")){
	   		    //metric system SI units
	   		    	    double bmi = hitchedUtils.getBmi(wt, ht, 10000);
	   	    		   // model.addAttribute("welcome", "You selected "+m+" Height "+ h+ " cm weight " +w+ " kg" );
	   			    	model.addAttribute("bmi", "Your BMI is " +bmi);
	   			    	model.addAttribute("status", "You are " +hitchedUtils.getStatus(bmi));  
	   			    	model.addAttribute("m", m); 
	   			    	model.addAttribute("wt", "kg");
	   			    	model.addAttribute("ht", "cm");
	   			    	model.addAttribute("w", wt);
	   			    	model.addAttribute("h", ht);
	       	     }else {
	       		 // Imperial system US units
	   	    		    if(m.equals("imperial")){
	   	    		    double bmi = hitchedUtils.getBmi(wt, ht, 703);
	   			    	//model.addAttribute("welcome", "You selected " +m+ " Hight "+ h+ " in weight " +w+ " lb");
	   			    	model.addAttribute("bmi", "Your BMI is " +bmi);
	   			    	model.addAttribute("status", " - " +hitchedUtils.getStatus(bmi));
	   			    	model.addAttribute("m", m); 
	   			    	model.addAttribute("wt", "lb");
	   			    	model.addAttribute("ht", "ft");
	   			    	model.addAttribute("w", wt);
	   			    	model.addAttribute("h", ht);
	   	    		    }
	       		   		 
	       	 }
	       	return "index";
	       }
	       
		    @RequestMapping(value = "/about", method = RequestMethod.GET)
		    public String about(Model model) {
		    	
		    	model.addAttribute("addresses", locationService.getAllAddresses());
		    	
		        return "about";
		    }
		    
		    
		    @RequestMapping(value = "/contact", method = RequestMethod.GET)
		    public String contact(Model model) {
		    	model.addAttribute("sendEmail", new UserLogin());
		    	model.addAttribute("message", "Welcome to Gettin' Hitched, please drop us an email"); 
		    	
		        return "contact";
		    }
		    
	    @RequestMapping("messageArtist")
		    public String contactartist(@ModelAttribute("messageArtist") Hitched hitched, BindingResult bindingResult, Model model) { 
		    			  
			    			  String validemails = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
			    	          String message="Name: " +hitched.getName()+ "\nEmail: " +hitched.getSubject()+"\n\nMessage: " + hitched.getMsg();
			    	          
			    	          
			   	                try { 
					            	   UserLogin userdetail= hitchedService.getUserById(hitched.getId());
					            	   
					            	   if(!hitched.getSubject().matches(validemails)) {
						    	        	  model.addAttribute("attribs", userdetail);
						                	  model.addAttribute("error", "Invalid email "+ hitched.getSubject()+ " please use format johnsmith@scglobal.com ");		                  
						   	           }else{
					            	   sendMail.sendMails(hitched.getEmailAddress() , hitched.getSubject(), message.toString());					          
					                   model.addAttribute("success", "Thanks you "+hitched.getName()+" an email has been sent to "+ userdetail.getFname());
					                   model.addAttribute("attribs", userdetail);
						   	           }
							        }catch(Exception e) {
							                 model.addAttribute("sendmailfail", "Error sending email  " );
							                 e.printStackTrace();
							        }
			   	                
						
						return "bio";
				    }
		    
		    
		    @RequestMapping(value = "/sendEmail", method = RequestMethod.POST)
		    public String sendMail(@ModelAttribute("sendEmail") Hitched hitched, BindingResult bindingResult, Model model) { 
		    	          
		    	          String validemails = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"; 
		    	          model.addAttribute("em", hitched.getEmailAddress());
		       	          model.addAttribute("sb", hitched.getSubject());              
		                  String message="Subject: " +hitched.getSubject()+"\nMessage: " + hitched.getMsg();
		          
		                  if(!hitched.getEmailAddress().matches(validemails)) {
		                	  model.addAttribute("emailrror", "Invalid email "+ hitched.getEmailAddress()+ " use format johnsmith@scglobal.com ");		                  
		   	               }
		                  
		                  if(hitched.getSubject().isEmpty()) {
		                	  model.addAttribute("sbjrror", "Please enter Emain Subject");		                    
		   		           }  
		                  
		                  if(hitched.getMsg().length() <= 1 || hitched.getMsg().length() > 3000) {
		                	  model.addAttribute("msgerror", "Must be between 10 and 300 characters");		                   
		   		           }
		                  
		                  else
				       
				             try {     
				            	   sendMail.sendMails(hitched.getEmailAddress() , hitched.getSubject(), message.toString());					          
				                   model.addAttribute("success", "Thanks Email has been sent to "+ hitched.getEmailAddress());
				                   model.addAttribute("emailrror", "");  
				                   model.addAttribute("sbjrror", "");		                   
				       	           model.addAttribute("msgerror", "");
				       	           model.addAttribute("em", "");
				       	           model.addAttribute("sb", "");   
				       	           
						        }catch(Exception e) {
						                 model.addAttribute("sendmailfail", "Error sending email  " );
						                 e.printStackTrace();
						        }
						
						return "contact";
				    }
		    
	
		    
		    @SuppressWarnings({ "unchecked", "rawtypes" })			
			public UserLogin getUserByNane(String name){
				String sql = "SELECT * FROM hitched.users WHERE lname = ?";
		     JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		     UserLogin userLogin = (UserLogin) jdbcTemplate.queryForObject(
		     sql, new Object[] { name }, new BeanPropertyRowMapper(UserLogin.class));
		     return userLogin;
			}
		    
		    public void writeBio(UserLogin userLogin, String bio, long id) { 
		    	 
		    	 String UpdateSql = "UPDATE users SET background=? where id=? ";    
		    	      JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource); 
		    	     jdbcTemplate.update( 
		    	   UpdateSql, 
		    	   new Object[] {   
		    	   userLogin.getBackground(), 
		    	   userLogin.getId() 
		    	   
		    	   });   
		    	       }
		    
			@RequestMapping("writeBio")
			public String changeMembershipType(@ModelAttribute("writeBio") UserLogin userLogin, ModelMap model)
					throws ParseException {
				try {
					writeBio(userLogin, userLogin.getBackground(), userLogin.getId());
					model.addAttribute("success",
							"Hi " + hitchedService.getUserById(userLogin.getId()).getFname() + " Thanks, Biography saved");
					model.addAttribute("attribs", hitchedService.getUserById(userLogin.getId()));
					} catch (RuntimeException e) {
					model.addAttribute("attribs", hitchedService.getUserById(userLogin.getId()));
					model.addAttribute("error", "Unexpected error occured");
					e.printStackTrace();
					}
				return "profile";
			}
			
			public List<UserLogin> lookupMembers(String name) {
				JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);        	
		       	final String sql  = "SELECT m.* FROM hitched.users m WHERE m.lname like '%"+ name +"%'"
		       						+ " UNION "
		       						+ "SELECT m.* FROM hitched.users m WHERE m.fname like '%"+ name +"%'";
		       	@SuppressWarnings("unchecked")
		   		List<UserLogin> memberList = jdbcTemplate.query(sql, new BeanPropertyRowMapper( UserLogin.class ));						
		   		return memberList;
		   		 
			}
			
			@ModelAttribute("states")
			public List<States> populateStates() {
				return Arrays.asList(States.values());
			}

			      
		
}
