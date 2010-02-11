<h3>Enter your login credentials</h3>
<?php //echo validationErrors(); ?>
<? echo form_open('authentication/validateLogin/');?>		
	<table>
		<tr>
				<td><label>Email:</label></td>
				<td><input type="text" name="username" id="usernameField" size="30"></input></td>
		</tr><tr>
				<td><label>Password:</label></td>
				<td><input type="password" name="password" id="passwordField" size="30"></input></td>
		</tr><tr>
				<td> <? echo anchor('authentication/register','not a user?');?> <!-- <button id="blabla">Check Availabilty</button>--></td>
				<td><input type="submit" value="Submit"></input></td>
		</tr>
	</table>
<? echo form_close('');?>
<? if(isset($login)) echo "<p class='error'>".$login['error']."<p>"; ?>	