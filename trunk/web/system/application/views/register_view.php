        <h3>Register new user</h3>
		<? echo form_open('authentication/validateRegistration'); ?>
			<table>
				<tr>
						<td><label>Email:</label></td>
						<td><input type="text" name="username" id="usernameField" size="30"></input></td>
				</tr><tr>
				<tr>
						<td><label>Repeat Email:</label></td>
						<td><input type="text" name="username2" id="usernameField" size="30"></input></td>
				</tr><tr>

						<td><label>Password:</label></td>
						<td><input type="password" name="password" id="passwordField" size="30"></input></td>
				</tr><tr>
						<td><label>Repeat password:</label></td>
						<td><input type="password" name="password2" id="passwordField" size="30"></input></td>
				</tr><tr>
						<td></td>
						<td><input type="submit" value="Submit"></input></td>
				</tr>
			</table>
		<? echo form_close('');?>
			<p class="notify">
			Once registered you will receive an email confirmation with a link to activate your account.
			</p>