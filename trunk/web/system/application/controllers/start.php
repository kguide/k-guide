<?php 
class Start extends Controller {

	function __construct(){
		parent::Controller();
	}

	function index(){ 
		$module['content']['frontpage'] = 'frontpage_view';
		$this->View_model->getDefaults($module);
		$this->load->view('index',$module);	
	}
		
	function about(){
		$module['content']['frontpage'] = 'about_view';
		$this->View_model->getDefaults($module);
		$this->load->view('index',$module);	
	}
}
?>