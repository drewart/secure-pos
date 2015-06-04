<?php
	// returning json data type
	header('Content-Type: application/json');
	$data['code']=0;
	$public_key = null;
	/*
	foreach (getallheaders() as $name => $value) {
	  echo "$name: $value\n";
	  if (strtolower($name) == "public-key") {
	    $data['public-key'] = "got public key";
	    $public_key = $value;
	  }
        }
        */
	
	
	if ($_REQUEST['email'] != "") {
		include 'settings.inc';
		
		// check for post
	
		$db_con = mysql_connect($db_host,$db_username,$db_password) or die("connection failed");
		mysql_select_db($db_name,$db_con) or die("db selection failed");
		 
		
		$name=$_REQUEST['name'];
		$email=$_REQUEST['email'];
		$phone=$_REQUEST['phone'];
		$bank=$_REQUEST['bank'];
		$pin=$_REQUEST['pin'];
		$publicKey =  $public_key;
		
	
		
	
		$sql_insert="INSERT INTO `Customer`(`Email`, `Name`,`Phone`, `Bank`, `SecPin`, `PublicKey`,`Created`) VALUES ('$email','$name','$phone','$bank','$pin','$publicKey',CURTIME())";
		
		if($r=mysql_query($sql_insert,$db_con))
		{
			$data['code']=1;
			$data['id']=mysql_insert_id();
			// insert token  
			$sql_token_insert = "";
			
			$data['message']="registered";
			// $token = uniqid()
			// email token link
		}
		else 
		{
			$data['message']="Email already used";
			//mysql_query("insert"
		}
	
		
		mysql_close($db_con);
	}
	print(json_encode($data));
?>
