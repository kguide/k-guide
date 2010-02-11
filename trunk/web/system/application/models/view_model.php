<?php 
class View_model extends Model{

var $headerInfo;
var $userMenu;

	function __construct(){
		parent::Model();
		$this->load->model('User_model');
	}
	
	
	private function isLoggedIn(){
		return $this->User_model->loggedIn();	
	}
	
	private function getDefaultHeaderArray(){	
		if($this->isLoggedIn()) {
			$this->headerInfo['loginText'] = "logout";
			$this->headerInfo['loginController'] = "authentication/logout/";				

		}
		else {
			$this->headerInfo['loginText'] = "login";
			$this->headerInfo['loginController'] = "authentication/login/";			
		}	
	
	return $this->headerInfo;
	}


	function setHeaderArray($arr){
		$this->headerInfo = $arr;
	}
	
	function getHeaderArray(){
		return $this->headerInfo;
	}
	
	private function getUserMenuArray(){
		$this->userMenu['myPage/'] = 'My page';
		$this->userMenu['route/createRoute/'] = 'Create a route';
		return $this->userMenu;
	}
	
	private function getMainMenuArray(){
		$this->mainMenu['/'] = 'Home';
		$this->mainMenu['start/about/'] = 'About';		
		return $this->mainMenu;	
	}
		
	function getDefaults(&$module){
		if($this->User_model->loggedIn())
			$module['usermenu'] = $this->getUserMenuArray();		
		$module['mainmenu'] = $this->getMainMenuArray();
		$module['header'] = $this->getDefaultHeaderArray();			
	}
	
	
}
?>