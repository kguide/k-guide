<?php 
class PlayedGames extends Controller{

	function __construct(){
		parent::Controller();
	}
	
	function index(){
		$this->load->model('Game_model');		
		$module['content']['playedGames'] = 'playedGames_view';
		$result = $this->Game_model->getPlayedGamesResult($this->User_model->getLoggedInUserId());
		$module['playedGames']['queryResult'] = $result;

		$module['mainmenu'] = $this->View_model->getMainMenuArray();
		$this->View_model->getDefaults($module);
		$this->load->view('index',$module);		
	}
}
?>