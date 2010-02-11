<?php
class DefaultModules {

	function getDefault(&modules){
		if($this->User_model->loggedIn())
			$module['usermenu'] = $this->View_model->getUserMenuArray();		
		$module['mainmenu'] = $this->View_model->getMainMenuArray();
		$module['header'] = $this->View_model->getDefaultHeaderArray();	
	}
}
?>