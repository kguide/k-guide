$(document).ready(function(){
	$("#blabla").click(function(event){
		event.preventDefault();
		usernameText = $("#usernameField").val();		
			$.get("http://hgphoto.net/treasure/controller.php", { username: usernameText , method: "usernameExists" },function(data){
				if(data == "false"){
					$("#errorMsg").html("This username is yours for the taking !");
				}
				else{
					$("#errorMsg").html("Selected username is already taken");  
				}
			});
 		});
});
