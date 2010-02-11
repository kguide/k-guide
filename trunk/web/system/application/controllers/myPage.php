<?php
class MyPage extends controller {

	function __construct() {
		parent::controller();	
	}


	function index() {
		$this->load->model('Mypage_model');		
		$module['content']['myPage'] = 'myPage_view';
		$module['myPage']['username'] = $this->Mypage_model->getUser();
		$module['myPage']['score'] = $this->Mypage_model->getScore();		
		$module['myPage']['scoreAbove'] = $this->Mypage_model->getScoreAbove();		
		$module['myPage']['numberOfLocations'] = $this->Mypage_model->getNumberOfLocations();
		$this->View_model->getDefaults($module);
		$this->load->view('index',$module);		
	}
}
?>