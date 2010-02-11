<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<base href="http://hgphoto.net/kguide/" />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Treasure Hunter</title>

<link href="css/default.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div id="mainWrapper">
	<div id="header">
		<div id="logo">
			<?php echo anchor('/','<img src="images/logo.png" border="0">') ?>
		</div>
		<div id="menuBar">		
				<div id="loginButton"><?php echo anchor($header['loginController'],$header['loginText']);?></div>
		</div>
	</div>

	<div id="contentWrapper">
		<div id="sideBarWrapper">
			<div id="leftSidebar">
				<p>
				<? if(isset($mainmenu)){
					echo "<p class='menuTitle'>Mainmenu:</p>";
					echo "<ul class='menuList'>";
					while($item = current($mainmenu)) {
						echo "<li class='mainmenu'>".anchor(key($mainmenu),$item)."</li>";
						next($mainmenu);
						}
					echo "</ul>";
					}
				?>
				</p>

				<p>				
				<? if(isset($usermenu)) {
					echo "<p class='menuTitle'>Usermenu:</p>";
					echo "<ul class='menuList'>";
					while($item = current($usermenu)){
						echo "<li class='usermenu'>".anchor(key($usermenu),$item)."</li>";
						next($usermenu);
						}
					echo "</ul>";
					}
				?>
				</p>
			</div>
		</div>
			<div id="content">
				<? 	
				/*				if(isset($content)) 
										while($mod = current($content)) {
											if(isset($message[key($content)])
												$this->load->view($mod,$message[key($content)]);
											else
												$this->load->view($mod);
											}	
				*/
				if(isset($content)) 	
					foreach($content as $mod) {
							if(isset($message[key($content)])) //display message for module $mod if it is set
								//message loaded into coresponding view which can access it by it's key	name						
								$this->load->view($mod,$message[key($content)]);
							else
								$this->load->view($mod);
							}
				?></div>
	</div>
			
	<div id="footerWrapper">
		<div id="footerText">k-guide | <? echo anchor('/','www.ourGreatDomain.com');?></div>
	</div>	
</div>

<? //echo "<div style='color:#fff'>Debug: ".$this->User_model->getLoggedInUserId()."</div>"?>
</body>
</html>
