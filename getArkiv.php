<?php
include_once("config.php");

// Dette skriptet henter alle notatene til bruker

$brukerid = (int) $_POST["brukerid"];

$statement = mysqli_prepare($conn, "SELECT tittel, tekst, mood, dato FROM Notater WHERE brukerid = ? Order By dato DESC");
mysqli_stmt_bind_param($statement, "s", $brukerid);

if (mysqli_stmt_execute($statement)){
    $response["success"] = true;
    mysqli_stmt_bind_result($statement, $tittel, $tekst, $mood, $dato);

	$response["array"] = array();
	$num = 1;
	while(mysqli_stmt_fetch($statement)){
		$response["array"][$num] = array($tittel, $tekst, $mood, $dato);
		$num = $num + 1;
	}
} else {
    $response["success"] = false;
}
echo json_encode($response);
?>