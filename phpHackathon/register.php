<?php
include_once("config.php");
// Dette skriptet registrerer en ny bruker
// Først sjekker det om brukernavnet finnes, dersom
// det ikke finnes utfører den en insert på bruker

// Profilbilde blir også lastet opp, og URL
// til bildet blir lagret i Brukerdatabasen
$user = $_POST["user"];
$password = $_POST["password"];
$hash = password_hash($password, PASSWORD_DEFAULT);
$imagepath = $_POST["name"]; 
$image = $_POST["image"];

$response = array();
$webPath = "https://itfag.usn.no/~161741/hackathon2019/uploadedFiles/";
$fullPath = $webPath . $imagepath;

$decodedImage = base64_decode("$image");

$imageDir = "uploadedFiles/";
$return = file_put_contents("uploadedFiles/".$imagepath.".JPG", $decodedImage);
if($return !== false){
	$response['message'] .= "Image Uploaded Successfully";
}else{
	$response['message'] .= "Image Uploaded Failed";
}
$selectStmt = mysqli_prepare($conn, "SELECT * FROM Bruker WHERE brukernavn = ?");
mysqli_stmt_bind_param($selectStmt, "s", $user);
mysqli_stmt_execute($selectStmt);

$response["error"] = $user;
$response["success"] = false;
if (mysqli_stmt_fetch($selectStmt)){
	$response["error"] = "Username already exists";
	echo json_encode($response);
	exit;
}
$insertStmt = mysqli_prepare($conn, "INSERT INTO Bruker (brukernavn, passord, profilbilde) VALUES(?, ?, ?);");
mysqli_stmt_bind_param($insertStmt, "sss", $user, $hash, $fullPath);

if(mysqli_stmt_execute($insertStmt)){
	$response["success"] = true;
} else {
	$response["success"] = false;
	exit;
}
$response["error"] = "Bruker opprettet";
echo json_encode($response);

?>