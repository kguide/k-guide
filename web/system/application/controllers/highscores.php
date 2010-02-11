<?php
class Highscores extends Controller {

	function __construct(){
		parent::Controller();	
	}
	
	function index(){
		$this->load->model('Game_model');
		$module['content']['highscores'] = 'highscores_view';
		$module['highscores']['result'] = $this->Game_model->getHighscoresResult(20);
		$this->View_model->getDefaults($module);
		$this->load->view('index',$module);						
	}
}
?>