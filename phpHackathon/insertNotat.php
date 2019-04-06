<?php
include_once("config.php");

// Utfører en insert til Notaterdatabasen.
// Returnerer true dersom alt er vellykket.

$brukerid = (int) $_POST["brukerid"];
$tittel = $_POST["tittel"];
$tekst = $_POST["tekst"];
$mood = (int) $_POST["mood"]; 

$response = array();

$selectStmt = mysqli_prepare($conn, "INSERT INTO Notater(brukerid, tittel, tekst, mood) VALUES(?, ?, ?, ?)");
mysqli_stmt_bind_param($selectStmt, "issi", $brukerid, $tittel, $tekst, $mood);
if (mysqli_stmt_execute($selectStmt)){
	$response["success"] = true;
} else {
	$response["success"] = true;
	exit;
}

echo json_encode($response);

?>