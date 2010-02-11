<?php
class Mypage_model extends Model {

var $userId;

	function __construct() {
		parent::Model();	
		$parameters['username'] = $this->getUser();			
		$query = $this->db->query('SELECT userId FROM users WHERE username="'.$this->getUser().'"');
		$row = $query->row_array();
		$this->userId = $row['userId'];
	}

	function getUser() {
		return $userName = $this->session->userdata('username');
	}
		
	function getScore() {
		$query = $this->db->query('SELECT score FROM userScores where userId='.$this->userId);
		$row = $query->row_array();
		$score = $row['score'];
		return $score;
	}
	
	function getNumberOfLocations() {
		$query = $this->db->query('SELECT locations FROM userScores where userId='.$this->userId);
		$row = $query->row_array();
		$locations = $row['locations'];
		return $locations;
	}

	function getScoreAbove() {

		$query = $this->db->query('SELECT score FROM userScores where score > '.$this->getScore().' ORDER by score DESC LIMIT 1');	
		if($query->num_rows() > 0) {
			$row = $query->row_array();
			$scoreAbove = $row['score'];
			return $scoreAbove;		
		}
		else 
			return 0;

	}

}
?>