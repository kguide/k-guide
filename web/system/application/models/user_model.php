<?php 
class User_model extends Model{

var $username;
var $password;
var $last_login;
var $register_date;

function __construct(){
	parent::Model();
	$this->load->helper('date');
	$this->load->database();
}


/**
* createUser(): Takes username & password as http post input
* @after: User has been created in the user database table and has been initialized in the userScore table
**/
function createUser(){
	$this->username = $this->input->post('username');
	$this->password = md5($this->input->post('password'));
	$this->last_login = mdate("%Y-%m-%d");	
	$this->register_date = $this->last_login;
	
	
	$this->db->insert('users',$this);
	// Get the userId provided for the new user, result will always be one record due to constraints
	$query = $this->db->select('user_id')->where('username',$this->username)->where('password',$this->password)->get('users');
	$userId = $query->row(0)->user_id;
	return $userId;
}


/**
/ activates users account
**/
function activateUser($userId){
	$this->db->where('user_id' ,$userId)->update('users', array ( 'activated' => '1'));
}

function sendConfirmationMail($userId){
		$link = base_url()."index.php/authentication/activate/".$userId;
		$this->load->library('email');		
		$this->email->from('hgphoto@hgphoto.net', 'K-guide');
		$this->email->to('hordurg83@gmail.com');
		$this->email->subject('K-Guide: User activation');
		$this->email->message('Welcome to K-Guide, follow this link to activate your account '.$link);
		$this->email->send();
}

function loginSuccessfull(){
	$params['username'] = $this->input->post('username');
	$params['password'] = md5($this->input->post('password'));
	$this->db->select('user_id')->from('users')->where('username',$params['username'])->where('password',$params['password']);
	$query = $this->db->get();
	if($query->num_rows() == 1) {	
		$row=$query->row_array();
		$params['user_id']=$row['user_id'];
		$this->session->set_userdata('username', $params['username']);
		$this->session->set_userdata('user_id', $params['user_id']);
		return true;		
	}
	else 
		return false;	
	}
	
function logOut(){
		$this->session->sess_destroy();
		redirect('/','refresh');
}

function loggedIn(){
	if($this->session->userdata('username') == '')
		return false;
	else
		return true;
}

function getLoggedInUserId(){
	if($this->loggedIn())
		return $this->session->userdata('user_id');
	else
		return -1;
	}
}
?>