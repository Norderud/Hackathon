<?php	
	include_once("config.php");

	// Dette skriptet utfører en sjekk i databasen
	// på om brukernavn og passord finnes/stemmer.
	// Returnerer true dersom alt er godkjent

	$user = $_POST["user"];
	$password = $_POST["password"];

	$response["success"] = true;
	$response["error"] = "";

	$statement = mysqli_prepare($conn, "SELECT brukerid, passord FROM Bruker WHERE brukernavn = ? LIMIT 1");
	mysqli_stmt_bind_param($statement, "s", $user);
	mysqli_stmt_execute($statement);
	mysqli_stmt_bind_result($statement, $userID, $hash);

	if(!mysqli_stmt_fetch($statement)){
		$response["error"] = "Bruker finnes ikke";
		$response["success"] = false;
		echo json_encode($response);
		exit;
	}
	if(!password_verify($password, $hash)){
		$response["error"] = "Feil passord";
		$response["success"] = false;
	}
	$response["userID"] = $userID;
	echo json_encode($response);
?>