<?php 
class Route extends Controller {

	function __construct(){
		parent::Controller();
		// All functions in this class require authentication
		//if(!$this->User_model->loggedIn())		
		//	redirect('/','refresh');		
		$this->load->model('Route_model');
		$this->load->library('firephp');	
	}	
		
	function index(){
		redirect('/','refresh');
	}
		
	/**
	* Calls the create route view
	*/	
	function createRoute(){
		$module['content']['view'] = 'createRoute_view';
		$this->View_model->getDefaults($module);
		$this->load->view('index',$module);	
	}
		
	/**
	* Calls the route model and asks for a array of all routes
	*/
	function routeList(){
		$module['content']['view'] = 'routeList_view';
		$module['routeList']['routes'] = $this->Route_model->getListOfRoutes($this->User_model->getLoggedInUserId());
		$this->View_model->getDefaults($module);
		$this->load->view('index',$module);	
	
	}
	
	/**
	* Calls the route model and asks it to put route into database 
	**/
	function addRoute(){
		if($this->Route_model->addRoute()) {
			$module['content']['view'] = 'routeGame_view';
			$this->View_model->getDefaults($module);
			$this->load->view('index',$module);	
			}
		else 
			redirect('/','refresh');
	}
	
	/**
	*	Gets the route with the id provided as a parameter
	**/
	function getRouteJson($routeId){
		$json = $this->Route_model->getRouteJsonString($routeId);
		$this->firephp->log($json);		
		echo $json;		
	}
	
	/**
	*
	*
	**/
	function routeExists($rotueId){
		return true;
	}
	
	/**
	*
	*
	**/
	function getRouteListJson($limit=0,$offset=0){
		return true;
	}
	
	
	
}
?>