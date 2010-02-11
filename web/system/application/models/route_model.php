<?php
class Route_model extends model {

	function __construct(){
		parent::Model();
		$this->load->helper('date');
		$this->load->database();
		$this->load->library('firephp');
	}

	/**
	* Gets the post data, a json string containing the information
	* about a created route.
	**/
	function addRoute(){
		$this->ownerId = $this->User_model->getLoggedInUserId();
		$jsonString = $this->input->post('jsonData');	
		$routeInfo= json_decode($jsonString);					
		$routeName = $routeInfo->{'routeName'};
		// arrays of equal lenghts
		$routeType = $routeInfo->{'routeType'};
		$textMedia = $routeInfo->{'routeText'};
		$photoMedia = $routeInfo->{'routePhotoUrl'};
		$audioMedia = $routeInfo->{'routeAudioUrl'};
		$urlMedia = $routeInfo->{'routeWebUrl'};
		$latitude = $routeInfo->{'lat'};
		$longitude = $routeInfo->{'lng'};		
		$created = mdate("%Y-%m-%d");
		$numberOfCoordinates = count($longitude);
		$route = Array(
			'type_id' => $routeType,
			'name' => $routeName,
			'created' => $created);
			
		
		/* Start of database transaction */		
		$this->db->trans_start();
		$this->db->insert('routes',$route);
		$routeId = $this->db->insert_id();
		$this->db->insert('complete_routes_json',Array('routeId' => $routeId, 'json' => $jsonString));
		
		//all media arrays, latitude and longitude should be of equal length, 		
		//if(!(count($textmedia)==count($photoMedia)==count($audioMedia)==count($latitude)==count($longitude))){
		//}	
		$coordinateId = 0;
		
		for($i = 0; $i < count($latitude);$i++){
			$route_coordinate = Array(
				'routeId' => $routeId,
				'latitude' => $latitude[$i],
				'longitude' => $longitude[$i]);				
			$this->db->insert('route_coordinates',$route_coordinate);
			$coordinateId = $this->db->insert_id();
			if($audioMedia[$i] != ''){
				$this->db->insert('media_audio',Array('coordinate_id' => $coordinateId, 'url' => $audioMedia[$i]));
			}
			
			if($photoMedia[$i] != ''){
				$this->db->insert('media_photos',Array('coordinate_id' => $coordinateId, 'url' => $photoMedia[$i]));
			}
			
			if($textMedia[$i] != ''){
				$this->db->insert('media_texts',Array('coordinate_id' => $coordinateId, 'information' => $textMedia[$i]));
			}

			if($urlMedia[$i] != ''){
				$this->db->insert('media_links',Array('coordinate_id' => $coordinateId, 'url' => $urlMedia[$i]));
			}
			
		}			
		$this->db->trans_complete();
		/* End of database transaction */
	}

	/**
	* Used to get info on all games created by userId.
	* Returns Active Record result set of all games user has created.
	* 
	* @param:
	*	$userId : Userid to look up games for
	*
	* @return: 
	*	$array :  Active Record result set with gamenames ordered by gameId descending
	*
	**/
	function getListOfGames($userId){
		$this->db->select('gameId, gameName,active')->from('games')->where('ownerId',$userId)->order_by('gameId', "desc"); ;
		$query = $this->db->get();
		if($query->num_rows() > 0) {
			$i=0;
			foreach($query->result() as $row) {
				$result[$i++] = $row;
			}		
			return $result;
		}
		return -1;
	}

	/**
	* Used to get info about a given games user has been member of.
	* Returns names of all games user has created as a SQL result
	* 
	* @param:
	*	$userId : Userid to look up games for
	*
	* @return: 
	*	$array :  SQL result object with gamens ordered by date joined
	**/
	function getPlayedGamesResult($userId){
		$query = $this->db->query("SELECT A.gameId,B.gameName, B.active, A.finished FROM gameMembers A, games B WHERE A.userId =".$userId." AND A.gameId = B.gameId");
		if($query->num_rows() > 0) {
			$i=0;
			foreach($query->result_array() as $row) {
				$result[$i++] = $row;
			}		
			return $result;
		}
		else
			return -1;
	}
	
	/**
	* Used to get json information about a specific route
	* @param:
	*	$routeId : The route to be fetched
	*
	* @return : Json string for the route
	*
	*
	**/
	function getRouteJsonString($routeId){
		$this->db->select('routeId')->select('json')->from('complete_routes_json')->where(Array('routeId' => $routeId));
		$query = $this->db->get();
		$routeJson = '';
		foreach($query->result_array() as $row){			
			$routeJson = $row['json'];
		}
		return $routeJson;
	}

	/**
	* Used to get info on highscores for given number of users.
	* 
	* @param:
	*	$userId : Userid to look up games for
	*
	* @return: 
	*	$array :  SQL result set with gamenames ordered by gameId descending
	**/
	function getHighscoresResult($n){
		$query = $this->db->query("SELECT A.username,B.score ".
							"FROM users A, userScores B ".
							"WHERE B.userId = A.userId ".
							"ORDER BY score DESC LIMIT 0,".$n);

		if($query->num_rows() > 0) {
			$i=0;
			foreach($query->result_array() as $row) {
				$result[$i++] = $row;
			}		
			return $result;
		}
		else
			return -1;
	}
}
?>