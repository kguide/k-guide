<?php
class MyGames extends Controller {

	function __construct(){
		parent::Controller();	
		$this->load->model('Game_model');
	}
	
	function index(){
		if($this->User_model->loggedIn()) {
			$module['content']['view'] = 'myGames_view';
			$module['myGames']['games'] = $this->Game_model->getListOfGames($this->User_model->getLoggedInUserId()); //pass games array to view
			
			$this->View_model->getDefaults($module);
			$this->load->view('index',$module);	
		}
		else
			redirect('/','refresh');		
	}
}
?>